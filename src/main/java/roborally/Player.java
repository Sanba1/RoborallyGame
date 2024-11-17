package dtu.roborally;

import java.util.ArrayList;

public class Player {
    private ArrayList<Card> hand = new ArrayList<>();
    private ArrayList<Card> cardsChosen = new ArrayList<>();
    private Robot robot;

    /**
     * The player clears the previous hand, then draws 9 cards from the shuffled deck
     */
    public void getHand() {
        hand.clear();
        Card.random(hand);
    }

    /**
     * Robot runs commands given by the card
     *
     * @param card (Card)
     */
    public void runCard(Card card) {
        Board board = Game.getGame().getBoard();
        Position newPosition = card.useCard(robot.getPosition());
//        System.out.println(card);
//        System.out.println("orientation: " + robot.getPosition().getOrientation());
//        System.out.println("x: " + robot.getPosition().getX());
//        System.out.println("y: " + robot.getPosition().getY());
        robot.move(board, newPosition);
    }

    /**
     * Place robot into the board onto a start position square.
     *
     * @param o (int)
     * @param x (int)
     * @param y (int)
     */
    public void initializeRobot(int x, int y,int o) {
        robot = new Robot(x, y, o);
    }

    /**
     * Get method to get information of robot stored in Player
     *
     * @return (Robot)
     */
    public Robot getRobot() {
        return robot;
    }

//    /**
//     * Set method for the robot on the board with a position and an orientation
//     *
//     * @param ori orientation (int)
//     * @param x (int)
//     * @param y	(int
//     */
//    public void setRobot(int x, int y, int ori) {
//        robot = new Robot(x, y, ori);
//    }

    /**
     * Get method to check player's hand (used for GUI)
     *
     * @return (ArrayList<Card>)
     */
    public ArrayList<Card> showHand() {
        return hand;
    }

    /**
     * Get method to check player's chosen cards
     *
     * @return (ArrayList<Card>)
     */
    public ArrayList<Card> getCardsChosen() {
        return cardsChosen;
    }
}