package com.zhili.activity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.webkit.WebView;

import com.zhili.manager.NetDataManager;

public class ViewOrderActivity extends ActionBarActivity {

	WebView webview;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.createorder);
        //webview = (WebView)findViewById(R.id.createorderactivity_webview);
        
        //start();
        webview = new WebView(this); 
        webview.getSettings().setJavaScriptEnabled(true); 
        webview.loadUrl(NetDataManager.getBaseURL() + "/meidiserver/user/serch_list.jsp");
        setContentView(webview);
    }

    
   
}