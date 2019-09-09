package com.cy.review.webviewtest;

/**
 * 可以借助WebView控件帮助我们在应用当中嵌入一个浏览器，展示各种网页。
 * WebView对许多功能已经进行了非常完善的封装，已经在后台处理好了发送HTTP请求、接收服务响应、解析返回数据以及最终页面展示的工作。
 */

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView webView =  findViewById(R.id.web_view);
        //getSettings()设置一些浏览器的属性
        //setJavaScriptEnabled()方法让WebView支持JavaScript脚本
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());//使的当从一个网页跳到另一个网页的时候目标网页仍然在当前WebView当中显示，而不是打开系统浏览器。
        webView.loadUrl("https://github.com/Tianxianbanban");//传入网址，展示相应网页内容
    }
}
