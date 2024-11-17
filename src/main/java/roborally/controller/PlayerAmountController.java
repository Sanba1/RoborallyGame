package dtu.roborally.controller;

import dtu.roborally.view.PlayerAmountView;
import javafx.stage.Stage;

/**
 * Controller for the first Scene that sets the number of player
 */
public class PlayerAmountController {
	
	private RoboRallyController application;
	private PlayerAmountView PAV;
	private Stage primaryStage;

	private GameRulesController GRC;

	/**
	 * Constructor that instantiates the PlayerAmountView
	 * @param application (RoboRallyController)
	 * @param primaryStage (Stage)
	 */
	public PlayerAmountController(RoboRallyController application, Stage primaryStage) {
		this.application = application;
		this.primaryStage = primaryStage;
		PAV = new PlayerAmountView(this);

		GRC = new GameRulesController(this, primaryStage);
	}

	/**
	 * called when pushing the button 'start game'
	 * asks the Controller to instantiate the game with the correct number of player
	 * asks the controller to call the next Scene
	 * @param nbOfPlayers (int)
	 */
	public void setName(int nbOfPlayers, int difficulty) {
		application.instantiateGame(difficulty, nbOfPlayers);
		application.setName(primaryStage);
	}

	/**
	 * sets the Scene corresponding to this view on the primary Stage
	 */
	public void display() {
		primaryStage.setScene(PAV.initialGUI());
	}

	/**
	 * to call the rules controller
	 */
	public void displayRules(){
		GRC.display();
	}
	
}