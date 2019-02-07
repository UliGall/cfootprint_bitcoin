package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;

import com.google.gson.Gson;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import model.distribution.Distribution;
import model.location.Location;

/**
 * <h1>App (JavaFX Application)</h1>
 * <p>This class creates the GUI of the application and handles the parsing of the json to csv.</p>
 * <p>The GUI is created completly with the JavaFX library and is build in a minimal way. As soon as the application is started by the user
 * the a GET-Request via the java.net library will be made to get a json, this will be parsed with the Gson Library. </p>
 * 
 * @author Isabel Hoekstein & Christian Stoll
 * 
 */
public class App extends Application {
	private final static int TOTAL_WIDTH = 5;
	private final static int COL_WIDTH = 2;

	private final static String LOCATION = "https://slushpool.com/api/v1/stats/get_btc_server_hr_series/";
	private final static String DISTRIBUTION = "https://slushpool.com/api/v1/stats/get_btc_pool_hr_distribution/";

	private int locationTotal = 0;
	private int locationSuccess = 0;
	private int distributionTotal = 0;
	private int distributionSuccess = 0;

	private Text locationFiles = new Text();
	private Text distributionFiles = new Text();

	private File locationOutputDir;
	private File distributionOutputDir;

	private TextArea log;
	private int logInput = 0;

	private Stage stage;
	
	private boolean running = false;


	@Override
	public void start(Stage stage) {
		this.stage = stage;
		stage.setTitle("SlushPool-Tool");

		GridPane root = new GridPane();
		Scene scene = new Scene(root);

		Text locationText = new Text("Hash Rate Per Location");
		Text distributionText = new Text("Hash Rate Distribution");

		Text outputText = new Text("Output Directory ");
		Text intervalTimeText = new Text("Interval-Time [min]");

		TextField outputTextFieldLocation = new TextField();
		TextField intervalTimeTextFieldLocation = new TextField();
		TextField outputTextFieldDistribution = new TextField();
		TextField intervalTimeTextFieldDistribution = new TextField();
		
		intervalTimeTextFieldLocation.setPromptText("720");
		intervalTimeTextFieldDistribution.setPromptText("10");

		outputTextFieldLocation.setEditable(false);
		outputTextFieldDistribution.setEditable(false);

		Button openOutputLocation = new Button("Open");
		Button openOutputDistribution = new Button("Open");

		this.addOpenButtonLogic(openOutputLocation, outputTextFieldLocation, true);
		this.addOpenButtonLogic(openOutputDistribution, outputTextFieldDistribution, false);

		Text downloadedText = new Text("Downloaded Files ");

		Button startStopBtn = new Button("Start");
		startStopBtn.disableProperty().bind(outputTextFieldDistribution.textProperty().isEmpty()
				.or(outputTextFieldLocation.textProperty().isEmpty()));
		startStopBtn.setOnAction(event -> {
			running = !running;
			long locationSleepTime;
			long distributionSleepTime;
			try {
				locationSleepTime = Long.valueOf(intervalTimeTextFieldLocation.getText()) * 60000;
				distributionSleepTime = Long.valueOf(intervalTimeTextFieldDistribution.getText()) * 60000;
			} catch (NumberFormatException e) {
				log.appendText("Interval-Time must be an integer!\n");
				return;
			}
			DownloadLocationThread thread = new DownloadLocationThread(this, locationSleepTime);
			DownloadDistributionThread threadDist = new DownloadDistributionThread(this, distributionSleepTime);
			Platform.runLater(() -> {
			if (running) {
				log.appendText("Starting Download...\n");
				startStopBtn.setText("Stop");
				thread.start();
				threadDist.start();
			} else {
				log.appendText("Paused Download...\n");
				startStopBtn.setText("Start");
				thread.interrupt();
				threadDist.interrupt();
			}
			});
		});

		log = new TextArea();
		log.setEditable(false);
		

		int rowIndex = 0;
		root.setPadding(new Insets(16, 16, 16, 16));
		root.setVgap(8);
		root.setHgap(8);
		root.add(locationText, 1, rowIndex, COL_WIDTH, 1);
		root.add(distributionText, 1 + COL_WIDTH, rowIndex++, COL_WIDTH, 1);
		root.add(new Separator(), 0, rowIndex++, TOTAL_WIDTH, 1);
		root.add(outputText, 0, rowIndex);
		root.add(outputTextFieldLocation, 1, rowIndex);
		root.add(openOutputLocation, 2, rowIndex);
		root.add(outputTextFieldDistribution, 3, rowIndex);
		root.add(openOutputDistribution, 4, rowIndex++);
		root.add(intervalTimeText, 0, rowIndex);
		root.add(intervalTimeTextFieldLocation, 1, rowIndex, COL_WIDTH, 1);
		root.add(intervalTimeTextFieldDistribution, COL_WIDTH + 1, rowIndex++, COL_WIDTH, 1);
		root.add(new Separator(), 0, rowIndex++, TOTAL_WIDTH, 1);
		root.add(downloadedText, 0, rowIndex);
		root.add(locationFiles, 1, rowIndex, COL_WIDTH, 1);
		root.add(distributionFiles, COL_WIDTH + 1, rowIndex++, COL_WIDTH, 1);

		root.add(startStopBtn, 0, rowIndex++, TOTAL_WIDTH, 1);
		root.add(log, 0, rowIndex++, TOTAL_WIDTH, 1);

		GridPane.setHalignment(locationText, HPos.CENTER);
		GridPane.setHalignment(distributionText, HPos.CENTER);
		GridPane.setHalignment(startStopBtn, HPos.CENTER);	
		GridPane.setHalignment(locationFiles, HPos.CENTER);
		GridPane.setHalignment(distributionFiles, HPos.CENTER);

		this.updateDownloadStatus();
		
		stage.setOnCloseRequest(t -> {
		        Platform.exit();
		        System.exit(0);
		});

		stage.setScene(scene);
		stage.show();
	}
	
	/**
	 * This function handles the Log Output Textfield overflow. As soon as there are more than 10 input, the input will be removed. 
	 * It is automatically called by the Download Threads.
	 * 
	 * @see DownloadDistributionThread
	 * @see DownloadLocationThread
	 */
	public synchronized void newLogInput() {
		logInput++;
		if (logInput > 10) {
			log.setText("");
			logInput = 0;
		}
	}

	/**
	 * This function will call {@link App#func(boolean)} to get the json through a GET-Request and write it to a File after parsing it into a json.
	 */
	public synchronized void exportDistribution() {
		Gson gson = new Gson();
		String json = this.func(false);
		Distribution distribution = gson.fromJson(json, Distribution.class);
		log.appendText("Check:\t" + distribution.getChecksum() + "\n");
		try {
			String timestamp = Instant.now().toString().replace("-", "").replace(":", "").replace(".", "").substring(0,  13);
			Path p = Paths.get(distributionOutputDir.getAbsolutePath(), timestamp + "_" + distribution.getChecksum() + ".csv");
			Path path = Files.write(p, distribution.toCSV().getBytes());
			log.appendText("Output:\t" + path + "\n");
		} catch (IOException e) {
			log.appendText("ERROR:\t" + e.getCause() + "\n");
		}
		
	}

	/**
	 * This function works the same like {@link App#exportDistribution()} but getting the location file instead of the distribution from the server.
	 */
	public synchronized void exportLocation() {
		Gson gson = new Gson();
		String json = this.func(true);
		Location location = gson.fromJson(json, Location.class);
		log.appendText("Check:\t" + location.getChecksum() + "\n");
		try {
			String timestamp = Instant.now().toString().replace("-", "").replace(":", "").replace(".", "").substring(0,  13);
			Path p = Paths.get(locationOutputDir.getAbsolutePath(), timestamp + "_" + location.getChecksum() + ".csv");
			Path path = Files.write(p, location.toCSV().getBytes());
			log.appendText("Output:\t" + path + "\n");
		} catch (IOException e) {
			log.appendText("ERROR:\t" + e.getCause() + "\n");
		}
	}

	/**
	 * Calling this function will result in a new output in the log textarea. It displays the successrate of the distribution and location downloads. 
	 * A download is counted as success if the status code from the server is 200.
	 */
	public synchronized void updateDownloadStatus() {
		this.locationFiles.setText(locationSuccess + " / " + locationTotal);
		this.distributionFiles.setText(distributionSuccess + " / " + distributionTotal);
	}

	private void addOpenButtonLogic(Button btn, TextField tf, boolean location) {
		btn.setOnAction(event -> {
			DirectoryChooser dirChooser = new DirectoryChooser();
			File file = dirChooser.showDialog(stage);
			if (file != null) {
				tf.setText(file.getAbsolutePath());
				if (location)
					this.locationOutputDir = file;
				else
					this.distributionOutputDir = file;
			}
		});
	}

	/**
	 * This function creates a GET-Request to the SlushPool Site and reads the reponse-body (json) into string
	 * @param location passing true if location file should be downloaded otherwise (distribution) false
	 * @return json as String 
	 */
	private synchronized String func(boolean location) {
		try {
			log.appendText(Instant.now() + "\n");
			URL url = new URL(location ? LOCATION : DISTRIBUTION);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			log.appendText("GET " + url + "\n");

			int status = con.getResponseCode();
			if (status == 200) {
				if (location)
					this.locationSuccess++;
				else
					this.distributionSuccess++;
			}
			log.appendText("Status:\t" + status + "\n");
			if (location)
				this.locationTotal++;
			else
				this.distributionTotal++;

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer content = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			in.close();
			log.appendText("Content:\t" + content.length() + "\n");
			System.out.println("Content: " + content);

			con.disconnect();
			System.out.println("Closing Connection");
			return content.toString();
		} catch (IOException e) {
			log.appendText("ERROR:\t" + e.getCause() + "\n");
		}
		return null;
	}
	
	public static void main(String args[]) {
		launch(args);
	}

}
