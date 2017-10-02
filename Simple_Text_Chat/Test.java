
public class Test {
	public static void main(String[] args)
	{
		ChatServer r = new ChatServer();
		r.start();
		
		ChatClient c1 = new ChatClient("001","Eric");
		ChatClient c2 = new ChatClient("001","Black");
	}
}
