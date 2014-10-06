package core.setups;

import java.awt.geom.Point2D;

import core.Camera;
import core.Theater;
import core.cards.Card;
import core.cards.Deck;
import core.cards.Field;
import core.cards.PairStack;
import core.cards.Suit;
import core.cards.Tower;
import core.render.DrawUtils;
import core.ui.Button;
import core.utilities.mouse.MouseInput;
import core.utilities.text.Text;
import core.utilities.keyboard.Keybinds;

public class Stage extends GameSetup {
	
	private Field[] fields = new Field[4];
	private Tower[] towers = new Tower[4];
	private PairStack pairs;
	private Card selectedCard;
	private String[] endPhrases = {"Congratulations, you cleared all the cards!",
			"So close, only one pair left!",
			"Two pairs left, could do better!",
			"Three pairs, room for improvement.",
			"Four pairs is almost a feat in itself!",
			"Ouch, better luck next time."};
	private int gameover = -1;
	
	private float timer = 0;
	private int moveCount = 0;
	
	private Button replay;
	private Button newDeal;
	
	public Stage() {
		dealDeck();
	}
	
	@Override
	public void update() {
		if(selectedCard != null) {
			selectedCard.setPosition(MouseInput.getMouseX() - (float) (selectedCard.getBox().getWidth() / 2f), 
					MouseInput.getMouseY() - (float) (selectedCard.getBox().getHeight() / 2f));
		}
		
		for(Field f : fields) {
			f.update();
		}
		for(Tower t : towers) {
			t.update();
		}
		pairs.update();
		
		if(replay != null && newDeal != null) {
			replay.update();
			newDeal.update();
			if(replay.isClicked()) {
				clearDeck();
				dealDeck();
			} else if(newDeal.isClicked()) {
				clearDeck();
				Theater.get().seed = 0;
				dealDeck();
			}
		}
		
		if(Camera.get().isFocus() && moveCount > 0 && gameover == -1)
			timer += Theater.getDeltaSpeed(0.025f);
	}

	@Override
	public void draw() {
		DrawUtils.fillColor(0f, 0.5f, 0f, 1f);
		
		for(int x = 0; x<fields.length; x++) {
			fields[x].draw();
		}
		for(int x = 0; x<towers.length; x++) {
			towers[x].draw();
		}
		pairs.draw();
		
		if(replay != null && newDeal != null) {
			replay.draw();
			newDeal.draw();
		}
		
		if(selectedCard != null) {
			selectedCard.draw();
		}
		
		Text.getFont("SYSTEM").drawString("Seed: " + Theater.get().seed, 15, 10, null);
		Text.getFont("SYSTEM").drawString("Press " + Keybinds.CANCEL.getKey() + " to return to title",
				15, 15 + Text.getFont("SYSTEM").getHeight("Seed: "), null);
				
		if(gameover > -1) {
			Text.getFont("SYSTEM").drawCenteredString("GAME OVER", Camera.get().getDisplayWidth(2f), Camera.get().getDisplayHeight(1.8f), null);
			Text.getFont("SYSTEM").drawCenteredString(endPhrases[gameover > 8 ? 5 : gameover / 2], Camera.get().getDisplayWidth(2f), Camera.get().getDisplayHeight(1.65f), null);
		}
		
		Text.getFont("SYSTEM").drawString("Time: " + 
				(((int)timer / 60) < 10 ? "0" + ((int)timer / 60) : ((int)timer / 60)) + ":" + 
				(((int)timer % 60) < 10 ? "0" + ((int)timer % 60) : ((int)timer % 60)), 
				Camera.get().getDisplayWidth(1.23f), Camera.get().getDisplayHeight(1.07f), null);
		Text.getFont("SYSTEM").drawString("Moves: " + moveCount, Camera.get().getDisplayWidth(1.23f), Camera.get().getDisplayHeight(1.12f), null);
	}
	
	public void placeCard() {
		for(Field f : fields) {
			if(f.getBox().contains(MouseInput.getMouse())) {
				if(f.getCards().isEmpty() && towerContains() && !Theater.get().stoneField) {
					returnToField(f);
					return;
				} else if(f.getTopCard() != null && f.getTopCard().equals(selectedCard)) {
					selectedCard = null;
					f.getTopCard().setPosition((float)f.getPosition().getX(), (float)f.getPosition().getY());
					return;
				} else if(f.getTopCard() != null && canPair(f.getTopCard())) {
					makePair(f.getTopCard());
					return;
				}
				
				resetCard();
				return;
			}
		}
		
		for(Tower t : towers) {
			if(t.getBox().contains(MouseInput.getMouse())) {
				if(t.getCards().isEmpty() && canBuildTower(selectedCard.getSuit())) {
					buildTower(t);
					return;
				} else if(!t.getCards().isEmpty() && t.getTopCard().getSuit().equals(selectedCard.getSuit()) &&
						(Theater.get().acesHigh ? (t.getTopCard().getRank() == 0 ? true : (selectedCard.getRank() == 0 ? false : true)) 
								: t.getTopCard().getRank() > selectedCard.getRank())) {
					buildTower(t);
					return;
				} else if(t.getTopCard() != null && canPair(t.getTopCard())	&& !towerContains()) {
					makePair(t.getTopCard());
					return;
				}
				
				resetCard();
				return;
			}
		}
		
		resetCard();
	}
	
	public boolean towerContains() {
		for(Tower t : towers) {
			if(t.getCards().contains(selectedCard))
				return true;
		}
		
		return false;
	}
	
	public boolean canBuildTower(Suit suit) {
		for(Tower t : towers) {
			if(!t.getCards().isEmpty() && t.getTopCard().getSuit().equals(suit))
				return false;
		}
		
		return true;
	}
	
	public void buildTower(Tower tower) {
		tower.addCard(selectedCard);
		
		for(Field f : fields) {
			if(f.getCards().contains(selectedCard)) {
				f.getCards().remove(selectedCard);
			}
		}
		
		selectedCard = null;
		moveCount++;
	}
	
	public void returnToField(Field field) {
		field.addCard(selectedCard);
		field.getTopCard().setPosition((float)field.getPosition().getX(), (float)field.getPosition().getY());
		
		for(Tower t : towers) {
			if(t.getCards().contains(selectedCard)) {
				t.getCards().remove(selectedCard);
			}
		}
		
		selectedCard = null;
		moveCount++;
	}
	
	public boolean canPair(Card card) {
		if(card.getSuit().areOppositeSuits(selectedCard.getSuit()) && ((card.getRank() == 12 ? 0 : card.getRank() + 1) == selectedCard.getRank() 
				|| (card.getRank() == 0 ? 12 : card.getRank() - 1) == selectedCard.getRank()))
			return true;
		return false;
	}
	
	public boolean canPair(Card card1, Card card2) {
		if(card1.getSuit().areOppositeSuits(card2.getSuit()) && ((card1.getRank() == 12 ? 0 : card1.getRank() + 1) == card2.getRank() 
				|| (card1.getRank() == 0 ? 12 : card1.getRank() - 1) == card2.getRank()))
			return true;
		return false;
	}
	
	public void makePair(Card card) {
		pairs.addCard(card);
		pairs.addCard(selectedCard);
		for(Field f : fields) {
			if(f.getCards().contains(card)) {
				f.getCards().remove(card);
			} else if(f.getCards().contains(selectedCard)) {
				f.getCards().remove(selectedCard);
			}
		}
		
		for(Tower t : towers) {
			if(t.getCards().contains(card)) {
				t.getCards().remove(card);
			} else if(t.getCards().contains(selectedCard)) {
				t.getCards().remove(selectedCard);
			}
		}
		
		selectedCard = null;
		moveCount++;
	}
	
	public void resetCard() {
		for(Field f : fields) {
			if(f.getCards().contains(selectedCard)) {
				selectedCard = null;
				f.getTopCard().setSpeed(15f);
				f.getTopCard().setMovement(f.getPosition());
				return;
			}
		}
		
		for(Tower t : towers) {
			if(t.getCards().contains(selectedCard)) {
				selectedCard = null;
				t.getTopCard().setSpeed(15f);
				t.getTopCard().setMovement(t.getPosition());
				return;
			}
		}
	}

	public Card getSelectedCard() {
		return selectedCard;
	}
	
	public void selectCard() {
		for(Field f : fields) {
			if(!f.getCards().isEmpty() && f.getTopCard().getBox().contains(MouseInput.getMouse())) {
				//Ensemble.get().playSoundEffect(new SoundEffect("Card Draw2", 0.35f, false));
				selectedCard = f.getTopCard();
			}
		}
		
		for(Tower t : towers) {
			if(!t.getCards().isEmpty() && t.getTopCard().getBox().contains(MouseInput.getMouse())) {
				//Ensemble.get().playSoundEffect(new SoundEffect("Card Draw2", 0.35f, false));
				selectedCard = t.getTopCard();
			}
		}
	}
	
	public void checkWin() {
		if(!hasRemainingMoves()) {
			gameover = 0;
			for(Field f : fields) {
				gameover += f.getCards().size();
			}
			for(Tower t : towers) {
				gameover += t.getCards().size();
			}
			
			replay = new Button("Replay", Camera.get().getDisplayWidth(2.5f), Camera.get().getDisplayHeight(1.15f), 0, null);
			newDeal = new Button("New Game", Camera.get().getDisplayWidth(1.66f), Camera.get().getDisplayHeight(1.15f), 0, null);
		}
	}
	
	public boolean hasRemainingMoves() {
		for(int x = 0; x<fields.length; x++) {
			for(int y = fields.length - 1; y>x; y--) {
				if(fields[x].getTopCard() != null && fields[y].getTopCard() != null && canPair(fields[x].getTopCard(), fields[y].getTopCard())) {
					return true;
				}
			}
		}
		
		int faceUpCards = 0;
		for(Field f : fields) {
			if(f.getTopCard() != null)
				faceUpCards++;
		}
		for(Tower t : towers) {
			if(t.getTopCard() != null)
				faceUpCards++;
		}

		for(int x = 0; x<fields.length; x++) {
			for(int y = 0; y<towers.length; y++) {
				if(fields[x].getTopCard() != null && towers[y].getTopCard() != null && canPair(fields[x].getTopCard(), towers[y].getTopCard())) {
					//System.out.println(fields[x].getTopCard().toString() + " " + towers[y].getTopCard().toString());
					return true;
				} else if(fields[x].getTopCard() != null && towers[y].getTopCard() != null 
						&& towers[y].getTopCard().getSuit().equals(fields[x].getTopCard().getSuit())
						&& towers[y].getTopCard().getRank() > fields[x].getTopCard().getRank() && faceUpCards != totalCardsLeft()) {
					return true;
				} else if(fields[x].getTopCard() != null && canBuildTower(fields[x].getTopCard().getSuit()) && faceUpCards != totalCardsLeft()) {
					return true;
				} else if(fields[x].getTopCard() == null && towers[y].getTopCard() != null && faceUpCards != totalCardsLeft() && !Theater.get().stoneField) {
					return true;
				}
			}
		}
		
		for(int a = 0; a<fields.length; a++) {
			for(int x = 0; x<towers.length; x++) {
				for(int y = towers.length - 1; y>x; y--) {
					if(fields[a].getTopCard() == null && !Theater.get().stoneField
							&& towers[x].getTopCard() != null && towers[y].getTopCard() != null && canPair(towers[x].getTopCard(), towers[y].getTopCard())) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	public int totalCardsLeft() {
		int leftovers = 0;
		for(Field f : fields) {
			leftovers += f.getCards().size();
		}
		for(Tower t : towers) {
			leftovers += t.getCards().size();
		}
		
		return leftovers;
	}
	
	public void dealDeck() {
		fields = new Field[4];
		towers = new Tower[4];
		gameover = -1;
		timer = 0;
		moveCount = 0;
		
		Deck deck = new Deck();
		if(Theater.get().seed == 0) {
			Theater.get().seed = System.currentTimeMillis();
			deck.shuffle(Theater.get().seed);
		} else
			deck.shuffle(Theater.get().seed);
		
		for(int x = 0; x<4; x++) {
			fields[x] = new Field(Camera.get().getDisplayWidth(3.2f) + (x * 75), Camera.get().getDisplayHeight(3f));
			towers[x] = new Tower(Camera.get().getDisplayWidth(3.2f) + (x * 75), Camera.get().getDisplayHeight(8f));
		}
		pairs = new PairStack(Camera.get().getDisplayWidth(2f) - 37.5f, Camera.get().getDisplayHeight(1.5f));
		
		int i = 0;
		for(int x = 0; x<deck.getCards().length; x++) {
			fields[i].addCard(deck.getCards()[x]);
			i++;
			if(i == 4)
				i = 0;
		}
	}
	
	public void clearDeck() {
		fields = null;
		towers = null;
		pairs = null;
		selectedCard = null;
		replay = null;
		newDeal = null;
	}
	
	@Override
	public void resizeRefresh() {
		for(int x = 0; x<4; x++) {
			fields[x].setPosition(new Point2D.Double(Camera.get().getDisplayWidth(3.2f) + (x * 75), Camera.get().getDisplayHeight(3f)));
			towers[x].setPosition(new Point2D.Double(Camera.get().getDisplayWidth(3.2f) + (x * 75), Camera.get().getDisplayHeight(8f)));
		}
		pairs.setPosition(new Point2D.Double(Camera.get().getDisplayWidth(2f) - 37.5f, Camera.get().getDisplayHeight(1.5f)));
		
		for(Field f : fields) {
			for(Card c : f.getCards()) {
				c.setPosition((float) f.getBox().getX(), (float) f.getBox().getY());
			}
		}
		
		for(Tower t : towers) {
			for(Card c : t.getCards()) {
				c.setPosition((float) t.getBox().getX(), (float) t.getBox().getY());
			}
		}
		
		for(Card c : pairs.getCards()) {
			c.setPosition((float) pairs.getBox().getX(), (float) pairs.getBox().getY());
		}
		
		if(replay != null && newDeal != null) {
			replay.setPosition(Camera.get().getDisplayWidth(2.5f), Camera.get().getDisplayHeight(1.15f));
			newDeal.setPosition(Camera.get().getDisplayWidth(1.66f), Camera.get().getDisplayHeight(1.15f));
		}
	}

}
