/*     */ package sexftp.views;
/*     */ 
/*     */ import java.util.Map;
/*     */ import org.eclipse.jface.dialogs.TitleAreaDialog;
/*     */ import org.eclipse.jface.resource.ImageDescriptor;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Control;
/*     */ import org.eclipse.swt.widgets.Label;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ import org.eclipse.swt.widgets.Text;
/*     */ import org.eclipse.ui.plugin.AbstractUIPlugin;
/*     */ import org.sexftp.core.utils.StringUtil;
/*     */ import sexftp.uils.LangUtil;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ConfirmSexftpEncoderDialog
/*     */   extends TitleAreaDialog
/*     */ {
/*     */   private String FileAssociations;
/*     */   private String FileNoAssociations;
/*     */   private String path;
/*     */   
/*     */   public ConfirmSexftpEncoderDialog(Shell parentShell, String path, String fileAssociations, String fileNoAssociations)
/*     */   {
/*  53 */     super(parentShell);
/*  54 */     this.FileAssociations = fileAssociations;
/*  55 */     this.FileNoAssociations = fileNoAssociations;
/*  56 */     this.path = path;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Control createDialogArea(Composite parent)
/*     */   {
/*  64 */     setTitleImage(AbstractUIPlugin.imageDescriptorFromPlugin("sexftp", "/icons/Orange forum.ico").createImage());
/*     */     
/*  66 */     Composite composite = (Composite)
/*     */     
/*  68 */       super.createDialogArea(parent);
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
/*     */ 
/*  82 */     setTitle(LangUtil.langText("Chose File Associations in [" + StringUtil.simpString(this.path, 30) + "] Which is Text File."));
/*     */     
/*     */ 
/*     */ 
/*  86 */     setMessage(LangUtil.langText("Only Anyasis And Handle These File Associations.\r\nMatch Ways:Match File Name,? = any character,* = any string."));
/*     */     
/*     */ 
/*     */ 
/*  90 */     createContentPane(composite);
/*     */     
/*  92 */     createLoginControls();
/*     */     
/*  94 */     return composite;
/*     */   }
/*     */   
/*     */ 
/*  98 */   Composite contentPane = null;
/*  99 */   Map<String, String> FTP_MAP = null;
/*     */   
/*     */   private void createContentPane(Composite parent) {
/* 102 */     this.contentPane = new Composite(parent, 0);
/*     */     
/*     */ 
/*     */ 
/* 106 */     GridLayout layout = new GridLayout(3, false);
/*     */     
/*     */ 
/*     */ 
/* 110 */     layout.marginHeight = 20;
/*     */     
/* 112 */     layout.marginWidth = 70;
/*     */     
/* 114 */     layout.verticalSpacing = 10;
/*     */     
/* 116 */     layout.horizontalSpacing = 10;
/*     */     
/*     */ 
/* 119 */     this.contentPane.setLayout(layout);
/*     */     
/*     */ 
/*     */ 
/* 123 */     this.contentPane.setLayoutData(new GridData(1808));
/*     */     
/* 125 */     this.contentPane.setFont(parent.getFont());
/*     */   }
/*     */   
/* 128 */   Text FileAssociationsText = null;
/* 129 */   Text FileNoAssociationsText = null;
/*     */   
/*     */ 
/*     */ 
/*     */   private void createLoginControls()
/*     */   {
/* 135 */     this.FileAssociationsText = createText("Text File Associations", "Only Anyasis And Handle These File Associations\r\nMatch File Name:? = any character,* = any string");
/* 136 */     this.FileAssociationsText.setText(this.FileAssociations);
/*     */     
/* 138 */     this.FileNoAssociationsText = createText("Other File Associations", "Other Unknown Type File Associations Show You For reference\r\n,If Need Anyasis And Handle These File Associations\r\nAdd Them To {Text File Associations}.");
/* 139 */     this.FileNoAssociationsText.setText(this.FileNoAssociations);
/* 140 */     this.FileNoAssociationsText.setEditable(false);
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
/*     */   private Text createText(String label, String tips)
/*     */   {
/* 153 */     Label user = new Label(this.contentPane, 0);
/*     */     
/*     */ 
/*     */ 
/* 157 */     GridData layoutData = new GridData(128);
/*     */     
/* 159 */     user.setLayoutData(layoutData);
/*     */     
/* 161 */     user.setText(LangUtil.langText(label) + ":");
/*     */     
/*     */ 
/*     */ 
/* 165 */     user.setToolTipText(LangUtil.langText(tips));
/*     */     
/*     */ 
/*     */ 
/* 169 */     Text userText = new Text(this.contentPane, 18946);
/*     */     
/*     */ 
/*     */ 
/* 173 */     layoutData = new GridData(768);
/*     */     
/*     */ 
/* 176 */     layoutData.heightHint = 80;
/* 177 */     layoutData.widthHint = 300;
/*     */     
/*     */ 
/* 180 */     layoutData.horizontalSpan = 2;
/*     */     
/* 182 */     userText.setLayoutData(layoutData);
/*     */     
/* 184 */     userText.setToolTipText(LangUtil.langText(tips));
/* 185 */     return userText;
/*     */   }
/*     */   
/*     */   protected void configureShell(Shell newShell) {
/* 189 */     super.configureShell(newShell);
/*     */     
/* 191 */     newShell.setText(LangUtil.langText("Confirm File Associations"));
/*     */   }
/*     */   
/*     */ 
/*     */   protected void okPressed()
/*     */   {
/* 197 */     this.FileAssociations = this.FileAssociationsText.getText();
/* 198 */     this.FileNoAssociations = this.FileNoAssociationsText.getText();
/* 199 */     super.okPressed();
/*     */   }
/*     */   
/*     */   public String getFileAssociations() {
/* 203 */     return this.FileAssociations;
/*     */   }
/*     */   
/*     */   public void setFileAssociations(String fileAssociations) {
/* 207 */     this.FileAssociations = fileAssociations;
/*     */   }
/*     */   
/*     */   public String getFileNoAssociations() {
/* 211 */     return this.FileNoAssociations;
/*     */   }
/*     */   
/*     */   public void setFileNoAssociations(String fileNoAssociations) {
/* 215 */     this.FileNoAssociations = fileNoAssociations;
/*     */   }
/*     */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\sexftp\views\ConfirmSexftpEncoderDialog.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */