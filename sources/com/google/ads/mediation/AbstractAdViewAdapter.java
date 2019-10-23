package com.google.ads.mediation;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.doubleclick.AppEventListener;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.NativeAdView;
import com.google.android.gms.ads.formats.NativeAdViewHolder;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeAppInstallAd.OnAppInstallAdLoadedListener;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.formats.NativeContentAd.OnContentAdLoadedListener;
import com.google.android.gms.ads.formats.NativeCustomTemplateAd;
import com.google.android.gms.ads.formats.NativeCustomTemplateAd.OnCustomClickListener;
import com.google.android.gms.ads.formats.NativeCustomTemplateAd.OnCustomTemplateAdLoadedListener;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd.OnUnifiedNativeAdLoadedListener;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.android.gms.ads.mediation.MediationAdRequest;
import com.google.android.gms.ads.mediation.MediationBannerAdapter;
import com.google.android.gms.ads.mediation.MediationBannerListener;
import com.google.android.gms.ads.mediation.MediationInterstitialListener;
import com.google.android.gms.ads.mediation.MediationNativeAdapter;
import com.google.android.gms.ads.mediation.MediationNativeListener;
import com.google.android.gms.ads.mediation.NativeAdMapper;
import com.google.android.gms.ads.mediation.NativeAppInstallAdMapper;
import com.google.android.gms.ads.mediation.NativeContentAdMapper;
import com.google.android.gms.ads.mediation.NativeMediationAdRequest;
import com.google.android.gms.ads.mediation.OnImmersiveModeUpdatedListener;
import com.google.android.gms.ads.mediation.UnifiedNativeAdMapper;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.ads.reward.mediation.MediationRewardedVideoAdAdapter;
import com.google.android.gms.ads.reward.mediation.MediationRewardedVideoAdListener;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.android.gms.internal.ads.zzark;
import com.google.android.gms.internal.ads.zzbat;
import com.google.android.gms.internal.ads.zzbbd;
import com.google.android.gms.internal.ads.zzbiy;
import com.google.android.gms.internal.ads.zzvt;
import com.google.android.gms.internal.ads.zzwu;
import com.google.android.gms.internal.ads.zzyp;
import java.util.Date;
import java.util.Map;
import java.util.Set;

@zzark
public abstract class AbstractAdViewAdapter implements MediationBannerAdapter, MediationNativeAdapter, OnImmersiveModeUpdatedListener, com.google.android.gms.ads.mediation.zzb, MediationRewardedVideoAdAdapter, zzbiy {
    public static final String AD_UNIT_ID_PARAMETER = "pubid";
    private AdView zzhs;
    private InterstitialAd zzht;
    private AdLoader zzhu;
    private Context zzhv;
    /* access modifiers changed from: private */
    public InterstitialAd zzhw;
    /* access modifiers changed from: private */
    public MediationRewardedVideoAdListener zzhx;
    @VisibleForTesting
    private final RewardedVideoAdListener zzhy = new zza(this);

    static class zza extends NativeAppInstallAdMapper {
        private final NativeAppInstallAd zzia;

        public zza(NativeAppInstallAd nativeAppInstallAd) {
            this.zzia = nativeAppInstallAd;
            setHeadline(nativeAppInstallAd.getHeadline().toString());
            setImages(nativeAppInstallAd.getImages());
            setBody(nativeAppInstallAd.getBody().toString());
            setIcon(nativeAppInstallAd.getIcon());
            setCallToAction(nativeAppInstallAd.getCallToAction().toString());
            if (nativeAppInstallAd.getStarRating() != null) {
                setStarRating(nativeAppInstallAd.getStarRating().doubleValue());
            }
            if (nativeAppInstallAd.getStore() != null) {
                setStore(nativeAppInstallAd.getStore().toString());
            }
            if (nativeAppInstallAd.getPrice() != null) {
                setPrice(nativeAppInstallAd.getPrice().toString());
            }
            setOverrideImpressionRecording(true);
            setOverrideClickHandling(true);
            zza(nativeAppInstallAd.getVideoController());
        }

        public final void trackView(View view) {
            if (view instanceof NativeAdView) {
                ((NativeAdView) view).setNativeAd(this.zzia);
            }
            NativeAdViewHolder nativeAdViewHolder = (NativeAdViewHolder) NativeAdViewHolder.zzblg.get(view);
            if (nativeAdViewHolder != null) {
                nativeAdViewHolder.setNativeAd((NativeAd) this.zzia);
            }
        }
    }

    static class zzb extends NativeContentAdMapper {
        private final NativeContentAd zzib;

        public zzb(NativeContentAd nativeContentAd) {
            this.zzib = nativeContentAd;
            setHeadline(nativeContentAd.getHeadline().toString());
            setImages(nativeContentAd.getImages());
            setBody(nativeContentAd.getBody().toString());
            if (nativeContentAd.getLogo() != null) {
                setLogo(nativeContentAd.getLogo());
            }
            setCallToAction(nativeContentAd.getCallToAction().toString());
            setAdvertiser(nativeContentAd.getAdvertiser().toString());
            setOverrideImpressionRecording(true);
            setOverrideClickHandling(true);
            zza(nativeContentAd.getVideoController());
        }

        public final void trackView(View view) {
            if (view instanceof NativeAdView) {
                ((NativeAdView) view).setNativeAd(this.zzib);
            }
            NativeAdViewHolder nativeAdViewHolder = (NativeAdViewHolder) NativeAdViewHolder.zzblg.get(view);
            if (nativeAdViewHolder != null) {
                nativeAdViewHolder.setNativeAd((NativeAd) this.zzib);
            }
        }
    }

    static class zzc extends UnifiedNativeAdMapper {
        private final UnifiedNativeAd zzic;

        public zzc(UnifiedNativeAd unifiedNativeAd) {
            this.zzic = unifiedNativeAd;
            setHeadline(unifiedNativeAd.getHeadline());
            setImages(unifiedNativeAd.getImages());
            setBody(unifiedNativeAd.getBody());
            setIcon(unifiedNativeAd.getIcon());
            setCallToAction(unifiedNativeAd.getCallToAction());
            setAdvertiser(unifiedNativeAd.getAdvertiser());
            setStarRating(unifiedNativeAd.getStarRating());
            setStore(unifiedNativeAd.getStore());
            setPrice(unifiedNativeAd.getPrice());
            zzp(unifiedNativeAd.zzic());
            setOverrideImpressionRecording(true);
            setOverrideClickHandling(true);
            zza(unifiedNativeAd.getVideoController());
        }

        public final void trackViews(View view, Map<String, View> map, Map<String, View> map2) {
            if (view instanceof UnifiedNativeAdView) {
                ((UnifiedNativeAdView) view).setNativeAd(this.zzic);
                return;
            }
            NativeAdViewHolder nativeAdViewHolder = (NativeAdViewHolder) NativeAdViewHolder.zzblg.get(view);
            if (nativeAdViewHolder != null) {
                nativeAdViewHolder.setNativeAd(this.zzic);
            }
        }
    }

    @VisibleForTesting
    static final class zzd extends AdListener implements AppEventListener, zzvt {
        @VisibleForTesting
        private final AbstractAdViewAdapter zzid;
        @VisibleForTesting
        private final MediationBannerListener zzie;

        public zzd(AbstractAdViewAdapter abstractAdViewAdapter, MediationBannerListener mediationBannerListener) {
            this.zzid = abstractAdViewAdapter;
            this.zzie = mediationBannerListener;
        }

        public final void onAdLoaded() {
            this.zzie.onAdLoaded(this.zzid);
        }

        public final void onAdFailedToLoad(int i) {
            this.zzie.onAdFailedToLoad(this.zzid, i);
        }

        public final void onAdOpened() {
            this.zzie.onAdOpened(this.zzid);
        }

        public final void onAdClosed() {
            this.zzie.onAdClosed(this.zzid);
        }

        public final void onAdLeftApplication() {
            this.zzie.onAdLeftApplication(this.zzid);
        }

        public final void onAdClicked() {
            this.zzie.onAdClicked(this.zzid);
        }

        public final void onAppEvent(String str, String str2) {
            this.zzie.zza(this.zzid, str, str2);
        }
    }

    @VisibleForTesting
    static final class zze extends AdListener implements zzvt {
        @VisibleForTesting
        private final AbstractAdViewAdapter zzid;
        @VisibleForTesting
        private final MediationInterstitialListener zzif;

        public zze(AbstractAdViewAdapter abstractAdViewAdapter, MediationInterstitialListener mediationInterstitialListener) {
            this.zzid = abstractAdViewAdapter;
            this.zzif = mediationInterstitialListener;
        }

        public final void onAdLoaded() {
            this.zzif.onAdLoaded(this.zzid);
        }

        public final void onAdFailedToLoad(int i) {
            this.zzif.onAdFailedToLoad(this.zzid, i);
        }

        public final void onAdOpened() {
            this.zzif.onAdOpened(this.zzid);
        }

        public final void onAdClosed() {
            this.zzif.onAdClosed(this.zzid);
        }

        public final void onAdLeftApplication() {
            this.zzif.onAdLeftApplication(this.zzid);
        }

        public final void onAdClicked() {
            this.zzif.onAdClicked(this.zzid);
        }
    }

    @VisibleForTesting
    static final class zzf extends AdListener implements OnAppInstallAdLoadedListener, OnContentAdLoadedListener, OnCustomClickListener, OnCustomTemplateAdLoadedListener, OnUnifiedNativeAdLoadedListener {
        @VisibleForTesting
        private final AbstractAdViewAdapter zzid;
        @VisibleForTesting
        private final MediationNativeListener zzig;

        public zzf(AbstractAdViewAdapter abstractAdViewAdapter, MediationNativeListener mediationNativeListener) {
            this.zzid = abstractAdViewAdapter;
            this.zzig = mediationNativeListener;
        }

        public final void onAdLoaded() {
        }

        public final void onAdFailedToLoad(int i) {
            this.zzig.onAdFailedToLoad(this.zzid, i);
        }

        public final void onAdOpened() {
            this.zzig.onAdOpened(this.zzid);
        }

        public final void onAdClosed() {
            this.zzig.onAdClosed(this.zzid);
        }

        public final void onAdLeftApplication() {
            this.zzig.onAdLeftApplication(this.zzid);
        }

        public final void onAdClicked() {
            this.zzig.onAdClicked(this.zzid);
        }

        public final void onAdImpression() {
            this.zzig.onAdImpression(this.zzid);
        }

        public final void onAppInstallAdLoaded(NativeAppInstallAd nativeAppInstallAd) {
            this.zzig.onAdLoaded((MediationNativeAdapter) this.zzid, (NativeAdMapper) new zza(nativeAppInstallAd));
        }

        public final void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
            this.zzig.onAdLoaded((MediationNativeAdapter) this.zzid, (UnifiedNativeAdMapper) new zzc(unifiedNativeAd));
        }

        public final void onContentAdLoaded(NativeContentAd nativeContentAd) {
            this.zzig.onAdLoaded((MediationNativeAdapter) this.zzid, (NativeAdMapper) new zzb(nativeContentAd));
        }

        public final void onCustomTemplateAdLoaded(NativeCustomTemplateAd nativeCustomTemplateAd) {
            this.zzig.zza(this.zzid, nativeCustomTemplateAd);
        }

        public final void onCustomClick(NativeCustomTemplateAd nativeCustomTemplateAd, String str) {
            this.zzig.zza(this.zzid, nativeCustomTemplateAd, str);
        }
    }

    /* access modifiers changed from: protected */
    public abstract Bundle zza(Bundle bundle, Bundle bundle2);

    private final AdRequest zza(Context context, MediationAdRequest mediationAdRequest, Bundle bundle, Bundle bundle2) {
        Builder builder = new Builder();
        Date birthday = mediationAdRequest.getBirthday();
        if (birthday != null) {
            builder.setBirthday(birthday);
        }
        int gender = mediationAdRequest.getGender();
        if (gender != 0) {
            builder.setGender(gender);
        }
        Set<String> keywords = mediationAdRequest.getKeywords();
        if (keywords != null) {
            for (String addKeyword : keywords) {
                builder.addKeyword(addKeyword);
            }
        }
        Location location = mediationAdRequest.getLocation();
        if (location != null) {
            builder.setLocation(location);
        }
        if (mediationAdRequest.isTesting()) {
            zzwu.zzpv();
            builder.addTestDevice(zzbat.zzbf(context));
        }
        if (mediationAdRequest.taggedForChildDirectedTreatment() != -1) {
            boolean z = true;
            if (mediationAdRequest.taggedForChildDirectedTreatment() != 1) {
                z = false;
            }
            builder.tagForChildDirectedTreatment(z);
        }
        builder.setIsDesignedForFamilies(mediationAdRequest.isDesignedForFamilies());
        builder.addNetworkExtrasBundle(AdMobAdapter.class, zza(bundle, bundle2));
        return builder.build();
    }

    public void onDestroy() {
        AdView adView = this.zzhs;
        if (adView != null) {
            adView.destroy();
            this.zzhs = null;
        }
        if (this.zzht != null) {
            this.zzht = null;
        }
        if (this.zzhu != null) {
            this.zzhu = null;
        }
        if (this.zzhw != null) {
            this.zzhw = null;
        }
    }

    public void onPause() {
        AdView adView = this.zzhs;
        if (adView != null) {
            adView.pause();
        }
    }

    public void onResume() {
        AdView adView = this.zzhs;
        if (adView != null) {
            adView.resume();
        }
    }

    public String getAdUnitId(Bundle bundle) {
        return bundle.getString(AD_UNIT_ID_PARAMETER);
    }

    public void requestBannerAd(Context context, MediationBannerListener mediationBannerListener, Bundle bundle, AdSize adSize, MediationAdRequest mediationAdRequest, Bundle bundle2) {
        this.zzhs = new AdView(context);
        this.zzhs.setAdSize(new AdSize(adSize.getWidth(), adSize.getHeight()));
        this.zzhs.setAdUnitId(getAdUnitId(bundle));
        this.zzhs.setAdListener(new zzd(this, mediationBannerListener));
        this.zzhs.loadAd(zza(context, mediationAdRequest, bundle2, bundle));
    }

    public View getBannerView() {
        return this.zzhs;
    }

    public void requestInterstitialAd(Context context, MediationInterstitialListener mediationInterstitialListener, Bundle bundle, MediationAdRequest mediationAdRequest, Bundle bundle2) {
        this.zzht = new InterstitialAd(context);
        this.zzht.setAdUnitId(getAdUnitId(bundle));
        this.zzht.setAdListener(new zze(this, mediationInterstitialListener));
        this.zzht.loadAd(zza(context, mediationAdRequest, bundle2, bundle));
    }

    public void onImmersiveModeUpdated(boolean z) {
        InterstitialAd interstitialAd = this.zzht;
        if (interstitialAd != null) {
            interstitialAd.setImmersiveMode(z);
        }
        InterstitialAd interstitialAd2 = this.zzhw;
        if (interstitialAd2 != null) {
            interstitialAd2.setImmersiveMode(z);
        }
    }

    public zzyp getVideoController() {
        AdView adView = this.zzhs;
        if (adView != null) {
            VideoController videoController = adView.getVideoController();
            if (videoController != null) {
                return videoController.zzbc();
            }
        }
        return null;
    }

    public void showInterstitial() {
        this.zzht.show();
    }

    public Bundle getInterstitialAdapterInfo() {
        return new com.google.android.gms.ads.mediation.MediationAdapter.zza().zzdi(1).zzafg();
    }

    public void requestNativeAd(Context context, MediationNativeListener mediationNativeListener, Bundle bundle, NativeMediationAdRequest nativeMediationAdRequest, Bundle bundle2) {
        zzf zzf2 = new zzf(this, mediationNativeListener);
        AdLoader.Builder withAdListener = new AdLoader.Builder(context, bundle.getString(AD_UNIT_ID_PARAMETER)).withAdListener(zzf2);
        NativeAdOptions nativeAdOptions = nativeMediationAdRequest.getNativeAdOptions();
        if (nativeAdOptions != null) {
            withAdListener.withNativeAdOptions(nativeAdOptions);
        }
        if (nativeMediationAdRequest.isUnifiedNativeAdRequested()) {
            withAdListener.forUnifiedNativeAd(zzf2);
        }
        if (nativeMediationAdRequest.isAppInstallAdRequested()) {
            withAdListener.forAppInstallAd(zzf2);
        }
        if (nativeMediationAdRequest.isContentAdRequested()) {
            withAdListener.forContentAd(zzf2);
        }
        if (nativeMediationAdRequest.zzvg()) {
            for (String str : nativeMediationAdRequest.zzvh().keySet()) {
                withAdListener.forCustomTemplateAd(str, zzf2, ((Boolean) nativeMediationAdRequest.zzvh().get(str)).booleanValue() ? zzf2 : null);
            }
        }
        this.zzhu = withAdListener.build();
        this.zzhu.loadAd(zza(context, nativeMediationAdRequest, bundle2, bundle));
    }

    public void initialize(Context context, MediationAdRequest mediationAdRequest, String str, MediationRewardedVideoAdListener mediationRewardedVideoAdListener, Bundle bundle, Bundle bundle2) {
        this.zzhv = context.getApplicationContext();
        this.zzhx = mediationRewardedVideoAdListener;
        this.zzhx.onInitializationSucceeded(this);
    }

    public void loadAd(MediationAdRequest mediationAdRequest, Bundle bundle, Bundle bundle2) {
        Context context = this.zzhv;
        if (context == null || this.zzhx == null) {
            zzbbd.m38e("AdMobAdapter.loadAd called before initialize.");
            return;
        }
        this.zzhw = new InterstitialAd(context);
        this.zzhw.zza(true);
        this.zzhw.setAdUnitId(getAdUnitId(bundle));
        this.zzhw.setRewardedVideoAdListener(this.zzhy);
        this.zzhw.setAdMetadataListener(new zzb(this));
        this.zzhw.loadAd(zza(this.zzhv, mediationAdRequest, bundle2, bundle));
    }

    public void showVideo() {
        this.zzhw.show();
    }

    public boolean isInitialized() {
        return this.zzhx != null;
    }
}
