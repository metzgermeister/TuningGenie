package brownian.motion.scheduler;

import brownian.motion.model.Particle;

import java.util.concurrent.Callable;

/**
 * User: Pavlo_Ivanenko
 * Date: 1/21/13
 * Time: 5:37 PM
 */
public class ModellingTask implements Callable<Boolean> {
    private final int numberOfSteps;
    private final Particle particle;

    public ModellingTask(int numberOfSteps, Particle particle) {
        this.numberOfSteps = numberOfSteps;
        this.particle = particle;
    }

    @Override
    public Boolean call() throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
