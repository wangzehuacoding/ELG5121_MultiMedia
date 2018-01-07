package Utility;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JOptionPane;



public class JavaRecorder {
	public static AudioFormat getAudioFormat() {
		//PCM is Pulse Code Modulation The Audio(analog signal) turn to signed pulse sequence without encoding and compression. Strong resilient to noise
		AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
		float rate = 8000f;
		int sampleSize = 16;
		boolean bigEndian = false;
		int channels = 1;
		return new AudioFormat(encoding, rate, sampleSize, channels, (sampleSize / 8) * channels, rate, bigEndian);
	}
	
	AudioFormat audioFormat;
	boolean flg,isRealTime;
	DataOutputStream dataOutputStream = null;
	public ByteArrayOutputStream byteArrayOutputStream = null;
	DataLine.Info targetInfo,sourceInfo;		
	TargetDataLine targetDataLine; //A target data line is a type of DataLine from which audio data can be read. The most common example is a data line that gets its data from an audio capture device. (The device is implemented as a mixer that writes to the target data line.
	SourceDataLine sourceDataLine;//A source data line is a data line to which data may be written. It acts as a source to its mixer. An application writes audio bytes to a source data line, which handles the buffering of the bytes and delivers them to the mixer.
		
	public JavaRecorder(DataOutputStream _dos){		
		try {
			dataOutputStream = _dos;
			audioFormat = getAudioFormat();
			targetInfo = new DataLine.Info(TargetDataLine.class, audioFormat);				
			targetDataLine = (TargetDataLine) (AudioSystem.getLine(targetInfo));
				
			sourceInfo = new DataLine.Info(SourceDataLine.class, audioFormat);	
			sourceDataLine = (SourceDataLine) (AudioSystem.getLine(sourceInfo));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	class player implements Runnable{
		byte buf[];int len;
		player(byte _buf[],int _len){
			buf = _buf;
			len = _len;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try{
				sourceDataLine.open();
				sourceDataLine.start();
				sourceDataLine.write(buf,0,len);	
				sourceDataLine.drain();
				sourceDataLine.stop();
				sourceDataLine.close();					
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		
	}
	
	public void play(byte buf[],int len){
		player p = new player(buf,len);
		new Thread(p).start();
	}
	
	class Recorder implements Runnable {
		@Override
		public void run() {
			try {		
				targetDataLine.open(audioFormat);
				targetDataLine.start();
				
				byte bts[] = new byte[9000];
				byteArrayOutputStream = new ByteArrayOutputStream();
				flg = true;
				
				while (flg) {				
					int cnt = targetDataLine.read(bts, 0, bts.length);
					if (cnt > 0) {
						if(isRealTime){
							dataOutputStream.writeInt(cnt);
							dataOutputStream.write(bts,0,cnt);	
						}
						else byteArrayOutputStream.write(bts,0,cnt);						
					}				
				}
				
				byteArrayOutputStream.close();				
				targetDataLine.close();
							
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void start(boolean _isRealTime){
		isRealTime = _isRealTime;
		Thread t = new Thread(new Recorder());
		
		t.start();
	}
	
	public void stop(){
		flg = false;
	}

}
