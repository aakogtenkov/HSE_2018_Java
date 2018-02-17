public class MultiThreadingImageTransformer extends ImageTransformer {

    private static AsciiImage transform(AsciiImage ascii_image, char transform_type, int num_rows, int num_cols, float argfloat1, float argfloat2, int argint1) {
        AsciiImage[][] ascii_images = ImageTransformer.split(ascii_image, num_rows, num_cols);
        AsciiThreadTransformer[][] threads = new AsciiThreadTransformer[num_rows][num_cols];
        for (int i = 0; i < num_rows; i++) {
            for (int j = 0; j < num_cols; j++) {
                threads[i][j] = new AsciiThreadTransformer(ascii_images[i][j], transform_type, argfloat1, argfloat2, argint1);
                threads[i][j].start();
            }
        }
        for (int i = 0; i < num_rows; i++) {
            for (int j = 0; j < num_cols; j++) {
                try {
                    threads[i][j].join();
                    ascii_images[i][j] = threads[i][j].getImage();
                } catch (Exception e) {
                    System.out.println("MultiThreading exception.");
                    throw new RuntimeException();
                }
            }
        }
        ascii_image = ImageTransformer.concat(ascii_images, ascii_image.getAsciiPalette());
        return ascii_image;
    }

    public static AsciiImage flip(AsciiImage ascii_image, char axis, int num_rows, int num_cols) {
        return transform(ascii_image, axis, num_rows, num_cols, 0, 0, 0);
    }

    public static AsciiImage invert(AsciiImage ascii_image, int num_rows, int num_cols) {
        return transform(ascii_image, AsciiThreadTransformer.TYPE_TRANSFORM_INVERT, num_rows, num_cols, 0, 0, 0);
    }

    public static AsciiImage blur(AsciiImage ascii_image, int radius, float eps, float pow_val, int num_rows, int num_cols) {
        return transform(ascii_image, AsciiThreadTransformer.TYPE_TRANSFORM_BLUR, num_rows, num_cols, eps, pow_val, radius);
    }

    public static AsciiImage contrast(AsciiImage ascii_image, float contrast_significance, int num_rows, int num_cols) {
        return transform(ascii_image, AsciiThreadTransformer.TYPE_TRANSFORM_CONTRAST, num_rows, num_cols, contrast_significance, 0, 0);
    }
}
