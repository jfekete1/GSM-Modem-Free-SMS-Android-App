package com.gsmmodem;

import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.arch.persistence.room.Room;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.p001v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.gsmmodem.dao_impl.DatabaseAccess;
import com.gsmmodem.entities.AppSettingsEntity;
import com.gsmmodem.entities.UsersEntity;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.json.JSONException;
import org.json.JSONObject;

public class MySMSReceiver extends BroadcastReceiver {
    private static final String DATABASE_NAME = "gsm_modem_db";
    private AppSettingsEntity appSettingsEntity;
    private Context context;
    private DatabaseAccess databaseAccess;
    final SmsManager sms = SmsManager.getDefault();
    private UsersEntity usersEntity;

    public void onReceive(final Context context2, Intent intent) {
        this.databaseAccess = (DatabaseAccess) Room.databaseBuilder(context2, DatabaseAccess.class, DATABASE_NAME).fallbackToDestructiveMigration().allowMainThreadQueries().build();
        DatabaseAccess databaseAccess2 = this.databaseAccess;
        if (databaseAccess2 != null) {
            this.appSettingsEntity = databaseAccess2.daoAccess().fetchAppSettingsEntityById(Integer.valueOf(1));
            String str = null;
            Boolean valueOf = Boolean.valueOf(false);
            AppSettingsEntity appSettingsEntity2 = this.appSettingsEntity;
            if (appSettingsEntity2 != null) {
                str = appSettingsEntity2.getGET_SMS_URL();
                valueOf = this.appSettingsEntity.getGET_SMS_SWITCH();
            }
            Bundle extras = intent.getExtras();
            if (extras != null) {
                try {
                    String string = extras.getString("phone");
                    String string2 = extras.getString("message");
                    if (str != null) {
                        try {
                            if (valueOf.booleanValue()) {
                                Builder contentTitle = new Builder(context2).setSmallIcon(C0524R.C0525drawable.mobile).setContentTitle("SMS Receiver Service");
                                StringBuilder sb = new StringBuilder();
                                sb.append("SMS received by ");
                                sb.append(string);
                                sb.append(" and posted to service");
                                Builder when = contentTitle.setTicker(sb.toString()).setWhen(System.currentTimeMillis());
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append("SMS received by ");
                                sb2.append(string);
                                sb2.append(" and posted to service");
                                ((NotificationManager) context2.getSystemService("notification")).notify(0, when.setContentText(sb2.toString()).setPriority(2).build());
                                RequestQueue newRequestQueue = Volley.newRequestQueue(context2);
                                try {
                                    string2 = URLEncoder.encode(string2, "UTF-8");
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                                StringBuilder sb3 = new StringBuilder();
                                sb3.append(str);
                                sb3.append("?phoneNumber=");
                                sb3.append(string);
                                sb3.append("&message=");
                                sb3.append(string2);
                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(0, sb3.toString(), null, new Listener<JSONObject>() {
                                    public void onResponse(JSONObject jSONObject) {
                                        try {
                                            Integer valueOf = Integer.valueOf(jSONObject.getInt(NotificationCompat.CATEGORY_STATUS));
                                            String string = jSONObject.getString("message");
                                            Context context = context2;
                                            StringBuilder sb = new StringBuilder();
                                            sb.append("Status: ");
                                            sb.append(valueOf);
                                            sb.append(" | Message: ");
                                            sb.append(string);
                                            Toast.makeText(context, sb.toString(), 0).show();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new ErrorListener() {
                                    public void onErrorResponse(VolleyError volleyError) {
                                        Toast.makeText(context2, "Error Occurred!", 0).show();
                                    }
                                });
                                newRequestQueue.add(jsonObjectRequest);
                            }
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                } catch (Exception e3) {
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("Exception smsReceiver");
                    sb4.append(e3);
                    Log.e("SmsReceiver", sb4.toString());
                }
            }
        }
    }
}
