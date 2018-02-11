import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Main {

    public void test(String[] args) {
        int NUM_ROWS = 2;
        int NUM_COLS = 2;
        if (args.length < 1) {
            System.out.println("No filename as argument");
            return;
        }
        if (args.length >= 3) {
            NUM_ROWS = Integer.valueOf(args[1]);
            NUM_COLS = Integer.valueOf(args[2]);
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
            ImageIO.write(ImageTransformer.blur(image, 3, 1f, 3f), "jpg", new File("output_image_blur.jpg"));
        } catch (Exception e) {
            return;
        }
        //test image contrast
        try {
            ImageIO.write(ImageTransformer.contrast(image, 6f), "jpg", new File("output_image_contrast.jpg"));
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
        image = ImageConverter.asciiToImage(ImageTransformer.blur(ascii_image, 3, 1f, 3f), false, BufferedImage.TYPE_INT_RGB);
        try {
            ImageIO.write(image, "jpg", new File("output_ascii_blur.jpg"));
        } catch (Exception e) {
            return;
        }
        //test ascii contrast using multi threading
        AsciiImage[][] ascii_images = ImageTransformer.split(ascii_image, NUM_ROWS, NUM_COLS);
        AsciiThreadTransformer[][] threads = new AsciiThreadTransformer[NUM_ROWS][NUM_COLS];
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLS; j++) {
                threads[i][j] = new AsciiThreadTransformer(ascii_images[i][j], AsciiThreadTransformer.TYPE_TRANSFORM_CONTRAST, 5);
                threads[i][j].start();
            }
        }
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLS; j++) {
                try {
                    threads[i][j].join();
                    ascii_images[i][j] = threads[i][j].getImage();
                } catch (Exception e) {
                    return;
                }
            }
        }
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
