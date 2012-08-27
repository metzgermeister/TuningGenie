package org.tuner.sample;

import java.util.Date;
import java.util.concurrent.Callable;

public class EnhancedQuickSortWrapper implements Callable {

    public void doSort() {

        int[] toSort = new int[0];
        new EnhancedQuickSort().sort(toSort);

    }


    @Override
    public Object call() throws Exception {
        long start = new Date().getTime();
        doSort();
        long stop = new Date().getTime();
        return stop - start;
    }
}
