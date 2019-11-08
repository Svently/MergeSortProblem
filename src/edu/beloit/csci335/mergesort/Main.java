package edu.beloit.csci335.mergesort;
import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    static final int ARRAY_LENGTH = 17;
    static final int ARRAY_LENGTH = 1000000;
    static final long ARRAY_MAX_VALUE = 100000000;
    static long[] myArray;
public static void main(String[] args) {
	    var tBeforeSort = System.nanoTime();
	    var sortedArray = sortArrayInSingleProcess(myArray);
	    var tAfterSort = System.nanoTime();

	    print(myArray);
	    doMergeSort();
	    print(myArray);
	    System.out.println("After sorting for " + (tAfterSort - tBeforeSort) + " nanoseconds, is the array sorted?: " + isSorted(sortedArray));
        System.out.println("After sorting for " + (tAfterSort - tBeforeSort) + " nanoseconds, is the array sorted?: " + isSorted(sortedArray));


        var tBeforeFJSort = System.nanoTime();
	    var fjSortedArray = doMergeSort(myArray);
	    var tAfterFJSort = System.nanoTime();
        System.out.println("After sorting for " + (tAfterFJSort - tBeforeFJSort) + " nanoseconds with fork/join, is the array sorted?: " + isSorted(fjSortedArray));
    }

    public static void doMergeSort() {
    	MergeSort mainTask=new MergeSort(myArray,0, myArray.length);
    public static long[] doMergeSort(long[] array) {
    	MergeSort mainTask=new MergeSort(array);
    	ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(mainTask);
        return mainTask.join();
    }

    private static boolean isSorted(long[] array) {
@@ -98,30 +102,33 @@ private static void print(long[] array) {
}


class MergeSort extends RecursiveAction{
class MergeSort extends RecursiveTask<long[]> {

	long[] arr;
	int start;
	int end;

	public MergeSort(long[] array,int start, int end) {
		this.arr=array;
		this.start=start;
		this.end=end;
	public MergeSort(long[] array) {
		this.arr=array.clone();
		this.start=0;
		this.end=array.length;
	}

	@Override
	protected void compute() {
	protected long[] compute() {
		if(end-start<=20) {
			computeDirectly();
		}else {
			int middle = (end + start) / 2;

			MergeSort subMerge1=new MergeSort(arr,start,middle);
			MergeSort subMerge2=new MergeSort(arr,middle,start);
			MergeSort subMerge1=new MergeSort(Arrays.copyOfRange(arr, start, middle));
			MergeSort subMerge2=new MergeSort(Arrays.copyOfRange(arr, middle, end));

			invokeAll(subMerge1,subMerge2);

			this.arr = combineArrays(subMerge1.join(), subMerge2.join());
		}
		return this.arr;
	}

	protected void computeDirectly() {
@@ -140,4 +147,30 @@ protected void computeDirectly() {
            arr[j + 1] = key; 
        } 
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