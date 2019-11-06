package edu.beloit.csci335.mergesort;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    static final int ARRAY_LENGTH = 10000;
    static final long ARRAY_MAX_VALUE = 100000000;
    static long[] myArray;
    public static void main(String[] args) {
        myArray = new long[ARRAY_LENGTH];
	    for (int i = 0; i < ARRAY_LENGTH; i++) {
            myArray[i] = ThreadLocalRandom.current().nextLong(ARRAY_MAX_VALUE);
        }
	    System.out.println("Before sorting, is the array sorted?: " + isSorted(myArray));

	    var tBeforeSort = System.nanoTime();
	    var sortedArray = sortArrayInSingleProcess(myArray);
	    var tAfterSort = System.nanoTime();

	    System.out.println("After sorting for " + (tAfterSort - tBeforeSort) + " nanoseconds, is the array sorted?: " + isSorted(sortedArray));
    }

    private static void printArray(long[] array) {
        System.out.print("[");
        for (long element : array) {
            System.out.print("" + element + ", ");
        }
        System.out.println("]");
    }

    private static boolean isSorted(long[] array) {
        long last;
        last = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < last) {
                return false;
            }
        }
        return true;
    }

    private static long[] sortArrayInSingleProcess(long[] array) {
        if (array.length == 0) {
            return new long[] {};
        }
        if (array.length == 1) {
            return new long[] {array[0]};
        }

        if (array.length == 2) {
            if (array[0] >= array[1]) {
                return new long[] {array[1], array[0]};
            } else {
                return new long[] {array[0], array[1]};
            }
        }

        var split = array.length / 2;
        return combineArrays(sortArrayInSingleProcess(Arrays.copyOfRange(array, 0, split)), sortArrayInSingleProcess(Arrays.copyOfRange(array, split, array.length)));
    }

    private static long[] combineArrays(long[] a, long[] b) {
        int ia = 0;
        int ib = 0;
        int it = 0;
        long[] target = new long[a.length + b.length];
        while (ia < a.length && ib < b.length) {
            if (a[ia] < b[ib]) {
                target[it] = a[ia];
                ia += 1;
            } else {
                target[it] = b[ib];
                ib += 1;
            }
            it += 1;
        }
        for (; ia < a.length; ia ++) {
            target[it] = a[ia];
            it += 1;
        }
        for (; ib < b.length; ib++) {
            target[it] = b[ib];
            it += 1;
        }
        return target;
    }
}
