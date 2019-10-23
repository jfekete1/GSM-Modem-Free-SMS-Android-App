package android.arch.persistence.p000db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import java.io.File;

/* renamed from: android.arch.persistence.db.SupportSQLiteOpenHelper */
public interface SupportSQLiteOpenHelper {

    /* renamed from: android.arch.persistence.db.SupportSQLiteOpenHelper$Callback */
    public static abstract class Callback {
        private static final String TAG = "SupportSQLite";
        public final int version;

        public void onConfigure(SupportSQLiteDatabase supportSQLiteDatabase) {
        }

        public abstract void onCreate(SupportSQLiteDatabase supportSQLiteDatabase);

        public void onOpen(SupportSQLiteDatabase supportSQLiteDatabase) {
        }

        public abstract void onUpgrade(SupportSQLiteDatabase supportSQLiteDatabase, int i, int i2);

        public Callback(int i) {
            this.version = i;
        }

        public void onDowngrade(SupportSQLiteDatabase supportSQLiteDatabase, int i, int i2) {
            StringBuilder sb = new StringBuilder();
            sb.append("Can't downgrade database from version ");
            sb.append(i);
            sb.append(" to ");
            sb.append(i2);
            throw new SQLiteException(sb.toString());
        }

        /* JADX WARNING: Code restructure failed: missing block: B:10:0x0034, code lost:
            if (r0 != null) goto L_0x0036;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:11:0x0036, code lost:
            r4 = r0.iterator();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x003e, code lost:
            if (r4.hasNext() != false) goto L_0x0040;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x0040, code lost:
            deleteDatabaseFile((java.lang.String) ((android.util.Pair) r4.next()).second);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x004e, code lost:
            deleteDatabaseFile(r4.getPath());
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x0055, code lost:
            throw r1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:7:0x002e, code lost:
            r1 = move-exception;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:8:0x0030 */
        /* JADX WARNING: Removed duplicated region for block: B:19:0x0058  */
        /* JADX WARNING: Removed duplicated region for block: B:23:0x0070  */
        /* JADX WARNING: Removed duplicated region for block: B:7:0x002e A[ExcHandler: all (r1v2 'th' java.lang.Throwable A[CUSTOM_DECLARE]), PHI: r0 
          PHI: (r0v9 java.util.List) = (r0v2 java.util.List), (r0v3 java.util.List), (r0v3 java.util.List) binds: [B:5:0x0029, B:8:0x0030, B:9:?] A[DONT_GENERATE, DONT_INLINE], Splitter:B:5:0x0029] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onCorruption(android.arch.persistence.p000db.SupportSQLiteDatabase r4) {
            /*
                r3 = this;
                java.lang.String r0 = "SupportSQLite"
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                r1.<init>()
                java.lang.String r2 = "Corruption reported by sqlite on database: "
                r1.append(r2)
                java.lang.String r2 = r4.getPath()
                r1.append(r2)
                java.lang.String r1 = r1.toString()
                android.util.Log.e(r0, r1)
                boolean r0 = r4.isOpen()
                if (r0 != 0) goto L_0x0028
                java.lang.String r4 = r4.getPath()
                r3.deleteDatabaseFile(r4)
                return
            L_0x0028:
                r0 = 0
                java.util.List r0 = r4.getAttachedDbs()     // Catch:{ SQLiteException -> 0x0030, all -> 0x002e }
                goto L_0x0030
            L_0x002e:
                r1 = move-exception
                goto L_0x0034
            L_0x0030:
                r4.close()     // Catch:{ IOException -> 0x0056, all -> 0x002e }
                goto L_0x0056
            L_0x0034:
                if (r0 == 0) goto L_0x004e
                java.util.Iterator r4 = r0.iterator()
            L_0x003a:
                boolean r0 = r4.hasNext()
                if (r0 == 0) goto L_0x0055
                java.lang.Object r0 = r4.next()
                android.util.Pair r0 = (android.util.Pair) r0
                java.lang.Object r0 = r0.second
                java.lang.String r0 = (java.lang.String) r0
                r3.deleteDatabaseFile(r0)
                goto L_0x003a
            L_0x004e:
                java.lang.String r4 = r4.getPath()
                r3.deleteDatabaseFile(r4)
            L_0x0055:
                throw r1
            L_0x0056:
                if (r0 == 0) goto L_0x0070
                java.util.Iterator r4 = r0.iterator()
            L_0x005c:
                boolean r0 = r4.hasNext()
                if (r0 == 0) goto L_0x0077
                java.lang.Object r0 = r4.next()
                android.util.Pair r0 = (android.util.Pair) r0
                java.lang.Object r0 = r0.second
                java.lang.String r0 = (java.lang.String) r0
                r3.deleteDatabaseFile(r0)
                goto L_0x005c
            L_0x0070:
                java.lang.String r4 = r4.getPath()
                r3.deleteDatabaseFile(r4)
            L_0x0077:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: android.arch.persistence.p000db.SupportSQLiteOpenHelper.Callback.onCorruption(android.arch.persistence.db.SupportSQLiteDatabase):void");
        }

        private void deleteDatabaseFile(String str) {
            if (!str.equalsIgnoreCase(":memory:") && str.trim().length() != 0) {
                String str2 = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("deleting the database file: ");
                sb.append(str);
                Log.w(str2, sb.toString());
                try {
                    if (VERSION.SDK_INT >= 16) {
                        SQLiteDatabase.deleteDatabase(new File(str));
                    } else {
                        try {
                            if (!new File(str).delete()) {
                                String str3 = TAG;
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append("Could not delete the database file ");
                                sb2.append(str);
                                Log.e(str3, sb2.toString());
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "error while deleting corrupted database file", e);
                        }
                    }
                } catch (Exception e2) {
                    Log.w(TAG, "delete failed: ", e2);
                }
            }
        }
    }

    /* renamed from: android.arch.persistence.db.SupportSQLiteOpenHelper$Configuration */
    public static class Configuration {
        @NonNull
        public final Callback callback;
        @NonNull
        public final Context context;
        @Nullable
        public final String name;

        /* renamed from: android.arch.persistence.db.SupportSQLiteOpenHelper$Configuration$Builder */
        public static class Builder {
            Callback mCallback;
            Context mContext;
            String mName;

            public Configuration build() {
                Callback callback = this.mCallback;
                if (callback != null) {
                    Context context = this.mContext;
                    if (context != null) {
                        return new Configuration(context, this.mName, callback);
                    }
                    throw new IllegalArgumentException("Must set a non-null context to create the configuration.");
                }
                throw new IllegalArgumentException("Must set a callback to create the configuration.");
            }

            Builder(@NonNull Context context) {
                this.mContext = context;
            }

            public Builder name(@Nullable String str) {
                this.mName = str;
                return this;
            }

            public Builder callback(@NonNull Callback callback) {
                this.mCallback = callback;
                return this;
            }
        }

        Configuration(@NonNull Context context2, @Nullable String str, @NonNull Callback callback2) {
            this.context = context2;
            this.name = str;
            this.callback = callback2;
        }

        public static Builder builder(Context context2) {
            return new Builder(context2);
        }
    }

    /* renamed from: android.arch.persistence.db.SupportSQLiteOpenHelper$Factory */
    public interface Factory {
        SupportSQLiteOpenHelper create(Configuration configuration);
    }

    void close();

    String getDatabaseName();

    SupportSQLiteDatabase getReadableDatabase();

    SupportSQLiteDatabase getWritableDatabase();

    @RequiresApi(api = 16)
    void setWriteAheadLoggingEnabled(boolean z);
}
