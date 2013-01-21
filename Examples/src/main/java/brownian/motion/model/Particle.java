package brownian.motion.model;

/**
 * User: Pavlo_Ivanenko
 * Date: 1/18/13
 * Time: 12:19 PM
 */
public class Particle {
    private Coordinate coordinate;
    private Vector vector;
    private final SpaceBounds bounds;

    public Particle(Vector vector, SpaceBounds bounds) {
        this.vector = vector;
        this.bounds = bounds;
    }

    public void move() {
        if (coordinate.getX() == bounds.getxBound().getStop() || coordinate.getX() == bounds.getxBound().getStart()) {
            vector.setX(-vector.getX());
        }
        if (coordinate.getY() == bounds.getyBound().getStop() || coordinate.getY() == bounds.getyBound().getStart()) {
            vector.setY(-vector.getY());
        }

        Coordinate newCoordinate = new Coordinate(coordinate.getX() + vector.getX(), coordinate.getY() + vector.getY());
        if (isOutOfBounds(newCoordinate)) {
            return;
        }
        this.setCoordinate(newCoordinate);

    }


    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        if (isOutOfBounds(coordinate)) {
            throw new IllegalArgumentException("Coordinate is out of bounds");
        }
        this.coordinate = coordinate;
    }

    private boolean isOutOfBounds(Coordinate coordinate) {
        return coordinate.getX() < bounds.getxBound().getStart() ||
                coordinate.getX() > bounds.getxBound().getStop() ||
                coordinate.getY() < bounds.getyBound().getStart() || coordinate.getY() >
                bounds.getyBound().getStop();

    }

    public Vector getVector() {
        return vector;
    }

    public void setVector(Vector vector) {
        this.vector = vector;
    }


}
