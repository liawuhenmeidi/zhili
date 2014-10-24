package com.zhili.manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.zhili.util.TimeTools;

import android.os.Looper;

public class NetDataManager {
	
	static String baseURL = "http://203.195.223.16:8080/meidi";
	
	private static NetDataManager ndm ; 
    
	public static NetDataManager getStaticInstance(){
		if(ndm == null){
			ndm = new NetDataManager();
		}
		return ndm;
	}
	
	public static String getBaseURL(){
		return baseURL;
	}
	
	//发送登录请求
	public boolean sendLoginRequest(String username,String password){
		
		boolean flag = false;
		LoginThread workThread = new LoginThread(username,password);
		workThread.start();
		

		TimeTools.startClock();
		while(!workThread.isComplete()){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(TimeTools.getClockSecend() > workThread.getTimeout()){
				break;
			}
			
		}
		
		flag = workThread.isSuccess();
		return flag;
		
	}
	
	private class LoginThread extends Thread{
		String username = "";
		String password = "";
		boolean success = false;
		private int timeout = 10;
		boolean complete = false;
		

		public boolean isSuccess() {
			return success;
		}
		
		public boolean isComplete(){
			return complete;
		}
		
		public int getTimeout(){
			return timeout;
		}

		public LoginThread(String username, String password) {
			super();
			this.username = username;
			this.password = password;
		}

		@Override
		public void run() {
			Looper.prepare();
			// 响应
		    HttpResponse mHttpResponse = null;
		    // 实体
		    HttpEntity mHttpEntity = null;
		    
			
	        
	        // 生成一个Http客户端对象
	        HttpClient httpClient = new DefaultHttpClient();
	        
	        //添加对应的参数
//	        HttpParams params = httpClient.getParams();
//			params.setParameter("username", username);
//			httpGet.setParams(params);
//			params.setParameter("password", password);
			
	        // 生成一个请求对象
	        HttpGet httpGet = new HttpGet(baseURL+"/servlet/LoginServlet" + "?username=" + username +"&password="+password);

	        // 下面使用Http客户端发送请求，并获取响应内容

	        InputStream inputStream = null;
	        try
	        {
	            // 发送请求并获得响应对象
	            mHttpResponse = httpClient.execute(httpGet);
	            // 获得响应的消息实体
	            mHttpEntity = mHttpResponse.getEntity();

	            // 获取一个输入流
	            inputStream = mHttpEntity.getContent();

	            BufferedReader bufferedReader = new BufferedReader(
	                    new InputStreamReader(inputStream));

	            String result = "";
	            String line = "";

	            while (null != (line = bufferedReader.readLine()))
	            {
	                result += line;
	            }

	            
	            System.out.println("login => username = " + username + ",password = "+ password);
	            if(result.startsWith("success")){
	            	success = true;
	            	// 将结果打印出来，可以在LogCat查看
		            System.out.println(result);
		            
	            }else if(result.startsWith("failed")){
	            	success = false;
	            	// 将结果打印出来，可以在LogCat查看
		            System.out.println(result);
		            
	            }else{
	            	// 将结果打印出来，可以在LogCat查看
		            System.out.println(result);
		            
	            }
	            complete = true;
	           
	        }
	        catch (Exception e)
	        {
	            e.printStackTrace();
	            success = false;
	        }
	        finally
	        {
	            try
	            {
	                inputStream.close();
	            }
	            catch (IOException e)
	            {
	                e.printStackTrace();
	            }
	            complete = true;
	        }
	        Looper.loop();
		}
		
		
	}
	
	
}
