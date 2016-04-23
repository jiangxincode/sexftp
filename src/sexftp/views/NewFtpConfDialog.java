/*     */ package sexftp.views;
/*     */ 
/*     */ import com.lowagie.text.html.HtmlEncoder;
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.desy.xbean.XbeanUtil;
/*     */ import org.eclipse.jface.dialogs.TitleAreaDialog;
/*     */ import org.eclipse.jface.resource.ImageDescriptor;
/*     */ import org.eclipse.swt.events.ModifyEvent;
/*     */ import org.eclipse.swt.events.ModifyListener;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Combo;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Control;
/*     */ import org.eclipse.swt.widgets.Label;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ import org.eclipse.swt.widgets.Text;
/*     */ import org.eclipse.ui.plugin.AbstractUIPlugin;
/*     */ import org.sexftp.core.bean.FileZilla;
/*     */ import org.sexftp.core.bean.FileZillaServer;
/*     */ import org.sexftp.core.ftp.FtpPools;
/*     */ import org.sexftp.core.ftp.bean.FtpConf;
/*     */ import org.sexftp.core.utils.FileUtil;
/*     */ import sexftp.uils.LangUtil;
/*     */ import sexftp.uils.LogUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NewFtpConfDialog
/*     */   extends TitleAreaDialog
/*     */ {
/*     */   private static final String COMBO_SPT_SERVERTYPE = " - ";
/*  39 */   private String defaultConfigName = null;
/*     */   
/*  41 */   public NewFtpConfDialog(Shell parentShell, String defaultConfigName) { super(parentShell);
/*  42 */     this.defaultConfigName = defaultConfigName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Control createDialogArea(Composite parent)
/*     */   {
/*  52 */     Composite composite = (Composite)
/*     */     
/*  54 */       super.createDialogArea(parent);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  62 */     setTitleImage(AbstractUIPlugin.imageDescriptorFromPlugin("sexftp", "/icons/Twitter bird.ico").createImage());
/*     */     
/*     */ 
/*     */ 
/*  66 */     setTitle(LangUtil.langText("Generate Sexftp Upload Unit Config File"));
/*     */     
/*     */ 
/*     */ 
/*  70 */     setMessage(LangUtil.langText("Help You Generate Sexftp Upload Unit Config File Based Xml.After Save The XML File,\r\nThe Sexftp Upload Unit Config Will Show In [Sexftp Local View] and [Sexftp Server View]"));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  76 */     createContentPane(composite);
/*     */     
/*  78 */     createLoginControls();
/*     */     
/*  80 */     return composite;
/*     */   }
/*     */   
/*     */ 
/*  84 */   Composite contentPane = null;
/*  85 */   Map<String, String> FTP_MAP = null;
/*     */   
/*     */   private void createContentPane(Composite parent) {
/*  88 */     this.contentPane = new Composite(parent, 0);
/*     */     
/*     */ 
/*     */ 
/*  92 */     GridLayout layout = new GridLayout(3, false);
/*     */     
/*     */ 
/*     */ 
/*  96 */     layout.marginHeight = 20;
/*     */     
/*  98 */     layout.marginWidth = 70;
/*     */     
/* 100 */     layout.verticalSpacing = 10;
/*     */     
/* 102 */     layout.horizontalSpacing = 10;
/*     */     
/* 104 */     this.contentPane.setLayout(layout);
/*     */     
/*     */ 
/*     */ 
/* 108 */     this.contentPane.setLayoutData(new GridData(1808));
/*     */     
/* 110 */     this.contentPane.setFont(parent.getFont());
/*     */   }
/*     */   
/* 113 */   Text configFileNameText = null;
/* 114 */   Text serverHostText = null;
/* 115 */   Text serverPortText = null;
/* 116 */   Combo serverTypeCombo = null;
/* 117 */   Text passwordText = null;
/* 118 */   Text userText = null;
/* 119 */   Text clilentPathText = null;
/* 120 */   Text serverPathText = null;
/*     */   
/*     */   private void createLoginControls()
/*     */   {
/* 124 */     final FtpConf[] outerFtpconfs = getOuterFtpconfs();
/* 125 */     if (outerFtpconfs.length > 0)
/*     */     {
/* 127 */       String[] outftpconfitem = new String[outerFtpconfs.length];
/* 128 */       for (int i = 0; i < outerFtpconfs.length; i++) {
/* 129 */         outftpconfitem[i] = outerFtpconfs[i].toString();
/*     */       }
/* 131 */       final Combo impComb = createSelect("Import From", "", outftpconfitem);
/* 132 */       impComb.addModifyListener(new ModifyListener() {
/*     */         public void modifyText(ModifyEvent e) {
/* 134 */           int sel = impComb.getSelectionIndex();
/* 135 */           if (sel >= 0)
/*     */           {
/* 137 */             FtpConf ftpconfsel = outerFtpconfs[sel];
/* 138 */             NewFtpConfDialog.this.serverHostText.setText(ftpconfsel.getHost());
/* 139 */             if (ftpconfsel.getPort() != null)
/* 140 */               NewFtpConfDialog.this.serverPortText.setText(ftpconfsel.getPort());
/* 141 */             NewFtpConfDialog.this.userText.setText(ftpconfsel.getUsername());
/* 142 */             NewFtpConfDialog.this.passwordText.setText(ftpconfsel.getPassword());
/* 143 */             String serverType = ftpconfsel.getServerType();
/* 144 */             if (serverType != null)
/*     */             {
/* 146 */               NewFtpConfDialog.this.serverTypeCombo.setText(serverType + " - " + (String)NewFtpConfDialog.this.FTP_MAP.get(serverType));
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/* 151 */             impComb.select(0);
/*     */           }
/*     */         }
/*     */       });
/*     */     }
/*     */     
/*     */ 
/* 158 */     this.configFileNameText = createText("Config File Name", "");
/* 159 */     this.configFileNameText.setText(this.defaultConfigName);
/* 160 */     this.serverHostText = createText("Server Host", "");
/* 161 */     this.serverHostText.setText("localhost");
/* 162 */     this.serverPortText = createText("Server Port", "");
/* 163 */     this.serverPortText.setText("21");
/*     */     
/* 165 */     this.FTP_MAP = new LinkedHashMap();
/*     */     
/* 167 */     this.FTP_MAP.put("ftp", LangUtil.langText("FTP Transfer Protocal"));
/* 168 */     this.FTP_MAP.put("ftps", LangUtil.langText("Implicit TLS/SSL FTP"));
/* 169 */     this.FTP_MAP.put("ftpes", LangUtil.langText("Explicit TLS/SSL FTP"));
/* 170 */     this.FTP_MAP.put("sftp", LangUtil.langText("SSH File Transfer Protocal"));
/* 171 */     this.FTP_MAP.put("file", LangUtil.langText("Local File Transfer Protocal"));
/*     */     
/* 173 */     String[] arrays = (String[])FtpPools.FTP_MAP.keySet().toArray(new String[0]);
/* 174 */     for (int i = 0; i < arrays.length; i++) {
/* 175 */       arrays[i] = (arrays[i].toUpperCase() + " - " + (String)this.FTP_MAP.get(arrays[i]));
/*     */     }
/* 177 */     this.serverTypeCombo = createSelect("Server Type", "", arrays);
/* 178 */     this.serverTypeCombo.select(0);
/* 179 */     this.serverTypeCombo.addModifyListener(new ModifyListener()
/*     */     {
/*     */       public void modifyText(ModifyEvent e)
/*     */       {
/* 183 */         if (NewFtpConfDialog.this.serverTypeCombo.getSelectionIndex() < 0)
/*     */         {
/* 185 */           NewFtpConfDialog.this.serverTypeCombo.select(0);
/*     */         }
/*     */         
/*     */       }
/* 189 */     });
/* 190 */     this.userText = createText("Login User", "Login User Name");
/* 191 */     this.userText.setText("root");
/* 192 */     this.passwordText = createText("Login Password", "Login Password");
/* 193 */     this.passwordText.setText("root123");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Combo createSelect(String label, String tips, String[] items)
/*     */   {
/* 204 */     Label user = new Label(this.contentPane, 0);
/*     */     
/*     */ 
/*     */ 
/* 208 */     GridData layoutData = new GridData(128);
/*     */     
/* 210 */     user.setLayoutData(layoutData);
/*     */     
/* 212 */     user.setText(LangUtil.langText(label) + ":");
/*     */     
/*     */ 
/*     */ 
/* 216 */     user.setToolTipText(LangUtil.langText(tips));
/*     */     
/*     */ 
/* 219 */     Combo combo = new Combo(this.contentPane, 18432);
/* 220 */     combo.setItems(items);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 225 */     layoutData = new GridData(768);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 231 */     layoutData.horizontalSpan = 2;
/*     */     
/* 233 */     combo.setLayoutData(layoutData);
/*     */     
/* 235 */     combo.setToolTipText(tips);
/* 236 */     return combo;
/*     */   }
/*     */   
/*     */   private Text createText(String label, String tips)
/*     */   {
/* 241 */     Label user = new Label(this.contentPane, 0);
/*     */     
/*     */ 
/*     */ 
/* 245 */     GridData layoutData = new GridData(128);
/*     */     
/* 247 */     user.setLayoutData(layoutData);
/*     */     
/* 249 */     user.setText(LangUtil.langText(label) + ":");
/*     */     
/*     */ 
/*     */ 
/* 253 */     user.setToolTipText(LangUtil.langText(tips));
/*     */     
/*     */ 
/*     */ 
/* 257 */     Text userText = new Text(this.contentPane, 18432);
/*     */     
/*     */ 
/*     */ 
/* 261 */     layoutData = new GridData(768);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 267 */     layoutData.horizontalSpan = 2;
/*     */     
/* 269 */     userText.setLayoutData(layoutData);
/*     */     
/* 271 */     userText.setToolTipText(tips);
/* 272 */     return userText;
/*     */   }
/*     */   
/*     */   protected void configureShell(Shell newShell) {
/* 276 */     super.configureShell(newShell);
/*     */     
/* 278 */     newShell.setText(LangUtil.langText("New Sexftp Upload Unit"));
/*     */   }
/*     */   
/* 281 */   FtpConf ftpconf = new FtpConf();
/*     */   
/*     */   protected void okPressed()
/*     */   {
/* 285 */     this.ftpconf.setName(HtmlEncoder.encode(this.configFileNameText.getText().trim()));
/* 286 */     this.ftpconf.setHost(HtmlEncoder.encode(this.serverHostText.getText().trim()));
/*     */     try {
/* 288 */       this.ftpconf.setPort(Integer.valueOf(Integer.parseInt(this.serverPortText.getText().trim())));
/*     */     } catch (NumberFormatException localNumberFormatException) {
/* 290 */       this.ftpconf.setPort(Integer.valueOf(21));
/*     */     }
/* 292 */     int sel = this.serverTypeCombo.getSelectionIndex();
/* 293 */     if (sel >= 0)
/*     */     {
/* 295 */       int index = this.serverTypeCombo.getText().toLowerCase().indexOf(" - ");
/* 296 */       this.ftpconf.setServerType(this.serverTypeCombo.getText().toLowerCase().substring(0, index));
/*     */     }
/* 298 */     this.ftpconf.setPassword(HtmlEncoder.encode(this.passwordText.getText().trim()));
/* 299 */     this.ftpconf.setUsername(HtmlEncoder.encode(this.userText.getText().trim()));
/* 300 */     super.okPressed();
/*     */   }
/*     */   
/*     */   public FtpConf getFtpconf() {
/* 304 */     return this.ftpconf;
/*     */   }
/*     */   
/*     */ 
/* 308 */   public void setFtpconf(FtpConf ftpconf) { this.ftpconf = ftpconf; }
/*     */   
/*     */   public static FtpConf[] getOuterFtpconfs() {
/* 311 */     List<FtpConf> ftpconfs = new ArrayList();
/* 312 */     Set<FileZillaServer> fSet = new LinkedHashSet();
/* 313 */     String userHome = System.getProperty("user.home");
/* 314 */     if ((userHome != null) && (userHome.trim().length() > 0)) {
/*     */       String[] arrayOfString1;
/* 316 */       int j = (arrayOfString1 = new String[] { userHome }).length; for (int i = 0; i < j; i++) { String folderPath = arrayOfString1[i];
/*     */         String[] arrayOfString2;
/* 318 */         int m = (arrayOfString2 = new String[] { "\\AppData\\Roaming\\FileZilla\\sitemanager.xml", "\\application data\\FileZilla\\sitemanager.xml" }).length; for (int k = 0; k < m; k++) { String subPath = arrayOfString2[k];
/*     */           
/* 320 */           File f = new File(folderPath + subPath);
/* 321 */           if (f.exists()) {
/*     */             try
/*     */             {
/* 324 */               String xml = FileUtil.getTextFromFile(f.getAbsolutePath(), "utf-8");
/* 325 */               xml = org.sexftp.core.utils.StringUtil.split(org.sexftp.core.utils.StringUtil.split(xml, "<Servers>")[1], "</Servers>")[0];
/* 326 */               xml = "<FileZilla>" + xml + "</FileZilla>";
/* 327 */               FileZilla fiz = (FileZilla)XbeanUtil.xml2Bean(FileZilla.class, xml);
/* 328 */               FileZillaServer[] arrayOfFileZillaServer; int i1 = (arrayOfFileZillaServer = fiz.getServer()).length; for (int n = 0; n < i1; n++) { FileZillaServer serv = arrayOfFileZillaServer[n];
/*     */                 
/* 330 */                 fSet.add(serv);
/*     */               }
/*     */             } catch (Exception e) {
/* 333 */               LogUtil.error("Import Other Config Error:" + e.getMessage(), e);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 339 */     for (FileZillaServer fizserver : fSet)
/*     */     {
/* 341 */       FtpConf ftpconf = new FtpConf();
/* 342 */       ftpconf.setHost(fizserver.getHost());
/*     */       try {
/* 344 */         ftpconf.setPort(new Integer(fizserver.getPort()));
/*     */       }
/*     */       catch (NumberFormatException localNumberFormatException) {}
/*     */       
/* 348 */       ftpconf.setUsername(fizserver.getUser());
/* 349 */       ftpconf.setPassword(fizserver.getPass());
/*     */       try {
/* 351 */         ftpconf.setServerType(((String[])FtpPools.FTP_MAP.keySet().toArray(new String[0]))[new Integer(fizserver.getProtocol()).intValue()]);
/*     */       }
/*     */       catch (Exception localException1) {}
/*     */       
/* 355 */       ftpconf.setName(LangUtil.langText("From FileZilla"));
/* 356 */       ftpconfs.add(ftpconf);
/*     */     }
/*     */     
/* 359 */     return (FtpConf[])ftpconfs.toArray(new FtpConf[0]);
/*     */   }
/*     */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\sexftp\views\NewFtpConfDialog.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */