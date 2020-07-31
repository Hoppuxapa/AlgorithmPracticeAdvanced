
class BinarySearch {
    public int search(int arr[], int low, int arrayLength, int findIt) {
        if (arrayLength >= low) {
            int mid = low + (arrayLength - low) / 2;

            if (arr[mid] == findIt) {
                return mid;
            }

            if (arr[mid] > findIt) {
                return search(arr, low, mid - 1, findIt);
            }
            return search(arr, mid + 1, arrayLength, findIt);
        }

        return -1;
    }


} 