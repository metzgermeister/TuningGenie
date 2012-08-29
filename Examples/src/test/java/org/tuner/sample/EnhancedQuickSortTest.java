package org.tuner.sample;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.Random;

/**
 * User: Pavlo_Ivanenko
 * Date: 8/28/12
 * Time: 3:46 PM
 */
public class EnhancedQuickSortTest {
    int array[];
    final int goal[] = {1, 3, 4, 9, 10, 12, 13, 99, 120};

    @Before
    public void setUp() throws Exception {
        array = new int[]{12, 9, 4, 99, 120, 1, 3, 10, 13};
    }

    @Test
    public void testEnhancedQuickSort() throws Exception {
        EnhancedQuickSort.enhancedQuick(array, 0, array.length - 1);
        Assert.assertArrayEquals(goal, array);
    }

    @Test
    public void testQuickSort() throws Exception {
        EnhancedQuickSort.quick(array, 0, array.length - 1);
        Assert.assertArrayEquals(goal, array);
    }


    @Test
    public void testInsertionSort() throws Exception {
        EnhancedQuickSort.insertionSort(array, 0, array.length);
        Assert.assertArrayEquals(goal, array);
    }

    @Test
    public void testName() throws Exception {
        Random random = new Random(new Date().getTime());
        for (int i = 0; i < 7; i++) {
            System.out.print(random.nextInt(9999999) + " ,");
        }
    }
}
