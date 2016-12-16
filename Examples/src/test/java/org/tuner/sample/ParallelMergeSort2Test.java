package org.tuner.sample;

import junit.framework.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.Random;

public class ParallelMergeSort2Test {
    
    private static final int SIZE = 10 * 1000 * 1000;
    
    @Test
    public void shouldSort() throws Exception {
        int[] array = new int[SIZE];
        Random random = new Random(42L);
        for (int i = 0; i < SIZE; i++) {
            array[i] = random.nextInt(SIZE * 100);
        }
        
        long start = new Date().getTime();
        ParallelMergeSort2.parallelMergeSort(array);
        long stop = new Date().getTime();
        System.out.println(stop - start);
        
        boolean wrong = false;
        for (int i = 1; i < array.length - 1; i++) {
            if (array[i] > array[i + 1]) {
                wrong = true;
                break;
            }
        }
        Assert.assertFalse(wrong);
        
    }
}