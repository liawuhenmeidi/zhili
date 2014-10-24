package com.zhili.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.zhili.R;
import com.zhili.util.TimeTools;

public class SigninActivity extends ActionBarActivity {
	MapView mMapView = null;  
	private LocationClient mLocationClient = null;
	private BDLocationListener myListener = new MyLocationListener();
	private LocationMode mCurrentMode = LocationMode.NORMAL;
	
	private String photoSavePath = "/zhili/SigninImage/";
	TextView address = null;
	TextView photoButton = null;
	ImageView photoResult = null;
	
	TextView comment = null;
	Button commit = null;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);   
		  
		  /**
		   * 地图相关
		   */
	      //在使用SDK各组件之前初始化context信息，传入ApplicationContext  
	      //注意该方法要再setContentView方法之前实现  
	      SDKInitializer.initialize(getApplicationContext());  
	      setContentView(R.layout.signin);  
	      mMapView = (MapView) findViewById(R.id.signinactivity_map);    
	      mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
	      mLocationClient.registerLocationListener( myListener );    //注册监听函数
	      initMapOption();
	      mLocationClient.start();
	      address = (TextView)findViewById(R.id.signinactivity_address);
	      
	      
	      
	      /**
	       * 照片相关
	       */
	      photoButton = (TextView)findViewById(R.id.signinactivity_photobutton);
	      photoResult = (ImageView)findViewById(R.id.signinactivity_photoresult);
	      photoButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  
				  
                startActivityForResult(intent, 1);  
			}
		});
	      
	      /**
	       * 填写摘要
	       */
	      comment = (TextView)findViewById(R.id.signinactivity_comment);
          
	      
	      /**
	       * 提交
	       */
	      commit = (Button)findViewById(R.id.signinactivity_commit);
	      commit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
    }
    
    @Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        // TODO Auto-generated method stub  
        super.onActivityResult(requestCode, resultCode, data);  
        if (resultCode == Activity.RESULT_OK) {
            String sdStatus = Environment.getExternalStorageState();  
            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用  
                Log.i("TestFile",  
                        "SD card is not avaiable/writeable right now.");  
                return;  
            }
            String name =TimeTools.getSimpleFormatForFileName().format(new Date()) + ".jpg";     
            Toast.makeText(this, name, Toast.LENGTH_LONG).show();  
            Bundle bundle = data.getExtras();  
            Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式  
          
            FileOutputStream b = null;
           //???????????????????????????????为什么不能直接保存在系统相册位置呢？？？？？？？？？？？？  
            
            File file = new File(Environment.getExternalStorageDirectory().getPath() + photoSavePath);  
            file.mkdirs();// 创建文件夹  
            String fileName = Environment.getExternalStorageDirectory().getPath() + photoSavePath + name;  
  
            try {  
                b = new FileOutputStream(fileName);  
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件  
            } catch (FileNotFoundException e) {  
                e.printStackTrace();  
            } finally {  
                try {  
                    b.flush();  
                    b.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
            photoResult.setImageBitmap(bitmap);// 将图片显示在ImageView里  
        }  
    }  
    
    public void initMapOption(){
    	LocationClientOption option = new LocationClientOption();
        //option.setLocationMode(LocationMode.Hight_Accuracy);//设置定位模式
        option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);//返回的定位结果包含地址信息
        option.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向
        mLocationClient.setLocOption(option);
    }
    
    class MyLocationListener implements BDLocationListener {
    	@Override
    	public void onReceiveLocation(BDLocation location) {
    		if (location == null)
    	            return ;
    		StringBuffer sb = new StringBuffer(256);
    		sb.append("time : ");
    		sb.append(location.getTime());
    		sb.append("\nerror code : ");
    		sb.append(location.getLocType());
    		sb.append("\nlatitude : ");
    		sb.append(location.getLatitude());
    		sb.append("\nlontitude : ");
    		sb.append(location.getLongitude());
    		sb.append("\nradius : ");
    		sb.append(location.getRadius());
    		if (location.getLocType() == BDLocation.TypeGpsLocation){
    			sb.append("\nspeed : ");
    			sb.append(location.getSpeed());
    			sb.append("\nsatellite : ");
    			sb.append(location.getSatelliteNumber());
    		} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
    			sb.append("\naddr : ");
    			sb.append(location.getAddrStr());
    		} 
    		//System.out.println("地址是" + location.getAddrStr());
    		
    		//设置地址信息
    		address.setText(address.getText().toString() + location.getAddrStr());
    		//打印接到的消息
    		System.out.println(sb.toString());
    		
    		//关闭service
    		mLocationClient.stop();
    		
    		BaiduMap mBaiduMap = mMapView.getMap();
    		// 开启定位图层  
    		mBaiduMap.setMyLocationEnabled(true);  
    		// 构造定位数据  
    		MyLocationData locData = new MyLocationData.Builder()  
    		    .accuracy(location.getRadius())  
    		    // 此处设置开发者获取到的方向信息，顺时针0-360  
    		    .direction(100).latitude(location.getLatitude())  
    		    .longitude(location.getLongitude()).build();  
    		// 设置定位数据  
    		mBaiduMap.setMyLocationData(locData);  
    		// 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）  
    		BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory  
    		   .fromResource(R.drawable.arrow);  
    		
    		MyLocationConfiguration config = new MyLocationConfiguration(mCurrentMode, true, mCurrentMarker);  
    		mBaiduMap.setMyLocationConfigeration(config);
    		// 当不需要定位图层时关闭定位图层  
    		//mBaiduMap.setMyLocationEnabled(false);
    		
    		//移动到指定位置
             LatLng pos = new LatLng(location.getLatitude(),
                     location.getLongitude());
             
             MapStatus mMapStatus = new MapStatus.Builder().target(pos)
            		 .zoom(17)
            		 .build();
             MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(mMapStatus);;
             //mBaiduMap.animateMapStatus(u);
             mBaiduMap.setMapStatus(u);
  
             
    	}
    }
   
}


