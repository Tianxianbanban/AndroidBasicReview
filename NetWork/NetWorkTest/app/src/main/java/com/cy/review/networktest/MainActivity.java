package com.cy.review.networktest;

/**
 * HTTP协议：
 * 网络请求-get请求/post请求
 *
 * 使用HTTP协议访问网络：
 * 使用HttpURLConnection
 * 1.开启子线程 -> 建立连接 -> 发送HTTP请求 -> 读取响应的输入流 -> 处理返回数据 -> 切换回UI线程即主线程展示数据
 * 2.声明网络权限
 */

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    TextView responseText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button sendRequest = (Button) findViewById(R.id.send_request);
        responseText =  findViewById(R.id.response_text);
        sendRequest.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.send_request) {
            sendRequestWithHttpURLConnection();
//            sendRequestWithOkHttp();
        }
    }

    private void sendRequestWithHttpURLConnection() {
        //开启线程发起网络请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                BufferedReader reader=null;
                try {
                    URL url=new URL("https://www.baidu.com");
                    connection= (HttpURLConnection) url.openConnection();

                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);

                    InputStream in=connection.getInputStream();
                    //下面对获取到的输入流进行读取
                    reader=new BufferedReader(new InputStreamReader(in));
                    StringBuilder response=new StringBuilder();
                    String line;
                    while ((line=reader.readLine())!=null){
                        response.append(line);
                    }
                    showResponse(response.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    if (reader!=null){
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection!=null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    private void showResponse(final String response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 在这里进行UI操作，将结果显示到界面上
                responseText.setText(response);
            }
        });
    }

}
