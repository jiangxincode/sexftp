/*     */ package sexftp.views;
/*     */ 
/*     */ import org.eclipse.jface.dialogs.TitleAreaDialog;
/*     */ import org.eclipse.jface.resource.ImageDescriptor;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Control;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ import org.eclipse.swt.widgets.Text;
/*     */ import org.eclipse.ui.console.ConsolePlugin;
/*     */ import org.eclipse.ui.console.IConsole;
/*     */ import org.eclipse.ui.console.IConsoleManager;
/*     */ import org.eclipse.ui.console.MessageConsole;
/*     */ import org.eclipse.ui.console.MessageConsoleStream;
/*     */ import org.eclipse.ui.plugin.AbstractUIPlugin;
/*     */ import org.sexftp.core.Tosimpleable;
/*     */ import org.sexftp.core.ftp.bean.FtpConf;
/*     */ import org.sexftp.core.ftp.bean.FtpUploadConf;
/*     */ import sexftp.uils.LangUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UploadConfirmDialog
/*     */   extends TitleAreaDialog
/*     */   implements IFtpStreamMonitor
/*     */ {
/*     */   private String title;
/*     */   private String message;
/*     */   private java.util.List okFtpUploadConfList;
/*     */   private FtpConf ftpConf;
/*     */   
/*     */   public UploadConfirmDialog(Shell parentShell)
/*     */   {
/*  37 */     super(parentShell);
/*     */   }
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
/*     */   public UploadConfirmDialog(Shell parentShell, String title, String message, java.util.List okFtpUploadConfList, FtpConf ftpConf)
/*     */   {
/*  52 */     super(parentShell);
/*  53 */     this.title = LangUtil.langText(title);
/*  54 */     this.message = LangUtil.langText(message);
/*  55 */     this.okFtpUploadConfList = okFtpUploadConfList;
/*  56 */     this.ftpConf = ftpConf;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void create()
/*     */   {
/*  64 */     super.create();
/*  65 */     setTitle(this.title);
/*  66 */     setMessage(this.message);
/*     */   }
/*     */   
/*     */ 
/*     */   protected Control createDialogArea(Composite parent)
/*     */   {
/*  72 */     Control c = super.createDialogArea(parent);
/*  73 */     createContentPane(parent);
/*  74 */     setTitleImage(AbstractUIPlugin.imageDescriptorFromPlugin("sexftp", "/icons/Twitter bird.ico").createImage());
/*  75 */     createLoginControls();
/*  76 */     return c; }
/*     */   
/*  78 */   Composite contentPane = null;
/*     */   
/*     */   private void createContentPane(Composite parent) {
/*  81 */     this.contentPane = new Composite(parent, 0);
/*  82 */     GridLayout layout = new GridLayout(1, false);
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
/*  94 */     this.contentPane.setLayout(layout);
/*     */     
/*     */ 
/*     */ 
/*  98 */     this.contentPane.setLayoutData(new GridData(1808));
/*     */     
/* 100 */     this.contentPane.setFont(parent.getFont());
/*     */   }
/*     */   
/*     */   private void createLoginControls()
/*     */   {
/* 105 */     org.eclipse.swt.widgets.List list = new org.eclipse.swt.widgets.List(this.contentPane, 2816);
/*     */     
/* 107 */     list.setSize(800, 300);
/* 108 */     for (Object item : this.okFtpUploadConfList) {
/* 109 */       list.add(((Tosimpleable)item).toSimpleString(60));
/*     */     }
/* 111 */     GridData gridData = new GridData(768);
/*     */     
/*     */ 
/* 114 */     gridData.heightHint = 300;
/* 115 */     gridData.widthHint = 800;
/* 116 */     list.setLayoutData(gridData);
/* 117 */     Text te = new Text(this.contentPane, 2048);
/* 118 */     gridData = new GridData(768);
/*     */     
/*     */ 
/* 121 */     gridData.heightHint = 35;
/* 122 */     gridData.widthHint = 800;
/* 123 */     te.setLayoutData(gridData);
/* 124 */     te.setVisible(false);
/*     */   }
/*     */   
/*     */ 
/*     */   protected void createButtonsForButtonBar(Composite parent)
/*     */   {
/* 130 */     super.createButtonsForButtonBar(parent);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void printSimple(String info)
/*     */   {
/* 138 */     console(info);
/*     */   }
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
/* 150 */   private MessageConsole console = null;
/* 151 */   private MessageConsoleStream consoleStream = null;
/*     */   
/*     */   public void printStreamString(FtpUploadConf ftpUploadConf, long uploadedSize, long totalSize, String info) {}
/*     */   
/* 155 */   protected void initConsole() { if (this.console == null)
/*     */     {
/*     */ 
/* 158 */       this.console = new MessageConsole("SexFtpConsole", null);
/*     */       
/*     */ 
/* 161 */       ConsolePlugin.getDefault().getConsoleManager().addConsoles(
/* 162 */         new IConsole[] { this.console });
/*     */       
/*     */ 
/* 165 */       this.consoleStream = this.console.newMessageStream();
/*     */     }
/*     */   }
/*     */   
/*     */   protected void openConsole() {
/* 170 */     initConsole();
/*     */     
/* 172 */     ConsolePlugin.getDefault().getConsoleManager().showConsoleView(this.console);
/*     */   }
/*     */   
/*     */ 
/*     */   protected void console(String text)
/*     */   {
/* 178 */     this.consoleStream.println(text);
/*     */   }
/*     */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\sexftp\views\UploadConfirmDialog.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */