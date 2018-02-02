import java.awt.*;
import java.awt.image.BufferedImage;

import static java.lang.Math.round;

public class ImageConverter {
    private String ascii_palette = "   `...',-^\":;*~clodxkO0#KXNWM@";;

    public ImageConverter() {}

    public AsciiImage imageToAscii(BufferedImage image, boolean invert) {
        int width = image.getWidth();
        int height = image.getHeight();
        StringBuilder result = new StringBuilder();
        AsciiImage result_image = new AsciiImage();

        for (int y = 0; y < height; ++y) {

            for (int x = 0; x < width; x++) {
                Color color = new Color(image.getRGB(x, y));
                float R = color.getRed();
                float G = color.getGreen();
                float B = color.getBlue();
                float Y = (R + B + G) / 3.0f / 256.0f;
                float Y_inv = 1.0f - Y;

                int pos = round((ascii_palette.length() - 1) * (!invert? Y_inv : Y));
                char ch = ascii_palette.charAt(pos);
                result.append(ch);
            }
            result.append('\n');
        }
        result_image.setImage(result.toString(), ascii_palette);
        return result_image;
    }

    public static BufferedImage asciiToImage(AsciiImage image, boolean invert, int type) {
        String ascii_palette = image.getAsciiPalette();
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage result = new BufferedImage(width, height, type);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int ind = ascii_palette.indexOf(image.charAt(j, i));
                int col;
                if (invert) {
                    col = ind * 256 / ascii_palette.length();
                }
                else {
                    col = (ascii_palette.length() - ind - 1) * 256 / ascii_palette.length();
                }
                result.setRGB(j, i, (new Color(col, col, col)).getRGB());
            }
        }
        return result;
    }

    public void setAsciiPalette(String new_palette) {
        ascii_palette = new_palette;
    }
}
