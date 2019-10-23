package com.gsmmodem.Servers;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.support.p001v4.app.NotificationCompat;
import android.util.Log;
import com.gsmmodem.dao_impl.DatabaseAccess;
import com.gsmmodem.entities.AppSettingsEntity;
import com.gsmmodem.entities.UsersEntity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import p005fi.iki.elonen.NanoHTTPD;
import p005fi.iki.elonen.NanoHTTPD.IHTTPSession;
import p005fi.iki.elonen.NanoHTTPD.Response;
import p005fi.iki.elonen.NanoHTTPD.Response.Status;

public class MyHttpServer extends NanoHTTPD {
    private static final String DATABASE_NAME = "gsm_modem_db";
    public static final long MINUTE_MILLIS = 60000;
    private static final String PACKAGE_TO_COM = "helper.gsm.com.gsmhelpertool";
    public static final long SECOND_MILLIS = 1000;
    private final String TAG = "GSM Modem: ";
    private AppSettingsEntity appSettingsEntity;
    /* access modifiers changed from: private */
    public Context context;
    private DatabaseAccess databaseAccess;
    Intent intent;
    final Map<String, String> response = new HashMap();
    private UsersEntity usersEntity;

    public MyHttpServer(Context context2, int i) {
        super(i);
        this.context = context2;
        this.databaseAccess = (DatabaseAccess) Room.databaseBuilder(context2, DatabaseAccess.class, DATABASE_NAME).fallbackToDestructiveMigration().allowMainThreadQueries().build();
    }

    public MyHttpServer(String str, int i) {
        super(str, i);
    }

    public Response serve(IHTTPSession iHTTPSession) {
        this.intent = new Intent();
        this.intent.addFlags(32);
        this.intent.setAction(PACKAGE_TO_COM);
        String uri = iHTTPSession.getUri();
        StringBuilder sb = new StringBuilder();
        sb.append("url: ");
        sb.append(uri);
        Log.v("GSM Modem: ", sb.toString());
        Map parms = iHTTPSession.getParms();
        if (((uri.hashCode() == -342705790 && uri.equals("/SendSMS")) ? (char) 0 : 65535) != 0) {
            return newFixedLengthResponse(Status.OK, NanoHTTPD.MIME_HTML, homePage());
        }
        sendSMS(parms);
        return newFixedLengthResponse(Status.OK, "text/json", new JSONObject(this.response).toString());
    }

    private void sendSMS(final Map<String, String> map) {
        if (!isPackageInstalled(PACKAGE_TO_COM, this.context.getPackageManager())) {
            this.response.put(NotificationCompat.CATEGORY_STATUS, "404");
            this.response.put("message", "GSM helper tool app is not installed in your device, please visit link for guide!");
            this.response.put("guideLink", "http://sindhitutorials.com/blog/gsm-modem-free-sms-android-app/");
        } else if (map.get("username") == null || map.get("password") == null) {
            this.response.put(NotificationCompat.CATEGORY_STATUS, "422");
            this.response.put("message", "username and password is required!");
        } else if (map.get("username") != null && ((String) map.get("username")).length() <= 0) {
            this.response.put(NotificationCompat.CATEGORY_STATUS, "422");
            this.response.put("message", "username inappropriate length!");
        } else if (map.get("password") != null && ((String) map.get("password")).length() <= 0) {
            this.response.put(NotificationCompat.CATEGORY_STATUS, "422");
            this.response.put("message", "password inappropriate length!");
        } else if (map.get("phone") == null) {
            this.response.put(NotificationCompat.CATEGORY_STATUS, "422");
            this.response.put("message", "phone number is required!");
        } else if (map.get("message") == null) {
            this.response.put(NotificationCompat.CATEGORY_STATUS, "422");
            this.response.put("message", "message is required!");
        } else if (((String) map.get("phone")).length() <= 3) {
            this.response.put(NotificationCompat.CATEGORY_STATUS, "422");
            this.response.put("message", "phone number inappropriate length!");
        } else if (((String) map.get("message")).length() <= 1) {
            this.response.put(NotificationCompat.CATEGORY_STATUS, "422");
            this.response.put("message", "message inappropriate length!");
        } else {
            this.usersEntity = this.databaseAccess.daoAccess().fetchUserByNameAndPass((String) map.get("username"), (String) map.get("password"));
            if (this.usersEntity != null) {
                this.appSettingsEntity = this.databaseAccess.daoAccess().fetchAppSettingsEntityById(Integer.valueOf(1));
                AppSettingsEntity appSettingsEntity2 = this.appSettingsEntity;
                if (appSettingsEntity2 == null || !appSettingsEntity2.getOUTGOING_SMS_SWITCH().booleanValue()) {
                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                MyHttpServer.this.intent.putExtra("phone", ((String) map.get("phone")).toLowerCase());
                                MyHttpServer.this.intent.putExtra("message", (String) map.get("message"));
                                MyHttpServer.this.context.sendBroadcast(MyHttpServer.this.intent);
                            } catch (Exception e) {
                                MyHttpServer.this.response.put(NotificationCompat.CATEGORY_STATUS, "500");
                                StringBuilder sb = new StringBuilder();
                                sb.append("Exception: ");
                                sb.append(e.getMessage());
                                MyHttpServer.this.response.put("message", sb.toString());
                            }
                        }
                    }).start();
                    this.response.put(NotificationCompat.CATEGORY_STATUS, "200");
                    this.response.put("message", "Message has been sent");
                    this.response.put("phone", map.get("phone"));
                    return;
                }
                Integer valueOf = Integer.valueOf(0);
                try {
                    valueOf = Integer.valueOf(minutesDiff(new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").parse(this.appSettingsEntity.getDATE_LAST_SMS()), new Date()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (this.appSettingsEntity.getSMS_NUMBER_COUNT() == null) {
                    AppSettingsEntity appSettingsEntity3 = this.appSettingsEntity;
                    StringBuilder sb = new StringBuilder();
                    sb.append("");
                    sb.append(new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(new Date()));
                    appSettingsEntity3.setDATE_LAST_SMS(sb.toString());
                    this.appSettingsEntity.setSMS_NUMBER_COUNT(Integer.valueOf(0));
                }
                if (this.appSettingsEntity.getSMS_NUMBER_COUNT().intValue() < this.appSettingsEntity.getNO_SMS().intValue() || valueOf.intValue() >= this.appSettingsEntity.getNO_MINUTES().intValue()) {
                    if (this.appSettingsEntity.getSMS_NUMBER_COUNT().intValue() >= this.appSettingsEntity.getNO_SMS().intValue() && valueOf.intValue() >= this.appSettingsEntity.getNO_MINUTES().intValue()) {
                        AppSettingsEntity appSettingsEntity4 = this.appSettingsEntity;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("");
                        sb2.append(new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(new Date()));
                        appSettingsEntity4.setDATE_LAST_SMS(sb2.toString());
                        this.appSettingsEntity.setSMS_NUMBER_COUNT(Integer.valueOf(0));
                    }
                    AppSettingsEntity appSettingsEntity5 = this.appSettingsEntity;
                    appSettingsEntity5.setSMS_NUMBER_COUNT(Integer.valueOf(appSettingsEntity5.getSMS_NUMBER_COUNT().intValue() + 1));
                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                MyHttpServer.this.intent.putExtra("phone", ((String) map.get("phone")).toLowerCase());
                                MyHttpServer.this.intent.putExtra("message", (String) map.get("message"));
                                MyHttpServer.this.context.sendBroadcast(MyHttpServer.this.intent);
                            } catch (Exception e) {
                                MyHttpServer.this.response.put(NotificationCompat.CATEGORY_STATUS, "500");
                                StringBuilder sb = new StringBuilder();
                                sb.append("Exception: ");
                                sb.append(e.getMessage());
                                MyHttpServer.this.response.put("message", sb.toString());
                            }
                        }
                    }).start();
                    this.response.put(NotificationCompat.CATEGORY_STATUS, "200");
                    this.response.put("message", "Message has been sent");
                    this.response.put("phone", map.get("phone"));
                } else {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("Your SMS limit reached! SMS (");
                    sb3.append(this.appSettingsEntity.getSMS_NUMBER_COUNT());
                    sb3.append(") - MINUTES (");
                    sb3.append(valueOf);
                    sb3.append(")");
                    this.response.put("message", sb3.toString());
                    this.response.put(NotificationCompat.CATEGORY_STATUS, "201");
                }
                this.databaseAccess.daoAccess().updateAppSettingsEntity(this.appSettingsEntity);
                StringBuilder sb4 = new StringBuilder();
                sb4.append("");
                sb4.append(this.appSettingsEntity.getSMS_NUMBER_COUNT());
                this.response.put("SMS_COUNT", sb4.toString());
                StringBuilder sb5 = new StringBuilder();
                sb5.append("");
                sb5.append(valueOf);
                this.response.put("MINUTES_COUNT", sb5.toString());
                return;
            }
            this.response.put(NotificationCompat.CATEGORY_STATUS, "422");
            this.response.put("message", "Username or Password is incorrect!");
        }
    }

    private String homePage() {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html> <html> <head> <meta charset=\"UTF-8\"> <title>GSM Modem</title> </head> <body>");
        sb.append("<h1>Welcome to GSM Modem</h1>");
        String sb2 = sb.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(sb2);
        sb3.append("<h2>Send SMS</h2>");
        String sb4 = sb3.toString();
        StringBuilder sb5 = new StringBuilder();
        sb5.append(sb4);
        sb5.append("<p> http://DeviceIP:port/SendSMS?username=abcd&amp;password=1234&amp;phone=111111111&amp;message=hello+test</p>");
        String sb6 = sb5.toString();
        StringBuilder sb7 = new StringBuilder();
        sb7.append(sb6);
        sb7.append(" </body> </html>");
        return sb7.toString();
    }

    private int minutesDiff(Date date, Date date2) {
        if (date == null || date2 == null) {
            return 0;
        }
        return (int) ((date2.getTime() / MINUTE_MILLIS) - (date.getTime() / MINUTE_MILLIS));
    }

    private boolean isPackageInstalled(String str, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(str, 0);
            return true;
        } catch (NameNotFoundException unused) {
            return false;
        }
    }
}
