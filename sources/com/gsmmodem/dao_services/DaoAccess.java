package com.gsmmodem.dao_services;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;
import com.gsmmodem.entities.AppSettingsEntity;
import com.gsmmodem.entities.UsersEntity;
import java.util.List;

@Dao
public interface DaoAccess {
    @Delete
    void deleteAppSettingsEntity(AppSettingsEntity appSettingsEntity);

    @Delete
    void deleteUser(UsersEntity usersEntity);

    @Query("SELECT * FROM AppSettingsEntity WHERE id = :id")
    AppSettingsEntity fetchAppSettingsEntityById(Integer num);

    @Query("SELECT * FROM UsersEntity WHERE id = :id")
    UsersEntity fetchUserById(Integer num);

    @Query("SELECT * FROM UsersEntity where name = :username AND password = :pass")
    UsersEntity fetchUserByNameAndPass(String str, String str2);

    @Query("SELECT * FROM UsersEntity")
    Cursor fetchUsersAll();

    @Insert
    void insertAppSettingsEntity(AppSettingsEntity appSettingsEntity);

    @Insert
    void insertUser(UsersEntity usersEntity);

    @Insert
    void insertUsers(List<UsersEntity> list);

    @Update
    void updateAppSettingsEntity(AppSettingsEntity appSettingsEntity);

    @Update
    void updateUser(UsersEntity usersEntity);
}
