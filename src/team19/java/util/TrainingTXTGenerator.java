/**
 * 
 */
package team19.java.util;

/**
 * @author Chu Wu
 *  
 */

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class TrainingTXTGenerator {

	public void generate(String srcPath, String dstPath) throws IOException {
		File outputFile = new File(dstPath);
		PrintWriter pw = new PrintWriter(outputFile);
		File srcFile = new File(srcPath);

		getFiles(srcFile, pw);
		pw.flush();
		pw.close();
	}

	public void generate() throws IOException {
		String src = "resource/TrainingPhoto";
		String dst = "lib/PhotoAndLableList/PhotoAndLableList.txt";
		generate(src, dst);
	}

	private void getFiles(File dir, PrintWriter pw) throws IOException {

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
					String parentFolder = dirs[dirs.length-1];
					String label = parentFolder.substring(1, parentFolder.length()-1);
					String outputString = dir.toString() + ";"+ label +  "\n";
					pw.write(outputString);
				}

			}

		}
	}

	public static void main(String[] a) throws Exception {

		TrainingTXTGenerator fl = new TrainingTXTGenerator();
		fl.generate();

	}

}