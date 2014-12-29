package com.zhili.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zhili.R;
import com.zhili.manager.DataManager;

public class OrderSystemActivity extends Activity implements OnClickListener{
	

	
	private WebView web;
	
	private Button btn_home,btn_zoomin,btn_zoomout,btn_exit;
	
	private boolean isExit;
	
	DataManager dataManager = DataManager.getInstance();

	private String homeStr = dataManager.getBaseURL() + "/meidiserver/dengluN.jsp" + dataManager.get("urlParam") + "&action=login";
	
	//-------------------------------------------
	/**
	 * 基本数据初始化
	 */
	private void init(){
		
		isExit = false;

		
		web = (WebView)findViewById(R.id.web);
		WebSettings ws = web.getSettings();
		//是否允许脚本支持
		ws.setJavaScriptEnabled(true);
		ws.setJavaScriptCanOpenWindowsAutomatically(true);
		ws.setSaveFormData(false);
		ws.setAppCacheEnabled(true);
		ws.setAppCacheMaxSize(10240);

//			ws.setCacheMode(WebSettings.LOAD_NO_CACHE);
		//是否允许缩放
		ws.setSupportZoom(true);
		ws.setBuiltInZoomControls(true);
		
		web.setWebViewClient(wvc);
		web.setWebChromeClient(wcc);
		
		btn_home = (Button)findViewById(R.id.btn_home);
		btn_zoomin = (Button)findViewById(R.id.btn_zoomin);
		btn_zoomout = (Button)findViewById(R.id.btn_zoomout);
		btn_exit = (Button)findViewById(R.id.btn_exit);
		
		btn_home.setOnClickListener(this);
		btn_zoomin.setOnClickListener(this);
		btn_zoomout.setOnClickListener(this);
		btn_exit.setOnClickListener(this);
		
		web.setOnTouchListener(touchListener);
		
		conn(homeStr);
		
	}
	
	//-------------------------------------------
	/**
	 * 触摸监听
	 */
	OnTouchListener touchListener = new OnTouchListener() {
		
		public boolean onTouch(View v, MotionEvent event) {
			switch(v.getId()){
			case R.id.web:
				web.requestFocus();
				break;
			}
			return false;
		}
	};
	//-------------------------------------------
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ordersystem);
        init();
    }
    
    
    //-------------------------------------------
    
    WebViewClient wvc = new WebViewClient(){
    	
    	public boolean shouldOverrideUrlLoading(WebView view, String url) {
    		
    		view.loadUrl(url);
    		
    		return true;
    	};
    };
    
    
    //-------------------------------------------
    
    WebChromeClient wcc = new WebChromeClient(){

		public void onRequestFocus(WebView view) {
			super.onRequestFocus(view);
			view.requestFocus();
			
		}
    	
    	
    };
    
    
    
    //-------------------------------------------
    /**
     * 访问url
     * @param urlStr
     */
    private void conn(String urlStr){
    	String url = "";
    	if(urlStr.contains("http://")){
    		url = urlStr;
    	}else{
    		url = "http://"+urlStr;
    	}
    	web.loadUrl(url);
    }
    
    //-------------------------------------------
    /**
     * 缩
     */
    private void zoomIn(){
//    	if(web.canGoBack()){
//    		web.goBack();
//    	}else{
//    		Toast.makeText(this, "已经是第一页",Toast.LENGTH_SHORT).show();
//    	}
    	web.zoomIn();
    }
    
    //-------------------------------------------
    /**
     * 放
     */
    private void zoomOut(){
//    	if(web.canGoForward()){
//    		web.goForward();
//    	}else{
//    		Toast.makeText(this, "已经是最后一页",Toast.LENGTH_SHORT).show();
//    	}
    	web.zoomOut();
    }
    //-------------------------------------------
    /**
     * 退出
     */
    private void exit(){
    	
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	
    	builder.setTitle("提示")
    	.setMessage("确定退出？")
    	.setPositiveButton("确定", dialogListener)
    	.setNegativeButton("取消", dialogListener)
    	.create()
    	.show();
    	
    }
    
    DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
		
		public void onClick(DialogInterface dialog, int which) {
			
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				finish();
				break;

			default:
				dialog.cancel();
				break;
			}
		}
	};
    
    //-------------------------------------------
    
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	
    	if(keyCode==KeyEvent.KEYCODE_BACK||web.canGoBack()){
    		web.goBack();
    		if(!web.canGoBack()){
    			if(isExit){
    				return super.onKeyDown(keyCode, event);
    			}
    			isExit = true;
    			Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
    		}else{
    			isExit = false;
    		}
    	}
    	return true;
    	
    }
    
    
	//-------------------------------------------
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_home:
			
			//conn("www.taobao.com");
			conn(homeStr);
			break;
		case R.id.btn_zoomin:
			zoomIn();
			break;
		case R.id.btn_zoomout:
			zoomOut();
			break;
		case R.id.btn_exit:
			exit();
			break;

		}
	}

}
