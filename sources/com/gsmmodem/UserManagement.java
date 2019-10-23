package com.gsmmodem;

import android.arch.persistence.room.Room;
import android.database.Cursor;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.p004v7.app.AppCompatActivity;
import android.support.p004v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.measurement.api.AppMeasurementSdk.ConditionalUserProperty;
import com.gsmmodem.dao_impl.DatabaseAccess;
import com.gsmmodem.entities.UsersEntity;
import java.util.ArrayList;

public class UserManagement extends AppCompatActivity {
    private static final String DATABASE_NAME = "gsm_modem_db";
    /* access modifiers changed from: private */
    public CoordinatorLayout coordinatorLayout;
    /* access modifiers changed from: private */
    public Cursor cursor;
    /* access modifiers changed from: private */
    public CustomUserListAdapter customUserListAdapter;
    /* access modifiers changed from: private */
    public DatabaseAccess databaseAccess;
    /* access modifiers changed from: private */
    public CheckBox isCheckedBox;
    /* access modifiers changed from: private */
    public AdView mAdView;
    /* access modifiers changed from: private */
    public InterstitialAd mInterstitialAd;
    /* access modifiers changed from: private */
    public PopupWindow mPopupWindow;
    /* access modifiers changed from: private */
    public EditText passwordEdit;
    ListView userListView;
    /* access modifiers changed from: private */
    public EditText userNameEdit;
    /* access modifiers changed from: private */
    public ArrayList<UsersEntity> usersEntities;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0524R.layout.activity_user_management);
        setSupportActionBar((Toolbar) findViewById(C0524R.C0526id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.userListView = (ListView) findViewById(C0524R.C0526id.usersList);
        this.userListView.post(new Runnable() {
            public void run() {
                UserManagement userManagement = UserManagement.this;
                userManagement.databaseAccess = (DatabaseAccess) Room.databaseBuilder(userManagement.getApplicationContext(), DatabaseAccess.class, UserManagement.DATABASE_NAME).fallbackToDestructiveMigration().allowMainThreadQueries().build();
                MobileAds.initialize(UserManagement.this.getApplicationContext(), UserManagement.this.getString(C0524R.string.admob_app_id));
                UserManagement userManagement2 = UserManagement.this;
                userManagement2.mAdView = (AdView) userManagement2.findViewById(C0524R.C0526id.adView);
                UserManagement.this.mAdView.loadAd(new Builder().build());
                UserManagement userManagement3 = UserManagement.this;
                userManagement3.mInterstitialAd = new InterstitialAd(userManagement3.getApplicationContext());
                UserManagement.this.mInterstitialAd.setAdUnitId(UserManagement.this.getString(C0524R.string.interstitial_ad_unit_id));
                UserManagement.this.mInterstitialAd.loadAd(new Builder().build());
                UserManagement userManagement4 = UserManagement.this;
                userManagement4.cursor = userManagement4.databaseAccess.daoAccess().fetchUsersAll();
                if (UserManagement.this.cursor != null) {
                    UserManagement.this.usersEntities = new ArrayList();
                    if (UserManagement.this.cursor.moveToFirst()) {
                        do {
                            UsersEntity usersEntity = new UsersEntity();
                            usersEntity.setId(Integer.valueOf(UserManagement.this.cursor.getInt(UserManagement.this.cursor.getColumnIndex("id"))));
                            usersEntity.setName(UserManagement.this.cursor.getString(UserManagement.this.cursor.getColumnIndex(ConditionalUserProperty.NAME)));
                            usersEntity.setPassword(UserManagement.this.cursor.getString(UserManagement.this.cursor.getColumnIndex("password")));
                            usersEntity.setChecked(Boolean.valueOf(UserManagement.this.cursor.getInt(UserManagement.this.cursor.getColumnIndex("isChecked")) > 0));
                            UserManagement.this.usersEntities.add(usersEntity);
                        } while (UserManagement.this.cursor.moveToNext());
                    }
                    UserManagement.this.updateUsersList();
                }
            }
        });
        this.coordinatorLayout = (CoordinatorLayout) findViewById(C0524R.C0526id.user_management);
        ((FloatingActionButton) findViewById(C0524R.C0526id.fab)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                View inflate = ((LayoutInflater) UserManagement.this.getApplicationContext().getSystemService("layout_inflater")).inflate(C0524R.layout.window_add_user, null);
                UserManagement.this.mPopupWindow = new PopupWindow(inflate, -2, -2);
                if (VERSION.SDK_INT >= 21) {
                    UserManagement.this.mPopupWindow.setElevation(5.0f);
                }
                ((ImageButton) inflate.findViewById(C0524R.C0526id.ib_close)).setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        UserManagement.this.mPopupWindow.dismiss();
                    }
                });
                UserManagement.this.userNameEdit = (EditText) inflate.findViewById(C0524R.C0526id.usernameEdit);
                UserManagement.this.passwordEdit = (EditText) inflate.findViewById(C0524R.C0526id.passwordEdit);
                UserManagement.this.isCheckedBox = (CheckBox) inflate.findViewById(C0524R.C0526id.isCheckedEdit);
                ((Button) inflate.findViewById(C0524R.C0526id.saveOrUpdateUser)).setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        UsersEntity usersEntity = new UsersEntity();
                        if (UserManagement.this.userNameEdit.getText() != null && UserManagement.this.userNameEdit.getText().toString().equals("")) {
                            Toast.makeText(UserManagement.this, "Username is required!", 0).show();
                        } else if (UserManagement.this.userNameEdit.getText() != null && UserManagement.this.isUserNameAvailable(UserManagement.this.userNameEdit.getText().toString(), Integer.valueOf(-1)).booleanValue()) {
                            Toast.makeText(UserManagement.this, "Username already exists!", 0).show();
                        } else if (UserManagement.this.passwordEdit.getText() == null || !UserManagement.this.passwordEdit.getText().toString().equals("")) {
                            usersEntity.setName(UserManagement.this.userNameEdit.getText().toString());
                            usersEntity.setPassword(UserManagement.this.passwordEdit.getText().toString());
                            usersEntity.setChecked(Boolean.valueOf(UserManagement.this.isCheckedBox.isChecked()));
                            UserManagement.this.databaseAccess.daoAccess().insertUser(usersEntity);
                            UserManagement.this.usersEntities.add(usersEntity);
                            if (UserManagement.this.customUserListAdapter != null) {
                                UserManagement.this.customUserListAdapter.notifyDataSetChanged();
                            }
                            UserManagement.this.mPopupWindow.dismiss();
                        } else {
                            Toast.makeText(UserManagement.this, "Password is required!", 0).show();
                        }
                    }
                });
                UserManagement.this.mPopupWindow.showAtLocation(UserManagement.this.coordinatorLayout, 17, 0, 0);
                UserManagement.this.mPopupWindow.setFocusable(true);
                UserManagement.this.mPopupWindow.update();
            }
        });
    }

    /* access modifiers changed from: private */
    public void updateUsersList() {
        this.customUserListAdapter = new CustomUserListAdapter(this, this.usersEntities);
        this.userListView.setAdapter(this.customUserListAdapter);
        this.userListView.setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j) {
                final Integer valueOf = Integer.valueOf(i);
                final UsersEntity usersEntity = (UsersEntity) UserManagement.this.userListView.getItemAtPosition(i);
                View inflate = ((LayoutInflater) UserManagement.this.getApplicationContext().getSystemService("layout_inflater")).inflate(C0524R.layout.window_update_delete_user, null);
                UserManagement.this.mPopupWindow = new PopupWindow(inflate, -2, -2);
                if (VERSION.SDK_INT >= 21) {
                    UserManagement.this.mPopupWindow.setElevation(5.0f);
                }
                ((ImageButton) inflate.findViewById(C0524R.C0526id.ib_close)).setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        UserManagement.this.mPopupWindow.dismiss();
                    }
                });
                UserManagement.this.userNameEdit = (EditText) inflate.findViewById(C0524R.C0526id.usernameEdit);
                UserManagement.this.userNameEdit.setText(usersEntity.getName());
                UserManagement.this.passwordEdit = (EditText) inflate.findViewById(C0524R.C0526id.passwordEdit);
                UserManagement.this.passwordEdit.setText(usersEntity.getPassword());
                UserManagement.this.isCheckedBox = (CheckBox) inflate.findViewById(C0524R.C0526id.isCheckedEdit);
                UserManagement.this.isCheckedBox.setChecked(usersEntity.getChecked().booleanValue());
                ((Button) inflate.findViewById(C0524R.C0526id.updateUserButton)).setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        if (UserManagement.this.userNameEdit.getText() != null && UserManagement.this.userNameEdit.getText().toString().equals("")) {
                            Toast.makeText(UserManagement.this, "Username is required!", 0).show();
                        } else if (UserManagement.this.userNameEdit.getText() != null && UserManagement.this.isUserNameAvailable(UserManagement.this.userNameEdit.getText().toString(), valueOf).booleanValue()) {
                            Toast.makeText(UserManagement.this, "Username already exists!", 0).show();
                        } else if (UserManagement.this.passwordEdit.getText() == null || !UserManagement.this.passwordEdit.getText().toString().equals("")) {
                            usersEntity.setName(UserManagement.this.userNameEdit.getText().toString());
                            usersEntity.setPassword(UserManagement.this.passwordEdit.getText().toString());
                            usersEntity.setChecked(Boolean.valueOf(UserManagement.this.isCheckedBox.isChecked()));
                            UserManagement.this.databaseAccess.daoAccess().updateUser(usersEntity);
                            UserManagement.this.usersEntities.set(valueOf.intValue(), usersEntity);
                            if (UserManagement.this.customUserListAdapter != null) {
                                UserManagement.this.customUserListAdapter.notifyDataSetChanged();
                            }
                            UserManagement.this.mPopupWindow.dismiss();
                        } else {
                            Toast.makeText(UserManagement.this, "Password is required!", 0).show();
                        }
                    }
                });
                ((Button) inflate.findViewById(C0524R.C0526id.deleteUserButton)).setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        UserManagement.this.databaseAccess.daoAccess().deleteUser(usersEntity);
                        UserManagement.this.usersEntities.remove(usersEntity);
                        if (UserManagement.this.customUserListAdapter != null) {
                            UserManagement.this.customUserListAdapter.notifyDataSetChanged();
                        }
                        UserManagement.this.mPopupWindow.dismiss();
                    }
                });
                UserManagement.this.mPopupWindow.showAtLocation(UserManagement.this.coordinatorLayout, 17, 0, 0);
                UserManagement.this.mPopupWindow.setFocusable(true);
                UserManagement.this.mPopupWindow.update();
                return true;
            }
        });
    }

    /* access modifiers changed from: private */
    public Boolean isUserNameAvailable(String str, Integer num) {
        for (Integer valueOf = Integer.valueOf(0); valueOf.intValue() < this.usersEntities.size(); valueOf = Integer.valueOf(valueOf.intValue() + 1)) {
            if (str.equalsIgnoreCase(((UsersEntity) this.usersEntities.get(valueOf.intValue())).getName()) && num != valueOf) {
                return Boolean.valueOf(true);
            }
        }
        return Boolean.valueOf(false);
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
