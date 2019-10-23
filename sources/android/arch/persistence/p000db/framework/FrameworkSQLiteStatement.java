package android.arch.persistence.p000db.framework;

import android.arch.persistence.p000db.SupportSQLiteStatement;
import android.database.sqlite.SQLiteStatement;

/* renamed from: android.arch.persistence.db.framework.FrameworkSQLiteStatement */
class FrameworkSQLiteStatement extends FrameworkSQLiteProgram implements SupportSQLiteStatement {
    private final SQLiteStatement mDelegate;

    FrameworkSQLiteStatement(SQLiteStatement sQLiteStatement) {
        super(sQLiteStatement);
        this.mDelegate = sQLiteStatement;
    }

    public void execute() {
        this.mDelegate.execute();
    }

    public int executeUpdateDelete() {
        return this.mDelegate.executeUpdateDelete();
    }

    public long executeInsert() {
        return this.mDelegate.executeInsert();
    }

    public long simpleQueryForLong() {
        return this.mDelegate.simpleQueryForLong();
    }

    public String simpleQueryForString() {
        return this.mDelegate.simpleQueryForString();
    }
}
