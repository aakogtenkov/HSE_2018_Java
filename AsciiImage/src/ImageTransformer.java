import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.awt.*;
import java.awt.image.BufferedImage;

import static java.lang.Float.max;

public class ImageTransformer {
    public static char TYPE_FLIP_X = 'x';
    public static char TYPE_FLIP_Y = 'y';
    public static char TYPE_FLIP_DIAG = 'd';

    public ImageTransformer() {}

    public static AsciiImage flip(AsciiImage ascii_image, char axis) {
        String[] substr = ascii_image.asString().split("\n");
        StringBuilder result = new StringBuilder();
        if (axis == 'x') {
            for (int i = 0; i < substr.length; i++) {
                for (int j = substr[i].length() - 1; j >= 0; j--) {
                    result.append(substr[i].charAt(j));
                }
                result.append('\n');
            }
        }
        else if (axis == 'y') {
            for (int i = substr.length - 1; i >= 0; i--) {
                result.append(substr[i]);
                result.append('\n');
            }
        }
        else if (axis == 'd') {
            for (int i = 0; i < substr[0].length(); i++) {
                for (int j = 0; j < substr.length; j++) {
                    result.append(substr[j].charAt(i));
                }
                result.append('\n');
            }
        }
        AsciiImage result_image = new AsciiImage();
        result_image.setImage(result.toString(), ascii_image.getAsciiPalette());
        return result_image;
    }

    public static BufferedImage flip(BufferedImage image, char axis) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage result;
        if (axis == 'x') {
            result = new BufferedImage(width, height, image.getType());
            for (int i = 0; i < height; i++) {
                for (int j = width - 1; j >= 0; j--) {
                    result.setRGB(width - j - 1, i, image.getRGB(j, i));
                }
            }
        }
        else if (axis == 'y') {
            result = new BufferedImage(width, height, image.getType());
            for (int i = height - 1; i >= 0; i--) {
                for (int j = 0; j < width; j++) {
                    result.setRGB(j, height - i - 1, image.getRGB(j, i));
                }
            }
        }
        else if (axis == 'd') {
            result = new BufferedImage(height, width, image.getType());
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    result.setRGB(j, i, image.getRGB(i, j));
                }
            }
        }
        else {
            throw new NotImplementedException();
        }
        return result;
    }

    public static AsciiImage invert(AsciiImage image) {
        float[][] gray_image = image.asFloat();
        for (int i = 0; i < gray_image.length; i++) {
            for (int j = 0; j < gray_image[i].length; j++) {
                gray_image[i][j] = max(0, 1 - gray_image[i][j]);
            }
        }
        return new AsciiImage(gray_image, image.getAsciiPalette());
    }

    public static BufferedImage invert(BufferedImage image) {
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                Color color = new Color(image.getRGB(i, j));
                int R = 255 - color.getRed();
                int B = 255 - color.getBlue();
                int G = 255 - color.getGreen();
                color = new Color(R, G, B);
                result.setRGB(i, j, color.getRGB());
            }
        }
        return result;
    }

    public static AsciiImage blur(AsciiImage image, int radius, float eps, float pow_val) {
        if (radius < 0) {
            throw new ValueException("Radius must be >= 0");
        }
        if (eps < 1) {
            throw new ValueException("Epsilon must be >= 1");
        }
        float[][] gray_image = image.asFloat();
        if (gray_image.length == 0 || gray_image[0].length == 0) {
            return new AsciiImage(gray_image, image.getAsciiPalette());
        }
        int shade_size = radius * 2 + 1;
        float[][] shade = new float[shade_size][shade_size];
        for (int i = -radius; i <= radius; i++) {
            for (int j = -radius; j <= radius; j++) {
                double dist = Math.sqrt(Math.pow(i, 2) + Math.pow(j, 2));
                shade[i + radius][j + radius] = (float)(1.0 / Math.pow(dist + eps, pow_val));
            }
        }
        float[][] shade_sum = new float[gray_image.length][gray_image[0].length];
        float[][] result = new float[gray_image.length][gray_image[0].length];
        for (int i = 0; i < gray_image.length; i++) {
            for (int j = 0; j < gray_image[i].length; j++) {
                float color = gray_image[i][j];
                for (int k1 = -radius; k1 <= radius; k1++) {
                    for (int k2 = -radius; k2 <= radius; k2++) {
                        if (k1 + i < gray_image.length &&
                                k2 + j < gray_image[i].length &&
                                k1 + i >= 0 && k2 + j >= 0) {
                            result[i + k1][j + k2] += color * shade[k1 + radius][k2 + radius];
                            shade_sum[i + k1][j + k2] += shade[k1 + radius][k2 + radius];
                        }
                    }
                }
            }
        }
        for (int i = 0; i < gray_image.length; i++) {
            for (int j = 0; j < gray_image[i].length; j++) {
                result[i][j] /= shade_sum[i][j];
            }
        }
        return new AsciiImage(result, image.getAsciiPalette());
    }

    public static BufferedImage blur(BufferedImage image, int radius, float eps, float pow_val) {
        if (radius < 0) {
            throw new ValueException("Radius must be >= 0");
        }
        if (eps < 1) {
            throw new ValueException("Epsilon must be >= 1");
        }
        int shade_size = radius * 2 + 1;
        float[][] shade = new float[shade_size][shade_size];
        for (int i = -radius; i <= radius; i++) {
            for (int j = -radius; j <= radius; j++) {
                double dist = Math.sqrt(Math.pow(i, 2) + Math.pow(j, 2));
                shade[i + radius][j + radius] = (float)(1.0 / Math.pow(dist + eps, pow_val));
            }
        }
        int width = image.getWidth();
        int height = image.getHeight();
        float[][] shade_sum = new float[height][width];
        float[][] resultR = new float[height][width];
        float[][] resultG = new float[height][width];
        float[][] resultB = new float[height][width];
        BufferedImage result = new BufferedImage(width, height, image.getType());
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Color color = new Color(image.getRGB(j, i));
                for (int k1 = -radius; k1 <= radius; k1++) {
                    for (int k2 = -radius; k2 <= radius; k2++) {
                        if (k1 + i < height &&
                                k2 + j < width &&
                                k1 + i >= 0 && k2 + j >= 0) {
                            resultR[i + k1][j + k2] += (float)color.getRed() / 256 * shade[k1 + radius][k2 + radius];
                            resultG[i + k1][j + k2] += (float)color.getGreen() / 256 * shade[k1 + radius][k2 + radius];
                            resultB[i + k1][j + k2] += (float)color.getBlue() / 256 * shade[k1 + radius][k2 + radius];
                            shade_sum[i + k1][j + k2] += shade[k1 + radius][k2 + radius];
                        }
                    }
                }
            }
        }
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                resultR[i][j] /= shade_sum[i][j];
                resultG[i][j] /= shade_sum[i][j];
                resultB[i][j] /= shade_sum[i][j];
                if (resultR[i][j] > 255 || resultB[i][j] > 255 || resultG[i][j] > 255) {
                    System.out.println("A");
                    System.out.flush();
                }
                result.setRGB(j, i, (new Color(resultR[i][j], resultG[i][j], resultB[i][j])).getRGB());
            }
        }
        return result;
    }
}
