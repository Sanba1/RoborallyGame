package dtu.roborally.controller;


import dtu.roborally.*;
import dtu.roborally.view.*;

import java.util.ArrayList;
import javafx.stage.Stage;

public class StartPosController {

    private RoboRallyController application;
    private StartPosView SPV;
    private Stage primaryStage;

    private ArrayList<Player> players;

    /**
     * Constructor retrieves relevant data and instantiates the view
     * picks the first random player to pick their SS
     * @param application (RoboRallyController)
     * @param primaryStage (Stage)
     * @param playerIDList (ArrayList<String>)
     */
    public StartPosController(RoboRallyController application, Stage primaryStage, ArrayList<String> playerIDList){
        this.application = application;
        this.primaryStage = primaryStage;
        players = Game.getGame().getPlayers();
        
        int playerID = choosingOrder();
        SPV = new StartPosView(this, playerID, playerIDList);
    }

    /**
     * sets the Scene to the primary stage
     */
    public void display() {
    	primaryStage.setScene(SPV.initialGUI());
    }

    /**
     * called when the player picked his starting position
     * either asks the next player to pick theirs, either starts the pickCard phase
     */
    public void nextPlayer(){
        
        if(!checkingPlayers()){
            int playerID = choosingOrder();
            SPV.nextPlayerStart(playerID);
            display();
        } else {
            application.managePlayerTurn(primaryStage, 0);
        }
    }

    /**
     * picks a random player that still has to choose their SS
     * @return (int)
     */
    public int choosingOrder(){
        System.out.println("RANDOM");
        int order = (int)Math.floor(Math.random()*players.size());
        if(players.get(order).getRobot() != null){ //the player already has a robot
            return choosingOrder();
        } else {
            return order;
        }
    }

    /**
     * checks if all player got their SS
     * @return (boolean)
     */
    public boolean checkingPlayers(){
        for(Player p: players){
            if(p.getRobot() == null){
                return false;
            }
        }
        return true;
    }

    /**
     * asks the application to initialize the player's robot on the chosen SS
     * @param playerID (int)
     * @param x (int)
     * @param y (int)
     */
    public void pickingRobot(int playerID, int x, int y){
        application.initializeRobot(players.get(playerID), x, y);
    }
}