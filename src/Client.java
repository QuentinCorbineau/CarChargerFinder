
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

import javax.swing.JOptionPane;
/*
 * while(true){
			try {
				if (in.readInt()==Constants.GET_LIST){
					System.out.println("maj");
					for(Borne bor : borneList)
					{
						bor.setNbreAvailable(in.readInt());
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
 */

public class Client{
	
	DataOutputStream out;
	DataInputStream in;
	List<Borne> borneList;
	int listLength;
	GUI gui;
	Socket sock;
	ClientListener cliListen;
	
	private static final String HOST = "localhost";
	
	Client () {
		borneList= new ArrayList<Borne>();
		try {
			sock = new Socket(HOST, Server.DEFAULT_PORT);
			in = new DataInputStream(sock.getInputStream());
			out = new DataOutputStream(sock.getOutputStream());
			
			/*Thread updateMap = new Thread();
			updateMap.start();*/
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	      
		getListBornes();
		//Lets print the Borne List
        for(Borne bor : borneList)
        {
            System.out.println(bor.getX()+"   "+bor.getY());
        }
		gui = new GUI(borneList, this) ;
		//cliListen = new ClientListener(sock, this);

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Client() ;
		
		
	}
	
	public List<Borne> getBorneList(){
		return borneList;
	};
	
	public void repaint(){
		if (gui!=null){
			gui.rePaint();
		}
	}
	
	/**
	 * 
	 * @param msg
	 */
	void getListBornes() {
		String line=new String();
		String[] borneDetails ;
		try {
			//getting the length of the list of bornes
			listLength = in.readInt() ;			
	        for(int i=0;i<listLength;i++)
	        {
	        	line = in.readUTF() ;
	        	borneDetails= line.split(";");
	        	borneList.add(new Borne(borneDetails[1],borneDetails[3], Integer.parseInt(borneDetails[5]),Integer.parseInt(borneDetails[7])));
	        }
	        gui.rePaint();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.out.println(HOST+" unknown");
		} catch (IOException e) {
			e.printStackTrace();
		}
		catch (NullPointerException e) {
		e.printStackTrace();
	}
	}
	
	void sendListBornes() {
		try {
			//sending the command
			out.writeInt(Constants.SEND_LIST);
			//sending the list of bornes			
	        for(Borne bor : borneList)
	        {
	        	out.writeInt(bor.getNbreAvailable());
	        }
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.out.println(HOST+" unknown");
		} catch (IOException e) {
			e.printStackTrace();
		}
		catch (NullPointerException e) {
		e.printStackTrace();
	}
	}
	
	public String connectLogin(String login, String pwd){
		try {
			out.writeInt(Constants.CONNECT);
			out.writeUTF(login);
			int hashpsw = 1;
			hashpsw = pwd.hashCode();
			String Shashpsw = Integer.toString(hashpsw);
			out.writeUTF(Shashpsw);
			String resConnex= "";
			resConnex=in.readUTF();
			
			System.out.println(resConnex);
			return resConnex;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Erreur";
		}
	}
	
	public String createLogin(String login, String pwd){
		try {
			
			String res;
        	
        	if (! login.matches("^[a-zA-Z0-9\\.\\-\\_]+@([a-zA-Z0-9\\-\\_\\.]+\\.)+([a-zA-Z]{2,4})$"))
        	{
        		res="Ceci n'est pas une adresse email";
        	}
        	else if (pwd.length()<6){
        		res="Le mot de passe doit être composé d'au moins 6 caractères";
        	}
        	else
        	{
        		out.writeInt(Constants.CREATE_LOGIN);
        		out.writeUTF(login);
        		int hashpass = 1;
				hashpass = pwd.hashCode();
        		out.writeInt(hashpass);
        		res=in.readUTF();										
			} 
        	return res;
		}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "Erreur";
		}
	}

	public void disconnect() {
		try {
			out.writeInt(Constants.DISCONNECT);
			in.close();
			out.close();
			//cliListen.deconnect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}

