package com.gsmmodem.dao_services;

import android.arch.persistence.p000db.SupportSQLiteStatement;
import android.arch.persistence.room.EntityDeletionOrUpdateAdapter;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.database.Cursor;
import com.google.android.gms.measurement.api.AppMeasurementSdk.ConditionalUserProperty;
import com.gsmmodem.entities.AppSettingsEntity;
import com.gsmmodem.entities.UsersEntity;
import java.util.List;

public class DaoAccess_Impl implements DaoAccess {
    private final RoomDatabase __db;
    private final EntityDeletionOrUpdateAdapter __deletionAdapterOfAppSettingsEntity;
    private final EntityDeletionOrUpdateAdapter __deletionAdapterOfUsersEntity;
    private final EntityInsertionAdapter __insertionAdapterOfAppSettingsEntity;
    private final EntityInsertionAdapter __insertionAdapterOfUsersEntity;
    private final EntityDeletionOrUpdateAdapter __updateAdapterOfAppSettingsEntity;
    private final EntityDeletionOrUpdateAdapter __updateAdapterOfUsersEntity;

    public DaoAccess_Impl(RoomDatabase roomDatabase) {
        this.__db = roomDatabase;
        this.__insertionAdapterOfAppSettingsEntity = new EntityInsertionAdapter<AppSettingsEntity>(roomDatabase) {
            public String createQuery() {
                return "INSERT OR ABORT INTO `AppSettingsEntity`(`id`,`PORT`,`GET_SMS_SWITCH`,`GET_SMS_URL`,`OUTGOING_SMS_SWITCH`,`NO_SMS`,`NO_MINUTES`,`DATE_LAST_SMS`,`SMS_NUMBER_COUNT`,`IS_SERVER_STARTED`) VALUES (?,?,?,?,?,?,?,?,?,?)";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, AppSettingsEntity appSettingsEntity) {
                if (appSettingsEntity.getId() == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindLong(1, (long) appSettingsEntity.getId().intValue());
                }
                if (appSettingsEntity.getPORT() == null) {
                    supportSQLiteStatement.bindNull(2);
                } else {
                    supportSQLiteStatement.bindString(2, appSettingsEntity.getPORT());
                }
                Integer num = null;
                Integer valueOf = appSettingsEntity.getGET_SMS_SWITCH() == null ? null : Integer.valueOf(appSettingsEntity.getGET_SMS_SWITCH().booleanValue() ? 1 : 0);
                if (valueOf == null) {
                    supportSQLiteStatement.bindNull(3);
                } else {
                    supportSQLiteStatement.bindLong(3, (long) valueOf.intValue());
                }
                if (appSettingsEntity.getGET_SMS_URL() == null) {
                    supportSQLiteStatement.bindNull(4);
                } else {
                    supportSQLiteStatement.bindString(4, appSettingsEntity.getGET_SMS_URL());
                }
                Integer valueOf2 = appSettingsEntity.getOUTGOING_SMS_SWITCH() == null ? null : Integer.valueOf(appSettingsEntity.getOUTGOING_SMS_SWITCH().booleanValue() ? 1 : 0);
                if (valueOf2 == null) {
                    supportSQLiteStatement.bindNull(5);
                } else {
                    supportSQLiteStatement.bindLong(5, (long) valueOf2.intValue());
                }
                if (appSettingsEntity.getNO_SMS() == null) {
                    supportSQLiteStatement.bindNull(6);
                } else {
                    supportSQLiteStatement.bindLong(6, (long) appSettingsEntity.getNO_SMS().intValue());
                }
                if (appSettingsEntity.getNO_MINUTES() == null) {
                    supportSQLiteStatement.bindNull(7);
                } else {
                    supportSQLiteStatement.bindLong(7, (long) appSettingsEntity.getNO_MINUTES().intValue());
                }
                if (appSettingsEntity.getDATE_LAST_SMS() == null) {
                    supportSQLiteStatement.bindNull(8);
                } else {
                    supportSQLiteStatement.bindString(8, appSettingsEntity.getDATE_LAST_SMS());
                }
                if (appSettingsEntity.getSMS_NUMBER_COUNT() == null) {
                    supportSQLiteStatement.bindNull(9);
                } else {
                    supportSQLiteStatement.bindLong(9, (long) appSettingsEntity.getSMS_NUMBER_COUNT().intValue());
                }
                if (appSettingsEntity.getIS_SERVER_STARTED() != null) {
                    num = Integer.valueOf(appSettingsEntity.getIS_SERVER_STARTED().booleanValue() ? 1 : 0);
                }
                if (num == null) {
                    supportSQLiteStatement.bindNull(10);
                } else {
                    supportSQLiteStatement.bindLong(10, (long) num.intValue());
                }
            }
        };
        this.__insertionAdapterOfUsersEntity = new EntityInsertionAdapter<UsersEntity>(roomDatabase) {
            public String createQuery() {
                return "INSERT OR ABORT INTO `UsersEntity`(`id`,`name`,`password`,`isChecked`) VALUES (?,?,?,?)";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, UsersEntity usersEntity) {
                if (usersEntity.getId() == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindLong(1, (long) usersEntity.getId().intValue());
                }
                if (usersEntity.getName() == null) {
                    supportSQLiteStatement.bindNull(2);
                } else {
                    supportSQLiteStatement.bindString(2, usersEntity.getName());
                }
                if (usersEntity.getPassword() == null) {
                    supportSQLiteStatement.bindNull(3);
                } else {
                    supportSQLiteStatement.bindString(3, usersEntity.getPassword());
                }
                Integer valueOf = usersEntity.getChecked() == null ? null : Integer.valueOf(usersEntity.getChecked().booleanValue() ? 1 : 0);
                if (valueOf == null) {
                    supportSQLiteStatement.bindNull(4);
                } else {
                    supportSQLiteStatement.bindLong(4, (long) valueOf.intValue());
                }
            }
        };
        this.__deletionAdapterOfAppSettingsEntity = new EntityDeletionOrUpdateAdapter<AppSettingsEntity>(roomDatabase) {
            public String createQuery() {
                return "DELETE FROM `AppSettingsEntity` WHERE `id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, AppSettingsEntity appSettingsEntity) {
                if (appSettingsEntity.getId() == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindLong(1, (long) appSettingsEntity.getId().intValue());
                }
            }
        };
        this.__deletionAdapterOfUsersEntity = new EntityDeletionOrUpdateAdapter<UsersEntity>(roomDatabase) {
            public String createQuery() {
                return "DELETE FROM `UsersEntity` WHERE `id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, UsersEntity usersEntity) {
                if (usersEntity.getId() == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindLong(1, (long) usersEntity.getId().intValue());
                }
            }
        };
        this.__updateAdapterOfAppSettingsEntity = new EntityDeletionOrUpdateAdapter<AppSettingsEntity>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `AppSettingsEntity` SET `id` = ?,`PORT` = ?,`GET_SMS_SWITCH` = ?,`GET_SMS_URL` = ?,`OUTGOING_SMS_SWITCH` = ?,`NO_SMS` = ?,`NO_MINUTES` = ?,`DATE_LAST_SMS` = ?,`SMS_NUMBER_COUNT` = ?,`IS_SERVER_STARTED` = ? WHERE `id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, AppSettingsEntity appSettingsEntity) {
                if (appSettingsEntity.getId() == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindLong(1, (long) appSettingsEntity.getId().intValue());
                }
                if (appSettingsEntity.getPORT() == null) {
                    supportSQLiteStatement.bindNull(2);
                } else {
                    supportSQLiteStatement.bindString(2, appSettingsEntity.getPORT());
                }
                Integer num = null;
                Integer valueOf = appSettingsEntity.getGET_SMS_SWITCH() == null ? null : Integer.valueOf(appSettingsEntity.getGET_SMS_SWITCH().booleanValue() ? 1 : 0);
                if (valueOf == null) {
                    supportSQLiteStatement.bindNull(3);
                } else {
                    supportSQLiteStatement.bindLong(3, (long) valueOf.intValue());
                }
                if (appSettingsEntity.getGET_SMS_URL() == null) {
                    supportSQLiteStatement.bindNull(4);
                } else {
                    supportSQLiteStatement.bindString(4, appSettingsEntity.getGET_SMS_URL());
                }
                Integer valueOf2 = appSettingsEntity.getOUTGOING_SMS_SWITCH() == null ? null : Integer.valueOf(appSettingsEntity.getOUTGOING_SMS_SWITCH().booleanValue() ? 1 : 0);
                if (valueOf2 == null) {
                    supportSQLiteStatement.bindNull(5);
                } else {
                    supportSQLiteStatement.bindLong(5, (long) valueOf2.intValue());
                }
                if (appSettingsEntity.getNO_SMS() == null) {
                    supportSQLiteStatement.bindNull(6);
                } else {
                    supportSQLiteStatement.bindLong(6, (long) appSettingsEntity.getNO_SMS().intValue());
                }
                if (appSettingsEntity.getNO_MINUTES() == null) {
                    supportSQLiteStatement.bindNull(7);
                } else {
                    supportSQLiteStatement.bindLong(7, (long) appSettingsEntity.getNO_MINUTES().intValue());
                }
                if (appSettingsEntity.getDATE_LAST_SMS() == null) {
                    supportSQLiteStatement.bindNull(8);
                } else {
                    supportSQLiteStatement.bindString(8, appSettingsEntity.getDATE_LAST_SMS());
                }
                if (appSettingsEntity.getSMS_NUMBER_COUNT() == null) {
                    supportSQLiteStatement.bindNull(9);
                } else {
                    supportSQLiteStatement.bindLong(9, (long) appSettingsEntity.getSMS_NUMBER_COUNT().intValue());
                }
                if (appSettingsEntity.getIS_SERVER_STARTED() != null) {
                    num = Integer.valueOf(appSettingsEntity.getIS_SERVER_STARTED().booleanValue() ? 1 : 0);
                }
                if (num == null) {
                    supportSQLiteStatement.bindNull(10);
                } else {
                    supportSQLiteStatement.bindLong(10, (long) num.intValue());
                }
                if (appSettingsEntity.getId() == null) {
                    supportSQLiteStatement.bindNull(11);
                } else {
                    supportSQLiteStatement.bindLong(11, (long) appSettingsEntity.getId().intValue());
                }
            }
        };
        this.__updateAdapterOfUsersEntity = new EntityDeletionOrUpdateAdapter<UsersEntity>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `UsersEntity` SET `id` = ?,`name` = ?,`password` = ?,`isChecked` = ? WHERE `id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, UsersEntity usersEntity) {
                if (usersEntity.getId() == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindLong(1, (long) usersEntity.getId().intValue());
                }
                if (usersEntity.getName() == null) {
                    supportSQLiteStatement.bindNull(2);
                } else {
                    supportSQLiteStatement.bindString(2, usersEntity.getName());
                }
                if (usersEntity.getPassword() == null) {
                    supportSQLiteStatement.bindNull(3);
                } else {
                    supportSQLiteStatement.bindString(3, usersEntity.getPassword());
                }
                Integer valueOf = usersEntity.getChecked() == null ? null : Integer.valueOf(usersEntity.getChecked().booleanValue() ? 1 : 0);
                if (valueOf == null) {
                    supportSQLiteStatement.bindNull(4);
                } else {
                    supportSQLiteStatement.bindLong(4, (long) valueOf.intValue());
                }
                if (usersEntity.getId() == null) {
                    supportSQLiteStatement.bindNull(5);
                } else {
                    supportSQLiteStatement.bindLong(5, (long) usersEntity.getId().intValue());
                }
            }
        };
    }

    public void insertAppSettingsEntity(AppSettingsEntity appSettingsEntity) {
        this.__db.beginTransaction();
        try {
            this.__insertionAdapterOfAppSettingsEntity.insert(appSettingsEntity);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    public void insertUser(UsersEntity usersEntity) {
        this.__db.beginTransaction();
        try {
            this.__insertionAdapterOfUsersEntity.insert(usersEntity);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    public void insertUsers(List<UsersEntity> list) {
        this.__db.beginTransaction();
        try {
            this.__insertionAdapterOfUsersEntity.insert((Iterable<T>) list);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    public void deleteAppSettingsEntity(AppSettingsEntity appSettingsEntity) {
        this.__db.beginTransaction();
        try {
            this.__deletionAdapterOfAppSettingsEntity.handle(appSettingsEntity);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    public void deleteUser(UsersEntity usersEntity) {
        this.__db.beginTransaction();
        try {
            this.__deletionAdapterOfUsersEntity.handle(usersEntity);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    public void updateAppSettingsEntity(AppSettingsEntity appSettingsEntity) {
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfAppSettingsEntity.handle(appSettingsEntity);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    public void updateUser(UsersEntity usersEntity) {
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfUsersEntity.handle(usersEntity);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    public AppSettingsEntity fetchAppSettingsEntityById(Integer num) {
        DaoAccess_Impl daoAccess_Impl;
        AppSettingsEntity appSettingsEntity;
        Integer num2;
        Integer num3;
        Boolean bool;
        Integer num4;
        Boolean bool2;
        Integer num5;
        Integer num6;
        Integer num7;
        Integer num8;
        boolean z = true;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM AppSettingsEntity WHERE id = ?", 1);
        if (num == null) {
            acquire.bindNull(1);
            daoAccess_Impl = this;
        } else {
            acquire.bindLong(1, (long) num.intValue());
            daoAccess_Impl = this;
        }
        Cursor query = daoAccess_Impl.__db.query(acquire);
        try {
            int columnIndexOrThrow = query.getColumnIndexOrThrow("id");
            int columnIndexOrThrow2 = query.getColumnIndexOrThrow("PORT");
            int columnIndexOrThrow3 = query.getColumnIndexOrThrow("GET_SMS_SWITCH");
            int columnIndexOrThrow4 = query.getColumnIndexOrThrow("GET_SMS_URL");
            int columnIndexOrThrow5 = query.getColumnIndexOrThrow("OUTGOING_SMS_SWITCH");
            int columnIndexOrThrow6 = query.getColumnIndexOrThrow("NO_SMS");
            int columnIndexOrThrow7 = query.getColumnIndexOrThrow("NO_MINUTES");
            int columnIndexOrThrow8 = query.getColumnIndexOrThrow("DATE_LAST_SMS");
            int columnIndexOrThrow9 = query.getColumnIndexOrThrow("SMS_NUMBER_COUNT");
            int columnIndexOrThrow10 = query.getColumnIndexOrThrow("IS_SERVER_STARTED");
            Boolean bool3 = null;
            if (query.moveToFirst()) {
                appSettingsEntity = new AppSettingsEntity();
                if (query.isNull(columnIndexOrThrow)) {
                    num2 = null;
                } else {
                    num2 = Integer.valueOf(query.getInt(columnIndexOrThrow));
                }
                appSettingsEntity.setId(num2);
                appSettingsEntity.setPORT(query.getString(columnIndexOrThrow2));
                if (query.isNull(columnIndexOrThrow3)) {
                    num3 = null;
                } else {
                    num3 = Integer.valueOf(query.getInt(columnIndexOrThrow3));
                }
                if (num3 == null) {
                    bool = null;
                } else {
                    bool = Boolean.valueOf(num3.intValue() != 0);
                }
                appSettingsEntity.setGET_SMS_SWITCH(bool);
                appSettingsEntity.setGET_SMS_URL(query.getString(columnIndexOrThrow4));
                if (query.isNull(columnIndexOrThrow5)) {
                    num4 = null;
                } else {
                    num4 = Integer.valueOf(query.getInt(columnIndexOrThrow5));
                }
                if (num4 == null) {
                    bool2 = null;
                } else {
                    bool2 = Boolean.valueOf(num4.intValue() != 0);
                }
                appSettingsEntity.setOUTGOING_SMS_SWITCH(bool2);
                if (query.isNull(columnIndexOrThrow6)) {
                    num5 = null;
                } else {
                    num5 = Integer.valueOf(query.getInt(columnIndexOrThrow6));
                }
                appSettingsEntity.setNO_SMS(num5);
                if (query.isNull(columnIndexOrThrow7)) {
                    num6 = null;
                } else {
                    num6 = Integer.valueOf(query.getInt(columnIndexOrThrow7));
                }
                appSettingsEntity.setNO_MINUTES(num6);
                appSettingsEntity.setDATE_LAST_SMS(query.getString(columnIndexOrThrow8));
                if (query.isNull(columnIndexOrThrow9)) {
                    num7 = null;
                } else {
                    num7 = Integer.valueOf(query.getInt(columnIndexOrThrow9));
                }
                appSettingsEntity.setSMS_NUMBER_COUNT(num7);
                if (query.isNull(columnIndexOrThrow10)) {
                    num8 = null;
                } else {
                    num8 = Integer.valueOf(query.getInt(columnIndexOrThrow10));
                }
                if (num8 != null) {
                    if (num8.intValue() == 0) {
                        z = false;
                    }
                    bool3 = Boolean.valueOf(z);
                }
                appSettingsEntity.setIS_SERVER_STARTED(bool3);
            } else {
                appSettingsEntity = null;
            }
            return appSettingsEntity;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public UsersEntity fetchUserById(Integer num) {
        UsersEntity usersEntity;
        Integer num2;
        Integer num3;
        boolean z = true;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM UsersEntity WHERE id = ?", 1);
        if (num == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindLong(1, (long) num.intValue());
        }
        Cursor query = this.__db.query(acquire);
        try {
            int columnIndexOrThrow = query.getColumnIndexOrThrow("id");
            int columnIndexOrThrow2 = query.getColumnIndexOrThrow(ConditionalUserProperty.NAME);
            int columnIndexOrThrow3 = query.getColumnIndexOrThrow("password");
            int columnIndexOrThrow4 = query.getColumnIndexOrThrow("isChecked");
            Boolean bool = null;
            if (query.moveToFirst()) {
                usersEntity = new UsersEntity();
                if (query.isNull(columnIndexOrThrow)) {
                    num2 = null;
                } else {
                    num2 = Integer.valueOf(query.getInt(columnIndexOrThrow));
                }
                usersEntity.setId(num2);
                usersEntity.setName(query.getString(columnIndexOrThrow2));
                usersEntity.setPassword(query.getString(columnIndexOrThrow3));
                if (query.isNull(columnIndexOrThrow4)) {
                    num3 = null;
                } else {
                    num3 = Integer.valueOf(query.getInt(columnIndexOrThrow4));
                }
                if (num3 != null) {
                    if (num3.intValue() == 0) {
                        z = false;
                    }
                    bool = Boolean.valueOf(z);
                }
                usersEntity.setChecked(bool);
            } else {
                usersEntity = null;
            }
            return usersEntity;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public UsersEntity fetchUserByNameAndPass(String str, String str2) {
        UsersEntity usersEntity;
        Integer num;
        Integer num2;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM UsersEntity where name = ? AND password = ?", 2);
        boolean z = true;
        if (str == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, str);
        }
        if (str2 == null) {
            acquire.bindNull(2);
        } else {
            acquire.bindString(2, str2);
        }
        Cursor query = this.__db.query(acquire);
        try {
            int columnIndexOrThrow = query.getColumnIndexOrThrow("id");
            int columnIndexOrThrow2 = query.getColumnIndexOrThrow(ConditionalUserProperty.NAME);
            int columnIndexOrThrow3 = query.getColumnIndexOrThrow("password");
            int columnIndexOrThrow4 = query.getColumnIndexOrThrow("isChecked");
            Boolean bool = null;
            if (query.moveToFirst()) {
                usersEntity = new UsersEntity();
                if (query.isNull(columnIndexOrThrow)) {
                    num = null;
                } else {
                    num = Integer.valueOf(query.getInt(columnIndexOrThrow));
                }
                usersEntity.setId(num);
                usersEntity.setName(query.getString(columnIndexOrThrow2));
                usersEntity.setPassword(query.getString(columnIndexOrThrow3));
                if (query.isNull(columnIndexOrThrow4)) {
                    num2 = null;
                } else {
                    num2 = Integer.valueOf(query.getInt(columnIndexOrThrow4));
                }
                if (num2 != null) {
                    if (num2.intValue() == 0) {
                        z = false;
                    }
                    bool = Boolean.valueOf(z);
                }
                usersEntity.setChecked(bool);
            } else {
                usersEntity = null;
            }
            return usersEntity;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public Cursor fetchUsersAll() {
        return this.__db.query(RoomSQLiteQuery.acquire("SELECT * FROM UsersEntity", 0));
    }
}
