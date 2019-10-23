package com.gsmmodem;

import android.app.Notification;
import android.app.NotificationManager;
import android.arch.persistence.room.Room;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.p004v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.gsmmodem.Servers.HttpSendSMSService;
import com.gsmmodem.Servers.MyHttpServer;
import com.gsmmodem.dao_impl.DatabaseAccess;
import com.gsmmodem.entities.AppSettingsEntity;

public class MainActivity extends AppCompatActivity {
    private static final String DATABASE_NAME = "gsm_modem_db";
    /* access modifiers changed from: private */
    public static boolean isStarted = false;
    /* access modifiers changed from: private */
    public AppSettingsEntity appSettingsEntity;
    /* access modifiers changed from: private */
    public BroadcastReceiver broadcastReceiver = new MySMSReceiver();
    private BroadcastReceiver broadcastReceiverNetworkState;
    /* access modifiers changed from: private */
    public DatabaseAccess databaseAccess;
    /* access modifiers changed from: private */
    public AdView mAdView;
    /* access modifiers changed from: private */
    public InterstitialAd mInterstitialAd;
    private MyHttpServer myHttpServer;
    /* access modifiers changed from: private */
    public String portNumber = "";
    private TextView serverAddress;
    /* access modifiers changed from: private */
    public TextView statusAction;
    /* access modifiers changed from: private */
    public ToggleButton toggleButton;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0524R.layout.activity_main);
        this.serverAddress = (TextView) findViewById(C0524R.C0526id.serverAddress);
        this.serverAddress.setText(getIpAccess());
        MobileAds.initialize(getApplicationContext(), getString(C0524R.string.admob_app_id));
        this.statusAction = (TextView) findViewById(C0524R.C0526id.statusAction);
        this.toggleButton = (ToggleButton) findViewById(C0524R.C0526id.startStopButton);
        this.toggleButton.setOnClickListener(new OnClickListener() {
            final Intent intent = new Intent(MainActivity.this, HttpSendSMSService.class);

            public void onClick(View view) {
                MainActivity.this.showAdd();
                if (!MainActivity.this.isConnectedInWifi()) {
                    MainActivity mainActivity = MainActivity.this;
                    StringBuilder sb = new StringBuilder();
                    sb.append("");
                    sb.append(MainActivity.this.getString(C0524R.string.wifi_message));
                    Toast.makeText(mainActivity, sb.toString(), 0).show();
                    MainActivity.this.toggleButton.setChecked(false);
                    MainActivity.this.statusAction.setText("Stopped");
                    MainActivity.this.statusAction.setBackgroundResource(C0524R.C0525drawable.status_style_red);
                } else if (!MainActivity.isStarted && MainActivity.this.startServer().booleanValue()) {
                    MainActivity.isStarted = true;
                    MainActivity.this.toggleButton.setChecked(true);
                    MainActivity.this.statusAction.setText("Started");
                    MainActivity.this.statusAction.setBackgroundResource(C0524R.C0525drawable.status_style_green);
                } else if (MainActivity.this.stopServer().booleanValue()) {
                    MainActivity.isStarted = false;
                    MainActivity.this.toggleButton.setChecked(false);
                    MainActivity.this.statusAction.setText("Stopped");
                    MainActivity.this.statusAction.setBackgroundResource(C0524R.C0525drawable.status_style_red);
                }
            }
        });
        findViewById(C0524R.C0526id.serverConfigs).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                MainActivity.this.startActivityBayName("settings");
            }
        });
        findViewById(C0524R.C0526id.jsonSamples).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                MainActivity.this.startActivityBayName("json_samples");
            }
        });
        findViewById(C0524R.C0526id.userManageButton).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                MainActivity.this.startActivityBayName("users");
            }
        });
        this.toggleButton.post(new Runnable() {
            public void run() {
                MainActivity mainActivity = MainActivity.this;
                mainActivity.databaseAccess = (DatabaseAccess) Room.databaseBuilder(mainActivity.getApplicationContext(), DatabaseAccess.class, MainActivity.DATABASE_NAME).fallbackToDestructiveMigration().allowMainThreadQueries().build();
                MainActivity.this.appSettingsEntity = new AppSettingsEntity();
                MainActivity mainActivity2 = MainActivity.this;
                mainActivity2.appSettingsEntity = mainActivity2.databaseAccess.daoAccess().fetchAppSettingsEntityById(Integer.valueOf(1));
                if (MainActivity.this.appSettingsEntity != null) {
                    MainActivity mainActivity3 = MainActivity.this;
                    mainActivity3.portNumber = mainActivity3.appSettingsEntity.getPORT();
                } else {
                    MainActivity.this.portNumber = "8090";
                }
                MainActivity.this.initBroadcastReceiverNetworkStateChanged();
            }
        });
        this.mAdView = (AdView) findViewById(C0524R.C0526id.adView);
        this.mAdView.loadAd(new Builder().build());
        this.mInterstitialAd = new InterstitialAd(getApplicationContext());
        this.mInterstitialAd.setAdUnitId(getString(C0524R.string.interstitial_ad_unit_id));
        this.mInterstitialAd.loadAd(new Builder().build());
        this.mInterstitialAd.setAdListener(new AdListener() {
            public void onAdClosed() {
                MainActivity.this.mInterstitialAd.loadAd(new Builder().build());
                MainActivity.this.mAdView.loadAd(new Builder().build());
            }
        });
    }

    /* access modifiers changed from: private */
    public void showAdd() {
        if (this.mInterstitialAd.isLoaded()) {
            this.mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0524R.C0527menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == C0524R.C0526id.action_settings) {
            startActivityBayName("settings");
        } else if (itemId == C0524R.C0526id.action_json_samples) {
            startActivityBayName("json_samples");
        } else if (itemId == C0524R.C0526id.action_user_manual) {
            startActivityBayName("user_manual");
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("isStarted", isStarted);
    }

    public void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        isStarted = bundle.getBoolean("isStarted");
        if (isStarted) {
            this.toggleButton.setChecked(true);
            this.statusAction.setText("Started");
            this.statusAction.setBackgroundColor(Color.parseColor("#04ab1a"));
            return;
        }
        this.toggleButton.setChecked(false);
        this.statusAction.setText("Stopped");
        this.statusAction.setBackgroundColor(Color.parseColor("#ab0407"));
    }

    public void onStart() {
        super.onStart();
    }

    public void onStop() {
        super.onStop();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this.toggleButton.post(new Runnable() {
            public void run() {
                MainActivity mainActivity = MainActivity.this;
                mainActivity.appSettingsEntity = mainActivity.databaseAccess.daoAccess().fetchAppSettingsEntityById(Integer.valueOf(1));
                if (MainActivity.this.appSettingsEntity != null) {
                    MainActivity mainActivity2 = MainActivity.this;
                    mainActivity2.portNumber = mainActivity2.appSettingsEntity.getPORT();
                    if (MainActivity.this.portNumber == null || "".equals(MainActivity.this.portNumber)) {
                        MainActivity.this.portNumber = "8090";
                    }
                    if (MainActivity.this.appSettingsEntity.getGET_SMS_SWITCH() != null && MainActivity.this.appSettingsEntity.getGET_SMS_SWITCH().booleanValue()) {
                        String get_sms_url = MainActivity.this.appSettingsEntity.getGET_SMS_URL();
                        IntentFilter intentFilter = new IntentFilter();
                        intentFilter.addAction(BuildConfig.APPLICATION_ID);
                        try {
                            MainActivity.this.registerReceiver(MainActivity.this.broadcastReceiver, intentFilter);
                            Notification.Builder contentTitle = new Notification.Builder(MainActivity.this.getApplicationContext()).setSmallIcon(C0524R.C0525drawable.mobile).setContentTitle("SMS Receiver Service");
                            StringBuilder sb = new StringBuilder();
                            sb.append("Service started for posting SMS to ");
                            sb.append(get_sms_url);
                            Notification.Builder when = contentTitle.setTicker(sb.toString()).setWhen(System.currentTimeMillis());
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("Service started for posting SMS to ");
                            sb2.append(get_sms_url);
                            ((NotificationManager) MainActivity.this.getSystemService("notification")).notify(0, when.setContentText(sb2.toString()).setPriority(2).build());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    MainActivity.this.portNumber = "8090";
                }
            }
        });
        setIpAccess();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        stopServer();
        isStarted = false;
        try {
            super.unregisterReceiver(this.broadcastReceiverNetworkState);
            this.appSettingsEntity = this.databaseAccess.daoAccess().fetchAppSettingsEntityById(Integer.valueOf(1));
            if (this.appSettingsEntity != null) {
                this.appSettingsEntity.setGET_SMS_SWITCH(Boolean.valueOf(false));
                this.databaseAccess.daoAccess().updateAppSettingsEntity(this.appSettingsEntity);
            }
            super.unregisterReceiver(this.broadcastReceiver);
            ((NotificationManager) getSystemService("notification")).notify(0, new Notification.Builder(getApplicationContext()).setSmallIcon(C0524R.C0525drawable.mobile).setContentTitle("SMS Receiver Service").setTicker("Service stopped for posting SMS to service").setWhen(System.currentTimeMillis()).setContentText("Service stopped for posting SMS to service").setPriority(2).build());
            this.databaseAccess.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        DatabaseAccess databaseAccess2 = this.databaseAccess;
        if (databaseAccess2 != null) {
            databaseAccess2.close();
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0046  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0051  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x005c  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0067  */
    /* JADX WARNING: Removed duplicated region for block: B:30:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void startActivityBayName(java.lang.String r3) {
        /*
            r2 = this;
            int r0 = r3.hashCode()
            r1 = -1354667086(0xffffffffaf416bb2, float:-1.7591509E-10)
            if (r0 == r1) goto L_0x0037
            r1 = 3327407(0x32c5af, float:4.66269E-39)
            if (r0 == r1) goto L_0x002d
            r1 = 111578632(0x6a68e08, float:6.2650956E-35)
            if (r0 == r1) goto L_0x0023
            r1 = 1434631203(0x5582bc23, float:1.79680691E13)
            if (r0 == r1) goto L_0x0019
            goto L_0x0041
        L_0x0019:
            java.lang.String r0 = "settings"
            boolean r3 = r3.equals(r0)
            if (r3 == 0) goto L_0x0041
            r3 = 0
            goto L_0x0042
        L_0x0023:
            java.lang.String r0 = "users"
            boolean r3 = r3.equals(r0)
            if (r3 == 0) goto L_0x0041
            r3 = 1
            goto L_0x0042
        L_0x002d:
            java.lang.String r0 = "logs"
            boolean r3 = r3.equals(r0)
            if (r3 == 0) goto L_0x0041
            r3 = 2
            goto L_0x0042
        L_0x0037:
            java.lang.String r0 = "json_samples"
            boolean r3 = r3.equals(r0)
            if (r3 == 0) goto L_0x0041
            r3 = 3
            goto L_0x0042
        L_0x0041:
            r3 = -1
        L_0x0042:
            switch(r3) {
                case 0: goto L_0x0067;
                case 1: goto L_0x005c;
                case 2: goto L_0x0051;
                case 3: goto L_0x0046;
                default: goto L_0x0045;
            }
        L_0x0045:
            goto L_0x0071
        L_0x0046:
            android.content.Intent r3 = new android.content.Intent
            java.lang.Class<com.gsmmodem.JsonSamples> r0 = com.gsmmodem.JsonSamples.class
            r3.<init>(r2, r0)
            r2.startActivity(r3)
            goto L_0x0071
        L_0x0051:
            android.content.Intent r3 = new android.content.Intent
            java.lang.Class<com.gsmmodem.Logs> r0 = com.gsmmodem.Logs.class
            r3.<init>(r2, r0)
            r2.startActivity(r3)
            goto L_0x0071
        L_0x005c:
            android.content.Intent r3 = new android.content.Intent
            java.lang.Class<com.gsmmodem.UserManagement> r0 = com.gsmmodem.UserManagement.class
            r3.<init>(r2, r0)
            r2.startActivity(r3)
            goto L_0x0071
        L_0x0067:
            android.content.Intent r3 = new android.content.Intent
            java.lang.Class<com.gsmmodem.Settings> r0 = com.gsmmodem.Settings.class
            r3.<init>(r2, r0)
            r2.startActivity(r3)
        L_0x0071:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.gsmmodem.MainActivity.startActivityBayName(java.lang.String):void");
    }

    public boolean isConnectedInWifi() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService("wifi");
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isAvailable() && activeNetworkInfo.isConnected() && wifiManager.isWifiEnabled() && "WIFI".equals(activeNetworkInfo.getTypeName());
    }

    private String getIpAccess() {
        int ipAddress = ((WifiManager) getApplicationContext().getSystemService("wifi")).getConnectionInfo().getIpAddress();
        String format = String.format("%d.%d.%d.%d", new Object[]{Integer.valueOf(ipAddress & 255), Integer.valueOf((ipAddress >> 8) & 255), Integer.valueOf((ipAddress >> 16) & 255), Integer.valueOf((ipAddress >> 24) & 255)});
        StringBuilder sb = new StringBuilder();
        sb.append("http://");
        sb.append(format);
        sb.append(":");
        sb.append(this.portNumber);
        return sb.toString();
    }

    /* access modifiers changed from: private */
    public Boolean startServer() {
        if (!isStarted) {
            try {
                if (this.portNumber == null || "".equals(this.portNumber)) {
                    this.portNumber = "8080";
                }
                this.myHttpServer = new MyHttpServer(getApplicationContext(), Integer.parseInt(this.portNumber));
                this.myHttpServer.start();
                return Boolean.valueOf(true);
            } catch (Exception e) {
                e.printStackTrace();
                StringBuilder sb = new StringBuilder();
                sb.append("The PORT ");
                sb.append(this.portNumber);
                sb.append(" doesn't work, please change it between 1000 and 9999.");
                Toast.makeText(this, sb.toString(), 0).show();
            }
        }
        return Boolean.valueOf(false);
    }

    /* access modifiers changed from: private */
    public Boolean stopServer() {
        if (isStarted) {
            MyHttpServer myHttpServer2 = this.myHttpServer;
            if (myHttpServer2 != null) {
                myHttpServer2.stop();
                return Boolean.valueOf(true);
            }
        }
        return Boolean.valueOf(false);
    }

    /* access modifiers changed from: private */
    public void initBroadcastReceiverNetworkStateChanged() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        intentFilter.addAction("android.net.wifi.STATE_CHANGE");
        this.broadcastReceiverNetworkState = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                MainActivity.this.setIpAccess();
            }
        };
        super.registerReceiver(this.broadcastReceiverNetworkState, intentFilter);
    }

    /* access modifiers changed from: private */
    public void setIpAccess() {
        this.serverAddress.setText(getIpAccess());
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        if (i == 1) {
            if (iArr.length <= 0 || iArr[0] != 0) {
                Toast.makeText(this, "Permission denied to read your External storage", 0).show();
            }
        }
    }
}
