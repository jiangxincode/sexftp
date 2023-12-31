package org.sexftp.core.utils;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.thoughtworks.xstream.XStream;

public class StringUtil {

	public static String iso88591(String str, String encode) {
		try {
			return new String(str.getBytes(encode), "iso-8859-1");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public static String bakFromiso88591(String str, String encode) {
		try {
			return new String(str.getBytes("iso-8859-1"), encode);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public static String readExceptionDetailInfo(Throwable e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString();
	}

	public static final char[] REGKEYS = { '\\', '^', '{', '}', '[', ']', '-', '+', '$', '*', '?', '.' };

	public static final String FILE_STYLE_MF = "d79d6a9cd10a4c5cb523b00fea9dda7f";

	public static String replaceAll(String str, String findstr, String replacestr) {
		String[] sps = split(str, findstr);
		StringBuffer s = new StringBuffer();
		String[] arrayOfString1;
		int j = (arrayOfString1 = sps).length;
		for (int i = 0; i < j; i++) {
			String sp = arrayOfString1[i];

			if (i > 0) {
				s.append(replacestr);
			}
			s.append(sp);
			i++;
		}
		return s.toString();
	}

	public static String[] split(String str, String findstr) {
		int indexOf = str.indexOf(findstr);
		int lastIndex = 0;
		List<String> lst = new ArrayList<String>();
		for (int i = 0; (i < 5000) && (indexOf >= 0); i++) {
			String sp = str.substring(lastIndex, indexOf);
			lst.add(sp);
			lastIndex = indexOf + findstr.length();
			if (indexOf + findstr.length() >= str.length())
				break;
			indexOf = str.indexOf(findstr, indexOf + findstr.length());
		}

		lst.add(str.substring(lastIndex, str.length()));
		return (String[]) lst.toArray(new String[0]);
	}

	public static boolean fileStyleMatch(String str, String[] matchstrs) {
		if (matchstrs != null) {
			String[] arrayOfString;
			int j = (arrayOfString = matchstrs).length;
			for (int i = 0; i < j; i++) {
				String matchstr = arrayOfString[i];
				if ((matchstr != null) && (matchstr.trim().length() > 0) && (fileStyleMatch(str, matchstr))) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean fileStyleEIMatch(String str, String[] excludes, String[] includes) {
		boolean isExlude = (excludes == null) || (!fileStyleMatch(str, excludes));
		boolean isInclude = false;
		if (includes != null) {
			if ((new File(str).isDirectory()) || (str.endsWith("/"))) {
				isInclude = true;
			} else {
				isInclude = fileStyleMatch(str, includes);
			}

		} else {
			isInclude = true;
		}
		return (isInclude) && (isExlude);
	}

	public static boolean fileStyleMatch(String str, String matchstr) {
		str = str.replace('\\', '/').toLowerCase();
		matchstr = matchstr.replace('\\', '/').toLowerCase();
		try {
			String s = matchstr;

			for (int i = 0; i < REGKEYS.length; i++) {
				char regKey = REGKEYS[i];
				// s = replaceAll(s, regKey, String.format("%s%2d", new Object[]
				// { "d79d6a9cd10a4c5cb523b00fea9dda7f", Integer.valueOf(i) }));
			}
			for (int i = 0; i < REGKEYS.length; i++) {
				String newRK = String.format("%s%2d",
						new Object[] { "d79d6a9cd10a4c5cb523b00fea9dda7f", Integer.valueOf(i) });
				char regKey = REGKEYS[i];

				switch (regKey) {
				case '\\':
					s = replaceAll(s, newRK, "\\\\");
					break;
				case '.':
					s = replaceAll(s, newRK, "\\.");
					break;
				case '*':
					s = replaceAll(s, newRK, ".*");
					break;
				case '?':
					s = replaceAll(s, newRK, ".?");
					break;
				default:
					s = replaceAll(s, newRK, "\\" + regKey);
				}

			}

			s = "^" + s + "$";

			Pattern p = Pattern.compile(s);

			Matcher matcher = p.matcher(str);

			return matcher.matches();
		} catch (Exception e) {
			throw new RuntimeException(String.format("%s - %s", new Object[] { str, matchstr }), e);
		}
	}

	public static String simpString(String str, int maxlen) {
		if (str.length() > maxlen) {
			int lstind = str.length() - maxlen * 4 / 5;
			int fstind = maxlen * 1 / 5;
			return str.substring(0, fstind) + "..." + str.substring(lstind);
		}
		return str;
	}

	static XStream xStream = new XStream();

	public static Object deepClone(Object o) {
		return xStream.fromXML(xStream.toXML(o));
	}
}
