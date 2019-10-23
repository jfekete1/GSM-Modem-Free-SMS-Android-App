package com.gsmmodem;

import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.p004v7.app.AppCompatActivity;
import android.support.p004v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.gsmmodem.dao_impl.DatabaseAccess;
import com.gsmmodem.entities.AppSettingsEntity;

public class Settings extends AppCompatActivity {
    private static final String DATABASE_NAME = "gsm_modem_db";
    private final String TAG = "GSM";
    AppSettingsEntity appSettingsEntity;
    /* access modifiers changed from: private */
    public DatabaseAccess databaseAccess;
    /* access modifiers changed from: private */
    public EditText getSMSAtURL;
    /* access modifiers changed from: private */
    public Switch getSMSAtURLSwitch;
    /* access modifiers changed from: private */
    public AdView mAdView;
    /* access modifiers changed from: private */
    public InterstitialAd mInterstitialAd;
    /* access modifiers changed from: private */
    public EditText noOfMinutes;
    /* access modifiers changed from: private */
    public EditText noOfSMS;
    /* access modifiers changed from: private */
    public Switch noOfSMSPerMinutesSwitch;
    private Integer operationIndex = Integer.valueOf(-1);
    EditText portNumberEditText;
    private String portNumberStr = "";

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0524R.layout.activity_settings);
        this.portNumberEditText = (EditText) findViewById(C0524R.C0526id.portNumber);
        this.getSMSAtURLSwitch = (Switch) findViewById(C0524R.C0526id.getSMSAtURLSwitch);
        this.noOfSMSPerMinutesSwitch = (Switch) findViewById(C0524R.C0526id.noOfSMSPerMinutesSwitch);
        this.getSMSAtURL = (EditText) findViewById(C0524R.C0526id.getSMSAtURL);
        this.noOfSMS = (EditText) findViewById(C0524R.C0526id.noOfSMS);
        this.noOfMinutes = (EditText) findViewById(C0524R.C0526id.noOfMinutes);
        ((Button) findViewById(C0524R.C0526id.saveSettings)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (Settings.this.mInterstitialAd.isLoaded()) {
                    Settings.this.mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
                if (Settings.this.portNumberEditText.getText() != null && Settings.this.portNumberEditText.getText().toString().equals("")) {
                    Toast.makeText(Settings.this, "Port number is required!", 0).show();
                } else if (Settings.this.getSMSAtURLSwitch != null && Settings.this.getSMSAtURLSwitch.isChecked() && Settings.this.getSMSAtURL != null && Settings.this.getSMSAtURL.getText().toString().equals("")) {
                    Toast.makeText(Settings.this, "GET SMS at URL is required!", 0).show();
                } else if (Settings.this.noOfSMSPerMinutesSwitch != null && Settings.this.noOfSMSPerMinutesSwitch.isChecked() && Settings.this.noOfSMS != null && Settings.this.noOfSMS.getText().toString().equals("") && Settings.this.noOfMinutes != null && Settings.this.noOfMinutes.getText().toString().equals("")) {
                    Toast.makeText(Settings.this, "SMS and Minutes are required!", 0).show();
                } else if (Settings.this.appSettingsEntity != null) {
                    Settings.this.setObjectProperties();
                    Settings.this.databaseAccess.daoAccess().updateAppSettingsEntity(Settings.this.appSettingsEntity);
                    Toast.makeText(Settings.this, "New parameters updated!", 0).show();
                } else {
                    Settings.this.appSettingsEntity = new AppSettingsEntity();
                    Settings.this.setObjectProperties();
                    Settings.this.databaseAccess.daoAccess().insertAppSettingsEntity(Settings.this.appSettingsEntity);
                    Toast.makeText(Settings.this, "New parameters inserted!", 0).show();
                }
            }
        });
        this.getSMSAtURLSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    try {
                        Builder contentTitle = new Builder(Settings.this.getApplicationContext()).setSmallIcon(C0524R.C0525drawable.mobile).setContentTitle("SMS Receiver Service");
                        StringBuilder sb = new StringBuilder();
                        sb.append("Service started for posting SMS to ");
                        sb.append(Settings.this.getSMSAtURL.getText());
                        Builder when = contentTitle.setTicker(sb.toString()).setWhen(System.currentTimeMillis());
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Service started for posting SMS to ");
                        sb2.append(Settings.this.getSMSAtURL.getText());
                        ((NotificationManager) Settings.this.getSystemService("notification")).notify(0, when.setContentText(sb2.toString()).setPriority(2).build());
                        Settings.this.getSMSAtURL.setEnabled(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Builder contentTitle2 = new Builder(Settings.this.getApplicationContext()).setSmallIcon(C0524R.C0525drawable.mobile).setContentTitle("SMS Receiver Service");
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("Service stopped for posting SMS to ");
                        sb3.append(Settings.this.getSMSAtURL.getText());
                        Builder when2 = contentTitle2.setTicker(sb3.toString()).setWhen(System.currentTimeMillis());
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("Service stopped for posting SMS to ");
                        sb4.append(Settings.this.getSMSAtURL.getText());
                        ((NotificationManager) Settings.this.getSystemService("notification")).notify(0, when2.setContentText(sb4.toString()).setPriority(2).build());
                        Settings.this.getSMSAtURL.setEnabled(false);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
                if (Settings.this.mInterstitialAd.isLoaded()) {
                    Settings.this.mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
            }
        });
        this.noOfSMSPerMinutesSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    Settings.this.noOfSMS.setEnabled(true);
                    Settings.this.noOfMinutes.setEnabled(true);
                } else {
                    Settings.this.noOfSMS.setEnabled(false);
                    Settings.this.noOfMinutes.setEnabled(false);
                }
                if (Settings.this.mInterstitialAd.isLoaded()) {
                    Settings.this.mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
            }
        });
        ((Button) findViewById(C0524R.C0526id.cancelSettingsButton)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Settings.this.finish();
            }
        });
        setSupportActionBar((Toolbar) findViewById(C0524R.C0526id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.noOfMinutes.post(new Runnable() {
            public void run() {
                MobileAds.initialize(Settings.this.getApplicationContext(), Settings.this.getString(C0524R.string.admob_app_id));
                Settings settings = Settings.this;
                settings.mAdView = (AdView) settings.findViewById(C0524R.C0526id.adView);
                Settings.this.mAdView.loadAd(new AdRequest.Builder().build());
                Settings settings2 = Settings.this;
                settings2.mInterstitialAd = new InterstitialAd(settings2.getApplicationContext());
                Settings.this.mInterstitialAd.setAdUnitId(Settings.this.getString(C0524R.string.interstitial_ad_unit_id));
                Settings.this.mInterstitialAd.loadAd(new AdRequest.Builder().build());
                Settings settings3 = Settings.this;
                settings3.databaseAccess = (DatabaseAccess) Room.databaseBuilder(settings3.getApplicationContext(), DatabaseAccess.class, Settings.DATABASE_NAME).fallbackToDestructiveMigration().allowMainThreadQueries().build();
                Settings settings4 = Settings.this;
                settings4.appSettingsEntity = settings4.databaseAccess.daoAccess().fetchAppSettingsEntityById(Integer.valueOf(1));
                if (Settings.this.appSettingsEntity != null) {
                    Settings.this.runOnUiThread(new Runnable() {
                        public void run() {
                            String str;
                            String str2;
                            Settings.this.portNumberEditText.setText(Settings.this.appSettingsEntity.getPORT() == null ? "8090" : Settings.this.appSettingsEntity.getPORT());
                            boolean z = false;
                            Settings.this.getSMSAtURLSwitch.setChecked(Settings.this.appSettingsEntity.getGET_SMS_SWITCH() == null ? false : Settings.this.appSettingsEntity.getGET_SMS_SWITCH().booleanValue());
                            Settings.this.getSMSAtURL.setEnabled(Settings.this.appSettingsEntity.getGET_SMS_SWITCH() == null ? false : Settings.this.appSettingsEntity.getGET_SMS_SWITCH().booleanValue());
                            Settings.this.getSMSAtURL.setText(Settings.this.appSettingsEntity.getGET_SMS_URL() == null ? "" : Settings.this.appSettingsEntity.getGET_SMS_URL());
                            Settings.this.noOfSMSPerMinutesSwitch.setChecked(Settings.this.appSettingsEntity.getOUTGOING_SMS_SWITCH() == null ? false : Settings.this.appSettingsEntity.getOUTGOING_SMS_SWITCH().booleanValue());
                            Settings.this.noOfSMS.setEnabled(Settings.this.appSettingsEntity.getOUTGOING_SMS_SWITCH() == null ? false : Settings.this.appSettingsEntity.getOUTGOING_SMS_SWITCH().booleanValue());
                            EditText access$500 = Settings.this.noOfMinutes;
                            if (Settings.this.appSettingsEntity.getOUTGOING_SMS_SWITCH() != null) {
                                z = Settings.this.appSettingsEntity.getOUTGOING_SMS_SWITCH().booleanValue();
                            }
                            access$500.setEnabled(z);
                            EditText access$400 = Settings.this.noOfSMS;
                            if (Settings.this.appSettingsEntity.getNO_SMS() == null) {
                                str = "0";
                            } else {
                                StringBuilder sb = new StringBuilder();
                                sb.append("");
                                sb.append(Settings.this.appSettingsEntity.getNO_SMS());
                                str = sb.toString();
                            }
                            access$400.setText(str);
                            EditText access$5002 = Settings.this.noOfMinutes;
                            if (Settings.this.appSettingsEntity.getNO_MINUTES() == null) {
                                str2 = "0";
                            } else {
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append("");
                                sb2.append(Settings.this.appSettingsEntity.getNO_MINUTES());
                                str2 = sb2.toString();
                            }
                            access$5002.setText(str2);
                        }
                    });
                } else {
                    Settings.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Settings.this.portNumberEditText.setText("");
                            Settings.this.getSMSAtURLSwitch.setChecked(false);
                            Settings.this.getSMSAtURL.setEnabled(false);
                            Settings.this.getSMSAtURL.setText("");
                            Settings.this.noOfSMSPerMinutesSwitch.setChecked(false);
                            Settings.this.noOfSMS.setEnabled(false);
                            Settings.this.noOfMinutes.setEnabled(false);
                            Settings.this.noOfSMS.setText("");
                            Settings.this.noOfMinutes.setText("");
                        }
                    });
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void setObjectProperties() {
        this.appSettingsEntity.setPORT(this.portNumberEditText.getText() == null ? "8090" : this.portNumberEditText.getText().toString());
        this.appSettingsEntity.setGET_SMS_SWITCH(Boolean.valueOf(this.getSMSAtURLSwitch.isChecked()));
        this.appSettingsEntity.setGET_SMS_URL(this.getSMSAtURL.getText() == null ? "" : this.getSMSAtURL.getText().toString());
        this.appSettingsEntity.setOUTGOING_SMS_SWITCH(Boolean.valueOf(this.noOfSMSPerMinutesSwitch.isChecked()));
        int i = 0;
        this.appSettingsEntity.setNO_SMS(Integer.valueOf((this.noOfSMS.getText() != null && !this.noOfSMS.getText().toString().equals("")) ? Integer.parseInt(this.noOfSMS.getText().toString()) : 0));
        AppSettingsEntity appSettingsEntity2 = this.appSettingsEntity;
        if (this.noOfMinutes.getText() != null && !this.noOfMinutes.getText().toString().equals("")) {
            i = Integer.parseInt(this.noOfMinutes.getText().toString());
        }
        appSettingsEntity2.setNO_MINUTES(Integer.valueOf(i));
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
