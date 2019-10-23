package com.android.volley;

import android.support.p004v7.widget.helper.ItemTouchHelper.Callback;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class NetworkResponse {
    public final List<Header> allHeaders;
    public final byte[] data;
    public final Map<String, String> headers;
    public final long networkTimeMs;
    public final boolean notModified;
    public final int statusCode;

    @Deprecated
    public NetworkResponse(int i, byte[] bArr, Map<String, String> map, boolean z, long j) {
        this(i, bArr, map, toAllHeaderList(map), z, j);
    }

    public NetworkResponse(int i, byte[] bArr, boolean z, long j, List<Header> list) {
        this(i, bArr, toHeaderMap(list), list, z, j);
    }

    @Deprecated
    public NetworkResponse(int i, byte[] bArr, Map<String, String> map, boolean z) {
        this(i, bArr, map, z, 0);
    }

    public NetworkResponse(byte[] bArr) {
        this((int) Callback.DEFAULT_DRAG_ANIMATION_DURATION, bArr, false, 0, Collections.emptyList());
    }

    @Deprecated
    public NetworkResponse(byte[] bArr, Map<String, String> map) {
        this((int) Callback.DEFAULT_DRAG_ANIMATION_DURATION, bArr, map, false, 0);
    }

    private NetworkResponse(int i, byte[] bArr, Map<String, String> map, List<Header> list, boolean z, long j) {
        this.statusCode = i;
        this.data = bArr;
        this.headers = map;
        if (list == null) {
            this.allHeaders = null;
        } else {
            this.allHeaders = Collections.unmodifiableList(list);
        }
        this.notModified = z;
        this.networkTimeMs = j;
    }

    private static Map<String, String> toHeaderMap(List<Header> list) {
        if (list == null) {
            return null;
        }
        if (list.isEmpty()) {
            return Collections.emptyMap();
        }
        TreeMap treeMap = new TreeMap(String.CASE_INSENSITIVE_ORDER);
        for (Header header : list) {
            treeMap.put(header.getName(), header.getValue());
        }
        return treeMap;
    }

    private static List<Header> toAllHeaderList(Map<String, String> map) {
        if (map == null) {
            return null;
        }
        if (map.isEmpty()) {
            return Collections.emptyList();
        }
        ArrayList arrayList = new ArrayList(map.size());
        for (Entry entry : map.entrySet()) {
            arrayList.add(new Header((String) entry.getKey(), (String) entry.getValue()));
        }
        return arrayList;
    }
}
