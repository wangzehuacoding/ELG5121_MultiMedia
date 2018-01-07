package Logic;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import Utility.JavaRecorder;

public class AudioServer{
	
	ServerSocket audoServersocket;
	List<Client> clients = new ArrayList<Client>();
	JavaRecorder jrecorder;
	
	
	/*public static void main(String args[]) {
			new AudoServer().start();		
	}*/
	public void start(){
		try {
			 audoServersocket = new ServerSocket(8888);	
		}catch (Exception e) {
			System.out.println("The server is open");		
			System.exit(0);
		}
		try {			
			while(true){				
				Client audoClient = new Client(audoServersocket.accept());
				//System.out.println("1");
				audoClient.start();				
				clients.add(audoClient);
//				if(audoClient != null)
//				{
//					break;
//				}
			}
		} catch (IOException e) {
			e.printStackTrace();			
		} finally{
			try {
				if(audoServersocket != null) 	audoServersocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}	
	
	
	
	class Client extends Thread
	{		
		private Socket audoClientSocket;
		private DataInputStream dataInputStream;		
		private DataOutputStream dataOutputStream;	
		
		Client(Socket s){				
			this.audoClientSocket = s;			
			try {				
				dataInputStream = new DataInputStream(s.getInputStream());
				dataOutputStream = new DataOutputStream(s.getOutputStream());				
				jrecorder = new JavaRecorder(dataOutputStream);
			} catch (IOException e) {
				e.printStackTrace();				
				System.exit(0);
			}
		}
		public void sendMsg(byte buf[]){
			try{
				dataOutputStream.writeInt(buf.length);
				dataOutputStream.write(buf);
				dataOutputStream.flush();	
			}catch (Exception e){
				System.out.println("Write ERROR!");				
				try {
					if(dataInputStream != null) dataInputStream.close();
					if(dataOutputStream != null) dataOutputStream.close();
					if(audoClientSocket != null)   audoClientSocket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}				
				clients.remove(this);
			}
		}
		//may exist create too many clients problem 
		public void run(){
			//System.out.println("A new client connected!");
			//System.out.println("Create Thread :" + audoClientSocket);
			//System.out.println("The Client is too much!");
			try {				
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				while(true){		
					int len = dataInputStream.readInt();
					for(int i = 0 ; i < len ; ++i){
						baos.write(dataInputStream.readByte());
					}
					if(len == -1)	continue;									
					for(int i = 0 ; i < clients.size();++i){
						Client c = clients.get(i);	
						try{
							if(c != this)	c.sendMsg(baos.toByteArray());
						}catch (NullPointerException e) {
							clients.remove(c);
						}
					}
					baos.reset();
				}			
			}catch (Exception e) {
				try {
					//System.out.println(audoClientSocket + ":" + "Client Closed");	
					if(dataInputStream != null) dataInputStream.close();
					if(dataOutputStream != null) dataOutputStream.close();
					if(audoClientSocket != null)   audoClientSocket.close();
					clients.remove(this);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}					
		}
	}


}
