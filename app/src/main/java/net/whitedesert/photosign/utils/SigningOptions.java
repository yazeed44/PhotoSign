package net.whitedesert.photosign.utils;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.PointF;

/**
 * Created by yazeed44 on 10/10/14.
 */
public final class SigningOptions {

    private Signature signature;
    private PointF signingXY;
    private Point signDimension;
    private Point originalPhotoDimen; // Original photo dimension
    private Bitmap photo;


    public Signature getSignature() {
        return signature;
    }

    public SigningOptions setSignature(Signature signature) {
        this.signature = signature;
        return this;
    }

    public PointF getSigningXY() {
        return signingXY;
    }

    public SigningOptions setSigningXY(final PointF signingXY) {
        this.signingXY = signingXY;
        return this;
    }

    public Point getSignDimension() {
        return signDimension;
    }

    public SigningOptions setSignDimension(Point signDimension) {
        this.signDimension = signDimension;
        return this;
    }

    public Point getOriginalPhotoDimen() {
        return originalPhotoDimen;
    }

    public SigningOptions setOriginalPhotoDimen(Point originalPhotoDimen) {
        this.originalPhotoDimen = originalPhotoDimen;
        return this;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public SigningOptions setPhoto(Bitmap photo) {
        this.photo = photo;
        return this;
    }
}
