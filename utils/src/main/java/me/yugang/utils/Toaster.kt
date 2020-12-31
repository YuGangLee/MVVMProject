package me.yugang.utils

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.LayoutRes
import java.lang.ref.SoftReference

/**
 * after [Build.VERSION_CODES.R] is no longer support
 *
 * @see Toast.setView
 */
@Deprecated("No longer supported after api ${Build.VERSION_CODES.R}")
object Toaster {
    private var softReference: SoftReference<MyToast?> = SoftReference(null)

    @LayoutRes
    private var layoutRes = -1
    private var toastContentSetter: (view: View?, content: String) -> Unit = { _, _ -> }

    fun with(context: Context): MyToast {
        synchronized(this) {
            if (layoutRes == -1) {
                Log.e("Toaster", "Toaster need config first")
            }
            softReference = if (softReference.get() == null) {
                SoftReference(MyToast(context))
            } else {
                softReference.get()!!.cancel()
                SoftReference(MyToast(context))
            }
        }
        return softReference.get()!!
    }

    fun config(@LayoutRes layout: Int, toastContentSetter: (view: View?, content: String) -> Unit) {
        this.layoutRes = layout
        this.toastContentSetter = toastContentSetter
    }

    class MyToast(context: Context) : Toast(context) {

        init {
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = if (layoutRes != -1) {
                inflater.inflate(layoutRes, null, false)
            } else {
                TextView(context)
            }
        }

        fun showShort(content: String) {
            toastContentSetter(view, content)
            duration = LENGTH_SHORT
            show()
        }

        fun showLong(content: String) {
            toastContentSetter(view, content)
            duration = LENGTH_LONG
            show()
        }
    }
}