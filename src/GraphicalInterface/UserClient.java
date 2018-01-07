package GraphicalInterface;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Entity.MessageEntity;
import Logic.AudioServer;

import java.awt.event.*;
import java.io.*;
import java.net.*;
import Server.TextChatServer;
import Utility.FileTransfer;

public class UserClient extends JFrame {
	private String groupID;
	private String clientName;
	private DatagramSocket messageSendSocket;
	private final int SERVER_PORT = 10000;
	private InetAddress serverIp;

	private JFrame userClientFrame;
	private JPanel contentPanel;
	private JTextArea showMessageArea;
	public JTextField inputMessageField;

	JButton btnSend;
	JButton btnVideo;
	JButton btnAudio;
	JButton btnFile;

	public UserClient(String groupID, String clientName) {
		super("ChatRoom #" + groupID + "/" + clientName);
		this.clientName = clientName;
		this.groupID = groupID;
		Initialization();
		initServer();
	}

	private void Initialization() {
		userClientFrame = new JFrame();
		userClientFrame.setBounds(100, 100, 359, 485);
		userClientFrame.setTitle("ChatRoom: " + groupID + " User: " + clientName);
		contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setLayout(null);

		btnSend = new JButton("send");
		btnSend.setBounds(167, 385, 65, 29);
		contentPanel.add(btnSend);
		btnSend.addMouseListener(new EventResponder());

		btnVideo = new JButton("");
		ImageIcon IconVideo = new ImageIcon("D:\\refactor_MM_Project\\Refactor_Project\\icon\\video.jpg");
		btnVideo.setIcon(IconVideo);
		btnVideo.setBounds(236, 385, 29, 29);
		contentPanel.add(btnVideo);
		btnVideo.addMouseListener(new EventResponder());

		btnFile = new JButton("");
		ImageIcon IconFile = new ImageIcon("D:\\refactor_MM_Project\\Refactor_Project\\icon\\file.png");
		btnFile.setIcon(IconFile);
		btnFile.setBounds(299, 385, 29, 29);
		contentPanel.add(btnFile);
		btnFile.addMouseListener(new EventResponder());

		btnAudio = new JButton("");
		ImageIcon IconAudio = new ImageIcon("D:\\refactor_MM_Project\\Refactor_Project\\icon\\audio.jpg");
		btnAudio.setIcon(IconAudio);
		btnAudio.setBounds(267, 385, 29, 29);
		contentPanel.add(btnAudio);
		btnAudio.addMouseListener(new EventResponder());

		showMessageArea = new JTextArea();
		showMessageArea.setBounds(15, 3, 307, 369);
		contentPanel.add(showMessageArea);

		inputMessageField = new JTextField();
		inputMessageField.setBounds(15, 386, 146, 26);
		contentPanel.add(inputMessageField);

		userClientFrame.add(contentPanel);
		userClientFrame.setResizable(false);

		userClientFrame.setVisible(true);

		userClientFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				//System.out.println("JFrame WindowClosing Listener");
				messageSendSocket.disconnect();
				TextChatServer.textChatServer.close();
				boolean b = TextChatServer.textChatServer.isClosed();
				//System.out.println(b);
				dispose();
				System.exit(0);
			}
		});
	}

	private void initServer() {
		TextChatServer.logInGroup(groupID, this);
		try {
			messageSendSocket = new DatagramSocket();

			try {
				serverIp = InetAddress.getByName("127.0.0.1");
			}

			catch (UnknownHostException e) {
				//System.out.println("Unknown Host Exception..");
			}

		} catch (SocketException e) {
			//System.out.println("Socket Connection Exception...");
		}

	}

	public class EventResponder extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {

			if (e.getSource().equals(btnSend)) {
				String content = inputMessageField.getText();
				SendMessage(content);
				inputMessageField.setText(null);
			}

			if (e.getSource().equals(btnFile)) {
				FileSendDialog senddialog = new FileSendDialog();
				try {
					senddialog.InitFileChooser();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

			if (e.getSource().equals(btnAudio)) {
				if(!isLoclePortUsing(8888))
				{
					new Thread(){
						public void run() {						
							AudioServer server = new AudioServer();
							server.start();
						}
					}.start();
				}
				
				AudioFrame audioFrame = new AudioFrame();
				audioFrame.Initialization();

			}

			if (e.getSource().equals(btnVideo)) {

				VideoFrame videoFrame = new VideoFrame();
				try {
					videoFrame.Initialization();
				} catch (Throwable e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Thread thread = new Thread() {
					@Override
					public void run() {
						try {
							FileTransfer.receiveAndCreate(9300, "C:\\Users\\Homeuser\\Desktop\\FileTransferTest-Public\\Public\\output.mp4");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				};
				thread.start();

			}

		}
	}

	private void SendMessage(String content) {
		String message = messageFormat(content);
		byte[] messageByte = message.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(messageByte, messageByte.length, serverIp, SERVER_PORT);
		try {
			messageSendSocket.send(sendPacket);
			//System.out.println("Text from " + clientName + " have been sent");
		} catch (IOException e) {
			//System.out.println("IO Exception..");
		}
	}

	private String messageFormat(String content) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("{\"groupID\":").append("\"").append(groupID).append("\",");
		buffer.append("\"userName\":\"").append(clientName).append("\",");
		buffer.append("\"text\":\"").append(content).append("\"}");
		return buffer.toString();
	}

	public void pushBackMessageFromServer(MessageEntity message) {
		showMessageArea.append(message.getUserName() + ":" + message.getText());
		showMessageArea.append("\n");
	}
	
	public static boolean isLoclePortUsing(int port){
		boolean flag = true;
		try {
			flag = IsPortUsing("127.0.0.1", port);
		} catch (Exception e) {
		}
		return flag;
	}

	
	public static boolean IsPortUsing(String host,int port) throws UnknownHostException{
		boolean flag = false;
		InetAddress theAddress = InetAddress.getByName(host);
		try {
			Socket socket = new Socket(theAddress,port);
			flag = true;
		} catch (IOException e) {
			
		}
		return flag;
	}
}