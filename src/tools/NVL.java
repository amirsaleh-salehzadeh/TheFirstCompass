package tools;

import java.lang.reflect.InvocationTargetException;

public class NVL {

	public static int getInt(Object obj,int defaultValue){
		if(obj!=null && !"".equalsIgnoreCase(obj.toString())){
			try{
			defaultValue=Integer.parseInt(obj.toString());
			}catch(Exception ex){
			}
		}
		return defaultValue;
	}
	public static int getInt(Object obj){
		return getInt(obj,0);
	}
	public static double getDbl(Object obj,double defaultValue){
		if(obj!=null && !"".equalsIgnoreCase(obj.toString())){
			try{
			defaultValue=Double.parseDouble(obj.toString());
			}catch(Exception ex){
			}
		}
		return defaultValue;
	}
	public static double getDbl(Object obj){
		return getDbl(obj,0.0);
	}
	public static int getIntUtf8(Object obj,int defaultValue){
		if(obj!=null && !"".equalsIgnoreCase(obj.toString())){
			defaultValue=Integer.parseInt(UTF8.cnvUTF8(obj.toString()));
		}
		return defaultValue;
	}
	public static int getIntUtf8(Object obj){
		return getIntUtf8(obj,0);
	}
	public static double getDblUtf8(Object obj,double defaultValue){
		if(obj!=null && !"".equalsIgnoreCase(obj.toString())){
			defaultValue=Double.parseDouble(UTF8.cnvUTF8(obj.toString()));
		}
		return defaultValue;
	}
	public static double getDblUtf8(Object obj){
		return getDblUtf8(obj,0);
	}
	public static String getString(Object obj,String defaultValue){
		if(obj!=null){
			defaultValue=obj.toString();
		}
		return defaultValue;
	}
	public static String getString(Object obj){
		return getString(obj,"");
	}
	public static String getStringNull(String obj,String nullValue){
		if(obj!=null){
			if(obj.toString().trim().equalsIgnoreCase(nullValue.trim())){
				obj=null;
			}
		}
		return obj;
	}
	public static String getStringNull(Object obj){
		if(obj==null){
			return (String)obj;
		}else{
			return getStringNull(obj.toString(),"");
		}
	}

	public static String getEmptyString(Object obj,String defaultValue) {
		if(obj!=null && !"".equals(obj.toString())) {
			defaultValue=obj.toString();
		}
		return defaultValue;
	}
	public static Integer getInteger(Object obj,int nullValue){
		if(obj!=null && !"".equalsIgnoreCase(obj.toString())){
			int cur=0;
			try{
				cur=Integer.parseInt(obj.toString());
				obj=new Integer(cur);
			}catch(Exception ex){
				obj=null;
				System.out.println("NVL.getInteger():"+ex.getMessage());
			}
			if(cur==nullValue){
				obj=null;
			}
		}
		return (Integer)obj;
	}
	public static Integer getInteger(Object obj){
		return getInteger(obj,0);
	}
	public static Double getDouble(Object obj,double nullValue){
		if(obj!=null && !"".equalsIgnoreCase(obj.toString())){
			double cur=0;
			try{
				cur=Double.parseDouble(obj.toString());
				obj=new Double(cur);
			}catch(Exception ex){
				obj=null;
				System.out.println("NVL.getInteger():"+ex.getMessage());
			}
			if(cur==nullValue){
				obj=null;
			}
		}
		return (Double)obj;
	}
	public static Double getDouble(Object obj){
		return getDouble(obj,0);
	}





	// Shahabi
	public static Boolean getBln(Object obj) {
		Boolean res = false;
		if(obj!=null && !"".equalsIgnoreCase(obj.toString())){
 			try {
 				if ( "true".equalsIgnoreCase(obj.toString()) || "1".equalsIgnoreCase(obj.toString()) || "yes".equalsIgnoreCase(obj.toString()) ) {
 					res = true; 
 				} else if ("false".equalsIgnoreCase(obj.toString()) || "0".equalsIgnoreCase(obj.toString()) || "no".equalsIgnoreCase(obj.toString()) ) {
 					res = false; 
 				}
			} catch(Exception ex) {
				obj=null;
				System.out.println("getDbl():"+ex.getMessage());
			}
		}
		return res;
	}
	
	/**
	 * Boolean
	 */
	public static boolean getBool(Object obj,boolean defaultValue){
		if(obj!=null && !"".equalsIgnoreCase(obj.toString())){
			try {
				defaultValue = Boolean.parseBoolean(obj.toString());
				if ("on".equals(obj.toString())) {
					defaultValue = true;	
				}
			} catch (Exception ex) {
			}
		}
		return defaultValue;
	}
	public static boolean getBool(Object obj){
		return getBool(obj,false);
	}
	public static Boolean getBoolean(Object obj,boolean nullValue){
		if(obj!=null && !"".equalsIgnoreCase(obj.toString())){
			boolean cur=false;
			try{
				cur=Boolean.parseBoolean(obj.toString());
				obj=new Boolean(cur);
			}catch(Exception ex){
				obj=null;
				System.out.println("NVL.getInteger():"+ex.getMessage());
			}
			if(cur==nullValue){
				obj=null;
			}
		}
		return (Boolean)obj;
	}
	public static Boolean getBoolean(Object obj){
		return getBoolean(obj,false);
	}

	/**
	 * Long
	 */
	public static long getLng(Object obj,long defaultValue){
		if(obj!=null && !"".equalsIgnoreCase(obj.toString())){
			try{
			defaultValue=Long.parseLong(obj.toString());
			}catch(Exception ex){
			}
		}
		return defaultValue;
	}
	public static double getLng(Object obj){
		return getLng(obj,0);
	}
	public static Long getLong(Object obj,long nullValue){
		if(obj!=null) {// && !"".equalsIgnoreCase(obj.toString())){
			long cur=0;
			try{
				cur=Long.parseLong(obj.toString());
				obj=new Long(cur);
			}catch(Exception ex){
				obj=null;
				System.out.println("NVL.getDouble():"+ex.getMessage());
			}
			if(cur==nullValue){
				obj=null;
			}
		}
		return (Long)obj;
	}
	public static Long getLong(Object obj){
		return getLong(obj,0);
	}



}
