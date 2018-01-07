package Logic;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import Utility.JavaRecorder;



public class AudioClient implements Runnable{
	
	Socket audoSocket = null;
	DataOutputStream dos = null;
	DataInputStream dis = null;
	public JavaRecorder jRecorder;
	String IP="127.0.0.1";
	int port = 8888;
	
	public AudioClient(){
		try {
			audoSocket = new Socket(IP,port);
			dos = new DataOutputStream(audoSocket.getOutputStream());
			dis = new DataInputStream(audoSocket.getInputStream());
			jRecorder = new JavaRecorder(dos);
			new Thread(this).start();
		} catch (Exception e){
			System.out.println("Can not connect to the server");
			System.exit(0);
		}
	}
	
	
	public void disConnect()  {
		try {
			dos.close();
			dis.close();
			audoSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendMsg(byte buf[]){
		try {
			dos.writeInt(buf.length);
			dos.write(buf);
			dos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while(true){
				int len = dis.readInt();
				for(int i = 0 ; i < len ; ++i){
					baos.write(dis.readByte());
				}
				if(len == -1)	continue;
				jRecorder.play(baos.toByteArray(),len);
				baos.reset();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
