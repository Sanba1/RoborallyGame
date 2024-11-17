package dtu.roborally.controller;

import java.util.ArrayList;

import dtu.roborally.view.PlayerNameView;
import javafx.stage.Stage;

/**
 * Controller for the second Scene: prompts the players to enter their names
 */
public class PlayerNameController
{
    private RoboRallyController application;
    private Stage primaryStage;
    private PlayerNameView PNV;
    private ArrayList<String> playerIDList;

    /**
     * Controller responsible for fetching necessary data from the application and initializing the view
     * retrieves an empty array named playerNames and updates it with the newly entered names
     * @param application (RoboRallyController)
     * @param primaryStage (Stage)
     * @param playerIDList (ArrayList<String>)
     */
    public PlayerNameController(RoboRallyController application, Stage primaryStage, ArrayList<String> playerIDList)
    {
        this.application = application;
        this.primaryStage = primaryStage;
        this.playerIDList = playerIDList;
        this.PNV = new PlayerNameView(this);
    }

    /**
     * triggered by the '__placeholder__' button
     * requests the application to proceed to the next Scene in order to select the starting positions
     */
    //pickStartingPositions in roborallycontroller is changed to startPositions
    public void startPositions()
    {
        application.pickStartingPositions(primaryStage);
    }

    /**
     * activated when a textField is modified, and incorporates the playerIDList list
     * @param numberName (int) the player's number list
     * @param name (String)
     */
    //addPLayerName to newName
    public void newPlayer(int numberName, String name)
    {
        playerIDList.add(numberName, name);
    }

    /**
     * designates the corresponding scene as the primary stage
     */
    public void display()
    {
        primaryStage.setScene(PNV.initialGUI());
    }

    /**
     * Subtracts the player in question
     * @param numberName (int)
     */
    public void deletePlayer(int numberName)
    {
        playerIDList.remove(numberName);
    }

    /**
     * Starts again RoboRallyController.startApplication
     */
    public void goBack()
    {
        application.startApplication(primaryStage);
    }
}
