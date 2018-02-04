import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Main {

    public void test(String[] args) {
        if (args.length < 1) {
            System.out.println("No filename as argument");
            return;
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
            ImageIO.write(ImageTransformer.contrast(image, 6, true), "jpg", new File("output_image_contrast.jpg"));
        } catch (Exception e) {
            return;
        }
        ImageConverter converter = new ImageConverter();
        //test converting image to ascii
        AsciiImage ascii_image = converter.imageToAscii(image, false);
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
        //test ascii contrast
        image = ImageConverter.asciiToImage(ImageTransformer.contrast(ascii_image, 6, true), false, BufferedImage.TYPE_INT_RGB);
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
