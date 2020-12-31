package me.yugang.mvvm.ext

import android.app.Activity
import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.abs

open class KeyboardActivity : AppCompatActivity() {
    private var isMoved = false
    private var lastY = 0f

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        val touchEvent: Boolean = super.dispatchTouchEvent(event)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastY = event.y
                isMoved = false
            }
            MotionEvent.ACTION_MOVE -> if (abs(event.y - lastY) > 10) {
                isMoved = true
            }
            MotionEvent.ACTION_UP -> {
                val view: View? = currentFocus
                if (view != null && !isMoved) {
                    hideKeyboard(event, view, this@KeyboardActivity) //调用方法判断是否需要隐藏键盘
                }
                isMoved = false
            }
            else -> {
            }
        }
        return touchEvent
    }

    /**
     * 根据传入控件的坐标和用户的焦点坐标，判断是否隐藏键盘，如果点击的位置在控件内，则不隐藏键盘
     *
     * @param view  控件view
     * @param event 焦点位置
     * @return 是否隐藏
     */
    fun hideKeyboard(
        event: MotionEvent, view: View,
        activity: Activity
    ) {
        try {
            if (view is EditText) {
                val location = intArrayOf(0, 0)
                view.getLocationInWindow(location)
                val left = location[0]
                val top = location[1]
                val right = left + view.getWidth()
                val bottom = top + view.getHeight()
                // 判断焦点位置坐标是否在空间内
                if (event.rawX < left || event.rawX > right || event.rawY < top || event.rawY > bottom
                ) { // 隐藏键盘
                    hideSoftInput(view, activity)
                }
            } else {
                hideSoftInput(view, activity)
            }
        } catch (ignored: Exception) {
        }
    }

    private fun hideSoftInput(view: View, activity: Activity) {
        val token = view.windowToken
        val inputMethodManager = (activity
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
        inputMethodManager.hideSoftInputFromWindow(
            token,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
        view.clearFocus()
    }
}