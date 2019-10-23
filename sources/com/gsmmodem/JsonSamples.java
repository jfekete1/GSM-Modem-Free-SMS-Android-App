package com.gsmmodem;

import android.arch.persistence.room.Room;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.p004v7.app.AppCompatActivity;
import android.support.p004v7.widget.Toolbar;
import android.widget.TextView;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.gsmmodem.dao_impl.DatabaseAccess;
import com.gsmmodem.entities.AppSettingsEntity;

public class JsonSamples extends AppCompatActivity {
    private static final String DATABASE_NAME = "gsm_modem_db";
    /* access modifiers changed from: private */
    public AppSettingsEntity appSettingsEntity;
    /* access modifiers changed from: private */
    public DatabaseAccess databaseAccess;
    /* access modifiers changed from: private */
    public AdView mAdView;
    /* access modifiers changed from: private */
    public InterstitialAd mInterstitialAd;
    /* access modifiers changed from: private */
    public String portNumber;
    /* access modifiers changed from: private */
    public TextView urlToSemdSMS;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0524R.layout.activity_json_samples);
        setSupportActionBar((Toolbar) findViewById(C0524R.C0526id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.urlToSemdSMS = (TextView) findViewById(C0524R.C0526id.textView3);
        this.urlToSemdSMS.post(new Runnable() {
            public void run() {
                MobileAds.initialize(JsonSamples.this.getApplicationContext(), JsonSamples.this.getString(C0524R.string.admob_app_id));
                JsonSamples jsonSamples = JsonSamples.this;
                jsonSamples.mAdView = (AdView) jsonSamples.findViewById(C0524R.C0526id.adView);
                JsonSamples.this.mAdView.loadAd(new Builder().build());
                JsonSamples jsonSamples2 = JsonSamples.this;
                jsonSamples2.mInterstitialAd = new InterstitialAd(jsonSamples2.getApplicationContext());
                JsonSamples.this.mInterstitialAd.setAdUnitId(JsonSamples.this.getString(C0524R.string.interstitial_ad_unit_id));
                JsonSamples.this.mInterstitialAd.loadAd(new Builder().build());
                JsonSamples jsonSamples3 = JsonSamples.this;
                jsonSamples3.databaseAccess = (DatabaseAccess) Room.databaseBuilder(jsonSamples3.getApplicationContext(), DatabaseAccess.class, JsonSamples.DATABASE_NAME).fallbackToDestructiveMigration().allowMainThreadQueries().build();
                JsonSamples.this.appSettingsEntity = new AppSettingsEntity();
                JsonSamples jsonSamples4 = JsonSamples.this;
                jsonSamples4.appSettingsEntity = jsonSamples4.databaseAccess.daoAccess().fetchAppSettingsEntityById(Integer.valueOf(1));
                if (JsonSamples.this.appSettingsEntity == null) {
                    JsonSamples.this.portNumber = "8090";
                } else if (JsonSamples.this.appSettingsEntity.getPORT() == null || "".equals(JsonSamples.this.appSettingsEntity.getPORT())) {
                    JsonSamples.this.portNumber = "8090";
                } else {
                    JsonSamples jsonSamples5 = JsonSamples.this;
                    jsonSamples5.portNumber = jsonSamples5.appSettingsEntity.getPORT();
                }
                if (!(JsonSamples.this.appSettingsEntity == null || JsonSamples.this.appSettingsEntity.getGET_SMS_SWITCH() == null || !JsonSamples.this.appSettingsEntity.getGET_SMS_SWITCH().booleanValue())) {
                    ((TextView) JsonSamples.this.findViewById(C0524R.C0526id.textView5)).setText(JsonSamples.this.appSettingsEntity.getGET_SMS_URL());
                }
                TextView access$600 = JsonSamples.this.urlToSemdSMS;
                StringBuilder sb = new StringBuilder();
                sb.append(JsonSamples.this.getIpAccess());
                sb.append("/SendSMS?username=Sadiq&password=1234&phone=0300xxxxxxx&message=hello");
                access$600.setText(sb.toString());
            }
        });
    }

    /* access modifiers changed from: private */
    public String getIpAccess() {
        int ipAddress = ((WifiManager) getApplicationContext().getSystemService("wifi")).getConnectionInfo().getIpAddress();
        String format = String.format("%d.%d.%d.%d", new Object[]{Integer.valueOf(ipAddress & 255), Integer.valueOf((ipAddress >> 8) & 255), Integer.valueOf((ipAddress >> 16) & 255), Integer.valueOf((ipAddress >> 24) & 255)});
        StringBuilder sb = new StringBuilder();
        sb.append("http://");
        sb.append(format);
        sb.append(":");
        sb.append(this.portNumber);
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
    }
}
