package com.cy.review.sharedpreferencestest;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * 使用SP方式存储数据主要通过
 * 向SharedPreferences的SharedPreferences.Editor对象添加数据。
 * SharedPreferences.Editor对象通过SharedPreferences对象的edit()方法获取。
 * 而获得SharedPreferences对象主要有三种方法：
 * Context类中的getSharedPreferences()方法；
 * Activity类中的getPreferences()方法；
 * PreferenceManager类中的getDefaultSharedPreferences()方法。
 * 添加数据以后需要进行提交，
 * SharedPreferences.Editor中有两个方法：commit()和apply()，
 * 这两个方法都是数据的提交，但是存在一些区别：
 * 首先commit()方法会在提交数据后返回一个修改是否成功的布尔值，apply()方法修改数据后没有返回结果。
 * 另外，commit()是同步提交，commit将同步的把数据写入磁盘和内存缓存；
 * apply()是异步提交，但是apply()是一个原子请求,会把数据同步写入内存缓存，然后异步保存到磁盘。
 *
 * 读取数据通过SharedPreferences对象对应不同数据类型的get()方法获取。
 *
 *
 */

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //存储数据
        Button saveData = findViewById(R.id.save_data);
        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                editor.putString("name", "Tom");
                editor.putInt("age", 28);
                editor.putBoolean("married", false);
                editor.apply();
            }
        });

        //读取数据
        Button restoreData = findViewById(R.id.restore_data);
        restoreData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences=getSharedPreferences("data",MODE_PRIVATE);
                String name=sharedPreferences.getString("name",null);
                int age=sharedPreferences.getInt("age",0);
                boolean married=sharedPreferences.getBoolean("married",false);
                Log.d(TAG, "name is "+name);
                Log.d(TAG, "age is "+age);
                Log.d(TAG, "married is "+married);
            }
        });
    }
}
