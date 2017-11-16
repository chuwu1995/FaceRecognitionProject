/**
 * 
 */
package team19.java.core;

import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;

import team19.java.DB.DBManager;
import team19.java.DB.User;
import team19.java.util.ImageProcessing;

/**
 * @author Chu Wu
 * 
 */
public class Detector {

	private int absoluteFaceSize;
	// face cascade classifier
	private CascadeClassifier faceCascade;
	private Recognizer recognizer;
	private boolean onlyDetect;

	public Detector() {
		this.faceCascade = new CascadeClassifier();
		this.faceCascade.load("lib/FaceDetectionClassifier/lbpcascade_frontalface.xml");
		this.absoluteFaceSize = 0;
		try {
			this.recognizer = new Recognizer();
		} catch (Exception e) {
			onlyDetect = true;
		}
	}

	/**
	 * Method for face detection and tracking
	 * 
	 * @param frame
	 *            it looks for faces in this frame
	 */
	public ArrayList<User> detectFace(Mat frame) {
		if (frame.empty())
			return null;
		
		MatOfRect faces = new MatOfRect();
		Mat grayFrame = new Mat();

		// convert the frame in gray scale
		Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
		// equalize the frame histogram to improve the result
		Imgproc.equalizeHist(grayFrame, grayFrame);

		// compute minimum face size (20% of the frame height, in our case)
		if (this.absoluteFaceSize == 0) {
			int height = grayFrame.rows();
			if (Math.round(height * 0.2f) > 0) {
				this.absoluteFaceSize = Math.round(height * 0.2f);
			}
		}

		// detect faces
		this.faceCascade.detectMultiScale(grayFrame, faces, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE,
				new Size(this.absoluteFaceSize, this.absoluteFaceSize), new Size());

		if (onlyDetect)
			return null;

		// each rectangle in faces is a face: draw them!
		Rect[] facesArray = faces.toArray();

		return recognizer.recognizeFace(frame, facesArray);

	}

	public Mat catchTrainingFaces(Mat image) {

		MatOfRect faces = new MatOfRect();

		Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2GRAY);
		// equalize the frame histogram to improve the result
		Imgproc.equalizeHist(image, image);

		faceCascade.detectMultiScale(image, faces);

		Rect[] facesArray = faces.toArray();

		if (facesArray.length == 0)
			return null;

		for (Rect i : facesArray) {
			Rect roi = i;
			// Mat face = new Mat();

			Mat face = new Mat(image, roi);

			Imgproc.resize(face, face, ImageProcessing.size_92_112);
			return face;

		}

		return null;

	}
}
