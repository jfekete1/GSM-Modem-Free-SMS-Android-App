package com.gsmmodem.Servers;

import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;

public class HttpSendSMSService extends IntentService {
    private static final String TAG = "HttpSendSMSService";
    MyHttpServer serverTest = null;

    public HttpSendSMSService() {
        super(TAG);
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        return super.onStartCommand(intent, i, i2);
    }

    /* access modifiers changed from: protected */
    public void onHandleIntent(Intent intent) {
        Toast.makeText(this, "Service Started!", 0).show();
    }
}
