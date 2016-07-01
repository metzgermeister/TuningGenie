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

    public void doSort() {
        int[] array = generateArray(2 *  1000 * 1000);
        ParallelMergeSort.parallelMergeSort(array);
    }

    private int[] generateArray(int size) {
        int[] array = new int[size];
        Random random = new Random(42L);
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(size * 100);
        }
        return array;
    }


    @Override
    public Object call() throws Exception {
        long start = new Date().getTime();
        doSort();
        long stop = new Date().getTime();
        return stop - start;
    }

    public static void main(String[] args) {
        new ParallelMergeSortWrapper().doSort();
    }

}
