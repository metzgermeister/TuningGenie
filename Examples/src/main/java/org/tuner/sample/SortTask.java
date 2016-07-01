package org.tuner.sample;

public class SortTask implements Runnable {
    private final int threadCount;
    private final int[] array;
    
    SortTask(int[] array, int threadCount) {
        this.threadCount = threadCount;
        this.array = array;
    }
    
    public void run() {
        ParallelMergeSort.parallelMergeSort(array, threadCount);
    }
}
