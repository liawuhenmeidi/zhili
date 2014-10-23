package com.zhili.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhili.R;
import com.zhili.manager.NetDataManager;
import com.zhili.manager.UpdateManager;

public class LoginActivity extends ActionBarActivity {

	Button confirmButton;
	EditText usernameTextView;
	EditText passwordTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //RatingBar
        setContentView(R.layout.login);
        checkUpdate();//检查更新
        
        confirmButton = (Button)findViewById(R.id.loginactivity_confirm);
        usernameTextView = (EditText)findViewById(R.id.loginactivity_username);
        passwordTextView = (EditText)findViewById(R.id.loginactivity_password);
        
        
        confirmButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean login = NetDataManager.getStaticInstance().sendLoginRequest(usernameTextView.getText().toString(), passwordTextView.getText().toString());
				if(login){
					Intent i = new Intent(LoginActivity.this,MainConsoleActivity.class);
					startActivity(i);
				}else{
					Toast.makeText(getApplicationContext(), "帐号密码有误，请重新输入",Toast.LENGTH_SHORT).show();
					//usernameTextView.setText("");
					passwordTextView.setText("");
				}
				
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