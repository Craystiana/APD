package parallel_quicksort;

import java.util.Random;
import java.util.concurrent.RecursiveTask;

public class ParallelQuickSort extends RecursiveTask<Integer> {
	
	int[] array;
	int low, high;
	
	// Constructor
	public ParallelQuickSort(int low, int high, int array[]) {
		this.low = low;
		this.high = high;
		this.array = array;
	}
	
	/*
	 * This function a random number as pivot, places the pivot element at its
	 * correct position in sorted array, and places all smaller (smaller than pivot)
	 * to left of pivot and all greater elements to right of pivot
	 */
	int partition(int low, int high) {
		int start = low, end = high;
		  
        // Decide random pivot
        int pivot = new Random().nextInt(end - start) + start;
  
        // Swap the pivot with end element of array;
        int last = array[end];
        array[end] = array[pivot];
        array[pivot] = last;
        end--;
  
        // Start partitioning
        while (start <= end) {
  
            if (array[start] <= array[high]) {
                start++;
                continue;
            }
  
            if (array[end] >= array[high]) {
                end--;
                continue;
            }
  
            last = array[end];
            array[end] = array[start];
            array[start] = last;
            end--;
            start++;
        }
  
        // Swap pivot to its correct position
        last = array[end + 1];
        array[end + 1] = array[high];
        array[high] = last;
        return end + 1;
	}

	@Override
	protected Integer compute() {
		// Base case
        if (low >= high)
            return null;
  
		// Find partition
        int partition = partition(low, high);
  
        // Divide array
        ParallelQuickSort left = new ParallelQuickSort(low, partition - 1, array);
  
        ParallelQuickSort right = new ParallelQuickSort(partition + 1, high, array);
  
        // Left subproblem as separate thread
        left.fork();
        right.compute();
  
        // Wait until left thread complete
        left.join();
  
        return null;
	}
}
