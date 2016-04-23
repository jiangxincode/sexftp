/*    */ package org.sexftp.core.license;
/*    */ 
/*    */ import com.thoughtworks.xstream.XStream;
/*    */ import java.io.File;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.IOException;
/*    */ import java.lang.reflect.Method;
/*    */ import java.net.URL;
/*    */ import java.util.Arrays;
/*    */ import java.util.Map;
/*    */ import org.eclipse.core.runtime.Platform;
/*    */ import org.osgi.framework.Bundle;
/*    */ import org.sexftp.core.exceptions.BizException;
/*    */ import org.sexftp.core.utils.ByteUtils;
/*    */ import org.sexftp.core.utils.FileUtil;
/*    */ import sexftp.uils.LogUtil;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LicenseUtils
/*    */ {
/*    */   public static final String CL_STAR_FIX = "cebdb0766dea4c3ca131839b4cb63587";
/*    */   public static final String CL_END_FIX = "cebdb0766dea4c3ca131839b4cb63585";
/* 25 */   public static int SUC_TIMES = 0;
/* 26 */   private static String pluginPath = null;
/* 27 */   public static String licensePath = null;
/* 28 */   private static Class licenseCheckClass = null;
/*    */   
/*    */   public static void checkUpdateAndLicense(String actionName) {
/* 31 */     if (licenseCheckClass == null)
/*    */     {
/* 33 */       if (pluginPath == null)
/*    */         try {
/* 35 */           pluginPath = Platform.asLocalURL(Platform.getBundle("sexftp").getEntry("")).getFile();
/*    */         } catch (IOException localIOException) {
/* 37 */           pluginPath = "CannotGet";
/*    */         }
/* 39 */       if (!"CannotGet".equals(pluginPath))
/*    */       {
/* 41 */         licensePath = pluginPath + "/license/";
/* 42 */         File licenseFileFolder = new File(licensePath);
/* 43 */         if (licenseFileFolder.exists())
/*    */         {
/* 45 */           File[] licenseFiles = licenseFileFolder.listFiles();
/* 46 */           Arrays.sort(licenseFiles);
/* 47 */           for (int i = licenseFiles.length - 1; i >= 0; i--)
/*    */           {
/* 49 */             File licenseFile = licenseFiles[i];
/* 50 */             if (licenseFile.getName().endsWith(".s")) {
/* 51 */               LogUtil.info("License Update And Check:" + licenseFile.getName());
/* 52 */               String clsName = null;
/*    */               try {
/* 54 */                 byte[] licdata = FileUtil.readBytesFromInStream(new FileInputStream(licenseFile));
/* 55 */                 String licXmlStr = new String(ByteUtils.encryption(ByteUtils.getByteArray(new String(licdata, "utf-8"))), "utf-8");
/* 56 */                 Map<String, String> clsMap = (Map)new XStream().fromXML(licXmlStr);
/* 57 */                 clsName = (String)clsMap.get("clsname");
/* 58 */                 Class lc = new LicenseLoader(ByteUtils.getByteArray((String)clsMap.get("clsdata")), clsName).loadClass(clsName);
/* 59 */                 licenseCheckClass = lc;
/* 60 */                 lc.getMethod("updateCheckLicense", new Class[] { String.class }).invoke(lc.newInstance(), new Object[] { actionName });
/* 61 */                 SUC_TIMES += 1;
/* 62 */                 LogUtil.info("License Update And Check:" + lc.getName());
/*    */               }
/*    */               catch (BizException e) {
/* 65 */                 SUC_TIMES += 1;
/* 66 */                 throw e;
/*    */               }
/*    */               catch (Throwable e) {
/* 69 */                 LogUtil.error("Sexftp License Check And Update Error " + clsName, e);
/*    */               }
/*    */             }
/*    */           }
/*    */         }
/*    */       }
/*    */     }
/*    */     else {
/* 77 */       Class lc = licenseCheckClass;
/*    */       try {
/* 79 */         lc.getMethod("updateCheckLicense", new Class[] { String.class }).invoke(lc.newInstance(), new Object[] { actionName });
/*    */       } catch (Throwable e) {
/* 81 */         LogUtil.error("Sexftp License Check And Update Error", e);
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\org\sexftp\core\license\LicenseUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */