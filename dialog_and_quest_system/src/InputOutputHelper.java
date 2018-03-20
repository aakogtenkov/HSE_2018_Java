import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class InputOutputHelper {
    private static InputOutputHelper instance = null;
    private String inputCharset = "utf-8";
    private String fileCharset = "windows-1251";
    private Scanner inputScanner = new Scanner(System.in, "utf-8");

    public InputOutputHelper(String inputCharset, String fileCharset) {
        this.inputCharset = inputCharset;
        this.fileCharset = fileCharset;
        this.inputScanner = new Scanner(System.in, inputCharset);
        instance = this;
    }

    public InputOutputHelper(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
            this.inputCharset = reader.readLine().trim();
            this.fileCharset = reader.readLine().trim();
            this.inputScanner = new Scanner(System.in, inputCharset);
            instance = this;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public String readline() {
        return inputScanner.nextLine().trim();
    }

    public void printString(String s) {
        try {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out, inputCharset),true);
            writer.println(s);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public BufferedReader openFile(String filename) {
        try {
            return new BufferedReader(new InputStreamReader(new FileInputStream(filename), fileCharset));
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

    public static InputOutputHelper getInstance() {
        if (instance == null) {
            throw new RuntimeException("InputOutputHelper did not initialized.");
        }
        return instance;
    }
}
