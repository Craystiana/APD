/* C implementation QuickSort */
#include <stdio.h>
#include <iostream>
#include <fstream>
#include <string>

#define SIZE 10000000

using namespace std;

/* This function takes last element as pivot, places
   the pivot element at its correct position in sorted
   array, and places all smaller (smaller than pivot)
   to left of pivot and all greater elements to right
   of pivot */
int partition(int arr[], int low, int high)
{
    int pivot = arr[high];  
    int i = (low - 1); 

    for (int j = low; j <= high - 1; j++)
    {
        if (arr[j] <= pivot)
        {
            i++; 
            swap(arr[i], arr[j]);
        }
    }
    swap(arr[i + 1], arr[high]);
    return (i + 1);
}

/* The main function that implements QuickSort
   arr[] --> Array to be sorted,
   low  --> Starting index,
   high  --> Ending index */
void quickSort(int arr[], int low, int high)
{
    if (low < high)
    {
        int pi = partition(arr, low, high);

        // Separately sort elements before partition and after partition
        quickSort(arr, low, pi - 1);
        quickSort(arr, pi + 1, high);
    }
}

// Driver program to test above functions
int main()
{
    auto arr = (int*)malloc(SIZE * sizeof(int));
    clock_t start;
    clock_t finish;

    ifstream fin(".\\Inputs\\input" + to_string(SIZE) + ".txt");
    ofstream fout(".\\Outputs\\output" + to_string(SIZE) + ".txt");

    for (int i = 0; i < SIZE; i++)
    {
        fin >> arr[i];
    }

    start = clock();

    quickSort(arr, 0, SIZE - 1);

    finish = clock();
    cout << "Time: " << ((double)finish - (double)start) / CLOCKS_PER_SEC * 1000;

    for (int i = 0; i < SIZE; i++)
    {
        if (i % 100 == 0 && i != 0)
            fout << endl;
        fout << arr[i] << " ";
    }

    return 0;
}