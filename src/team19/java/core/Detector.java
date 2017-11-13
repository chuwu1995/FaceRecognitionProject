/**
 * 
 */
package team19.java.core;

import java.io.IOException;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;


/**
 * @author Chu Wu
 *  
 */
public class Detector {

	private int absoluteFaceSize;
	// face cascade classifier
	private CascadeClassifier faceCascade;

	public Detector(){
		this.faceCascade = new CascadeClassifier();
		this.faceCascade.load("lib/FaceDetectionClassifier/lbpcascade_frontalface.xml");
		this.absoluteFaceSize = 0;
	}
			
	/**
	 * Method for face detection and tracking
	 * 
	 * @param frame
	 *            it looks for faces in this frame
	 */
	public void detectFace(Mat frame)
	{
		
			MatOfRect faces = new MatOfRect();
			Mat grayFrame = new Mat();
			
			// convert the frame in gray scale
			Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
			// equalize the frame histogram to improve the result
			Imgproc.equalizeHist(grayFrame, grayFrame);

			// compute minimum face size (20% of the frame height, in our case)
			if (this.absoluteFaceSize == 0)
			{
				int height = grayFrame.rows();
				if (Math.round(height * 0.2f) > 0)
				{
					this.absoluteFaceSize = Math.round(height * 0.2f);
				}
			}

			
			// detect faces
			this.faceCascade.detectMultiScale(grayFrame, faces, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE,
					new Size(this.absoluteFaceSize, this.absoluteFaceSize), new Size());

			// each rectangle in faces is a face: draw them!
			Rect[] facesArray = faces.toArray();
			for (int i = 0; i < facesArray.length; i++)
				Imgproc.rectangle(frame, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 255, 0), 3);
		
		
			
	}
}
