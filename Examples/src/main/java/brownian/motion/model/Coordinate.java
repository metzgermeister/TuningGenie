package brownian.motion.model;

/**
 * User: Pavlo_Ivanenko
 * Date: 1/18/13
 * Time: 12:18 PM
 */
public class Coordinate {

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    private int x;
    private int y;

    public int getX() {
        return x;
    }


    public int getY() {
        return y;
    }


    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
