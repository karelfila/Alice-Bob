import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int[] superSequence = generateSuperSequence(16);

        int u = generateU(superSequence);
        int v = generateV(u);
        int invertedV = createInvertedV(v, u);

        int[] modulatedSequence = generateModulatedSuperSequence(superSequence, u, v);

        System.out.println("Please enter a char input: ");
        char input = sc.nextLine().charAt(0);

        int publicKey = createPublicKey(input, modulatedSequence);

        char decodedInput = createPrivateKey(publicKey, invertedV, u, superSequence);
        System.out.println(decodedInput);

    }

    public static int[] generateSuperSequence(int length) {
        Random rdm = new Random();

        int[] seq = new int[length];

        int sum = 0;
        for (int i = 0; i < seq.length; i++) {
            seq[i] = rdm.nextInt(10) + sum + 1;
            sum += seq[i];
        }
        return seq;
    }

    public static boolean isNumberPrime(int number) {

        if (number <= 1) {
            return false;
        }

        for (int i = 0; i < Math.sqrt(number); i++) {
            if(number % i == 0 && i != 0) {
                return false;
            }
        }
        return true;
    }

    public static int generateU(int[] superGrowingSequence) {
        Random random = new Random();

        int superGrowingSequenceSum = Arrays.stream(superGrowingSequence).sum();
        int u = random.nextInt(50) + superGrowingSequenceSum + 1;

        while (!isNumberPrime(u)) {
            u = random.nextInt(50) + superGrowingSequenceSum + 1;
        }

        return u;

    }

    public static int generateV(int u) {
        Random random = new Random();

        int v = random.nextInt((u - 1) - 1) + 1;

        while (!isNumberPrime(v)) {
            v = random.nextInt((u - 1) - 1) + 1;
        }

        return v;
    }

    public static int[] generateModulatedSuperSequence(int[] superSequence, int u, int v) {
        int[] e = new int[superSequence.length];
        for (int i = 0; i < e.length; i++) {
            e[i] = superSequence[i] * v % u;
        }
        return e;
    }

    public static <T> void reverseArray(T[] array) {
        for (int i = 0; i < array.length / 2; i++) {
            T next = array[i];
            array[i] = array[array.length - i - 1];
            array[array.length - i - 1] = next;
        }
    }

    public static int createPublicKey(char input, int[] modulatedSuperSequence) {
        String binary = Integer.toBinaryString(input);
        int sum = 0;
        String[] bow = binary.split("");

        reverseArray(bow);
        reverseArray(modulatedSuperSequence);

        for (int i = 0; i < bow.length; i++) {
            if (bow[i].equals("1")) sum += modulatedSuperSequence[i];
        }
        return sum;
    }

    public static int createInvertedV(int v, int u) {

        for (int i = 1; i < u - 1; i++) {
            if (v * i % u == 1) {
                return i;
            }
        }
        return -1;
    }

    public static char createPrivateKey(int sum, int vMinus, int u, int[] superGrowingSequence) {
        StringBuilder sb = new StringBuilder();
        int Dt = sum * vMinus % u;

        reverseArray(superGrowingSequence);

        for (int i = 0; i < superGrowingSequence.length; i++) {
            if (Dt - superGrowingSequence[i] >= 0) {
                sb.append("1");
                Dt -= superGrowingSequence[i];
            } else sb.append("0");


        }
        return (char) (Integer.parseInt(sb.reverse().toString(), 2));
    }



}