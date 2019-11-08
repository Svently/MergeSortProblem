package edu.beloit.csci335.mergesort;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    static final int ARRAY_LENGTH = 5000000;
    static final long ARRAY_MAX_VALUE = 100000000;
    static final long INSERTION_BREAK_POINT = 64;
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


        var tBeforeFJSort = System.nanoTime();
	    var fjSortedArray = doMergeSort(myArray);
	    var tAfterFJSort = System.nanoTime();
        System.out.println("After sorting for " + (tAfterFJSort - tBeforeFJSort) + " nanoseconds with fork/join, is the array sorted?: " + isSorted(fjSortedArray));
    }
    
    public static long[] doMergeSort(long[] array) {
    	MergeSort mainTask=new MergeSort(array);
    	ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(mainTask);
        return mainTask.join();
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
        if (array.length <= INSERTION_BREAK_POINT) {
            return computeDirectly(array);
        }

        var split = array.length / 2;
        return combineArrays(sortArrayInSingleProcess(Arrays.copyOfRange(array, 0, split)), sortArrayInSingleProcess(Arrays.copyOfRange(array, split, array.length)));
    }

    static long[] computeDirectly(long[] arr) {
        long n = arr.length;
        for (int i = 1; i < n; ++i) {
            long key = arr[i];
            int j = i - 1;

            /* Move elements of arr[0..i-1], that are
               greater than key, to one position ahead
               of their current position */
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j = j - 1;
            }
            arr[j + 1] = key;
        }
        return arr;
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
    
    private static void print(long[] array) {
    	for(int i=0;i<array.length;i++) {
    		System.out.print(array[i]+",");
    	}
    	System.out.println();
    }
}


class MergeSort extends RecursiveTask<long[]> {
	
	long[] arr;
	int start;
	int end;
	
	public MergeSort(long[] array) {
		this.arr=array.clone();
		this.start=0;
		this.end=array.length;
	}

	@Override
	protected long[] compute() {
		if(end-start<=20) {
			return sortArrayInSingleProcess(this.arr);
		}else {
			int middle = (end + start) / 2;
			
			MergeSort subMerge1=new MergeSort(Arrays.copyOfRange(arr, start, middle));
			MergeSort subMerge2=new MergeSort(Arrays.copyOfRange(arr, middle, end));
			
			invokeAll(subMerge1,subMerge2);

			this.arr = combineArrays(subMerge1.join(), subMerge2.join());
		}
		return this.arr;
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
