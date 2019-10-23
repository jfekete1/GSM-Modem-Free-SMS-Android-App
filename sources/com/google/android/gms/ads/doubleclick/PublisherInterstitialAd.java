package com.google.android.gms.ads.doubleclick;

import android.content.Context;
import android.support.annotation.RequiresPermission;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.Correlator;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.ads.zzzb;

public final class PublisherInterstitialAd {
    private final zzzb zzvy;

    public PublisherInterstitialAd(Context context) {
        this.zzvy = new zzzb(context, this);
        Preconditions.checkNotNull(context, "Context cannot be null");
    }

    public final AdListener getAdListener() {
        return this.zzvy.getAdListener();
    }

    public final String getAdUnitId() {
        return this.zzvy.getAdUnitId();
    }

    public final AppEventListener getAppEventListener() {
        return this.zzvy.getAppEventListener();
    }

    public final OnCustomRenderedAdLoadedListener getOnCustomRenderedAdLoadedListener() {
        return this.zzvy.getOnCustomRenderedAdLoadedListener();
    }

    public final boolean isLoaded() {
        return this.zzvy.isLoaded();
    }

    public final boolean isLoading() {
        return this.zzvy.isLoading();
    }

    @RequiresPermission("android.permission.INTERNET")
    public final void loadAd(PublisherAdRequest publisherAdRequest) {
        this.zzvy.zza(publisherAdRequest.zzaz());
    }

    public final void setAdListener(AdListener adListener) {
        this.zzvy.setAdListener(adListener);
    }

    public final void setAdUnitId(String str) {
        this.zzvy.setAdUnitId(str);
    }

    public final void setAppEventListener(AppEventListener appEventListener) {
        this.zzvy.setAppEventListener(appEventListener);
    }

    public final void setOnCustomRenderedAdLoadedListener(OnCustomRenderedAdLoadedListener onCustomRenderedAdLoadedListener) {
        this.zzvy.setOnCustomRenderedAdLoadedListener(onCustomRenderedAdLoadedListener);
    }

    public final void setCorrelator(Correlator correlator) {
        this.zzvy.setCorrelator(correlator);
    }

    public final String getMediationAdapterClassName() {
        return this.zzvy.getMediationAdapterClassName();
    }

    public final void show() {
        this.zzvy.show();
    }

    public final void setImmersiveMode(boolean z) {
        this.zzvy.setImmersiveMode(z);
    }
}
