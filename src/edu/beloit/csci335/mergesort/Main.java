package edu.beloit.csci335.mergesort;

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
}
