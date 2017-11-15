/**
 * 
 */
package team19.java.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

/**
 * @author Chu Wu
 * 
 */
public class PhotoExporter {
	private static String root = "resource" + File.separator + "TrainingPhoto" + File.separator;

	public static void export(ArrayList<Mat> photos, int uid) {
		String targetFolder = root + "s" + uid;

		File file = new File(targetFolder);

		if (!file.exists()) {
			file.mkdirs();
			String path = null;
			for (int i = 1; i <= photos.size(); i++) {
				String name = i + ".jpg";
				path = targetFolder + File.separator + name;

				Imgcodecs.imwrite(path, photos.get(i - 1));
			}

		}

		else {

			remove(uid);
			export(photos, uid);
		}
	}

	public static void remove(int uid) {
		String targetFolder = root + "s" + uid;

		File file = new File(targetFolder);
		if (file.exists()) {
			deleteDir(file);
		}

		else
			System.out.println("Folder not exists!");
	}

	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		if (dir.delete()) {
			return true;
		} else {
			System.out.println("fail to remove");
			return false;
		}
	}

	/**
	 * out put profile photo, name is uid
	 * 
	 * @param photo
	 */
	public static void exportProfilePhoto(Mat photo, int uid) {

		String path = "resource" + File.separator + "Profile" + File.separator + uid + ".jpg";
		Imgcodecs.imwrite(path, photo);
	}

}
