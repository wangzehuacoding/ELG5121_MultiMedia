import java.io.IOException;
import java.net.*;
import java.util.*;
import com.google.gson.*;
public class ChatServer extends Thread{
	//程序占用端口号
	private static final int PORT = 10000;
	private static DatagramSocket server = null;
	private static HashMap<String,ArrayList<ChatClient>> groups = new HashMap<String,ArrayList<ChatClient>>();
	
	public ChatServer() {
		try {
			server = new DatagramSocket(PORT);
		}
		catch(SocketException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void logInGroup(String groupID, ChatClient client)
	{
		ArrayList<ChatClient> clients = groups.get(groupID);
		if(clients == null)
		{
			clients = new ArrayList<ChatClient>();
		}
		
		clients.add(client);
		groups.put(groupID, clients);
	}
	
	@Override
	public void run()
	{
		while(true)
		{
			receiveMessage();
		}
	}
	
	private void receiveMessage()
	{
		byte[] buf = new byte[1024];
		DatagramPacket packet = new DatagramPacket(buf,buf.length);
		while(true)
		{
			try {
				server.receive(packet);
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			
			String content = new String(packet.getData(),0,packet.getLength());
			
			Gson gson = new Gson();
			MessageEntity me = gson.fromJson(content, MessageEntity.class);
			
			ArrayList<ChatClient> clients = groups.get(me.getGroupId());
			
			for (ChatClient client : clients)
			{
				client.pushBackMessage(me);
			}	
		}	
	}
}
