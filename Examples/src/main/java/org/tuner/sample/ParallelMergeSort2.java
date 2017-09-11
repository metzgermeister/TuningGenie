package org.tuner.sample;


import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

public class ParallelMergeSort2 {
    
    private static void insertionSort(int array[], int low, int n) {
        for (int i = low + 1; i < n; i = i + 1) {
            int j = i;
            int B = array[i];
            while ((j > 0) && (array[j - 1] > B)) {
                array[j] = array[j - 1];
                j = j - 1;
            }
            array[j] = B;
        }
    }
    
    public static void parallelMergeSort(int[] array) {
        //tuneAbleParam name=parallelism start=1 stop=2 step=5
        int parallelism = 1;
    
        ForkJoinPool pool = PoolCache.get(parallelism);
        ForkJoinTask<Void> job = pool.submit(new MergeSortTask(array));
        job.join();
        
    }
    
    private static class MergeSortTask extends RecursiveAction {

        private static final long serialVersionUID = -2998426627022866737L;
        private final int[] array;

        MergeSortTask(int arrayToSort[]) {
            this.array = arrayToSort;
        }
        
        @Override
        protected void compute() {
            //tuneAbleParam name=insertionSortThreshold start=10 stop=200 step=10
            int insertionSortThreshold = 100;
            
            //tuneAbleParam name=mergeSortBucketSize start=10000000 stop=10000001 step=500000
            int mergeSortBucketSize = 50000;
            
            if (array.length <= insertionSortThreshold) {
                insertionSort(array, 0, array.length);
            } else if (array.length <= mergeSortBucketSize) {
                sequentialMergeSort(array, insertionSortThreshold);
            } else {
                final int[] left = Arrays.copyOfRange(array, 0, array.length / 2);
                final int[] right = Arrays.copyOfRange(array, array.length / 2, array.length);
                invokeAll(new MergeSortTask(left), new MergeSortTask(right));
                merge(left, right, array);
            }
        }
    }
    
    // Arranges the elements of the given array into sorted order
    // using the "merge sort" algorithm, which splits the array in half,
    // recursively sorts the halves, then merges the sorted halves.
    // It is O(N log N) for all inputs.
    private static void sequentialMergeSort(int[] array, int insertionSortThreshold) {
        if (array.length >= insertionSortThreshold) {
            // split array in half
            int[] left = Arrays.copyOfRange(array, 0, array.length / 2);
            int[] right = Arrays.copyOfRange(array, array.length / 2, array.length);
            
            // sort the halves
            sequentialMergeSort(left, insertionSortThreshold);
            sequentialMergeSort(right, insertionSortThreshold);
            
            // merge them back together
            merge(left, right, array);
        } else {
            insertionSort(array, 0, array.length);
        }
    }
    
    // Combines the contents of sorted left/right arrays into output array a.
    // Assumes that left.length + right.length == a.length.
    
    private static void merge(int[] left, int[] right, int[] a) {
        int i1 = 0;
        int i2 = 0;
        for (int i = 0; i < a.length; i = i + 1) {
            if (i2 >= right.length || (i1 < left.length && left[i1] < right[i2])) {
                a[i] = left[i1];
                i1 = i1 + 1;
            } else {
                a[i] = right[i2];
                i2 = i2 + 1;
            }
        }
    }
}
    
    
