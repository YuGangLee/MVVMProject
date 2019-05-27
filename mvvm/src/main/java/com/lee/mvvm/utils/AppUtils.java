package com.lee.mvvm.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.List;

/**
 * @author 请叫我张懂
 * @createTime 2018/7/10 21:33
 * @description
 */

public class AppUtils {

    private static SoftReference<Context> contextReference;

    private AppUtils() {
        throw new UnsupportedOperationException("You can't instantiate me...");
    }

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    public static void init(Context context) {
        AppUtils.contextReference = new SoftReference<>(context);
    }

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    public static Context getContext() {
        if (contextReference != null && contextReference.get() != null)
            return contextReference.get();
        throw new NullPointerException("You should init first");
    }

    /**
     * View获取Activity的工具
     *
     * @param view view
     * @return Activity
     */
    public static Activity getActivity(View view) {
        Context context = view.getContext();

        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }

        throw new IllegalStateException("View " + view + " is not attached to an Activity");
    }

    /**
     * 全局获取String的方法
     *
     * @param id 资源Id
     * @return String
     */
    public static String getString(@StringRes int id) {
        if (getContext() != null) {
            return getContext().getResources().getString(id);
        } else {
            throw new NullPointerException("You should init first");
        }
    }

    /**
     * 判断App是否是Debug版本
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isAppDebug() {
        if (StringUtils.isSpace(getContext().getPackageName())) return false;
        try {
            PackageManager pm = getContext().getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(getContext().getPackageName(), 0);
            return ai != null && (ai.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 判断目标activity是否在前台运行
     *
     * @param className Activity Name
     * @return is running on foreground
     */
    public static boolean isActivityForeground(String className) {
        Context context = getContext();
        if (context == null || TextUtils.isEmpty(className)) {
            return false;
        }

        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (manager == null) {
            return false;
        }
        List<ActivityManager.RunningTaskInfo> list = manager.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            return className.equals(cpn.getClassName());
        }
        return false;
    }

    private static <T> void checkNotNull(T obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
    }

    /**
     * The {@code fragment} is added to the container view with id {@code frameId}. The operation is
     * performed by the {@code fragmentManager}.
     */
    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                             @NonNull Fragment fragment, int frameId) {
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();
    }

    public static String getVersionCode() {
        int versionCode = -1;
        try {
            versionCode =
                    getContext().getPackageManager()
                            .getPackageInfo(getContext().getPackageName(), PackageManager.GET_ACTIVITIES)
                            .versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return (versionCode == -1) ? "UnKnow" : String.valueOf(versionCode);
    }

    public static String getVersionName() {
        String versionName = "UnKnow";
        try {
            versionName =
                    getContext().getPackageManager()
                            .getPackageInfo(getContext().getPackageName(), PackageManager.GET_ACTIVITIES)
                            .versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    public static String getMobileInfo() {
        return StringUtils.builder(Build.BRAND, " ", Build.MODEL);
    }

    public static String getSdkInt() {
        return String.valueOf(Build.VERSION.SDK_INT);
    }

    /**
     * 获取项目存储文件(对应清除数据操作的文件夹)
     * 通过Context.getExternalFilesDir()方法可以获取到 SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
     *
     * @param type
     * @return
     */
    public static File getRootFilesDir(String type) {
        return getContext().getExternalFilesDir(type);
    }

    /**
     * 获取项目缓存文件(对应清除数据操作的文件夹)
     * 通过Context.getExternalCacheDir()方法可以获取到 SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
     *
     * @return
     */
    public static File getRootCacheDir() {
        return getContext().getExternalCacheDir();
    }

    public static int getScreenWidth() {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    public static int getScreenHeight() {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

    public static int dp2px(float dpValue) {
        float scale = contextReference.get().getResources().getDisplayMetrics().density;
        return Math.round(dpValue * scale + 0.5f);
    }

    public static int px2dp(float pxValue) {
        float scale = contextReference.get().getResources().getDisplayMetrics().density;
        return Math.round(pxValue / scale + 0.5f);
    }
}
