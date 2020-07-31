public class Main {
    public static void main(String args[]) {
        BinarySearch binarySearch = new BinarySearch();
        int array[] = {6, 9, 14, 44, 55, 88, 100, 250};
        int lengthOfArray = array.length;
        int needToFind = 88;
        int result = binarySearch.search(array, 0, lengthOfArray - 1, needToFind);
        if (result == -1) {
            System.out.println("The item is not present");
        } else {
            System.out.println("The item was found and has an index " + result);
        }
    }
}
