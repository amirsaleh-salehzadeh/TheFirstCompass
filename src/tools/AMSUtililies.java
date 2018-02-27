package tools;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

public class AMSUtililies {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// //example
		// List<StructureNodeDTO> nodes = qry.list();
		// nodes = AIPUtil.exchangeList2Tree(nodes, "NodeId", "ParentId",
		// "Children");
	}

	public static Object invoke(Object obj, String field)
			throws IllegalArgumentException, SecurityException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		String field2invoke = field.substring(0, 1).toUpperCase()
				+ field.substring(1);
		Object v = obj.getClass().getMethod("get" + field2invoke, null)
				.invoke(obj, null);
		return v;
	}

	public static Object invokeMethod(Object obj, String method)
			throws IllegalArgumentException, SecurityException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		Object v = obj.getClass().getMethod(method, null).invoke(obj, null);
		return v;
	}

	public static List exchangeList2Tree(List list, String pkField,
			String foriegnField, String childrenField) {
		try {
			Hashtable ht = new Hashtable();
			String keyParent, key;
			for (int i = 0; i < list.size(); i++) {
				key = "" + invoke(list.get(i), pkField);// .getNodeId();
				ht.put(key, list.get(i));

				List children = (List) invoke(list.get(i), childrenField);// .getChildren();
				children.clear();

			}
			for (int i = list.size() - 1; i >= 0; i--) {
				keyParent = "" + invoke(list.get(i), foriegnField);// .getParentId();
				if (ht.containsKey(keyParent)) {
					// StructureNodeDTO parent=(StructureNodeDTO)
					// ht.get(keyParent);
					Object parent = ht.get(keyParent);
					List children = (List) invoke(parent, childrenField);// .getChildren();
					children.add(list.get(i));
					list.remove(i);
				}
			}
			return list;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void printObject(Object obj, boolean drillListFields,
			boolean drillObjectFields, boolean showFullName) {
		StringBuffer sb = new StringBuffer();
		printObject(obj, sb, 0, drillListFields, drillObjectFields,
				showFullName);
		System.out.println(sb.toString() + "\n");
	}

	public static void printObject(Object obj) {
		printObject(obj, true, true, true);
	}

	synchronized public static String encodeMD5(String inputStr) {
		byte[] defaultBytes = inputStr.getBytes();
		try {
			MessageDigest algorithm = MessageDigest.getInstance("MD5");
			algorithm.reset();
			algorithm.update(defaultBytes);
			byte messageDigest[] = algorithm.digest();
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++) {
				String hex = Integer.toHexString(0xFF & messageDigest[i]);
				if (hex.length() == 1)
					hexString.append('0');

				hexString.append(hex);
			}
			inputStr = hexString + "";
		} catch (NoSuchAlgorithmException nsae) {
		}
		return inputStr;
	}

	private static void printObject(Object obj, StringBuffer sb, int level,
			boolean drillListFields, boolean drillObjectFields,
			boolean showFullName) {
		String tab = "";
		for (int m = 0; m < level; m++)
			tab += "\t";
		if (level > 0)
			tab += level + ".";
		if (obj == null) {
			sb.append("{null}");
			return;
		}
		if (drillListFields && obj instanceof List) {
			List l = (List) obj;
			sb.append("\n>>>" + obj.getClass().getSimpleName() + ";");
			for (int j = 0; j < l.size(); j++) {
				sb.append("\n>>>");
				printObject(l.get(j), sb, level + 1, drillListFields,
						drillObjectFields, showFullName);
			}
			sb.append("\n<<<");
		}
		Class cls = obj.getClass();
		ArrayList<Field> flds = new ArrayList<Field>();
		Field[] fs;
		while (cls != null) {
			fs = cls.getDeclaredFields();
			for (int i = 0; i < fs.length; i++) {
				flds.add(fs[i]);
			}
			cls = cls.getSuperclass();
		}
		sb.append(tab + obj.getClass().getSimpleName() + ":[");
		for (int i = 0; i < flds.size(); i++) {
			String firstChar = flds.get(i).getName().substring(0, 1)
					.toUpperCase();
			String otherChars = flds.get(i).getName().substring(1);
			String fld = firstChar + otherChars;
			Object value = "";
			try {
				value = invoke(obj, fld);
			} catch (Exception e) {
				value = "(err)";
				// e.printStackTrace();
			}
			if (value == null) {
				sb.append(flds.get(i).getName() + "={null};");
			} else if (drillListFields && value instanceof List) {
				List l = (List) value;
				sb.append("\n>>>" + tab + flds.get(i).getName() + ":"
						+ value.getClass().getSimpleName() + ";");
				for (int j = 0; j < l.size(); j++) {
					sb.append("\n>>>");
					printObject(l.get(j), sb, level + 1, drillListFields,
							drillObjectFields, showFullName);
				}
				sb.append("\n<<<");
			} else if (drillListFields && value instanceof Object[]) {
				Object[] l = (Object[]) value;
				sb.append("\n>>" + tab + flds.get(i).getName() + ":"
						+ value.getClass().getSimpleName() + ";");
				sb.append("[");
				for (int j = 0; j < l.length; j++) {
					// printObject(l[j],sb,level+1,drillListFields,drillObjectFields,showFullName);
					sb.append(l[j].toString() + ";");
				}
				sb.append("]\n<<");
			} else if (drillObjectFields
					&& (value instanceof Integer || value instanceof Double)) {
				sb.append(flds.get(i).getName() + "=" + value + ";");
			} else if (drillObjectFields && !(value instanceof String)
					&& value instanceof Object) {
				String pkg = value.getClass().getPackage().getName();
				// if(pkg.indexOf("aip")>=0){
				sb.append("\n>");
				printObject(value, sb, level + 1, drillListFields,
						drillObjectFields, showFullName);
				sb.append("\n>\t" + tab + (level + 1) + ".");
				// }else{
				// sb.append(flds.get(i).getName()+"="+value+";");
				// }
			} else {
				sb.append(flds.get(i).getName() + "=" + value + ";");
			}
		}
		sb.append("]:fullName=" + obj.toString());
	}

	/**
	 * Convert Integer Array 2 String and reverse
	 */
	public static Integer[] splitString2Integer(String src, String delimeter) {
		String[] ar = src.split(delimeter);
		Integer[] arInt = new Integer[ar.length];
		for (int i = 0; i < ar.length; i++) {
			if (ar[i] != null && !"".equals(ar[i])) {
				try {
					Integer ii = Integer.parseInt(ar[i]);
					arInt[i] = ii;
				} catch (Exception ex) {
				}
			}
		}
		return arInt;
	}

	public static String mergeInteger2String(Integer[] ar, String delimeter) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < ar.length; i++) {
			sb.append(ar[i] + delimeter);
		}
		return sb.toString();
	}

	/**
	 * Convert Integer Array 2 String and reverse
	 */
	public static Double[] splitString2Double(String src, String delimeter) {
		String[] ar = src.split(delimeter);
		Double[] arInt = new Double[ar.length];
		for (int i = 0; i < ar.length; i++) {
			if (ar[i] != null && !"".equals(ar[i])) {
				try {
					Double ii = Double.parseDouble(ar[i]);
					arInt[i] = ii;
				} catch (Exception ex) {
				}
			}
		}
		return arInt;
	}

	public static String mergeDouble2String(Double[] ar, String delimeter) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < ar.length; i++) {
			sb.append(ar[i] + delimeter);
		}
		return sb.toString();
	}

	public static String replace(String str, String pattern, String replace) {
		int s = 0;
		int e = 0;
		StringBuffer result = new StringBuffer();
		while ((e = str.indexOf(pattern, s)) >= 0) {
			result.append(str.substring(s, e));
			result.append(replace);
			s = e + pattern.length();
		}
		result.append(str.substring(s));
		return result.toString();
	}

	public static String replaceString(String src, String find,
			String replacement) {
		if (src != null && !"".equals(src) && find != null) {
			int pos = src.indexOf(find);
			if (pos >= 0) {
				src = src.substring(0, pos) + replacement
						+ src.substring(pos + find.length());
			}
		}
		return src;
	}

	public static String replaceAllString(String src, String find,
			String replacement) {
		StringBuffer sb = new StringBuffer(src);
		if (src != null && !"".equals(src) && find != null) {
			int pos = sb.lastIndexOf(find);
			while (pos >= 0) {
				sb.replace(pos, pos + find.length(), replacement);
				pos = sb.lastIndexOf(find, pos - 1);
			}
		}
		return sb.toString();
	}

	public static String truncate(String src, int length) {
		if (src != null && src.length() > length) {
			src = src.substring(0, length) + "...";
		}
		return src;
	}

	public static void replaceString(StringBuffer src, String find,
			String replacement) {
		if (src != null && find != null) {
			int pos = src.indexOf(find);
			if (pos >= 0) {
				src.replace(pos, pos + find.length(), replacement);
			}
		}
	}

	public static void replaceAllString(StringBuffer src, String find,
			String replacement) {
		if (src != null && find != null) {
			int pos = src.indexOf(find);
			while (pos >= 0) {
				src.replace(pos, pos + find.length(), replacement);
				pos = src.indexOf(find, pos + 1);
			}
		}
	}

	public static Object invokeMulti(Object obj, String field)
			throws IllegalArgumentException, SecurityException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		Object v = null;
		if (field.indexOf(".") > -1) {
			String ar[] = splitString(field, ".");
			Object oo = obj;
			for (int i = 0; i < ar.length; i++) {
				oo = invoke(oo, ar[i]);
			}
			v = oo;
		} else {
			v = invoke(obj, field);
		}
		return v;
	}

	public static Object invokeMultiMethod(Object obj, String method)
			throws IllegalArgumentException, SecurityException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		Object v = null;
		if (method.indexOf(".") > -1) {
			String ar[] = splitString(method, ".");
			Object oo = obj;
			for (int i = 0; i < ar.length; i++) {
				oo = invokeMethod(oo, ar[i]);
			}
			v = oo;
		} else {
			v = invokeMethod(obj, method);
		}
		return v;
	}

	public static String[] splitString(String src, String delimeter) {
		StringTokenizer tokenizer = new StringTokenizer(src, delimeter);
		int n = tokenizer.countTokens();
		String ar[] = new String[n];
		for (int i = 0; i < n; i++) {
			ar[i] = tokenizer.nextToken();
		}
		return ar;
	}

	public static Object invokeGetter(Object obj, String field)
			throws IllegalArgumentException, SecurityException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		Object v = obj.getClass().getMethod("get" + field, null)
				.invoke(obj, null);
		return v;
	}

	public static Object invokeSetter(Object obj, String field, String value)
			throws IllegalArgumentException, SecurityException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		Object v = obj.getClass().getMethod("set" + field, String.class)
				.invoke(obj, value);
		return v;
	}

	public static String joinSelectedIds(Object[] ids, String delimeter) {
		String res = "";
		if (ids != null) {
			for (int i = 0; i < ids.length; i++) {
				res += (ids[i] == null ? "" : ids[i] + ",");
			}
			if (res.length() > 0) {
				res = res.substring(0, res.length() - 1);
			}
		}
		return res;
	}

	public static Object invoke(Object obj, String field, Object value)
			throws IllegalArgumentException, SecurityException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, NoSuchFieldException {
		Class fieldType = null;
		try {
			fieldType = obj.getClass().getDeclaredField(field).getType();
		} catch (NoSuchFieldException e) {
			fieldType = obj.getClass().getSuperclass().getDeclaredField(field)
					.getType();
		}

		Object v = obj.getClass()
				.getMethod("set" + change2Capital(field), fieldType)
				.invoke(obj, getTypeValue(fieldType, value));
		return v;
	}

	public static Object getTypeValue(Class type, Object obj) {
		Object res = null;

		if (type.isAssignableFrom(Integer.class)) {
			return NVL.getInt(obj);
		} else if (type.isAssignableFrom(Double.class)) {
			return NVL.getDbl(obj);
		} else if (type.isAssignableFrom(Boolean.class)) {
			return NVL.getBool(obj);
		} else if (type.isAssignableFrom(Long.class)) {
			return NVL.getLng(obj);
		} else if (type.isAssignableFrom(String.class)) {
			return NVL.getString(obj);
		}
		return null;
	}

	public static String change2Capital(String fieldName) {
		String firstChar = fieldName.substring(0, 1).toUpperCase();
		String otherChars = fieldName.substring(1);
		String fld = firstChar + otherChars;

		return fld;
	}

	public static String[] splitSelectedIds(String selectedIds, String delimeter) {
		String[] res = {};
		ArrayList<String> al = new ArrayList<String>();
		if (selectedIds != null) {
			String[] ar = selectedIds.split(delimeter);
			for (int i = 0; i < ar.length; i++) {
				if (!"".equals(ar[i].trim())) {
					al.add(ar[i]);
				}
			}
			res = al.toArray(res);
		}
		return res;
	}

	public static String convertToString(ArrayList ids) {

		String r = ",";
		for (int i = 0; i < ids.size(); i++) {
			r = r + ids.get(i) + ",";
		}

		return r;
	}

	/*********************************************************************************************/
	// public static SyncIds getSyncIds(String nids,String oids){
	//
	// SyncIds sync = new SyncIds();
	//
	// String[] newIds = getSyncIds(nids);
	// String[] oldIds = getSyncIds(oids);
	// Hashtable<String, String> ht = new Hashtable<String, String>();
	//
	// ArrayList<String> news = new ArrayList<String>();
	// for(int i = 0; i<newIds.length;i++){
	// if(NVL.getInt(newIds[i])>0)
	// news.add(newIds[i]);
	// }
	//
	// for (int i = 0; i < oldIds.length; i++) {
	// if(NVL.getInt(oldIds[i])>0)
	// ht.put(oldIds[i], oldIds[i]);
	// }
	//
	//
	// for(int j=news.size()-1;j>=0;j--)
	// if(ht.containsKey(news.get(j))){
	// ht.remove(news.get(j));
	// news.remove(j);
	// }else{
	// if(NVL.getInt(news.get(j))>0)
	// sync.getNewIds().add(news.get(j));
	// }
	//
	// Enumeration<String> enumtree = ht.elements();
	//
	//
	// while(enumtree.hasMoreElements()){
	// String elm = enumtree.nextElement();
	// if(NVL.getInt(elm)>0)
	// sync.getDelIds().add(elm);
	// }
	//
	//
	// System.out.println("AIPUtil.getSyncIds()!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
	// System.out.println("oids="+oids);
	// System.out.println("nids="+nids);
	// System.out.println("sync=");
	// printObject(sync);
	//
	//
	// return sync;
	//
	// }
	private static String[] getSyncIds(String ids) {

		String[] syncIds = splitter(NVL.getString(ids), ",");
		for (int i = 0; i < syncIds.length; i++) {
			int pos = syncIds[i].indexOf(';');
			if (pos > 0) {
				syncIds[i] = splitter(syncIds[i], ";")[0];
			}
		}

		return syncIds;
	}

	public static String[] splitter(String input, String delim) {
		Vector<String> v = new Vector<String>();
		StringTokenizer tokenizer = new StringTokenizer(input, delim, false);
		while (tokenizer.hasMoreTokens()) {
			v.addElement(tokenizer.nextToken());
		}

		String[] output = new String[v.size()];
		for (int i = 0; i < v.size(); i++) {
			output[i] = v.elementAt(i);
		}
		return output;
	}

	public static String prepareTheJSONStringForDataTable(int currentPage,
			int totalItems, String json, String rowId, String success,
			String error) {
		String tmp = "";
		tmp = "{ \"draw\": " + currentPage + ", \"recordsTotal\":" + totalItems
				+ ", \"recordsFiltered\":" + totalItems;
		if (!success.equals(""))
			tmp += ", \"successm\":\"" + success+"\"";
		if (!error.equals(""))
			tmp += ", \"errorm\":\"" + error +"\"";
		json = tmp + ", \"data\": " + json + "}";
		json = json.replaceAll(rowId, "DT_RowId");
		return json;
	}

}
