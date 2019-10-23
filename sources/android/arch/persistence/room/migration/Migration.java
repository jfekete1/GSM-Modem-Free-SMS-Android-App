package android.arch.persistence.room.migration;

import android.arch.persistence.p000db.SupportSQLiteDatabase;
import android.support.annotation.NonNull;

public abstract class Migration {
    public final int endVersion;
    public final int startVersion;

    public abstract void migrate(@NonNull SupportSQLiteDatabase supportSQLiteDatabase);

    public Migration(int i, int i2) {
        this.startVersion = i;
        this.endVersion = i2;
    }
}
