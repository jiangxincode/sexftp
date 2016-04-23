/*      */ package sexftp.views;
/*      */ 
/*      */ import com.lowagie.text.html.HtmlEncoder;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.PrintStream;
/*      */ import java.lang.reflect.Method;
/*      */ import java.net.URL;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Hashtable;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import org.eclipse.core.resources.IProject;
/*      */ import org.eclipse.core.runtime.IAdaptable;
/*      */ import org.eclipse.core.runtime.IStatus;
/*      */ import org.eclipse.core.runtime.Platform;
/*      */ import org.eclipse.core.runtime.Status;
/*      */ import org.eclipse.jface.action.Action;
/*      */ import org.eclipse.jface.action.IMenuListener;
/*      */ import org.eclipse.jface.action.IMenuManager;
/*      */ import org.eclipse.jface.action.IToolBarManager;
/*      */ import org.eclipse.jface.action.MenuManager;
/*      */ import org.eclipse.jface.action.Separator;
/*      */ import org.eclipse.jface.dialogs.ErrorDialog;
/*      */ import org.eclipse.jface.dialogs.InputDialog;
/*      */ import org.eclipse.jface.dialogs.MessageDialog;
/*      */ import org.eclipse.jface.dialogs.MessageDialogWithToggle;
/*      */ import org.eclipse.jface.preference.IPreferenceStore;
/*      */ import org.eclipse.jface.resource.ImageDescriptor;
/*      */ import org.eclipse.jface.util.IPropertyChangeListener;
/*      */ import org.eclipse.jface.viewers.DoubleClickEvent;
/*      */ import org.eclipse.jface.viewers.IColorProvider;
/*      */ import org.eclipse.jface.viewers.IDoubleClickListener;
/*      */ import org.eclipse.jface.viewers.IFontProvider;
/*      */ import org.eclipse.jface.viewers.ISelectionChangedListener;
/*      */ import org.eclipse.jface.viewers.IStructuredContentProvider;
/*      */ import org.eclipse.jface.viewers.ITreeContentProvider;
/*      */ import org.eclipse.jface.viewers.ITreeViewerListener;
/*      */ import org.eclipse.jface.viewers.LabelProvider;
/*      */ import org.eclipse.jface.viewers.SelectionChangedEvent;
/*      */ import org.eclipse.jface.viewers.TreeExpansionEvent;
/*      */ import org.eclipse.jface.viewers.TreeViewer;
/*      */ import org.eclipse.jface.viewers.Viewer;
/*      */ import org.eclipse.jface.viewers.ViewerSorter;
/*      */ import org.eclipse.osgi.service.datalocation.Location;
/*      */ import org.eclipse.swt.events.KeyEvent;
/*      */ import org.eclipse.swt.events.KeyListener;
/*      */ import org.eclipse.swt.graphics.Color;
/*      */ import org.eclipse.swt.graphics.Font;
/*      */ import org.eclipse.swt.graphics.Image;
/*      */ import org.eclipse.swt.widgets.Composite;
/*      */ import org.eclipse.swt.widgets.Control;
/*      */ import org.eclipse.swt.widgets.Display;
/*      */ import org.eclipse.swt.widgets.Menu;
/*      */ import org.eclipse.swt.widgets.Shell;
/*      */ import org.eclipse.swt.widgets.Tree;
/*      */ import org.eclipse.ui.IActionBars;
/*      */ import org.eclipse.ui.ISharedImages;
/*      */ import org.eclipse.ui.IViewSite;
/*      */ import org.eclipse.ui.IWorkbench;
/*      */ import org.eclipse.ui.IWorkbenchPage;
/*      */ import org.eclipse.ui.IWorkbenchPartSite;
/*      */ import org.eclipse.ui.PlatformUI;
/*      */ import org.eclipse.ui.help.IWorkbenchHelpSystem;
/*      */ import org.eclipse.ui.part.DrillDownAdapter;
/*      */ import org.eclipse.ui.part.ViewPart;
/*      */ import org.eclipse.ui.plugin.AbstractUIPlugin;
/*      */ import org.osgi.framework.Bundle;
/*      */ import org.sexftp.core.exceptions.AbortException;
/*      */ import org.sexftp.core.exceptions.BizException;
/*      */ import org.sexftp.core.exceptions.SRuntimeException;
/*      */ import org.sexftp.core.ftp.Consoleable;
/*      */ import org.sexftp.core.ftp.bean.FtpConf;
/*      */ import org.sexftp.core.ftp.bean.FtpUploadConf;
/*      */ import org.sexftp.core.ftp.bean.FtpUploadPro;
/*      */ import org.sexftp.core.utils.StringUtil;
/*      */ import sexftp.SrcViewable;
/*      */ import sexftp.uils.Console;
/*      */ import sexftp.uils.LangUtil;
/*      */ import sexftp.uils.LogUtil;
/*      */ import sexftp.uils.PluginUtil;
/*      */ import sexftp.uils.PluginUtil.RunAsDisplayThread;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class AbstractSexftpView
/*      */   extends ViewPart
/*      */   implements Consoleable, SrcViewable
/*      */ {
/*      */   public static final String ID = "sexftp.views.MainView";
/*  102 */   public static String workspacePath = "";
/*      */   
/*  104 */   public static String workspaceWkPath = "";
/*      */   
/*      */   protected TreeViewer viewer;
/*      */   
/*      */   private DrillDownAdapter drillDownAdapter;
/*      */   
/*      */   protected Action actionApplySexFtpConf;
/*      */   
/*      */   protected Action actionEditSexFtpConf;
/*      */   
/*      */   protected Action actionDeleteSexFtpConf;
/*      */   
/*      */   protected Action actionFormat;
/*      */   
/*      */   protected Action actionPrepareUpload;
/*      */   
/*      */   protected Action actionLocationTo;
/*      */   
/*      */   protected Action actionPrepareServUpload;
/*      */   protected Action actionUpload;
/*      */   protected Action actionDownload;
/*      */   protected Action actionEdit;
/*      */   protected Action actionLocalEdit;
/*      */   protected Action actionCompare;
/*      */   protected Action actionDisconnect;
/*      */   protected Action actionDirectSServer;
/*      */   protected Action actionDirectSLocal;
/*      */   protected Action actionRefreshSexftp;
/*      */   protected Action actionRefreshFile;
/*      */   private Action doubleClickAction;
/*      */   protected Action actionCopy;
/*      */   protected Action actionCopyQualifiedName;
/*      */   protected Action actionCopyCientPath;
/*      */   protected Action actionCopyServerPath;
/*      */   protected Action actionExplorer;
/*      */   protected Action actionCollapseAll;
/*      */   protected Action actionExpandAll;
/*      */   protected TreeParent invisibleRoot;
/*      */   
/*      */   public class TreeObject
/*      */     implements IAdaptable
/*      */   {
/*      */     private String name;
/*      */     private AbstractSexftpView.TreeParent parent;
/*      */     private Object o;
/*  149 */     private boolean visible = true;
/*      */     
/*  151 */     public TreeObject(String name, Object o) { this.name = name;
/*  152 */       this.o = o;
/*      */     }
/*      */     
/*  155 */     public String getName() { return this.name; }
/*      */     
/*      */     public void setParent(AbstractSexftpView.TreeParent parent) {
/*  158 */       this.parent = parent;
/*      */     }
/*      */     
/*  161 */     public AbstractSexftpView.TreeParent getParent() { return this.parent; }
/*      */     
/*      */     public String toString() {
/*  164 */       return getName();
/*      */     }
/*      */     
/*  167 */     public Object getAdapter(Class key) { return null; }
/*      */     
/*      */     public Object getO() {
/*  170 */       return this.o;
/*      */     }
/*      */     
/*  173 */     public void setO(Object o) { this.o = o; }
/*      */     
/*      */     public boolean isVisible() {
/*  176 */       return this.visible;
/*      */     }
/*      */     
/*  179 */     public void setVisible(boolean visible) { this.visible = visible; }
/*      */   }
/*      */   
/*      */   public class TreeParent extends AbstractSexftpView.TreeObject
/*      */   {
/*      */     private Object o;
/*      */     private ArrayList<AbstractSexftpView.TreeObject> children;
/*      */     
/*      */     public TreeParent(String name, Object o) {
/*  188 */       super(name, o);
/*  189 */       this.o = o;
/*  190 */       this.children = new ArrayList();
/*      */     }
/*      */     
/*      */     public void addChild(AbstractSexftpView.TreeObject child) {
/*  194 */       this.children.add(child);
/*  195 */       child.setParent(this);
/*      */     }
/*      */     
/*  198 */     public void removeChild(AbstractSexftpView.TreeObject child) { this.children.remove(child);
/*  199 */       child.setParent(null);
/*      */     }
/*      */     
/*  202 */     public void removeAll() { for (AbstractSexftpView.TreeObject child : this.children) {
/*  203 */         child.setParent(null);
/*      */       }
/*  205 */       this.children.clear();
/*      */     }
/*      */     
/*  208 */     public AbstractSexftpView.TreeObject[] getChildren() { return (AbstractSexftpView.TreeObject[])this.children.toArray(new AbstractSexftpView.TreeObject[this.children.size()]); }
/*      */     
/*      */     public boolean hasChildren() {
/*  211 */       return this.children.size() > 0;
/*      */     }
/*      */     
/*      */     public Object getO() {
/*  215 */       return this.o;
/*      */     }
/*      */     
/*      */     public void setO(Object o) {
/*  219 */       this.o = o;
/*      */     }
/*      */   }
/*      */   
/*      */   class ViewContentProvider
/*      */     implements IStructuredContentProvider, ITreeContentProvider
/*      */   {
/*      */     ViewContentProvider() {}
/*      */     
/*      */     public void inputChanged(Viewer v, Object oldInput, Object newInput) {}
/*      */     
/*      */     public void dispose() {}
/*      */     
/*      */     public Object[] getElements(Object parent)
/*      */     {
/*  234 */       if (parent.equals(AbstractSexftpView.this.getViewSite())) {
/*  235 */         if (AbstractSexftpView.this.invisibleRoot == null) initialize();
/*  236 */         return getChildren(AbstractSexftpView.this.invisibleRoot);
/*      */       }
/*  238 */       return getChildren(parent);
/*      */     }
/*      */     
/*  241 */     public Object getParent(Object child) { if ((child instanceof AbstractSexftpView.TreeObject)) {
/*  242 */         return ((AbstractSexftpView.TreeObject)child).getParent();
/*      */       }
/*  244 */       return null;
/*      */     }
/*      */     
/*  247 */     public Object[] getChildren(Object parent) { if ((parent instanceof AbstractSexftpView.TreeParent)) {
/*  248 */         AbstractSexftpView.TreeObject[] children = ((AbstractSexftpView.TreeParent)parent).getChildren();
/*  249 */         List<AbstractSexftpView.TreeObject> childList = new ArrayList();
/*  250 */         AbstractSexftpView.TreeObject[] arrayOfTreeObject1; int j = (arrayOfTreeObject1 = children).length; for (int i = 0; i < j; i++) { AbstractSexftpView.TreeObject c = arrayOfTreeObject1[i];
/*      */           
/*  252 */           if (c.isVisible())
/*      */           {
/*  254 */             childList.add(c);
/*      */           }
/*      */         }
/*  257 */         return childList.toArray(new AbstractSexftpView.TreeObject[0]);
/*      */       }
/*  259 */       return new Object[0];
/*      */     }
/*      */     
/*  262 */     public boolean hasChildren(Object parent) { if ((parent instanceof AbstractSexftpView.TreeParent))
/*  263 */         return true;
/*  264 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected void initialize()
/*      */     {
/*      */       try
/*      */       {
/*  275 */         String p = Platform.asLocalURL(Platform.getBundle("sexftp").getEntry("")).getFile();
/*  276 */         System.out.println(p);
/*      */       }
/*      */       catch (Exception localException) {}
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  283 */       AbstractSexftpView.workspacePath = Platform.getInstanceLocation().getURL().getFile() + "/.sexftp10/";
/*  284 */       AbstractSexftpView.workspaceWkPath = AbstractSexftpView.workspacePath + "/.work/";
/*  285 */       if (!new File(AbstractSexftpView.workspaceWkPath).exists())
/*      */       {
/*  287 */         new File(AbstractSexftpView.workspaceWkPath).mkdirs();
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  349 */       AbstractSexftpView.this.refreshTreeViewData();
/*      */     }
/*      */   }
/*      */   
/*  353 */   protected Map<String, String> customizedImgMap = new Hashtable();
/*      */   
/*      */   class ViewLabelProvider extends LabelProvider implements IFontProvider, IColorProvider { ViewLabelProvider() {}
/*      */     
/*  357 */     public String getText(Object obj) { return obj.toString(); }
/*      */     
/*      */     public Font getFont(Object obj)
/*      */     {
/*  361 */       return null;
/*      */     }
/*      */     
/*      */     public Color getBackground(Object obj) {
/*  365 */       return null;
/*      */     }
/*      */     
/*      */     public Color getForeground(Object obj) {
/*  369 */       return null;
/*      */     }
/*      */     
/*      */     public Image getImage(Object obj) {
/*  373 */       String sexFtpIcon = null;
/*  374 */       String imageKey = "IMG_OBJ_ELEMENTS";
/*  375 */       if ((AbstractSexftpView.this instanceof SexftpServerView))
/*      */       {
/*  377 */         sexFtpIcon = "javaassist_co.gif";
/*      */       }
/*  379 */       if ((obj instanceof AbstractSexftpView.TreeParent))
/*      */       {
/*  381 */         imageKey = "IMG_OBJ_FOLDER";
/*  382 */         if ((AbstractSexftpView.this instanceof SexftpServerView))
/*      */         {
/*  384 */           sexFtpIcon = "cprj_obj.gif";
/*      */         }
/*      */       }
/*      */       
/*  388 */       if ((obj instanceof AbstractSexftpView.TreeObject))
/*      */       {
/*  390 */         AbstractSexftpView.TreeObject treeObj = (AbstractSexftpView.TreeObject)obj;
/*  391 */         if ((treeObj.getO() == null) && (treeObj.getName().equals("Sexftp Start")))
/*      */         {
/*  393 */           sexFtpIcon = "Twitter bird.ico";
/*      */         }
/*  395 */         else if ((treeObj.getO() instanceof FtpConf))
/*      */         {
/*  397 */           sexFtpIcon = "Duckling.ico";
/*      */         }
/*  399 */         else if ((treeObj.getO() instanceof FtpUploadConf))
/*      */         {
/*  401 */           sexFtpIcon = "Online writing.ico";
/*  402 */           if ((AbstractSexftpView.this instanceof SexftpServerView))
/*      */           {
/*  404 */             sexFtpIcon = "repository_rep.gif";
/*      */           }
/*  406 */           else if ((AbstractSexftpView.this instanceof SexftpSyncView))
/*      */           {
/*  408 */             sexFtpIcon = "compare.gif";
/*      */           }
/*      */           
/*      */ 
/*      */         }
/*  413 */         else if ((treeObj.getO() instanceof IProject))
/*      */         {
/*  415 */           imageKey = "IMG_OBJ_PROJECT";
/*  416 */           sexFtpIcon = null;
/*      */         }
/*  418 */         else if ((treeObj.getO() instanceof String))
/*      */         {
/*  420 */           if (treeObj.getO().toString().startsWith("Projects"))
/*      */           {
/*  422 */             sexFtpIcon = "Follow me.ico";
/*      */           }
/*      */         }
/*  425 */         else if ((treeObj.getO() instanceof FtpUploadPro))
/*      */         {
/*  427 */           String clientPath = ((FtpUploadPro)treeObj.getO()).getFtpUploadConf().getClientPath();
/*  428 */           if (AbstractSexftpView.this.customizedImgMap.containsKey(clientPath))
/*      */           {
/*  430 */             sexFtpIcon = (String)AbstractSexftpView.this.customizedImgMap.get(clientPath);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  435 */       if (sexFtpIcon != null)
/*      */       {
/*      */ 
/*  438 */         return AbstractUIPlugin.imageDescriptorFromPlugin("sexftp", "/icons/" + sexFtpIcon).createImage();
/*      */       }
/*  440 */       return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
/*      */     }
/*      */   }
/*      */   
/*      */   class NameSorter extends ViewerSorter {
/*      */     NameSorter() {}
/*      */     
/*  447 */     public int compare(Viewer viewer, Object e1, Object e2) { int c = 0;
/*  448 */       c = (e2 instanceof AbstractSexftpView.TreeParent) ? c + 1 : c - 1;
/*  449 */       c = (e1 instanceof AbstractSexftpView.TreeParent) ? c - 1 : c + 1;
/*  450 */       if (c != 0) return c;
/*  451 */       if (((e1 instanceof AbstractSexftpView.TreeParent)) && ((((AbstractSexftpView.TreeParent)e1).getO() instanceof String)) && (((AbstractSexftpView.TreeParent)e1).getO().toString().startsWith("Projects")))
/*      */       {
/*  453 */         return -1;
/*      */       }
/*  455 */       if (((e2 instanceof AbstractSexftpView.TreeParent)) && ((((AbstractSexftpView.TreeParent)e2).getO() instanceof String)) && (((AbstractSexftpView.TreeParent)e2).getO().toString().startsWith("Projects")))
/*      */       {
/*  457 */         return 1;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  463 */       return super.compare(viewer, e1, e2);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void createPartControl(Composite parent)
/*      */   {
/*  480 */     this.viewer = new TreeViewer(parent, 770);
/*  481 */     this.drillDownAdapter = new DrillDownAdapter(this.viewer);
/*  482 */     this.viewer.setContentProvider(new ViewContentProvider());
/*  483 */     this.viewer.setLabelProvider(new ViewLabelProvider());
/*  484 */     this.viewer.setSorter(new NameSorter());
/*  485 */     this.viewer.setInput(getViewSite());
/*      */     
/*      */ 
/*  488 */     PlatformUI.getWorkbench().getHelpSystem().setHelp(this.viewer.getControl(), "sexftp.viewer");
/*      */     
/*      */ 
/*      */ 
/*  492 */     makeActions();
/*  493 */     hookContextMenu();
/*  494 */     hookDoubleClickAction();
/*  495 */     hookSelectionChange();
/*  496 */     hookTreeListener();
/*  497 */     contributeToActionBars();
/*      */     
/*  499 */     this.viewer.expandToLevel(2);
/*      */   }
/*      */   
/*      */   private void hookContextMenu() {
/*  503 */     MenuManager menuMgr = new MenuManager("#PopupMenu");
/*  504 */     menuMgr.setRemoveAllWhenShown(true);
/*  505 */     menuMgr.addMenuListener(new IMenuListener() {
/*      */       public void menuAboutToShow(IMenuManager manager) {
/*  507 */         AbstractSexftpView.this.menuAboutToShow_event(manager);
/*      */       }
/*  509 */     });
/*  510 */     Menu menu = menuMgr.createContextMenu(this.viewer.getControl());
/*  511 */     this.viewer.getControl().setMenu(menu);
/*  512 */     getSite().registerContextMenu(menuMgr, this.viewer);
/*      */   }
/*      */   
/*      */   private void contributeToActionBars() {
/*  516 */     IActionBars bars = getViewSite().getActionBars();
/*  517 */     fillLocalPullDown(bars.getMenuManager());
/*  518 */     fillLocalToolBar(bars.getToolBarManager());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected void actionEnableHandle() {}
/*      */   
/*      */ 
/*      */   private void fillLocalPullDown(IMenuManager manager)
/*      */   {
/*  528 */     manager.add(this.actionRefreshSexftp);
/*  529 */     manager.add(this.actionDirectSServer);
/*  530 */     manager.add(this.actionDirectSLocal);
/*      */   }
/*      */   
/*      */   public void fillLocalToolBar(IToolBarManager manager) {
/*  534 */     actionEnableHandle();
/*  535 */     new Action("Refresh And Reload Sexftp Config Files(&w)"); {};
/*  538 */     manager.add(this.actionRefreshSexftp);
/*  539 */     manager.add(this.actionRefreshFile);
/*  540 */     manager.add(new Separator());
/*  541 */     manager.add(this.actionDirectSServer);
/*  542 */     manager.add(this.actionDirectSLocal);
/*  543 */     manager.add(this.actionLocationTo);
/*  544 */     manager.add(new Separator());
/*  545 */     manager.add(this.actionApplySexFtpConf);
/*  546 */     manager.add(this.actionEditSexFtpConf);
/*  547 */     manager.add(this.actionDeleteSexFtpConf);
/*  548 */     manager.add(new Separator());
/*  549 */     manager.add(this.actionFormat);
/*  550 */     manager.add(this.actionPrepareUpload);
/*  551 */     manager.add(this.actionPrepareServUpload);
/*  552 */     manager.add(new Separator());
/*  553 */     manager.add(this.actionUpload);
/*  554 */     manager.add(this.actionDownload);
/*  555 */     manager.add(this.actionDisconnect);
/*  556 */     manager.add(new Separator());
/*  557 */     manager.add(this.actionExplorer);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Shell getShell()
/*      */   {
/*  565 */     (Shell)PluginUtil.runAsDisplayThread(new PluginUtil.RunAsDisplayThread() {
/*      */       public Object run() throws Exception {
/*  567 */         return AbstractSexftpView.this.viewer.getControl().getShell();
/*      */       }
/*      */     });
/*      */   }
/*      */   
/*  572 */   IWorkbenchPage activePage = null;
/*      */   
/*      */   public IWorkbenchPage getWorkbenchPage() {
/*  575 */     IWorkbenchPage activePage = PluginUtil.getActivePage();
/*  576 */     if (activePage != null)
/*      */     {
/*  578 */       this.activePage = activePage;
/*      */     }
/*  580 */     return this.activePage;
/*      */   }
/*      */   
/*      */ 
/*      */   private void makeActions()
/*      */   {
/*  586 */     this.actionApplySexFtpConf = new SexftpViewAction() {
/*      */       public void run() {
/*      */         try {
/*  589 */           AbstractSexftpView.this.actionApplySexFtpConf_actionPerformed();
/*      */         } catch (Exception e) {
/*  591 */           AbstractSexftpView.this.handleException(e);
/*      */         }
/*      */       }
/*  594 */     };
/*  595 */     this.actionApplySexFtpConf.setText("New Sexftp Upload Unit");
/*  596 */     this.actionApplySexFtpConf.setToolTipText("Generate Sexftp Config File,Using Your Chosed Folders And Files,Main Option Start Here!");
/*  597 */     this.actionApplySexFtpConf.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("sexftp", "/icons/gif-0108.gif"));
/*      */     
/*      */ 
/*      */ 
/*  601 */     this.actionEditSexFtpConf = new SexftpViewAction() {
/*      */       public void run() {
/*      */         try {
/*  604 */           AbstractSexftpView.this.actionEditSexFtpConf_actionPerformed();
/*      */         } catch (Exception e) {
/*  606 */           AbstractSexftpView.this.handleException(e);
/*      */         }
/*      */       }
/*  609 */     };
/*  610 */     this.actionEditSexFtpConf.setText("Edit Sexftp Upload Unit");
/*  611 */     this.actionEditSexFtpConf.setToolTipText("Edit Sexftp Config File");
/*  612 */     this.actionEditSexFtpConf.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("sexftp", "/icons/gif-0708.gif"));
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  617 */     this.actionDeleteSexFtpConf = new SexftpViewAction() {
/*      */       public void run() {
/*      */         try {
/*  620 */           AbstractSexftpView.this.actionDeleteSexFtpConf_actionPerformed();
/*      */         } catch (Exception e) {
/*  622 */           AbstractSexftpView.this.handleException(e);
/*      */         }
/*      */       }
/*  625 */     };
/*  626 */     this.actionDeleteSexFtpConf.setText("Remove Sexftp Upload Unit");
/*  627 */     this.actionDeleteSexFtpConf.setToolTipText("Remove Sexftp Config File");
/*  628 */     this.actionDeleteSexFtpConf.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
/*  629 */       .getImageDescriptor("IMG_TOOL_DELETE"));
/*      */     
/*  631 */     this.actionFormat = new SexftpViewAction() {
/*      */       public void run() {
/*      */         try {
/*  634 */           AbstractSexftpView.this.actionFormat_actionPerformed();
/*      */         } catch (Exception e) {
/*  636 */           AbstractSexftpView.this.handleException(e);
/*      */         }
/*      */       }
/*  639 */     };
/*  640 */     this.actionFormat.setText("Format Local File Upload Point(&F)");
/*  641 */     this.actionFormat.setToolTipText("Format Local File Upload Point,New Modify Checking Will Based On this Result!");
/*  642 */     this.actionFormat.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("sexftp", "/icons/xhrmon.gif"));
/*      */     
/*  644 */     this.actionPrepareUpload = new SexftpViewAction() {
/*      */       public void run() {
/*      */         try {
/*  647 */           AbstractSexftpView.this.actionPrepareUpload_actionPerformed();
/*      */         } catch (Exception e) {
/*  649 */           AbstractSexftpView.this.handleException(e);
/*      */         }
/*      */       }
/*  652 */     };
/*  653 */     this.actionPrepareUpload.setText("View Or Upload Local New Modified Files(&M)");
/*  654 */     this.actionPrepareUpload.setToolTipText("After This Action,We'll Gevi You Which File Modified After Last Format or Upload Option,And Then You Can Chose Them To Upload!");
/*  655 */     this.actionPrepareUpload.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("sexftp", "/icons/compare_view.gif"));
/*      */     
/*      */ 
/*  658 */     this.actionLocationTo = new SexftpViewAction() {
/*      */       public void run() {
/*      */         try {
/*  661 */           AbstractSexftpView.this.actionLocationTo_actionPerformed();
/*      */         } catch (Exception e) {
/*  663 */           AbstractSexftpView.this.handleException(e);
/*      */         }
/*      */       }
/*  666 */     };
/*  667 */     this.actionLocationTo.setText("Location To(&N)");
/*      */     
/*  669 */     this.actionLocationTo.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("sexftp", "/icons/filter_history.gif"));
/*      */     
/*      */ 
/*      */ 
/*  673 */     this.actionPrepareServUpload = new SexftpViewAction() {
/*      */       public void run() {
/*      */         try {
/*  676 */           AbstractSexftpView.this.actionPrepareServUpload_actionPerformed();
/*      */         }
/*      */         catch (Exception e) {
/*  679 */           AbstractSexftpView.this.handleException(e);
/*      */         }
/*      */       }
/*  682 */     };
/*  683 */     this.actionPrepareServUpload.setText("View Or Upload Files Witch Different From Server(&A)");
/*  684 */     this.actionPrepareServUpload.setToolTipText("View Or Upload Files Witch Different From Server!");
/*  685 */     this.actionPrepareServUpload.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("sexftp", "/icons/synch_synch.gif"));
/*      */     
/*      */ 
/*  688 */     this.actionUpload = new SexftpViewAction() {
/*      */       public void run() {
/*      */         try {
/*  691 */           AbstractSexftpView.this.actionUpload_actionPerformed();
/*      */         } catch (Exception e) {
/*  693 */           AbstractSexftpView.this.handleException(e);
/*      */         }
/*      */       }
/*  696 */     };
/*  697 */     this.actionUpload.setText("Upload To Server(&U)");
/*  698 */     this.actionUpload.setToolTipText("After This Action,Upload Files You Chose To Ftp Server!");
/*  699 */     this.actionUpload.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("sexftp", "/icons/prev_nav.gif"));
/*      */     
/*  701 */     this.actionDownload = new SexftpViewAction() {
/*      */       public void run() {
/*      */         try {
/*  704 */           AbstractSexftpView.this.actionDownload_actionPerformed();
/*      */         } catch (Exception e) {
/*  706 */           AbstractSexftpView.this.handleException(e);
/*      */         }
/*      */       }
/*  709 */     };
/*  710 */     this.actionDownload.setText("Download From Server(&D)");
/*  711 */     this.actionDownload.setToolTipText("After This Action,Download Files You Chose From Ftp Server!");
/*  712 */     this.actionDownload.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("sexftp", "/icons/next_nav.gif"));
/*      */     
/*  714 */     this.actionEdit = new SexftpViewAction() {
/*      */       public void run() {
/*      */         try {
/*  717 */           AbstractSexftpView.this.actionEdit_actionPerformed();
/*      */         } catch (Exception e) {
/*  719 */           AbstractSexftpView.this.handleException(e);
/*      */         }
/*      */       }
/*  722 */     };
/*  723 */     this.actionEdit.setText("View Or Edit ServerSide File(&E)");
/*  724 */     this.actionEdit.setToolTipText("View Or Edit ServerSide File!");
/*  725 */     this.actionEdit.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("sexftp", "/icons/history_view.gif"));
/*      */     
/*      */ 
/*  728 */     this.actionLocalEdit = new SexftpViewAction() {
/*      */       public void run() {
/*      */         try {
/*  731 */           AbstractSexftpView.this.actionLocalEdit_actionPerformed();
/*      */         } catch (Exception e) {
/*  733 */           AbstractSexftpView.this.handleException(e);
/*      */         }
/*      */       }
/*  736 */     };
/*  737 */     this.actionLocalEdit.setText("View Or Edit LocalSide File(&T)");
/*  738 */     this.actionLocalEdit.setToolTipText("View Or Edit LocalSide File!");
/*  739 */     this.actionLocalEdit.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("sexftp", "/icons/edit_action.gif"));
/*      */     
/*      */ 
/*  742 */     this.actionDisconnect = new SexftpViewAction() {
/*      */       public void run() {
/*      */         try {
/*  745 */           AbstractSexftpView.this.actionDisconnect_actionPerformed();
/*      */         } catch (Exception e) {
/*  747 */           AbstractSexftpView.this.handleException(e);
/*      */         }
/*      */       }
/*  750 */     };
/*  751 */     this.actionDisconnect.setText("Disconnect All Connections(&Q)");
/*  752 */     this.actionDisconnect.setToolTipText("Disconnect All Connections!");
/*  753 */     this.actionDisconnect.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("sexftp", "/icons/disconnect_co.gif"));
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  759 */     this.actionDirectSServer = new SexftpViewAction() {
/*      */       public void run() {
/*      */         try {
/*  762 */           AbstractSexftpView.this.actionDirectSServer_actionPerformed();
/*      */         } catch (Exception e) {
/*  764 */           AbstractSexftpView.this.handleException(e);
/*      */         }
/*      */       }
/*  767 */     };
/*  768 */     this.actionDirectSServer.setText("Open Server Viewer(&S)");
/*  769 */     this.actionDirectSServer.setToolTipText("Open Server Viewer!");
/*  770 */     this.actionDirectSServer.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("sexftp", "/icons/repository_rep.gif"));
/*      */     
/*      */ 
/*      */ 
/*  774 */     this.actionCompare = new SexftpViewAction() {
/*      */       public void run() {
/*      */         try {
/*  777 */           AbstractSexftpView.this.actionCompare_actionPerformed();
/*      */         } catch (Throwable e) {
/*  779 */           AbstractSexftpView.this.handleException(e);
/*      */         }
/*      */       }
/*  782 */     };
/*  783 */     this.actionCompare.setText("Compare (&C)");
/*  784 */     this.actionCompare.setToolTipText("Compare");
/*  785 */     this.actionCompare.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("sexftp", "/icons/compare.gif"));
/*      */     
/*      */ 
/*      */ 
/*  789 */     this.actionDirectSLocal = new SexftpViewAction() {
/*      */       public void run() {
/*      */         try {
/*  792 */           AbstractSexftpView.this.actionDirectSLocal_actionPerformed();
/*      */         } catch (Exception e) {
/*  794 */           AbstractSexftpView.this.handleException(e);
/*      */         }
/*      */       }
/*  797 */     };
/*  798 */     this.actionDirectSLocal.setText("Location To Local Viewer(&L)");
/*  799 */     this.actionDirectSLocal.setToolTipText("Location To Local Viewer!");
/*  800 */     this.actionDirectSLocal.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("sexftp", "/icons/Follow me.ico"));
/*      */     
/*      */ 
/*  803 */     this.actionRefreshSexftp = new SexftpViewAction() {
/*      */       public void run() {
/*      */         try {
/*  806 */           AbstractSexftpView.this.actionRefreshSexftp_actionPerformed();
/*      */         } catch (Exception e) {
/*  808 */           AbstractSexftpView.this.handleException(e);
/*      */         }
/*      */       }
/*  811 */     };
/*  812 */     this.actionRefreshSexftp.setText("Refresh And Reload Sexftp Config Files(&w)");
/*  813 */     this.actionRefreshSexftp.setToolTipText("Refresh And Reload All Sexftp Configs");
/*  814 */     this.actionRefreshSexftp.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("sexftp", "/icons/refresh.gif"));
/*      */     
/*      */ 
/*  817 */     this.actionRefreshFile = new SexftpViewAction() {
/*      */       public void run() {
/*      */         try {
/*  820 */           AbstractSexftpView.this.actionRefreshFile_actionPerformed();
/*      */         } catch (Exception e) {
/*  822 */           AbstractSexftpView.this.handleException(e);
/*      */         }
/*      */       }
/*  825 */     };
/*  826 */     this.actionRefreshFile.setText("Refresh Files And Folders(&R)");
/*  827 */     this.actionRefreshFile.setToolTipText("Refresh Files And Folders");
/*  828 */     this.actionRefreshFile.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("sexftp", "/icons/refresh (5).gif"));
/*      */     
/*      */ 
/*  831 */     this.actionCopy = new SexftpViewAction() {
/*      */       public void run() {
/*      */         try {
/*  834 */           AbstractSexftpView.this.actionCopy_actionPerformed();
/*      */         } catch (Exception e) {
/*  836 */           AbstractSexftpView.this.handleException(e);
/*      */         }
/*      */       }
/*  839 */     };
/*  840 */     this.actionCopy.setText("Copy");
/*  841 */     this.actionCopy.setToolTipText("Copy Name");
/*  842 */     this.actionCopy.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
/*  843 */       .getImageDescriptor("IMG_TOOL_COPY"));
/*      */     
/*  845 */     this.actionCopyQualifiedName = new SexftpViewAction() {
/*      */       public void run() {
/*      */         try {
/*  848 */           AbstractSexftpView.this.actionCopyQualifiedName_actionPerformed();
/*      */         } catch (Exception e) {
/*  850 */           AbstractSexftpView.this.handleException(e);
/*      */         }
/*      */       }
/*  853 */     };
/*  854 */     this.actionCopyQualifiedName.setText("Copy Qualified Name");
/*  855 */     this.actionCopyQualifiedName.setToolTipText("Copy Qualified Name");
/*  856 */     this.actionCopyQualifiedName.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
/*  857 */       .getImageDescriptor("IMG_TOOL_COPY"));
/*      */     
/*      */ 
/*  860 */     this.actionCopyCientPath = new SexftpViewAction() {
/*      */       public void run() {
/*      */         try {
/*  863 */           AbstractSexftpView.this.actionCopyCientPath_actionPerformed();
/*      */         } catch (Exception e) {
/*  865 */           AbstractSexftpView.this.handleException(e);
/*      */         }
/*      */       }
/*  868 */     };
/*  869 */     this.actionCopyCientPath.setText("Copy Cient File/Folder Path(&Z)");
/*  870 */     this.actionCopyCientPath.setToolTipText("Copy Cient File or Folder's Path");
/*  871 */     this.actionCopyCientPath.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
/*  872 */       .getImageDescriptor("IMG_TOOL_COPY"));
/*      */     
/*      */ 
/*  875 */     this.actionCopyServerPath = new SexftpViewAction() {
/*      */       public void run() {
/*      */         try {
/*  878 */           AbstractSexftpView.this.actionCopyServerPath_actionPerformed();
/*      */         } catch (Exception e) {
/*  880 */           AbstractSexftpView.this.handleException(e);
/*      */         }
/*      */       }
/*  883 */     };
/*  884 */     this.actionCopyServerPath.setText("Copy Server File/Folder Path(&S)");
/*  885 */     this.actionCopyServerPath.setToolTipText("Copy Server File/Folder Path");
/*  886 */     this.actionCopyServerPath.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
/*  887 */       .getImageDescriptor("IMG_TOOL_COPY"));
/*      */     
/*  889 */     this.actionExplorer = new SexftpViewAction() {
/*      */       public void run() {
/*      */         try {
/*  892 */           AbstractSexftpView.this.actionExplorer_actionPerformed();
/*      */         } catch (Exception e) {
/*  894 */           AbstractSexftpView.this.handleException(e);
/*      */         }
/*      */       }
/*  897 */     };
/*  898 */     this.actionExplorer.setText("Open In Explorer");
/*  899 */     this.actionExplorer.setToolTipText("Open In Explorer");
/*  900 */     this.actionExplorer.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("sexftp", "/icons/explorer_open.gif"));
/*      */     
/*      */ 
/*  903 */     this.actionCollapseAll = new SexftpViewAction() {
/*      */       public void run() {
/*      */         try {
/*  906 */           AbstractSexftpView.this.actionCollapseAll_actionPerformed();
/*      */         } catch (Exception e) {
/*  908 */           AbstractSexftpView.this.handleException(e);
/*      */         }
/*      */       }
/*  911 */     };
/*  912 */     this.actionCollapseAll.setText("Collapse All");
/*  913 */     this.actionCollapseAll.setToolTipText("Collapse All");
/*  914 */     this.actionCollapseAll.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("sexftp", "/icons/collapseall.gif"));
/*      */     
/*      */ 
/*  917 */     this.actionExpandAll = new SexftpViewAction() {
/*      */       public void run() {
/*      */         try {
/*  920 */           AbstractSexftpView.this.actionExpandAll_actionPerformed();
/*      */         } catch (Exception e) {
/*  922 */           AbstractSexftpView.this.handleException(e);
/*      */         }
/*      */       }
/*  925 */     };
/*  926 */     this.actionExpandAll.setText("Expand All");
/*  927 */     this.actionExpandAll.setToolTipText("Expand All");
/*  928 */     this.actionExpandAll.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("sexftp", "/icons/expand_all.gif"));
/*      */     
/*      */ 
/*      */ 
/*  932 */     this.doubleClickAction = new SexftpViewAction() {
/*      */       public void run() {
/*      */         try {
/*  935 */           AbstractSexftpView.this.doubleClickAction_actionPerformed();
/*      */         } catch (Exception e) {
/*  937 */           AbstractSexftpView.this.handleException(e);
/*      */         }
/*      */         
/*      */       }
/*  941 */     };
/*  942 */     actionPrepare();
/*      */   }
/*      */   
/*      */ 
/*      */   protected void actionPrepare() {}
/*      */   
/*      */ 
/*      */   protected void actionPaste_actionPerformed()
/*      */     throws Exception
/*      */   {}
/*      */   
/*      */ 
/*      */   protected void actionLocation_actionPerformed()
/*      */     throws Exception
/*      */   {}
/*      */   
/*      */ 
/*      */   protected void actionExplorer_actionPerformed()
/*      */     throws Exception
/*      */   {}
/*      */   
/*      */ 
/*      */   protected void actionCopyQualifiedName_actionPerformed()
/*      */     throws Exception
/*      */   {}
/*      */   
/*      */ 
/*      */   protected void actionCopy_actionPerformed()
/*      */     throws Exception
/*      */   {}
/*      */   
/*      */ 
/*      */   protected void actionCopyCientPath_actionPerformed()
/*      */     throws Exception
/*      */   {}
/*      */   
/*      */ 
/*      */   protected void actionCopyServerPath_actionPerformed()
/*      */     throws Exception
/*      */   {}
/*      */   
/*      */ 
/*      */   protected void actionCompare_actionPerformed()
/*      */     throws Exception
/*      */   {}
/*      */   
/*      */ 
/*      */   protected void actionApplySexFtpConf_actionPerformed()
/*      */     throws Exception
/*      */   {}
/*      */   
/*      */   protected void actionEditSexFtpConf_actionPerformed()
/*      */     throws Exception
/*      */   {}
/*      */   
/*      */   protected void actionDeleteSexFtpConf_actionPerformed()
/*      */     throws Exception
/*      */   {}
/*      */   
/*      */   protected void actionDisconnect_actionPerformed()
/*      */     throws Exception
/*      */   {}
/*      */   
/*      */   protected void actionFormat_actionPerformed()
/*      */     throws Exception
/*      */   {}
/*      */   
/*      */   protected void actionPrepareUpload_actionPerformed()
/*      */     throws Exception
/*      */   {}
/*      */   
/*      */   protected void actionPrepareServUpload_actionPerformed()
/*      */     throws Exception
/*      */   {}
/*      */   
/*      */   protected void actionLocationTo_actionPerformed()
/*      */     throws Exception
/*      */   {
/* 1020 */     actionLocation_actionPerformed();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected void actionUpload_actionPerformed()
/*      */     throws Exception
/*      */   {}
/*      */   
/*      */ 
/*      */ 
/*      */   protected void actionDownload_actionPerformed()
/*      */     throws Exception
/*      */   {}
/*      */   
/*      */ 
/*      */ 
/*      */   protected void actionEdit_actionPerformed()
/*      */     throws Exception
/*      */   {}
/*      */   
/*      */ 
/*      */   protected void actionLocalEdit_actionPerformed()
/*      */     throws Exception
/*      */   {}
/*      */   
/*      */ 
/*      */   protected void actionDirectSLocal_actionPerformed()
/*      */     throws Exception
/*      */   {}
/*      */   
/*      */ 
/*      */   protected void actionDirectSServer_actionPerformed()
/*      */     throws Exception
/*      */   {}
/*      */   
/*      */ 
/*      */   protected void actionRefreshSexftp_actionPerformed()
/*      */     throws Exception
/*      */   {}
/*      */   
/*      */ 
/*      */   protected void actionRefreshFile_actionPerformed()
/*      */     throws Exception
/*      */   {}
/*      */   
/*      */ 
/*      */   protected void doubleClickAction_actionPerformed()
/*      */     throws Exception
/*      */   {}
/*      */   
/*      */ 
/*      */   protected void actionCollapseAll_actionPerformed()
/*      */     throws Exception
/*      */   {}
/*      */   
/*      */ 
/*      */   protected void actionExpandAll_actionPerformed()
/*      */     throws Exception
/*      */   {}
/*      */   
/*      */ 
/*      */   protected void treeExpanded_actionPerformed(TreeExpansionEvent e)
/*      */     throws Exception
/*      */   {}
/*      */   
/*      */ 
/*      */   protected void treeCollapsed_actionPerformed(TreeExpansionEvent e)
/*      */     throws Exception
/*      */   {}
/*      */   
/*      */ 
/*      */   protected void menuAboutToShow_event(IMenuManager manager) {}
/*      */   
/*      */ 
/*      */   protected void refreshTreeViewData() {}
/*      */   
/*      */ 
/*      */   private void hookDoubleClickAction()
/*      */   {
/* 1100 */     this.viewer.addDoubleClickListener(new IDoubleClickListener() {
/*      */       public void doubleClick(DoubleClickEvent event) {
/*      */         try {
/* 1103 */           AbstractSexftpView.this.doubleClickAction.run();
/*      */         } catch (Exception e) {
/* 1105 */           AbstractSexftpView.this.handleException(e);
/*      */         }
/*      */       }
/*      */     });
/*      */   }
/*      */   
/*      */ 
/*      */   private void hookSelectionChange()
/*      */   {
/* 1114 */     this.viewer.addSelectionChangedListener(new ISelectionChangedListener()
/*      */     {
/*      */       public void selectionChanged(SelectionChangedEvent arg0) {
/* 1117 */         AbstractSexftpView.this.actionEnableHandle();
/*      */       }
/*      */     });
/*      */   }
/*      */   
/*      */   public void expandToObject(String filePath) {
/*      */     Object[] arrayOfObject;
/* 1124 */     int j = (arrayOfObject = this.viewer.getExpandedElements()).length; for (int i = 0; i < j; i++) { Object o = arrayOfObject[i];
/*      */       
/* 1126 */       if ((o instanceof TreeParent)) {
/*      */         TreeObject[] arrayOfTreeObject;
/* 1128 */         int m = (arrayOfTreeObject = ((TreeParent)o).getChildren()).length; for (int k = 0; k < m; k++) { TreeObject to = arrayOfTreeObject[k];
/*      */           
/* 1130 */           expandToObject(to, filePath);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void expandToObject(TreeObject to, String filePath) {
/* 1137 */     if ((to instanceof TreeParent))
/*      */     {
/* 1139 */       if ((to.getO() instanceof FtpUploadConf))
/*      */       {
/* 1141 */         if (filePath.startsWith(((FtpUploadConf)to.getO()).getClientPath()))
/*      */         {
/* 1143 */           this.viewer.expandToLevel(to.getParent(), 1); return;
/*      */         }
/*      */       }
/*      */       TreeObject[] arrayOfTreeObject;
/* 1147 */       int j = (arrayOfTreeObject = ((TreeParent)to).getChildren()).length; for (int i = 0; i < j; i++) { TreeObject co = arrayOfTreeObject[i];
/*      */         
/* 1149 */         expandToObject(co, filePath);
/*      */       }
/*      */     } else {
/* 1152 */       (to instanceof TreeObject);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void hookTreeListener()
/*      */   {
/* 1160 */     this.viewer.addTreeListener(new ITreeViewerListener()
/*      */     {
/*      */       public void treeExpanded(TreeExpansionEvent e) {
/*      */         try {
/* 1164 */           AbstractSexftpView.this.treeExpanded_actionPerformed(e);
/*      */         } catch (Exception e1) {
/* 1166 */           AbstractSexftpView.this.handleException(e1);
/*      */         }
/*      */       }
/*      */       
/*      */       public void treeCollapsed(TreeExpansionEvent e)
/*      */       {
/*      */         try {
/* 1173 */           AbstractSexftpView.this.treeCollapsed_actionPerformed(e);
/*      */         } catch (Exception e1) {
/* 1175 */           AbstractSexftpView.this.handleException(e1);
/*      */         }
/*      */         
/*      */       }
/* 1179 */     });
/* 1180 */     this.viewer.getTree().addKeyListener(new KeyListener()
/*      */     {
/*      */       public void keyReleased(KeyEvent arg0) {}
/*      */       
/*      */ 
/*      */ 
/*      */       public void keyPressed(KeyEvent e)
/*      */       {
/*      */         try
/*      */         {
/* 1190 */           if (e.stateMask == 262144)
/*      */           {
/* 1192 */             if (e.keyCode == 118)
/*      */             {
/*      */ 
/* 1195 */               AbstractSexftpView.this.actionPaste_actionPerformed();
/*      */             }
/* 1197 */             else if (e.keyCode == 108)
/*      */             {
/*      */ 
/* 1200 */               AbstractSexftpView.this.actionLocation_actionPerformed();
/*      */             }
/*      */           }
/*      */         } catch (Throwable e1) {
/* 1204 */           AbstractSexftpView.this.handleException(e1);
/*      */         }
/*      */       }
/*      */     });
/*      */   }
/*      */   
/*      */ 
/*      */   protected void showMessage(final String message)
/*      */   {
/* 1213 */     Runnable runnable = new Runnable() {
/*      */       public void run() {
/* 1215 */         MessageDialog.openInformation(
/* 1216 */           AbstractSexftpView.this.viewer.getControl().getShell(), 
/* 1217 */           LangUtil.langText("Sexftp Messsage"), 
/* 1218 */           LangUtil.langText(message));
/*      */       }
/*      */       
/* 1221 */     };
/* 1222 */     Display.getDefault().syncExec(runnable);
/*      */   }
/*      */   
/* 1225 */   protected void showError(final String message) { Runnable runnable = new Runnable() {
/*      */       public void run() {
/* 1227 */         MessageDialog.openError(AbstractSexftpView.this.viewer.getControl().getShell(), 
/* 1228 */           LangUtil.langText("Sexftp Error"), 
/* 1229 */           LangUtil.langText(message));
/*      */       }
/*      */       
/* 1232 */     };
/* 1233 */     Display.getDefault().syncExec(runnable);
/*      */   }
/*      */   
/*      */ 
/*      */   protected void showError(String message, final Throwable e)
/*      */   {
/* 1239 */     Status s = new Status(4, "sexftp", e.getMessage(), e)
/*      */     {
/*      */       public IStatus[] getChildren() {
/* 1242 */         String[] es = StringUtil.readExceptionDetailInfo(e).split("\n");
/* 1243 */         List<IStatus> st = new ArrayList();
/* 1244 */         String[] arrayOfString1; int j = (arrayOfString1 = es).length; for (int i = 0; i < j; i++) { String m = arrayOfString1[i];
/*      */           
/* 1246 */           if (m.trim().length() > 0)
/*      */           {
/* 1248 */             st.add(new Status(4, "sexftp", m.trim(), null));
/*      */           }
/*      */         }
/* 1251 */         return (IStatus[])st.toArray(new IStatus[0]);
/*      */       }
/*      */       
/*      */ 
/* 1255 */     };
/* 1256 */     ErrorDialog.openError(this.viewer.getControl().getShell(), "Sexftp Error", message, s);
/*      */   }
/*      */   
/*      */   public boolean showQuestion(final String message)
/*      */   {
/* 1261 */     Runnable runnable = new Runnable() {
/* 1262 */       private boolean q = false;
/*      */       
/* 1264 */       public void run() { this.q = MessageDialog.openQuestion(AbstractSexftpView.this.viewer.getControl().getShell(), LangUtil.langText("Sexftp Question"), LangUtil.langText(message)); }
/*      */       
/*      */ 
/*      */       public boolean isOk()
/*      */       {
/* 1269 */         return this.q;
/*      */       }
/* 1271 */     };
/* 1272 */     Display.getDefault().syncExec(runnable);
/*      */     try {
/* 1274 */       return ((Boolean)runnable.getClass().getMethod("isOk", new Class[0]).invoke(runnable, new Object[0])).booleanValue();
/*      */     } catch (Exception e) {
/* 1276 */       throw new SRuntimeException(e);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public MessageDialogWithToggle showQuestionInDTread(final String message, final String toggleMessage)
/*      */   {
/* 1283 */     Runnable runnable = new Runnable() {
/* 1284 */       private MessageDialogWithToggle s = null;
/*      */       
/* 1286 */       public void run() { this.s = AbstractSexftpView.this.showQuestion(message, toggleMessage); }
/*      */       
/*      */       public MessageDialogWithToggle getS() {
/* 1289 */         return this.s;
/*      */       }
/* 1291 */     };
/* 1292 */     Display.getDefault().syncExec(runnable);
/*      */     try {
/* 1294 */       return (MessageDialogWithToggle)runnable.getClass().getMethod("getS", new Class[0]).invoke(runnable, new Object[0]);
/*      */     } catch (Exception e) {
/* 1296 */       throw new SRuntimeException(e);
/*      */     }
/*      */   }
/*      */   
/*      */   public MessageDialogWithToggle showQuestion(String message, String toggleMessage) {
/* 1301 */     MessageDialogWithToggle t = MessageDialogWithToggle.openYesNoCancelQuestion(this.viewer.getControl().getShell(), "Sexftp Question", message, toggleMessage, false, new IPreferenceStore()
/*      */     {
/*      */       public void setValue(String arg0, boolean arg1) {}
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */       public void setValue(String arg0, String arg1) {}
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */       public void setValue(String arg0, long arg1) {}
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */       public void setValue(String arg0, int arg1) {}
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */       public void setValue(String arg0, float arg1) {}
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */       public void setValue(String arg0, double arg1) {}
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */       public void setToDefault(String arg0) {}
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */       public void setDefault(String arg0, boolean arg1) {}
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */       public void setDefault(String arg0, String arg1) {}
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */       public void setDefault(String arg0, long arg1) {}
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */       public void setDefault(String arg0, int arg1) {}
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */       public void setDefault(String arg0, float arg1) {}
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */       public void setDefault(String arg0, double arg1) {}
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */       public void removePropertyChangeListener(IPropertyChangeListener arg0) {}
/*      */       
/*      */ 
/*      */ 
/*      */       public void putValue(String arg0, String arg1) {}
/*      */       
/*      */ 
/*      */ 
/*      */       public boolean needsSaving()
/*      */       {
/* 1378 */         return false;
/*      */       }
/*      */       
/*      */       public boolean isDefault(String arg0)
/*      */       {
/* 1383 */         return false;
/*      */       }
/*      */       
/*      */       public String getString(String arg0)
/*      */       {
/* 1388 */         return null;
/*      */       }
/*      */       
/*      */       public long getLong(String arg0)
/*      */       {
/* 1393 */         return 0L;
/*      */       }
/*      */       
/*      */       public int getInt(String arg0)
/*      */       {
/* 1398 */         return 0;
/*      */       }
/*      */       
/*      */       public float getFloat(String arg0)
/*      */       {
/* 1403 */         return 0.0F;
/*      */       }
/*      */       
/*      */ 
/*      */       public double getDouble(String arg0)
/*      */       {
/* 1409 */         return 0.0D;
/*      */       }
/*      */       
/*      */ 
/*      */       public String getDefaultString(String arg0)
/*      */       {
/* 1415 */         return null;
/*      */       }
/*      */       
/*      */ 
/*      */       public long getDefaultLong(String arg0)
/*      */       {
/* 1421 */         return 0L;
/*      */       }
/*      */       
/*      */ 
/*      */       public int getDefaultInt(String arg0)
/*      */       {
/* 1427 */         return 0;
/*      */       }
/*      */       
/*      */ 
/*      */       public float getDefaultFloat(String arg0)
/*      */       {
/* 1433 */         return 0.0F;
/*      */       }
/*      */       
/*      */ 
/*      */       public double getDefaultDouble(String arg0)
/*      */       {
/* 1439 */         return 0.0D;
/*      */       }
/*      */       
/*      */ 
/*      */       public boolean getDefaultBoolean(String arg0)
/*      */       {
/* 1445 */         return false;
/*      */       }
/*      */       
/*      */ 
/*      */       public boolean getBoolean(String arg0)
/*      */       {
/* 1451 */         return false;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */       public void firePropertyChangeEvent(String arg0, Object arg1, Object arg2) {}
/*      */       
/*      */ 
/*      */ 
/*      */       public boolean contains(String arg0)
/*      */       {
/* 1462 */         return false;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */       public void addPropertyChangeListener(IPropertyChangeListener arg0) {}
/* 1469 */     }, "tquestion");
/* 1470 */     return t;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected String input(String title, String message, String inival)
/*      */   {
/* 1509 */     title = "Sexftp - " + LangUtil.langText(title);
/* 1510 */     message = LangUtil.langText(message);
/* 1511 */     InputDialog input = new InputDialog(this.viewer.getControl().getShell(), title, message, inival, null);
/*      */     
/* 1513 */     int r = input.open();
/* 1514 */     if (r == 1)
/*      */     {
/* 1516 */       throw new AbortException("Cancel Input");
/*      */     }
/* 1518 */     return input.getValue();
/*      */   }
/*      */   
/*      */ 
/* 1522 */   private Console console = null;
/*      */   
/*      */   protected void initConsole()
/*      */   {
/* 1526 */     if (this.console == null)
/*      */     {
/* 1528 */       this.console = Console.createConsole("SexFtpConsole", "Twitter bird.ico");
/*      */       
/*      */       try
/*      */       {
/* 1532 */         this.console.console("Welcome to Sexftp - " + Platform.asLocalURL(Platform.getBundle("sexftp").getEntry("")).getFile());
/*      */       } catch (IOException e) {
/* 1534 */         this.console.console("Get SexFtp Bundle Failed!" + e.toString());
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected void openConsole() {
/* 1540 */     initConsole();
/*      */     
/* 1542 */     this.console.openConsole();
/*      */   }
/*      */   
/*      */   public void console(String text)
/*      */   {
/* 1547 */     initConsole();
/*      */     
/* 1549 */     this.console.console(text);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void handleException(final Throwable e)
/*      */   {
/* 1556 */     Display.getDefault().asyncExec(new Runnable()
/*      */     {
/*      */       public void run() {
/* 1559 */         if ((e instanceof BizException))
/*      */         {
/* 1561 */           AbstractSexftpView.this.showError(e.getMessage());
/* 1562 */           LogUtil.error(e.getMessage(), e);
/*      */         }
/* 1564 */         else if ((e instanceof AbortException))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/* 1569 */           if (e.getMessage() != null)
/*      */           {
/* 1571 */             AbstractSexftpView.this.console(e.getMessage());
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/* 1576 */           AbstractSexftpView.this.openConsole();
/* 1577 */           AbstractSexftpView.this.console(e.getMessage());
/* 1578 */           LogUtil.error(e.getMessage(), e);
/* 1579 */           AbstractSexftpView.this.showError(e.getMessage(), e);
/*      */         }
/*      */       }
/*      */     });
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void directTo(String expandPath, Integer ftpUploadTreeNodesIndex) {}
/*      */   
/*      */ 
/*      */   public TreeViewer getViewer()
/*      */   {
/* 1592 */     return this.viewer;
/*      */   }
/*      */   
/*      */ 
/*      */   public TreeParent getRoot()
/*      */   {
/* 1598 */     return this.invisibleRoot;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setFocus()
/*      */   {
/* 1605 */     this.viewer.getControl().setFocus();
/*      */   }
/*      */   
/*      */   public static void main(String[] args) {
/* 1609 */     System.out.println(HtmlEncoder.encode("&"));
/*      */   }
/*      */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\sexftp\views\AbstractSexftpView.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */