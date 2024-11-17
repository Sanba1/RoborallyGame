package dtu.roborally.controller;

import java.util.ArrayList;

import dtu.roborally.*;
import dtu.roborally.view.SelectCardsView;
import javafx.stage.Stage;

/**
 * Scene to pick cards for corresponding player
 * Is instantiated one time for every player
 * either calls a next player to start their turn or makes the robots move
 */
public class SelectCardsController{
	
	private RoboRallyController application;
	private Stage primaryStage;
	private SelectCardsView SCV;

	private ArrayList<String> playerIDList;
	private int numberName;
	private Player player;

	/**
	 * Constructor to retrieve relevant data from application and instantiate view
	 * @param application (RoboRallyController)
	 * @param primaryStage (Stage)
	 * @param playerIDList (ArrayList<String>)
	 * @param numberName (int) player whose turn it is
	 */
	public SelectCardsController(RoboRallyController application, Stage primaryStage, ArrayList<String> playerIDList, int numberName) {
		this.playerIDList = playerIDList;
		this.application = application;
		this.primaryStage = primaryStage;
		this.numberName = numberName;
		player = Game.getGame().getPlayers().get(numberName);
		this.SCV = new SelectCardsView(this, player.showHand());
	}

	/**
	 * set the corresponding Scene to the primary stage
	 */
	public void display() {
		primaryStage.setScene(SCV.initialGUI());
	}

	/**
	 * called when the user clicks on a card in the hand
	 * adds the card to the card in play of the player
	 * @param card (Card)
	 */
	public void addCardInPlay(Card card) {
		player.getCardsChosen().add(card);
	}

	/**
	 * called when the user clicks on a card in the cardInPlay
	 * removes this card from the player's cardInPlay
	 * @param card (Card)
	 */
	public void removeCardInPlay(Card card) {
		player.getCardsChosen().remove(card);
	}

	/**
	 * asks the application to switch Scene, either next player to pick cards either moving robots
	 */
	public void nextPlayer() {
		application.managePlayerTurn(primaryStage, numberName+1);
	}

	/**
	 * getter for the view to get data about the robot and start positions
	 * used to compute the progress bar
	 * @return (Position[])
	 */
	public Position[] extractPosition(){
		Position[] positions = new Position[2];
		positions[0] = player.getRobot().getPosition();
		positions[1] = player.getRobot().getStartPosition();


		return positions;
	}

	/**
	 * getter for the view to retrieve the player names
	 * @return ArrayList<String>)
	 */
	public ArrayList<String> getPlayerIDList() {
		return playerIDList;
	}
}