package dtu.roborally.controller;
import dtu.roborally.Game;
import dtu.roborally.Player;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.util.ArrayList;

public class RoboRallyController extends Application {

    private PlayerAmountController PAC;
    private PlayerNameController PNC;
    private StartPosController SPC;
    private SelectCardsController SCC;
    private NextTurnMechanicController NTMC;
    private RobotController RC;
    private GoldController GC;
    private MusicPlayerController MP;

    private boolean hasWinner;
    private String nameOfWinner;
    private ArrayList<String> playerNames;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Initializes and launches the JavaFX application. This method sets up the primary stage
     * with its configurations and triggers the start of the game sequence through the first controller.
     * @param primaryStage (Stage)
     */

    @Override
    public void start(Stage primaryStage) {
        setStage(primaryStage);
        startApplication(primaryStage);
        primaryStage.show();

        // Initialize music player
        MP = new MusicPlayerController("src/main/resources/music/medieval.mp3");
        MP.play();
    }

    /**
     *  Configures the primary stage with a pre-defined title, width, and height.
     *  This method prepares the stage to display the game window with specified dimensions and sets a title for the window.
     * @param primaryStage (Stage)
     */
    public void setStage(Stage primaryStage) {
        primaryStage.setTitle("RoboRally");
        primaryStage.setWidth(1300);
        primaryStage.setHeight(830);

    }
    /**
     * Initializes the game by setting up the first controller, which is responsible for determining the number of players.
     * This method serves as a starting point for the game's sequence of events and resets any necessary states if starting a new game.
     * @param primaryStage (Stage)
     */
    public void startApplication(Stage primaryStage) {
        hasWinner = false;
        Game.endGame();
        playerNames = new ArrayList<>();


        PAC = new PlayerAmountController(this, primaryStage);
        PAC.display();

    }

    /**
     * Creates a game instance with the specified number of players and difficulty setting.
     * @param nbOfPlayers (int)
     * @param difficulty (int)
     */

    public void instantiateGame(int nbOfPlayers, int difficulty) {
        Game.getGame(this, nbOfPlayers, difficulty);
    }

    /**
     *  Initiates the process of entering player names
     * @param primaryStage (Stage)
     */
    public void setName(Stage primaryStage) {
        PNC = new PlayerNameController(this, primaryStage, playerNames);
        PNC.display();
    }






    // CHANGE BELOW

    /**
     * This will call initialiseRobot and initialise a new setStartingPositionController.
     * @param primaryStage (Stage)
     */
    public void pickStartingPositions(Stage primaryStage){
        SPC = new StartPosController(this, primaryStage, playerNames);
        SPC.display();
    }


    /**
     * This method is used for setting the initial state of each player's robot before the game begins.
     * @param player (Player)
     * @param x (int)
     * @param y (int)
     */
    public void initializeRobot(Player player, int x, int y){
        player.initializeRobot(x, y, 1);
    }

    /**
     * Manages the sequence of players' turns, either moving to the next player or shifting to the robot movement phase.
     * This method checks player progression, manages game flow, and transitions between different phases of the game.
     * @param primaryStage (Stage)
     * @param playerIndex (int)
     */
    public void managePlayerTurn(Stage primaryStage, int playerIndex) {
        if(playerIndex < playerNames.size() && !hasWinner) {
            nextPlayer(primaryStage, playerNames.get(playerIndex), playerIndex);
        } else {
            RC = new RobotController(this, primaryStage, playerNames);
            Game.getGame().gamePlay();
            RC.display();
        }
    }

    /**
     * Transition to the next player's turn for card selection, handled by NextPlayerController
     * @param primaryStage (Stage)
     * @param playerName (String)
     * @param playerIndex (int)
     */
    public void nextPlayer(Stage primaryStage, String playerName, int playerIndex) {
        NTMC = new NextTurnMechanicController(this, primaryStage, playerName, playerIndex);
        NTMC.display();
    }

    /**
     * This method initiates the card selection process, allowing players to choose their actions for the round.
     * @param primaryStage (int)
     * @param playerIndex (int)
     */
    public void pickCards(Stage primaryStage, int playerIndex) {
        Game.getGame().getPlayers().get(playerIndex).getHand();
        SCC = new SelectCardsController(this, primaryStage, playerNames, playerIndex);
        SCC.display();
    }

    /**
     * Notifies the MovingRobotsController to update its view based on the robots' movements.
     */
    public void notifyRobotMove() {
        RC.addBoardToList();
    }


    /**
     * This method sets the game's winner's name used for displaying the final result
     * @param playerIndex (int)
     */
    public void notifyWin(int playerIndex) {
        nameOfWinner = playerNames.get(playerIndex);
        hasWinner = true;
    }

    /**
     * Displays the winning screen and concludes the game
     * @param primaryStage (int)
     */
    public void crownWinner(Stage primaryStage) {
        GC = new GoldController(this, primaryStage, nameOfWinner);
        GC.display();
    }

    /**
     * This method is used primarily for checking the game's state
     * @return winner
     */
    public boolean hasWinner() {
        return hasWinner;
    }


}