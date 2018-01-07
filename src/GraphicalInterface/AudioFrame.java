package GraphicalInterface;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;

import Logic.AudioClient;
import Logic.AudioServer;
import Utility.JavaRecorder;

public class AudioFrame extends JFrame {

	private static final long serialVersionUID = -1082166342481848841L;
	public AudioClient audioClient = new AudioClient();

	JButton btnBegin;
	JButton btnStop;
	JButton btnPlay;
	JButton btnSend;
	JButton btnBeginRealTime;
	JButton btnCloseRealTime;

	void Initialization() {
		
		setLayout(new GridLayout(3, 2));
		btnBegin = new JButton("begin");
		add(btnBegin);
		btnBegin.addMouseListener(new EventResponder());

		btnStop = new JButton("stop");
		add(btnStop);
		btnStop.addMouseListener(new EventResponder());

		btnPlay = new JButton("play");
		add(btnPlay);
		btnPlay.addMouseListener(new EventResponder());

		btnSend = new JButton("send");
		add(btnSend);
		btnSend.addMouseListener(new EventResponder());

		btnBeginRealTime = new JButton("beginRealTime");
		add(btnBeginRealTime);
		btnBeginRealTime.addMouseListener(new EventResponder());

		btnCloseRealTime = new JButton("CloseRealTime");
		add(btnCloseRealTime);
		btnCloseRealTime.addMouseListener(new EventResponder());

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				audioClient.disConnect();
			}
		});

		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public class EventResponder extends MouseAdapter {
		AudioFrame frame = new AudioFrame();
		JavaRecorder jRecorder = frame.audioClient.jRecorder;

		@Override
		public void mouseClicked(MouseEvent e) {
			
			if (e.getSource().equals(btnBegin)) {
				jRecorder.start(false);
			}

			if (e.getSource().equals(btnStop)) {
				jRecorder.stop();
			}

			if (e.getSource().equals(btnPlay)) {
				byte buf[] = jRecorder.byteArrayOutputStream.toByteArray();
				//System.out.println(buf[0]);
				jRecorder.play(buf, buf.length);
			}

			if (e.getSource().equals(btnSend)) {
				//System.out.println("send" + jRecorder.byteArrayOutputStream.toByteArray().length);
				audioClient.sendMsg(jRecorder.byteArrayOutputStream.toByteArray());
			}

			if (e.getSource().equals(btnBeginRealTime)) {
				jRecorder.start(true);
			}

			if (e.getSource().equals(btnCloseRealTime)) {
				jRecorder.stop();
			}

		}

	}
}