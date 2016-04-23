package org.sexftp.core.license;

import com.thoughtworks.xstream.XStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.sexftp.core.exceptions.BizException;
import org.sexftp.core.utils.ByteUtils;
import org.sexftp.core.utils.FileUtil;
import sexftp.uils.LogUtil;

public class LicenseUtils {
	public static final String CL_STAR_FIX = "cebdb0766dea4c3ca131839b4cb63587";
	public static final String CL_END_FIX = "cebdb0766dea4c3ca131839b4cb63585";
	public static int SUC_TIMES = 0;
	private static String pluginPath = null;
	public static String licensePath = null;
	private static Class licenseCheckClass = null;

	public static void checkUpdateAndLicense(String actionName) {
		if (licenseCheckClass == null) {
			if (pluginPath == null)
				try {
					pluginPath = Platform.asLocalURL(Platform.getBundle("sexftp").getEntry("")).getFile();
				} catch (IOException localIOException) {
					pluginPath = "CannotGet";
				}
			if (!"CannotGet".equals(pluginPath)) {
				licensePath = pluginPath + "/license/";
				File licenseFileFolder = new File(licensePath);
				if (licenseFileFolder.exists()) {
					File[] licenseFiles = licenseFileFolder.listFiles();
					Arrays.sort(licenseFiles);
					for (int i = licenseFiles.length - 1; i >= 0; i--) {
						File licenseFile = licenseFiles[i];
						if (licenseFile.getName().endsWith(".s")) {
							LogUtil.info("License Update And Check:" + licenseFile.getName());
							String clsName = null;
							try {
								byte[] licdata = FileUtil.readBytesFromInStream(new FileInputStream(licenseFile));
								String licXmlStr = new String(
										ByteUtils.encryption(ByteUtils.getByteArray(new String(licdata, "utf-8"))),
										"utf-8");
								Map<String, String> clsMap = (Map) new XStream().fromXML(licXmlStr);
								clsName = (String) clsMap.get("clsname");
								Class lc = new LicenseLoader(ByteUtils.getByteArray((String) clsMap.get("clsdata")),
										clsName).loadClass(clsName);
								licenseCheckClass = lc;
								lc.getMethod("updateCheckLicense", new Class[] { String.class })
										.invoke(lc.newInstance(), new Object[] { actionName });
								SUC_TIMES += 1;
								LogUtil.info("License Update And Check:" + lc.getName());
							} catch (BizException e) {
								SUC_TIMES += 1;
								throw e;
							} catch (Throwable e) {
								LogUtil.error("Sexftp License Check And Update Error " + clsName, e);
							}
						}
					}
				}
			}
		} else {
			Class lc = licenseCheckClass;
			try {
				lc.getMethod("updateCheckLicense", new Class[] { String.class }).invoke(lc.newInstance(),
						new Object[] { actionName });
			} catch (Throwable e) {
				LogUtil.error("Sexftp License Check And Update Error", e);
			}
		}
	}
}
