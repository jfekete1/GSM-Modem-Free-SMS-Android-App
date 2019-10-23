package com.google.android.gms.ads.formats;

import android.os.RemoteException;
import android.view.View;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.dynamic.ObjectWrapper;
import com.google.android.gms.internal.ads.zzadk;
import com.google.android.gms.internal.ads.zzbbd;
import com.google.android.gms.internal.ads.zzwu;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

public final class NativeAdViewHolder {
    public static WeakHashMap<View, NativeAdViewHolder> zzblg = new WeakHashMap<>();
    private zzadk zzblf;
    private WeakReference<View> zzblh;

    public NativeAdViewHolder(View view, Map<String, View> map, Map<String, View> map2) {
        Preconditions.checkNotNull(view, "ContainerView must not be null");
        if ((view instanceof NativeAdView) || (view instanceof UnifiedNativeAdView)) {
            zzbbd.m38e("The provided containerView is of type of NativeAdView, which cannot be usedwith NativeAdViewHolder.");
        } else if (zzblg.get(view) != null) {
            zzbbd.m38e("The provided containerView is already in use with another NativeAdViewHolder.");
        } else {
            zzblg.put(view, this);
            this.zzblh = new WeakReference<>(view);
            this.zzblf = zzwu.zzpw().zza(view, zzb(map), zzb(map2));
        }
    }

    private static HashMap<String, View> zzb(Map<String, View> map) {
        if (map == null) {
            return new HashMap<>();
        }
        return new HashMap<>(map);
    }

    public final void setNativeAd(NativeAd nativeAd) {
        zza((IObjectWrapper) nativeAd.zzhy());
    }

    public final void setNativeAd(UnifiedNativeAd unifiedNativeAd) {
        zza((IObjectWrapper) unifiedNativeAd.zzhy());
    }

    private final void zza(IObjectWrapper iObjectWrapper) {
        WeakReference<View> weakReference = this.zzblh;
        View view = weakReference != null ? (View) weakReference.get() : null;
        if (view == null) {
            zzbbd.zzeo("NativeAdViewHolder.setNativeAd containerView doesn't exist, returning");
            return;
        }
        if (!zzblg.containsKey(view)) {
            zzblg.put(view, this);
        }
        zzadk zzadk = this.zzblf;
        if (zzadk != null) {
            try {
                zzadk.zza(iObjectWrapper);
            } catch (RemoteException e) {
                zzbbd.zzb("Unable to call setNativeAd on delegate", e);
            }
        }
    }

    public final void unregisterNativeAd() {
        zzadk zzadk = this.zzblf;
        if (zzadk != null) {
            try {
                zzadk.unregisterNativeAd();
            } catch (RemoteException e) {
                zzbbd.zzb("Unable to call unregisterNativeAd on delegate", e);
            }
        }
        WeakReference<View> weakReference = this.zzblh;
        View view = weakReference != null ? (View) weakReference.get() : null;
        if (view != null) {
            zzblg.remove(view);
        }
    }

    public final void setClickConfirmingView(View view) {
        try {
            this.zzblf.zzc(ObjectWrapper.wrap(view));
        } catch (RemoteException e) {
            zzbbd.zzb("Unable to call setClickConfirmingView on delegate", e);
        }
    }
}
