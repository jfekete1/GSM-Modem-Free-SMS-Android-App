package com.gsmmodem;

import android.os.Bundle;
import android.support.p004v7.app.AppCompatActivity;
import android.support.p004v7.widget.Toolbar;

public class Logs extends AppCompatActivity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0524R.layout.activity_logs);
        setSupportActionBar((Toolbar) findViewById(C0524R.C0526id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
