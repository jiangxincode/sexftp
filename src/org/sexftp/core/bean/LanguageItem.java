/*    */ package org.sexftp.core.bean;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LanguageItem
/*    */ {
/* 11 */   private String key = "";
/* 12 */   private String enus = "";
/* 13 */   private String zhcn = "";
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public LanguageItem(String key)
/*    */   {
/* 22 */     this.enus = key;
/* 23 */     this.key = key;
/*    */   }
/*    */   
/*    */   public LanguageItem() {}
/*    */   
/*    */   public String getEnus() {
/* 29 */     return this.enus;
/*    */   }
/*    */   
/* 32 */   public void setEnus(String enus) { this.enus = enus; }
/*    */   
/*    */   public String getZhcn() {
/* 35 */     return this.zhcn;
/*    */   }
/*    */   
/* 38 */   public void setZhcn(String zhcn) { this.zhcn = zhcn; }
/*    */   
/*    */   public String getKey() {
/* 41 */     return this.key;
/*    */   }
/*    */   
/* 44 */   public void setKey(String key) { this.key = key; }
/*    */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\org\sexftp\core\bean\LanguageItem.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */