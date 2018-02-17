public class MultiThreadingImageTransformer extends ImageTransformer {

    public static char TYPE_FLIP_X = ImageTransformer.TYPE_FLIP_X;
    public static char TYPE_FLIP_Y = ImageTransformer.TYPE_FLIP_Y;
    public static char TYPE_FLIP_DIAG = ImageTransformer.TYPE_FLIP_DIAG;

    private static AsciiImage[][] transform(AsciiImage[][] ascii_images, char transform_type, float argfloat1, float argfloat2, int argint1) {
        int num_rows = ascii_images.length;
        int num_cols = ascii_images[0].length;
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
        return ascii_images;
    }

    public static AsciiImage[][] flip(AsciiImage[][] ascii_images, char axis) {
        return transform(ascii_images, axis, 0, 0, 0);
    }

    public static AsciiImage[][] invert(AsciiImage[][] ascii_images) {
        return transform(ascii_images, AsciiThreadTransformer.TYPE_TRANSFORM_INVERT, 0, 0, 0);
    }

    public static AsciiImage[][] blur(AsciiImage[][] ascii_images, int radius, float eps, float pow_val) {
        return transform(ascii_images, AsciiThreadTransformer.TYPE_TRANSFORM_BLUR, eps, pow_val, radius);
    }

    public static AsciiImage[][] contrast(AsciiImage[][] ascii_images, float contrast_significance) {
        return transform(ascii_images, AsciiThreadTransformer.TYPE_TRANSFORM_CONTRAST, contrast_significance, 0, 0);
    }
}
