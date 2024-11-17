package dtu.roborally;

import javafx.util.Pair;

public class Robot
{
    private Position position;
    private Position initialPosition;
    private int lives;
    private boolean isAlive;

    // Final variable is being implemented for the finite life of a robot
    private final int startLives = 5;

    /**
     * A robot is created using a constructor with an orientation and a position
     * @param Ori    = orientation (int)
     * @param initialX (int)
     * @param initialY (int)
     */
    public Robot(int initialX, int initialY, int Ori)
    {
        initialPosition = new Position(initialX, initialY, Ori);
        setPosition(initialPosition.clone());
        this.lives = startLives;
        isAlive = true;
        Square currentSquare = Game.getGame().getBoard().getSquare(initialX, initialY);
        currentSquare.setOccupied(true);
        currentSquare.setOccupiedRobot(this);
    }

    // The moveStep is used to move by one with the help of the direction on the correct axis
    private boolean moveStep(int direction, boolean onX_axis, Board board)
    {
        int NTX;
        int NTY;
        Square nextSquare;

        Square currentSquare = board.getSquare(position.getX(), position.getY());

        if (onX_axis)
        {
            NTX = position.getX() + direction;
            NTY = position.getY();
        }
        else
        {
            NTX = position.getX();
            NTY = position.getY() + direction;
        }

        if (!board.boundaryCheck(NTX, NTY)) {
            System.out.println("Error: The robot cannot leave the board's perimeter.");
            return false;
        }

        nextSquare = board.getSquare(NTX, NTY);

        // Determine the orientation of the movement. It starts facing north and turn clockwise
        int moveOri;

        if (onX_axis)
        {
            // If moving on the X-axis
            moveOri = direction > 0 ? 1 : 3; // Set orientation based on direction
        }
        else
        {
            // If moving on the Y-axis
            moveOri = direction > 0 ? 0 : 2; // Set orientation based on direction
        }

        // Two boolean variables are created showing the +/- values to enter and leave a square
        boolean moveIn = nextSquare.canMoveOrigin(moveOri);
        boolean moveOut = currentSquare.canMoveDestination(moveOri);

        if (!moveIn || !moveOut)
        {
            return false;
        }

        if (nextSquare.isOccupied())
        {
            Position beforeRobotPosition = nextSquare.getOccupiedRobot().getPosition();
            Position afterRobotPosition = beforeRobotPosition.clone();

            // Calculate the new position based on the movement orientation
            if (moveOri == 0) {
                afterRobotPosition.setY(beforeRobotPosition.getY() + 1);
            } else if (moveOri == 1) {
                afterRobotPosition.setX(beforeRobotPosition.getX() + 1);
            } else if (moveOri == 2) {
                afterRobotPosition.setY(beforeRobotPosition.getY() - 1);
            } else {
                afterRobotPosition.setX(beforeRobotPosition.getX() - 1);
            }

            // Move the occupied robot to the new position
            moveIn = nextSquare.getOccupiedRobot().move(board, afterRobotPosition);
        }


        if (moveIn)
        {
            position.setX(NTX);
            position.setY(NTY);
            currentSquare.setOccupied(false);
            nextSquare.setOccupied(true);
            nextSquare.setOccupiedRobot(this);
        }
        return true;
    }

    /**
     * If feasible, relocate the robot to the adjacent cell
     * @param board       (Board)
     * @param newPosition (Position)
     * @return A position
     */
    public boolean move(Board board, Position newPosition)
    {
        int changeInX = newPosition.getX() - position.getX();
        int changeInY = newPosition.getY() - position.getY();
        int changeInOri = newPosition.getOrientation() - position.getOrientation();

        // Check if there's a rotation (change in orientation)
        if (changeInOri != 0) {
            position = newPosition;
            return true;
        }

        // Direction is a 1D variable. It can either be +1 or -1. Used to identify which side it faces
        int direction = (changeInX < 0 || changeInY < 0) ? -1 : 1;


        // A variable (change) is used to show if change occurs
        // the onX_Axis boolean variable is used to show if it is on the X or Y axis
        int change = 0;

        boolean onX_Axis;
        if (changeInX != 0)
        {
            change = changeInX;
            onX_Axis = true;
        }
        else if (changeInY != 0)
        {
            change = changeInY;
            onX_Axis = false;
        }
        else
        {
            onX_Axis = false;
            System.out.println("An error has occurred in calculating the changed position of the robot");
        }

        for (int i = 0; i < Math.abs(change); i++)
        {
            if (!isAlive) return false;
            if (moveStep(direction, onX_Axis, board))
            {
                Square currentSquare = board.getSquare(position.getX(), position.getY());
                currentSquare.interact(this);
            }
            else
            {
                return false;
            }
        }
        return true;
    }

    /**
     * When a robot encounters a repair, it restores one life.
     */
    public void repair()
    {
        lives = 5;
    }

    /**
     * The robot when it gets damaged. If the robot depletes its lives, it perishes.
     * @param damage (int)
     */
    public void damage(int damage)
    {
        lives -= damage;

        if (lives <= 0)
        {
            isAlive = false;
            respawn();
        }
    }


    public void respawn()
    {
        setPosition(initialPosition.clone());
        setLives(startLives);
    }

    /**
     * Adjusting the number of lives.
     * @param lives (int)
     */
    public void setLives(int lives)
    {
        this.lives = lives;
    }

    /**
     * Method for obtaining the current amount of lives.
     * @return (int)
     */
    public int getLives()
    {
        return lives;
    }

    /**
     * Method for obtaining the current position.
     * @return (Position)
     */
    public Position getPosition()
    {
        return position;
    }

    /**
     * Method for obtaining the initial position.
     * @return (Position)
     */
    public Position getStartPosition()
    {
        return initialPosition;
    }

    /**
     * Method for setting the current position
     * @param position (Position)
     */
    public void setPosition(Position position)
    {
        this.position = position;
    }

    /**
     * Verifies if the robot is still alive.
     * @return (boolean)
     */
    public boolean isAlive()
    {
        return isAlive;
    }
}