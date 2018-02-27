package tools;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;

public class UTF8 {
	
	public static void main(String[] args) {
//		System.out.println("UTF8.main():"+URLEncoder.encode("ÛŒ"));
//		System.out.println("UTF8.main():"+URLEncoder.encode("ÙŠ"));
		try {
			System.out.println("UTF8.main()"+URLEncoder.encode(URLEncoder.encode("ÛŒ"), "iso8859-1"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static boolean imageDirectoriesBuilt = false;
	public static boolean treeDirectoriesBuilt = false;
	public static boolean lawviewDirectoryBuilt = false;
	public static boolean searchDirectoryBuilt = false;
	public static boolean regulationhDirectoryBuilt = false;
	public static boolean advancedsearchDirectoryBuilt = false;
	public static boolean lawStructureDirectoryBuilt = false;

	private static void append(StringBuilder dest, StringBuilder tmp, String ch, String extra) {
		try {
			if (tmp.length() > 0) {
				dest.append(new String(URLDecoder.decode(tmp.toString(), "iso8859-1").getBytes(), "utf8"));
			}
			tmp.delete(0, tmp.length());
			dest.append(ch);
			if (extra.length() > 0) {
				dest.append(new String(URLDecoder.decode(extra, "iso8859-1").getBytes(), "utf8"));
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public static String cnvUTF8(String text) {
		if (text == null || "".equals(text))
			return text;
		String st = "";
		StringBuilder sb = new StringBuilder();
		try {
			st = URLEncoder.encode(text, "iso8859-1");
//			System.out.println(st);

			String[] ar = st.split("%");
			StringBuilder tmpb = new StringBuilder();
			for (int i = 0; i < ar.length; i++) {
				if ("D9".equalsIgnoreCase(ar[i])) {
					if (i + 1 < ar.length) {
						i += 1;
						if (ar[i].startsWith("88")) {
							append(sb, tmpb, "Ùˆ", ar[i].substring(2));
						} else if (ar[i].startsWith("82")) {
							append(sb, tmpb, "Ù‚", ar[i].substring(2));
						} else if (ar[i].startsWith("81")) {
							append(sb, tmpb, "Ù�", ar[i].substring(2));
						} else if (ar[i].startsWith("87")) {
							append(sb, tmpb, "Ù‡", ar[i].substring(2));
						} else if (ar[i].startsWith("84")) {
							append(sb, tmpb, "Ù„", ar[i].substring(2));
						} else if (ar[i].startsWith("86")) {
							append(sb, tmpb, "Ù†", ar[i].substring(2));
						} else if (ar[i].startsWith("85")) {
							append(sb, tmpb, "Ù…", ar[i].substring(2));
						} else if (ar[i].startsWith("8A")) {
							append(sb, tmpb, "ÛŒ", ar[i].substring(2));
						} else if (ar[i].startsWith("BE")) {
							append(sb, tmpb, "Ù¾", ar[i].substring(2));
						} else {
							tmpb.append((i > 0 ? "%" : "") + ar[i]);
						}
					} else {
						tmpb.append((i > 0 ? "%" : "") + ar[i]);
					}
				} else if ("DA".equalsIgnoreCase(ar[i])) {
					if (i + 1 < ar.length) {
						i += 1;
						if (ar[i].startsWith("86")) {
							append(sb, tmpb, "Ú†", ar[i].substring(2));
						} else if (ar[i].startsWith("98")) {
							append(sb, tmpb, "Ú˜", ar[i].substring(2));
						} else if (ar[i].startsWith("A9")) {				// By Keshavarz
							append(sb, tmpb, "Ú©", ar[i].substring(2));
						} else if (ar[i].startsWith("AF")) {
							append(sb, tmpb, "Ú¯", ar[i].substring(2));
						} else {
							tmpb.append((i > 0 ? "%" : "") + ar[i]);
						}
					} else {
						tmpb.append((i > 0 ? "%" : "") + ar[i]);
					}
					// these are only for linux
				} else if ("D8".equalsIgnoreCase(ar[i])) {
					if (i + 1 < ar.length) {
						i += 1;
						if (ar[i].startsWith("8C")) {
							append(sb, tmpb, "ÙŠ", ar[i].substring(2));
						} else if(ar[i].startsWith("A3")) {					// By Keshavarz
							append(sb, tmpb, "Ø£", ar[i].substring(2));
						} else if(ar[i].startsWith("A4")) {					// By Keshavarz
							append(sb, tmpb, "Ø¤", ar[i].substring(2));
						}  else if(ar[i].startsWith("A5")) {				// By Keshavarz
							append(sb, tmpb, "Ø¥", ar[i].substring(2));
						} else if(ar[i].startsWith("A2")) {					// By Keshavarz
							append(sb, tmpb, "Ø¢", ar[i].substring(2));
						} else if(ar[i].startsWith("A6")) {					// By Keshavarz
							append(sb, tmpb, "Ø¦", ar[i].substring(2));
						} else if(ar[i].startsWith("A9")) {					// By Keshavarz
							append(sb, tmpb, "Ø©", ar[i].substring(2));
						} else if (ar[i].startsWith("B6")) {
							append(sb, tmpb, "Ø¶", ar[i].substring(2));
						} else if (ar[i].startsWith("B5")) {
							append(sb, tmpb, "Øµ", ar[i].substring(2));
						} else if (ar[i].startsWith("AB")) {
							append(sb, tmpb, "Ø«", ar[i].substring(2));
						} else if (ar[i].startsWith("BA")) {
							append(sb, tmpb, "Øº", ar[i].substring(2));
						} else if (ar[i].startsWith("B9")) {
							append(sb, tmpb, "Ø¹", ar[i].substring(2));
						} else if (ar[i].startsWith("AE")) {
							append(sb, tmpb, "Ø®", ar[i].substring(2));
						} else if (ar[i].startsWith("AD")) {
							append(sb, tmpb, "Ø­", ar[i].substring(2));
						} else if (ar[i].startsWith("AC")) {
							append(sb, tmpb, "Ø¬", ar[i].substring(2));
						} else if (ar[i].startsWith("B4")) {
							append(sb, tmpb, "Ø´", ar[i].substring(2));
						} else if (ar[i].startsWith("B3")) {
							append(sb, tmpb, "Ø³", ar[i].substring(2));
						} else if (ar[i].startsWith("A8")) {
							append(sb, tmpb, "Ø¨", ar[i].substring(2));
						} else if (ar[i].startsWith("A7")) {
							append(sb, tmpb, "Ø§", ar[i].substring(2));
						} else if (ar[i].startsWith("AA")) {
							append(sb, tmpb, "Øª", ar[i].substring(2));
						} else if (ar[i].startsWith("B8")) {
							append(sb, tmpb, "Ø¸", ar[i].substring(2));
						} else if (ar[i].startsWith("B7")) {
							append(sb, tmpb, "Ø·", ar[i].substring(2));
						} else if (ar[i].startsWith("B2")) {
							append(sb, tmpb, "Ø²", ar[i].substring(2));
						} else if (ar[i].startsWith("B1")) {
							append(sb, tmpb, "Ø±", ar[i].substring(2));
						} else if (ar[i].startsWith("B0")) {
							append(sb, tmpb, "Ø°", ar[i].substring(2));
						} else if (ar[i].startsWith("AF")) {
							append(sb, tmpb, "Ø¯", ar[i].substring(2));
						} else if (ar[i].startsWith("A1")) {
							append(sb, tmpb, "Ø¡", ar[i].substring(2));
						} else {
							tmpb.append((i > 0 ? "%" : "") + ar[i]);
						}
					} else {
						tmpb.append((i > 0 ? "%" : "") + ar[i]);
					}
				} else if ("DB".equalsIgnoreCase(ar[i])) {
					// %D8%
					// %D9%
					if (i + 1 < ar.length) {
						i += 1;
						if (ar[i].startsWith("8C")) {
							append(sb, tmpb, "ÛŒ", ar[i].substring(2));
						} else if (ar[i].startsWith("B0")) {
							append(sb, tmpb, "0", ar[i].substring(2));
						} else if (ar[i].startsWith("B1")) {
							append(sb, tmpb, "1", ar[i].substring(2));
						} else if (ar[i].startsWith("B2")) {
							append(sb, tmpb, "2", ar[i].substring(2));
						} else if (ar[i].startsWith("B3")) {
							append(sb, tmpb, "3", ar[i].substring(2));
						} else if (ar[i].startsWith("B4")) {
							append(sb, tmpb, "4", ar[i].substring(2));
						} else if (ar[i].startsWith("B5")) {
							append(sb, tmpb, "5", ar[i].substring(2));
						} else if (ar[i].startsWith("B6")) {
							append(sb, tmpb, "6", ar[i].substring(2));
						} else if (ar[i].startsWith("B7")) {
							append(sb, tmpb, "7", ar[i].substring(2));
						} else if (ar[i].startsWith("B8")) {
							append(sb, tmpb, "8", ar[i].substring(2));
						} else if (ar[i].startsWith("B9")) {
							append(sb, tmpb, "9", ar[i].substring(2));
						} else {
							tmpb.append((i > 0 ? "%" : "") + ar[i]);
						}
					} else {
						tmpb.append((i > 0 ? "%" : "") + ar[i]);
					}
				} else {
					tmpb.append((i > 0 ? "%" : "") + ar[i]);
				}
			}
			append(sb, tmpb, "", "");
			text = sb.toString(); // new
			// String(sb.toString().getBytes(),"utf8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
//		System.out.println(text);

		return text;

		// theses are only for linux
		// Ø¶ %D8%B6
		// Øµ %D8%B5
		// Ø« %D8%AB
		// Øº %D8%BA
		// Ø¹ %D8%B9
		// Ø® %D8%AE
		// %D8%AD Ø­
		// %D8%AC Ø¬
		// %D8%B4 Ø´
		// %D8%B3 Ø³
		// %D8%A8 Ø¨
		// %D8%A7 Ø§
		// %D8%AA Øª
		// %D8%B8 Ø¸
		// %D8%B7 Ø·
		// %D8%B2 Ø²
		// %D8%B1 Ø±
		// %D8%B0 Ø°
		// %D8%AF Ø¯
		// %D9%BE Ù¾

	}

	public static boolean createDirectory(String path) {
		File file = new File(path);
		if (!file.exists() || !file.isDirectory()) {
			file.mkdirs();
		}
		return true;
	}

	public static void makeObjUTF8(Object obj) {
		Class cls = obj.getClass();
		ArrayList<Field> fieldsArray = new ArrayList<Field>();
		Field[] fields;
		while (cls != null) {
			fields = cls.getDeclaredFields();
			for(int i=0; i< fields.length; i++) {
				fieldsArray.add(fields[i]);
			}
			cls = cls.getSuperclass();
		}
		for(int i=0; i< fieldsArray.size(); i++) {
			String fieldName = fieldsArray.get(i).getName().substring(0,1).toUpperCase() + fieldsArray.get(i).getName().substring(1);
			Object value = null;
			try {
				value = AMSUtililies.invokeGetter(obj, fieldName);
			} catch (Exception e) {
				//e.printStackTrace();
			}
			try {
				if(value != null && value instanceof String) {
					value = cnvUTF8(value.toString());
					AMSUtililies.invokeSetter(obj, fieldName, value.toString());
				}else if(!(value instanceof String) && value instanceof Object) {
					makeObjUTF8(value);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}

}
