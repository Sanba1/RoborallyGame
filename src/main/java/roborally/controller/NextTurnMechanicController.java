package dtu.roborally.controller;

import dtu.roborally.view.*;

import javafx.stage.Stage;

/**
 * Controller for managing player switching in the scene
 * invokes the GenerateCardsController to allow the next player to select cards
 */
public class NextTurnMechanicController
{
    private RoboRallyController application;
    private Stage primaryStage;
    private NextTurnMechanicView NTMV;
    private int numberName;

    /**
     * Constructor that retrieves relevant data from the application and initializes the view
     * @param application (RoboRallyController)
     * @param primaryStage (Stage)
     * @param name (String)
     * @param numberName (int)
     */
    public NextTurnMechanicController(RoboRallyController application, Stage primaryStage, String name, int numberName)
    {
        this.application = application;
        this.primaryStage = primaryStage;
        this.numberName = numberName;
        NTMV = new NextTurnMechanicView(this, name);
    }

    /**
     * assigns the scene to the primaryStage
     */
    public void display()
    {
        primaryStage.setScene(NTMV.initialGUI());
    }

    /**
     * triggered when this scene is clicked
     * requests the application to initiate the GenerateCardsController for this player
     */
    public void pickCards() {
        application.pickCards(primaryStage, numberName);
    }

}
