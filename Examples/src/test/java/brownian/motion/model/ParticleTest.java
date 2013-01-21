package brownian.motion.model;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * User: Pavlo_Ivanenko
 * Date: 1/21/13
 * Time: 12:51 PM
 */
public class ParticleTest {

    public static final int INITIAL_VECTOR_X = 1;
    public static final int INITIAL_VECTOR_Y = 1;
    public static final int X_START = 2;
    public static final int X_STOP = 10;
    public static final int Y_START = 1;
    public static final int Y_STOP = 20;
    private Particle particle;

    @Before
    public void setUp() throws Exception {
        SpaceBounds spaceBounds = createSpace();
        Vector vector = new Vector(INITIAL_VECTOR_X, INITIAL_VECTOR_Y);
        particle = new Particle(vector, spaceBounds);
    }

    private SpaceBounds createSpace() {
        Interval xBound = new Interval(X_START, X_STOP);
        Interval yBound = new Interval(Y_START, Y_STOP);
        return new SpaceBounds(xBound, yBound);
    }

    @Test
    public void testSimpleMove() throws Exception {


        Coordinate startPoint = new Coordinate(5, 5);
        particle.setCoordinate(startPoint);
        particle.move();
        assertEquals(6, particle.getCoordinate().getX());
        assertEquals(6, particle.getCoordinate().getY());

        particle.move();

        assertEquals(7, particle.getCoordinate().getX());
        assertEquals(7, particle.getCoordinate().getY());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailIfCoordinateIsOutOfBounds() throws Exception {
        Coordinate wrongPoint = new Coordinate(-1, -500);
        particle.setCoordinate(wrongPoint);
    }

    @Test
    public void shouldPermitParticleToBorders() throws Exception {
        particle.setCoordinate(new Coordinate(X_START, Y_START));
        particle.setCoordinate(new Coordinate(X_STOP, Y_STOP));
    }

    @Test
    public void testCollisionWithCorners() throws Exception {
        //collision with the upper-right corner
        particle.setCoordinate(new Coordinate(X_STOP, Y_STOP));
        particle.move();
        Assert.assertEquals(X_STOP - INITIAL_VECTOR_X, particle.getCoordinate().getX());
        Assert.assertEquals(-INITIAL_VECTOR_X, particle.getVector().getX());

        Assert.assertEquals(Y_STOP - INITIAL_VECTOR_Y, particle.getCoordinate().getY());
        Assert.assertEquals(-INITIAL_VECTOR_Y, particle.getVector().getY());


        //collision with the lower-left
        particle.setCoordinate(new Coordinate(X_START, Y_START));
        particle.move();
        Assert.assertEquals(X_START + INITIAL_VECTOR_X, particle.getCoordinate().getX());
        Assert.assertEquals(INITIAL_VECTOR_X, particle.getVector().getX());

        Assert.assertEquals(Y_START + INITIAL_VECTOR_Y, particle.getCoordinate().getY());
        Assert.assertEquals(INITIAL_VECTOR_Y, particle.getVector().getY());
    }

    @Test
    public void testCollisionWithBorders() throws Exception {
        //collision with the upper border
        int offset = 5;
        particle.setCoordinate(new Coordinate(X_STOP - offset, Y_STOP));
        particle.move();
        Assert.assertEquals(X_STOP - offset + INITIAL_VECTOR_X, particle.getCoordinate().getX());
        Assert.assertEquals(INITIAL_VECTOR_X, particle.getVector().getX());

        Assert.assertEquals(Y_STOP - INITIAL_VECTOR_Y, particle.getCoordinate().getY());
        Assert.assertEquals(-INITIAL_VECTOR_Y, particle.getVector().getY());

        //collision with the lower border
        particle.setCoordinate(new Coordinate(X_STOP - offset, Y_START));
        particle.move();
        Assert.assertEquals(X_STOP - offset + INITIAL_VECTOR_X, particle.getCoordinate().getX());
        Assert.assertEquals(INITIAL_VECTOR_X, particle.getVector().getX());

        Assert.assertEquals(Y_START + INITIAL_VECTOR_Y, particle.getCoordinate().getY());
        Assert.assertEquals(INITIAL_VECTOR_Y, particle.getVector().getY());

        //collision with the left border
        particle.setVector(new Vector(-INITIAL_VECTOR_X, INITIAL_VECTOR_Y));
        particle.setCoordinate(new Coordinate(X_START, Y_STOP - offset));
        particle.move();
        Assert.assertEquals(X_START + INITIAL_VECTOR_X, particle.getCoordinate().getX());
        Assert.assertEquals(INITIAL_VECTOR_X, particle.getVector().getX());

        Assert.assertEquals(Y_STOP - offset + INITIAL_VECTOR_Y, particle.getCoordinate().getY());
        Assert.assertEquals(INITIAL_VECTOR_Y, particle.getVector().getY());

        //collision with the right border
        particle.setCoordinate(new Coordinate(X_STOP, Y_STOP - offset));
        particle.move();
        Assert.assertEquals(X_STOP - INITIAL_VECTOR_X, particle.getCoordinate().getX());
        Assert.assertEquals(-INITIAL_VECTOR_X, particle.getVector().getX());

        Assert.assertEquals(Y_STOP - offset + INITIAL_VECTOR_Y, particle.getCoordinate().getY());
        Assert.assertEquals(INITIAL_VECTOR_Y, particle.getVector().getY());
    }
}
