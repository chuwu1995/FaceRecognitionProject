/**
 * 
 */
package team19.java.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.face.EigenFaceRecognizer;
import org.opencv.face.FaceRecognizer;
import org.opencv.face.FisherFaceRecognizer;
import org.opencv.face.LBPHFaceRecognizer;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import team19.java.DB.User;
import team19.java.util.ImageProcessing;
import team19.java.util.PrepareTraining;

/**
 * @author Chu Wu
 * 
 */
public class Model {
	private FaceRecognizer faceRecognizer;
	private PrepareTraining prepareTraining;
	private static final String OUTPUT = "lib" + File.separator + "FaceRecognizer" + File.separator + "Model.xml";
	private static Model model;
	private Model() {

		File file = new File(OUTPUT);
		faceRecognizer = LBPHFaceRecognizer.create(1, 8, 8, 8, 100);
		
		if (file.exists())
			load();
		else {
			prepareTraining = new PrepareTraining();
			faceRecognizer = train(prepareTraining.getImageList(), prepareTraining.getLabelList());
		}
			

	}
	
	public static Model getInstance(){
		if (model != null)
			return model;
		else
			model = new Model();
		return model;
	}

	/**
	 * @param args
	 * @throws IOException
	 */

	public int predict(Mat face) {

		int predictLabel = faceRecognizer.predict_label(face);
		return predictLabel;
	}

	public void predict(Mat src, int[] label, double[] confidence) {

		faceRecognizer.predict(src, label, confidence);

	}

	private FaceRecognizer train(ArrayList<Mat> imagesList, ArrayList<Integer> labelsList) {
		/******************************
		 * only LBPHFaceRecognizer support update!
		 */
		// FaceRecognizer faceRecognizer= FisherFaceRecognizer.create(0,1500);

		// The number of components (read: Fisherfaces) kept for this Linear
		// Discriminant Analysis with the Fisherfaces criterion. It's useful to
		// keep all components, that means the number of your classes c (read:
		// subjects, persons you want to recognize). If you leave this at the
		// default (0) or set it to a value less-equal 0 or greater (c-1), it
		// will be set to the correct number (c-1) automatically.

		// FaceRecognizer faceRecognizer= EigenFaceRecognizer.create(0,5800);

		// The number of components (read: Eigenfaces) kept for this Principal
		// Component Analysis. As a hint: There's no rule how many components
		// (read: Eigenfaces) should be kept for good reconstruction
		// capabilities. It is based on your input data, so experiment with the
		// number. Keeping 80 components should almost always be sufficient.

//		FaceRecognizer faceRecognizer = LBPHFaceRecognizer.create(1, 8, 8, 8, 115);

		// radius The radius used for building the Circular Local Binary
		// Pattern. The greater the radius, the smoother the image but more
		// spatial information you can get.
		// neighbors The number of sample points to build a Circular Local
		// Binary Pattern from. An appropriate value is to use 8 sample points.
		// Keep in mind: the more sample points you include, the higher the
		// computational cost.
		// grid_x The number of cells in the horizontal direction, 8 is a common
		// value used in publications. The more cells, the finer the grid, the
		// higher the dimensionality of the resulting feature vector.
		// grid_y The number of cells in the vertical direction, 8 is a common
		// value used in publications. The more cells, the finer the grid, the
		// higher the dimensionality of the resulting feature vector.
		// threshold The threshold applied in the prediction. If the distance to
		// the nearest neighbor is larger than the threshold, this method
		// returns -1.

		MatOfInt labelMat = new MatOfInt();
		labelMat.fromList(labelsList);

		faceRecognizer.train(imagesList, labelMat);

		// user write and read, instead of save and load!!! or you will fail to
		// load an existed model
		faceRecognizer.write(OUTPUT);

		System.out.println("model saved");
		return faceRecognizer;
	}

	public void load() {
		System.out.println("read:"+OUTPUT);
		faceRecognizer.read(OUTPUT);
	}

	public void save(){
		faceRecognizer.write(OUTPUT);
	}
	
	public void update(ArrayList<Mat> imagesList, int label){
		ArrayList<Integer> labelsList = new ArrayList<Integer>();
		for(int i=0;i<imagesList.size();i++)
			labelsList.add(label);
		
		MatOfInt labelMat = new MatOfInt();
		labelMat.fromList(labelsList);
		faceRecognizer.update(imagesList, labelMat);
		System.out.println("Face model updated");
	}
	

//	public static void main(String[] c) {
//		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//		// new Model();
////		FaceRecognizer faceRecognizer = LBPHFaceRecognizer.create(1, 8, 8, 8, 120);
////		faceRecognizer.read("lib/FaceRecognizer/Model.xml");
////		Mat image = Imgcodecs.imread("resource/TrainingPhoto/s1000000/1.jpg", Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
////		Mat image1 = Imgcodecs.imread("resource/TrainingPhoto/s1000000/2.jpg", Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
////		ArrayList<Mat> imagesList = new ArrayList<Mat>();
////		imagesList.add(image);
////		imagesList.add(image1);
////		ArrayList<Integer> labelsList = new ArrayList<Integer>();
////		for(int i=0;i<imagesList.size();i++)
////			labelsList.add(2000);
////		MatOfInt labelMat = new MatOfInt();
////		labelMat.fromList(labelsList);
////		
////		System.out.println("1");
////
////		faceRecognizer.update(imagesList,labelMat);
////		
////		System.out.println("2");
//
//		Mat i = Imgcodecs.imread("/Users/wuchu/Desktop/me/10.jpg", Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
////		Mat i = Imgcodecs.imread("resource/TrainingPhoto/s1000000/1.jpg", Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
//
//		
//
//		Detector d = new Detector();
//		ArrayList<User> label = d.detectFace(i);
//		System.out.println(label.get(0).toString());
//	}
	
}
