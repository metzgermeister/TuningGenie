package brownian.motion.scheduler;

import brownian.motion.model.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * User: Pavlo_Ivanenko
 * Date: 1/21/13
 * Time: 5:23 PM
 */
public class TasksExecutor {

    public static final int DEFAULT_numberOfParticles = 10;
    public static final int DEFAULT_NUMBER_OF_THREADS = 10;
    public static final long DEFAULT_NUMBER_OF_ASYNCHRONOUS_STEPS = 100 * 50;
    public static final long DEFAULT_TOTAL_NUMBER_OF__STEPS = DEFAULT_NUMBER_OF_ASYNCHRONOUS_STEPS * 5;
    private final int numberOfParticles;
    private final int numberOfThreads;
    private final long numberOfAsynchronousSteps;
    private final long totalNumberOfSteps;
    private List<Particle> particles;
    private SpaceBounds bounds;
    private final Vector vector = new Vector(1, 1);

    Random random = new Random(new Date().getTime());

    public TasksExecutor(int numberOfParticles, int numberOfThreads, long numberOfAsynchronousSteps, long totalNumberOfSteps) {
        this.numberOfThreads = numberOfThreads;
        this.numberOfParticles = numberOfParticles;
        this.numberOfAsynchronousSteps = numberOfAsynchronousSteps;
        this.totalNumberOfSteps = totalNumberOfSteps;
    }


    public void performSimulation() throws InterruptedException {
        initBounds();
        initParticles(numberOfParticles);

        long stepsDone = 0L;
        do {
            simulate();
            stepsDone += numberOfAsynchronousSteps;
//            System.out.println("tic " + stepsDone);
        } while (stepsDone < totalNumberOfSteps);
    }

    private void simulate() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        List<ModellingTask> tasks = generateTasks();
        System.out.println("invoking");
        executorService.invokeAll(tasks);
        System.out.println("invoked");

        executorService.shutdown();
        while (!executorService.awaitTermination(1, TimeUnit.SECONDS)) {
            System.out.println("tic");
        }
    }

    private List<ModellingTask> generateTasks() {
        long start = System.nanoTime();
        ArrayList<ModellingTask> tasks = new ArrayList<ModellingTask>(numberOfParticles);
        for (int i = 0; i < numberOfParticles; i++) {
            tasks.add(new ModellingTask(numberOfAsynchronousSteps, particles.get(i)));

        }
        long stop = System.nanoTime();
        System.out.println("generate tasks: " + (stop - start) / (1000 * 1000));
        return tasks;
    }

    private void initBounds() {
        Interval yBound = new Interval(0, numberOfParticles);
        Interval xBound = new Interval(0, numberOfParticles);
        bounds = new SpaceBounds(xBound, yBound);
    }


    private void initParticles(int numberOfParticles) {
        long start = System.nanoTime();

        particles = new ArrayList<Particle>(numberOfParticles);
        for (int i = 0; i < numberOfParticles; i++) {
            Particle particle = new Particle(vector, bounds);
            Coordinate coordinate = new Coordinate(random.nextInt(numberOfParticles),
                    random.nextInt(numberOfParticles));
            particle.setCoordinate(coordinate);
            particles.add(particle);
        }
        long stop = System.nanoTime();

        System.out.println("init particles: " + (stop - start) / (1000 * 1000));
    }

    public static void main(String[] args) throws InterruptedException {
        int numberOfParticles = DEFAULT_numberOfParticles;
        int numberOfThreads = DEFAULT_NUMBER_OF_THREADS;
        long numberOfAsynchronousSteps = DEFAULT_NUMBER_OF_ASYNCHRONOUS_STEPS;
        long totalNumberOfSteps = DEFAULT_TOTAL_NUMBER_OF__STEPS;
        if (args.length == 4) {
            numberOfParticles = Integer.parseInt(args[0]);
            numberOfThreads = Integer.parseInt(args[1]);
            numberOfAsynchronousSteps = Long.parseLong(args[2]);
            totalNumberOfSteps = Long.parseLong(args[3]);
        }

        System.out.println(String.format("particles:%d threads:%d asyncSteps:%d total_steps:%d", numberOfParticles,
                numberOfThreads, numberOfAsynchronousSteps, totalNumberOfSteps));
        long start = System.nanoTime();
        new TasksExecutor(numberOfParticles, numberOfThreads, numberOfAsynchronousSteps, totalNumberOfSteps)
                .performSimulation();
        long stop = System.nanoTime();


        System.out.println(String.format("particles:%d threads:%d asyncSteps:%d total_steps:%d", numberOfParticles,
                numberOfThreads, numberOfAsynchronousSteps, totalNumberOfSteps));
        System.out.println("total time: " + (stop - start) / (1000 * 1000));
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }
}
