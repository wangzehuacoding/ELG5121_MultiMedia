package GraphicalInterface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.video.ConverterFactory;
import com.xuggle.xuggler.video.IConverter;
import Utility.FileTransfer;

public class VideoFrame {
	JButton btnStart;
	JButton btnClose;
	JButton btnSend;
	
	private boolean flag = false;
	File file = new File("output.mp4");
	IMediaWriter writer;
	Webcam webcam;
	
	public void Initialization() {
		JFrame vedioFrame = new JFrame("Test webcam panel");
		JPanel buttonPanel = new JPanel();

		btnStart = new JButton("Start");
		buttonPanel.add(btnStart);
		btnStart.addMouseListener(new VedioEventRespon());

		btnClose = new JButton("Close");
		buttonPanel.add(btnClose);
		btnClose.addMouseListener(new VedioEventRespon());

		btnSend = new JButton("send");
		buttonPanel.add(btnSend);
		btnSend.addMouseListener(new VedioEventRespon());

		
		writer = ToolFactory.makeWriter(file.getName());
		Dimension size = WebcamResolution.QVGA.getSize();//QVGA is the screen resolution(320*240), it can show enough details and is good for the text and video playing
		writer.addVideoStream(0, 0, ICodec.ID.CODEC_ID_H264, size.width, size.height);//H.264 is a video encoding standard. 
		webcam = Webcam.getDefault();
		webcam.setViewSize(size);
		webcam.open(true);
		WebcamPanel panel = new WebcamPanel(webcam);
		panel.setFPSDisplayed(true);
		panel.setDisplayDebugInfo(true);
		panel.setImageSizeDisplayed(true);
		panel.setMirrored(true);

		vedioFrame.setLayout(new BorderLayout());
		vedioFrame.add(panel, BorderLayout.CENTER);
		vedioFrame.add(buttonPanel, BorderLayout.SOUTH);
		vedioFrame.setResizable(true);
		vedioFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		vedioFrame.pack();
		vedioFrame.setVisible(true);

	}

	public class VedioEventRespon extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {

			if (e.getSource().equals(btnStart)) {
				flag = true;
				{
					if(flag==true)
					{
						Thread thread = new Thread()
						{
							@Override
							public void run()
							{
								{
									long start = System.currentTimeMillis();
									for (int i = 0; i < 100; i++) {
										System.out.println("Capture frame " + i);
										BufferedImage image = ConverterFactory.convertToType(webcam.getImage(), BufferedImage.TYPE_3BYTE_BGR);//TYPE_3BYTE_BGR is a 8 bit depth RGB color image
										IConverter converter = ConverterFactory.createConverter(image, IPixelFormat.Type.YUV420P);//Y Luminance U V -Chrominance 420 is the sample way - every 4 Y use one group of (UV)
										IVideoPicture frame = converter.toPicture(image, (System.currentTimeMillis() - start) * 1000);
										frame.setKeyFrame(i == 0);
										frame.setQuality(0);
										writer.encodeVideo(0, frame);
										// 30 FPS
										try {
											Thread.sleep(30);
										} catch (InterruptedException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
									}
									writer.close();
									System.out.println("Video recorded in file: " + file.getAbsolutePath());
									}
							}
						};
						thread.start();
					}
				}
			}
			
			if (e.getSource().equals(btnClose)) {
				webcam.close();
			}
			
			if (e.getSource().equals(btnSend)) {
				try {
					sendVideo();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}


		}		
		
		public void sendVideo() throws IOException {
			String hostName = "localhost";
			int port = 9300;
			String getFileName = "D:\\refactor_MM_Project\\Refactor_Project\\output.mp4";
			// System.out.println(getFileName);
			FileTransfer.createAndSend(hostName, port, getFileName);
		}
	}
	
	
}