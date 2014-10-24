package com.zhili.manager;

import java.util.HashMap;

public class DataManager {
	private static HashMap<String, Object> map = new HashMap<String, Object>();  
    private static DataManager dm = null;
    
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
