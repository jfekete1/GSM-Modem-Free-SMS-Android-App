package com.android.volley.toolbox;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.support.annotation.GuardedBy;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.widget.ImageView.ScaleType;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Request.Priority;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyLog;

public class ImageRequest extends Request<Bitmap> {
    public static final float DEFAULT_IMAGE_BACKOFF_MULT = 2.0f;
    public static final int DEFAULT_IMAGE_MAX_RETRIES = 2;
    public static final int DEFAULT_IMAGE_TIMEOUT_MS = 1000;
    private static final Object sDecodeLock = new Object();
    private final Config mDecodeConfig;
    @Nullable
    @GuardedBy("mLock")
    private Listener<Bitmap> mListener;
    private final Object mLock;
    private final int mMaxHeight;
    private final int mMaxWidth;
    private final ScaleType mScaleType;

    public ImageRequest(String str, Listener<Bitmap> listener, int i, int i2, ScaleType scaleType, Config config, @Nullable ErrorListener errorListener) {
        super(0, str, errorListener);
        this.mLock = new Object();
        setRetryPolicy(new DefaultRetryPolicy(1000, 2, 2.0f));
        this.mListener = listener;
        this.mDecodeConfig = config;
        this.mMaxWidth = i;
        this.mMaxHeight = i2;
        this.mScaleType = scaleType;
    }

    @Deprecated
    public ImageRequest(String str, Listener<Bitmap> listener, int i, int i2, Config config, ErrorListener errorListener) {
        this(str, listener, i, i2, ScaleType.CENTER_INSIDE, config, errorListener);
    }

    public Priority getPriority() {
        return Priority.LOW;
    }

    private static int getResizedDimension(int i, int i2, int i3, int i4, ScaleType scaleType) {
        if (i == 0 && i2 == 0) {
            return i3;
        }
        if (scaleType == ScaleType.FIT_XY) {
            return i == 0 ? i3 : i;
        }
        if (i == 0) {
            double d = (double) i2;
            double d2 = (double) i4;
            Double.isNaN(d);
            Double.isNaN(d2);
            double d3 = d / d2;
            double d4 = (double) i3;
            Double.isNaN(d4);
            return (int) (d4 * d3);
        } else if (i2 == 0) {
            return i;
        } else {
            double d5 = (double) i4;
            double d6 = (double) i3;
            Double.isNaN(d5);
            Double.isNaN(d6);
            double d7 = d5 / d6;
            if (scaleType == ScaleType.CENTER_CROP) {
                double d8 = (double) i;
                Double.isNaN(d8);
                double d9 = (double) i2;
                if (d8 * d7 < d9) {
                    Double.isNaN(d9);
                    i = (int) (d9 / d7);
                }
                return i;
            }
            double d10 = (double) i;
            Double.isNaN(d10);
            double d11 = (double) i2;
            if (d10 * d7 > d11) {
                Double.isNaN(d11);
                i = (int) (d11 / d7);
            }
            return i;
        }
    }

    /* access modifiers changed from: protected */
    public Response<Bitmap> parseNetworkResponse(NetworkResponse networkResponse) {
        Response<Bitmap> doParse;
        synchronized (sDecodeLock) {
            try {
                doParse = doParse(networkResponse);
            } catch (OutOfMemoryError e) {
                VolleyLog.m11e("Caught OOM for %d byte image, url=%s", Integer.valueOf(networkResponse.data.length), getUrl());
                return Response.error(new ParseError((Throwable) e));
            } catch (Throwable th) {
                throw th;
            }
        }
        return doParse;
    }

    private Response<Bitmap> doParse(NetworkResponse networkResponse) {
        Bitmap bitmap;
        byte[] bArr = networkResponse.data;
        Options options = new Options();
        if (this.mMaxWidth == 0 && this.mMaxHeight == 0) {
            options.inPreferredConfig = this.mDecodeConfig;
            bitmap = BitmapFactory.decodeByteArray(bArr, 0, bArr.length, options);
        } else {
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(bArr, 0, bArr.length, options);
            int i = options.outWidth;
            int i2 = options.outHeight;
            int resizedDimension = getResizedDimension(this.mMaxWidth, this.mMaxHeight, i, i2, this.mScaleType);
            int resizedDimension2 = getResizedDimension(this.mMaxHeight, this.mMaxWidth, i2, i, this.mScaleType);
            options.inJustDecodeBounds = false;
            options.inSampleSize = findBestSampleSize(i, i2, resizedDimension, resizedDimension2);
            bitmap = BitmapFactory.decodeByteArray(bArr, 0, bArr.length, options);
            if (bitmap != null && (bitmap.getWidth() > resizedDimension || bitmap.getHeight() > resizedDimension2)) {
                Bitmap createScaledBitmap = Bitmap.createScaledBitmap(bitmap, resizedDimension, resizedDimension2, true);
                bitmap.recycle();
                bitmap = createScaledBitmap;
            }
        }
        if (bitmap == null) {
            return Response.error(new ParseError(networkResponse));
        }
        return Response.success(bitmap, HttpHeaderParser.parseCacheHeaders(networkResponse));
    }

    public void cancel() {
        super.cancel();
        synchronized (this.mLock) {
            this.mListener = null;
        }
    }

    /* access modifiers changed from: protected */
    public void deliverResponse(Bitmap bitmap) {
        Listener<Bitmap> listener;
        synchronized (this.mLock) {
            listener = this.mListener;
        }
        if (listener != null) {
            listener.onResponse(bitmap);
        }
    }

    @VisibleForTesting
    static int findBestSampleSize(int i, int i2, int i3, int i4) {
        double d = (double) i;
        double d2 = (double) i3;
        Double.isNaN(d);
        Double.isNaN(d2);
        double d3 = d / d2;
        double d4 = (double) i2;
        double d5 = (double) i4;
        Double.isNaN(d4);
        Double.isNaN(d5);
        double min = Math.min(d3, d4 / d5);
        float f = 1.0f;
        while (true) {
            float f2 = 2.0f * f;
            if (((double) f2) > min) {
                return (int) f;
            }
            f = f2;
        }
    }
}
