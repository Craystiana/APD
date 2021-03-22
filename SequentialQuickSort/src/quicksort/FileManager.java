package quicksort;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

public class FileManager {

	// Represents the number of elements in the file
	int size;

	// Constructor
	public FileManager(int size) {
		this.size = size;
	}

	// Reads the values from the file that has (size) elements into an array and returns the array
	public int[] ReadFromFile() {
		Scanner scanner = null;
		int index = 0;

		try {
			scanner = new Scanner(new File(Paths.get("").toAbsolutePath().toString() + "\\Inputs\\input" + size + ".txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		int[] array = new int[size];

		while (scanner.hasNextInt()) {
			array[index++] = scanner.nextInt();
		}
		
		scanner.close();
		return array;
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
