package core;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.util.ResourceLoader;

import core.render.DrawUtils;
import core.setups.GameSetup;
import core.utilities.text.Text;
import de.matthiasmann.twl.utils.PNGDecoder;

public class Camera {
	
	private final int WIDTH = 800;
	private final int HEIGHT = 600;
	private int displayWidth = WIDTH;
	private int displayHeight = HEIGHT;
		
	public final Rectangle2D fixedFrame = new Rectangle2D.Double(0, 0, WIDTH, HEIGHT);
	public Rectangle2D frame = new Rectangle2D.Double(0, 0, WIDTH, HEIGHT);
	
	private float fadeTotal;
	private float fadeTimer;
	private float fade = 0f;
	
	/** Currently disabled due to errors */
	private boolean upscale = true;
	
	private static Camera screen;
	
	public static void init() {
		screen = new Camera();
	}
	
	public static Camera get() {
		return screen;
	}
	
	public Camera() {
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			updateHeader();
			try {
				Display.setIcon(loadIcon(System.getProperty("resources") + "/ui/AGDG Logo.png"));
			} catch (IOException e) {
				System.out.println("Failed to load icon");
			}
			Display.setResizable(true);
			Display.create();
			
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glOrtho(0, displayWidth, displayHeight, 0, -1, 1);
			GL11.glViewport(0, 0, displayWidth, displayHeight);
		} catch (LWJGLException e) {
			System.err.println("Could not create display.");
		}
		
		frame = new Rectangle2D.Double(0, 0, WIDTH, HEIGHT);
		setFadeTimer(-1f);
	}
	
	public static ByteBuffer[] loadIcon(String ref) throws IOException {
        InputStream fis = ResourceLoader.getResourceAsStream(ref);
        try {
            PNGDecoder decoder = new PNGDecoder(fis);
            ByteBuffer bb = ByteBuffer.allocateDirect(decoder.getWidth()*decoder.getHeight()*4);
            decoder.decode(bb, decoder.getWidth()*4, PNGDecoder.Format.RGBA);
            bb.flip();
            ByteBuffer[] buffer = new ByteBuffer[1];
            buffer[0] = bb;
            return buffer;
        } finally {
            fis.close();
        }
    }
	
	public void update() {
		Display.update();
		Display.sync(200);
		
		if(resized())
			resize();
	}
	
	public void updateHeader() {
		Display.setTitle(Theater.title + "  FPS: " + Theater.fps + " " + Theater.version);
	}
	
	public void draw(GameSetup setup) {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		
		DrawUtils.fillColor(0f, 0.2f, 0.5f, 1f);
		
		// Draw current game setup
		setup.draw();
		
		// Process fading
		fade();
		
		// Draw debug info
		if(Theater.get().debug) {
			Text.drawString(Theater.get().getSetup().getClass().getName(), 15, 15, Color.white);
		}
	}
	
	public boolean resized() {
		if(Display.getWidth() != displayWidth || Display.getHeight() != displayHeight)
			return true;
		
		return false;
	}
	
	public void resize() {
		displayWidth = Display.getWidth();
		displayHeight = Display.getHeight();
		GL11.glViewport(0, 0, displayWidth, displayHeight);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		
		frame = new Rectangle2D.Double(0, 0, displayWidth, displayHeight);
		
		if(upscale) {
			// Upscale
			GL11.glOrtho(0, WIDTH, HEIGHT, 0, -1, 1);
		} else {
			// Increase view window
			GL11.glOrtho(0, displayWidth, displayHeight, 0, -1, 1);
			Theater.get().getSetup().resizeRefresh();
		}
	}

	public float getFrameXScale() {
		if(upscale) {
			return (float) (frame.getWidth() / fixedFrame.getWidth());
		} else {
			return 1f;
		}
	}
	
	public float getFrameYScale() {
		if(upscale) {
			return (float) (frame.getHeight() / fixedFrame.getHeight());
		} else {
			return 1f;
		}
	}
	
	public float getDisplayWidth() {
		return displayWidth / getFrameXScale();
	}
	
	public float getDisplayHeight() {
		return displayHeight / getFrameYScale();
	}
	
	public float getDisplayWidth(float mod) {
		return (displayWidth / mod) / getFrameXScale();
	}
	
	public float getDisplayHeight(float mod) {
		return (displayHeight / mod) / getFrameYScale();
	}
	
	public boolean isFocus() {
		return Display.isActive();
	}
	
	public boolean toBeClosed() {
		if(Display.isCloseRequested()) 
			return true;
		
		return false;
	}
	
	public void close() {
		Display.destroy();
	}
	
	public float getFadeTimer() {
		return fadeTimer;
	}
	
	/**
	 * Set the screen to fade in or out over a specified time.
	 * 
	 * @param fadeTimer Time to fade, positive to fade out, negative to fade in, 0 for no fade
	 */
	public void setFadeTimer(float fadeTimer) {
		this.fadeTimer = fadeTimer;
		this.fadeTotal = fadeTimer;
		
		if(fadeTimer >= 0f)
			fade = 0f;
		else
			fade = 1f;
	}
	
	public void fade() {
		if(fadeTotal > 0f) {
			fade += (1f / fadeTotal) * Theater.getDeltaSpeed(0.025f);
			fadeTimer -= Theater.getDeltaSpeed(0.025f);
		} else if(fadeTotal < 0f) {
			fade -= (1f / Math.abs(fadeTotal)) * Theater.getDeltaSpeed(0.025f);
			fadeTimer += Theater.getDeltaSpeed(0.025f);
		}
		
		if(fadeTotal > 0f ? fadeTimer < 0f : fadeTimer > 0f) {
			fadeTimer = 0f;
			fadeTotal = 0f;
		}
		
		DrawUtils.fillColor(0f, 0f, 0f, fade);
	}
	
}
