#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <omp.h>

#define SIZE 100

using namespace std;

class Array
{
public:

	int partition(int arr[], int low_index, int high_index) const
	{
		int key = arr[low_index];
		int start = low_index + 1;
		int end = high_index;

		while (true)
		{
			while (start < high_index && key >= arr[start])
				start++;
			while (key < arr[end])
				end--;
			if (start < end)
			{
				swap(arr[start], arr[end]);
			}
			else
			{
				swap(arr[low_index], arr[end]);
				return end;
			}
		}
	}

	void quicksort(int arr[], int low_index, int high_index)
	{
		if (low_index < high_index)
		{
			int pivot = partition(arr, low_index, high_index);

			#pragma omp parallel sections
			{
				#pragma omp section
				{
					quicksort(arr, low_index, pivot - 1);
				}

				#pragma omp section
				{
					quicksort(arr, pivot + 1, high_index);
				}

			}
		}
	}

};

int main()
{
	Array a;
	auto arr = (int*)malloc(SIZE * sizeof(int));
	double start;
	double end;

	ifstream fin("..\\Inputs\\input" + to_string(SIZE) + ".txt");
	ofstream fout("..\\Outputs\\output" + to_string(SIZE) + ".txt");

	for (int i = 0; i < SIZE; i++)
	{
		fin >> arr[i];
	}

	start = omp_get_wtime();

	a.quicksort(arr, 0, SIZE - 1);

	end = omp_get_wtime();
	fout << "Time: " << (end - start) * 1000;

	for (int i = 0; i < SIZE; i++)
	{
		if (i % 100 == 0) {
			fout << endl;
		}
		fout << arr[i] << " ";
	}
}