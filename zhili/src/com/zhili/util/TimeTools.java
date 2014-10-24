package com.zhili.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeTools {
	
	private static long startTimeMisllis = 0;
	private static SimpleDateFormat sdf = new SimpleDateFormat();
	
	public static void startClock(){
		startTimeMisllis = getCurrentTimeMillis();
	}
	
	public static int getClockSecend(){
		return (int)(getCurrentTimeMillis()-startTimeMisllis)/1000;
	}
	

	public static Date getTime(){
		return new Date();
	}
	
	public static long getCurrentTimeMillis(){
		return System.currentTimeMillis();
	}
	
	public static SimpleDateFormat getSimpleFormatForFileName(){
		sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		return sdf;
	}
}
