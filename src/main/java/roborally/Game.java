package dtu.roborally;

import java.util.ArrayList;

import dtu.roborally.controller.RoboRallyController;
/**
 * This class represents the main logic of the RoboRally game.
 *  It manages player actions, game flow, and win conditions.
 */
public class Game {
    private Board gameboard;
    private ArrayList<Player> players = new ArrayList<>();
    private static Game game;
    private RoboRallyController observer;
    private boolean hasWon = false;

    /**
     * Private constructor to initialize the game with a board layout and players.
     * @param observer Controller responsible for managing game interface
     * @param numberOfPlayers Number of players participating in the game
     */
    private Game(RoboRallyController observer, int numberOfPlayers, int difficulty) {
        this.observer = observer;
        gameboard = new Board(difficulty, numberOfPlayers,observer);
        for(int i = 0; i<numberOfPlayers; i++) {
            players.add(new Player());
        }
    }

    /**
     * Executes the game flow, managing player actions, card priorities, and win conditions.
     * Determines the order in which players play their cards based on priorities.
     * Checks for winning conditions at the end of the game flow class.
     * Notifies the Graphical User Interface (GUI) when a card is played, prompting a view update.
     */
    public void gamePlay() {

        // loop for each round of the game (fixed at 5 cards, maybe we can change later idk)
        for (int round = 0; round < 5; round++) {

            // Game Loop
            for(int playerIndex=0; playerIndex<players.size(); playerIndex++) {

                // Obtain cards from the current player to run, then discard that card
                Player currPlayer = game.getPlayers().get(playerIndex);
                currPlayer.runCard(currPlayer.getCardsChosen().removeFirst());

                // Displays the robot movement
                observer.notifyRobotMove();
            }
        }

        // Check if player has won
        for (int i = 0; i < players.size(); i++) {

            Position p = players.get(i).getRobot().getPosition();
            if (gameboard.getSquare(p.getX(), p.getY()) instanceof EndPosition) {
                victory(i);
                observer.notifyWin(i);
            }
        }
    }

    public void victory(int i) {
        hasWon = true;
    }

    /**
     * checking if the singleton instance of the Game is initialized.
     * @return Singleton instance of the Game class
     */
    public static Game getGame() {
        if(game==null) {
            System.out.println("Game not initialised");
        }

        return game;
    }
    /**
     * Returns the singleton instance of the Game class, creating it if necessary.
     * @param observer Controller responsible for managing game interface
     * @param NPlayers Number of players participating in the game
     * @return Singleton instance of the Game class
     */
    public static Game getGame(RoboRallyController observer, int NPlayers, int difficulty) {
        if(game == null) {
            game = new Game(observer, NPlayers, difficulty);
        }
        return game;
    }


    /**
     * Resets the game state when starting a new game session.
     * Clears the singleton instance, allowing a new game instance to be created.
     */
    public static void endGame() {
        game = null;
    }

    /**
     * Gets a list containing all players currently participating in the game.
     *
     * @return An ArrayList containing all players.
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * Gets the game board associated with the current game instance.
     *
     * @return The game board.
     */
    public Board getBoard() {
        return gameboard;
    }

    public boolean hasWon() {
        return hasWon;
    }
}



