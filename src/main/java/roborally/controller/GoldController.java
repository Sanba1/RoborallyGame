package dtu.roborally.controller;

import dtu.roborally.view.GoldView;
import javafx.stage.Stage;

/**
 * Controller for the victory (Anakin Skywalker) scene
 */
public class GoldController
{
    private RoboRallyController application;
    private Stage primaryStage;
    private GoldView GV;

    /**
     * Constructor responsible for fetching pertinent data and initializing the view
     * @param application (RoboRallyController)
     * @param primaryStage (Stage)
     * @param countGold (String)
     */
    public GoldController(RoboRallyController application, Stage primaryStage, String countGold)
    {
        this.application = application;
        this.primaryStage = primaryStage;
        GV = new GoldView(this, countGold);
    }

    /**
     * triggered by the 'new game' button in the view
     * instructs the application to initiate a new game
     */
    public void newGame()
    {
        application.startApplication(primaryStage);
    }

    /**
     * sets the scene on the primary stage
     */
    public void display()
    {
        primaryStage.setScene(GV.initialGUI());
    }
}
