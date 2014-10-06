package core;

import java.io.IOException;

import org.newdawn.slick.Color;

import core.audio.Ensemble;
import core.setups.GameSetup;
import core.setups.SplashScreen;
import core.setups.Stage;
import core.setups.TitleMenu;
import core.utilities.Config;
import core.utilities.keyboard.Keybinds;
import core.utilities.text.GameFont;
import core.utilities.text.Text;

public class Theater {
	
	/** TODO
	 */

	private Stage stage;
	private SplashScreen splashScreen;
	private TitleMenu titleMenu;

	private boolean playing;
	private boolean paused;
	public boolean debug;
	public long seed;

	private float delta;
	private float deltaMax = 25f;
	private long currentTime;
	private long lastLoopTime;
	public static int fps = 0;
	public static String version = "v1.3.1";
	public static String title = "Double Down";
	
	public boolean stoneField;
	public boolean acesHigh;
	
	private static Theater theater;

	public static void init() {
		theater = new Theater();
	}

	public static Theater get() {
		return theater;
	}

	public Theater() {
		Camera.init();
		Text.addFont("SYSTEM", new GameFont("avocado_.ttf", 24f, Color.white));
		Ensemble.init();
		Config.loadConfig();
	
		splashScreen = new SplashScreen();
	}

	public void update() {
		getFps();

		Camera.get().draw(getSetup());
		Camera.get().update();

		Ensemble.get().update();

		Input.checkInput(getSetup());
		
		if(!paused)
			getSetup().update();

		if(Camera.get().toBeClosed() || Keybinds.EXIT.clicked()) {
			close();
		}
	}

	public void play() {
		currentTime = getTime();

		playing = true;

		while(playing) {
			update();
		}
	}

	public void pause() {
		paused = !paused;
	}

	public void close() {
		try {
			Config.saveConfig();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Ensemble.get().close();
		Camera.get().close();
		System.exit(0);
	}

	public void getFps() {
		delta = getTime() - currentTime;
		currentTime = getTime();
		lastLoopTime += delta;
		fps++;
		if(lastLoopTime >= 1000) {
			Camera.get().updateHeader();
			fps = 0;
			lastLoopTime = 0;
		}
	}

	public GameSetup getSetup() {
		if(splashScreen != null) {
			return splashScreen;
		} else if(titleMenu != null) {
			return titleMenu;
		} else if(stage != null) {
			return stage;
		}

		return null;
	}

	public void cycleSetup() {
		if(splashScreen != null) {
			splashScreen = null;
			titleMenu = new TitleMenu();
		} else if(titleMenu != null) {
			titleMenu = null;
			stage = new Stage();
		} else if(stage != null) {
			stage = null;
			titleMenu = new TitleMenu();
		}
	}

	public Stage getTable() {
		return stage;
	}

	public static long getTime() {
		return System.nanoTime() / 1000000;
	}

	/**
	 * 
	 * @param speed 0.025f to get as close as you can to reducing something by 1 every second
	 * @return
	 */
	public static float getDeltaSpeed(float speed) {
		return (Theater.get().delta * speed) / Theater.get().deltaMax;
	}

	public static void main(String[] args) {
		// Determine OS and set natives path accordingly
		if(System.getProperty("os.name").startsWith("Windows")) {
			System.setProperty("org.lwjgl.librarypath", System.getProperty("user.dir") + "/native/windows");
		} else if(System.getProperty("os.name").startsWith("Mac")) {
			System.setProperty("org.lwjgl.librarypath", System.getProperty("user.dir") + "/native/macosx");
		} else if(System.getProperty("os.name").startsWith("Linux")) {
			System.setProperty("org.lwjgl.librarypath", System.getProperty("user.dir") + "/native/linux");
		} else {
			System.setProperty("org.lwjgl.librarypath", System.getProperty("user.dir") + "/native/solaris");
		}
		System.setProperty("resources", System.getProperty("user.dir") + "/resources");
		
		Theater.init();
		theater.play();
	}

}
