package brownian.motion.scheduler;

import brownian.motion.model.Particle;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * User: Pavlo_Ivanenko
 * Date: 1/21/13
 * Time: 5:40 PM
 */
public class ModellingTaskTest {
    @Test
    public void testCall() throws Exception {
        Particle particleMock = Mockito.mock(Particle.class);
        int numberOfSteps = 7;
        ModellingTask modellingTask = new ModellingTask(numberOfSteps, particleMock);
        modellingTask.call();
        Mockito.verify(particleMock, Mockito.times(numberOfSteps)).move();

    }


}
