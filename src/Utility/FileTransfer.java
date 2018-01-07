package Utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class FileTransfer {
	
	public static void createAndSend(String hostName, int port, String filePathName) throws IOException {
		System.out.println(port);

		DatagramSocket fileSocket = new DatagramSocket();
		InetAddress address = InetAddress.getByName(hostName);
		File file = new File(filePathName);

		InputStream inFromFile = new FileInputStream(file);
		byte[] fileByteArray = new byte[(int) file.length()];
		inFromFile.read(fileByteArray);

		int sequenceNumber = 0;
		boolean lastMessageFlag = false;

		for (int i = 0; i < fileByteArray.length; i = i + 1021)
		{
			// Increment sequence number
			sequenceNumber += 1;
			// Create new message. Set first and second bytes of the message to sequence
			// number
			byte[] message = new byte[1024];
			message[0] = (byte) (sequenceNumber >> 8);
			message[1] = (byte) (sequenceNumber);
			
			// Set flag to 1 if packet is last packet and store it in third byte of header
			if ((i + 1021) >= fileByteArray.length)
			{
				lastMessageFlag = true;
				message[2] = (byte) (1);
				
				for (int j = 0; j < (fileByteArray.length - i); j++) 
				{
					message[j + 3] = fileByteArray[i + j];
				}
			} 
			else 
			{ // If not last packet, store flag as 0
				lastMessageFlag = false;
				message[2] = (byte) (0);
				
				for (int j = 0; j <= 1020; j++) 
				{
					message[j + 3] = fileByteArray[i + j];
				}
			}
				
			// Send the message
			DatagramPacket sendPacket = new DatagramPacket(message, message.length, address, port);
			fileSocket.send(sendPacket);
			System.out.println("Sent: Sequence number = " + sequenceNumber + ", Flag = " + lastMessageFlag);
			// Sleep for 40 milliseconds to avoid sending too quickly
			try {
				Thread.sleep(40);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		fileSocket.close();
		inFromFile.close();
		System.out.println("File " + filePathName + " has been sent");
	}

	
	
	public static void receiveAndCreate(int port, String filePathName) throws IOException {

		System.out.println("Ready to receive");
		DatagramSocket fileSocket = new DatagramSocket(port);
	
		File file = new File(filePathName);
		FileOutputStream outToFile = new FileOutputStream(file);
		
		
		boolean lastMessageFlag = false;		
		int sequenceNumber = 0;

		while (true) {

			byte[] message = new byte[1024];
			byte[] fileByteArray = new byte[1021];

			DatagramPacket receivedPacket = new DatagramPacket(message, message.length);
			fileSocket.receive(receivedPacket);
			message = receivedPacket.getData();

			sequenceNumber = ((message[0] & 0xff) << 8) + (message[1] & 0xff);

			if ((message[2] & 0xff) == 1) {
				lastMessageFlag = true;
			} else {
				lastMessageFlag = false;
			}

			for (int i = 3; i < 1024; i++) {
				fileByteArray[i - 3] = message[i];
			}

			outToFile.write(fileByteArray);
			System.out.println("Received: Sequence number = " + sequenceNumber + ",Flag =" + lastMessageFlag);

			if (lastMessageFlag) {
				outToFile.close();
				fileSocket.close();				
				break;
			}

		}

		fileSocket.close();
		System.out.println("File " + filePathName + "has been received and created");

	}


}
