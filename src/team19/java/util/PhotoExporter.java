/**
 * 
 */
package team19.java.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

/**
 * Export photos to files
 * @author Chu Wu
 * 
 */
public class PhotoExporter {
	// training image root
	private static String root = "resource" + File.separator + "TrainingPhoto" + File.separator;

	/**
	 * export training image
	 * @param photos photos
	 * @param uid user id
	 */
	public static void export(ArrayList<Mat> photos, int uid) {
		String targetFolder = root + "s" + uid;

		File file = new File(targetFolder);
		int startIndex = 1;
		
		if (!file.exists()) {
			file.mkdirs();
			
		}

		else {
			startIndex = getMaxIndex(file) + 1;
			
		}
		
		String path = null;
		int length = photos.size();
		for (int i = startIndex; i < length + startIndex; i++) {
			String name = i + ".jpg";
			path = targetFolder + File.separator + name;
			System.out.println(path);
			Imgcodecs.imwrite(path, photos.get(i - startIndex));
		}
	}


	/**
	 * get the max index of photo in a folder
	 * @param file
	 * @return
	 */
	private static int getMaxIndex(File file){
		
		
		File[] names = file.listFiles();
		ArrayList<Integer> indexList = new ArrayList<Integer>();
		TreeSet<Integer> set = new 	TreeSet<Integer>();


		for (File f : names) {
			if (f.isHidden())
				continue;
			
			String[] dirs = f.toString().split("/");
			String name = dirs[dirs.length - 1];
			Pattern pattern = Pattern.compile("[0-9]*");
			Matcher isNum = pattern.matcher(name);
			isNum.find();
			String index = isNum.group();
			int indexNumber = Integer.parseInt(index);
			indexList.add(indexNumber);
			set.add(indexNumber);

		}
		int last = set.last();
		System.out.println(last);
		
		return last;
	}

	/**
	 * remove a folder
	 * @param uid
	 */
	public static void remove(int uid) {
		String targetFolder = root + "s" + uid;

		File file = new File(targetFolder);
		if (file.exists()) {
			deleteDir(file);
		}

		else
			System.out.println("Folder not exists!");
	}

	/**
	 * delete a dir
	 * @param dir
	 * @return
	 */
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
