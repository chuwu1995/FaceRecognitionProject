/**
 * 
 */
package team19.java.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.face.FaceRecognizer;
import org.opencv.face.FisherFaceRecognizer;

import team19.java.util.PrepareTraining;


/**
 * @author Chu Wu
 *  
 */
public class Model {
	private FaceRecognizer faceRecognizer;
	private PrepareTraining prepareTraining;
	private static final String OUTPUT = "lib" + File.separator + "FaceRecognizer" + File.separator + "Model.xml";
	

	public Model() {
		
		prepareTraining = new PrepareTraining();
//		File file = new File(OUTPUT);
		
//		if(file.exists())
//			load();
//		else
		faceRecognizer=train(prepareTraining.getImageList(),prepareTraining.getLabelList());
	}
	/**
	 * @param args
	 * @throws IOException 
	 */

	
	public int predict(Mat face) {
	
		int predictLabel = faceRecognizer.predict_label(face);
		return predictLabel;
	}
	
	public void predict(Mat src,int[] label, double[] confidence) {
		
		faceRecognizer.predict(src, label, confidence);;
	}
	

	private FaceRecognizer train(ArrayList<Mat> imagesList,ArrayList<Integer> labelsList) {
		


		FaceRecognizer faceRecognizer= FisherFaceRecognizer.create(42, 1800);

		MatOfInt labelMat = new MatOfInt();
		labelMat.fromList(labelsList);
		
		
		
		faceRecognizer.train(imagesList, labelMat);;
		faceRecognizer.save(OUTPUT);
		
		System.out.println("my faceRecognizer saved");
		return faceRecognizer;
	}
	
	
	
	public void load(){
		System.out.println(OUTPUT);
		faceRecognizer.read(OUTPUT);
	}

}
