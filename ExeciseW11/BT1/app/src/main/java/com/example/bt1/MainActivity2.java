package com.example.bt1;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.annotation.SuppressLint;

import com.example.bt1.databinding.ActivityMain2Binding;

public class MainActivity2 extends AppCompatActivity {

    private ActivityMain2Binding binding; // Sử dụng ViewBinding

    @SuppressLint({"SetJavaScriptEnabled", "WebViewApiAvailability"}) // Annotation từ slide
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide(); // Ẩn ActionBar nếu có
        }

        // Cấu hình WebView - Dùng binding.webview thay vì binding.webview2 nếu ID trong XML là webview
        WebSettings webSettings = binding.webview.getSettings(); // Sử dụng ID từ XML

        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        // Dòng CacheMode bị thiếu trong OCR, ví dụ:
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        binding.webview.setWebViewClient(new WebViewClient()); // Quan trọng để link mở trong app
        binding.webview.setWebChromeClient(new WebChromeClient()); // Để xử lý các sự kiện UI của trình duyệt

        // Load URL
        binding.webview.loadUrl("http://iotstar.vn");
    }
}