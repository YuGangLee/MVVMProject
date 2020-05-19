package me.yugang.mvvmproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.view_toaster.view.*
import me.yugang.utils.Toaster

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Toaster.config(R.layout.view_toaster) { view, content ->
            view.tvToastContent.text = content
        }
        Toaster.with(this).showShort("Test")
    }
}
