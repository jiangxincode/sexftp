/*    */ package org.sexftp.core.license;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LicenseLoader
/*    */   extends ClassLoader
/*    */ {
/*    */   private byte[] data;
/*    */   
/*    */ 
/*    */   private String clsname;
/*    */   
/*    */ 
/*    */ 
/*    */   public LicenseLoader(byte[] data, String clsname)
/*    */   {
/* 18 */     this.data = data;
/* 19 */     this.clsname = clsname;
/*    */   }
/*    */   
/*    */   public Class<?> loadClass(String name)
/*    */     throws ClassNotFoundException
/*    */   {
/* 25 */     if (this.clsname.equals(name))
/* 26 */       return super.defineClass(name, this.data, 0, this.data.length);
/* 27 */     Class<?> loadClass = getClass().getClassLoader().loadClass(name);
/* 28 */     if (loadClass != null)
/* 29 */       return loadClass;
/* 30 */     return super.loadClass(name);
/*    */   }
/*    */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\org\sexftp\core\license\LicenseLoader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */