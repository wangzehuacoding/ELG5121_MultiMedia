package GraphicalInterface;

import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JLabel;

import Entity.FileEntity;
import Utility.FileTransfer;

public class FileSendDialog {

	public void InitFileChooser() throws IOException {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(null);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int port = 9300;

		int select = fileChooser.showDialog(new JLabel(), "Choose the File ");

		if (select == JFileChooser.APPROVE_OPTION) {

			//getFileInfo
			File file = fileChooser.getSelectedFile();
			FileEntity currentFileEntity =GetChoosenFileInfo(file);
			FileEntity targetFileEntity = GetTargetFileInfo(file);
			
			//FileReceive
			new Thread(new Runnable() {
				public void run() {
					try {

						FileTransfer.receiveAndCreate(port, targetFileEntity.getfilePath());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
			
			//FileSend
			String hostName = "localhost";
			FileTransfer.createAndSend(hostName, port, currentFileEntity.getfilePath());
		}
	}

	// Get the current file Info
	public FileEntity GetChoosenFileInfo(File file) {			
		FileEntity currentFileEntity = new FileEntity();
		currentFileEntity.setfileName(file.getName());
		currentFileEntity.setfilePath(file.getAbsolutePath());

		return currentFileEntity;
	}

	// Get the target file path
	public FileEntity GetTargetFileInfo(File file) {	
		String targetFolderPath = "C:\\Users\\Homeuser\\Desktop\\FileTransferTest-Public\\Public";
		String targetFilePath = targetFolderPath + "\\" + file.getName();
		FileEntity targetFileEntity = new FileEntity();
		targetFileEntity.setfilePath(targetFilePath);

		return targetFileEntity;
	}

}