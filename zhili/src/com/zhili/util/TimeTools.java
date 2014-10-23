package com.zhili.util;

import java.util.Date;

public class TimeTools {
	
	private static long startTimeMisllis = 0;
	
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
}
