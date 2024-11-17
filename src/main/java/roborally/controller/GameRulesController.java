package dtu.roborally.controller;

import dtu.roborally.view.GameRulesView;
import javafx.stage.Stage;

public class GameRulesController {

    private PlayerAmountController PAC;
    private GameRulesView GRV;
    private Stage primaryStage;

    /**
     * constructor
     * @param application (SetNumberOfPlayersController)
     * @param primaryStage (Stage)
     */
    public GameRulesController(PlayerAmountController application, Stage primaryStage) {
        this.PAC = application;
        this.primaryStage = primaryStage;

        GRV = new GameRulesView(this);

    }

    /**
     * sets the scene on the stage
     */
    public void display(){
        primaryStage.setScene(GRV.initialGUI());
    }

    /**
     * displays the SetNumberOfPlayerView
     */
    public void exit(){
        PAC.display();
    }
}
