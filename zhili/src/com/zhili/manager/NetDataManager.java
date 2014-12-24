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
import org.json.JSONArray;
import org.json.JSONObject;

import com.zhili.util.TimeTools;

import android.os.Looper;

public class NetDataManager {
	
	private static NetDataManager ndm ; 
    
	public static NetDataManager getStaticInstance(){
		if(ndm == null){
			ndm = new NetDataManager();
		}
		return ndm;
	}
	
	//发送baseURL请求
	public boolean sendBaseURLRequest(String code){
		boolean flag = false;
		BaseURLThread bur = new BaseURLThread(code);
		bur.start();

		TimeTools.startClock();
		while(!bur.isComplete()){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(TimeTools.getClockSecend() > bur.getTimeout()){
				break;
			}
			
		}	
		flag = bur.isSuccess();
		return flag;
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
	        HttpGet httpGet = new HttpGet(DataManager.getBaseURL()+"/servlet/LoginServlet" + "?username=" + username +"&password="+password);

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
	            	System.out.println("登录成功,登录的地址为" + DataManager.getBaseURL());
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
	
	private class BaseURLThread extends Thread{
		String code = "";

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

		public BaseURLThread(String code) {
			super();
			this.code = code;
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
	        // 生成一个请求对象
	        HttpGet httpGet = new HttpGet(DataManager.getBaseURLRequestURL()+"?coding="+code);

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
	            
	            
	            System.out.println("baseURLRequest => code = " + code);
	            if(result.contains("coding") && result.contains("url")){
	            	JSONObject  resultJSON;
		            resultJSON = new JSONObject(result);  
					System.out.println();
					System.out.println();
					if(resultJSON.getString("coding").equals(code)){
						DataManager.setBaseURL(resultJSON.getString("url"));
						success = true;
					}else{
						success = false;
						System.out.println("小心，可能是破解");
						//
					}
	            	
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
