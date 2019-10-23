package com.google.android.gms.ads;

import android.content.Context;
import android.support.annotation.RequiresPermission;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.ads.doubleclick.AppEventListener;
import com.google.android.gms.internal.ads.zzbbd;
import com.google.android.gms.internal.ads.zzvt;
import com.google.android.gms.internal.ads.zzyz;

class BaseAdView extends ViewGroup {
    protected final zzyz zzvw;

    public BaseAdView(Context context, int i) {
        super(context);
        this.zzvw = new zzyz(this, i);
    }

    public BaseAdView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet);
        this.zzvw = new zzyz(this, attributeSet, false, i);
    }

    public BaseAdView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i);
        this.zzvw = new zzyz(this, attributeSet, false, i2);
    }

    public void destroy() {
        this.zzvw.destroy();
    }

    public AdListener getAdListener() {
        return this.zzvw.getAdListener();
    }

    public AdSize getAdSize() {
        return this.zzvw.getAdSize();
    }

    public String getAdUnitId() {
        return this.zzvw.getAdUnitId();
    }

    @RequiresPermission("android.permission.INTERNET")
    public void loadAd(AdRequest adRequest) {
        this.zzvw.zza(adRequest.zzaz());
    }

    public void pause() {
        this.zzvw.pause();
    }

    public void resume() {
        this.zzvw.resume();
    }

    public boolean isLoading() {
        return this.zzvw.isLoading();
    }

    public void setAdListener(AdListener adListener) {
        this.zzvw.setAdListener(adListener);
        if (adListener == null) {
            this.zzvw.zza((zzvt) null);
            this.zzvw.setAppEventListener(null);
            return;
        }
        if (adListener instanceof zzvt) {
            this.zzvw.zza((zzvt) adListener);
        }
        if (adListener instanceof AppEventListener) {
            this.zzvw.setAppEventListener((AppEventListener) adListener);
        }
    }

    public void setAdSize(AdSize adSize) {
        this.zzvw.setAdSizes(adSize);
    }

    public void setAdUnitId(String str) {
        this.zzvw.setAdUnitId(str);
    }

    public String getMediationAdapterClassName() {
        return this.zzvw.getMediationAdapterClassName();
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        View childAt = getChildAt(0);
        if (childAt != null && childAt.getVisibility() != 8) {
            int measuredWidth = childAt.getMeasuredWidth();
            int measuredHeight = childAt.getMeasuredHeight();
            int i5 = ((i3 - i) - measuredWidth) / 2;
            int i6 = ((i4 - i2) - measuredHeight) / 2;
            childAt.layout(i5, i6, measuredWidth + i5, measuredHeight + i6);
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int i3;
        int i4 = 0;
        View childAt = getChildAt(0);
        if (childAt == null || childAt.getVisibility() == 8) {
            AdSize adSize = null;
            try {
                adSize = getAdSize();
            } catch (NullPointerException e) {
                zzbbd.zzb("Unable to retrieve ad size.", e);
            }
            if (adSize != null) {
                Context context = getContext();
                int widthInPixels = adSize.getWidthInPixels(context);
                i3 = adSize.getHeightInPixels(context);
                i4 = widthInPixels;
            } else {
                i3 = 0;
            }
        } else {
            measureChild(childAt, i, i2);
            i4 = childAt.getMeasuredWidth();
            i3 = childAt.getMeasuredHeight();
        }
        setMeasuredDimension(View.resolveSize(Math.max(i4, getSuggestedMinimumWidth()), i), View.resolveSize(Math.max(i3, getSuggestedMinimumHeight()), i2));
    }
}
