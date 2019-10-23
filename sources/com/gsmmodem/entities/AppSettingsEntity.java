package com.gsmmodem.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class AppSettingsEntity {
    private String DATE_LAST_SMS;
    private Boolean GET_SMS_SWITCH;
    private String GET_SMS_URL;
    private Boolean IS_SERVER_STARTED;
    private Integer NO_MINUTES;
    private Integer NO_SMS;
    private Boolean OUTGOING_SMS_SWITCH;
    private String PORT;
    private Integer SMS_NUMBER_COUNT;
    @PrimaryKey
    @NonNull

    /* renamed from: id */
    private Integer f72id;

    @NonNull
    public Integer getId() {
        return this.f72id;
    }

    public void setId(@NonNull Integer num) {
        this.f72id = num;
    }

    public String getPORT() {
        return this.PORT;
    }

    public void setPORT(String str) {
        this.PORT = str;
    }

    public Boolean getGET_SMS_SWITCH() {
        return this.GET_SMS_SWITCH;
    }

    public void setGET_SMS_SWITCH(Boolean bool) {
        this.GET_SMS_SWITCH = bool;
    }

    public String getGET_SMS_URL() {
        return this.GET_SMS_URL;
    }

    public void setGET_SMS_URL(String str) {
        this.GET_SMS_URL = str;
    }

    public Boolean getOUTGOING_SMS_SWITCH() {
        return this.OUTGOING_SMS_SWITCH;
    }

    public void setOUTGOING_SMS_SWITCH(Boolean bool) {
        this.OUTGOING_SMS_SWITCH = bool;
    }

    public Integer getNO_SMS() {
        return this.NO_SMS;
    }

    public void setNO_SMS(Integer num) {
        this.NO_SMS = num;
    }

    public Integer getNO_MINUTES() {
        return this.NO_MINUTES;
    }

    public void setNO_MINUTES(Integer num) {
        this.NO_MINUTES = num;
    }

    public String getDATE_LAST_SMS() {
        return this.DATE_LAST_SMS;
    }

    public void setDATE_LAST_SMS(String str) {
        this.DATE_LAST_SMS = str;
    }

    public Integer getSMS_NUMBER_COUNT() {
        return this.SMS_NUMBER_COUNT;
    }

    public void setSMS_NUMBER_COUNT(Integer num) {
        this.SMS_NUMBER_COUNT = num;
    }

    public Boolean getIS_SERVER_STARTED() {
        return this.IS_SERVER_STARTED;
    }

    public void setIS_SERVER_STARTED(Boolean bool) {
        this.IS_SERVER_STARTED = bool;
    }
}
