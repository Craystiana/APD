#include <stdio.h>
#include <stdlib.h>
#include <mpi.h>
#include <time.h>
#include <string>
#include <iostream>
#include <fstream>

#define SIZE 100

using namespace std;

/* (quick) sort slice of array v; slice starts at s and is of length n */
void quicksort(int* array, int start, int length)
{
	// base case
	if (length <= 1)
		return;

	// pick pivot and swap with first element
	int pivot = array[start + length / 2];
	swap(array[start], array[start + length / 2]);

	// partition slice starting at s+1
	int partition = start;
	for (int index = start + 1; index < start + length; index++) {
		if (array[index] < pivot) {
			partition++;
			swap(array[index], array[partition]);
		}
	}

	// swap pivot into place
	swap(array[start], array[partition]);

	// recurse into partition
	quicksort(array, start, partition - start);
	quicksort(array, partition + 1, start + length - partition - 1);
}


/* merge two sorted arrays v1, v2 of lengths n1, n2, respectively */
int* merge(int* left, int left_size, int* right, int right_size)
{
	auto result = (int*)malloc((left_size + right_size) * sizeof(int));
	int i = 0;
	int j = 0;

	for (int k = 0; k < left_size + right_size; k++) {
		if (i >= left_size) {
			result[k] = right[j];
			j++;
		}
		else if (j >= right_size || left[i] < right[j]) {
			result[k] = left[i];
			i++;
		}
		else {
			result[k] = right[j];
			j++;
		}
	}
	return result;
}

int main(int argc, char** argv)
{
	int n = SIZE;
	int* data = NULL;
	int chunk_size, local_chunk_size;
	int* chunk;
	int received_chunk_size;
	int* received_chunk;
	int size, rank;
	MPI_Status status;
	double elapsed_time;

	MPI_Init(&argc, &argv);
	MPI_Comm_size(MPI_COMM_WORLD, &size);
	MPI_Comm_rank(MPI_COMM_WORLD, &rank);

	if (rank == 0) {
		ifstream fin("..\\Inputs\\input" + to_string(SIZE) + ".txt");

		// compute chunk size
		chunk_size = (SIZE % size != 0) ? SIZE / size + 1 : SIZE / size;

		// read data from file
		data = (int*)malloc(size * chunk_size * sizeof(int));
		for (int i = 0; i < SIZE; i++)
			fin >> data[i];

		for (int i = SIZE; i < size * chunk_size; i++)
			data[i] = 0;

		elapsed_time = -MPI_Wtime();
	}

	MPI_Barrier(MPI_COMM_WORLD);

	// broadcast size
	MPI_Bcast(&n, 1, MPI_INT, 0, MPI_COMM_WORLD);

	// compute chunk size
	chunk_size = (SIZE % size != 0) ? SIZE / size + 1 : SIZE / size;

	// scatter data
	chunk = (int*)malloc(chunk_size * sizeof(int));
	MPI_Scatter(data, chunk_size, MPI_INT, chunk, chunk_size, MPI_INT, 0, MPI_COMM_WORLD);
	free(data);
	data = NULL;

	// compute size of own chunk and sort it
	local_chunk_size = rank == size - 1 ? SIZE - chunk_size * rank : chunk_size;
	quicksort(chunk, 0, local_chunk_size);

	for (int step = 1; step < size; step = 2 * step) {
		if (rank % (2 * step) != 0) {
			// id is no multiple of 2*step: send chunk to id-step and exit loop
			MPI_Send(chunk, local_chunk_size, MPI_INT, rank - step, 0, MPI_COMM_WORLD);
			break;
		}
		// id is multiple of 2*step: merge in chunk from id+step (if it exists)
		if (rank + step < size) {
			// compute size of chunk to be received
			received_chunk_size = (SIZE >= chunk_size * (rank + 2 * step)) ? chunk_size * step : SIZE - chunk_size * (rank + step);
			// receive chunk
			received_chunk = (int*)malloc(received_chunk_size * sizeof(int));
			MPI_Recv(received_chunk, received_chunk_size, MPI_INT, rank + step, 0, MPI_COMM_WORLD, &status);
			// merge and free memory
			data = merge(chunk, local_chunk_size, received_chunk, received_chunk_size);
			free(chunk);
			free(received_chunk);
			chunk = data;
			local_chunk_size = local_chunk_size + received_chunk_size;
		}
	}

	// write sorted data to out file and print out timer
	if (rank == 0) {
		elapsed_time += MPI_Wtime();

		// Opening output file to write sorted data
		ofstream fout("..\\Outputs\\output" + to_string(SIZE) + ".txt");

		for (int i = 0; i < local_chunk_size; i++) {
			if (i % 100 == 0 && i != 0) {
				fout << endl;
			}
			fout << chunk[i] << " ";
		}

		// stop the timer
		printf("Quicksort %d ints on %d procs: %f miliseconds\n", SIZE, size, elapsed_time * 1000);
	}

	MPI_Finalize();
	return 0;
}

