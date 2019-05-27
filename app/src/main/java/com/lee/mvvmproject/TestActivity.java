package com.lee.mvvmproject;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.jeremyliao.liveeventbus.LiveEventBus;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        TextView tv = findViewById(R.id.tv_test);
        LiveEventBus.get().with("test", String.class).observe(this, tv::setText);
        tv.setOnClickListener(v -> LiveEventBus.get().with("test", String.class).post("testtest"));
    }
}
