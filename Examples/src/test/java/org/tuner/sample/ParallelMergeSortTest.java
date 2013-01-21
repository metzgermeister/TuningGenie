package org.tuner.sample;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * User: Pavlo_Ivanenko
 * Date: 10/15/12
 * Time: 12:03 PM
 */
public class ParallelMergeSortTest {

    Integer array[];
    final Integer goal[] = {1, 3, 4, 9, 10, 12, 13, 27, 32, 99, 120, 141};

    @Before
    public void setUp() throws Exception {
        array = new Integer[]{12, 9, 27, 4, 99, 120, 1, 3, 10, 141, 32, 13};
    }

    @Test
    public void shouldSort() throws Exception {
        System.out.println(array.length);
        ParallelMergeSort.sort(array);
        Assert.assertArrayEquals(goal, array);
    }


}
