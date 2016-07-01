package org.tuner.sample;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.Callable;

/**
 * User: Pavlo_Ivanenko
 * Date: 10/15/12
 * Time: 1:52 PM
 */
public class ParallelMergeSortWrapper implements Callable {
    
    private static final int SIZE = 20 * 1000 * 1000;
    
    public void doSort(int[] array) {
        ParallelMergeSort.parallelMergeSort(array);
    }

    private static int[] generateArray(int size) {
        int[] array = new int[size];
        Random random = new Random(42L);
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(size * 100);
        }
        return array;
    }


    @Override
    public Object call() throws Exception {
        int[] array = generateArray(SIZE);
        long start = new Date().getTime();
        doSort(array);
        long stop = new Date().getTime();
        return stop - start;
    }

    public static void main(String[] args) {
        new ParallelMergeSortWrapper().doSort(generateArray(SIZE));
    }

}
