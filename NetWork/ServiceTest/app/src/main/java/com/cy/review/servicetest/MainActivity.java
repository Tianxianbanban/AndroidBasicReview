package com.cy.review.servicetest;

/**
 * 通常来讲活动只是通知服务去执行，
 * 但是也可以让活动和服务的关系更紧密一些，
 */

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity";

    private MyService.DownloadBinder downloadBinder;
    private ServiceConnection connection = new ServiceConnection() {

        //活动与服务断开时调用
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        //活动与服务绑定时调用
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            /*
            通过向下转型的到MyService.DownloadBinder的实例，
            有了这个实例，活动与服务的关系就变得非常紧密了，
            之后可以在活动中根据具体场景调用DownloadBinder的方法了
             */
            downloadBinder = (MyService.DownloadBinder) service;
            downloadBinder.startDownload();
            downloadBinder.getProgress();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startService =  findViewById(R.id.start_service);
        Button stopService =  findViewById(R.id.stop_service);
        startService.setOnClickListener(this);
        stopService.setOnClickListener(this);

        Button bindService =  findViewById(R.id.bind_service);
        Button unbindService =  findViewById(R.id.unbind_service);
        bindService.setOnClickListener(this);
        unbindService.setOnClickListener(this);
//        Button startIntentService =  findViewById(R.id.start_intent_service);
//        startIntentService.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*
            startService()和stopService()都是定义在Context类当中的，所以我们在活动里面可以直接调用这两个方法。
            除了在活动中控制什么时候停止服务以外，
            服务也可让自己停下来，在MyService中任何位置调用StopSlef()方法就可以让这个服务停止下来。
             */
            case R.id.start_service:
                Intent startIntent = new Intent(this, MyService.class);
                startService(startIntent); // 启动服务
                break;
            case R.id.stop_service:
                Intent stopIntent = new Intent(this, MyService.class);
                stopService(stopIntent); // 停止服务
                break;
            case R.id.bind_service:
                Intent bindIntent = new Intent(this, MyService.class);
                bindService(bindIntent, connection, BIND_AUTO_CREATE); // 绑定服务
                //BIND_AUTO_CREATE标志位表示在活动和服务进行绑定后自动创建服务
                break;
            case R.id.unbind_service:
                unbindService(connection); // 解绑服务
                break;
//            case R.id.start_intent_service:
//                // 打印主线程的id
//                Log.d("MainActivity", "Thread id is " + Thread.currentThread(). getId());
//                Intent intentService = new Intent(this, MyIntentService.class);
//                startService(intentService);
//                break;
            default:
                break;
        }
    }
}
