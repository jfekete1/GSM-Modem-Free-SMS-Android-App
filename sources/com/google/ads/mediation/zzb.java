package com.google.ads.mediation;

import com.google.android.gms.ads.reward.AdMetadataListener;

final class zzb extends AdMetadataListener {
    private final /* synthetic */ AbstractAdViewAdapter zzhz;

    zzb(AbstractAdViewAdapter abstractAdViewAdapter) {
        this.zzhz = abstractAdViewAdapter;
    }

    public final void onAdMetadataChanged() {
        if (this.zzhz.zzhw != null && this.zzhz.zzhx != null) {
            this.zzhz.zzhx.zzc(this.zzhz.zzhw.getAdMetadata());
        }
    }
}
