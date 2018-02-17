import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class AsciiThreadTransformer extends Thread{
    public static char TYPE_TRANSFORM_CONTRAST = 'c';
    public static char TYPE_TRANSFORM_INVERT = 'i';
    public static char TYPE_TRANSFORM_BLUR = 'b';
    public static char TYPE_TRANSFORM_FLIPX = ImageTransformer.TYPE_FLIP_X;
    public static char TYPE_TRANSFORM_FLIPY = ImageTransformer.TYPE_FLIP_Y;
    public static char TYPE_TRANSFORM_FLIPD = ImageTransformer.TYPE_FLIP_DIAG;
    private AsciiImage image;
    private char transform_type;
    private float argfloat1 = 1;
    private float argfloat2 = 1;
    private int argint1 = 1;

    // for blur
    public AsciiThreadTransformer(AsciiImage image, char transform_type, float eps, float pow_val, int radius) {
        this.image = image;
        this.transform_type = transform_type;
        this.argfloat1 = eps;
        this.argfloat2 = pow_val;
        this.argint1 = radius;
    }

    // for contrast
    public AsciiThreadTransformer(AsciiImage image, char transform_type, float arg) {
        this.image = image;
        this.transform_type = transform_type;
        this.argfloat1 = arg;
    }

    // for inverting and flipping
    public AsciiThreadTransformer(AsciiImage image, char transform_type) {
        this.image = image;
        this.transform_type = transform_type;
    }

    public void run() {
        if (transform_type == TYPE_TRANSFORM_CONTRAST) {
            image = ImageTransformer.contrast(image, argfloat1);
        }
        else if (transform_type == TYPE_TRANSFORM_INVERT) {
            image = ImageTransformer.invert(image);
        }
        else if (transform_type == TYPE_TRANSFORM_FLIPX) {
            image = ImageTransformer.flip(image, 'x');
        }
        else if (transform_type == TYPE_TRANSFORM_FLIPY) {
            image = ImageTransformer.flip(image, 'y');
        }
        else if (transform_type == TYPE_TRANSFORM_FLIPD) {
            image = ImageTransformer.flip(image, 'd');
        }
        else if (transform_type == TYPE_TRANSFORM_BLUR) {
            image = ImageTransformer.blur(image, argint1, argfloat1, argfloat2);
        }
        else {
            throw new NotImplementedException();
        }
    }

    public AsciiImage getImage() {
        return image;
    }
}
