package com.zhili.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhili.R;
import com.zhili.manager.UpdateManager;

public class MainConsoleActivity extends ActionBarActivity {
	
	TextView viewOrder;
	TextView createOrder;
	TextView signin;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //RatingBar
        setContentView(R.layout.mainconsole);
        
        viewOrder = (TextView)findViewById(R.id.mainconsoleactivity_vieworder);
        createOrder = (TextView)findViewById(R.id.mainconsoleactivity_createorder);
        signin = (TextView)findViewById(R.id.mainconsoleactivity_signin);
        
        viewOrder.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainConsoleActivity.this,ViewOrderActivity.class);
				startActivity(i);
			}
		});
        
        createOrder.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainConsoleActivity.this,CreateOrderActivity.class);
				startActivity(i);
			}
		});
        
        signin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainConsoleActivity.this,SigninActivity.class);
				startActivity(i);
			}
		});
    }
   
}