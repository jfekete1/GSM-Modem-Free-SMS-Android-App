package android.arch.persistence.p000db.framework;

import android.arch.persistence.p000db.SupportSQLiteOpenHelper;
import android.arch.persistence.p000db.SupportSQLiteOpenHelper.Configuration;
import android.arch.persistence.p000db.SupportSQLiteOpenHelper.Factory;

/* renamed from: android.arch.persistence.db.framework.FrameworkSQLiteOpenHelperFactory */
public final class FrameworkSQLiteOpenHelperFactory implements Factory {
    public SupportSQLiteOpenHelper create(Configuration configuration) {
        return new FrameworkSQLiteOpenHelper(configuration.context, configuration.name, configuration.callback);
    }
}
