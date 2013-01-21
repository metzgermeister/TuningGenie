package brownian.motion.model;

/**
 * Immutable
 * <p/>
 * User: Pavlo_Ivanenko
 * Date: 1/21/13
 * Time: 2:20 PM
 */

public class Interval {

    private final int start;
    private final int stop;

    public Interval(int start, int stop) {
        this.start = start;
        this.stop = stop;
    }

    public int getStart() {
        return start;
    }


    public int getStop() {
        return stop;
    }

}
