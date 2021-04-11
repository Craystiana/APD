package quicksort;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

public class Main {

	public static void main(String[] args) {
		Instant start = Instant.now();
        
		// FileManager object for reading from the input file with *size* elements
		FileManager fileManager = new FileManager(10000000);
		int[] array = fileManager.ReadFromFile();
		
		// QuickSort object for sorting the array resulted from the input file
        QuickSort sortObject = new QuickSort(array);
        sortObject.quicksort(0, array.length - 1);
        
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
