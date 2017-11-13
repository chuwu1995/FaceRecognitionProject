/**
 * 
 */
package team19.java.application;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.opencv.videoio.VideoCapture;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import team19.java.core.Detector;
import team19.java.util.ImageProcessing;

/**
 * face recognition function should be called in the grabFrame method
 * @author Chu Wu
 * 
 */
public class Controller {
	private Detector detector;
	@FXML
	private Text testInfo;

	@FXML
	private ImageView imageView;
	@FXML
	private Button startBtn;
	@FXML
	private Button addUserBtn;
	@FXML
	private Button addRecordBtn;
	@FXML
	private Button analyticBtn;
	@FXML
	private ImageView photo;
	@FXML
	private Text name;
	@FXML
	private Text gender;
	@FXML
	private Text program;
	@FXML
	private Text lastDate;
	@FXML
	private Text lastReason;
	@FXML
	private Text visitCount;
	@FXML
	private TableView recordTable;
	@FXML
	private TableColumn date;
	@FXML
	private TableColumn reason;

	@FXML
	private GridPane analyticGridPane;
	
	@FXML
	private Pane containerPane;
	
	// add user pane
	@FXML
	private Pane addUserPane; 
	@FXML
	private TextField nameInput;
	@FXML
	private ChoiceBox<String> genderChoiceBox;
	@FXML
	private ChoiceBox<String> programChoiceBox;
	@FXML
	private Button submitUserBtn;
	@FXML
	private Button submitUserInfoBtn;
	@FXML
	private Button catchImageBtn;
	private boolean catchImageFlag;
	

	// add record pane
	@FXML 
	private Pane addRecordPane;
	@FXML
	private ChoiceBox<String> reasonChoiceBox;
	@FXML
	private Button submitRecordBtn;
	
	// a timer for acquiring the video stream
	private ScheduledExecutorService timer;
	// the OpenCV object that performs the video capture
	private VideoCapture capture;
	// a flag to change the button behavior
	private boolean cameraActive;
	private boolean analyticActive;
	private boolean addUserActive;
	private boolean addRecordActive;



	/**
	 * Init the controller, at start time
	 * 
	 * @throws IOException
	 */
	@FXML
	private void initialize() throws IOException {
		// load opencv native library
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		// initialize capture
		this.capture = new VideoCapture();
		
		// initialize detector
		this.detector = new Detector();

		// initialize panes
		analyticGridPane.setVisible(false);
		addUserPane.setVisible(false);
		addRecordPane.setVisible(false);
		
		// initialize choice box
		initSalaryChoiceBox();
		initGenderChoiceBox();
		initProgramChoiceBox();

	}

	@FXML
	public void start() {

		testInfo.setText("start btn clicked");

		if (!this.cameraActive) {
			if (capture == null)
				testInfo.setText("capture null");

			// start the video capture
			this.capture.open(0);

			// is the video stream available?
			if (this.capture.isOpened()) {
				this.cameraActive = true;

				// grab a frame every 33 ms (30 frames/sec)
				Runnable frameGrabber = new Runnable() {

					@Override
					public void run() {
						// effectively grab and process a single frame
						Mat frame = grabFrame();
						// convert and show the frame
						Image imageToShow = ImageProcessing.mat2Image(frame);
						updateImageView(imageView, imageToShow);
					}
				};

				this.timer = Executors.newSingleThreadScheduledExecutor();
				this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);
				// update the button content
				this.startBtn.setText("Stop");
			} else {
				// log the error
				System.err.println("Failed to open the camera connection...");
			}
		} else {
			testInfo.setText("stop btn clicked");

			// the camera is not active at this point
			this.cameraActive = false;
			// update again the button content
			this.startBtn.setText("Start");
			// enable classifiers checkboxes

			// stop the timer
			this.stopAcquisition();
		}

	}

	/**
	 * analyze button
	 */
	@FXML
	public void analyze() {
		
		if (!analyticActive) {
			addUserBtn.setDisable(true);
			addRecordBtn.setDisable(true);
			analyticActive = true;
			
			//addUserPane.setVisible(false);

			
			analyticGridPane.setVisible(true);
		} else {
			analyticActive = false;
			analyticGridPane.setVisible(false);
			
			addUserBtn.setDisable(false);
			addRecordBtn.setDisable(false);


		}
		testInfo.setText("analytic btn clicked");

	}

	/**
	 * add user button
	 * @throws IOException
	 */
	@FXML
	public void addUser() throws IOException {

		if (!addUserActive) {
			analyticBtn.setDisable(true);
			addRecordBtn.setDisable(true);

			
			addUserActive = true;
			addUserPane.setVisible(true);
		} else {
			

			addUserActive = false;
			addUserPane.setVisible(false);
			
			analyticBtn.setDisable(false);
			addRecordBtn.setDisable(false);


		}
		testInfo.setText("add user btn clicked");

	}
	
	/**
	 * click add record button call this method
	 */
	@FXML 
	public void addRecord() {
		testInfo.setText("add record btn");
		if (!addRecordActive) {
			analyticBtn.setDisable(true);
			addUserBtn.setDisable(true);
	
			addRecordActive = true;
			addRecordPane.setVisible(true);
		} else {
			
			addRecordActive = false;
			addRecordPane.setVisible(false);
			
			analyticBtn.setDisable(false);
			addUserBtn.setDisable(false);

		}
	}
	
	@FXML
	public void submitRecord(){
		testInfo.setText("submit record");

	}
	
	@FXML
	public void submitUser(){
		testInfo.setText("submit user btn");

	}
	
	@FXML
	public void submitUserInfo(){
                //name gender program]
                // dao.insert(name.ge.p);
		testInfo.setText("submit user info");
                

	}
	@FXML
	public void catchImage(){
		if(cameraActive){
			catchImageFlag = true;
			testInfo.setText("catch image");

		}	
	}

	/**
	 * Update the {@link ImageView} in the JavaFX main thread
	 * 
	 * @param view
	 *            the {@link ImageView} to update
	 * @param image
	 *            the {@link Image} to show
	 */
	private void updateImageView(ImageView view, Image image) {
		Platform.runLater(() -> {
			view.imageProperty().set(image);
		});
	}

	/**
	 * Get a frame of image from the opened video stream (if any)
	 * 
	 * @return the {@link Image} to show
	 */
	private Mat grabFrame() {
		Mat frame = new Mat();

		// check if the capture is open
		if (this.capture.isOpened()) {
			try {
				// read the current frame
				this.capture.read(frame);

				// if the frame is not empty, process it
				if (!frame.empty()) {
					detector.detectFace(frame);
					// recognizeFace();
					// 
					if(catchImageFlag) {
						int uid = 1111111;
						exportProfilePhoto(frame,uid);
						Thread.sleep(1000);
						catchImageFlag = false;
					}
				}

			} catch (Exception e) {
				// log the (full) error
				System.err.println("Exception during the image elaboration: " + e);
			}
		}

		return frame;
	}

	/**
	 * Stop the acquisition from the camera and release all the resources
	 */
	private void stopAcquisition() {
		if (this.timer != null && !this.timer.isShutdown()) {
			try {
				// stop the timer
				this.timer.shutdown();
				this.timer.awaitTermination(100, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				// log any exception
				System.err.println("Exception in stopping the frame capture, trying to release the camera now... " + e);
			}
		}

		if (this.capture.isOpened()) {
			// release the camera
			this.capture.release();
		}
	}
	
	/**
	 * initialize add record choice box.
	 */
	private void initSalaryChoiceBox() {
		ObservableList<String> reasonChoiceBoxItem = FXCollections.observableArrayList();
		reasonChoiceBoxItem.add("Meet With A Person");
		reasonChoiceBoxItem.add("Retreive Something");
		reasonChoiceBoxItem.add("Lost Registration");
		reasonChoiceBoxItem.add("Other");

		reasonChoiceBox.setValue("Meet With A Person");
		reasonChoiceBox.setItems(reasonChoiceBoxItem);
	}
private void initGenderChoiceBox(){
	ObservableList<String> genderChoiceBoxItem = FXCollections.observableArrayList();
	genderChoiceBoxItem.add("Male");
	genderChoiceBoxItem.add("Female");
	

	genderChoiceBox.setValue("Male");
	genderChoiceBox.setItems(genderChoiceBoxItem);
}

private void initProgramChoiceBox(){
	ObservableList<String> programChoiceBoxItem = FXCollections.observableArrayList();
	programChoiceBoxItem.add("MISM");
	programChoiceBoxItem.add("MSIT");
	programChoiceBoxItem.add("MSPPM");
	

	programChoiceBox.setValue("MISM");
	programChoiceBox.setItems(programChoiceBoxItem);
}

/**
 * out put profile photo, name is uid
 * @param photo
 */
private void exportProfilePhoto (Mat photo, int uid){

	String path = "resource/photo/"+uid+".jpg";
	Imgcodecs.imwrite(path, photo);
}














}
