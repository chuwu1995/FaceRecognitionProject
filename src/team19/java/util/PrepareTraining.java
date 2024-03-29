/**
 * 
 */
package team19.java.util;

import java.io.BufferedReader;

/**
 * @author Chu Wu
 *  
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

/**
 * 
 * process image and label info to prepare training
 * 
 * @author Chu Wu
 *
 */
public class PrepareTraining {
	// two paths
	private static final String src = "resource/TrainingPhoto";
	private static final String dst = "lib/PhotoAndLableList/PhotoAndLableList.txt";

	private ArrayList<Mat> imageList = new ArrayList<Mat>();
	private ArrayList<Integer> labelList = new ArrayList<Integer>();

	public PrepareTraining() {
		getCurrentLists();
	}

	/**
	 * generate image and label list file
	 * 
	 * @return the file
	 */
	public File generate() {
		return generate(src, dst);
	}

	/**
	 * get image list
	 * 
	 * @return image list
	 */
	public ArrayList<Mat> getImageList() {
		return this.imageList;
	}

	/**
	 * get label list
	 * 
	 * @return image list
	 */
	public ArrayList<Integer> getLabelList() {
		return this.labelList;
	}

	/**
	 * generate image and label list
	 * 
	 * @param srcPath
	 *            src path
	 * @param dstPath
	 *            dst path
	 * @return the file
	 */
	private File generate(String srcPath, String dstPath) {
		File outputFile = new File(dstPath);
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(outputFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		File srcFile = new File(srcPath);

		getFiles(srcFile, pw);

		pw.flush();
		pw.close();

		return outputFile;

	}

	/**
	 * get all files in the training image folders
	 * 
	 * @param dir
	 * @param pw
	 */
	private void getFiles(File dir, PrintWriter pw) {

		if (dir.exists()) {

			if (dir.isDirectory()) {

				File[] files = dir.listFiles();
				for (File file : files) {
					getFiles(file, pw);
				}

			} else {
				if (!dir.isHidden()) {
					String parentPath = dir.getParent();
					String[] dirs = parentPath.split("/");
					String parentFolder = dirs[dirs.length - 1];
					String label = parentFolder.substring(1, parentFolder.length());
					String outputString = dir.toString() + ";" + label + "\n";
					pw.write(outputString);
				}

			}
		}
	}

	/**
	 * create image and label lists
	 */
	private void getCurrentLists() {
		File file = generate();
		FileReader fd = null;
		try {
			fd = new FileReader(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(fd);
		String line;
		int label = 0;
		Mat image = null;
		try {
			while ((line = br.readLine()) != null) {
				String item[] = line.split(";");
				label = Integer.parseInt(item[1]);
				image = Imgcodecs.imread(item[0], Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
				// Imgproc.equalizeHist(image, image);
				this.imageList.add(image);
				this.labelList.add(label);

			}
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}