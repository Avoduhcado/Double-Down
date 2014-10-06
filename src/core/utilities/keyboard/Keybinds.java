package core.utilities.keyboard;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;



public enum Keybinds {
	
	CONFIRM (Keyboard.KEY_RETURN),
	CANCEL (Keyboard.KEY_F),
	PAUSE (Keyboard.KEY_P),
	DEBUG (Keyboard.KEY_F3),
	CANCELTEXT (Keyboard.KEY_TAB),
	EXIT (Keyboard.KEY_ESCAPE);
	
	/** Enum's key mapping */
	private Press key;
	
	/**
	 * Create new key mapping.
	 * @param k integer to map key to
	 */
	Keybinds(int k) {
		this.key = new Press(k);
	}
	
	/**
	 * Check for key interactions.
	 */
	public static void update() {
		for(Keybinds keybinds : Keybinds.values()) {
			keybinds.key.setHeld(keybinds.key.isPressed());
			keybinds.key.update();
		}
	}
	
	/** Key has been pressed. */
	public boolean press() {
		return key.isPressed();
	}
	
	/** Key has been and is currently pressed. */
	public boolean held() {
		return key.isHeld();
	}
	
	/** Key was pressed and is no longer pressed. */
	public boolean clicked() {
		if(key.isPressed() && !key.isHeld())
			return true;
		else
			return false;
	}
	
	/** Key was released. */
	public boolean released() {
		return key.isReleased();
	}
	
	/** Disable specific keys for menu navigation */
	public static void inMenu() {
		Keybinds.PAUSE.key.setDisabled(true);
	}
	
	/** Enable specific keys after closing menu */
	public static void closeMenu() {
		Keybinds.PAUSE.key.setDisabled(false);
	}

	/**
	 * Change a key binding.
	 * 
	 * @param keybind to change key binding
	 */
	public static void changeBind(String keybind) {
		String[] temp = keybind.split("=");
		
		// Handled through Config file
		Keybinds.valueOf(temp[0]).setKey(Keyboard.getKeyIndex(temp[1]));
	}
	
	/**
	 * 
	 * @return The key name of this key press
	 */
	public String getKey() {
		return Keyboard.getKeyName(this.key.getKey());
	}
	
	/**
	 * Set key press to new key.
	 * 
	 * @param k new integer for key mapping
	 */
	public void setKey(int k) {
		this.key = new Press(k);
	}
	
	/**
	 * Destroy and recreate keyboard.
	 * Set all key presses to false.
	 */
	public static void clear() {
		Keyboard.destroy();
		try {
			Keyboard.create();
		} catch (LWJGLException e1) {
			e1.printStackTrace();
		}
		for(Keybinds e : Keybinds.values()) {
			e.key.setPressed(false);
		}
	}
}
