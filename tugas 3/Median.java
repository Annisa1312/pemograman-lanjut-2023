public class Median {
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int n1 = nums1.length, n2 = nums2.length;
        int[] merged = new int[n1 + n2];
        int i = 0, j = 0, k = 0;

        // Merge the two sorted arrays
        while (i < n1 && j < n2) {
            merged[k++] = (nums1[i] <= nums2[j]) ? nums1[i++] : nums2[j++];
        }
        while (i < n1) {
            merged[k++] = nums1[i++];
        }
        while (j < n2) {
            merged[k++] = nums2[j++];
        }

        // Find the median
        int mid = merged.length / 2;
        if (merged.length % 2 == 0) {
            return (merged[mid - 1] + merged[mid]) / 2.0;
        } else {
            return merged[mid];
        }
    }

    public static void main(String[] args) {
        Median medianFinder = new Median();

        // Test case 1
        int[] nums1_1 = {1, 3};
        int[] nums2_1 = {2};
        System.out.println("Median: " + medianFinder.findMedianSortedArrays(nums1_1, nums2_1)); // Output: Median: 2.0

        // Test case 2
        int[] nums1_2 = {1, 2};
        int[] nums2_2 = {3, 4};
        System.out.println("Median: " + medianFinder.findMedianSortedArrays(nums1_2, nums2_2)); // Output: Median: 2.5
    }
}