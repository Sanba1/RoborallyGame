package dtu.roborally.controller;

import java.util.ArrayList;

import dtu.roborally.utilities.*;
import dtu.roborally.view.*;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Controller for the scene when the robots are moving
 */
public class RobotController {

    private RoboRallyController application;
    private RobotView RV;
    private Stage primaryStage;

    private ArrayList<String> playerIDList;
    private ArrayList<GameLayout> GridPane = new ArrayList<>();
    private Pane root = new Pane();
    private Scene primaryscene = new Scene(root);

    /**
     * constructor that retrieves relevant data and instantiates the view
     * @param application (RoboRallyController)
     * @param primaryStage (Stage)
     * @param playerIDList (ArrayList<String>)
     */
    public RobotController(RoboRallyController application, Stage primaryStage, ArrayList<String> playerIDList) {
        this.application = application;
        this.primaryStage = primaryStage;
        this.playerIDList = playerIDList;

        RV = new RobotView(this);
    }

    /**
     * creates a timeline to display every GameLayout scene by scene, one per second
     */
    public void display() {
    	Timeline timeline = new Timeline();
    	timeline.setCycleCount(GridPane.size());
    	
    	KeyFrame kf = new KeyFrame(Duration.seconds(1), event -> primaryscene.setRoot(GridPane.remove(0)));
    	
    	timeline.getKeyFrames().add(kf);
    	timeline.play();
    	
    	primaryStage.setScene(primaryscene);
    }

    /**
     * called by the application whenever a card is played or a square is interacted with
     * creates a new GameLayout for the move that was just made, and save it in the list layouts
     * layouts will be displayed when all the robots moves were made
     */
    public void addBoardToList() {
    	GridPane.add(RV.initialGUI());
    }

    /**
     * manager called by clicking on the 'continue' button
     * either starts a new phase of picking card, either declare winner to application
     */
    public void endRound() {
    	if(!application.hasWinner()) {
            application.managePlayerTurn(primaryStage, 0);
    	} else {
            application.crownWinner(primaryStage);
    	}
    }

    /**
     * getter for the view to get the player names
     * @return (ArrayList<String>)
     */
    public ArrayList<String> getPlayerIDList(){
        return playerIDList;
    }
}