package com.zhili.manager;

import java.util.HashMap;

public class DataManager {
	private static HashMap<String, Object> map = new HashMap<String, Object>();  
    private static DataManager dm = null;
    //public static String baseURL = "http://liaowuhentest.gotoip55.com/";
    public static String baseURL = "";
    public static String updateURL = "http://liaowuhenapp.gotoip1.com/";
    
    //获取baseURL的URL
    public static String getBaseURLRequestURL(){
    	return updateURL + "JSON";
    }
    
    public static String getUpdateAppURL(){
    	return updateURL + "app/zhili.apk";
    }
    
    public static String getUpdateXMLURL(){
    	
    	return updateURL + "app/zhiliMetaData.xml";
    }
    
    public static String getBaseURL() {
		return baseURL;
	}
    
    public static void setBaseURL(String url){
    	baseURL = url;
    }
    
	public static DataManager getInstance(){
    	if (dm == null){
    		dm = new DataManager();
    	}
    	return dm;
    }
    public void put(String key,Object object){  
        map.put(key, object);
    }  
      
    public Object get(String key){  
        return map.get(key);
    } 
}
