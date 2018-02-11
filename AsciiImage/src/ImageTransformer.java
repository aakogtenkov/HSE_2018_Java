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
        float bias = 1.0f / image.getAsciiPalette().length();
        for (int i = 0; i < gray_image.length; i++) {
            for (int j = 0; j < gray_image[i].length; j++) {
                gray_image[i][j] = max(0, 1 - gray_image[i][j] - bias);
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
                result.setRGB(j, i, (new Color(resultR[i][j], resultG[i][j], resultB[i][j])).getRGB());
            }
        }
        return result;
    }

    public static AsciiImage contrast(AsciiImage image, float contrast_significance) {
        if (contrast_significance < 0) {
            throw new ValueException("contrast_significance must be >= 0");
        }
        float[][] gray_image = image.asFloat();
        int width = image.getWidth();
        int height = image.getHeight();
        float[][] result = new float[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                result[i][j] = gray_image[i][j] - 0.5f;
                if (result[i][j] < 0) {
                    result[i][j] -= (1.0 / (1 + Math.exp(result[i][j] * contrast_significance)) - 0.5f);
                }
                else {
                    result[i][j] += (1.0 / (1 + Math.exp(-result[i][j] * contrast_significance)) - 0.5f);
                }
                result[i][j] += 0.5f;
                result[i][j] = Math.max(0, result[i][j]);
                result[i][j] = Math.min(1, result[i][j]);
            }
        }
        return new AsciiImage(result, image.getAsciiPalette());
    }

    public static BufferedImage contrast(BufferedImage image, float contrast_significance) {
        if (contrast_significance < 0) {
            throw new ValueException("contrast_significance must be >= 0");
        }
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage result = new BufferedImage(width, height, image.getType());
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int rgb = image.getRGB(j, i);
                Color color = new Color(rgb);
                int r = color.getRed();
                int g = color.getGreen();
                int b = color.getBlue();
                float[] result_colors = new float[] {(r / 256.0f) - 0.5f,
                                                    (g / 256.0f) - 0.5f,
                                                    (b / 256.0f) - 0.5f};
                for (int c = 0; c < 3; c++) {
                    if (result_colors[c] < 0) {
                        result_colors[c] -= (1.0 / (1 + Math.exp(result_colors[c] * contrast_significance)) - 0.5f);
                    }
                    else {
                        result_colors[c] += (1.0 / (1 + Math.exp(-result_colors[c] * contrast_significance)) - 0.5f);
                    }
                    result_colors[c] += 0.5;
                    result_colors[c] = Math.max(0, result_colors[c]);
                    result_colors[c] = Math.min(1, result_colors[c]);
                }
                color = new Color(result_colors[0], result_colors[1], result_colors[2]);
                result.setRGB(j, i, color.getRGB());
            }
        }
        return result;
    }

    public static AsciiImage[][] split(AsciiImage image, int num_rows, int num_cols) {
        if (num_cols <= 0 || num_rows <= 0) {
            throw new ValueException("num_rows and num_cols must be > 0");
        }
        StringBuilder[][] substr = new StringBuilder[num_rows][num_cols];
        for (int i = 0; i < num_cols; i++) {
            for (int j = 0; j < num_rows; j++) {
                substr[j][i] = new StringBuilder();
            }
        }
        int width = image.getWidth();
        int height = image.getHeight();
        for (int i = 0; i < height; i++) {
            int ind_y = Math.round((float)i * (float)(num_rows - 1) / (float)height);
            for (int j = 0; j < width; j++) {
                int ind_x = Math.round((float)j * (float)(num_cols - 1) / (float)width);
                substr[ind_y][ind_x].append(image.charAt(j, i));
            }
            for (int j = 0; j < num_cols; j++) {
                substr[ind_y][j].append('\n');
            }
        }
        AsciiImage[][] images = new AsciiImage[num_rows][num_cols];
        for (int i = 0; i < num_rows; i++) {
            for (int j = 0; j < num_cols; j++) {
                images[i][j] = new AsciiImage(image.getAsciiPalette());
                images[i][j].setImage(substr[i][j].toString());
            }
        }
        return images;
    }

    public static AsciiImage concat(AsciiImage[][] images, String ascii_palette) {
        if (images.length == 0 || images[0].length == 0) {
            throw new ValueException("Trying to construct image from empty images");
        }
        StringBuilder str = new StringBuilder();
        AsciiImage image = new AsciiImage(ascii_palette);
        for (int i = 0; i < images.length; i++) {
            for (int y = 0; y < images[i][0].getHeight(); y++) {
                for (int j = 0; j < images[i].length; j++) {
                    for (int x = 0; x < images[i][j].getWidth(); x++) {
                        str.append(images[i][j].charAt(x, y));
                    }
                }
                str.append('\n');
            }
        }
        image.setImage(str.toString(), ascii_palette);
        return image;
    }
}
