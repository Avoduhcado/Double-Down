package core.setups;

import org.lwjgl.util.vector.Vector4f;

import core.Camera;
import core.Theater;
import core.render.textured.Icon;
import core.render.textured.Image;
import core.ui.Button;
import core.ui.ElementGroup;
import core.ui.InputBox;
import core.ui.TextBox;
import core.ui.overlays.OptionsMenu;
import core.utilities.text.Text;

public class TitleMenu extends GameSetup {

	/** Title logo */
	private Image logo;
	/** A button group contain New Game, Options, and Exit */
	private ElementGroup titleButtons;
	/** The options menu */
	private OptionsMenu optionsMenu;
	
	private String tempSeed;
	private Icon suitIcon;
	
	/**
	 * Title Menu
	 * Set up buttons for game operation.
	 */
	public TitleMenu() {
		// Ensure fading has reset
		Camera.get().setFadeTimer(-0.1f);
		
		// Load title logo
		logo = new Image("Double Down Title");
		
		suitIcon = new Icon("Suit Icon");
		suitIcon.setRotation(new Vector4f(0f, 0f, 1f, 0f), 50f);
		
		// Initialize game buttons
		titleButtons = new ElementGroup();
		titleButtons.add(new Button("New Game", Float.NaN, Camera.get().getDisplayHeight(1.6f), 0, null));
		titleButtons.add(new TextBox("Seed: ", null, Camera.get().getDisplayWidth(2f) - Text.getFont("SYSTEM").getWidth("Seed: "),
				(float) titleButtons.get(0).getBox().getMaxY(), null));
		titleButtons.add(new InputBox(Float.NaN, (float) titleButtons.get(0).getBox().getMaxY(), null, 0, "", 15));
		titleButtons.get(2).setEnabled(false);
		titleButtons.add(new Button("Options", Float.NaN, (float) titleButtons.get(1).getBox().getMaxY(), 0, null));
		titleButtons.add(new Button("Exit", Float.NaN, (float) titleButtons.get(3).getBox().getMaxY(), 0, null));
				
		Theater.get().seed = 0;
	}
	
	@Override
	public void update() {
		// Update buttons
		if(optionsMenu != null) {
			optionsMenu.update();
			// Close options if user chooses to close
			if(optionsMenu.isCloseRequest())
				optionsMenu = null;
		} else {
			titleButtons.update();
			
			if(titleButtons.get(0).isClicked()) {
				// Start game, proceed with state swap
				if(titleButtons.get(2).isEnabled()) {
					tempSeed = ((InputBox) titleButtons.get(2)).getText();
					setSeed();
				}
				Theater.get().cycleSetup();
			} else if(titleButtons.get(3).isClicked()) {
				optionsMenu = new OptionsMenu(20, 20, "Menu2");
			} else if(titleButtons.get(4).isClicked()) {
				// Exit game
				Theater.get().close();
			}
			
			if(titleButtons.get(2).isEnabled()) {
				tempSeed = ((InputBox) titleButtons.get(2)).input();
				setSeed();
			}
		}
		
		suitIcon.animate();
	}
	
	@Override
	public void draw() {
		// Draw logo
		logo.draw(Float.NaN, Camera.get().getDisplayHeight(6f));
		
		suitIcon.draw(Camera.get().getDisplayWidth(2f), Camera.get().getDisplayHeight(2f));
		
		// Draw buttons
		titleButtons.draw();
		
		if(optionsMenu != null) {
			optionsMenu.draw();
		}
	}

	@Override
	public void resizeRefresh() {
		//start.setPosition(Float.NaN, Camera.get().getDisplayHeight(1.6f));
		//seedButton.setPosition(Float.NaN, (float) ((start.getBox().getHeight() / 2f) + start.getBox().getMaxY()));
		//exit.setPosition(Float.NaN, (float) ((start.getBox().getHeight() / 2f) + seedButton.getBox().getMaxY()));
	}
	
	public void setSeed() {
		if(tempSeed != null) {
			if(!tempSeed.matches("")) {
				try {
					Theater.get().seed = Long.parseLong(tempSeed);
				} catch(NumberFormatException e) {
					Theater.get().seed = 0;
					char[] seed = tempSeed.toCharArray();
					for(char s : seed) {
						Theater.get().seed += (int)s;
					}
				}
			}
			titleButtons.get(2).setEnabled(false);
		}
	}

}
