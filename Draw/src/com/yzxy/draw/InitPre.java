package com.yzxy.draw;




import android.content.Context;
import android.content.SharedPreferences;



/**
 * 初始化第一次进来的数据
 * 
 * @author nxp71465
 * 
 */
public class InitPre {
	
    private static final String BlueMAC = "30:14:09:30:20:70";
    private static final String SHAREDPREFERENCES_INIT = "init_first";
	
	public static String getBlueMAC(Context context){
		SharedPreferences preferences = context.getSharedPreferences(SHAREDPREFERENCES_INIT, Context.MODE_PRIVATE);
		return preferences.getString(BlueMAC, "30:14:09:30:20:70");
	}
	
	public static void setBlueMAC(Context context,String pass){
		SharedPreferences preferences = context.getSharedPreferences(SHAREDPREFERENCES_INIT, Context.MODE_PRIVATE);
			preferences.edit().remove(BlueMAC).commit();
		
	}
	
	
	
}
