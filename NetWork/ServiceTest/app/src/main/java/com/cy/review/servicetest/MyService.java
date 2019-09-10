package com.cy.review.servicetest;

/**
 * 服务需要在清单文件中注册才能生效！
 * 启动停止服务主要借助Intent来实现。
 */

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {
    private static final String TAG = "MyService";
    public MyService() {
    }

    /*
   通常来讲活动只是通知服务去执行，
   但是也可以让活动和服务的关系更紧密一些，例如Binder的使用
   当一个活动和服务绑定了之后，就可以调用该服务里面的Binder提供的方法了。

   同时，任何一个服务在整个应用程序范围内都是通用的，
   即MyService不仅可以和MainActivity绑定，还可以和其他任何一个活动绑定，绑定完成后都可以获取相同的DownloadBinder实例
   */
    private DownloadBinder mBingder=new DownloadBinder();
    class DownloadBinder extends Binder {
        //假设希望在MyService当中提供一个下载的功能
        public void startDownload(){
            Log.d(TAG, "startDownload: ");
        }

        public int getProgress(){
            Log.d(TAG, "getProgress: ");
            return 0;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBingder;
    }

    //服务创建时调用
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
    }

    //服务启动时调用
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);

    }

    //服务销毁时调用
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }
}
