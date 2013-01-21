package brownian.motion.model;

/**
 * Immutable
 * <p/>
 * User: Pavlo_Ivanenko
 * Date: 1/21/13
 * Time: 12:49 PM
 */
public class SpaceBounds {
    private final Interval xBound;
    private final Interval yBound;

    public SpaceBounds(Interval xBound, Interval yBound) {
        this.yBound = yBound;
        this.xBound = xBound;
    }

    public Interval getyBound() {
        return yBound;
    }

    public Interval getxBound() {
        return xBound;
    }
}
