package core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class Utilities {
    private static Utilities instance;

    // Static method to get the instance
    public static Utilities _instance() {
        if(instance == null) {
            instance = new Utilities();
        }
        return instance;
    }

    // Constructor
    private Utilities() {}

    public static Utilities getInstance() {
        return _instance();
    }

    public Scanner getScanner() {
        Scanner scanner = new Scanner(System.in);
        return scanner;
    }

    public String DecToBinaryString(int dec) {
        int[] binaryNum = new int[10];
        String result = "";
        int i = 0;
        while (dec > 0) {
            binaryNum[i] = dec % 2;
            dec = dec / 2;
            i++;
        }
        for (int j = i - 1; j >= 0; j--) {
            result += Integer.toString(binaryNum[j]);
        }
        // If the result is not in 8 bit
        if (result.length() != 8) {
            String add = "";
            for (int k = 0; k < (8 - result.length()); k++) {
                add += 0;
            }
            result = add + result;
        }
        return result;
    }

    public byte[] readFile(File file) throws IOException {
        byte[] res;
        try (FileInputStream fis = new FileInputStream(file)) {
            int length = (int) file.length();
            res = new byte[length];
            fis.read(res, 0, length);
        }
        return res;
    }

    public boolean isValidFilePath(String path) {
        File f = new File(path);
        try {
            f.getCanonicalPath();
            return true;
        }
        catch (IOException e) {
            return false;
        }
    }

}
