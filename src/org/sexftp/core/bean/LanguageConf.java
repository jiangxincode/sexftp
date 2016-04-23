/*     */ package org.sexftp.core.bean;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.desy.textparse.SmartText;
/*     */ import org.desy.textparse.context.SmartTextContext;
/*     */ import org.desy.textparse.exceptions.FunExecuteException;
/*     */ import org.desy.textparse.exceptions.FunParseException;
/*     */ import org.desy.textparse.funexecutors.AbstractFunParamActiveExeutor;
/*     */ import org.desy.textparse.info.SmartTextFunConfig;
/*     */ import org.desy.textparse.interfaces.FunParseable;
/*     */ import org.sexftp.core.utils.StringUtil;
/*     */ import sexftp.uils.PluginUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LanguageConf
/*     */ {
/*  31 */   private String defaultLang = "";
/*  32 */   private List<LanguageItem> langList = new ArrayList();
/*     */   
/*  34 */   public List<LanguageItem> getLangList() { return this.langList; }
/*     */   
/*     */   public void setLangList(List<LanguageItem> langList) {
/*  37 */     this.langList = langList;
/*     */   }
/*     */   
/*  40 */   public String getDefaultLang() { return this.defaultLang; }
/*     */   
/*     */ 
/*  43 */   public void setDefaultLang(String defaultLang) { this.defaultLang = defaultLang; }
/*     */   
/*  45 */   private Map<String, LanguageItem> langMap = null;
/*     */   
/*     */   public Map<String, LanguageItem> getLangMap()
/*     */   {
/*  49 */     return this.langMap;
/*     */   }
/*     */   
/*  52 */   public void setLangMap(Map<String, LanguageItem> langMap) { this.langMap = langMap; }
/*     */   
/*     */   public String getLangText(String text)
/*     */   {
/*  56 */     String defaultlang = PluginUtil.getLanguage();
/*  57 */     if (defaultlang == null) defaultlang = "enus";
/*  58 */     List<String> params = new ArrayList();
/*  59 */     if (text != null)
/*     */     {
/*  61 */       text = fixStr(text, params);
/*     */     }
/*  63 */     if (this.langMap == null)
/*     */     {
/*  65 */       Map<String, LanguageItem> langMapTmp = new HashMap();
/*  66 */       if (this.langList != null)
/*     */       {
/*  68 */         for (LanguageItem langitem : this.langList)
/*     */         {
/*  70 */           langMapTmp.put(langitem.getKey(), langitem);
/*     */         }
/*     */       }
/*  73 */       this.langMap = langMapTmp;
/*     */     }
/*  75 */     LanguageItem langItem = (LanguageItem)this.langMap.get(text);
/*  76 */     String ok = "";
/*  77 */     if (langItem != null)
/*     */     {
/*  79 */       if (("zhcn".equals(defaultlang)) && (langItem.getZhcn() != null) && (langItem.getZhcn().length() > 0))
/*     */       {
/*  81 */         ok = langItem.getZhcn();
/*     */       }
/*  83 */       else if ((langItem.getEnus() != null) && (langItem.getEnus().length() > 0))
/*     */       {
/*  85 */         ok = langItem.getEnus();
/*     */       }
/*     */       else
/*     */       {
/*  89 */         ok = text;
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/*  94 */       synchronized (this.langList) {
/*  95 */         if (!this.langMap.containsKey(text))
/*  96 */           this.langList.add(new LanguageItem(text));
/*     */       }
/*  98 */       ok = text;
/*     */     }
/* 100 */     if (ok == null) ok = "";
/* 101 */     return String.format(ok, params.toArray());
/*     */   }
/*     */   
/*     */ 
/* 105 */   private static final String FUN_K = "##FUN" + new Date().getTime() + "##";
/*     */   
/*     */   String fixStr(String str, List<String> params) {
/* 108 */     if (str.length() > 0)
/*     */     {
/* 110 */       str = StringUtil.replaceAll(str, "(", FUN_K + "Ykh(");
/* 111 */       str = StringUtil.replaceAll(str, "[", FUN_K + "Fkh(");
/* 112 */       str = StringUtil.replaceAll(str, "]", ")");
/* 113 */       SmartTextContext ctx = new SmartTextContext();
/* 114 */       Ykh ykh = new Ykh("yhk", params);
/* 115 */       Ykh fkh = new Ykh("fhk", params);
/* 116 */       ctx.getFunConfig().registerFun("Ykh", ykh);
/* 117 */       ctx.getFunConfig().registerFun("Fkh", fkh);
/* 118 */       ctx.getFunConfig().setFeatureStr(FUN_K);
/* 119 */       SmartText s = new SmartText(ctx);
/* 120 */       str = (String)s.parse(str);
/* 121 */       return str;
/*     */     }
/* 123 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public class Ykh
/*     */     extends AbstractFunParamActiveExeutor
/*     */   {
/* 133 */     private List<String> params = new ArrayList();
/* 134 */     private String pix = "yhk";
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public Ykh(List<String> pix)
/*     */     {
/* 141 */       this.pix = pix;
/* 142 */       this.params = params;
/*     */     }
/*     */     
/*     */     public Object exec(SmartTextContext arg0, Object... args)
/*     */       throws FunExecuteException
/*     */     {
/* 148 */       String ag = "";
/* 149 */       if (args != null)
/*     */       {
/* 151 */         for (int i = 0; i < args.length; i++)
/*     */         {
/* 153 */           if (i > 0)
/*     */           {
/* 155 */             ag = ag + arg0.getFunConfig().getParamSeparater();
/*     */           }
/* 157 */           ag = ag + args[i];
/*     */         }
/*     */       }
/* 160 */       this.params.add(ag);
/* 161 */       if (this.pix.equals("yhk"))
/*     */       {
/* 163 */         return "(%s)";
/*     */       }
/* 165 */       if (this.pix.equals("fhk"))
/*     */       {
/* 167 */         return "[%s]";
/*     */       }
/*     */       
/* 170 */       return "%s";
/*     */     }
/*     */     
/*     */     public Object[] parseParams(List<FunParseable> arg0, SmartTextContext arg1)
/*     */       throws FunParseException
/*     */     {
/* 176 */       return super.parseParams(arg0, arg1);
/*     */     }
/*     */     
/*     */     public String[] getParams() {
/* 180 */       return (String[])this.params.toArray(new String[0]);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\org\sexftp\core\bean\LanguageConf.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */