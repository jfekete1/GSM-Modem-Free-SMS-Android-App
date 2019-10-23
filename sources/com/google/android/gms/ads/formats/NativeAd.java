package com.google.android.gms.ads.formats;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.gms.common.annotation.KeepForSdk;
import java.util.List;

public abstract class NativeAd {
    public static final String ASSET_ADCHOICES_CONTAINER_VIEW = "1098";

    public static abstract class AdChoicesInfo {
        public abstract List<Image> getImages();

        public abstract CharSequence getText();
    }

    public static abstract class Image {
        public abstract Drawable getDrawable();

        public abstract double getScale();

        public abstract Uri getUri();
    }

    @KeepForSdk
    @Deprecated
    public abstract void performClick(Bundle bundle);

    @KeepForSdk
    @Deprecated
    public abstract boolean recordImpression(Bundle bundle);

    @KeepForSdk
    @Deprecated
    public abstract void reportTouchEvent(Bundle bundle);

    /* access modifiers changed from: protected */
    public abstract Object zzhy();
}
