/*    */ package sexftp.uils;
/*    */ 
/*    */ import com.thoughtworks.xstream.XStream;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.io.UnsupportedEncodingException;
/*    */ import java.net.URL;
/*    */ import java.util.ArrayList;
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.eclipse.core.runtime.Platform;
/*    */ import org.osgi.framework.Bundle;
/*    */ import org.sexftp.core.bean.LanguageConf;
/*    */ import org.sexftp.core.bean.LanguageItem;
/*    */ import org.sexftp.core.exceptions.SRuntimeException;
/*    */ import org.sexftp.core.utils.FileUtil;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LangUtil
/*    */ {
/* 31 */   private static LanguageConf langconf = null;
/* 32 */   private static String langfile = null;
/* 33 */   private static String plugPath = null;
/* 34 */   private static XStream xstream = new XStream();
/*    */   
/*    */   static
/*    */   {
/* 38 */     xstream.addImplicitCollection(LanguageConf.class, "langList");
/* 39 */     xstream.alias("languageItem", LanguageItem.class);
/*    */   }
/*    */   
/*    */   public static String langText(String text)
/*    */   {
/* 44 */     if (text == null) return "";
/* 45 */     if (langconf == null) {
/*    */       try
/*    */       {
/* 48 */         plugPath = Platform.asLocalURL(Platform.getBundle("sexftp").getEntry("")).getFile();
/* 49 */         langfile = plugPath + "/languages/lang.xml";
/* 50 */         if (new File(langfile).exists())
/*    */         {
/* 52 */           String xml = FileUtil.getTextFromFile(langfile, "utf-8");
/* 53 */           langconf = (LanguageConf)xstream.fromXML(xml);
/*    */         }
/*    */         else
/*    */         {
/* 57 */           langconf = new LanguageConf();
/*    */         }
/*    */       } catch (IOException e) {
/* 60 */         e.printStackTrace();
/* 61 */         langconf = new LanguageConf();
/*    */       }
/*    */     }
/* 64 */     if (langconf.getLangList() == null)
/*    */     {
/* 66 */       langconf.setLangList(new ArrayList());
/*    */     }
/* 68 */     int lanSize = langconf.getLangList().size();
/* 69 */     String langText = langconf.getLangText(text);
/* 70 */     if ((langconf.getLangList().size() > lanSize) && (langfile != null))
/*    */     {
/* 72 */       Map<String, LanguageItem> langMap = langconf.getLangMap();
/* 73 */       langconf.setLangMap(null);
/*    */       
/*    */ 
/* 76 */       Map<String, LanguageItem> lMap = new LinkedHashMap();
/* 77 */       for (LanguageItem li : langconf.getLangList())
/*    */       {
/* 79 */         lMap.put(li.getEnus(), li);
/*    */       }
/* 81 */       langconf.setLangList(new ArrayList(lMap.values()));
/*    */       
/* 83 */       String newXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + xstream.toXML(langconf);
/* 84 */       langconf.setLangMap(langMap);
/*    */       try {
/* 86 */         FileUtil.writeByte2File(langfile, newXml.getBytes("utf-8"));
/*    */       } catch (UnsupportedEncodingException e) {
/* 88 */         throw new SRuntimeException(e);
/*    */       }
/*    */     }
/* 91 */     return langText;
/*    */   }
/*    */   
/*    */   public static void main(String[] args) {}
/*    */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\sexftp\uils\LangUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */