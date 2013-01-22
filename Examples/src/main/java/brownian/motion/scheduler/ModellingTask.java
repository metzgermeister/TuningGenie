package brownian.motion.scheduler;

import brownian.motion.model.Particle;

import java.util.concurrent.Callable;

/**
 * User: Pavlo_Ivanenko
 * Date: 1/21/13
 * Time: 5:37 PM
 */
public class ModellingTask implements Callable<Boolean> {
    private final long numberOfSteps;
    private final Particle particle;

    public ModellingTask(long numberOfSteps, Particle particle) {
        this.numberOfSteps = numberOfSteps;
        this.particle = particle;
    }

    @Override
    public Boolean call() throws Exception {
        long start = System.nanoTime();
        for (int i = 1; i <= numberOfSteps; i++) {
            particle.move();
        }
        long stop = System.nanoTime();

//        System.out.println("move for : " + numberOfSteps + " steps   " + (stop - start) / (1000 * 1000));
        return Boolean.TRUE;
    }
}
