package com.cy.review.notificationtest;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * 通知的基本用法：
 * PendingIntent -> NotificationManager -> Notification -> notificationManager.notify()
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button sendNotice = findViewById(R.id.send_notice);
        sendNotice.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_notice:
                Toast.makeText(this,"通知",Toast.LENGTH_SHORT).show();
                /*
                创建和取消通知：
                1.由于各个版本Android系统对通知功能的修改，导致api不稳定性问题，
                所以可以使用surport库中提供的兼容api解决，例如surport-v4中提供的NotificationCompat类，
                Android官方提供了NotificationCompat兼容类来帮助开发实现体验统一的Notification。
                2.
                NotificationCompat.Builder(Context)已经过时，
                可以用 NotificationCompat.Builder(Context，String)代替，第二个参数channelId与通知的优先级有关。
                3.
                在设置setContentIntent(pi)之前，通知显示能在状态栏或者下拉通知列表中显示，但是是没有点击效果的，
                需要添加一个延迟性质的意图PendingIntent，
                PendingIntent和Intent有些相似，但是它更倾向于在某个合适的时机去执行某个动作，
                而不是像Intent一样倾向于立即执行某个动作。
                4.
                通知被点击以后不会自动消除通知的存在，它会一直处于状态栏中，需要进行相关设置：
                setAutoCancel(true)
                或者在意图跳转的目标活动中通过NotificationManager进行设置manager.cancel(为某条通知指定的id);

                通知的进阶技巧和高级功能：
                1除了创建基本的通知，还有许多的效果都可以进行设置，并且NotificationCompat.Builder都含有API提供，
                包括设置通知时播放音频setSound(),通知时手机振动setVibrate()，通知时手机LED灯闪动setLights()等，
                还有通知当中需要设置长段文字或者大图的时候等等；
                还可以进行通知优先级的设置等情况。
                有的设置可能需要声明权限，这些在需要的时候可以查阅文档。
                 */
                NotificationManager manager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                Intent intent=new Intent(this,NotificationActivity.class);
                PendingIntent pi=PendingIntent.getActivity(this,0,intent,	0);

                Notification notification=new NotificationCompat.Builder(this,null)
                        .setContentTitle("This is content title")//标题内容
                        .setContentText("This is content text")//正文内容
                        .setWhen(System.currentTimeMillis())//通知被创建时间，单位毫秒
                        .setSmallIcon(R.mipmap.ic_launcher)//通知小图标，只能使用纯alpha图层的图片进行设置
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))//通知大图标
                        .setContentIntent(pi)//添加意图PendingIntent,这样这条通知才会有点击后的效果
                        .setDefaults(NotificationCompat.DEFAULT_ALL)//设置通知默认的全部效果，例如什么铃声或如何振动等等
                        .setPriority(NotificationCompat.PRIORITY_MAX)//将通知的优先级设置到最高，这个时候通知会从状态栏弹出一个明显的横幅，并且附带详细内容。
                        .setAutoCancel(true)//设置点击通知以后，不在显示在状态栏通知当中
                        .build();
                manager.notify(1,notification);
            default:
                break;
        }
    }
}
