package com.google.ads.mediation;

import android.location.Location;
import com.google.ads.AdRequest.Gender;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

@Deprecated
public class MediationAdRequest {
    private final Date zzih;
    private final Gender zzii;
    private final Set<String> zzij;
    private final boolean zzik;
    private final Location zzil;

    public MediationAdRequest(Date date, Gender gender, Set<String> set, boolean z, Location location) {
        this.zzih = date;
        this.zzii = gender;
        this.zzij = set;
        this.zzik = z;
        this.zzil = location;
    }

    public Integer getAgeInYears() {
        if (this.zzih == null) {
            return null;
        }
        Calendar instance = Calendar.getInstance();
        Calendar instance2 = Calendar.getInstance();
        instance.setTime(this.zzih);
        Integer valueOf = Integer.valueOf(instance2.get(1) - instance.get(1));
        if (instance2.get(2) < instance.get(2) || (instance2.get(2) == instance.get(2) && instance2.get(5) < instance.get(5))) {
            valueOf = Integer.valueOf(valueOf.intValue() - 1);
        }
        return valueOf;
    }

    public Date getBirthday() {
        return this.zzih;
    }

    public Gender getGender() {
        return this.zzii;
    }

    public Set<String> getKeywords() {
        return this.zzij;
    }

    public Location getLocation() {
        return this.zzil;
    }

    public boolean isTesting() {
        return this.zzik;
    }
}
