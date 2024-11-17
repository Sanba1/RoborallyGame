package dtu.roborally;

/**
 * Class for managing and retrieving the positions of the robot and obstacles.
 */
public class Position
{
    private int x_cor;
    private int y_cor;
    private int ori;

    /**
     * Constructor responsible for initializing the position.
     * @param x_cor = column (int)
     * @param y_cor = rows (int)
     * @param ori (int)
     */
    public Position(int x_cor, int y_cor, int ori)
    {
        this.setX(x_cor);
        this.setY(y_cor);
        this.setOrientation(ori);
    }

    /**
     * Clone a duplicate of the position for the respawn function.
     */
    @Override
    public Position clone()
    {
        return new Position(x_cor,y_cor,ori);
    }

    /**
     * A Get method to retrieve the x-coordinate.
     * @return (int)
     */
    public int getX()
    {
        return x_cor;
    }

    /**
     * A Set method for the x-position
     * @param x_cor (int)
     */
    public void setX(int x_cor)
    {
        this.x_cor = x_cor;
    }

    /**
     * A Get method to retrieve the y-position
     * @return (int)
     */
    public int getY()
    {
        return y_cor;
    }

    /**
     * A Set method for the y-position
     * @param y_cor (int)
     */
    public void setY(int y_cor)
    {
        this.y_cor = y_cor;
    }

    /**
     * A Get method to retrieve the orientation
     * @return (int)
     */
    public int getOrientation()
    {
        return ori;
    }

    /**
     * A Set method for the orientation
     * @param ori (int)
     */
    public void setOrientation(int ori)
    {
        this.ori = ori;
    }
}
