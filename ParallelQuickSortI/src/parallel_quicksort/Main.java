package parallel_quicksort;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ForkJoinPool;

public class Main {

	public static void main(String[] args) {
		Instant start = Instant.now();
        int array_length = 10000000;
        
		// FileManager object for reading from the input file with *array_length* elements
		FileManager fileManager = new FileManager(array_length);
		int[] array = fileManager.ReadFromFile();
		
		// ForkJoin ThreadPool to keep thread creation as per resources
        ForkJoinPool pool = ForkJoinPool.commonPool();
        
        // Start the first thread in fork join pool for range 0, n-1
        pool.invoke(new ParallelQuickSort(0, array_length - 1, array));
        
        // Write the sorted array to the output file
        try {
			fileManager.WriteToFile(array);
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        // Calculate the running time
        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end); 
        System.out.println("Running time: " + timeElapsed.toMillis() + " miliseconds");
	}
}
