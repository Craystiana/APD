package parallel_quicksort;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class FileManager {
	// Represents the number of elements in the file
	int size;

	// Constructor
	public FileManager(int size) {
		this.size = size;
	}

	// Reads the values from the file that has (size) elements into an array and returns the array
	public int[] ReadFromFile() {
		File filename = new File(Paths.get("").toAbsolutePath().toString() + "\\Inputs\\input" + size + ".txt");
		
		try {
			List<String> strings = new ArrayList<String>();
			// Read all the lines from the file
			List<String> file_content = Files.readAllLines(filename.toPath());
			
			// Separate the numbers from each line
			file_content.parallelStream().forEachOrdered(line -> strings.addAll(Arrays.asList(line.split(" "))));
			
			// Convert the list of strings into a list of integers
			List<Integer> numbers = strings.stream().map(s -> Integer.parseInt(s)).collect(Collectors.toList());
			
			// Convert the list of integers into an int array
			int[] array = numbers.stream().mapToInt(i->i).toArray();
			
			return array;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// Put the values from the array into the file with (size) elements
	public void WriteToFile(int[] array) throws IOException {
		BufferedWriter outputWriter = null;
		outputWriter = new BufferedWriter(new FileWriter(Paths.get("").toAbsolutePath().toString() + "\\Outputs\\output" + size + ".txt"));

		for (int i = 0; i < array.length; i++) {
			outputWriter.write(array[i] + " ");
			if ((i + 1) % 100 == 0) {
				outputWriter.newLine();
			}
		}

		outputWriter.flush();
		outputWriter.close();
	}
}
