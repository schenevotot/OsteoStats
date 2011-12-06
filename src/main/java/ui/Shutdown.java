package ui;

/**
 * A clean shutdown process. This process will save the records before exiting.
 * 
 * @author Seb
 * 
 */
public class Shutdown extends Thread {

	private GuiController controller;

	/**
	 * Instantiates a Shutdown process.
	 * 
	 * @param controller
	 *            the GuiController which will be used to save the records
	 */
	public Shutdown(GuiController controller) {
		this.controller = controller;
	}

	private void exitSmoothly() {
		controller.shutdown();
	}

	/**
	 * Save the records. A pop-up is raised in case of exception in the process.
	 */
	public void run() {
		exitSmoothly();
	}
}
