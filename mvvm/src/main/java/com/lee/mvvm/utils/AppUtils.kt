package com.lee.mvvm.utils

import android.app.Activity
import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.View

import java.io.File
import java.lang.ref.SoftReference

/**
 * @author 请叫我张懂
 * @createTime 2018/7/10 21:33
 * @description
 */

object AppUtils {
    private var contextReference: SoftReference<Context>? = null

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    fun init(context: Context) {
        contextReference = SoftReference(context)
    }

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    val context: Context?
        get() {
            if (contextReference != null && contextReference!!.get() != null)
                return contextReference!!.get()
            throw NullPointerException("You should init first")
        }

    /**
     * View获取Activity的工具
     *
     * @param view view
     * @return Activity
     */
    fun getActivity(view: View): Activity {
        var context = view.context

        while (context is ContextWrapper) {
            if (context is Activity) {
                return context
            }
            context = context.baseContext
        }

        throw IllegalStateException("View $view is not attached to an Activity")
    }

    /**
     * 全局获取String的方法
     *
     * @param id 资源Id
     * @return String
     */
    fun getString(@StringRes id: Int): String {
        return if (context != null) {
            context!!.resources.getString(id)
        } else {
            throw NullPointerException("You should init first")
        }
    }

    /**
     * 判断App是否是Debug版本
     *
     * @return `true`: 是<br></br>`false`: 否
     */
    val isAppDebug: Boolean
        get() {
            if (StringUtils.isSpace(context!!.packageName)) return false
            try {
                val pm = context!!.packageManager
                val ai = pm.getApplicationInfo(context!!.packageName, 0)
                return ai != null && ai.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
                return false
            }

        }

    /**
     * 判断目标activity是否在前台运行
     *
     * @param className Activity Name
     * @return is running on foreground
     */
    fun isActivityForeground(className: String): Boolean {
        val context = context
        if (context == null || TextUtils.isEmpty(className)) {
            return false
        }

        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
                ?: return false
        val list = manager.getRunningTasks(1)
        if (list != null && list.size > 0) {
            val cpn = list[0].topActivity
            return className == cpn.className
        }
        return false
    }

    private fun <T> checkNotNull(obj: T?) {
        if (obj == null) {
            throw NullPointerException()
        }
    }

    /**
     * The `fragment` is added to the container view with id `frameId`. The operation is
     * performed by the `fragmentManager`.
     */
    fun addFragmentToActivity(fragmentManager: FragmentManager,
                              fragment: Fragment, frameId: Int) {
        checkNotNull(fragmentManager)
        checkNotNull(fragment)
        val transaction = fragmentManager.beginTransaction()
        transaction.add(frameId, fragment)
        transaction.commit()
    }

    val versionCode: String
        get() {
            var versionCode = -1
            try {
                versionCode = context!!.packageManager
                        .getPackageInfo(context!!.packageName, PackageManager.GET_ACTIVITIES)
                        .versionCode
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

            return if (versionCode == -1) "UnKnow" else versionCode.toString()
        }

    val versionName: String
        get() {
            var versionName = "UnKnow"
            try {
                versionName = context!!.packageManager
                        .getPackageInfo(context!!.packageName, PackageManager.GET_ACTIVITIES)
                        .versionName
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

            return versionName
        }

    val mobileInfo: String
        get() = StringUtils.builder(Build.BRAND, " ", Build.MODEL)

    val sdkInt: String
        get() = Build.VERSION.SDK_INT.toString()

    /**
     * 获取项目存储文件(对应清除数据操作的文件夹)
     * 通过Context.getExternalFilesDir()方法可以获取到 SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
     *
     * @param type
     * @return
     */
    fun getRootFilesDir(type: String): File? {
        return context!!.getExternalFilesDir(type)
    }

    /**
     * 获取项目缓存文件(对应清除数据操作的文件夹)
     * 通过Context.getExternalCacheDir()方法可以获取到 SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
     *
     * @return
     */
    val rootCacheDir: File?
        get() = context!!.externalCacheDir

    val screenWidth: Int
        get() {
            val displayMetrics = context!!.resources.displayMetrics
            return displayMetrics.widthPixels
        }

    val screenHeight: Int
        get() {
            val displayMetrics = context!!.resources.displayMetrics
            return displayMetrics.heightPixels
        }

    fun dp2px(dpValue: Float): Int {
        val scale = contextReference!!.get()!!.resources.displayMetrics.density
        return Math.round(dpValue * scale + 0.5f)
    }

    fun px2dp(pxValue: Float): Int {
        val scale = contextReference!!.get()!!.resources.displayMetrics.density
        return Math.round(pxValue / scale + 0.5f)
    }
}
