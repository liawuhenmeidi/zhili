package com.zhili.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.zhili.R;
import com.zhili.manager.UpdateManager;

public class LoginActivity extends ActionBarActivity {

	Button confirmButton;
	TextView usernameTextView;
	TextView passwordTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //RatingBar
        setContentView(R.layout.login);
        checkUpdate();//检查更新
        
        confirmButton = (Button)findViewById(R.id.loginactivity_confirm);
        usernameTextView = (TextView)findViewById(R.id.loginactivity_username);
        passwordTextView = (TextView)findViewById(R.id.loginactivity_password);
        
        
        confirmButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(LoginActivity.this,MainConsoleActivity.class);
				startActivity(i);
			}
		});
    }
    
    //检查更新
    private void checkUpdate(){
    	//检查更新进程
        Thread updateThread = new Thread(){
        	@Override
        	public void run(){
        		Looper.prepare();
        		UpdateManager mUpdateManager = new UpdateManager(LoginActivity.this);
        		mUpdateManager.checkUpdateInfo();
        		Looper.loop();
        	}
        };
        updateThread.start();
    }
    
   
}