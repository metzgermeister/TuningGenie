package org.tuner.sample;

import java.util.Arrays;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.Callable;

public class EnhancedQuickSortWrapper implements Callable {


    public void doSort() {
        int[] array = generateArray(20 * 1000 * 1000);
        EnhancedQuickSort.enhancedQuick(array, 0, array.length - 1);
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
        int[] ints = new EnhancedQuickSortWrapper().generateArray(50);
        System.out.println(Arrays.toString(ints));
    }
}
