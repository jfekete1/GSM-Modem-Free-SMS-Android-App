package com.gsmmodem.dao_impl;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import com.gsmmodem.dao_services.DaoAccess;
import com.gsmmodem.entities.AppSettingsEntity;
import com.gsmmodem.entities.UsersEntity;

@Database(entities = {AppSettingsEntity.class, UsersEntity.class}, exportSchema = false, version = 1)
public abstract class DatabaseAccess extends RoomDatabase {
    public abstract DaoAccess daoAccess();
}
