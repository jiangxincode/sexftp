package org.sexftp.core.utils;

import info.monitorenter.cpdetector.io.ASCIIDetector;
import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;
import info.monitorenter.cpdetector.io.UnicodeDetector;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.sexftp.core.exceptions.SRuntimeException;

public class Cpdetector {
	private static Set<Character> ZH_SBC_CASE = new HashSet(Arrays.asList(new Character[] { Character.valueOf('　'),
			/*
			 * Character.valueOf(65281), Character.valueOf(65282),
			 * Character.valueOf(65283), Character.valueOf(65284),
			 * Character.valueOf(65285), Character.valueOf(65286),
			 * Character.valueOf(65287), Character.valueOf(65288),
			 * Character.valueOf(65289), Character.valueOf(65290),
			 * Character.valueOf(65291), Character.valueOf(65292),
			 * Character.valueOf(65293), Character.valueOf(65294),
			 * Character.valueOf(65295), Character.valueOf(65296),
			 * Character.valueOf(65297), Character.valueOf(65298),
			 * Character.valueOf(65299), Character.valueOf(65300),
			 * Character.valueOf(65301), Character.valueOf(65302),
			 * Character.valueOf(65303), Character.valueOf(65304),
			 * Character.valueOf(65305), Character.valueOf(65306),
			 * Character.valueOf(65307), Character.valueOf(65308),
			 * Character.valueOf(65309), Character.valueOf(65310),
			 * Character.valueOf(65311), Character.valueOf(65312),
			 * Character.valueOf(65313), Character.valueOf(65314),
			 * Character.valueOf(65315), Character.valueOf(65316),
			 * Character.valueOf(65317), Character.valueOf(65318),
			 * Character.valueOf(65319), Character.valueOf(65320),
			 * Character.valueOf(65321), Character.valueOf(65322),
			 * Character.valueOf(65323), Character.valueOf(65324),
			 * Character.valueOf(65325), Character.valueOf(65326),
			 * Character.valueOf(65327), Character.valueOf(65328),
			 * Character.valueOf(65329), Character.valueOf(65330),
			 * Character.valueOf(65331), Character.valueOf(65332),
			 * Character.valueOf(65333), Character.valueOf(65334),
			 * Character.valueOf(65335), Character.valueOf(65336),
			 * Character.valueOf(65337), Character.valueOf(65338),
			 * Character.valueOf(65339), Character.valueOf(65340),
			 * Character.valueOf(65341), Character.valueOf(65342),
			 * Character.valueOf(65343), Character.valueOf(65344),
			 * Character.valueOf(65345), Character.valueOf(65346),
			 * Character.valueOf(65347), Character.valueOf(65348),
			 * Character.valueOf(65349), Character.valueOf(65350),
			 * Character.valueOf(65351), Character.valueOf(65352),
			 * Character.valueOf(65353), Character.valueOf(65354),
			 * Character.valueOf(65355), Character.valueOf(65356),
			 * Character.valueOf(65357), Character.valueOf(65358),
			 * Character.valueOf(65359), Character.valueOf(65360),
			 * Character.valueOf(65361), Character.valueOf(65362),
			 * Character.valueOf(65363), Character.valueOf(65364),
			 * Character.valueOf(65365), Character.valueOf(65366),
			 * Character.valueOf(65367), Character.valueOf(65368),
			 * Character.valueOf(65369), Character.valueOf(65370),
			 * Character.valueOf(65371), Character.valueOf(65372),
			 * Character.valueOf(65373), Character.valueOf(65374)
			 */ }));
	public static final int sta = 161;

	public static void main(String[] args) throws Exception {
		byte[] data = FileUtil.readBytesFromInStream(new FileInputStream(
				"E:/coynn/workc/MyEclipse 8.5/productActivation/src/com/kkfun/productactive/monitor/ProductActivationMonitor.java"));
		new String(data, "GBK");
		new String(data, "x-EUC-TW");
		data = delASCIIdata(data);
		System.out.println(encode(new ByteArrayInputStream(data, 0, 2)));
		System.out.println(new String(data));
	}

	public static byte[] delASCIIdata(byte[] data) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		for (int i = 0; i < data.length; i++) {
			byte b = data[i];
			char c = (char) b;
			if ((c >= '') || (c <= '\b')) {

				bos.write(b);
				i++;

				if (i < data.length)
					bos.write(data[i]);
			}
		}
		return bos.toByteArray();
	}

	public static final String[] SUPORT_CHARSET = { "GBK", "UTF-8", "GB2312", "GB18030" };
	private static final int PS = 134217728;

	public static String isOnlyASC(byte[] data) throws UnsupportedEncodingException {
		String[] arrayOfString;
		int j = (arrayOfString = SUPORT_CHARSET).length;
		for (int i = 0; i < j; i++) {
			String suportchar = arrayOfString[i];

			boolean isallasc = true;
			String encode = "";
			char[] arrayOfChar;
			int m = (arrayOfChar = new String(data, suportchar).toCharArray()).length;
			for (int k = 0; k < m; k++) {
				char c = arrayOfChar[k];

				if ((c > '') || (c < '\b')) {
					if (ZH_SBC_CASE.contains(Character.valueOf(c))) {
						encode = suportchar;
					} else {
						isallasc = false;
						break;
					}
				}
			}
			if (isallasc) {
				if (encode.length() > 0) {
					return "US-ASCII_" + encode;
				}

				return "US-ASCII";
			}
		}
		return "";
	}

	public static String onlyNoneASCII(String str) {
		StringBuffer sb = new StringBuffer();
		char[] arrayOfChar;
		int j = (arrayOfChar = str.toCharArray()).length;
		for (int i = 0; i < j; i++) {
			char c = arrayOfChar[i];

			if ((c > '') || (c < '\b')) {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	public static Charset richencode(InputStream in) throws UnsupportedEncodingException {
		byte[] data = FileUtil.readBytesFromInStream(in);
		String isallasc = isOnlyASC(data);
		String encode = null;
		if (isallasc.length() > 0) {
			if (isallasc.startsWith("US-ASCII_")) {
				encode = isallasc.replace("US-ASCII_", "");
			} else {
				encode = isallasc;
			}
		}
		if (encode == null) {
			Charset c = encode(new ByteArrayInputStream(data));
			encode = c != null ? c.toString() : null;
		}

		if (encode != null) {
			if (("x-EUC-TW".equalsIgnoreCase(encode)) || ("windows-1252".equalsIgnoreCase(encode))
					|| ("EUC-KR".equalsIgnoreCase(encode))) {
				byte[] newdata = delASCIIdata(data);
				Charset c = encode(new ByteArrayInputStream(newdata));
				String newcode = c != null ? c.toString() : null;
				if (newcode != null) {
					if (newcode.startsWith("GB")) {
						encode = newcode;
					} else {
						encode = "GB18030";
					}
				}
			}
			try {
				return Charset.forName(encode);
			} catch (Exception e) {
				throw new SRuntimeException(e);
			}
		}
		return null;
	}

	public static Charset encode(InputStream in) {
		return encode(in, 134217728);
	}

	public static Charset xencode(InputStream in) {
		return null;
	}

	public static Charset encode(InputStream in, int length) {
		CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();

		detector.add(ASCIIDetector.getInstance());
		detector.add(JChardetFacade.getInstance());

		detector.add(UnicodeDetector.getInstance());
		Charset charset = null;
		BufferedInputStream bis = new BufferedInputStream(in);
		try {
			charset = detector.detectCodepage(bis, length);
		} catch (IllegalArgumentException localIllegalArgumentException) {
		} catch (IOException localIOException) {
		}

		return charset;
	}
}
