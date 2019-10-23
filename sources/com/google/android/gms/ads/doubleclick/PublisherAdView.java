package com.google.android.gms.ads.doubleclick;

import android.content.Context;
import android.support.annotation.RequiresPermission;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.Correlator;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.ads.zzbbd;
import com.google.android.gms.internal.ads.zzxl;
import com.google.android.gms.internal.ads.zzyz;

public final class PublisherAdView extends ViewGroup {
    private final zzyz zzvw;

    public PublisherAdView(Context context) {
        super(context);
        this.zzvw = new zzyz(this);
    }

    public PublisherAdView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.zzvw = new zzyz(this, attributeSet, true);
        Preconditions.checkNotNull(context, "Context cannot be null");
    }

    public PublisherAdView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.zzvw = new zzyz(this, attributeSet, true);
    }

    public final VideoController getVideoController() {
        return this.zzvw.getVideoController();
    }

    public final void setVideoOptions(VideoOptions videoOptions) {
        this.zzvw.setVideoOptions(videoOptions);
    }

    public final VideoOptions getVideoOptions() {
        return this.zzvw.getVideoOptions();
    }

    public final void destroy() {
        this.zzvw.destroy();
    }

    public final AdListener getAdListener() {
        return this.zzvw.getAdListener();
    }

    public final AdSize getAdSize() {
        return this.zzvw.getAdSize();
    }

    public final AdSize[] getAdSizes() {
        return this.zzvw.getAdSizes();
    }

    public final String getAdUnitId() {
        return this.zzvw.getAdUnitId();
    }

    public final AppEventListener getAppEventListener() {
        return this.zzvw.getAppEventListener();
    }

    public final OnCustomRenderedAdLoadedListener getOnCustomRenderedAdLoadedListener() {
        return this.zzvw.getOnCustomRenderedAdLoadedListener();
    }

    @RequiresPermission("android.permission.INTERNET")
    public final void loadAd(PublisherAdRequest publisherAdRequest) {
        this.zzvw.zza(publisherAdRequest.zzaz());
    }

    public final void pause() {
        this.zzvw.pause();
    }

    public final void setManualImpressionsEnabled(boolean z) {
        this.zzvw.setManualImpressionsEnabled(z);
    }

    public final void recordManualImpression() {
        this.zzvw.recordManualImpression();
    }

    public final void resume() {
        this.zzvw.resume();
    }

    public final void setAdListener(AdListener adListener) {
        this.zzvw.setAdListener(adListener);
    }

    public final void setAdSizes(AdSize... adSizeArr) {
        if (adSizeArr == null || adSizeArr.length <= 0) {
            throw new IllegalArgumentException("The supported ad sizes must contain at least one valid ad size.");
        }
        this.zzvw.zza(adSizeArr);
    }

    public final void setAdUnitId(String str) {
        this.zzvw.setAdUnitId(str);
    }

    public final void setAppEventListener(AppEventListener appEventListener) {
        this.zzvw.setAppEventListener(appEventListener);
    }

    public final void setCorrelator(Correlator correlator) {
        this.zzvw.setCorrelator(correlator);
    }

    public final void setOnCustomRenderedAdLoadedListener(OnCustomRenderedAdLoadedListener onCustomRenderedAdLoadedListener) {
        this.zzvw.setOnCustomRenderedAdLoadedListener(onCustomRenderedAdLoadedListener);
    }

    public final String getMediationAdapterClassName() {
        return this.zzvw.getMediationAdapterClassName();
    }

    public final boolean isLoading() {
        return this.zzvw.isLoading();
    }

    public final boolean zza(zzxl zzxl) {
        return this.zzvw.zza(zzxl);
    }

    /* access modifiers changed from: protected */
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
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
    public final void onMeasure(int i, int i2) {
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
