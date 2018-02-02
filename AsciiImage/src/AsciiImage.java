import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.*;

import static java.lang.Math.round;

public class AsciiImage {

    private String ascii_palette = "   `...',-^\":;*~clodxkO0#KXNWM@";
    private String image = "";
    private int width = 0;
    private int height = 0;

    public AsciiImage() {}

    public AsciiImage(String ascii_palette) {
        if (ascii_palette.length() == 0) {
            throw new ValueException("Ascii palette must be non-empty.");
        }
        else {
            this.ascii_palette = ascii_palette;
        }
    }

    public AsciiImage(float[][] image) {
        this.setImage(image, ascii_palette);
    }

    public AsciiImage(float[][] image, String new_palette) {
        this.setImage(image, new_palette);
    }

    public void setImage(String image) {
        try {
            setImage(image, ascii_palette);
        } catch (ValueException e) {
            throw e;
        }
    }

    public void setImage(String image, String new_palette) {
        if (new_palette.length() == 0) {
            throw new ValueException("Ascii palette must be non-empty.");
        }
        if (isAsciiImage(image, new_palette)) {
            String[] substr = image.split("\n");
            int width = -1;
            int height = substr.length;
            for (int i = 0; i < substr.length; i++) {
                if (width == -1) {
                    width = substr[i].length();
                }
                else if (width != substr[i].length()) {
                    System.out.println(substr[i]);
                    throw new ValueException("Trying to set non-ascii image as ascii image.");
                }
            }
            this.image = image;
            this.width = width;
            this.height = height;
        }
        else {
            throw new ValueException("Trying to set non-ascii image as ascii image.");
        }
    }

    public void setImage(float[][] image, String new_palette) {
        if (new_palette.length() == 0) {
            throw new ValueException("Ascii palette must be non-empty.");
        }
        StringBuilder result = new StringBuilder();
        int ascii_palette_length = new_palette.length();
        for (int i = 0; i < image.length; i++) {
            this.width = image[i].length;
            for (int j = 0; j < image[i].length; j++) {
                int pos = round((ascii_palette_length - 1) * image[i][j]);
                if (pos >= ascii_palette_length) {
                    pos = ascii_palette_length - 1;
                }
                result.append(new_palette.charAt(pos));
            }
            result.append('\n');
        }
        this.height = image.length;
        this.image = result.toString();
        this.ascii_palette = new_palette;
    }

    public String asString() {
        return image;
    }

    public float[][] asFloat() {
        float[][] result = new float[height][width];
        float ascii_palette_length = ascii_palette.length();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                float ind = ascii_palette.indexOf(charAt(j, i));
                result[i][j] = ind / (ascii_palette_length - 1);
            }
        }
        return result;
    }

    public static boolean isAsciiImage(String image, String ascii_palette) {
        for (int i = 0; i < image.length(); i++) {
            if (image.charAt(i) != '\n' && ascii_palette.indexOf(image.charAt(i)) < 0) {
                return false;
            }
        }
        return true;
    }

    public void setAsciiPalette(String new_palette) {
        /*if (new_palette.length() > 0) {
            ascii_palette = new_palette;
        }
        else {
            throw new ValueException("Ascii palette must me non-empty string.");
        }*/
        throw new NotImplementedException();
    }

    public String getAsciiPalette() {
        return ascii_palette;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public char charAt(int x, int y) {
        if (x >= 0 && y >= 0 && y * (width + 1) + x < image.length()) {
            return image.charAt(y * (width + 1) + x);
        }
        throw new ValueException("Index parameters " + String.valueOf(x) + ", " + String.valueOf(y) + " are out of range (" + String.valueOf(width) + ", " + String.valueOf(height) + ").");
    }

    public void load(String filename) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(filename));
        } catch (Exception e) {
            System.out.println("Cannot open file " + filename + ".");
            return;
        }
        StringBuffer result = new StringBuffer();
        String cur_str = "";
        int width = -1;
        int height = 0;
        while (cur_str != null) {
            try {
                result.append(cur_str);
                cur_str = reader.readLine();
                if (cur_str != null) {
                    cur_str += '\n';
                    height += 1;
                    if (width == -1) {
                        width = cur_str.length() - 1;
                    }
                    else if (width != cur_str.length() - 1) {
                        System.out.println("File " + filename + " does now contain ascii image.");
                        return;
                    }
                }
            } catch (Exception e) {
                System.out.println("Exception while reading " + filename + ".");
                return;
            }
        }
        if (!isAsciiImage(result.toString(), ascii_palette)) {
            System.out.println("File " + filename + " does now contain ascii image.");
            return;
        }
        this.height = height;
        this.width = width;
        image = result.toString();
    }

    public void save(String filename) {
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "utf-8"));
        }
        catch (Exception e) {
            System.out.println("Cannot open output file " + filename + ".");
            return;
        }

        try {
            String[] substr = image.split("\n");
            for (int i = 0; i < substr.length; i++) {
                writer.write(substr[i]);
                writer.newLine();
            }
        } catch (Exception e) {
            System.out.println("Exception while writing file " + filename + ".");
            return;
        }

        try {
            writer.close();
        } catch (Exception e) {
            System.out.println("Exception while closing file " + filename + ".");
        }

    }
}
