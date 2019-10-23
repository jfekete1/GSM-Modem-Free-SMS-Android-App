package com.google.android.gms.ads;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import com.google.android.gms.common.annotation.KeepForSdkWithMembers;
import com.google.android.gms.dynamic.ObjectWrapper;
import com.google.android.gms.internal.ads.zzaop;
import com.google.android.gms.internal.ads.zzbbd;
import com.google.android.gms.internal.ads.zzwu;

@KeepForSdkWithMembers
public class AdActivity extends Activity {
    public static final String CLASS_NAME = "com.google.android.gms.ads.AdActivity";
    public static final String SIMPLE_CLASS_NAME = "AdActivity";
    private zzaop zzvh;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.zzvh = zzwu.zzpw().zzb((Activity) this);
        zzaop zzaop = this.zzvh;
        if (zzaop == null) {
            zzbbd.zzd("#007 Could not call remote method.", null);
            finish();
            return;
        }
        try {
            zzaop.onCreate(bundle);
        } catch (RemoteException e) {
            zzbbd.zzd("#007 Could not call remote method.", e);
            finish();
        }
    }

    /* access modifiers changed from: protected */
    public void onRestart() {
        super.onRestart();
        try {
            if (this.zzvh != null) {
                this.zzvh.onRestart();
            }
        } catch (RemoteException e) {
            zzbbd.zzd("#007 Could not call remote method.", e);
            finish();
        }
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        try {
            if (this.zzvh != null) {
                this.zzvh.onStart();
            }
        } catch (RemoteException e) {
            zzbbd.zzd("#007 Could not call remote method.", e);
            finish();
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        try {
            if (this.zzvh != null) {
                this.zzvh.onResume();
            }
        } catch (RemoteException e) {
            zzbbd.zzd("#007 Could not call remote method.", e);
            finish();
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        try {
            if (this.zzvh != null) {
                this.zzvh.onPause();
            }
        } catch (RemoteException e) {
            zzbbd.zzd("#007 Could not call remote method.", e);
            finish();
        }
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle bundle) {
        try {
            if (this.zzvh != null) {
                this.zzvh.onSaveInstanceState(bundle);
            }
        } catch (RemoteException e) {
            zzbbd.zzd("#007 Could not call remote method.", e);
            finish();
        }
        super.onSaveInstanceState(bundle);
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        try {
            if (this.zzvh != null) {
                this.zzvh.onStop();
            }
        } catch (RemoteException e) {
            zzbbd.zzd("#007 Could not call remote method.", e);
            finish();
        }
        super.onStop();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        try {
            if (this.zzvh != null) {
                this.zzvh.onDestroy();
            }
        } catch (RemoteException e) {
            zzbbd.zzd("#007 Could not call remote method.", e);
        }
        super.onDestroy();
    }

    private final void zzay() {
        zzaop zzaop = this.zzvh;
        if (zzaop != null) {
            try {
                zzaop.zzay();
            } catch (RemoteException e) {
                zzbbd.zzd("#007 Could not call remote method.", e);
            }
        }
    }

    public void setContentView(int i) {
        super.setContentView(i);
        zzay();
    }

    public void setContentView(View view) {
        super.setContentView(view);
        zzay();
    }

    public void setContentView(View view, LayoutParams layoutParams) {
        super.setContentView(view, layoutParams);
        zzay();
    }

    public void onBackPressed() {
        boolean z = true;
        try {
            if (this.zzvh != null) {
                z = this.zzvh.zzvq();
            }
        } catch (RemoteException e) {
            zzbbd.zzd("#007 Could not call remote method.", e);
        }
        if (z) {
            super.onBackPressed();
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        try {
            this.zzvh.onActivityResult(i, i2, intent);
        } catch (Exception e) {
            zzbbd.zzd("#007 Could not call remote method.", e);
        }
        super.onActivityResult(i, i2, intent);
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        try {
            this.zzvh.zzq(ObjectWrapper.wrap(configuration));
        } catch (RemoteException e) {
            zzbbd.zzd("#007 Could not call remote method.", e);
        }
    }
}
