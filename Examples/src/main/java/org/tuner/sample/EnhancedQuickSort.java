package org.tuner.sample;

import java.util.Stack;

/**
 * User: Pavlo_Ivanenko
 * Date: 8/27/12
 * Time: 1:08 PM
 */
public class EnhancedQuickSort {


    public static void insertionSort(int array[], int low, int n) {
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


    private static int Partition(int x[], int lb, int ub) {
        int a, down, temp, up, pj;
        a = x[lb];
        up = ub;
        down = lb;
        while (down < up) {
            while (x[down] <= a && down < up)
                down = down + 1;
            while (x[up] > a)
                up = up - 1;

            if (down < up) {
                temp = x[down];
                x[down] = x[up];
                x[up] = temp;
            }
        }
        x[lb] = x[up];
        x[up] = a;
        pj = up;
        return (pj);
    }

    public static void enhancedQuick(int[] a, int lb, int ub) {
        Stack S = new Stack();
        //tuneAbleParam name=threshold start=500 stop=5000 step=100
        int threshold = 1;

        addPartitionOrSort(a, lb, ub, S, threshold);


        while (!S.empty()) {
            ub = (Integer) S.pop();
            lb = (Integer) S.pop();
            if (ub <= lb) continue;
            int i = Partition(a, lb, ub);
            if (i - lb > ub - i) {
                addPartitionOrSort(a, lb, i - 1, S, threshold);
            }
            addPartitionOrSort(a, i + 1, ub, S, threshold);
            if (ub - i >= i - lb) {
                addPartitionOrSort(a, lb, i - 1, S, threshold);
            }
        }
    }

    private static void addPartitionOrSort(int[] a, int lb, int ub, Stack s, int threshold) {
        if (ub - lb >= threshold) {
            s.push(lb);
            s.push(ub);
        } else {
            insertionSort(a, lb, ub);
        }
    }


    public static void quick(int[] a, int lb, int ub) {
        Stack S = new Stack();

        S.push(lb);
        S.push(ub);

        while (!S.empty()) {
            ub = (Integer) S.pop();
            lb = (Integer) S.pop();
            if (ub <= lb) continue;
            int i = Partition(a, lb, ub);
            if (i - lb > ub - i) {
                S.push(lb);
                S.push(i - 1);
            }
            S.push(i + 1);
            S.push(ub);
            if (ub - i >= i - lb) {
                S.push(lb);
                S.push(i - 1);
            }
        }
    }

}
