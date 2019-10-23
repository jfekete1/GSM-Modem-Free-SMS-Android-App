package com.gsmmodem.dao_impl;

import android.arch.persistence.p000db.SupportSQLiteDatabase;
import android.arch.persistence.p000db.SupportSQLiteOpenHelper;
import android.arch.persistence.p000db.SupportSQLiteOpenHelper.Configuration;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.RoomDatabase.Callback;
import android.arch.persistence.room.RoomMasterTable;
import android.arch.persistence.room.RoomOpenHelper;
import android.arch.persistence.room.RoomOpenHelper.Delegate;
import android.arch.persistence.room.util.TableInfo;
import android.arch.persistence.room.util.TableInfo.Column;
import com.google.android.gms.measurement.api.AppMeasurementSdk.ConditionalUserProperty;
import com.gsmmodem.dao_services.DaoAccess;
import com.gsmmodem.dao_services.DaoAccess_Impl;
import java.util.HashMap;
import java.util.HashSet;

public class DatabaseAccess_Impl extends DatabaseAccess {
    private volatile DaoAccess _daoAccess;

    /* access modifiers changed from: protected */
    public SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration databaseConfiguration) {
        return databaseConfiguration.sqliteOpenHelperFactory.create(Configuration.builder(databaseConfiguration.context).name(databaseConfiguration.name).callback(new RoomOpenHelper(databaseConfiguration, new Delegate(1) {
            public void createAllTables(SupportSQLiteDatabase supportSQLiteDatabase) {
                supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `AppSettingsEntity` (`id` INTEGER NOT NULL, `PORT` TEXT, `GET_SMS_SWITCH` INTEGER, `GET_SMS_URL` TEXT, `OUTGOING_SMS_SWITCH` INTEGER, `NO_SMS` INTEGER, `NO_MINUTES` INTEGER, `DATE_LAST_SMS` TEXT, `SMS_NUMBER_COUNT` INTEGER, `IS_SERVER_STARTED` INTEGER, PRIMARY KEY(`id`))");
                supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `UsersEntity` (`id` INTEGER, `name` TEXT, `password` TEXT, `isChecked` INTEGER, PRIMARY KEY(`id`))");
                supportSQLiteDatabase.execSQL(RoomMasterTable.CREATE_QUERY);
                supportSQLiteDatabase.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"5a05a946fd1500e732ba18b8290c6d2e\")");
            }

            public void dropAllTables(SupportSQLiteDatabase supportSQLiteDatabase) {
                supportSQLiteDatabase.execSQL("DROP TABLE IF EXISTS `AppSettingsEntity`");
                supportSQLiteDatabase.execSQL("DROP TABLE IF EXISTS `UsersEntity`");
            }

            /* access modifiers changed from: protected */
            public void onCreate(SupportSQLiteDatabase supportSQLiteDatabase) {
                if (DatabaseAccess_Impl.this.mCallbacks != null) {
                    int size = DatabaseAccess_Impl.this.mCallbacks.size();
                    for (int i = 0; i < size; i++) {
                        ((Callback) DatabaseAccess_Impl.this.mCallbacks.get(i)).onCreate(supportSQLiteDatabase);
                    }
                }
            }

            public void onOpen(SupportSQLiteDatabase supportSQLiteDatabase) {
                DatabaseAccess_Impl.this.mDatabase = supportSQLiteDatabase;
                DatabaseAccess_Impl.this.internalInitInvalidationTracker(supportSQLiteDatabase);
                if (DatabaseAccess_Impl.this.mCallbacks != null) {
                    int size = DatabaseAccess_Impl.this.mCallbacks.size();
                    for (int i = 0; i < size; i++) {
                        ((Callback) DatabaseAccess_Impl.this.mCallbacks.get(i)).onOpen(supportSQLiteDatabase);
                    }
                }
            }

            /* access modifiers changed from: protected */
            public void validateMigration(SupportSQLiteDatabase supportSQLiteDatabase) {
                HashMap hashMap = new HashMap(10);
                hashMap.put("id", new Column("id", "INTEGER", true, 1));
                hashMap.put("PORT", new Column("PORT", "TEXT", false, 0));
                hashMap.put("GET_SMS_SWITCH", new Column("GET_SMS_SWITCH", "INTEGER", false, 0));
                hashMap.put("GET_SMS_URL", new Column("GET_SMS_URL", "TEXT", false, 0));
                hashMap.put("OUTGOING_SMS_SWITCH", new Column("OUTGOING_SMS_SWITCH", "INTEGER", false, 0));
                hashMap.put("NO_SMS", new Column("NO_SMS", "INTEGER", false, 0));
                hashMap.put("NO_MINUTES", new Column("NO_MINUTES", "INTEGER", false, 0));
                hashMap.put("DATE_LAST_SMS", new Column("DATE_LAST_SMS", "TEXT", false, 0));
                hashMap.put("SMS_NUMBER_COUNT", new Column("SMS_NUMBER_COUNT", "INTEGER", false, 0));
                hashMap.put("IS_SERVER_STARTED", new Column("IS_SERVER_STARTED", "INTEGER", false, 0));
                TableInfo tableInfo = new TableInfo("AppSettingsEntity", hashMap, new HashSet(0), new HashSet(0));
                TableInfo read = TableInfo.read(supportSQLiteDatabase, "AppSettingsEntity");
                if (tableInfo.equals(read)) {
                    HashMap hashMap2 = new HashMap(4);
                    hashMap2.put("id", new Column("id", "INTEGER", false, 1));
                    hashMap2.put(ConditionalUserProperty.NAME, new Column(ConditionalUserProperty.NAME, "TEXT", false, 0));
                    hashMap2.put("password", new Column("password", "TEXT", false, 0));
                    hashMap2.put("isChecked", new Column("isChecked", "INTEGER", false, 0));
                    TableInfo tableInfo2 = new TableInfo("UsersEntity", hashMap2, new HashSet(0), new HashSet(0));
                    TableInfo read2 = TableInfo.read(supportSQLiteDatabase, "UsersEntity");
                    if (!tableInfo2.equals(read2)) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Migration didn't properly handle UsersEntity(com.gsmmodem.entities.UsersEntity).\n Expected:\n");
                        sb.append(tableInfo2);
                        sb.append("\n");
                        sb.append(" Found:\n");
                        sb.append(read2);
                        throw new IllegalStateException(sb.toString());
                    }
                    return;
                }
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Migration didn't properly handle AppSettingsEntity(com.gsmmodem.entities.AppSettingsEntity).\n Expected:\n");
                sb2.append(tableInfo);
                sb2.append("\n");
                sb2.append(" Found:\n");
                sb2.append(read);
                throw new IllegalStateException(sb2.toString());
            }
        }, "5a05a946fd1500e732ba18b8290c6d2e", "d07a107d5ac6003eab59d5c36c94636e")).build());
    }

    /* access modifiers changed from: protected */
    public InvalidationTracker createInvalidationTracker() {
        return new InvalidationTracker(this, "AppSettingsEntity", "UsersEntity");
    }

    public void clearAllTables() {
        super.assertNotMainThread();
        SupportSQLiteDatabase writableDatabase = super.getOpenHelper().getWritableDatabase();
        try {
            super.beginTransaction();
            writableDatabase.execSQL("DELETE FROM `AppSettingsEntity`");
            writableDatabase.execSQL("DELETE FROM `UsersEntity`");
            super.setTransactionSuccessful();
        } finally {
            super.endTransaction();
            writableDatabase.query("PRAGMA wal_checkpoint(FULL)").close();
            if (!writableDatabase.inTransaction()) {
                writableDatabase.execSQL("VACUUM");
            }
        }
    }

    public DaoAccess daoAccess() {
        DaoAccess daoAccess;
        if (this._daoAccess != null) {
            return this._daoAccess;
        }
        synchronized (this) {
            if (this._daoAccess == null) {
                this._daoAccess = new DaoAccess_Impl(this);
            }
            daoAccess = this._daoAccess;
        }
        return daoAccess;
    }
}
