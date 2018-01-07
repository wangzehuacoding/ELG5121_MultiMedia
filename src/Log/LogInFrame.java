package Log;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.sql.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import GraphicalInterface.UserClient;
public class LogInFrame extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	JButton btnLogIn;
	JButton btnQuit;
	
	public void start() throws SQLException {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LogInFrame frame = new LogInFrame();
					frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	// Create the frame.
	public LogInFrame() {
		setTitle("LogIn Test");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(5, 5, 422, 32);
		contentPane.add(panel);

		JLabel lblWelcomeToThe = new JLabel("Welcome to the Chat Demo");
		panel.add(lblWelcomeToThe);

		JLabel lblPleaseInputUser = new JLabel("Please Input User Name");
		lblPleaseInputUser.setBounds(48, 81, 176, 16);
		contentPane.add(lblPleaseInputUser);

		textField = new JTextField();
		textField.setBounds(231, 78, 116, 22);
		contentPane.add(textField);
		textField.setColumns(10);

		JLabel lblPleaseInputPassword = new JLabel("Please Input PassWord");
		lblPleaseInputPassword.setBounds(48, 132, 142, 16);
		contentPane.add(lblPleaseInputPassword);

		textField_1 = new JTextField();
		textField_1.setBounds(231, 129, 116, 22);
		contentPane.add(textField_1);
		textField_1.setColumns(10);

		btnLogIn = new JButton("Log In");
		btnLogIn.addMouseListener(new LoginEvent());

		btnLogIn.setBounds(48, 190, 97, 25);
		contentPane.add(btnLogIn);

		btnQuit = new JButton("Quit");
		btnQuit.setBounds(250, 190, 97, 25);
		contentPane.add(btnQuit);
	}
	public class LoginEvent extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getSource().equals(btnLogIn)) {
				String name = textField.getText();
				String password = textField_1.getText();
				if (name.isEmpty()&&(!password.isEmpty())) {
					JOptionPane.showMessageDialog(null, "please enter your name!");
					return;
				}
				if (password.isEmpty()&&(!name.isEmpty())) {
					JOptionPane.showMessageDialog(null, "please enter your password!");
					return;
				}
				if(name.isEmpty() && password.isEmpty())
				{
					JOptionPane.showMessageDialog(null, "please enter your name and password!");
					return;
				}
				Connection myConn = null;
				LinkToDataBase(myConn,name,password);
			}
		}
		public void LinkToDataBase(Connection myConn,String name,String password ) {
			
			try {
				myConn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/userpassword", "root", "123456");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Authentication(myConn,name,password);
		}

		public void Authentication(Connection myConn,String name,String password ) {
			try
			{
				PreparedStatement myStmt = myConn.prepareStatement("select * from userpass where name = ?");
				myStmt.setString(1, name);
				ResultSet myRs = myStmt.executeQuery();
				String getPassWord = getPass(myRs);
				int intgetPassWord = Integer.parseInt(getPassWord);
				int intpassword = Integer.parseInt(password);
				if (intgetPassWord == intpassword) {
					//System.out.println("Succeful Log In");
					//System.out.println("Log in as " + name);
					UserClient c = new UserClient("001", name);
				} else {
					JOptionPane.showMessageDialog(null, "wrong password!");
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	private static String getPass(ResultSet myRs) throws SQLException {
		String password = null;
		while (myRs.next()) {
			String pass = myRs.getString("password");
			password = pass;
		}
		return password;
	}
}

