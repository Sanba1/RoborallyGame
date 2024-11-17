package dtu.roborally;

import dtu.roborally.controller.RoboRallyController;

import java.util.Random;

public abstract class Square {

    private int damage;
    private String label;
    private boolean occupied;
    private Robot occupiedRobot;
    protected int orientation;
    private RoboRallyController observer;


    /**
     * Constructor for Square, sets the square type give a certain amount of damage
     *
     * @param label (String)
     * @param damage (int)
     */
    public Square(String label, int damage) {
        this.label = label;
        this.damage = damage;
    }

    public void setObserver(RoboRallyController observer){
        this.observer = observer;
    }

    public RoboRallyController getObserver() {
        return observer;
    }



    /**
     * Base class of the interact method, which checks how much damage a square deals.
     * The method gets overridden by subclasses with different scenarios
     *
     * @param robot (Robot)
     */
    public void interact(Robot robot) {
        robot.damage(damage);
    }

    /**
     * Base method of robot's orientation to allow the movement command
     * The method is overridden in scenarios that do not allow, such as a wall
     *
     * @param robotOrientation (int)
     * @return (boolean) whether you can move in or not
     */
    public boolean canMoveOrigin(int robotOrientation) {
        return true;
    }

    /**
     * Base method of destination's orientation to allow the movement command
     * The method is overridden in scenarios that do not allow, such as a wall
     *
     * @param orientation (int)
     * @return (boolean)
     */
    public boolean canMoveDestination(int orientation) {
        return true;
    }

    /**
     * Get method for the label type
     *
     * @return (String)
     */
    public String getLabel() {
        return label;
    }


    /**
     * Get method to see if square occupation
     * @return boolean
     */
    public boolean isOccupied() {
        return occupied;
    }

    /**
     * Set method for square being occupied
     * @param occupied (boolean)
     */
    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    /**
     * Get method to see if square occupation by robot
     * @return occupiedRobot
     */
    public Robot getOccupiedRobot() {
        return occupiedRobot;
    }

    /**
     * Set method to occupy a square with a robot
     * @param occupiedRobot (Robot)
     */
    public void setOccupiedRobot(Robot occupiedRobot) {
        this.occupiedRobot = occupiedRobot;
    }


}

// Square subclasses to give environments to the board
// CHANGE HERE TO ADD SQUARE AND ITS DAMAGE TYPE

class Floor extends Square {
    public Floor() {
        // Replaces the Square(String label, int damage) to assign label and damage
        // label name and damage properties
        super("0 ",0);
    }

    @Override
    public void interact(Robot robot) {
        // do nothing when the square is floor
    }
}

class Pit extends Square {

    public Pit() {
        super("P ",5);
    }
}

class StartPosition extends Square {
    // Start position of robot
    public StartPosition() {
        super("S ", 0);
    }

    @Override
    public void interact(Robot robot) {
        // just like floor, should be safe
    }
}

class EndPosition extends Square {
    public EndPosition() {
        super("E ",0);
    }

    @Override
    public void interact(Robot robot) {
        // same
    }
}

class Repair extends Square {
    public Repair() {
        super("+ ",0);
    }
    @Override
    public void interact(Robot robot) {
        // Send a request to repair robot
        robot.repair();
    }
}


class Wall extends Square {

    public Wall(int orientation) {
        super("W"+orientation,0);
        super.orientation = orientation;
    }

    @Override
    public void interact(Robot robot) {
        // Wall is not dangerous
    }

    /**
     * Override method for wall, checking if robot's and square's orientations match
     *
     * @param robotOrientation (int)
     * @return boolean
     */
    @Override
    public boolean canMoveOrigin(int robotOrientation) {
        return Math.abs(orientation-robotOrientation) != 2;
    }

    /**
     * Override method for wall, checking if robot's and square's orientations match
     *
     * @param robotOrientation (int)
     * @return boolean
     */
    @Override
    public boolean canMoveDestination(int robotOrientation) {
        return orientation != robotOrientation;
    }
}

class ConveyorBelt extends Square {

    int orientation;
    public ConveyorBelt(int orientation) {
        super("C"+orientation,0);
        this.orientation = orientation;
    }

    /**
     * Overridden interact method for the conveyor belt Square, moves the robot to the square in front of the conveyor belt depending on the belt's rotation
     *
     * @param robot (Robot)
     */
    @Override
    public void interact(Robot robot) {
        super.getObserver().notifyRobotMove();
        Position position = robot.getPosition();
        Board board = Game.getGame().getBoard();
        int x,y,roboOrientation;
        int ynew = 0;
        int xnew = 0;
        x = position.getX();
        y = position.getY();
        roboOrientation = position.getOrientation();

        switch(orientation) {
            case 0: // North
                xnew = x;
                ynew = y-1;
                break;
            case 1: // East
                xnew = x+1;
                ynew = y;
                break;
            case 2: // South
                xnew = x;
                ynew = y+1;
                break;
            case 3: // West
                xnew = x-1;
                ynew = y;
                break;
        }

        robot.move(board, new Position(xnew, ynew, roboOrientation));
    }
}

class LaserPointer extends Square {
    int orientation;
    public LaserPointer(int orientation) {
        super("L" + orientation, 0);
        this.orientation = orientation;
    }
    /**
     * Override canMoveOrigin method for LaserShooter, returns false as the robot should not be able to move onto the laser shooter
     *
     * @param robotOrientation (int)
     * @return (boolean)
     */
    @Override
    public boolean canMoveOrigin(int robotOrientation) {
        return false;
    }
}

class LaserBeam extends Square {
    public LaserBeam(int orientation) {
        super("B" + orientation, 3);
        this.orientation = orientation;
    }
}

class Spring extends Square {
    public Spring() {
        super("T ", 0);
    }

    @Override
    public void interact(Robot robot) {
        if (robot == null) return; // Ensure there is a robot to interact with

        Board board = Game.getGame().getBoard();
        Random rnd = new Random();
        Position newPosition;
        int newX, newY;

        do {
            // Randomly generate new x and y coordinates within the board limits
            newX = rnd.nextInt(board.getCols());
            newY = rnd.nextInt(board.getRows());

            // Check if the new position is on the board, valid, and unoccupied
        } while (!board.boundaryCheck(newX, newY) || board.getSquare(newX, newY).isOccupied() || !board.getSquare(newX, newY).canMoveDestination(robot.getPosition().getOrientation()));

        // Set the robot's new position
        newPosition = new Position(newX, newY, robot.getPosition().getOrientation());
        robot.setPosition(newPosition);

    }
}