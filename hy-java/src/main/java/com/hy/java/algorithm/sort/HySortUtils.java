package com.hy.java.algorithm.sort;

/**
 * QuickSort
 *
 * @author Jie.Hu
 * @date 8/12/21 12:25 PM
 */
public class HySortUtils {

    public static <T extends Comparable> void quickSort(T[] data) {
        quickSort(data, 0, data.length - 1);
    }

    private static <T extends Comparable> void quickSort(T[] data, int low, int high) {
        if (low < high) {
            int index = findIndex(data, low, high);
            quickSort(data, low, index - 1);
            quickSort(data, index + 1, high);
        }
    }

    private static <T extends Comparable> int findIndex(T[] data, int low, int high) {
        T basic = data[low];
        while (low < high) {
            while (low < high && basic.compareTo(data[high]) <= 0) {
                high--;
            }
            data[low] = data[high];
            while (low < high && basic.compareTo(data[low]) >= 0) {
                low++;
            }
            data[high] = data[low];
        }
        data[low] = basic;
        return low;
    }

    public static void main(String[] args) {
        Integer[] arr = {10, 2, 1, 231, 41, 232, 221, 221};
        quickSort(arr);
        for (Integer value : arr) {
            System.out.print(value + " ");
        }
    }

}
