package core.setups;

public abstract class GameSetup {

	/** Update the current game state */
	public abstract void update();
	/** Draw the current game state */
	public abstract void draw();
	/** Readjust screen objects to match screen resize */
	public abstract void resizeRefresh();
	
}
