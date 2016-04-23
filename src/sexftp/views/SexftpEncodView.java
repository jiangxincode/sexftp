/*     */ package sexftp.views;
/*     */ 
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.datatransfer.Clipboard;
/*     */ import java.awt.datatransfer.StringSelection;
/*     */ import java.awt.datatransfer.Transferable;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import org.eclipse.core.resources.IFile;
/*     */ import org.eclipse.core.resources.IProject;
/*     */ import org.eclipse.core.runtime.CoreException;
/*     */ import org.eclipse.core.runtime.IPath;
/*     */ import org.eclipse.core.runtime.IProgressMonitor;
/*     */ import org.eclipse.core.runtime.IStatus;
/*     */ import org.eclipse.core.runtime.Status;
/*     */ import org.eclipse.core.runtime.content.IContentType;
/*     */ import org.eclipse.core.runtime.jobs.Job;
/*     */ import org.eclipse.jface.action.Action;
/*     */ import org.eclipse.jface.viewers.ISelection;
/*     */ import org.eclipse.jface.viewers.IStructuredSelection;
/*     */ import org.eclipse.jface.viewers.TreeViewer;
/*     */ import org.eclipse.swt.widgets.Control;
/*     */ import org.eclipse.swt.widgets.DirectoryDialog;
/*     */ import org.eclipse.swt.widgets.Display;
/*     */ import org.eclipse.swt.widgets.Tree;
/*     */ import org.eclipse.ui.ide.IDE;
/*     */ import org.sexftp.core.exceptions.AbortException;
/*     */ import org.sexftp.core.exceptions.BizException;
/*     */ import org.sexftp.core.ftp.bean.FtpUploadConf;
/*     */ import org.sexftp.core.ftp.bean.FtpUploadPro;
/*     */ import org.sexftp.core.utils.ByteUtils;
/*     */ import org.sexftp.core.utils.Cpdetector;
/*     */ import org.sexftp.core.utils.FileUtil;
/*     */ import org.sexftp.core.utils.StringUtil;
/*     */ import sexftp.SexftpJob;
/*     */ import sexftp.SexftpRun;
/*     */ import sexftp.SrcViewable;
/*     */ import sexftp.uils.PluginUtil;
/*     */ import sexftp.uils.PluginUtil.RunAsDisplayThread;
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
/*     */ public class SexftpEncodView
/*     */   extends AbstractSexftpEncodView
/*     */ {
/*     */   private String getProjectPath(IProject prject)
/*     */   {
/*  75 */     if (prject == null) return null;
/*  76 */     return prject.getFile("a.text").getLocation().toFile().getParentFile().getAbsolutePath(); }
/*     */   
/*  78 */   private static Set<String> innerTextSet = new HashSet();
/*     */   
/*  80 */   static { innerTextSet.addAll(Arrays.asList(new String[] {
/*  81 */       ".java", ".txt", ".jsp", ".htm", ".html", ".shtm", ".shtml", ".asp", ".php", ".aspx", ".properties", 
/*  82 */       ".bat", ".sh", ".css", ".js", ".sql" }));
/*     */   }
/*     */   
/*     */   private boolean isText(File sfile, IProject iproject)
/*     */     throws CoreException
/*     */   {
/*  88 */     int dotindex = sfile.getName().lastIndexOf(".");
/*  89 */     if (dotindex < 0) return false;
/*  90 */     String substring = sfile.getName().substring(dotindex);
/*  91 */     if (innerTextSet.contains(substring))
/*     */     {
/*  93 */       return true;
/*     */     }
/*  95 */     IFile ifiletype = iproject.getFile("/types/t" + substring);
/*  96 */     File ftp = ifiletype.getLocation().toFile().getParentFile();
/*  97 */     File ft = ifiletype.getLocation().toFile();
/*  98 */     if (!ftp.exists()) {
/*  99 */       ftp.mkdirs();
/*     */     }
/*     */     
/* 102 */     if (!ft.exists())
/*     */     {
/* 104 */       FileUtil.writeByte2File(ft.getAbsolutePath(), new byte[1]);
/* 105 */       ifiletype.refreshLocal(1, null);
/*     */     }
/*     */     
/* 108 */     IContentType contentType = IDE.getContentType(ifiletype);
/* 109 */     if ((contentType != null) && (contentType.getBaseType() != null))
/*     */     {
/* 111 */       String basename = contentType.getBaseType().getName();
/* 112 */       if ("text".equalsIgnoreCase(basename))
/*     */       {
/* 114 */         return true;
/*     */       }
/*     */     }
/* 117 */     return false;
/*     */   }
/*     */   
/*     */   private String asssetToString(Set<String> fileassoSet)
/*     */   {
/* 122 */     StringBuffer sb = new StringBuffer();
/* 123 */     for (String ass : fileassoSet)
/*     */     {
/* 125 */       if (ass.trim().length() > 0)
/* 126 */         sb.append("*" + ass.trim());
/* 127 */       sb.append("\r\n");
/*     */     }
/* 129 */     return sb.toString().trim();
/*     */   }
/*     */   
/*     */   private void stringToasset(String assos, Set<String> fileassoSet) {
/* 133 */     fileassoSet.clear();
/* 134 */     String[] arrayOfString; int j = (arrayOfString = assos.split("\n")).length; for (int i = 0; i < j; i++) { String ass = arrayOfString[i];
/*     */       
/* 136 */       if (ass.trim().length() > 0)
/* 137 */         fileassoSet.add(ass.trim());
/*     */     }
/*     */   }
/*     */   
/* 141 */   Set<String> fileassoSet = null;
/* 142 */   String path = null;
/*     */   
/*     */   public void checkAndView(String path) {
/* 145 */     checkAndView(path, null);
/*     */   }
/*     */   
/*     */   public void checkAndView(String path, final Set<String> deffileassoSet) {
/* 149 */     this.path = path;
/* 150 */     final List<AbstractSexftpEncodView.CorCod> corcodeList = new ArrayList();
/* 151 */     final File file = new File(path);
/* 152 */     final IProject iproject = PluginUtil.createSexftpIFileFromPath("/filetype/a.t").getProject();
/*     */     
/*     */ 
/* 155 */     final List<File> fileList = new ArrayList();
/*     */     
/* 157 */     Job job = new SexftpJob("Anyasising", this)
/*     */     {
/*     */       protected IStatus srun(IProgressMonitor monitor) throws Exception {
/* 160 */         SexftpEncodView.this.viewSubFile(file, fileList, monitor);
/* 161 */         monitor.beginTask("Anyasising", fileList.size());
/* 162 */         final Set<String> fileassoSet = deffileassoSet != null ? deffileassoSet : new LinkedHashSet();
/* 163 */         fileassoSet.add(".txt");
/* 164 */         fileassoSet.add(".java");
/* 165 */         final Set<String> ignorefileassoSet = new LinkedHashSet();
/*     */         
/* 167 */         if (fileList.size() == 1)
/*     */         {
/* 169 */           fileassoSet.add(((File)fileList.get(0)).getName());
/*     */         }
/* 171 */         else if ((fileList.size() > 1) && (deffileassoSet == null)) {
/*     */           String asso;
/* 173 */           for (File sfile : fileList) {
/* 174 */             if (monitor.isCanceled()) throw new AbortException();
/* 175 */             monitor.subTask("Anyasising " + sfile.getAbsolutePath());
/* 176 */             index = sfile.getName().lastIndexOf(".");
/* 177 */             asso = index >= 0 ? sfile.getName().substring(index) : "";
/* 178 */             if (asso.length() != 0)
/*     */             {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 184 */               boolean istext = SexftpEncodView.this.isText(sfile, iproject);
/* 185 */               if (istext)
/*     */               {
/* 187 */                 fileassoSet.add(asso);
/*     */               }
/*     */               else
/*     */               {
/* 191 */                 ignorefileassoSet.add(asso);
/*     */               }
/*     */             }
/*     */           }
/* 195 */           int index = (asso = (String[])ignorefileassoSet.toArray(new String[0])).length; for (int i = 0; i < index; i++) { String ass = asso[i];
/*     */             
/* 197 */             IFile ifiletype = iproject.getFile("/types/t" + ass);
/* 198 */             File ftp = ifiletype.getLocation().toFile().getParentFile();
/* 199 */             File ft = ifiletype.getLocation().toFile();
/* 200 */             if (!ftp.exists()) {
/* 201 */               ftp.mkdirs();
/*     */             }
/*     */             
/* 204 */             if (!ft.exists())
/*     */             {
/* 206 */               FileUtil.writeByte2File(ft.getAbsolutePath(), new byte[1]);
/* 207 */               ifiletype.refreshLocal(1, null);
/*     */             }
/* 209 */             IContentType contentType = IDE.getContentType(ifiletype);
/* 210 */             if (contentType != null)
/*     */             {
/* 212 */               ignorefileassoSet.remove(ass);
/* 213 */               ignorefileassoSet.add(ass);
/*     */             }
/*     */           }
/* 216 */           PluginUtil.runAsDisplayThread(new PluginUtil.RunAsDisplayThread() {
/*     */             public Object run() throws Exception {
/* 218 */               ConfirmSexftpEncoderDialog d = new ConfirmSexftpEncoderDialog(SexftpEncodView.this.viewer.getTree().getShell(), this.val$file.getAbsolutePath(), SexftpEncodView.this.asssetToString(fileassoSet), SexftpEncodView.this.asssetToString(ignorefileassoSet));
/* 219 */               int r = d.open();
/* 220 */               if (r != 0) throw new AbortException();
/* 221 */               SexftpEncodView.this.stringToasset(d.getFileAssociations(), fileassoSet);
/* 222 */               SexftpEncodView.this.stringToasset(d.getFileNoAssociations(), ignorefileassoSet);
/* 223 */               return null;
/*     */             }
/*     */           });
/*     */         }
/* 227 */         SexftpEncodView.this.fileassoSet = fileassoSet;
/* 228 */         for (File sfile : fileList) {
/* 229 */           monitor.subTask("Anyasising " + sfile.getAbsolutePath());
/* 230 */           AbstractSexftpEncodView.CorCod corCod = new AbstractSexftpEncodView.CorCod(SexftpEncodView.this, "");
/* 231 */           corCod.setParentFolder(file.getAbsolutePath());
/* 232 */           corCod.setEndexten(sfile.getAbsolutePath());
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
/* 254 */           boolean oktext = StringUtil.fileStyleMatch(sfile.getName(), (String[])fileassoSet.toArray(new String[0]));
/* 255 */           if (oktext)
/*     */           {
/*     */ 
/* 258 */             String encode = null;
/* 259 */             FileInputStream ascfilinput = new FileInputStream(sfile);
/*     */             
/* 261 */             if (sfile.length() < 10000000L)
/*     */             {
/* 263 */               byte[] ascdatas = FileUtil.readBytesFromInStream(ascfilinput);
/* 264 */               String isallasc = Cpdetector.isOnlyASC(ascdatas);
/* 265 */               if (isallasc.length() > 0)
/*     */               {
/* 267 */                 if (isallasc.equals("US-ASCII"))
/*     */                 {
/* 269 */                   encode = "US-ASCII";
/* 270 */                   corCod.setChengeDes("ASCII Text File Do not Need Change Charset Encoding.");
/* 271 */                   corCod.setDescIcon("stop.gif");
/*     */                 }
/* 273 */                 else if (isallasc.startsWith("US-ASCII_"))
/*     */                 {
/* 275 */                   encode = isallasc.replace("US-ASCII_", "");
/* 276 */                   corCod.setChengeDes("ASCII File,Only Use SBC case!");
/* 277 */                   corCod.setDescIcon("hprio_tsk.gif");
/*     */                 }
/*     */               }
/* 280 */               if (encode == null)
/*     */               {
/* 282 */                 Charset c = Cpdetector.encode(new FileInputStream(sfile));
/* 283 */                 encode = c != null ? c.toString() : null;
/*     */               }
/*     */               
/*     */ 
/* 287 */               if ((encode != null) && (!"void".equals(encode)))
/*     */               {
/* 289 */                 if (encode.startsWith("UTF-16"))
/*     */                 {
/* 291 */                   corCod.setChengeDes("You Must Check Samples!");
/* 292 */                   corCod.setDescIcon("hprio_tsk.gif");
/*     */                 }
/* 294 */                 byte[] newdata = Cpdetector.delASCIIdata(ascdatas);
/* 295 */                 if (("x-EUC-TW".equalsIgnoreCase(encode)) || ("windows-1252".equalsIgnoreCase(encode)) || ("EUC-KR".equalsIgnoreCase(encode)))
/*     */                 {
/* 297 */                   Charset c = Cpdetector.encode(new ByteArrayInputStream(newdata));
/* 298 */                   String newcode = c != null ? c.toString() : null;
/* 299 */                   if (newcode != null)
/*     */                   {
/* 301 */                     if (newcode.startsWith("GB"))
/*     */                     {
/* 303 */                       corCod.setChengeDes("Need Check [" + newcode + " or " + encode + "]  Samples");
/* 304 */                       encode = newcode;
/*     */                     }
/*     */                     else
/*     */                     {
/* 308 */                       encode = "GB18030";
/* 309 */                       corCod.setChengeDes("We Just Guess,You Must Check  Samples!");
/*     */                     }
/* 311 */                     corCod.setDescIcon("hprio_tsk.gif");
/*     */                   }
/*     */                 }
/*     */                 
/* 315 */                 corCod.setFileencode(encode);
/* 316 */                 String sample = Cpdetector.onlyNoneASCII(new String(ascdatas, encode));
/*     */                 
/* 318 */                 if (sample.length() > 50)
/*     */                 {
/* 320 */                   sample = sample.substring(0, 50) + "...";
/*     */                 }
/* 322 */                 corCod.setSamples(sample);
/*     */               }
/*     */               else
/*     */               {
/* 326 */                 corCod.setFileencode("<Unkown>");
/* 327 */                 corCod.setDescIcon("hprio_tsk.gif");
/*     */               }
/*     */             }
/*     */             else
/*     */             {
/* 332 */               corCod.setFileencode("File Too Large!");
/* 333 */               corCod.setEclipseeditorencode("File Too Large!");
/* 334 */               corCod.setDescIcon("stop.gif");
/* 335 */               continue;
/*     */             }
/*     */             
/*     */           }
/*     */           else
/*     */           {
/* 341 */             corCod.setFileencode("");
/* 342 */             corCod.setDescIcon("hprio_tsk.gif");
/* 343 */             continue;
/*     */           }
/*     */           
/* 346 */           corCod.setOldfileencode(corCod.getFileencode());
/* 347 */           corcodeList.add(corCod);
/* 348 */           monitor.worked(1);
/*     */         }
/* 350 */         SexftpEncodView.this.refreshData(SexftpEncodView.this.comin(corcodeList));
/* 351 */         SexftpEncodView.this.applyChanges.setEnabled(true);
/*     */         
/* 353 */         return Status.OK_STATUS;
/*     */       }
/* 355 */     };
/* 356 */     job.setUser(true);
/* 357 */     job.schedule();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private List<AbstractSexftpEncodView.ParentCorCod> comin(List<AbstractSexftpEncodView.CorCod> corcodeList)
/*     */   {
/* 364 */     Map<AbstractSexftpEncodView.ParentCorCod, AbstractSexftpEncodView.ParentCorCod> okMap = new HashMap();
/* 365 */     for (AbstractSexftpEncodView.CorCod corCod : corcodeList) {
/* 366 */       String path = corCod.getEndexten();
/* 367 */       if (path.endsWith("odi-table.js"))
/*     */       {
/* 369 */         System.out.println("");
/*     */       }
/* 371 */       path = path.replace('\\', '/');
/* 372 */       int findex = path.lastIndexOf("/");
/* 373 */       int ptIndex = path.lastIndexOf(".");
/* 374 */       if (ptIndex > findex)
/*     */       {
/* 376 */         AbstractSexftpEncodView.ParentCorCod key = new AbstractSexftpEncodView.ParentCorCod(this, "");
/* 377 */         key.setEndexten("*" + path.substring(ptIndex));
/* 378 */         key.setFileencode(corCod.getFileencode());
/* 379 */         key.setEclipseeditorencode(corCod.getEclipseeditorencode());
/* 380 */         key.setParentFolder(corCod.getParentFolder());
/* 381 */         key.setOldfileencode(corCod.getOldfileencode());
/* 382 */         key.setChengeDes(corCod.getChengeDes());
/* 383 */         key.setDescIcon(corCod.getDescIcon());
/* 384 */         if (!okMap.containsKey(key))
/*     */         {
/*     */ 
/* 387 */           okMap.put(key, key);
/*     */         }
/*     */         
/* 390 */         hindex(hash(key.hashCode()), 16);
/* 391 */         ((AbstractSexftpEncodView.ParentCorCod)okMap.get(key)).addChild(corCod);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 397 */     for (AbstractSexftpEncodView.ParentCorCod p : okMap.keySet())
/*     */     {
/* 399 */       int len = p.getChildren().length;
/* 400 */       int index = new Random().nextInt(len);
/* 401 */       p.setSamples(p.getChildren()[index].getSamples());
/*     */     }
/* 403 */     return new ArrayList(okMap.keySet());
/*     */   }
/*     */   
/*     */   private int hash(int h)
/*     */   {
/* 408 */     h ^= h >>> 20 ^ h >>> 12;
/* 409 */     return h ^ h >>> 7 ^ h >>> 4;
/*     */   }
/*     */   
/*     */   private int hindex(int h, int length) {
/* 413 */     return h & length - 1;
/*     */   }
/*     */   
/*     */   private void viewSubFile(File file, List<File> fileList, IProgressMonitor monitor) {
/* 417 */     if (monitor.isCanceled())
/*     */     {
/* 419 */       throw new AbortException();
/*     */     }
/* 421 */     if (file.isDirectory())
/*     */     {
/* 423 */       monitor.subTask("Scaning.. " + file.getAbsolutePath());
/* 424 */       File[] arrayOfFile; int j = (arrayOfFile = file.listFiles()).length; for (int i = 0; i < j; i++) { File subfile = arrayOfFile[i];
/*     */         
/* 426 */         viewSubFile(subfile, fileList, monitor);
/*     */       }
/*     */     }
/* 429 */     else if (file.isFile())
/*     */     {
/* 431 */       fileList.add(file);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void oldEncodeChanged(AbstractSexftpEncodView.CorCod c, String encode, final String newencode)
/*     */   {
/* 439 */     final AbstractSexftpEncodView.ParentCorCod p = (AbstractSexftpEncodView.ParentCorCod)c;
/* 440 */     if (!encode.equals(newencode))
/*     */     {
/* 442 */       if (showQuestion("Refresh Samples Use [" + newencode + "]?"))
/*     */       {
/* 444 */         Job job = new SexftpJob("Refreshing", this)
/*     */         {
/*     */           protected IStatus srun(IProgressMonitor monitor) throws Exception
/*     */           {
/* 448 */             monitor.beginTask("Refreshing", p.getChildren().length);
/* 449 */             AbstractSexftpEncodView.CorCod[] arrayOfCorCod; int j = (arrayOfCorCod = p.getChildren()).length; for (int i = 0; i < j; i++) { AbstractSexftpEncodView.CorCod co = arrayOfCorCod[i];
/*     */               
/* 451 */               monitor.subTask("refresh of " + co.getEndexten());
/* 452 */               String sample = Cpdetector.onlyNoneASCII(FileUtil.getTextFromFile(co.getEndexten(), newencode));
/* 453 */               if (sample.length() > 10)
/*     */               {
/* 455 */                 sample = sample.substring(0, 10) + "...";
/*     */               }
/* 457 */               co.setSamples(sample);
/* 458 */               monitor.worked(1);
/*     */             }
/* 460 */             return Status.OK_STATUS;
/*     */           }
/* 462 */         };
/* 463 */         job.setUser(true);
/* 464 */         job.schedule();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void applyChanges_actionPerformed() throws Exception
/*     */   {
/* 471 */     getActPage();
/* 472 */     applyEncodeChange();
/*     */   }
/*     */   
/*     */   public void applyEncodeChange()
/*     */   {
/* 477 */     Job job = new SexftpJob("Anyasising", this)
/*     */     {
/*     */       protected IStatus srun(IProgressMonitor monitor) throws Exception
/*     */       {
/* 481 */         int total = 0;
/* 482 */         for (AbstractSexftpEncodView.ParentCorCod c : SexftpEncodView.this.corcodeList)
/*     */         {
/*     */ 
/* 485 */           if (!c.getOldfileencode().equals(c.getFileencode()))
/*     */           {
/*     */             try {
/* 488 */               "test".getBytes(c.getOldfileencode());
/* 489 */               "test".getBytes(c.getFileencode());
/*     */             } catch (UnsupportedEncodingException localUnsupportedEncodingException1) {
/*     */               continue;
/*     */             }
/* 493 */             if (c.getFileencode().indexOf("ASCII") >= 0)
/*     */             {
/* 495 */               throw new BizException("Error:[" + c.getEndexten() + "] Cann't Changed To ASCII!");
/*     */             }
/*     */             
/*     */ 
/*     */ 
/*     */ 
/* 501 */             total += c.getChildren().length;
/*     */           }
/*     */         }
/* 504 */         if (total == 0)
/*     */         {
/* 506 */           throw new BizException("No Files To Change Encoding!");
/*     */         }
/* 508 */         monitor.beginTask("Change Encoding...", total);
/* 509 */         boolean changed = false;
/* 510 */         for (AbstractSexftpEncodView.ParentCorCod c : SexftpEncodView.this.corcodeList)
/*     */         {
/* 512 */           if (!c.getOldfileencode().equals(c.getFileencode()))
/*     */           {
/*     */             try {
/* 515 */               "test".getBytes(c.getOldfileencode());
/* 516 */               "test".getBytes(c.getFileencode());
/*     */             } catch (UnsupportedEncodingException localUnsupportedEncodingException2) { continue;
/*     */             }
/*     */             AbstractSexftpEncodView.CorCod[] arrayOfCorCod;
/* 520 */             int j = (arrayOfCorCod = c.getChildren()).length; for (int i = 0; i < j; i++) { AbstractSexftpEncodView.CorCod detCod = arrayOfCorCod[i];
/*     */               
/* 522 */               Cpdetector.encode(new FileInputStream(detCod.getEndexten()));
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
/* 536 */               String subtask = String.format("Change Encoding From %s to %s :%s", new Object[] { c.getOldfileencode(), c.getFileencode(), detCod.getEndexten() });
/* 537 */               monitor.subTask(subtask);
/* 538 */               SexftpEncodView.this.console(subtask);
/* 539 */               String text = FileUtil.getTextFromFile(detCod.getEndexten(), c.getOldfileencode());
/* 540 */               ByteUtils.writeByte2Stream(text.getBytes(c.getFileencode()), new FileOutputStream(detCod.getEndexten()));
/* 541 */               monitor.worked(1);
/* 542 */               changed = true;
/*     */             }
/*     */           }
/*     */         }
/* 546 */         if (changed)
/*     */         {
/* 548 */           SexftpEncodView.this.showMessage("[" + total + "] Files Charset Encoding Changed Success!");
/* 549 */           final Object fs = SexftpEncodView.this.fileassoSet;
/* 550 */           final String npath = SexftpEncodView.this.path;
/* 551 */           Display.getDefault().asyncExec(new SexftpRun(SexftpEncodView.this)
/*     */           {
/*     */             public void srun() throws Exception {
/* 554 */               SexftpEncodView.this.checkAndView(npath, fs);
/*     */             }
/* 556 */           });
/* 557 */           SexftpEncodView.this.applyChanges.setEnabled(false);
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 562 */           SexftpEncodView.this.showMessage("No Changed!");
/*     */         }
/* 564 */         SexftpEncodView.this.fileassoSet = null;
/* 565 */         return Status.OK_STATUS;
/*     */       }
/*     */       
/* 568 */     };
/* 569 */     job.setUser(true);
/* 570 */     job.schedule();
/*     */   }
/*     */   
/*     */   protected void doubleClick_actionPerformed() throws Exception
/*     */   {
/* 575 */     ISelection selection = this.viewer.getSelection();
/* 576 */     IStructuredSelection selection2 = (IStructuredSelection)selection;
/*     */     
/* 578 */     Object obj = selection2.getFirstElement();
/* 579 */     if (!(obj instanceof AbstractSexftpEncodView.ParentCorCod))
/*     */     {
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
/* 592 */       FtpUploadConf fu = new FtpUploadConf();
/* 593 */       fu.setClientPath(((AbstractSexftpEncodView.CorCod)obj).getEndexten());
/* 594 */       FtpUploadPro fpro = new FtpUploadPro(fu, null);
/* 595 */       PluginUtil.findLocalView(getActPage()).innerEditLocalFile(fpro);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void actionCopyQualifiedName_actionPerformed() throws Exception {
/* 600 */     AbstractSexftpEncodView.CorCod[] objes = getSelectionObjects();
/* 601 */     StringBuffer sb = new StringBuffer();
/* 602 */     AbstractSexftpEncodView.CorCod[] arrayOfCorCod1; int j = (arrayOfCorCod1 = objes).length; for (int i = 0; i < j; i++) { AbstractSexftpEncodView.CorCod o = arrayOfCorCod1[i];
/* 603 */       if ((o instanceof AbstractSexftpEncodView.ParentCorCod))
/*     */       {
/* 605 */         AbstractSexftpEncodView.ParentCorCod po = (AbstractSexftpEncodView.ParentCorCod)o;
/* 606 */         AbstractSexftpEncodView.CorCod[] arrayOfCorCod2; int m = (arrayOfCorCod2 = po.getChildren()).length; for (int k = 0; k < m; k++) { AbstractSexftpEncodView.CorCod corCod = arrayOfCorCod2[k];
/* 607 */           sb.append(corCod);
/* 608 */           sb.append("\r\n");
/*     */         }
/*     */       }
/*     */     }
/* 612 */     Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
/* 613 */     Object tText = new StringSelection(sb.toString().trim());
/* 614 */     systemClipboard.setContents((Transferable)tText, null);
/*     */   }
/*     */   
/*     */   protected void actionCopy_actionPerformed() throws Exception {
/* 618 */     AbstractSexftpEncodView.CorCod[] selectionObjects = getSelectionObjects();
/* 619 */     StringBuffer sb = new StringBuffer();
/* 620 */     AbstractSexftpEncodView.CorCod[] arrayOfCorCod1; int j = (arrayOfCorCod1 = selectionObjects).length; for (int i = 0; i < j; i++) { AbstractSexftpEncodView.CorCod corCod = arrayOfCorCod1[i];
/* 621 */       sb.append(corCod);
/* 622 */       sb.append("\r\n");
/*     */     }
/* 624 */     Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
/* 625 */     Object tText = new StringSelection(sb.toString().trim());
/* 626 */     systemClipboard.setContents((Transferable)tText, null);
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
/*     */   protected void filedillAction_actionPerformed()
/*     */     throws Exception
/*     */   {
/* 641 */     DirectoryDialog d = new DirectoryDialog(this.viewer.getControl().getShell());
/* 642 */     d.open();
/* 643 */     String filterPath = d.getFilterPath();
/* 644 */     if ((filterPath != null) && (filterPath.length() > 0))
/*     */     {
/* 646 */       checkAndView(filterPath);
/*     */     }
/*     */   }
/*     */   
/*     */   protected AbstractSexftpEncodView.CorCod[] getSelectionObjects()
/*     */   {
/* 652 */     List<AbstractSexftpEncodView.CorCod> r = new ArrayList();
/* 653 */     ISelection selection = this.viewer.getSelection();
/* 654 */     Iterator<AbstractSexftpEncodView.CorCod> it = ((IStructuredSelection)selection).iterator();
/* 655 */     while (it.hasNext())
/*     */     {
/* 657 */       AbstractSexftpEncodView.CorCod o = (AbstractSexftpEncodView.CorCod)it.next();
/* 658 */       r.add(o);
/*     */     }
/*     */     
/* 661 */     return (AbstractSexftpEncodView.CorCod[])r.toArray(new AbstractSexftpEncodView.CorCod[0]);
/*     */   }
/*     */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\sexftp\views\SexftpEncodView.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */