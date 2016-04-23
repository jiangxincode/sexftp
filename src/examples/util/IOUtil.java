/*    */ package examples.util;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
/*    */ import org.apache.commons.net.io.Util;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class IOUtil
/*    */ {
/*    */   public static final void readWrite(InputStream remoteInput, final OutputStream remoteOutput, InputStream localInput, final OutputStream localOutput)
/*    */   {
/* 47 */     Thread reader = new Thread()
/*    */     {
/*    */       public void run()
/*    */       {
/*    */         try
/*    */         {
/*    */           int ch;
/*    */           
/*    */           do
/*    */           {
/*    */             int ch;
/* 58 */             remoteOutput.write(ch);
/* 59 */             remoteOutput.flush();
/* 56 */             if (interrupted()) break; } while ((ch = IOUtil.this.read()) != -1);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */         }
/*    */         catch (IOException localIOException) {}
/*    */ 
/*    */ 
/*    */       }
/*    */       
/*    */ 
/*    */ 
/*    */ 
/* 70 */     };
/* 71 */     Thread writer = new Thread()
/*    */     {
/*    */ 
/*    */       public void run()
/*    */       {
/*    */         try
/*    */         {
/* 78 */           Util.copyStream(IOUtil.this, localOutput);
/*    */         }
/*    */         catch (IOException e)
/*    */         {
/* 82 */           e.printStackTrace();
/* 83 */           System.exit(1);
/*    */         }
/*    */         
/*    */       }
/*    */       
/* 88 */     };
/* 89 */     writer.setPriority(Thread.currentThread().getPriority() + 1);
/*    */     
/* 91 */     writer.start();
/* 92 */     reader.setDaemon(true);
/* 93 */     reader.start();
/*    */     
/*    */     try
/*    */     {
/* 97 */       writer.join();
/* 98 */       reader.interrupt();
/*    */     }
/*    */     catch (InterruptedException localInterruptedException) {}
/*    */   }
/*    */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\examples\util\IOUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */