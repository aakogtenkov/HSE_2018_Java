import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class InputOutputHelper {

    private static Scanner inputScanner = new Scanner(System.in, "utf-8");

    public static String readline() {
        return inputScanner.nextLine().trim();
    }

    /*public static void printString(String s) {
        try {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out, "windows-1251"),true);
            writer.println(s);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }*/

    public static BufferedReader openFile(String filename) {
        try {
            return new BufferedReader(new InputStreamReader(new FileInputStream(filename),"windows-1251"));
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public static void closeFile(BufferedReader reader) {
        try {
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public static String[] splitString(String s, char regex) {
        ArrayList<String> result = new ArrayList<>();
        StringBuilder cur_s = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == regex) {
                result.add(cur_s.toString());
                cur_s = new StringBuilder();
            }
            else {
                cur_s.append(s.charAt(i));
            }
        }
        result.add(cur_s.toString());
        String[] res = new String[result.size()];
        for (int i = 0; i < result.size(); i++) {
            res[i] = result.get(i);
        }
        return res;
    }
}
