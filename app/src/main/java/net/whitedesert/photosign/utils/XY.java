package net.whitedesert.photosign.utils;

/**
 * Created by yazeed44 on 8/7/14.
 */
public final class XY {

    private int x, y;

    public XY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public XY() {

    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public static final class Float {

        private float x, y;

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }
    }
}
