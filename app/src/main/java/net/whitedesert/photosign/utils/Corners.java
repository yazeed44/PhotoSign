package net.whitedesert.photosign.utils;

/**
 * Created by yazeed44 on 9/12/14.
 */
public class Corners {

    private XY.Float rightUp;
    private XY.Float rightDown;
    private XY.Float leftUp;
    private XY.Float leftDown;

    public Corners(final XY.Float rightUp, final XY.Float rightDown, final XY.Float leftUp, final XY.Float leftDown) {
        this.rightDown = rightDown;
        this.rightUp = rightUp;
        this.leftUp = leftUp;
        this.leftDown = leftDown;
    }

    public Corners() {

    }

    public XY.Float getRightUp() {
        return rightUp;
    }

    public void setRightUp(XY.Float rightUp) {
        this.rightUp = rightUp;
    }

    public XY.Float getRightDown() {
        return rightDown;
    }

    public void setRightDown(XY.Float rightDown) {
        this.rightDown = rightDown;
    }

    public XY.Float getLeftUp() {
        return leftUp;
    }

    public void setLeftUp(XY.Float leftUp) {
        this.leftUp = leftUp;
    }

    public XY.Float getLeftDown() {
        return leftDown;
    }

    public void setLeftDown(XY.Float leftDown) {
        this.leftDown = leftDown;
    }

    public XY.Float[] getCorners() {
        XY.Float[] corners = new XY.Float[4];
        corners[0] = getRightUp();
        corners[1] = getRightDown();
        corners[2] = getLeftUp();
        corners[3] = getLeftDown();
        return corners;
    }
}
