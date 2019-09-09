package com.cy.review.playaudiotest;

/**
 * 播放音视频文件
 *
 * 一般使用MediaPlayer类来实现：
 * 1.创建MediaPlayer对象 -> setDataSource()设置音视频文件的路径 -> prepare()进入准备状态 -> start()准备 pause()暂停 reset()停止 -> 活动销毁时的资源释放
 * 2.权限申请Manifest.permission.WRITE_EXTERNAL_STORAGE和权限声明。
 * 但是事先需要在SD卡根目录（也就是手机插上电脑以后对应盘符的目录下）准备好对应的音频文件。
 *
 * MediaPlayer类中还有一些常用的控制方法.
 *
 */

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private MediaPlayer mediaPlayer = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button play =  findViewById(R.id.play);
        Button pause =  findViewById(R.id.pause);
        Button stop =  findViewById(R.id.stop);
        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        stop.setOnClickListener(this);
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{ Manifest.permission. WRITE_EXTERNAL_STORAGE }, 1);
        } else {
            initMediaPlayer(); // 初始化MediaPlayer
        }
    }

    private void initMediaPlayer() {
        try {
            File file = new File(Environment.getExternalStorageDirectory(), "music.mp3");
            mediaPlayer.setDataSource(file.getPath()); // 指定音频文件的路径
            mediaPlayer.prepare(); // 让MediaPlayer进入到准备状态
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initMediaPlayer();
                } else {
                    Toast.makeText(this, "拒绝权限将无法使用程序", Toast.LENGTH_SHORT).show();
//                    finish();
                }
                break;
            default:
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play:
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start(); // 开始播放
                }
                break;
            case R.id.pause:
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause(); // 暂停播放
                }
                break;
            case R.id.stop:
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.reset(); // 停止播放
                    initMediaPlayer();
                }
                break;
            default:
                break;
                /*
                mediaPlayer.setDataSource();
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    mediaPlayer.reset();
                    mediaPlayer.seekTo();//从指定位置开始播放音频
                    mediaPlayer.stop();//停止播放音频，调用这个方法后对象无法再播放音频
                    mediaPlayer.release();//释放掉与mediaPlayer对象相关的资源
                    mediaPlayer.isPlaying();//判断当前是否正在播放音频
                    mediaPlayer.getDuration();//获取载入的音频文件的时长
                 */
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

}