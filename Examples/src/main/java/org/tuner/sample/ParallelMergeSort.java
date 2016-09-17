package org.tuner.sample;


import java.util.Arrays;

public class ParallelMergeSort {
    
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
    
    public static void parallelMergeSort(int[] a) {
        //int cores = Runtime.getRuntime().availableProcessors();
        //TODO pivanenko thread pool executor
        int threadCountNumber = 1;
        parallelMergeSort(a, threadCountNumber);
    }
    
    public static void parallelMergeSort(int[] a, final int threadCount) {
        //tuneAbleParam name=threshold start=1 stop=2 step=20
        int threshold = 1;
        //tuneAbleParam name=threadCountNumber start=2 stop=16 step=2
        if (a.length <= threshold) {
            insertionSort(a, 0, a.length);
            return;
        }
        
        if (threadCount <= 1) {
            mergeSort(a);
        } else if (a.length >= 2) {
            // split array in half
            final int[] left = Arrays.copyOfRange(a, 0, a.length / 2);
            final int[] right = Arrays.copyOfRange(a, a.length / 2, a.length);
            
            // sort the halves
            Thread lThread = new Thread(new SortTask(left, threadCount / 2));
            Thread rThread = new Thread(new SortTask(right, threadCount / 2));
            
            lThread.start();
            rThread.start();
            
            try {
                lThread.join();
                rThread.join();
            } catch (InterruptedException ie) {
                System.out.println(ie.getMessage());
                System.exit(1);
            }
            
            // merge them back together
            merge(left, right, a);
        }
    }
    
    // Arranges the elements of the given array into sorted order
    // using the "merge sort" algorithm, which splits the array in half,
    // recursively sorts the halves, then merges the sorted halves.
    // It is O(N log N) for all inputs.
    public static void mergeSort(int[] a) {
        if (a.length >= 2) {
            // split array in half
            int[] left = Arrays.copyOfRange(a, 0, a.length / 2);
            int[] right = Arrays.copyOfRange(a, a.length / 2, a.length);
            
            // sort the halves
            mergeSort(left);
            mergeSort(right);
            
            // merge them back together
            merge(left, right, a);
        }
    }
    
    // Combines the contents of sorted left/right arrays into output array a.
    // Assumes that left.length + right.length == a.length.
    public static void merge(int[] left, int[] right, int[] a) {
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
