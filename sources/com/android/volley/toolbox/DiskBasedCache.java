package com.android.volley.toolbox;

import android.os.SystemClock;
import android.support.annotation.VisibleForTesting;
import android.text.TextUtils;
import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.Header;
import com.android.volley.VolleyLog;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DiskBasedCache implements Cache {
    private static final int CACHE_MAGIC = 538247942;
    private static final int DEFAULT_DISK_USAGE_BYTES = 5242880;
    private static final float HYSTERESIS_FACTOR = 0.9f;
    private final Map<String, CacheHeader> mEntries;
    private final int mMaxCacheSizeInBytes;
    private final File mRootDirectory;
    private long mTotalSize;

    static class CacheHeader {
        final List<Header> allResponseHeaders;
        final String etag;
        final String key;
        final long lastModified;
        final long serverDate;
        long size;
        final long softTtl;
        final long ttl;

        private CacheHeader(String str, String str2, long j, long j2, long j3, long j4, List<Header> list) {
            this.key = str;
            if ("".equals(str2)) {
                str2 = null;
            }
            this.etag = str2;
            this.serverDate = j;
            this.lastModified = j2;
            this.ttl = j3;
            this.softTtl = j4;
            this.allResponseHeaders = list;
        }

        CacheHeader(String str, Entry entry) {
            this(str, entry.etag, entry.serverDate, entry.lastModified, entry.ttl, entry.softTtl, getAllResponseHeaders(entry));
            this.size = (long) entry.data.length;
        }

        private static List<Header> getAllResponseHeaders(Entry entry) {
            if (entry.allResponseHeaders != null) {
                return entry.allResponseHeaders;
            }
            return HttpHeaderParser.toAllHeaderList(entry.responseHeaders);
        }

        static CacheHeader readHeader(CountingInputStream countingInputStream) throws IOException {
            if (DiskBasedCache.readInt(countingInputStream) == DiskBasedCache.CACHE_MAGIC) {
                CacheHeader cacheHeader = new CacheHeader(DiskBasedCache.readString(countingInputStream), DiskBasedCache.readString(countingInputStream), DiskBasedCache.readLong(countingInputStream), DiskBasedCache.readLong(countingInputStream), DiskBasedCache.readLong(countingInputStream), DiskBasedCache.readLong(countingInputStream), DiskBasedCache.readHeaderList(countingInputStream));
                return cacheHeader;
            }
            throw new IOException();
        }

        /* access modifiers changed from: 0000 */
        public Entry toCacheEntry(byte[] bArr) {
            Entry entry = new Entry();
            entry.data = bArr;
            entry.etag = this.etag;
            entry.serverDate = this.serverDate;
            entry.lastModified = this.lastModified;
            entry.ttl = this.ttl;
            entry.softTtl = this.softTtl;
            entry.responseHeaders = HttpHeaderParser.toHeaderMap(this.allResponseHeaders);
            entry.allResponseHeaders = Collections.unmodifiableList(this.allResponseHeaders);
            return entry;
        }

        /* access modifiers changed from: 0000 */
        public boolean writeHeader(OutputStream outputStream) {
            try {
                DiskBasedCache.writeInt(outputStream, DiskBasedCache.CACHE_MAGIC);
                DiskBasedCache.writeString(outputStream, this.key);
                DiskBasedCache.writeString(outputStream, this.etag == null ? "" : this.etag);
                DiskBasedCache.writeLong(outputStream, this.serverDate);
                DiskBasedCache.writeLong(outputStream, this.lastModified);
                DiskBasedCache.writeLong(outputStream, this.ttl);
                DiskBasedCache.writeLong(outputStream, this.softTtl);
                DiskBasedCache.writeHeaderList(this.allResponseHeaders, outputStream);
                outputStream.flush();
                return true;
            } catch (IOException e) {
                VolleyLog.m10d("%s", e.toString());
                return false;
            }
        }
    }

    @VisibleForTesting
    static class CountingInputStream extends FilterInputStream {
        private long bytesRead;
        private final long length;

        CountingInputStream(InputStream inputStream, long j) {
            super(inputStream);
            this.length = j;
        }

        public int read() throws IOException {
            int read = super.read();
            if (read != -1) {
                this.bytesRead++;
            }
            return read;
        }

        public int read(byte[] bArr, int i, int i2) throws IOException {
            int read = super.read(bArr, i, i2);
            if (read != -1) {
                this.bytesRead += (long) read;
            }
            return read;
        }

        /* access modifiers changed from: 0000 */
        @VisibleForTesting
        public long bytesRead() {
            return this.bytesRead;
        }

        /* access modifiers changed from: 0000 */
        public long bytesRemaining() {
            return this.length - this.bytesRead;
        }
    }

    public DiskBasedCache(File file, int i) {
        this.mEntries = new LinkedHashMap(16, 0.75f, true);
        this.mTotalSize = 0;
        this.mRootDirectory = file;
        this.mMaxCacheSizeInBytes = i;
    }

    public DiskBasedCache(File file) {
        this(file, DEFAULT_DISK_USAGE_BYTES);
    }

    public synchronized void clear() {
        File[] listFiles = this.mRootDirectory.listFiles();
        if (listFiles != null) {
            for (File delete : listFiles) {
                delete.delete();
            }
        }
        this.mEntries.clear();
        this.mTotalSize = 0;
        VolleyLog.m10d("Cache cleared.", new Object[0]);
    }

    public synchronized Entry get(String str) {
        CountingInputStream countingInputStream;
        CacheHeader cacheHeader = (CacheHeader) this.mEntries.get(str);
        if (cacheHeader == null) {
            return null;
        }
        File fileForKey = getFileForKey(str);
        try {
            countingInputStream = new CountingInputStream(new BufferedInputStream(createInputStream(fileForKey)), fileForKey.length());
            CacheHeader readHeader = CacheHeader.readHeader(countingInputStream);
            if (!TextUtils.equals(str, readHeader.key)) {
                VolleyLog.m10d("%s: key=%s, found=%s", fileForKey.getAbsolutePath(), str, readHeader.key);
                removeEntry(str);
                countingInputStream.close();
                return null;
            }
            Entry cacheEntry = cacheHeader.toCacheEntry(streamToBytes(countingInputStream, countingInputStream.bytesRemaining()));
            countingInputStream.close();
            return cacheEntry;
        } catch (IOException e) {
            VolleyLog.m10d("%s: %s", fileForKey.getAbsolutePath(), e.toString());
            remove(str);
            return null;
        } catch (Throwable th) {
            countingInputStream.close();
            throw th;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0023, code lost:
        return;
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:27:0x0059 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void initialize() {
        /*
            r9 = this;
            monitor-enter(r9)
            java.io.File r0 = r9.mRootDirectory     // Catch:{ all -> 0x0061 }
            boolean r0 = r0.exists()     // Catch:{ all -> 0x0061 }
            r1 = 0
            if (r0 != 0) goto L_0x0024
            java.io.File r0 = r9.mRootDirectory     // Catch:{ all -> 0x0061 }
            boolean r0 = r0.mkdirs()     // Catch:{ all -> 0x0061 }
            if (r0 != 0) goto L_0x0022
            java.lang.String r0 = "Unable to create cache dir %s"
            r2 = 1
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ all -> 0x0061 }
            java.io.File r3 = r9.mRootDirectory     // Catch:{ all -> 0x0061 }
            java.lang.String r3 = r3.getAbsolutePath()     // Catch:{ all -> 0x0061 }
            r2[r1] = r3     // Catch:{ all -> 0x0061 }
            com.android.volley.VolleyLog.m11e(r0, r2)     // Catch:{ all -> 0x0061 }
        L_0x0022:
            monitor-exit(r9)
            return
        L_0x0024:
            java.io.File r0 = r9.mRootDirectory     // Catch:{ all -> 0x0061 }
            java.io.File[] r0 = r0.listFiles()     // Catch:{ all -> 0x0061 }
            if (r0 != 0) goto L_0x002e
            monitor-exit(r9)
            return
        L_0x002e:
            int r2 = r0.length     // Catch:{ all -> 0x0061 }
        L_0x002f:
            if (r1 >= r2) goto L_0x005f
            r3 = r0[r1]     // Catch:{ all -> 0x0061 }
            long r4 = r3.length()     // Catch:{ IOException -> 0x0059 }
            com.android.volley.toolbox.DiskBasedCache$CountingInputStream r6 = new com.android.volley.toolbox.DiskBasedCache$CountingInputStream     // Catch:{ IOException -> 0x0059 }
            java.io.BufferedInputStream r7 = new java.io.BufferedInputStream     // Catch:{ IOException -> 0x0059 }
            java.io.InputStream r8 = r9.createInputStream(r3)     // Catch:{ IOException -> 0x0059 }
            r7.<init>(r8)     // Catch:{ IOException -> 0x0059 }
            r6.<init>(r7, r4)     // Catch:{ IOException -> 0x0059 }
            com.android.volley.toolbox.DiskBasedCache$CacheHeader r7 = com.android.volley.toolbox.DiskBasedCache.CacheHeader.readHeader(r6)     // Catch:{ all -> 0x0054 }
            r7.size = r4     // Catch:{ all -> 0x0054 }
            java.lang.String r4 = r7.key     // Catch:{ all -> 0x0054 }
            r9.putEntry(r4, r7)     // Catch:{ all -> 0x0054 }
            r6.close()     // Catch:{ IOException -> 0x0059 }
            goto L_0x005c
        L_0x0054:
            r4 = move-exception
            r6.close()     // Catch:{ IOException -> 0x0059 }
            throw r4     // Catch:{ IOException -> 0x0059 }
        L_0x0059:
            r3.delete()     // Catch:{ all -> 0x0061 }
        L_0x005c:
            int r1 = r1 + 1
            goto L_0x002f
        L_0x005f:
            monitor-exit(r9)
            return
        L_0x0061:
            r0 = move-exception
            monitor-exit(r9)
            throw r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.volley.toolbox.DiskBasedCache.initialize():void");
    }

    public synchronized void invalidate(String str, boolean z) {
        Entry entry = get(str);
        if (entry != null) {
            entry.softTtl = 0;
            if (z) {
                entry.ttl = 0;
            }
            put(str, entry);
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:13|14|(1:16)|17|18) */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0048, code lost:
        if (r0.delete() == false) goto L_0x004a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x004a, code lost:
        com.android.volley.VolleyLog.m10d("Could not clean up file %s", r0.getAbsolutePath());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0058, code lost:
        return;
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0044 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void put(java.lang.String r7, com.android.volley.Cache.Entry r8) {
        /*
            r6 = this;
            monitor-enter(r6)
            byte[] r0 = r8.data     // Catch:{ all -> 0x0059 }
            int r0 = r0.length     // Catch:{ all -> 0x0059 }
            r6.pruneIfNeeded(r0)     // Catch:{ all -> 0x0059 }
            java.io.File r0 = r6.getFileForKey(r7)     // Catch:{ all -> 0x0059 }
            r1 = 0
            r2 = 1
            java.io.BufferedOutputStream r3 = new java.io.BufferedOutputStream     // Catch:{ IOException -> 0x0044 }
            java.io.OutputStream r4 = r6.createOutputStream(r0)     // Catch:{ IOException -> 0x0044 }
            r3.<init>(r4)     // Catch:{ IOException -> 0x0044 }
            com.android.volley.toolbox.DiskBasedCache$CacheHeader r4 = new com.android.volley.toolbox.DiskBasedCache$CacheHeader     // Catch:{ IOException -> 0x0044 }
            r4.<init>(r7, r8)     // Catch:{ IOException -> 0x0044 }
            boolean r5 = r4.writeHeader(r3)     // Catch:{ IOException -> 0x0044 }
            if (r5 == 0) goto L_0x002e
            byte[] r8 = r8.data     // Catch:{ IOException -> 0x0044 }
            r3.write(r8)     // Catch:{ IOException -> 0x0044 }
            r3.close()     // Catch:{ IOException -> 0x0044 }
            r6.putEntry(r7, r4)     // Catch:{ IOException -> 0x0044 }
            monitor-exit(r6)
            return
        L_0x002e:
            r3.close()     // Catch:{ IOException -> 0x0044 }
            java.lang.String r7 = "Failed to write header for %s"
            java.lang.Object[] r8 = new java.lang.Object[r2]     // Catch:{ IOException -> 0x0044 }
            java.lang.String r3 = r0.getAbsolutePath()     // Catch:{ IOException -> 0x0044 }
            r8[r1] = r3     // Catch:{ IOException -> 0x0044 }
            com.android.volley.VolleyLog.m10d(r7, r8)     // Catch:{ IOException -> 0x0044 }
            java.io.IOException r7 = new java.io.IOException     // Catch:{ IOException -> 0x0044 }
            r7.<init>()     // Catch:{ IOException -> 0x0044 }
            throw r7     // Catch:{ IOException -> 0x0044 }
        L_0x0044:
            boolean r7 = r0.delete()     // Catch:{ all -> 0x0059 }
            if (r7 != 0) goto L_0x0057
            java.lang.String r7 = "Could not clean up file %s"
            java.lang.Object[] r8 = new java.lang.Object[r2]     // Catch:{ all -> 0x0059 }
            java.lang.String r0 = r0.getAbsolutePath()     // Catch:{ all -> 0x0059 }
            r8[r1] = r0     // Catch:{ all -> 0x0059 }
            com.android.volley.VolleyLog.m10d(r7, r8)     // Catch:{ all -> 0x0059 }
        L_0x0057:
            monitor-exit(r6)
            return
        L_0x0059:
            r7 = move-exception
            monitor-exit(r6)
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.volley.toolbox.DiskBasedCache.put(java.lang.String, com.android.volley.Cache$Entry):void");
    }

    public synchronized void remove(String str) {
        boolean delete = getFileForKey(str).delete();
        removeEntry(str);
        if (!delete) {
            VolleyLog.m10d("Could not delete cache entry for key=%s, filename=%s", str, getFilenameForKey(str));
        }
    }

    private String getFilenameForKey(String str) {
        int length = str.length() / 2;
        String valueOf = String.valueOf(str.substring(0, length).hashCode());
        StringBuilder sb = new StringBuilder();
        sb.append(valueOf);
        sb.append(String.valueOf(str.substring(length).hashCode()));
        return sb.toString();
    }

    public File getFileForKey(String str) {
        return new File(this.mRootDirectory, getFilenameForKey(str));
    }

    private void pruneIfNeeded(int i) {
        long j;
        long j2 = (long) i;
        if (this.mTotalSize + j2 >= ((long) this.mMaxCacheSizeInBytes)) {
            if (VolleyLog.DEBUG) {
                VolleyLog.m13v("Pruning old cache entries.", new Object[0]);
            }
            long j3 = this.mTotalSize;
            long elapsedRealtime = SystemClock.elapsedRealtime();
            Iterator it = this.mEntries.entrySet().iterator();
            int i2 = 0;
            while (it.hasNext()) {
                CacheHeader cacheHeader = (CacheHeader) ((Map.Entry) it.next()).getValue();
                if (getFileForKey(cacheHeader.key).delete()) {
                    j = j2;
                    this.mTotalSize -= cacheHeader.size;
                } else {
                    j = j2;
                    VolleyLog.m10d("Could not delete cache entry for key=%s, filename=%s", cacheHeader.key, getFilenameForKey(cacheHeader.key));
                }
                it.remove();
                i2++;
                if (((float) (this.mTotalSize + j)) < ((float) this.mMaxCacheSizeInBytes) * HYSTERESIS_FACTOR) {
                    break;
                }
                j2 = j;
            }
            if (VolleyLog.DEBUG) {
                VolleyLog.m13v("pruned %d files, %d bytes, %d ms", Integer.valueOf(i2), Long.valueOf(this.mTotalSize - j3), Long.valueOf(SystemClock.elapsedRealtime() - elapsedRealtime));
            }
        }
    }

    private void putEntry(String str, CacheHeader cacheHeader) {
        if (!this.mEntries.containsKey(str)) {
            this.mTotalSize += cacheHeader.size;
        } else {
            this.mTotalSize += cacheHeader.size - ((CacheHeader) this.mEntries.get(str)).size;
        }
        this.mEntries.put(str, cacheHeader);
    }

    private void removeEntry(String str) {
        CacheHeader cacheHeader = (CacheHeader) this.mEntries.remove(str);
        if (cacheHeader != null) {
            this.mTotalSize -= cacheHeader.size;
        }
    }

    static byte[] streamToBytes(CountingInputStream countingInputStream, long j) throws IOException {
        long bytesRemaining = countingInputStream.bytesRemaining();
        if (j >= 0 && j <= bytesRemaining) {
            int i = (int) j;
            if (((long) i) == j) {
                byte[] bArr = new byte[i];
                new DataInputStream(countingInputStream).readFully(bArr);
                return bArr;
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("streamToBytes length=");
        sb.append(j);
        sb.append(", maxLength=");
        sb.append(bytesRemaining);
        throw new IOException(sb.toString());
    }

    /* access modifiers changed from: 0000 */
    public InputStream createInputStream(File file) throws FileNotFoundException {
        return new FileInputStream(file);
    }

    /* access modifiers changed from: 0000 */
    public OutputStream createOutputStream(File file) throws FileNotFoundException {
        return new FileOutputStream(file);
    }

    private static int read(InputStream inputStream) throws IOException {
        int read = inputStream.read();
        if (read != -1) {
            return read;
        }
        throw new EOFException();
    }

    static void writeInt(OutputStream outputStream, int i) throws IOException {
        outputStream.write((i >> 0) & 255);
        outputStream.write((i >> 8) & 255);
        outputStream.write((i >> 16) & 255);
        outputStream.write((i >> 24) & 255);
    }

    static int readInt(InputStream inputStream) throws IOException {
        return (read(inputStream) << 24) | (read(inputStream) << 0) | 0 | (read(inputStream) << 8) | (read(inputStream) << 16);
    }

    static void writeLong(OutputStream outputStream, long j) throws IOException {
        outputStream.write((byte) ((int) (j >>> 0)));
        outputStream.write((byte) ((int) (j >>> 8)));
        outputStream.write((byte) ((int) (j >>> 16)));
        outputStream.write((byte) ((int) (j >>> 24)));
        outputStream.write((byte) ((int) (j >>> 32)));
        outputStream.write((byte) ((int) (j >>> 40)));
        outputStream.write((byte) ((int) (j >>> 48)));
        outputStream.write((byte) ((int) (j >>> 56)));
    }

    static long readLong(InputStream inputStream) throws IOException {
        return ((((long) read(inputStream)) & 255) << 0) | 0 | ((((long) read(inputStream)) & 255) << 8) | ((((long) read(inputStream)) & 255) << 16) | ((((long) read(inputStream)) & 255) << 24) | ((((long) read(inputStream)) & 255) << 32) | ((((long) read(inputStream)) & 255) << 40) | ((((long) read(inputStream)) & 255) << 48) | ((255 & ((long) read(inputStream))) << 56);
    }

    static void writeString(OutputStream outputStream, String str) throws IOException {
        byte[] bytes = str.getBytes("UTF-8");
        writeLong(outputStream, (long) bytes.length);
        outputStream.write(bytes, 0, bytes.length);
    }

    static String readString(CountingInputStream countingInputStream) throws IOException {
        return new String(streamToBytes(countingInputStream, readLong(countingInputStream)), "UTF-8");
    }

    static void writeHeaderList(List<Header> list, OutputStream outputStream) throws IOException {
        if (list != null) {
            writeInt(outputStream, list.size());
            for (Header header : list) {
                writeString(outputStream, header.getName());
                writeString(outputStream, header.getValue());
            }
            return;
        }
        writeInt(outputStream, 0);
    }

    static List<Header> readHeaderList(CountingInputStream countingInputStream) throws IOException {
        int readInt = readInt(countingInputStream);
        if (readInt >= 0) {
            List<Header> emptyList = readInt == 0 ? Collections.emptyList() : new ArrayList<>();
            for (int i = 0; i < readInt; i++) {
                emptyList.add(new Header(readString(countingInputStream).intern(), readString(countingInputStream).intern()));
            }
            return emptyList;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("readHeaderList size=");
        sb.append(readInt);
        throw new IOException(sb.toString());
    }
}
