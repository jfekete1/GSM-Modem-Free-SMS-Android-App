package com.google.android.gms.ads;

import android.content.Context;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.NativeAppInstallAd.OnAppInstallAdLoadedListener;
import com.google.android.gms.ads.formats.NativeContentAd.OnContentAdLoadedListener;
import com.google.android.gms.ads.formats.NativeCustomTemplateAd.OnCustomClickListener;
import com.google.android.gms.ads.formats.NativeCustomTemplateAd.OnCustomTemplateAdLoadedListener;
import com.google.android.gms.ads.formats.OnPublisherAdViewLoadedListener;
import com.google.android.gms.ads.formats.PublisherAdViewOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd.OnUnifiedNativeAdLoadedListener;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.ads.zzacp;
import com.google.android.gms.internal.ads.zzaeb;
import com.google.android.gms.internal.ads.zzaee;
import com.google.android.gms.internal.ads.zzaeq;
import com.google.android.gms.internal.ads.zzafc;
import com.google.android.gms.internal.ads.zzafd;
import com.google.android.gms.internal.ads.zzafe;
import com.google.android.gms.internal.ads.zzaff;
import com.google.android.gms.internal.ads.zzafg;
import com.google.android.gms.internal.ads.zzafi;
import com.google.android.gms.internal.ads.zzalf;
import com.google.android.gms.internal.ads.zzbbd;
import com.google.android.gms.internal.ads.zzvx;
import com.google.android.gms.internal.ads.zzwe;
import com.google.android.gms.internal.ads.zzwf;
import com.google.android.gms.internal.ads.zzwu;
import com.google.android.gms.internal.ads.zzxa;
import com.google.android.gms.internal.ads.zzxd;
import com.google.android.gms.internal.ads.zzxg;
import com.google.android.gms.internal.ads.zzxz;
import com.google.android.gms.internal.ads.zzyx;

public class AdLoader {
    private final Context mContext;
    private final zzwe zzvn;
    private final zzxd zzvo;

    public static class Builder {
        private final Context mContext;
        private final zzxg zzvp;

        public Builder(Context context, String str) {
            this((Context) Preconditions.checkNotNull(context, "context cannot be null"), zzwu.zzpw().zzb(context, str, new zzalf()));
        }

        private Builder(Context context, zzxg zzxg) {
            this.mContext = context;
            this.zzvp = zzxg;
        }

        @Deprecated
        public Builder forContentAd(OnContentAdLoadedListener onContentAdLoadedListener) {
            try {
                this.zzvp.zza((zzaee) new zzafd(onContentAdLoadedListener));
            } catch (RemoteException e) {
                zzbbd.zzc("Failed to add content ad listener", e);
            }
            return this;
        }

        @Deprecated
        public Builder forAppInstallAd(OnAppInstallAdLoadedListener onAppInstallAdLoadedListener) {
            try {
                this.zzvp.zza((zzaeb) new zzafc(onAppInstallAdLoadedListener));
            } catch (RemoteException e) {
                zzbbd.zzc("Failed to add app install ad listener", e);
            }
            return this;
        }

        public Builder forUnifiedNativeAd(OnUnifiedNativeAdLoadedListener onUnifiedNativeAdLoadedListener) {
            try {
                this.zzvp.zza((zzaeq) new zzafi(onUnifiedNativeAdLoadedListener));
            } catch (RemoteException e) {
                zzbbd.zzc("Failed to add google native ad listener", e);
            }
            return this;
        }

        public Builder forCustomTemplateAd(String str, OnCustomTemplateAdLoadedListener onCustomTemplateAdLoadedListener, OnCustomClickListener onCustomClickListener) {
            zzafe zzafe;
            try {
                zzxg zzxg = this.zzvp;
                zzaff zzaff = new zzaff(onCustomTemplateAdLoadedListener);
                if (onCustomClickListener == null) {
                    zzafe = null;
                } else {
                    zzafe = new zzafe(onCustomClickListener);
                }
                zzxg.zza(str, zzaff, zzafe);
            } catch (RemoteException e) {
                zzbbd.zzc("Failed to add custom template ad listener", e);
            }
            return this;
        }

        public Builder forPublisherAdView(OnPublisherAdViewLoadedListener onPublisherAdViewLoadedListener, AdSize... adSizeArr) {
            if (adSizeArr == null || adSizeArr.length <= 0) {
                throw new IllegalArgumentException("The supported ad sizes must contain at least one valid ad size.");
            }
            try {
                this.zzvp.zza(new zzafg(onPublisherAdViewLoadedListener), new zzwf(this.mContext, adSizeArr));
            } catch (RemoteException e) {
                zzbbd.zzc("Failed to add publisher banner ad listener", e);
            }
            return this;
        }

        public Builder withAdListener(AdListener adListener) {
            try {
                this.zzvp.zzb((zzxa) new zzvx(adListener));
            } catch (RemoteException e) {
                zzbbd.zzc("Failed to set AdListener.", e);
            }
            return this;
        }

        public Builder withNativeAdOptions(NativeAdOptions nativeAdOptions) {
            try {
                this.zzvp.zza(new zzacp(nativeAdOptions));
            } catch (RemoteException e) {
                zzbbd.zzc("Failed to specify native ad options", e);
            }
            return this;
        }

        public Builder withPublisherAdViewOptions(PublisherAdViewOptions publisherAdViewOptions) {
            try {
                this.zzvp.zza(publisherAdViewOptions);
            } catch (RemoteException e) {
                zzbbd.zzc("Failed to specify DFP banner ad options", e);
            }
            return this;
        }

        public Builder withCorrelator(@NonNull Correlator correlator) {
            Preconditions.checkNotNull(correlator);
            try {
                this.zzvp.zzb((zzxz) correlator.zzvx);
            } catch (RemoteException e) {
                zzbbd.zzc("Failed to set correlator.", e);
            }
            return this;
        }

        public AdLoader build() {
            try {
                return new AdLoader(this.mContext, this.zzvp.zzkd());
            } catch (RemoteException e) {
                zzbbd.zzb("Failed to build AdLoader.", e);
                return null;
            }
        }
    }

    AdLoader(Context context, zzxd zzxd) {
        this(context, zzxd, zzwe.zzckj);
    }

    private AdLoader(Context context, zzxd zzxd, zzwe zzwe) {
        this.mContext = context;
        this.zzvo = zzxd;
        this.zzvn = zzwe;
    }

    private final void zza(zzyx zzyx) {
        try {
            this.zzvo.zzd(zzwe.zza(this.mContext, zzyx));
        } catch (RemoteException e) {
            zzbbd.zzb("Failed to load ad.", e);
        }
    }

    @RequiresPermission("android.permission.INTERNET")
    public void loadAd(AdRequest adRequest) {
        zza(adRequest.zzaz());
    }

    @RequiresPermission("android.permission.INTERNET")
    public void loadAds(AdRequest adRequest, int i) {
        try {
            this.zzvo.zza(zzwe.zza(this.mContext, adRequest.zzaz()), i);
        } catch (RemoteException e) {
            zzbbd.zzb("Failed to load ads.", e);
        }
    }

    public void loadAd(PublisherAdRequest publisherAdRequest) {
        zza(publisherAdRequest.zzaz());
    }

    @Deprecated
    public String getMediationAdapterClassName() {
        try {
            return this.zzvo.zzje();
        } catch (RemoteException e) {
            zzbbd.zzc("Failed to get the mediation adapter class name.", e);
            return null;
        }
    }

    public boolean isLoading() {
        try {
            return this.zzvo.isLoading();
        } catch (RemoteException e) {
            zzbbd.zzc("Failed to check if ad is loading.", e);
            return false;
        }
    }
}
