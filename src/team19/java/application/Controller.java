/**
 * 
 */
package team19.java.application;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import team19.java.DB.DBManager;
import team19.java.DB.Record;
import team19.java.DB.User;
import team19.java.core.Detector;
import team19.java.core.Model;
import team19.java.util.ImageProcessing;
import team19.java.util.PhotoExporter;
import team19.java.util.WelcomeVoice;

/**
 * Controller Class is used to control the user interface of the application.
 * face recognition function should be called in the grabFrame method.
 * 
 * @author Chu Wu
 * 
 */
public class Controller {
	/**
	 * Object which will be used later to implement functions
	 */
	private DBManager dbManager;
	private Detector detector;
	private Mat defaultProfile;

	/**
	 * FXML Object used in analytics
	 */
	@FXML
	private Button pieChartBtn;

	@FXML
	private DatePicker startDate;

	@FXML
	private DatePicker endDate;

	@FXML
	private Button barChartBtn;

	/**
	 * FXML Object used in dashboard
	 */
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
	private Text nameDashBoard;
	@FXML
	private Text genderDashBoard;
	@FXML
	private Text programDashBoard;
	@FXML
	private Text lastDate;
	@FXML
	private Text lastReason;
	@FXML
	private Text visitCount;
	@FXML
	private TableView<Record> recordTable;
	@FXML
	private TableColumn<Record, String> dateCol;
	@FXML
	private TableColumn<Record, String> reasonCol;

	@FXML
	private GridPane analyticGridPane;

	@FXML
	private Pane containerPane;

	/**
	 * FXML components in addUser pane
	 */
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
	private Button catchImageBtn;
	@FXML
	private Button catchTrainingImageBtn;
	@FXML
	private ImageView imageCatched;
	@FXML
	private Button discardBtn;
	@FXML
	private Text tempTrainingPhotoNumberText;

	private boolean catchProfileImageFlag;
	private boolean catchTrainingImageFlag;

	/**
	 * FXML components in addRecord pane
	 */
	@FXML
	private Pane addRecordPane;
	@FXML
	private ChoiceBox<String> reasonChoiceBox;
	@FXML
	private Button submitRecordBtn;

	// user table
	@FXML
	private Button displayUsersBtn;

	// logo
	// private ImageView logoImageView;

	// check if only one user is before the camera
	private int currentUID = -1;

	// a timer for acquiring the video stream
	private ScheduledExecutorService timer;
	// the OpenCV object that performs the video capture
	private VideoCapture capture;
	// a flag to change the button behavior
	private boolean cameraActive;
	private boolean analyticActive;
	private boolean addUserActive;
	private boolean addRecordActive;

	private boolean profileCatched;
	private boolean trainingPhotoCatched;

	private Mat tempProfile;
	private ArrayList<Mat> tempTrainingPhotos;

	// if discard a catched training image
	private boolean discardTempTrainingPhotoFlag;

	ObservableList<String> reasonChoiceBoxItem = FXCollections.observableArrayList();

	/**
	 * Initial the controller at start time
	 * 
	 * @throws IOException
	 */
	@FXML
	private void initialize() throws IOException {
		// load opencv native library
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		

		// initialize dbManager
		dbManager = new DBManager();
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

		// initialize default images
		defaultProfile = ImageProcessing.readImage("resource/Profile/DefaultPhoto.png");
		setProfile(defaultProfile);
		setCatchImageView(defaultProfile);

		// initialize record table
		initRecordTable();

		tempProfile = null;
		tempTrainingPhotos = new ArrayList<Mat>();

	}

	/**
	 * start method of the fxml application
	 */
	@FXML
	public void start() {

		if (!this.cameraActive) {

			if (addUserActive) {
				this.catchImageBtn.setDisable(false);
				this.catchTrainingImageBtn.setDisable(false);
			}
			
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

			// addUserPane.setVisible(false);

			analyticGridPane.setVisible(true);
		} else {
			analyticActive = false;
			analyticGridPane.setVisible(false);

			addUserBtn.setDisable(false);
			addRecordBtn.setDisable(false);

		}

	}

	/**
	 * add user button
	 * 
	 * @throws IOException
	 */
	@FXML
	public void addUser() throws IOException {

		if (!addUserActive) {
			if (currentUID == -1)
				setDashBoardToDefault();

			analyticBtn.setDisable(true);
			addRecordBtn.setDisable(true);
			addUserActive = true;
			addUserPane.setVisible(true);

			submitUserBtn.setDisable(true);

			if (!cameraActive) {
				catchImageBtn.setDisable(true);
				catchTrainingImageBtn.setDisable(true);
			}

		} else {

			addUserActive = false;
			addUserPane.setVisible(false);
			analyticBtn.setDisable(false);
			addRecordBtn.setDisable(false);
			this.imageCatched.setImage(null);
			 this.tempProfile = null;
			 this.tempTrainingPhotos = new ArrayList<Mat>();

		}

	}

	/**
	 * controller method of discard button
	 */
	@FXML
	public void discardTempTrainingPhoto() {
		if (cameraActive) {
			discardTempTrainingPhotoFlag = true;

		}

	}

	/**
	 * click add record button call this method
	 */
	@FXML
	public void addRecord() {
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
	
	/**
	 * click submit button in add Record pane call this method
	 */

	@FXML
	public void submitRecord() {
		String reason = reasonChoiceBox.getValue();
		dbManager.getRecordDAO().insertRecord(currentUID, reason);
		// *******************
		if (currentUID != -1)
			updateDashBoard(currentUID);

	}

	/**
	 * click submit button call in add User pand this method
	 */
	@FXML
	public void submitUser() {
		if (currentUID == -1) {
			// user name, gender, program
			String name = nameInput.getText();
			if (name.length() > 25)
				name = name.substring(0, 24);
			if (name.length() == 0) {
				alert("WARNING", "Incomplete Info", "Please enter name!", "WARNING");
				return;

			}

			String gender = genderChoiceBox.getValue();
			String program = programChoiceBox.getValue();

			// insert user to USERS table
			currentUID = dbManager.getUserDAO().insertUser(name, gender, program);

		}

		// export user profile
		if (tempProfile != null) {
			PhotoExporter.exportProfilePhoto(tempProfile, currentUID);
			setProfile(tempProfile);

			System.out.println("profile:" + currentUID);
		}

		// export training photos
		PhotoExporter.export(tempTrainingPhotos, currentUID);

		// update facerecognizer

		// this.detector = new Detector();

		Model.getInstance().update(this.tempTrainingPhotos, currentUID);

		// update dash board
		updateDashBoard(currentUID);

		this.submitUserBtn.setDisable(true);
		
		if(this.tempProfile != null)
			this.setProfile(this.tempProfile);

		this.setCatchImageView(null);

		this.tempProfile = null;
		this.tempTrainingPhotos = new ArrayList<Mat>();

		this.nameInput.clear();
		this.genderChoiceBox.setValue("Male");
		this.programChoiceBox.setValue("MISM");
		this.setCatchImageView(defaultProfile);
		this.tempTrainingPhotoNumberText.setText("");
		this.discardBtn.setDisable(true);

	}

	/**
	 * method to catch profile image
	 */
	@FXML
	public void catchImage() {
		if (cameraActive) {
			catchProfileImageFlag = true;

		}
	}

	/**
	 * method to catch training image
	 */
	public void catchTrainingImage() {
		if (cameraActive) {
			catchTrainingImageFlag = true;

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
					// add faces
					if (addUserActive) {
						
						System.out.println("Current UID: " + currentUID);
						
						if (currentUID == -1) {

							nameInput.setDisable(false);
							genderChoiceBox.setDisable(false);
							programChoiceBox.setDisable(false);

						} else {

							nameInput.setDisable(true);
							genderChoiceBox.setDisable(true);
							programChoiceBox.setDisable(true);

						}

						if (catchProfileImageFlag) {
							ImageProcessing.resize(frame, frame, ImageProcessing.size_92_112);
							tempProfile = frame;
							this.setProfile(tempProfile);
							this.setCatchImageView(tempProfile);
							catchProfileImageFlag = false;
							profileCatched = true;
						}

						if (catchTrainingImageFlag) {
							this.discardBtn.setDisable(false);
							Mat tempTrainingPhoto = detector.catchTrainingFaces(frame);
							if (tempTrainingPhoto != null) {
								tempTrainingPhotos.add(tempTrainingPhoto);
								setCatchImageView(tempTrainingPhoto);
								catchTrainingImageFlag = false;
								tempTrainingPhotoNumberText.setText("Photo Number: " + tempTrainingPhotos.size());
							}
							trainingPhotoCatched = true;

						}

						if (profileCatched && trainingPhotoCatched)
							submitUserBtn.setDisable(false);

						if (discardTempTrainingPhotoFlag) {

							int photoIndex = tempTrainingPhotos.size() - 1;

							if (photoIndex >= 0) {
								tempTrainingPhotos.remove(photoIndex);
								if (photoIndex > 0)
									setCatchImageView(tempTrainingPhotos.get(photoIndex - 1));
								else {
									setCatchImageView(defaultProfile);

								}

							}

							this.setProfile(defaultProfile);

							tempTrainingPhotoNumberText.setText("Photo Number: " + tempTrainingPhotos.size());
							discardTempTrainingPhotoFlag = false;
						}

					} else {
						ArrayList<User> users = detector.detectFace(frame);
						if (users != null) {
							if (users.size() >= 1) {
								currentUID = users.get(0).getUID().getValue();
								String name = users.get(0).getName().getValue();
								WelcomeVoice.addToMap(name);

								updateDashBoard(currentUID);

							} else {
								currentUID = -1;
								setDashBoardToDefault();
							}
						}

					}

				}

			} catch (Exception e) {
		
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

		reasonChoiceBoxItem.add("Meet With A Person");
		reasonChoiceBoxItem.add("Retreive Something");
		reasonChoiceBoxItem.add("Lost Registration");
		reasonChoiceBoxItem.add("Other");

		reasonChoiceBox.setValue("Meet With A Person");
		reasonChoiceBox.setItems(reasonChoiceBoxItem);
	}

	/**
	 * initialize the gender choice box.
	 */
	private void initGenderChoiceBox() {
		ObservableList<String> genderChoiceBoxItem = FXCollections.observableArrayList();
		genderChoiceBoxItem.add("Male");
		genderChoiceBoxItem.add("Female");

		genderChoiceBox.setValue("Male");
		genderChoiceBox.setItems(genderChoiceBoxItem);
	}

	/**
	 * initialize the program choice box.
	 */
	private void initProgramChoiceBox() {
		ObservableList<String> programChoiceBoxItem = FXCollections.observableArrayList();
		programChoiceBoxItem.add("MISM");
		programChoiceBoxItem.add("MSIT");
		programChoiceBoxItem.add("MSPPM");

		programChoiceBox.setValue("MISM");
		programChoiceBox.setItems(programChoiceBoxItem);
	}

	/**
	 * method to set the profile image
	 * @param mat picture format
	 */
	private void setProfile(Mat mat) {

		ImageProcessing.resize(mat, mat, ImageProcessing.size_150_200);
		updateImageView(photo, ImageProcessing.mat2Image(mat));
	}

	/**
	 * method to set training image
	 * @param mat picture format
	 */
	private void setCatchImageView(Mat mat) {
		if (mat != null) {
			ImageProcessing.resize(mat, mat, ImageProcessing.size_92_112);
			updateImageView(imageCatched, ImageProcessing.mat2Image(mat));
		}
	}

	/**
	 * method to set dash board to default
	 */
	private void setDashBoardToDefault() {
		nameDashBoard.setText("");
		genderDashBoard.setText("");
		programDashBoard.setText("");
		setRecordDashBoardToDefault();
		setProfile(defaultProfile);
	}

	/**
	 * method to set record dash board to default
	 */
	private void setRecordDashBoardToDefault() {
		lastDate.setText("");
		lastReason.setText("");
		visitCount.setText("");
		recordTable.setItems(null);
	}

	/**
	 * method to display user table
	 */
	@SuppressWarnings("unchecked")
	@FXML
	public void displayUsersTable() {
		dbManager = new DBManager();

		// a new stage
		Stage stage = new Stage();
		stage.setTitle("User List");

		// create pie chart

		TableView<User> table = new TableView<User>();
		table.setEditable(true);
		table.setTableMenuButtonVisible(true);

		TableColumn<User, String> nameCol = new TableColumn<User, String>("Name");
		TableColumn<User, String> genderCol = new TableColumn<User, String>("Gender");
		TableColumn<User, String> programCol = new TableColumn<User, String>("Program");

		nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
		genderCol.setCellFactory(TextFieldTableCell.forTableColumn());
		programCol.setCellFactory(TextFieldTableCell.forTableColumn());

		nameCol.setCellValueFactory(cellData -> cellData.getValue().getName());
		genderCol.setCellValueFactory(cellData -> cellData.getValue().getGender());
		programCol.setCellValueFactory(cellData -> cellData.getValue().getProgram());

		nameCol.setOnEditCommit(event -> {
			User row = event.getRowValue();
			int uid = row.getUID().getValue();
			String newName = event.getNewValue();
			dbManager.getUserDAO().updateUser("NAME", newName, uid);

		});

		genderCol.setOnEditCommit(event -> {
			User row = event.getRowValue();
			int uid = row.getUID().getValue();

			String newGender = event.getNewValue();
			dbManager.getUserDAO().updateUser("GENDER", newGender, uid);

		});

		programCol.setOnEditCommit(event -> {
			User row = event.getRowValue();
			int uid = row.getUID().getValue();
			String newProgram = event.getNewValue();
			dbManager.getUserDAO().updateUser("PROGRAM", newProgram, uid);

		});

		table.getColumns().addAll(nameCol, genderCol, programCol);

		ArrayList<User> userList = new ArrayList<User>();
		userList = dbManager.getUserDAO().getAllUser();
		ObservableList<User> userData = FXCollections.observableArrayList();
		for (User u : userList)
			userData.add(u);

		table.setItems(userData);

		Scene scene = new Scene(new Group());
		((Group) scene.getRoot()).getChildren().addAll(table);
		stage.setScene(scene);
		stage.show();

	}

	/**
	 * initialize the record table.
	 */
	private void initRecordTable() {

		dateCol.setCellValueFactory(cellData -> cellData.getValue().getDate());
		reasonCol.setCellValueFactory(cellData -> cellData.getValue().getReason());
	}

	/**
	 * update the record table
	 * 
	 * @param recordList
	 */
	private void updateRecordTable(ArrayList<Record> recordList) {

		ObservableList<Record> recordData = FXCollections.observableArrayList();
		for (Record e : recordList)
			recordData.add(e);

		recordTable.setItems(recordData);
	}

	/**
	 * update the dash board.
	 * @param uid userID
	 */
	private void updateDashBoard(int uid) {
		User user = dbManager.getUserDAO().getUserByUID(uid).get(0);

		setDashBoardUserInfo(user.getName().getValue(), user.getGender().getValue(), user.getProgram().getValue());
		ArrayList<Record> records = dbManager.getRecordDAO().getRecordsByUID(uid);

		String profilePath = "resource/Profile/" + uid + ".jpg";
		File profileFile = new File(profilePath);

		Mat profile = defaultProfile;
		if (profileFile.exists()) {
			profile = ImageProcessing.readImage(profilePath);
		}

		setProfile(profile);

		if (records.size() == 0) {
			setRecordDashBoardToDefault();
			return;
		}

		updateRecordTable(records);

		int count = records.size();
		Record lastRecord = dbManager.getRecordDAO().getLastRecordByUID(user.getUID().getValue()).get(0);
		updateDashBoardRecordInfo(lastRecord, count);

	}

	/**
	 * method to Set user Information on dash board 
	 * @param name students's name
	 * @param gender student's gender
	 * @param program student's program
	 */
	private void setDashBoardUserInfo(String name, String gender, String program) {
		nameDashBoard.setText(name);
		genderDashBoard.setText(gender);
		programDashBoard.setText(program);
	}

	/**
	 * method to update user information on dash board.
	 * @param record object of last record
	 * @param count total number of record
	 */
	private void updateDashBoardRecordInfo(Record record, int count) {
		lastDate.setText(record.getDate().getValue());
		lastReason.setText(record.getReason().getValue());
		visitCount.setText(String.valueOf(count));
	}

	/**
	 * method of display alert to user.
	 * @param title
	 * @param header
	 * @param content
	 * @param type
	 */
	private void alert(String title, String header, String content, String type) {
		Alert alert = null;
		switch (type) {
		case "WARNING":
			alert = new Alert(Alert.AlertType.WARNING);
		case "ERROR":
			alert = new Alert(Alert.AlertType.ERROR);
		default:
			alert = new Alert(Alert.AlertType.INFORMATION);
		}

		ButtonType close = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);

		alert.getButtonTypes().setAll(close);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);

		alert.showAndWait();
	}

	String start;
	String end;

	/**
	 * Mia get Date Range
	 */
	public ArrayList<Record> getDateRange() throws IOException {
		// start date
		if (startDate.getValue() != null) {
			start = startDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		} else {
			start = "2010-01-01";
		}
		// end date
		if (endDate.getValue() != null) {
			end = endDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		} else {
			end = "2100-01-01";
		}

		ArrayList<Record> recordByDateRange = dbManager.getRecordDAO().getRecordByDateRange(start, end);

		return recordByDateRange;
	}

	/**
	 * Mia pieChart button
	 */
	public void showPieChart() throws IOException {

		// a new stage
		Stage stage = new Stage();
		stage.setTitle("Visiting Report by Reasons");
		stage.setWidth(500);
		stage.setHeight(500);
		
		ArrayList<Record> recordByDateRange = getDateRange();
		ArrayList<Integer> countReasons = new ArrayList<>(Arrays.asList(0,0,0,0));
		
		for (Record r : recordByDateRange) {
			//User u = dbManager.getUserDAO().getUserByUID(r.getUid().getValue()).get(0);	
			for (int i =0; i < reasonChoiceBoxItem.size(); i++) {
			String reason = reasonChoiceBoxItem.get(i);
			//System.out.println(r.toString());
				if(r.getReason().getValue().equals(reason)){
					int temp = countReasons.get(i);
					countReasons.set(i, ++temp);
				}
				}
			}
		
		// create pie chart
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
		for(int i =0; i < reasonChoiceBoxItem.size(); i++) {
			pieChartData.add(new PieChart.Data(reasonChoiceBoxItem.get(i), countReasons.get(i)));
		}

		final PieChart chart = new PieChart(pieChartData);
		chart.setTitle("From " + start + " to " + end);

		Scene scene = new Scene(new Group());
		((Group) scene.getRoot()).getChildren().add(chart);
		stage.setScene(scene);
		stage.show();

	}
	
	/**
	 * Mia barChart button
	 */
	@SuppressWarnings("unchecked")
	public void showBarChart() throws IOException {
		
		ArrayList<Record> recordByDateRange = getDateRange();
		ArrayList<Integer> countFemale = new ArrayList<>(Arrays.asList(0,0,0,0));
		ArrayList<Integer> countMale = new ArrayList<>(Arrays.asList(0,0,0,0));
		
		for (Record r : recordByDateRange) {
			User u = dbManager.getUserDAO().getUserByUID(r.getUid().getValue()).get(0);	
			for (int i =0; i < reasonChoiceBoxItem.size(); i++) {
			String reason = reasonChoiceBoxItem.get(i);
				if(r.getReason().getValue().equals(reason)){
					if (u.getGender().getValue().equals("Female")) {
						int temp = countFemale.get(i);
						countFemale.set(i, ++temp);
					}else {
						int temp = countMale.get(i);
						countMale.set(i, ++temp);
					}
				}
			}
		}

		Stage stage = new Stage();
	      //Defining the axes   
	      CategoryAxis xAxis = new CategoryAxis();  
	      xAxis.setCategories(FXCollections.<String>
	      observableArrayList(Arrays.asList(reasonChoiceBoxItem.get(0),reasonChoiceBoxItem.get(1),reasonChoiceBoxItem.get(2),reasonChoiceBoxItem.get(3))));
	      //xAxis.setLabel("Reasons");
	       
	      NumberAxis yAxis = new NumberAxis();
	      //yAxis.setLabel("Numbers");
	     
	      //Creating the Bar chart
	      BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis); 
	      barChart.setTitle("From " + start + " to " + end);
	      
	      //Prepare XYChart.Series objects by setting data   
	      XYChart.Series<String, Number> series1 = new XYChart.Series<>();
	      series1.setName("Female");
	      
	      for(int i =0; i < reasonChoiceBoxItem.size(); i++) {		      
		      series1.getData().add(new XYChart.Data<>(reasonChoiceBoxItem.get(i), countFemale.get(i)));
	      }
	      
	      XYChart.Series<String, Number> series2 = new XYChart.Series<>();
	      series2.setName("Male");
	      
	      for(int i =0; i < reasonChoiceBoxItem.size(); i++) {		      
		      series2.getData().add(new XYChart.Data<>(reasonChoiceBoxItem.get(i), countMale.get(i)));
	      }

	      //Setting the data to bar chart       
	      barChart.getData().addAll(series1, series2);
	        
	      //Creating a Group object 
	      Group root = new Group(barChart);
	        
	      //Creating a scene object
	      Scene scene = new Scene(root, 600, 400);

		//Setting title to the Stage
	      stage.setTitle("Visiting Report by Gender");
	        
	      //Adding scene to the stage
	      stage.setScene(scene);
	        
	      //Displaying the contents of the stage
	      stage.show(); 
		}

}
