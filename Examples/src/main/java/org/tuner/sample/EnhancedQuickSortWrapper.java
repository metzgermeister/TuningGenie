package org.tuner.sample;

import java.util.concurrent.Callable;

public class EnhancedQuickSortWrapper implements Callable {

    public void doSort() {

        int[] toSort = new int[0];
        new EnhancedQuickSort().sort(toSort);

    }


    @Override
    public Object call() throws Exception {
        long start = System.nanoTime();
        doSort();
        long stop = System.nanoTime();
        return stop - start;
    }
}
