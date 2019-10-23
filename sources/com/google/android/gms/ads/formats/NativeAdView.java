package com.google.android.gms.ads.formats;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.RemoteException;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.dynamic.ObjectWrapper;
import com.google.android.gms.internal.ads.zzadf;
import com.google.android.gms.internal.ads.zzbbd;
import com.google.android.gms.internal.ads.zzwu;

@Deprecated
public class NativeAdView extends FrameLayout {
    private final FrameLayout zzbld;
    private final zzadf zzble = zzia();

    public NativeAdView(Context context) {
        super(context);
        this.zzbld = zzc(context);
    }

    public NativeAdView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.zzbld = zzc(context);
    }

    public NativeAdView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.zzbld = zzc(context);
    }

    @TargetApi(21)
    public NativeAdView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.zzbld = zzc(context);
    }

    public void setAdChoicesView(AdChoicesView adChoicesView) {
        zza(NativeAd.ASSET_ADCHOICES_CONTAINER_VIEW, adChoicesView);
    }

    public AdChoicesView getAdChoicesView() {
        View zzao = zzao(NativeAd.ASSET_ADCHOICES_CONTAINER_VIEW);
        if (zzao instanceof AdChoicesView) {
            return (AdChoicesView) zzao;
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public final void zza(String str, View view) {
        try {
            this.zzble.zzb(str, ObjectWrapper.wrap(view));
        } catch (RemoteException e) {
            zzbbd.zzb("Unable to call setAssetView on delegate", e);
        }
    }

    /* access modifiers changed from: protected */
    public final View zzao(String str) {
        try {
            IObjectWrapper zzbm = this.zzble.zzbm(str);
            if (zzbm != null) {
                return (View) ObjectWrapper.unwrap(zzbm);
            }
        } catch (RemoteException e) {
            zzbbd.zzb("Unable to call getAssetView on delegate", e);
        }
        return null;
    }

    public void setNativeAd(NativeAd nativeAd) {
        try {
            this.zzble.zza((IObjectWrapper) nativeAd.zzhy());
        } catch (RemoteException e) {
            zzbbd.zzb("Unable to call setNativeAd on delegate", e);
        }
    }

    public void destroy() {
        try {
            this.zzble.destroy();
        } catch (RemoteException e) {
            zzbbd.zzb("Unable to destroy native ad view", e);
        }
    }

    private final FrameLayout zzc(Context context) {
        FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.setLayoutParams(new LayoutParams(-1, -1));
        addView(frameLayout);
        return frameLayout;
    }

    private final zzadf zzia() {
        Preconditions.checkNotNull(this.zzbld, "createDelegate must be called after mOverlayFrame has been created");
        if (isInEditMode()) {
            return null;
        }
        return zzwu.zzpw().zza(this.zzbld.getContext(), (FrameLayout) this, this.zzbld);
    }

    public void addView(View view, int i, ViewGroup.LayoutParams layoutParams) {
        super.addView(view, i, layoutParams);
        super.bringChildToFront(this.zzbld);
    }

    public void removeView(View view) {
        if (this.zzbld != view) {
            super.removeView(view);
        }
    }

    public void removeAllViews() {
        super.removeAllViews();
        super.addView(this.zzbld);
    }

    public void bringChildToFront(View view) {
        super.bringChildToFront(view);
        FrameLayout frameLayout = this.zzbld;
        if (frameLayout != view) {
            super.bringChildToFront(frameLayout);
        }
    }

    public void onVisibilityChanged(View view, int i) {
        super.onVisibilityChanged(view, i);
        zzadf zzadf = this.zzble;
        if (zzadf != null) {
            try {
                zzadf.zzb(ObjectWrapper.wrap(view), i);
            } catch (RemoteException e) {
                zzbbd.zzb("Unable to call onVisibilityChanged on delegate", e);
            }
        }
    }
}
