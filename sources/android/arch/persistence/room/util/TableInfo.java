package android.arch.persistence.room.util;

import android.arch.persistence.p000db.SupportSQLiteDatabase;
import android.arch.persistence.room.ColumnInfo.SQLiteTypeAffinity;
import android.database.Cursor;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import com.google.android.gms.measurement.AppMeasurement.Param;
import com.google.android.gms.measurement.api.AppMeasurementSdk.ConditionalUserProperty;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

@RestrictTo({Scope.LIBRARY_GROUP})
public class TableInfo {
    public final Map<String, Column> columns;
    public final Set<ForeignKey> foreignKeys;
    @Nullable
    public final Set<Index> indices;
    public final String name;

    public static class Column {
        @SQLiteTypeAffinity
        public final int affinity;
        public final String name;
        public final boolean notNull;
        public final int primaryKeyPosition;
        public final String type;

        public Column(String str, String str2, boolean z, int i) {
            this.name = str;
            this.type = str2;
            this.notNull = z;
            this.primaryKeyPosition = i;
            this.affinity = findAffinity(str2);
        }

        @SQLiteTypeAffinity
        private static int findAffinity(@Nullable String str) {
            if (str == null) {
                return 5;
            }
            String upperCase = str.toUpperCase(Locale.US);
            if (upperCase.contains("INT")) {
                return 3;
            }
            if (upperCase.contains("CHAR") || upperCase.contains("CLOB") || upperCase.contains("TEXT")) {
                return 2;
            }
            if (upperCase.contains("BLOB")) {
                return 5;
            }
            return (upperCase.contains("REAL") || upperCase.contains("FLOA") || upperCase.contains("DOUB")) ? 4 : 1;
        }

        public boolean equals(Object obj) {
            boolean z = true;
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Column column = (Column) obj;
            if (VERSION.SDK_INT >= 20) {
                if (this.primaryKeyPosition != column.primaryKeyPosition) {
                    return false;
                }
            } else if (isPrimaryKey() != column.isPrimaryKey()) {
                return false;
            }
            if (!this.name.equals(column.name) || this.notNull != column.notNull) {
                return false;
            }
            if (this.affinity != column.affinity) {
                z = false;
            }
            return z;
        }

        public boolean isPrimaryKey() {
            return this.primaryKeyPosition > 0;
        }

        public int hashCode() {
            return (((((this.name.hashCode() * 31) + this.affinity) * 31) + (this.notNull ? 1231 : 1237)) * 31) + this.primaryKeyPosition;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Column{name='");
            sb.append(this.name);
            sb.append('\'');
            sb.append(", type='");
            sb.append(this.type);
            sb.append('\'');
            sb.append(", affinity='");
            sb.append(this.affinity);
            sb.append('\'');
            sb.append(", notNull=");
            sb.append(this.notNull);
            sb.append(", primaryKeyPosition=");
            sb.append(this.primaryKeyPosition);
            sb.append('}');
            return sb.toString();
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public static class ForeignKey {
        @NonNull
        public final List<String> columnNames;
        @NonNull
        public final String onDelete;
        @NonNull
        public final String onUpdate;
        @NonNull
        public final List<String> referenceColumnNames;
        @NonNull
        public final String referenceTable;

        public ForeignKey(@NonNull String str, @NonNull String str2, @NonNull String str3, @NonNull List<String> list, @NonNull List<String> list2) {
            this.referenceTable = str;
            this.onDelete = str2;
            this.onUpdate = str3;
            this.columnNames = Collections.unmodifiableList(list);
            this.referenceColumnNames = Collections.unmodifiableList(list2);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            ForeignKey foreignKey = (ForeignKey) obj;
            if (this.referenceTable.equals(foreignKey.referenceTable) && this.onDelete.equals(foreignKey.onDelete) && this.onUpdate.equals(foreignKey.onUpdate) && this.columnNames.equals(foreignKey.columnNames)) {
                return this.referenceColumnNames.equals(foreignKey.referenceColumnNames);
            }
            return false;
        }

        public int hashCode() {
            return (((((((this.referenceTable.hashCode() * 31) + this.onDelete.hashCode()) * 31) + this.onUpdate.hashCode()) * 31) + this.columnNames.hashCode()) * 31) + this.referenceColumnNames.hashCode();
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("ForeignKey{referenceTable='");
            sb.append(this.referenceTable);
            sb.append('\'');
            sb.append(", onDelete='");
            sb.append(this.onDelete);
            sb.append('\'');
            sb.append(", onUpdate='");
            sb.append(this.onUpdate);
            sb.append('\'');
            sb.append(", columnNames=");
            sb.append(this.columnNames);
            sb.append(", referenceColumnNames=");
            sb.append(this.referenceColumnNames);
            sb.append('}');
            return sb.toString();
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    static class ForeignKeyWithSequence implements Comparable<ForeignKeyWithSequence> {
        final String mFrom;
        final int mId;
        final int mSequence;
        final String mTo;

        ForeignKeyWithSequence(int i, int i2, String str, String str2) {
            this.mId = i;
            this.mSequence = i2;
            this.mFrom = str;
            this.mTo = str2;
        }

        public int compareTo(@NonNull ForeignKeyWithSequence foreignKeyWithSequence) {
            int i = this.mId - foreignKeyWithSequence.mId;
            return i == 0 ? this.mSequence - foreignKeyWithSequence.mSequence : i;
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public static class Index {
        public static final String DEFAULT_PREFIX = "index_";
        public final List<String> columns;
        public final String name;
        public final boolean unique;

        public Index(String str, boolean z, List<String> list) {
            this.name = str;
            this.unique = z;
            this.columns = list;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Index index = (Index) obj;
            if (this.unique != index.unique || !this.columns.equals(index.columns)) {
                return false;
            }
            if (this.name.startsWith(DEFAULT_PREFIX)) {
                return index.name.startsWith(DEFAULT_PREFIX);
            }
            return this.name.equals(index.name);
        }

        public int hashCode() {
            int i;
            if (this.name.startsWith(DEFAULT_PREFIX)) {
                i = DEFAULT_PREFIX.hashCode();
            } else {
                i = this.name.hashCode();
            }
            return (((i * 31) + (this.unique ? 1 : 0)) * 31) + this.columns.hashCode();
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Index{name='");
            sb.append(this.name);
            sb.append('\'');
            sb.append(", unique=");
            sb.append(this.unique);
            sb.append(", columns=");
            sb.append(this.columns);
            sb.append('}');
            return sb.toString();
        }
    }

    public TableInfo(String str, Map<String, Column> map, Set<ForeignKey> set, Set<Index> set2) {
        Set<Index> set3;
        this.name = str;
        this.columns = Collections.unmodifiableMap(map);
        this.foreignKeys = Collections.unmodifiableSet(set);
        if (set2 == null) {
            set3 = null;
        } else {
            set3 = Collections.unmodifiableSet(set2);
        }
        this.indices = set3;
    }

    public TableInfo(String str, Map<String, Column> map, Set<ForeignKey> set) {
        this(str, map, set, Collections.emptySet());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        TableInfo tableInfo = (TableInfo) obj;
        String str = this.name;
        if (str == null ? tableInfo.name != null : !str.equals(tableInfo.name)) {
            return false;
        }
        Map<String, Column> map = this.columns;
        if (map == null ? tableInfo.columns != null : !map.equals(tableInfo.columns)) {
            return false;
        }
        Set<ForeignKey> set = this.foreignKeys;
        if (set == null ? tableInfo.foreignKeys != null : !set.equals(tableInfo.foreignKeys)) {
            return false;
        }
        Set<Index> set2 = this.indices;
        if (set2 != null) {
            Set<Index> set3 = tableInfo.indices;
            if (set3 != null) {
                return set2.equals(set3);
            }
        }
        return true;
    }

    public int hashCode() {
        String str = this.name;
        int i = 0;
        int hashCode = (str != null ? str.hashCode() : 0) * 31;
        Map<String, Column> map = this.columns;
        int hashCode2 = (hashCode + (map != null ? map.hashCode() : 0)) * 31;
        Set<ForeignKey> set = this.foreignKeys;
        if (set != null) {
            i = set.hashCode();
        }
        return hashCode2 + i;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TableInfo{name='");
        sb.append(this.name);
        sb.append('\'');
        sb.append(", columns=");
        sb.append(this.columns);
        sb.append(", foreignKeys=");
        sb.append(this.foreignKeys);
        sb.append(", indices=");
        sb.append(this.indices);
        sb.append('}');
        return sb.toString();
    }

    public static TableInfo read(SupportSQLiteDatabase supportSQLiteDatabase, String str) {
        return new TableInfo(str, readColumns(supportSQLiteDatabase, str), readForeignKeys(supportSQLiteDatabase, str), readIndices(supportSQLiteDatabase, str));
    }

    private static Set<ForeignKey> readForeignKeys(SupportSQLiteDatabase supportSQLiteDatabase, String str) {
        HashSet hashSet = new HashSet();
        StringBuilder sb = new StringBuilder();
        sb.append("PRAGMA foreign_key_list(`");
        sb.append(str);
        sb.append("`)");
        Cursor query = supportSQLiteDatabase.query(sb.toString());
        try {
            int columnIndex = query.getColumnIndex("id");
            int columnIndex2 = query.getColumnIndex("seq");
            int columnIndex3 = query.getColumnIndex("table");
            int columnIndex4 = query.getColumnIndex("on_delete");
            int columnIndex5 = query.getColumnIndex("on_update");
            List<ForeignKeyWithSequence> readForeignKeyFieldMappings = readForeignKeyFieldMappings(query);
            int count = query.getCount();
            for (int i = 0; i < count; i++) {
                query.moveToPosition(i);
                if (query.getInt(columnIndex2) == 0) {
                    int i2 = query.getInt(columnIndex);
                    ArrayList arrayList = new ArrayList();
                    ArrayList arrayList2 = new ArrayList();
                    for (ForeignKeyWithSequence foreignKeyWithSequence : readForeignKeyFieldMappings) {
                        if (foreignKeyWithSequence.mId == i2) {
                            arrayList.add(foreignKeyWithSequence.mFrom);
                            arrayList2.add(foreignKeyWithSequence.mTo);
                        }
                    }
                    ForeignKey foreignKey = new ForeignKey(query.getString(columnIndex3), query.getString(columnIndex4), query.getString(columnIndex5), arrayList, arrayList2);
                    hashSet.add(foreignKey);
                }
            }
            return hashSet;
        } finally {
            query.close();
        }
    }

    private static List<ForeignKeyWithSequence> readForeignKeyFieldMappings(Cursor cursor) {
        int columnIndex = cursor.getColumnIndex("id");
        int columnIndex2 = cursor.getColumnIndex("seq");
        int columnIndex3 = cursor.getColumnIndex("from");
        int columnIndex4 = cursor.getColumnIndex("to");
        int count = cursor.getCount();
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < count; i++) {
            cursor.moveToPosition(i);
            arrayList.add(new ForeignKeyWithSequence(cursor.getInt(columnIndex), cursor.getInt(columnIndex2), cursor.getString(columnIndex3), cursor.getString(columnIndex4)));
        }
        Collections.sort(arrayList);
        return arrayList;
    }

    private static Map<String, Column> readColumns(SupportSQLiteDatabase supportSQLiteDatabase, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("PRAGMA table_info(`");
        sb.append(str);
        sb.append("`)");
        Cursor query = supportSQLiteDatabase.query(sb.toString());
        HashMap hashMap = new HashMap();
        try {
            if (query.getColumnCount() > 0) {
                int columnIndex = query.getColumnIndex(ConditionalUserProperty.NAME);
                int columnIndex2 = query.getColumnIndex(Param.TYPE);
                int columnIndex3 = query.getColumnIndex("notnull");
                int columnIndex4 = query.getColumnIndex("pk");
                while (query.moveToNext()) {
                    String string = query.getString(columnIndex);
                    hashMap.put(string, new Column(string, query.getString(columnIndex2), query.getInt(columnIndex3) != 0, query.getInt(columnIndex4)));
                }
            }
            return hashMap;
        } finally {
            query.close();
        }
    }

    @Nullable
    private static Set<Index> readIndices(SupportSQLiteDatabase supportSQLiteDatabase, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("PRAGMA index_list(`");
        sb.append(str);
        sb.append("`)");
        Cursor query = supportSQLiteDatabase.query(sb.toString());
        try {
            int columnIndex = query.getColumnIndex(ConditionalUserProperty.NAME);
            int columnIndex2 = query.getColumnIndex("origin");
            int columnIndex3 = query.getColumnIndex("unique");
            if (!(columnIndex == -1 || columnIndex2 == -1)) {
                if (columnIndex3 != -1) {
                    HashSet hashSet = new HashSet();
                    while (query.moveToNext()) {
                        if ("c".equals(query.getString(columnIndex2))) {
                            String string = query.getString(columnIndex);
                            boolean z = true;
                            if (query.getInt(columnIndex3) != 1) {
                                z = false;
                            }
                            Index readIndex = readIndex(supportSQLiteDatabase, string, z);
                            if (readIndex == null) {
                                query.close();
                                return null;
                            }
                            hashSet.add(readIndex);
                        }
                    }
                    query.close();
                    return hashSet;
                }
            }
            return null;
        } finally {
            query.close();
        }
    }

    @Nullable
    private static Index readIndex(SupportSQLiteDatabase supportSQLiteDatabase, String str, boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append("PRAGMA index_xinfo(`");
        sb.append(str);
        sb.append("`)");
        Cursor query = supportSQLiteDatabase.query(sb.toString());
        try {
            int columnIndex = query.getColumnIndex("seqno");
            int columnIndex2 = query.getColumnIndex("cid");
            int columnIndex3 = query.getColumnIndex(ConditionalUserProperty.NAME);
            if (!(columnIndex == -1 || columnIndex2 == -1)) {
                if (columnIndex3 != -1) {
                    TreeMap treeMap = new TreeMap();
                    while (query.moveToNext()) {
                        if (query.getInt(columnIndex2) >= 0) {
                            int i = query.getInt(columnIndex);
                            treeMap.put(Integer.valueOf(i), query.getString(columnIndex3));
                        }
                    }
                    ArrayList arrayList = new ArrayList(treeMap.size());
                    arrayList.addAll(treeMap.values());
                    Index index = new Index(str, z, arrayList);
                    query.close();
                    return index;
                }
            }
            return null;
        } finally {
            query.close();
        }
    }
}
