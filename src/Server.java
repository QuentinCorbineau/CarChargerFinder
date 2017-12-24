import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;


final public class Server implements Runnable{
	
	private static Server server ; 
	private DataOutputStream out;
	private DataInputStream in;
	final static int DEFAULT_PORT = 2000 ;
	
	private Socket lastSocket;
	private List<Borne> borneList;
	private boolean changingBorneList=false;

	/**
	 * le constructeur lance le serveur.
	 */
	private Server() {
		//Create List for holding Borne objects
        borneList = new ArrayList<Borne>();
        ReadCSVFile_BufferedReader.putDataInList(borneList);
        
        startServer() ;
	}
	
	public static Server getserver(){
		if(server == null)
			server = new Server();
		return server;
	}
	
	/**
	 * 
	 */
	@SuppressWarnings("resource")
	public void startServer() {
		System.out.println("Start Server");
		try {
			//thread to update openData every 99999,999s (1day and a bit)
			Thread thUpdateData = new Thread(new Runnable() {           
	            public synchronized void run() { 
	                while(true){
	                	try {
							Thread.sleep(99999999);
							ReadCSVFile_BufferedReader.putDataInList(borneList);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	                	
	                }
	            } 
	        });
	        thUpdateData.start();
	        
	        //preparing connection to clients
			ServerSocket ss = new ServerSocket(DEFAULT_PORT) ;
			
			 //Lets print the Borne List
            for(Borne bor : borneList)
            {
                System.out.println(bor.getX()+"   "+bor.getY());
            }
			
			while(true) {
				lastSocket = ss.accept() ;
				System.out.println("Client connected");
				
				//creating new thread for this client
				Thread th = new Thread(this);
				th.start();
				
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * écoute des commandes du client
	 */
	public void run(){
		
		
		try {
			
			out = new DataOutputStream(lastSocket.getOutputStream()) ;
			in = new DataInputStream(lastSocket.getInputStream());
			//sending the size of the list of bornes to the client
			out.writeInt(borneList.size());
			
			for(Borne bor : borneList)
            {
                out.writeUTF(bor.toString());
            }
			boolean connected=true;
			
			while (connected){
				
				if (!changingBorneList) {
					int cmd = in.readInt();
					System.out.println("CMD : " + cmd);
					if (cmd == Constants.CONNECT) {
						System.out.println("Connexion");
						checkLogin(lastSocket);
					} else if (cmd == Constants.DISCONNECT) {
						System.out.println("Deconnexion");
						out.close();
						in.close();
						connected = false;
					} else if (cmd == Constants.CREATE_LOGIN) {
						System.out.println("Creation compte");
						createLogin(lastSocket);
					} else if (cmd == Constants.SEND_LIST) {
						retrieveList(in);
					} 
				}
				else
				{
					sendToAll();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public synchronized void retrieveList(DataInputStream DIS){
		System.out.println("récuperer la liste actualisée");
		try {
			for(Borne bor : borneList)
			{
				bor.setNbreAvailable(DIS.readInt());
			}
			changingBorneList=true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	
	private void sendToAll() {
		System.out.println("spreading la liste actualisée");
		try {
			out.writeInt(Constants.GET_LIST);
			for(Borne bor : borneList)
			{
				out.writeInt(bor.getNbreAvailable());
			}
			System.out.println("Sending new map");
			Thread.sleep(3000);
			changingBorneList=false;
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	public synchronized boolean checkLogin(Socket sock){
		boolean res=false;
		try {
			DataInputStream in = new DataInputStream(sock.getInputStream());
			
			String log = in.readUTF() ;
			String sHashpsw = in.readUTF() ;
			System.out.println("log"+log);
			System.out.println("psw"+sHashpsw);
			int hashpsw = Integer.parseInt(sHashpsw);
			out=new DataOutputStream(sock.getOutputStream());
			String resp=new String();
			
		
        		Class.forName("com.mysql.jdbc.Driver");
        	      System.out.println("Driver O.K.");

        	      String url = "jdbc:mysql://localhost:3306/CarChargerFinder";
        	      String user = "root";
        	      String passwd = "";

        	      Connection conn = DriverManager.getConnection(url, user, passwd);
        	      Statement state = conn.createStatement(); 
				
        	      String query = "SELECT password FROM users WHERE email = '"+log+"'";
        	      ResultSet result = state.executeQuery(query);
        	     
        	      if(result.next()){
                      
                      int motDePasse = result.getInt(1);
           
                  if(motDePasse == hashpsw){
               
                	  	resp="Connexion réussie!";
						res=true;
                  }else {
                       
						resp="Erreur d'authentification ! \n Veuillez réessayer";

						
                  }
                  }else {
                       
                	  resp="L'utilisateur n'existe pas !";                
                	  }
   
      			out.writeUTF(resp);System.out.println(resp);

                conn.close();
        	      
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        	
        	
		
		return res;
	}
	
	public synchronized boolean createLogin(Socket sock){
		boolean res=false;
		try {
				String resp;
        		DataInputStream in = new DataInputStream(sock.getInputStream());
        		String logger=in.readUTF();
        		int hashpwd=in.readInt();
        		
  	        	  Class.forName("com.mysql.jdbc.Driver");
	        	      System.out.println("Driver O.K.");

	        	      String url = "jdbc:mysql://localhost:3306/CarChargerFinder";
	        	      String user = "root";
	        	      String passwd = "";

	        	      Connection conn = DriverManager.getConnection(url, user, passwd);
	        	      Statement state = conn.createStatement(); 
					
  	        		
	        	    String query = "SELECT email FROM users WHERE email like '"+logger+"'";
	        	    ResultSet result = state.executeQuery(query);
        	     
	        	    if(result.next()){
	        	    	result.getString(1);
	        			resp="L'utilisateur existe déjà";

	        	    }
	        	    else{
	        	        String queryi = "INSERT INTO users VALUES ('"+logger+"','"+hashpwd+"')" ;         
	        	        state.executeUpdate(queryi);
	        	        resp="Creation OK";
						res=true;			   
	        	    }
	      	       

	        	   conn.close();
        		   out.writeUTF(resp);
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
		return res;
	}
	
		
}
