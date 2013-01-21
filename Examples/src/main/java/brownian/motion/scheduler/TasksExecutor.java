package brownian.motion.scheduler;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * User: Pavlo_Ivanenko
 * Date: 1/21/13
 * Time: 5:23 PM
 */
public class TasksExecutor {

    ExecutorService executorService = Executors.newFixedThreadPool(4);

    public void execute() {
        Collection<? extends Callable<Object>> tasks;
//        executorService.invokeAll(tasks)
    }
}
