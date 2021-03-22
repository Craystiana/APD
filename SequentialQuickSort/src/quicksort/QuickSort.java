package quicksort;

public class QuickSort {
	
	int[] array;
	
	// Constructor
	public QuickSort(int array[]) {
		this.array = array;
	}
	
	/*
	 * This function takes last element as pivot, places the pivot element at its
	 * correct position in sorted array, and places all smaller (smaller than pivot)
	 * to left of pivot and all greater elements to right of pivot
	 */
	int partition(int low, int high) {
		int pivot = array[high];
		// index of smaller element
		int index = (low - 1); 
		
		for (int current = low; current < high; current++) {
			// If current element is smaller than or equal to pivot
			if (array[current] <= pivot) {
				index++;

				// swap array[index] and array[current]
				int temp = array[index];
				array[index] = array[current];
				array[current] = temp;
			}
		}

		// swap array[index + 1] and array[high] (or pivot)
		int temp = array[index + 1];
		array[index + 1] = array[high];
		array[high] = temp;

		return index + 1;
	}

	/*
	 * The main function that implements QuickSort() 
	 * array[] -> Array to be sorted
	 * low -> Starting index
	 * high -> Ending index
	 */
	void quicksort(int low, int high) {
		if (low < high) {
			// partition_index is partitioning index, arr[pi] is now at right place
			int partition_index = partition(low, high);

			// Recursively sort elements before partition and after partition
			quicksort(low, partition_index - 1);
			quicksort(partition_index + 1, high);
		}
	}
}
