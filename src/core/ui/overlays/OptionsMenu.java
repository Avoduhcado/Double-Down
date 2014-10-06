package core.ui.overlays;

import java.awt.geom.Rectangle2D;

import org.lwjgl.input.Keyboard;

import core.Camera;
import core.Theater;
import core.audio.Ensemble;
import core.setups.Stage;
import core.ui.Button;
import core.ui.CheckBox;
import core.ui.ElementGroup;
import core.ui.InputBox;
import core.ui.Slider;
import core.utilities.keyboard.Keybinds;
import core.utilities.text.Text;

public class OptionsMenu extends MenuOverlay {

	//private DisplayMode[] displayModes;
	//private String modes = "";
	private Slider volumeSlider;
	private Button close;
	private ElementGroup keybinds = new ElementGroup();
	private CheckBox stoneFieldBox;
	private CheckBox acesHighBox;
	
	public OptionsMenu(float x, float y, String image) {
		super(x, y, image);
		
		this.box = new Rectangle2D.Double(x, y, Camera.get().getDisplayWidth() - (this.frame.getWidth() / 1.5f), Camera.get().getDisplayHeight() - (this.frame.getWidth() / 1.5f));
		
		/*try {
			displayModes = Display.getAvailableDisplayModes();
			for(DisplayMode d : displayModes)
				modes += d.toString() + "\n";
		} catch (LWJGLException e) {
			e.printStackTrace();
		}*/
		
		volumeSlider = new Slider(Camera.get().getDisplayWidth(4.5f), Camera.get().getDisplayHeight(6f), 1f, Ensemble.get().getMasterVolume(), "SliderBG", "SliderValue");
		
		stoneFieldBox = new CheckBox(Camera.get().getDisplayWidth(4.5f), (float) volumeSlider.getBox().getMaxY(), null, "Stone Field");
		stoneFieldBox.setChecked(Theater.get().stoneField);
		acesHighBox = new CheckBox(Camera.get().getDisplayWidth(4.5f), (float) stoneFieldBox.getBox().getMaxY(), null, "Aces High");
		acesHighBox.setChecked(Theater.get().acesHigh);
		if(Theater.get().getSetup() instanceof Stage)
			acesHighBox.setEnabled(false);
		
		float keyX = Camera.get().getDisplayWidth(4f);
		float keyY = 0;
		for(int i = 0; i<Keybinds.values().length; i++) {
			if(!keybinds.isEmpty())
				keyY += keybinds.get(keybinds.size() - 1).getBox().getHeight();
			if(Camera.get().getDisplayHeight(2.5f) + keyY > this.getBox().getHeight() / 1.2f) {
				keyX *= 3f;
				keyY = 0;
			}
				
			keybinds.add(new InputBox(keyX, Camera.get().getDisplayHeight(3.5f) + keyY, null, -1, Keybinds.values()[i].getKey(), 0));
			keybinds.get(keybinds.size() - 1).setEnabled(false);
			((InputBox) keybinds.get(keybinds.size() - 1)).setCentered(false);
		}
		
		close = new Button("Close", Float.NaN, Camera.get().getDisplayHeight(1.2f), 0, null);
	}
	
	@Override
	public void update() {
		close.update();
		for(int i = 0; i<keybinds.size(); i++) {
			keybinds.get(i).update();
			if(keybinds.get(i).isClicked()) {
				keybinds.setEnabledAllExcept(false, keybinds.get(i));
			}
			if(keybinds.get(i).isEnabled() && ((InputBox) keybinds.get(i)).input() != null) {
				Keybinds.values()[i].setKey(Keyboard.getKeyIndex(((InputBox) keybinds.get(i)).getText()));
				keybinds.get(i).setEnabled(false);
			}
		}
		
		volumeSlider.update();
		if(Ensemble.get().getMasterVolume() != volumeSlider.getValue()) {
			Ensemble.get().setMasterVolume(volumeSlider.getValue());
		}
		
		stoneFieldBox.update();
		if(stoneFieldBox.isClicked())
			Theater.get().stoneField = stoneFieldBox.isChecked();
		acesHighBox.update();
		if(acesHighBox.isClicked())
			Theater.get().acesHigh = acesHighBox.isChecked();
	}
	
	@Override
	public void draw() {
		super.draw();
		
		Text.getFont("SYSTEM").drawCenteredString("Options", Camera.get().getDisplayWidth(2f), y, null);
		Text.getFont("SYSTEM").drawCenteredString("Volume", (float) volumeSlider.getBox().getCenterX(),
				volumeSlider.getY() - Text.getFont("SYSTEM").getHeight("Volume"), null);
		volumeSlider.draw();
		
		stoneFieldBox.draw();
		acesHighBox.draw();
		
		for(int i = 0; i<keybinds.size(); i++) {
			Text.getFont("SYSTEM").drawString(Keybinds.values()[i].toString() + ":", 
					keybinds.get(i).getX() - Text.getFont("SYSTEM").getWidth(Keybinds.values()[i].toString() + ": "), keybinds.get(i).getY(), null);
			keybinds.get(i).draw();
		}
		
		close.draw();
	}
	
	@Override
	public boolean isCloseRequest() {
		return close.isClicked();
	}

}
