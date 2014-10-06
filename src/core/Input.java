package core;

import java.awt.geom.Point2D;

import org.lwjgl.input.Mouse;

import core.setups.GameSetup;
import core.setups.Stage;
import core.utilities.keyboard.Keybinds;
import core.utilities.mouse.MouseInput;

public class Input {
	
	public static boolean mousePressed;
	public static boolean mouseHeld;
	public static Point2D mouseClick;
	public static Point2D mouseRelease;
	
	public static void checkInput(GameSetup setup) {
		Keybinds.update();
		
		if(Keybinds.DEBUG.clicked()) {
			Theater.get().debug = !Theater.get().debug;
		}
		
		if(Mouse.isButtonDown(0) || Mouse.isButtonDown(1)) {
			mousePress();
		} else {
			mouseRelease();
		}
		
		if(setup instanceof Stage) {
			if(Keybinds.PAUSE.clicked()) {
				Theater.get().pause();
			}
			if(Keybinds.CANCEL.clicked()) {
				Theater.get().cycleSetup();
			}
			
			if(mousePressed && !mouseHeld) {
				if(((Stage) setup).getSelectedCard() == null) {
					((Stage) setup).selectCard();
				} else {
					((Stage) setup).placeCard();
					((Stage) setup).checkWin();
				}
			}
		}
	}
	
	public static void mousePress() {
		if(!mousePressed) {
			mousePressed = true;
			mouseClick = MouseInput.getMouse();
		} else
			mouseHeld = true;
	}
	
	public static void mouseRelease() {
		if(mousePressed) {
			mousePressed = false;
			mouseHeld = false;
			mouseRelease = MouseInput.getMouse();
		}
	}
	
	public static void clearMouse() {
		mouseClick = null;
		mouseRelease = null;
	}
	
	public static Point2D getCurrentMouse() {
		return MouseInput.getMouse();
	}
	
}
