package com.google.ads.mediation;

import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

final class zza implements RewardedVideoAdListener {
    private final /* synthetic */ AbstractAdViewAdapter zzhz;

    zza(AbstractAdViewAdapter abstractAdViewAdapter) {
        this.zzhz = abstractAdViewAdapter;
    }

    public final void onRewardedVideoAdLoaded() {
        this.zzhz.zzhx.onAdLoaded(this.zzhz);
    }

    public final void onRewardedVideoAdOpened() {
        this.zzhz.zzhx.onAdOpened(this.zzhz);
    }

    public final void onRewardedVideoStarted() {
        this.zzhz.zzhx.onVideoStarted(this.zzhz);
    }

    public final void onRewardedVideoAdClosed() {
        this.zzhz.zzhx.onAdClosed(this.zzhz);
        this.zzhz.zzhw = null;
    }

    public final void onRewarded(RewardItem rewardItem) {
        this.zzhz.zzhx.onRewarded(this.zzhz, rewardItem);
    }

    public final void onRewardedVideoAdLeftApplication() {
        this.zzhz.zzhx.onAdLeftApplication(this.zzhz);
    }

    public final void onRewardedVideoAdFailedToLoad(int i) {
        this.zzhz.zzhx.onAdFailedToLoad(this.zzhz, i);
    }

    public final void onRewardedVideoCompleted() {
        this.zzhz.zzhx.onVideoCompleted(this.zzhz);
    }
}
