package Main;
import Log.LogInFrame;
import java.sql.SQLException;
import GraphicalInterface.UserClient;
import Server.TextChatServer;
public class Main {
	public static void main(String[] args) throws SQLException
	{
		LogInFrame logAuthentication = new LogInFrame();
		TextChatServer textChatServer = new TextChatServer();
		textChatServer.start();
		logAuthentication.start();
	}
}