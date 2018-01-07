package Server;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.*;
import GraphicalInterface.UserClient;
import com.google.gson.*;

import Entity.MessageEntity;
import GraphicalInterface.UserClient;
public class TextChatServer extends Thread{
	private static final int SERVERPORT = 10000;
	public static DatagramSocket textChatServer= null;
	private static HashMap<String,ArrayList<UserClient>> groups = new HashMap<String,ArrayList<UserClient>>();
	
	public TextChatServer()
	{
		try
		{
			textChatServer = new DatagramSocket(SERVERPORT);
		}
		catch(SocketException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void logInGroup(String groupID, UserClient client)
	{
		ArrayList<UserClient> clients = groups.get(groupID);
		if(clients == null)
		{
			clients = new ArrayList<UserClient>();
		}
		clients.add(client);
		groups.put(groupID, clients);
	}
	
	@Override
	public  void run()
	{
		while(true)
		{
			receiveMessageFromClients();
		}
	}
	
	private void receiveMessageFromClients()
	{
		byte[] messageBuf = new byte[1024];
		DatagramPacket receivePacket = new DatagramPacket(messageBuf,messageBuf.length);
		while(true)
		{
			try
			{
				if(!textChatServer.isClosed())
				textChatServer.receive(receivePacket);
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			String messageContent = new String(receivePacket.getData(),0,receivePacket.getLength());
			
			Gson gson = new Gson();
			MessageEntity receivedMessageEntity = gson.fromJson(messageContent, MessageEntity.class);
			ArrayList<UserClient> clients = groups.get(receivedMessageEntity.getGroupId());
			
			for(int i =0;i<clients.size();++i)
			{
		    //System.out.println(clients.get(i).toString());
		    UserClient client_temp = clients.get(i);
			client_temp.pushBackMessageFromServer(receivedMessageEntity);
			}
		}
	}
}
