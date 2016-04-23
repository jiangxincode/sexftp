/*     */ package sexftp.editors;
/*     */ 
/*     */ import java.io.StringWriter;
/*     */ import java.text.Collator;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.StringTokenizer;
/*     */ import org.eclipse.core.resources.IFile;
/*     */ import org.eclipse.core.resources.IMarker;
/*     */ import org.eclipse.core.resources.IProject;
/*     */ import org.eclipse.core.resources.IResourceChangeEvent;
/*     */ import org.eclipse.core.resources.IResourceChangeListener;
/*     */ import org.eclipse.core.resources.IWorkspace;
/*     */ import org.eclipse.core.resources.ResourcesPlugin;
/*     */ import org.eclipse.core.runtime.IProgressMonitor;
/*     */ import org.eclipse.jface.dialogs.ErrorDialog;
/*     */ import org.eclipse.jface.text.IDocument;
/*     */ import org.eclipse.swt.custom.StyledText;
/*     */ import org.eclipse.swt.events.SelectionAdapter;
/*     */ import org.eclipse.swt.events.SelectionEvent;
/*     */ import org.eclipse.swt.graphics.Font;
/*     */ import org.eclipse.swt.graphics.FontData;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Button;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Display;
/*     */ import org.eclipse.swt.widgets.FontDialog;
/*     */ import org.eclipse.ui.IEditorInput;
/*     */ import org.eclipse.ui.IEditorPart;
/*     */ import org.eclipse.ui.IEditorSite;
/*     */ import org.eclipse.ui.IFileEditorInput;
/*     */ import org.eclipse.ui.IWorkbenchPage;
/*     */ import org.eclipse.ui.IWorkbenchPartSite;
/*     */ import org.eclipse.ui.IWorkbenchWindow;
/*     */ import org.eclipse.ui.PartInitException;
/*     */ import org.eclipse.ui.editors.text.TextEditor;
/*     */ import org.eclipse.ui.ide.IDE;
/*     */ import org.eclipse.ui.part.FileEditorInput;
/*     */ import org.eclipse.ui.part.MultiPageEditorPart;
/*     */ import org.eclipse.ui.texteditor.IDocumentProvider;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SexftpMultiPageEditor
/*     */   extends MultiPageEditorPart
/*     */   implements IResourceChangeListener
/*     */ {
/*     */   private TextEditor editor;
/*     */   private Font font;
/*     */   private StyledText text;
/*     */   
/*     */   public SexftpMultiPageEditor()
/*     */   {
/*  61 */     ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
/*     */   }
/*     */   
/*     */ 
/*     */   void createPage0()
/*     */   {
/*     */     try
/*     */     {
/*  69 */       this.editor = new TextEditor();
/*  70 */       int index = addPage(this.editor, getEditorInput());
/*  71 */       setPageText(index, this.editor.getTitle());
/*     */     } catch (PartInitException e) {
/*  73 */       ErrorDialog.openError(
/*  74 */         getSite().getShell(), 
/*  75 */         "Error creating nested text editor", 
/*  76 */         null, 
/*  77 */         e.getStatus());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   void createPage1()
/*     */   {
/*  86 */     Composite composite = new Composite(getContainer(), 0);
/*  87 */     GridLayout layout = new GridLayout();
/*  88 */     composite.setLayout(layout);
/*  89 */     layout.numColumns = 2;
/*     */     
/*  91 */     Button fontButton = new Button(composite, 0);
/*  92 */     GridData gd = new GridData(1);
/*  93 */     gd.horizontalSpan = 2;
/*  94 */     fontButton.setLayoutData(gd);
/*  95 */     fontButton.setText("Change Font...");
/*     */     
/*  97 */     fontButton.addSelectionListener(new SelectionAdapter() {
/*     */       public void widgetSelected(SelectionEvent event) {
/*  99 */         SexftpMultiPageEditor.this.setFont();
/*     */       }
/*     */       
/* 102 */     });
/* 103 */     int index = addPage(composite);
/* 104 */     setPageText(index, "Properties");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   void createPage2()
/*     */   {
/* 112 */     Composite composite = new Composite(getContainer(), 0);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 119 */     int index = addPage(composite);
/* 120 */     setPageText(index, "EncodingConfig");
/*     */   }
/*     */   
/*     */ 
/*     */   protected void createPages()
/*     */   {
/* 126 */     createPage0();
/* 127 */     createPage1();
/* 128 */     createPage2();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void dispose()
/*     */   {
/* 136 */     ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
/* 137 */     super.dispose();
/*     */   }
/*     */   
/*     */ 
/*     */   public void doSave(IProgressMonitor monitor)
/*     */   {
/* 143 */     getEditor(0).doSave(monitor);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void doSaveAs()
/*     */   {
/* 151 */     IEditorPart editor = getEditor(0);
/* 152 */     editor.doSaveAs();
/* 153 */     setPageText(0, editor.getTitle());
/* 154 */     setInput(editor.getEditorInput());
/*     */   }
/*     */   
/*     */ 
/*     */   public void gotoMarker(IMarker marker)
/*     */   {
/* 160 */     setActivePage(0);
/* 161 */     IDE.gotoMarker(getEditor(0), marker);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void init(IEditorSite site, IEditorInput editorInput)
/*     */     throws PartInitException
/*     */   {
/* 169 */     if (!(editorInput instanceof IFileEditorInput))
/* 170 */       throw new PartInitException("Invalid Input: Must be IFileEditorInput");
/* 171 */     super.init(site, editorInput);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isSaveAsAllowed()
/*     */   {
/* 177 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   protected void pageChange(int newPageIndex)
/*     */   {
/* 183 */     super.pageChange(newPageIndex);
/* 184 */     if (newPageIndex == 2) {
/* 185 */       sortWords();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void resourceChanged(final IResourceChangeEvent event)
/*     */   {
/* 192 */     if (event.getType() == 2) {
/* 193 */       Display.getDefault().asyncExec(new Runnable() {
/*     */         public void run() {
/* 195 */           IWorkbenchPage[] pages = SexftpMultiPageEditor.this.getSite().getWorkbenchWindow().getPages();
/* 196 */           for (int i = 0; i < pages.length; i++) {
/* 197 */             if (((FileEditorInput)SexftpMultiPageEditor.this.editor.getEditorInput()).getFile().getProject().equals(event.getResource())) {
/* 198 */               IEditorPart editorPart = pages[i].findEditor(SexftpMultiPageEditor.this.editor.getEditorInput());
/* 199 */               pages[i].closeEditor(editorPart, true);
/*     */             }
/*     */           }
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   void setFont()
/*     */   {
/* 210 */     FontDialog fontDialog = new FontDialog(getSite().getShell());
/* 211 */     fontDialog.setFontList(this.text.getFont().getFontData());
/* 212 */     FontData fontData = fontDialog.open();
/* 213 */     if (fontData != null) {
/* 214 */       if (this.font != null)
/* 215 */         this.font.dispose();
/* 216 */       this.font = new Font(this.text.getDisplay(), fontData);
/* 217 */       this.text.setFont(this.font);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   void sortWords()
/*     */   {
/* 225 */     String editorText = 
/* 226 */       this.editor.getDocumentProvider().getDocument(this.editor.getEditorInput()).get();
/*     */     
/* 228 */     StringTokenizer tokenizer = 
/* 229 */       new StringTokenizer(editorText, " \t\n\r\f!@#$%^&*()-_=+`~[]{};:'\",.<>/?|\\");
/* 230 */     ArrayList editorWords = new ArrayList();
/* 231 */     while (tokenizer.hasMoreTokens()) {
/* 232 */       editorWords.add(tokenizer.nextToken());
/*     */     }
/*     */     
/* 235 */     Collections.sort(editorWords, Collator.getInstance());
/* 236 */     StringWriter displayText = new StringWriter();
/* 237 */     for (int i = 0; i < editorWords.size(); i++) {
/* 238 */       displayText.write((String)editorWords.get(i));
/* 239 */       displayText.write(System.getProperty("line.separator"));
/*     */     }
/* 241 */     this.text.setText(displayText.toString());
/*     */   }
/*     */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\sexftp\editors\SexftpMultiPageEditor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */