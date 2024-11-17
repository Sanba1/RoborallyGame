package dtu.roborally;

import java.io.IOException;
import java.util.Random;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;

import dtu.roborally.controller.*;

public class Board {

    //attributes: rows and cols builds the board with the given int, square is the specific square of the board, random is for giving us a random number, observer is a variable used for testing
    private final int rows;
    private final int cols;
    private int difficulty;

    private Square[][] board;


    private final RoboRallyController observer;

    private final Random random = new Random();


    /**
     * Creates a board with a given amount of columns and rows, with starting and end positions equal to amount of players, and one end position
     * @param difficulty (int)
     * @param observer (RoboRallyController)
     * @param PlayerCount (int)
     */
    public Board(int difficulty, int PlayerCount, RoboRallyController observer) {
        // Rows are fixed for now, we can implement the board to change size according to difficulty later idk
        this.rows = 12;
        this.cols = 14;
        this.difficulty = difficulty;
        this.observer = observer;
        this.board = new Square[rows][cols];


        // loading methods
        if (difficulty == 0) {
            entityLoading();
            loadStartPos(PlayerCount);
            loadEndPos();
            loadLasers();
            loadBelts();
        } else {
            loadBoardFromJson(difficulty);
        }
    }

    public int getDifficulty() {
        return difficulty;
    }

    /** Loops through the entire board matrix and assigns random square types, potentially
     * we can limit the randomness to a specific part of the board, and set the obstacles for the other*/
    public void entityLoading() {
        Square S;
        for (int j = 0; j < rows; j++) {
            for (int i = 0; i < cols;i++) {
                S = randomSquare();
                S.setObserver(observer);
                board[j][i] = S;
            }
        }
    }

    public void loadBoardFromJson(int difficulty) {
        ObjectMapper objectMapper = new ObjectMapper();
        String textDiff;
        if (difficulty == 1) {
            textDiff = "easy";
        } else if (difficulty == 2) {
            textDiff = "medium";
        } else if (difficulty == 3) {
            textDiff = "hard";
        } else {
            textDiff = "none";
            System.out.println("error: difficulty not recognized");
        }

        try {
            JsonNode rootNode = objectMapper.readTree(new File("src/main/resources/maps/map1.json"));
            JsonNode levelNode = rootNode.get("levels").get(textDiff);
            int rows = levelNode.get("rows").asInt();
            int cols = levelNode.get("cols").asInt();
            JsonNode squareNode = levelNode.get("tiles");
            Square S;


            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    String tileData = squareNode.get(i).get(j).asText();
                    char squareType = tileData.charAt(0); // Get the first character to determine type

                    switch (squareType) {
                        case '.':
                            S = new Floor();
                            S.setObserver(observer);
                            board[i][j] = S;
                            break;
                        case 'S':
                            S = new StartPosition();
                            S.setObserver(observer);
                            board[i][j] = S;
                            break;
                        case 'E':
                            S = new EndPosition();
                            S.setObserver(observer);
                            board[i][j] = S;
                            break;
                        case 'P':
                            S = new Pit();
                            S.setObserver(observer);
                            board[i][j] = S;
                            break;
                        case 'W':
                            int orientation = Integer.parseInt(tileData.substring(1)); // Get orientation from the second character
                            S = new Wall(orientation);
                            S.setObserver(observer);
                            board[i][j] = S;
                            break;
                        case 'L':
                            int O = Integer.parseInt(tileData.substring(1));
                            S = new LaserPointer(O);
                            S.setObserver(observer);
                            board[i][j] = S;
                            break;
                        case 'B':
                            int Or = Integer.parseInt(tileData.substring(1));
                            S = new LaserBeam(Or);
                            S.setObserver(observer);
                            board[i][j] = S;
                            break;
                        case 'C':
                            int o1 = Integer.parseInt(tileData.substring(1));
                            S = new ConveyorBelt(o1);
                            S.setObserver(observer);
                            board[i][j] = S;
                            break;
                        case 'R':
                            S = new Repair();
                            S.setObserver(observer);
                            board[i][j] = S;
                            break;
                        case'T':
                            S = new Spring();
                            S.setObserver(observer);
                            board[i][j] = S;
                            break;

                        // Add cases for other tile types as needed
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loading starting positions given the amount of players, can also alter which columns the can start at.
     * voids all objects in the surrounding area to avoid spawn trap.
     * @param PlayerCount (int)
     */
    public void loadStartPos(int PlayerCount) {
        for(int i=0; i<PlayerCount; i++) {
            int x,y;
            do {
                x = (int) Math.floor(Math.random()*3); /* change this value to the limit column in which the position can start*/
                y = (int) Math.floor(Math.random()*rows);
            } while(!(board[y][x] instanceof Floor)); /* in this case, if the random position does not have the attribute "floor" it will redo it, this will prevent a spawnpostion placed on another spawn position or obstacle */

            board[y][x] = new StartPosition();  /* we do not have to check for bounds as we defined the start position to be within the bounds */

            if(!(board[y][x+1] 							   instanceof StartPosition)) board[y][x+1] = new Floor(); //this square is always on the board
            if(boundaryCheck(x-1, y) && !(board[y][x-1] instanceof StartPosition)) board[y][x-1] = new Floor();
            if(boundaryCheck(x, y+1) && !(board[y+1][x] instanceof StartPosition)) board[y+1][x] = new Floor(); /* boundary check checks if the square is on board, otherwise no need, the other boolean statement checks that they are not other starting positions. if both conditions pass we convert them to floors to insure the robots can move as stated¨kpj*/
            if(boundaryCheck(x, y-1) && !(board[y-1][x] instanceof StartPosition)) board[y-1][x] = new Floor();

        }
    }
    /**
     * similiar to start position, this one takes no argument as there is only one endposition, It also check for surrounding obstacles
     */
    public void loadEndPos() {
        int x,y;
        do {
            x = (int) Math.floor(cols - Math.random()*3);
            y = (int) Math.floor(Math.random()*rows);
        } while(! (board[y][x] instanceof Floor));

        board[y][x] = new EndPosition();

        if(boundaryCheck(x,y+1)) board[y+1][x] = new Floor();
        if(boundaryCheck(x,y-1)) board[y-1][x] = new Floor();
        if(boundaryCheck(x+1,y)) board[y][x+1] = new Floor();
        board[y][x-1] = new Floor(); //this square is always on the board.
    }



    /**
     *
     * @return (square) random board gen, we can change the weight of each by changing the values for the boolean statements
     */
    public Square randomSquare() {
        double val = random.nextDouble();
        if (val < 0.80) {
            return new Floor();

        } else if (val < 0.84) {
            return new Repair();

        } else if (val < 0.88) {
            return new Wall((int)Math.floor(Math.random()*4));

        } else  {
            return new Pit(); }}




    /**
     * @param x (int)
     * @param y (int)
     * @return (square) returns the square piece at given coords.
     */
    public Square getSquare(int x, int y) {
        return board[rows-y - 1][x];
    }


    /**
     * check if the coords are that of an obstacle that are bigger than on square, this is necessary as smaller obstackles can simply be replaced, but if pieces of a larger obstackle is cut, it would not function.
     * @param x (int)
     * @param y (int)
     * @return (boolean)
     */
    public boolean greaterObstacleCheck(int x, int y) {
        return  board[y][x] instanceof LaserPointer || board[y][x] instanceof LaserBeam || board[y][x] instanceof ConveyorBelt || board[y][x] instanceof EndPosition ||  board[y][x] instanceof StartPosition;
    }

    /**
     * loads the laser obstacle, has a 50% chance of either being vertical or horizontal, will check for greater obstacles in its path, and if failed, will check for new coords.
     */
    public void loadLasers() {
        for(int n = 0; n<2; n++) { //* this loads 2 laser per game */
            int x,y;
            double val;
            val = random.nextDouble();
            if (val > 0.5) { /* check for vertical laser */
                do {
                    x = (int) (3 + Math.floor(Math.random() * (cols - 6)));
                    y = (int) Math.floor(Math.random() * (rows - 4));
                } while ((greaterObstacleCheck(x, y) || greaterObstacleCheck(x, y + 1) || greaterObstacleCheck(x, y + 2) || greaterObstacleCheck(x, y + 3))); /* checks if the chosen x y belongs to advanced opstackles, if true, will chose new xy coords */
                /*
                 *creates a beam of 4 total width
                 */
                board[y][x] = new LaserPointer(0);
                for(int i = 1; i<3; i++) {
                    board[y+i][x] = new LaserBeam(0);
                }
                board[y+3][x] = new LaserPointer(2);

            }
            if (val < 0.5) { /* check for horizontal laser */
                do {
                    x = (int) (3 + Math.floor(Math.random() * (cols - 6)));
                    y = (int) Math.floor(Math.random() * (rows - 4));
                } while ((greaterObstacleCheck(x, y) || greaterObstacleCheck(x + 1, y) || greaterObstacleCheck(x + 2, y) || greaterObstacleCheck(x + 3, y))); /* checks if the chosen x y belongs to advanced opstackles, if true, will chose new xy coords */

                board[y][x] = new LaserPointer(1);
                for (int i = 1; i < 3; i++) {
                    board[y][x + i] = new LaserBeam(1);
                }
                board[y][x + 3] = new LaserPointer(3);
            }
        }
    }

    /**
     * Loads conveyor belts, will do a random number which will be the orientation of the belt, then another squaare is added in the direction in which the belt is running.
     */
    public void loadBelts() {
        for(int i=0; i<3; i++) {
            int x,y, orientation;
            int nextY =0;
            int nextX=0;

            do { //checks if square is available
                x = (int) (3+ Math.floor(Math.random() * (cols-6)));
                y = (int) (Math.floor(Math.random() * rows));
                orientation = (int) Math.floor(Math.random() * 4);

                switch (orientation) {
                    case 0:
                        nextX = x;
                        nextY = y + 1;
                        break;

                    case 1:
                        nextX = x + 1;
                        nextY = y;
                        break;
                    case 2:
                        nextX = x;
                        nextY = y - 1;
                        break;

                    case 3:
                        nextX = x - 1;
                        nextY = y;
                }
            } while(!boundaryCheck(nextX, nextY) || greaterObstacleCheck(x,y) || greaterObstacleCheck(nextX, nextY));

            ConveyorBelt cb1 = new ConveyorBelt(orientation);
            cb1.setObserver(observer);
            board[y][x] = new ConveyorBelt(orientation);

            ConveyorBelt cb2 = new ConveyorBelt(orientation);
            cb2.setObserver(observer);
            board[nextY][nextX] = cb2;
        }
    }

    /**
     * Checks whether the square is within the map
     * @param x (int)
     * @param y (int)
     * @return (boolean)
     */
    public boolean boundaryCheck(int x, int y){
        boolean ValidX = ((x>-1) && (x<cols));
        boolean ValidY = ((y>-1) && (y<rows));
        return (ValidX && ValidY);
    }

    /**
     *
     * @return (int)
     */
    public int getRows() {
        return rows;
    }

    /**
     *
     * @return (int)
     */
    public int getCols() {
        return cols;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public void clearBoard() {
    }

    public void setSquareType(int x, int y, char squareType) {
        Square S;
        switch (squareType) {
            case '.':
                S = new Floor();
                S.setObserver(observer);
                board[y][x] = S;
                break;
            case 'S':
                S = new StartPosition();
                S.setObserver(observer);
                board[y][x] = S;
                break;
            case 'E':
                S = new EndPosition();
                S.setObserver(observer);
                board[y][x] = S;
                break;
            case 'P':
                S = new Pit();
                S.setObserver(observer);
                board[y][x] = S;
                break;
            case 'R':
                S = new Repair();
                S.setObserver(observer);
                board[y][x] = S;
        }
    }
}

