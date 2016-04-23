/*    */ package sexftp.uils;
/*    */ 
/*    */ import java.util.Hashtable;
/*    */ import java.util.Map;
/*    */ import org.eclipse.ui.console.ConsolePlugin;
/*    */ import org.eclipse.ui.console.IConsole;
/*    */ import org.eclipse.ui.console.IConsoleManager;
/*    */ import org.eclipse.ui.console.MessageConsole;
/*    */ import org.eclipse.ui.console.MessageConsoleStream;
/*    */ import org.eclipse.ui.plugin.AbstractUIPlugin;
/*    */ 
/*    */ public class Console
/*    */ {
/*    */   private String name;
/*    */   private String iconName;
/* 16 */   private MessageConsole console = null;
/* 17 */   private MessageConsoleStream consoleStream = null;
/*    */   
/* 19 */   private static Map<String, Console> cash = new Hashtable();
/*    */   
/*    */   public static Console createConsole(String name, String icon) {
/* 22 */     Console console2 = (Console)cash.get(name);
/* 23 */     if (console2 == null)
/*    */     {
/* 25 */       Console con = new Console(name, icon);
/* 26 */       con.init();
/* 27 */       console2 = con;
/* 28 */       cash.put(name, console2);
/*    */     }
/* 30 */     return console2;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private Console(String name, String iconName)
/*    */   {
/* 39 */     this.name = name;
/* 40 */     this.iconName = iconName;
/*    */   }
/*    */   
/*    */ 
/*    */   private void init()
/*    */   {
/* 46 */     if (this.console == null)
/*    */     {
/*    */ 
/* 49 */       this.console = new MessageConsole(this.name, AbstractUIPlugin.imageDescriptorFromPlugin("sexftp", "/icons/" + this.iconName));
/*    */       
/*    */ 
/* 52 */       ConsolePlugin.getDefault().getConsoleManager().addConsoles(
/* 53 */         new IConsole[] { this.console });
/*    */       
/*    */ 
/* 56 */       this.consoleStream = this.console.newMessageStream();
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public void openConsole()
/*    */   {
/* 63 */     ConsolePlugin.getDefault().getConsoleManager().showConsoleView(this.console);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void console(String text)
/*    */   {
/* 70 */     this.consoleStream.println(text);
/*    */     
/* 72 */     LogUtil.info(text);
/*    */   }
/*    */   
/*    */   public MessageConsoleStream getConsoleStream() {
/* 76 */     return this.consoleStream;
/*    */   }
/*    */   
/*    */   public void setConsoleStream(MessageConsoleStream consoleStream) {
/* 80 */     this.consoleStream = consoleStream;
/*    */   }
/*    */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\sexftp\uils\Console.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */