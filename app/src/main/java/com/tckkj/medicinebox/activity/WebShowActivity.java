package com.tckkj.medicinebox.activity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tckkj.medicinebox.R;
import com.tckkj.medicinebox.base.BaseActivity;

import static android.view.KeyEvent.KEYCODE_BACK;

/*
* 网页展示界面
* */
public class WebShowActivity extends BaseActivity {
    private WebView web_show;
    private String url = "";

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_web_show);

//        top.setVisibility(View.GONE);
        title.setText(getString(R.string.user_login_protocol));

        web_show = findViewById(R.id.web_show);
    }

    @Override
    protected void initListener() {
        back.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        if (null != getIntent()){
            url = getIntent().getStringExtra("url");
        }

        webSetting();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
        }
    }

    private void webSetting(){
        web_show.loadUrl(url);

        WebSettings webSettings = web_show.getSettings();



// User settings

        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setJavaScriptEnabled(true);// 设置支持javascript脚本
        webSettings.setUseWideViewPort(true);//关键点

        webSettings.setDomStorageEnabled(true);//开启DOM

        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        webSettings.setDisplayZoomControls(false);
        webSettings.setAllowFileAccess(true); // 允许访问文件
        webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
        webSettings.setSupportZoom(true); // 支持缩放



        webSettings.setLoadWithOverviewMode(true);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int mDensity = metrics.densityDpi;
        Log.d("maomao", "densityDpi = " + mDensity);
        if (mDensity == 240) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == 160) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        } else if(mDensity == 120) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        }else if(mDensity == DisplayMetrics.DENSITY_XHIGH){
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        }else if (mDensity == DisplayMetrics.DENSITY_TV){
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        }else{
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        }


/**
 * 用WebView显示图片，可使用这个参数 设置网页布局类型： 1、LayoutAlgorithm.NARROW_COLUMNS ：
 * 适应内容大小 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
 */
//        webSettings.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);

        web_show.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KEYCODE_BACK) && web_show.canGoBack()) {
            web_show.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
