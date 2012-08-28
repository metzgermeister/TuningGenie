package org.tuner.sample;

import java.util.Stack;

/**
 * User: Pavlo_Ivanenko
 * Date: 8/27/12
 * Time: 1:08 PM
 */
public class EnhancedQuickSort {


    public static void enhancedQuickSort(int array[], int low, int n) {

        int lo = low;
        int hi = n;
        if (lo >= n) {
            return;
        }
        int mid = array[(lo + hi) / 2];
        while (lo < hi) {
            while (lo < hi && array[lo] < mid) {
                lo = lo + 1;
            }
            while (lo < hi && array[hi] > mid) {
                hi = hi - 1;
            }
            if (lo < hi) {
                int T = array[lo];
                array[lo] = array[hi];
                array[hi] = T;
            }
        }
        if (hi < lo) {
            int T = hi;
            hi = lo;
            lo = T;
        }

        //tuneAbleParam name=threshold start=10 stop=50
        int threshold = 1;

        if (lo - low >= threshold) {
            enhancedQuickSort(array, low, lo);
        } else {
            insertionSort(array, low, lo);
        }
        int rightLow = lo == low ? lo + 1 : lo;

        if (lo - low >= threshold) {
            enhancedQuickSort(array, rightLow, n);
        } else {
            insertionSort(array, rightLow, n);
        }
    }

    public static void quickSort(int array[], int low, int n) {

        int lo = low;
        int hi = n;
        if (lo >= n) {
            return;
        }
        int mid = array[(lo + hi) / 2];
        while (lo < hi) {
            while (lo < hi && array[lo] < mid) {
                lo = lo + 1;
            }
            while (lo < hi && array[hi] > mid) {
                hi = hi - 1;
            }
            if (lo < hi) {
                int T = array[lo];
                array[lo] = array[hi];
                array[hi] = T;
            }
        }
        if (hi < lo) {
            int T = hi;
            hi = lo;
            lo = T;
        }
        quickSort(array, low, lo);
        int rightLow = lo == low ? lo + 1 : lo;
        quickSort(array, rightLow, n);
    }

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

    public static void nonReqQuick(int[] a, int lb, int ub) {
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
