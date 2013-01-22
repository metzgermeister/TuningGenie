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
    public static final long DEFAULT_NUMBER_OF_ASYNCHRONOUS_STEPS = 100 * 50;
    public static final int DEFAULT_NUMBER_OF_THREADS = 10;
    private final int numberOfParticles;
    private final long numberOfAsynchronousSteps;
    private final int numberOfThreads;
    private List<Particle> particles;
    private SpaceBounds bounds;
    private final Vector vector = new Vector(1, 1);

    Random random = new Random(new Date().getTime());

    public TasksExecutor(int numberOfParticles, long numberOfAsynchronousSteps, int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
        this.numberOfParticles = numberOfParticles;
        this.numberOfAsynchronousSteps = numberOfAsynchronousSteps;
    }


    public void performSimulation() throws InterruptedException {
        initBounds();
        initParticles(numberOfParticles);
        simulate();
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
        long numberOfAsynchronousSteps = DEFAULT_NUMBER_OF_ASYNCHRONOUS_STEPS;
        int numberOfThreads = DEFAULT_NUMBER_OF_THREADS;
        if (args.length == 3) {
            numberOfParticles = Integer.parseInt(args[0]);
            numberOfAsynchronousSteps = Integer.parseInt(args[1]);
            numberOfThreads = Integer.parseInt(args[2]);
        }

        System.out.println(String.format("particles:%d steps:%d threads:%d", numberOfParticles,
                numberOfAsynchronousSteps, numberOfThreads));

        long start = System.nanoTime();
        new TasksExecutor(numberOfParticles, numberOfAsynchronousSteps, numberOfThreads).performSimulation();
        long stop = System.nanoTime();


        System.out.println(String.format("particles:%d steps:%d threads:%d", numberOfParticles,
                numberOfAsynchronousSteps, numberOfThreads));
        System.out.println("total time: " + (stop - start) / (1000 * 1000));
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }
}
