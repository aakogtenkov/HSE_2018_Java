import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Main {

    public void test(String[] args) {
        int NUM_ROWS = 2;
        int NUM_COLS = 2;
        float CONTRAST_SIGNIFICANCE = 5;
        int BLUR_RADIUS = 3;
        float BLUR_EPS = 1;
        float BLUR_POW = 3;
        if (args.length < 1) {
            System.out.println("No filename as argument");
            return;
        }
        if (args.length >= 3) {
            NUM_ROWS = Integer.valueOf(args[1]);
            NUM_COLS = Integer.valueOf(args[2]);
        }
        if (args.length >= 4) {
            CONTRAST_SIGNIFICANCE = Float.valueOf(args[3]);
        }
        if (args.length >= 5) {
            BLUR_RADIUS = Integer.valueOf(args[4]);
        }
        if (args.length >= 6) {
            BLUR_EPS = Float.valueOf(args[5]);
        }
        if (args.length >= 7) {
            BLUR_POW = Float.valueOf(args[6]);
        }
        BufferedImage image;
        try {
            image = ImageIO.read(new File(args[0]));
        } catch (Exception e) {
            System.out.printf("Cannot open file " + args[0]);
            return;
        }

        //test image blur
        try {
            ImageIO.write(ImageTransformer.blur(image, BLUR_RADIUS, BLUR_EPS, BLUR_POW), "jpg", new File("output_image_blur.jpg"));
        } catch (Exception e) {
            return;
        }
        //test image contrast
        try {
            ImageIO.write(ImageTransformer.contrast(image, CONTRAST_SIGNIFICANCE), "jpg", new File("output_image_contrast.jpg"));
        } catch (Exception e) {
            return;
        }
        ImageConverter converter = new ImageConverter();
        //test converting image to ascii
        AsciiImage ascii_image = converter.imageToAscii(image, false);
        //test splitting and concatenating images
        ascii_image = ImageTransformer.concat(ImageTransformer.split(ascii_image, 100, 100), ascii_image.getAsciiPalette());
        //test saving ascii
        ascii_image.save("ascii_image.txt");
        //test flipping image, inverting image and converting ascii to image
        image = ImageTransformer.invert(ImageTransformer.flip(ImageConverter.asciiToImage(ascii_image, false, BufferedImage.TYPE_INT_RGB), 'd'));
        try {
            ImageIO.write(image, "jpg", new File("output_invert+flip.jpg"));
        } catch (Exception e) {
            return;
        }
        //test loading ascii
        ascii_image.load("ascii_image.txt");
        //test asFloat and constructor
        ascii_image = new AsciiImage(ascii_image.asFloat());
        //test asString and flipping ascii
        System.out.println(ImageTransformer.flip(ascii_image, ImageTransformer.TYPE_FLIP_X).asString());

        //test ascii blur
        image = ImageConverter.asciiToImage(ImageTransformer.blur(ascii_image, BLUR_RADIUS, BLUR_EPS, BLUR_POW), false, BufferedImage.TYPE_INT_RGB);
        try {
            ImageIO.write(image, "jpg", new File("output_ascii_blur.jpg"));
        } catch (Exception e) {
            return;
        }

        //test ascii contrast using multi threading
        AsciiImage[][] ascii_images = ImageTransformer.split(ascii_image, NUM_ROWS, NUM_COLS);
        ascii_images = MultiThreadingImageTransformer.contrast(ascii_images, CONTRAST_SIGNIFICANCE);
        ascii_image = ImageTransformer.concat(ascii_images, ascii_image.getAsciiPalette());
        image = ImageConverter.asciiToImage(ascii_image, false, BufferedImage.TYPE_INT_RGB);
        try {
            ImageIO.write(image, "jpg", new File("output_ascii_contrast.jpg"));
        } catch (Exception e) {
            return;
        }
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.test(args);
    }
}
