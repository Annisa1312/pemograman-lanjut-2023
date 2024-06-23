import java.util.ArrayList;
import java.util.Scanner;

public class tugas2 {
    public static void main(String[] args) {
        Scanner inputTerminal = new Scanner(System.in);

        // Input matrix size
        System.out.print("Masukkan panjang baris/kolom: ");
        int panjangMatriks = inputTerminal.nextInt();

        // Declare matrix using ArrayList
        ArrayList<ArrayList<Integer>> matriks1 = new ArrayList<>();
        ArrayList<ArrayList<Integer>> matriksTranspose = new ArrayList<>();
        ArrayList<ArrayList<Integer>> matriksSum = new ArrayList<>();

        // Initialize matrices
        for (int i = 0; i < panjangMatriks; i++) {
            matriks1.add(new ArrayList<>());
            matriksTranspose.add(new ArrayList<>());
            matriksSum.add(new ArrayList<>());
            for (int j = 0; j < panjangMatriks; j++) {
                matriks1.get(i).add(0);
                matriksTranspose.get(i).add(0);
                matriksSum.get(i).add(0);
            }
        }

        // Input matrix values
        for (int i = 0; i < panjangMatriks; i++) {
            for (int j = 0; j < panjangMatriks; j++) {
                System.out.print("Baris ke " + (i + 1) + " kolom ke " + (j + 1) + " : ");
                matriks1.get(i).set(j, inputTerminal.nextInt());
            }
        }
        System.out.println("\n");

        // Display the input matrix
        System.out.println("Hasil inputan Matriks:");
        printMatrix(matriks1);
        System.out.println("\n");

        // Create the transpose matrix
        for (int i = 0; i < panjangMatriks; i++) {
            for (int j = 0; j < panjangMatriks; j++) {
                matriksTranspose.get(i).set(j, matriks1.get(j).get(i));
            }
        }

        // Display the transpose matrix
        System.out.println("Matriks transpose:");
        printMatrix(matriksTranspose);
        System.out.println("\n");

        // Sum the original matrix and its transpose
        for (int i = 0; i < panjangMatriks; i++) {
            for (int j = 0; j < panjangMatriks; j++) {
                matriksSum.get(i).set(j, matriks1.get(i).get(j) + matriksTranspose.get(i).get(j));
            }
        }

        // Display the summed matrix
        System.out.println("Hasil jumlah matriks1 dan matriksTranspose:");
        printMatrix(matriksSum);
    }

    public static void printMatrix(ArrayList<ArrayList<Integer>> matrix) {
        for (ArrayList<Integer> row : matrix) {
            System.out.print("[");
            for (Integer elem : row) {
                System.out.print(" " + elem + " ");
            }
            System.out.print("]\n");
        }
    }
}
