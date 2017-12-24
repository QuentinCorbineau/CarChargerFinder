import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientListener implements Runnable{
	private Socket socket;
	private Client client;
	private Thread th;
	private DataInputStream in;
	public ClientListener(Socket sock, Client client){
		this.socket=sock;
		this.client=client;
		th = new Thread(this);
		th.start();
	}
	public void run() {
		try {
			in = new DataInputStream(socket.getInputStream());
			
			while (true) {
				int CMD = in.readInt();
				if (CMD == Constants.GET_LIST)
					;
				for (Borne bor : client.getBorneList()) {
					bor.setNbreAvailable(in.readInt());
				} 
				client.repaint();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void deconnect(){
		try {
			in.close();
			th=null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
