package controller;

/**
 * 
 * <h1>Download Location Thread</h1>
 * <p>This class represents the thread for downloading the location files. It triggers the download and waits a specific time 
 * until it will trigger the next download. </p>
 * <p>The Thread can be started and interrupted from outside of this class</p>
 * 
 * @author Isabel Hoekstein & Christian Stoll
 *
 */
public class DownloadLocationThread extends Thread {

	private boolean running;
	
	private App app;
	
	private long sleepingTime;

	/**
	 * A DownloadLocationThread can only be initialized with a App and a sleepingTime
	 * @param app JavaFx Application handling the export of the data and displaying informations
	 * @param sleepingTime Time in ms to wait before the next download will start
	 */
	public DownloadLocationThread(App app, long sleepingTime) {
		this.running = true;
		this.app = app;
		this.sleepingTime = sleepingTime;
	}

	/**
	 * When this function is called, each time it will create a new Log Input, export the csv-File and update the download status
	 * @see App#newLogInput()
	 * @see App#exportLocation()
	 * @see App#updateDownloadStatus()
	 * 
	 */
	@Override
	public void run() {
		while (running) {
			app.newLogInput();
			app.exportLocation();
			app.updateDownloadStatus();
			try {
				Thread.sleep(sleepingTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * Call this function to stop the Thread
	 */
	public void interrupt() {
		running = false;
	}

}
