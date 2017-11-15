/**
 * 
 */
package team19.java.core;

import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.face.FaceRecognizer;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import team19.java.DB.DBManager;
import team19.java.DB.User;
import team19.java.util.ImageProcessing;

/**
 * @author Chu Wu
 *  
 */
public class Recognizer {
	private DBManager dbManager = new DBManager();
	private ArrayList<User> users = new ArrayList<User>();
	private Model model = new Model();
	private static final int THRESHOLD = 800;


	public ArrayList<User> recognizeFace(Mat frame, Rect[] facesArray) {
		
		users.clear();

		for (int i = 0; i < facesArray.length; i++) {
			Mat face = new Mat(frame, facesArray[i]);
			ImageProcessing.resize(face, face, ImageProcessing.size_92_112);
			Imgproc.cvtColor(face, face, Imgproc.COLOR_BGR2GRAY);
			Imgproc.equalizeHist(face, face);

			Imgproc.rectangle(frame, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 255, 0), 3);
			int[] labelList = new int[1];
			double[] confidenceList	 = new double[1];		// get label
			model.predict(face,labelList,confidenceList);
			
			int label = labelList[0];
			double confidence = confidenceList[0];
			
			System.out.println(label + "    " + confidence);
		
			if (label == -1 || confidence<THRESHOLD) {
				Imgproc.putText(frame, "Strange!!!", new Point(facesArray[i].x, facesArray[i].y),
						Core.FONT_HERSHEY_TRIPLEX, 2.0, new Scalar(255, 0, 0));
				continue;
			}

			if (label > 1 && confidence>THRESHOLD) {
				User user = null;
				try {
					user = dbManager.getUserDAO().getUserByUID(label).get(0);
					users.add(user);

				} catch (ArrayIndexOutOfBoundsException e) {
					continue;
				}

				if (user != null)
					Imgproc.putText(frame, user.getName().getValue(), new Point(facesArray[i].x, facesArray[i].y),
							Core.FONT_HERSHEY_TRIPLEX, 2.0, new Scalar(0, 255, 0));

			}


		}
		return users;

	}
	
	
	
}
