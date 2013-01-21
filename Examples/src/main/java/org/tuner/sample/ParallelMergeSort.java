package org.tuner.sample;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class ParallelMergeSort {

    private static final ForkJoinPool threadPool = new ForkJoinPool();


    private static final int threshold = 16;

    public static void sort(Comparable[] a) {
        sort(a, 0, a.length - 1);
    }

    public static void sort(Comparable[] a, int lo, int hi) {
        if (hi - lo < threshold) {
            insertionsort(a, lo, hi);
            return;
        }

        Comparable[] tmp = new Comparable[a.length];
        threadPool.invoke(new SortTask(a, tmp, lo, hi));
    }

    /**
     * This class replaces the recursive function that was
     * previously here.
     */
    static class SortTask extends RecursiveAction {
        Comparable[] a;
        Comparable[] tmp;
        int low, high;

        public SortTask(Comparable[] a, Comparable[] tmp, int low, int high) {
            this.a = a;
            this.low = low;
            this.high = high;
            this.tmp = tmp;
        }

        @Override
        protected void compute() {
            //tuneAbleParam name=threshold start=10 stop=500 step=10
            if (high - low < threshold) {
                insertionsort(a, low, high);
                return;
            }

            int m = (low + high) / 2;
            invokeAll(new SortTask(a, tmp, low, m), new SortTask(a, tmp, m + 1, high));
            merge(a, tmp, low, m, high);
        }
    }

    private static void merge(Comparable[] a, Comparable[] b, int lo, int m, int hi) {
        if (a[m].compareTo(a[m + 1]) <= 0)
            return;

        System.arraycopy(a, lo, b, lo, m - lo + 1);

        int i = lo;
        int j = m + 1;
        int k = lo;

        //tuneAbleParam name=threshold start=10 stop=300 step=20
        while (k < j && j <= hi) {
            k = k + 1;
            i = i + 1;
            if (b[i].compareTo(a[j]) <= 0) {

                a[k] = b[i];
            } else {
                a[k] = a[j];
            }
        }

        System.arraycopy(b, i, a, k, j - k);

    }

    private static void insertionsort(Comparable[] a, int lo, int hi) {
        for (int i = lo + 1; i <= hi; i = i + 1) {
            int j = i;
            Comparable t = a[j];
            while (j > lo && t.compareTo(a[j - 1]) < 0) {
                a[j] = a[j - 1];
                j = j - 1;
            }
            a[j] = t;
        }
    }
}