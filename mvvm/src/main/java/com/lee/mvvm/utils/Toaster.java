package com.lee.mvvm.utils;

import android.os.Handler;
import android.os.Looper;
import androidx.annotation.StringRes;
import android.widget.Toast;

/**
 * 一个简单的Toast工具
 */
public class Toaster {

    private static Handler handler = new Handler(Looper.getMainLooper());

    private static Toast toast;

    public static void toastShort(CharSequence msg) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(AppUtils.getContext(), msg, Toast.LENGTH_SHORT);
        if (Looper.myLooper() != Looper.getMainLooper()) {
            handler.post(() -> toast.show());
        } else {
            toast.show();
        }
    }

    public static void toastLong(CharSequence msg) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(AppUtils.getContext(), msg, Toast.LENGTH_LONG);
        if (Looper.myLooper() != Looper.getMainLooper()) {
            handler.post(() -> toast.show());
        } else {
            toast.show();
        }
    }

    public static void toastShort(@StringRes int stringId) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(AppUtils.getContext(), stringId, Toast.LENGTH_SHORT);
        if (Looper.myLooper() != Looper.getMainLooper()) {
            handler.post(() -> toast.show());
        } else {
            toast.show();
        }
    }

    public static void toastLong(@StringRes int stringId) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(AppUtils.getContext(), stringId, Toast.LENGTH_LONG);
        if (Looper.myLooper() != Looper.getMainLooper()) {
            handler.post(() -> toast.show());
        } else {
            toast.show();
        }
    }
}
