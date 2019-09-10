package com.cy.review.androidthreadtes;


/**
 * 一：
 * Android不允许在子线程中更新UI，
 * 如果需要在子线程当中更新相应的UI控件，可以通过Android提供的异步消息处理机制：
 *
 * 1.Handler类对象的handleMessage()方法中的代码就是在子线程当中执行的。
 * Message是在线程之间传递的消息，可以在内部携带少量的信息，例如what字段、arg1、arg2、obj携带Object对象；
 * Handler用于发送盒处理消息，发送一般是sendMes(),传递至handlerMessage()方法中；
 * MessageQueue消息队列；
 * Looper进行消息队列的管理，Looper对象的loop()方法会进入一个无限的循环当中，
 * 每当发现消息队列当中存在一条消息，就会将它取出传递到Handler类对象的handleMessage()方法当中。
 *
 * 2.runOnUIThread()方法：
 * 其实就是一个异步消息处理机制的接口封装。
 *
 * 3.AsyncTask：
 * onPreExecute()界面初始化工作；
 * DoInBackground()处理耗时任务；
 * onProgressUpdate()进行UI操作；
 * onPostExecute()执行任务的收尾工作，可以操作UI。
 *
 * 二、Java开启线程的四种方式：
 * 继承Thread类；
 * 实现Runable接口；
 * 实现Callable接口；
 * 线程池
 */

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public static final int UPDATE_TEXT = 1;//定义一个常量用来表示某个动作

    private TextView text;


    /**
     * @SuppressLint("HandlerLeak")
     * 警告原因以及解决：
     * Handler在Android中用于消息的发送与异步处理，常常在Activity中作为一个匿名内部类来定义，
     * 此时Handler会隐式地持有一个外部类对象（通常是一个Activity）的引用。
     * 当Activity已经被用户关闭时，由于Handler持有Activity的引用造成Activity无法被GC回收，这样容易造成内存泄露。
     * 解决办法：将其定义成一个静态内部类（此时不会持有外部类对象的引用），
     * 在构造方法中传入Activity并对Activity对象增加一个弱引用，这样Activity被用户关闭之后，
     * 即便异步消息还未处理完毕，Activity也能够被GC回收，从而避免了内存泄露。
     */
    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case UPDATE_TEXT:
                    //可以在这里进行UI操作
                    text.setText("Nice to meet you!");
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text =  findViewById(R.id.text);
        Button changeText =  findViewById(R.id.change_text);
        changeText.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change_text:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = UPDATE_TEXT;//what字段的值等于常量UPDATE_TEXT的值
                        handler.sendMessage(message); // 将Message对象发送出去
                    }
                }).start();
                break;
            default:
                break;
        }
    }

}
