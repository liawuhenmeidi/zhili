package com.zhili.manager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import com.zhili.R;
import com.zhili.util.ParseXmlService;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

public class UpdateManager {
	 // 应用程序Context
	 private Context mContext;
	 // 提示消息
	 private String updateMsg = "有最新的软件包，请下载！";
	 // 下载安装包的网络路径
	 //private String apkUrl = "http://softfile.3g.qq.com:8080/msoft/179/24659/43549/qq_hd_mini_1.4.apk";

	 private HashMap<String, String> mHashMap; /* 保存读取的网络信息 */
	 private Dialog noticeDialog;// 提示有软件更新的对话框
	 private Dialog downloadDialog;// 下载对话框
	 private static String savePath = Environment.getExternalStorageDirectory().getPath() + "/zhili/update";// 保存apk的文件夹
	 private static String saveFileName = savePath + "/" + "zhili.apk";
	 // 进度条与通知UI刷新的handler和msg常量 
	 private ProgressBar mProgress;
	 private static final int DOWN_UPDATE = 1;
	 private static final int DOWN_OVER = 2;
	 private int progress;// 当前进度
	 private Thread downLoadThread; // 下载线程
	 private boolean interceptFlag = false;// 用户取消下载
	 
	 // 通知处理刷新界面的handler
	 private Handler mHandler = new Handler() {
		  @SuppressLint("HandlerLeak")
		  @Override
		  public void handleMessage(Message msg) {
		   switch (msg.what) {
		   case DOWN_UPDATE:
		    mProgress.setProgress(progress);
		    break;
		   case DOWN_OVER:
		    installApk();
		    break;
		   }
		   super.handleMessage(msg);
		  }
	 };
	 
	 
	 
	 public UpdateManager(Context context) {
		 this.mContext = context;
	 }
	 
	 
	// 显示更新程序对话框，供主程序调用
	 public void checkUpdateInfo() {
		 if(isUpdate()){
			 showNoticeDialog();
		 }
		 
	 }
	 
	 /**
     * 检查是否需要更新
     * 
     * @return
     */
    private boolean isUpdate()
    {
    	boolean flag = false;
    	// 显示更新程序对话框，供主程序调用
        int versionCode = getVersionCode(mContext);
        // 把version.xml放到网络上，然后获取文件信息
        //InputStream inStream = ParseXmlService.class.getClassLoader().getResourceAsStream("http://203.195.223.16:8080/zhili/zhiliMetaData.xml");
        try {
			URL url = new URL("http://203.195.223.16:8080/zhili/zhiliMetaData.xml");
			InputStream is = url.openStream();  
			// 解析XML文件。 由于XML文件比较小，因此使用DOM方式进行解析
	        ParseXmlService service = new ParseXmlService();
	        mHashMap = service.parseXml(is);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
       

        if (null != mHashMap)
        {
            int serviceCode = Integer.valueOf(mHashMap.get("versionCode"));
         // 版本判断
            if (serviceCode > versionCode)
            {
            	flag=true;
            }
        }
        return flag;
    }
    
    /**
     * 获取软件版本号
     * 
     * @param context
     * @return
     */
    private int getVersionCode(Context context)
    {
        int versionCode = 0;
        try
        {
        	// 获取软件版本号，
            versionCode = context.getPackageManager().getPackageInfo("com.zhili", 0).versionCode;
        } catch (NameNotFoundException e)
        {
            e.printStackTrace();
        }
        return versionCode;
    }
	 
	 
	 private void showNoticeDialog() {
		  android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(
		    mContext);// Builder，可以通过此builder设置改变AleartDialog的默认的主题样式及属性相关信息
		  builder.setTitle("软件版本更新");
		  builder.setMessage(updateMsg);
		  
		  
		  builder.setPositiveButton("下载", new OnClickListener() {
		   @Override
		   public void onClick(DialogInterface dialog, int which) {
			   dialog.dismiss();// 当取消对话框后进行操作一定的代码？取消对话框
			   showDownloadDialog();
		   }
		  });
		  
		  builder.setNegativeButton("以后再说", new OnClickListener() {
		   @Override
		   public void onClick(DialogInterface dialog, int which) {
		    dialog.dismiss();
		   }
		  });
		  
		  noticeDialog = builder.create();
		  noticeDialog.show();
	 }
	 
	 
	 
	 protected void showDownloadDialog() {
		  android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext);
		  builder.setTitle("软件版本更新");
		  final LayoutInflater inflater = LayoutInflater.from(mContext);
		  View v = inflater.inflate(R.layout.update_progressbar, null);
		  mProgress = (ProgressBar) v.findViewById(R.id.progressbar);
		  builder.setView(v);// 设置对话框的内容为一个View
		  builder.setNegativeButton("取消", new OnClickListener() {
		   @Override
		   public void onClick(DialogInterface dialog, int which) {
		    dialog.dismiss();
		    interceptFlag = true;
		   }
		  });
		  downloadDialog = builder.create();
		  downloadDialog.show();
		  downloadApk();
	 }
	 private void downloadApk() {
		  downLoadThread = new Thread(mdownApkRunnable);
		  downLoadThread.start();
	 }
	 
	 protected void installApk() {
		  File apkfile = new File(saveFileName);
		  if (!apkfile.exists()) {
		   return;
		  }
		  Intent i = new Intent(Intent.ACTION_VIEW);
		  i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
		    "application/vnd.android.package-archive");// File.toString()会返回路径信息
		  mContext.startActivity(i);
	 }
	 
	 
	 private Runnable mdownApkRunnable = new Runnable() {
		  @Override
		  public void run() {
			  URL url;
			  try {
				    url = new URL(mHashMap.get("url"));
				    saveFileName = savePath + "/" + mHashMap.get("name");
				    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				    conn.connect();
				    int length = conn.getContentLength();
				    InputStream ins = conn.getInputStream();
				    File file = new File(savePath);
				    if (!file.exists()) {
				     file.mkdirs();
				    }
				    String apkFile = saveFileName;
				    File ApkFile = new File(apkFile);
				    FileOutputStream outStream = new FileOutputStream(ApkFile);
				    int count = 0;
				    byte buf[] = new byte[1024];
				    do {
					     int numread = ins.read(buf);
					     count += numread;
					     progress = (int) (((float) count / length) * 100);
					     // 下载进度
					     mHandler.sendEmptyMessage(DOWN_UPDATE);
					     if (numread <= 0) {
					    	 // 下载完成通知安装
						      mHandler.sendEmptyMessage(DOWN_OVER);
						      break;
					     }
					     outStream.write(buf, 0, numread);
				    } while (!interceptFlag);// 点击取消停止下载
				    
				    outStream.close();
				    ins.close();
			   } catch (Exception e) {
				   e.printStackTrace();
			   }
		  }
	 };
	 
}