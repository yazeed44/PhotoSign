package net.whitedesert.photosign.utils;

import android.graphics.Bitmap;

/**
 * Created by yazeed44 on 10/10/14.
 */
public final class SigningOptions {

    private Signature signature;
    private XY.Float signingXY;
    private XY signDimension;
    private XY originalPhotoDimen; // Original photo dimension
    private Bitmap photo;


    public Signature getSignature() {
        return signature;
    }

    public SigningOptions setSignature(Signature signature) {
        this.signature = signature;
        return this;
    }

    public XY.Float getSigningXY() {
        return signingXY;
    }

    public SigningOptions setSigningXY(XY.Float signingXY) {
        this.signingXY = signingXY;
        return this;
    }

    public XY getSignDimension() {
        return signDimension;
    }

    public SigningOptions setSignDimension(XY signDimension) {
        this.signDimension = signDimension;
        return this;
    }

    public XY getOriginalPhotoDimen() {
        return originalPhotoDimen;
    }

    public SigningOptions setOriginalPhotoDimen(XY originalPhotoDimen) {
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
