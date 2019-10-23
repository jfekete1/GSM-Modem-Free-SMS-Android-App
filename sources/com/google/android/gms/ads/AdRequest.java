package com.google.android.gms.ads;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.mediation.MediationAdapter;
import com.google.android.gms.ads.mediation.NetworkExtras;
import com.google.android.gms.ads.mediation.customevent.CustomEvent;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.android.gms.internal.ads.zzyx;
import com.google.android.gms.internal.ads.zzyy;
import java.util.Date;
import java.util.Set;

@VisibleForTesting
public final class AdRequest {
    public static final String DEVICE_ID_EMULATOR = "B3EEABB8EE11C2BE770B684D95219ECB";
    public static final int ERROR_CODE_INTERNAL_ERROR = 0;
    public static final int ERROR_CODE_INVALID_REQUEST = 1;
    public static final int ERROR_CODE_NETWORK_ERROR = 2;
    public static final int ERROR_CODE_NO_FILL = 3;
    public static final int GENDER_FEMALE = 2;
    public static final int GENDER_MALE = 1;
    public static final int GENDER_UNKNOWN = 0;
    public static final String MAX_AD_CONTENT_RATING_G = "G";
    public static final String MAX_AD_CONTENT_RATING_MA = "MA";
    public static final String MAX_AD_CONTENT_RATING_PG = "PG";
    public static final String MAX_AD_CONTENT_RATING_T = "T";
    public static final int MAX_CONTENT_URL_LENGTH = 512;
    public static final int TAG_FOR_UNDER_AGE_OF_CONSENT_FALSE = 0;
    public static final int TAG_FOR_UNDER_AGE_OF_CONSENT_TRUE = 1;
    public static final int TAG_FOR_UNDER_AGE_OF_CONSENT_UNSPECIFIED = -1;
    private final zzyx zzvq;

    @VisibleForTesting
    public static final class Builder {
        /* access modifiers changed from: private */
        public final zzyy zzvr = new zzyy();

        public Builder() {
            this.zzvr.zzbe("B3EEABB8EE11C2BE770B684D95219ECB");
        }

        public final Builder addKeyword(String str) {
            this.zzvr.zzbd(str);
            return this;
        }

        public final Builder addNetworkExtras(NetworkExtras networkExtras) {
            this.zzvr.zza(networkExtras);
            return this;
        }

        public final Builder addNetworkExtrasBundle(Class<? extends MediationAdapter> cls, Bundle bundle) {
            this.zzvr.zza(cls, bundle);
            if (cls.equals(AdMobAdapter.class) && bundle.getBoolean("_emulatorLiveAds")) {
                this.zzvr.zzbf("B3EEABB8EE11C2BE770B684D95219ECB");
            }
            return this;
        }

        public final Builder addCustomEventExtrasBundle(Class<? extends CustomEvent> cls, Bundle bundle) {
            this.zzvr.zzb(cls, bundle);
            return this;
        }

        public final Builder addTestDevice(String str) {
            this.zzvr.zzbe(str);
            return this;
        }

        public final AdRequest build() {
            return new AdRequest(this);
        }

        @Deprecated
        public final Builder setBirthday(Date date) {
            this.zzvr.zza(date);
            return this;
        }

        public final Builder setContentUrl(String str) {
            Preconditions.checkNotNull(str, "Content URL must be non-null.");
            Preconditions.checkNotEmpty(str, "Content URL must be non-empty.");
            Preconditions.checkArgument(str.length() <= 512, "Content URL must not exceed %d in length.  Provided length was %d.", Integer.valueOf(512), Integer.valueOf(str.length()));
            this.zzvr.zzbg(str);
            return this;
        }

        @Deprecated
        public final Builder setGender(int i) {
            this.zzvr.zzch(i);
            return this;
        }

        public final Builder setLocation(Location location) {
            this.zzvr.zzb(location);
            return this;
        }

        public final Builder setRequestAgent(String str) {
            this.zzvr.zzbi(str);
            return this;
        }

        public final Builder tagForChildDirectedTreatment(boolean z) {
            this.zzvr.zzu(z);
            return this;
        }

        @Deprecated
        public final Builder setIsDesignedForFamilies(boolean z) {
            this.zzvr.zzv(z);
            return this;
        }

        public final Builder setTagForUnderAgeOfConsent(int i) {
            this.zzvr.zzci(i);
            return this;
        }

        public final Builder setMaxAdContentRating(String str) {
            this.zzvr.zzbk(str);
            return this;
        }
    }

    private AdRequest(Builder builder) {
        this.zzvq = new zzyx(builder.zzvr);
    }

    @Deprecated
    public final Date getBirthday() {
        return this.zzvq.getBirthday();
    }

    public final String getContentUrl() {
        return this.zzvq.getContentUrl();
    }

    @Deprecated
    public final int getGender() {
        return this.zzvq.getGender();
    }

    public final Set<String> getKeywords() {
        return this.zzvq.getKeywords();
    }

    public final Location getLocation() {
        return this.zzvq.getLocation();
    }

    @Deprecated
    public final <T extends NetworkExtras> T getNetworkExtras(Class<T> cls) {
        return this.zzvq.getNetworkExtras(cls);
    }

    public final <T extends MediationAdapter> Bundle getNetworkExtrasBundle(Class<T> cls) {
        return this.zzvq.getNetworkExtrasBundle(cls);
    }

    public final <T extends CustomEvent> Bundle getCustomEventExtrasBundle(Class<T> cls) {
        return this.zzvq.getCustomEventExtrasBundle(cls);
    }

    public final boolean isTestDevice(Context context) {
        return this.zzvq.isTestDevice(context);
    }

    public final zzyx zzaz() {
        return this.zzvq;
    }
}
