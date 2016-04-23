/*      */ package sexftp.views;
/*      */ 
/*      */ import java.io.File;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.lang.reflect.Method;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import org.eclipse.core.runtime.IAdaptable;
/*      */ import org.eclipse.jface.action.Action;
/*      */ import org.eclipse.jface.action.IMenuListener;
/*      */ import org.eclipse.jface.action.IMenuManager;
/*      */ import org.eclipse.jface.action.IToolBarManager;
/*      */ import org.eclipse.jface.action.MenuManager;
/*      */ import org.eclipse.jface.action.Separator;
/*      */ import org.eclipse.jface.dialogs.MessageDialog;
/*      */ import org.eclipse.jface.resource.ImageDescriptor;
/*      */ import org.eclipse.jface.util.IPropertyChangeListener;
/*      */ import org.eclipse.jface.util.PropertyChangeEvent;
/*      */ import org.eclipse.jface.viewers.CellEditor;
/*      */ import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
/*      */ import org.eclipse.jface.viewers.ColumnWeightData;
/*      */ import org.eclipse.jface.viewers.ComboBoxCellEditor;
/*      */ import org.eclipse.jface.viewers.DoubleClickEvent;
/*      */ import org.eclipse.jface.viewers.ICellModifier;
/*      */ import org.eclipse.jface.viewers.IDoubleClickListener;
/*      */ import org.eclipse.jface.viewers.IStructuredContentProvider;
/*      */ import org.eclipse.jface.viewers.ITableLabelProvider;
/*      */ import org.eclipse.jface.viewers.ITreeContentProvider;
/*      */ import org.eclipse.jface.viewers.LabelProvider;
/*      */ import org.eclipse.jface.viewers.TableLayout;
/*      */ import org.eclipse.jface.viewers.TreeViewer;
/*      */ import org.eclipse.jface.viewers.TreeViewerEditor;
/*      */ import org.eclipse.jface.viewers.Viewer;
/*      */ import org.eclipse.jface.viewers.ViewerSorter;
/*      */ import org.eclipse.swt.custom.CCombo;
/*      */ import org.eclipse.swt.graphics.Image;
/*      */ import org.eclipse.swt.widgets.Composite;
/*      */ import org.eclipse.swt.widgets.Control;
/*      */ import org.eclipse.swt.widgets.Display;
/*      */ import org.eclipse.swt.widgets.Menu;
/*      */ import org.eclipse.swt.widgets.Tree;
/*      */ import org.eclipse.swt.widgets.TreeColumn;
/*      */ import org.eclipse.swt.widgets.TreeItem;
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
/*      */ import org.sexftp.core.exceptions.AbortException;
/*      */ import org.sexftp.core.exceptions.BizException;
/*      */ import org.sexftp.core.exceptions.SRuntimeException;
/*      */ import org.sexftp.core.utils.Cpdetector;
/*      */ import org.sexftp.core.utils.StringUtil;
/*      */ import sexftp.SrcViewable;
/*      */ import sexftp.uils.Console;
/*      */ import sexftp.uils.LangUtil;
/*      */ import sexftp.uils.LogUtil;
/*      */ import sexftp.uils.PluginUtil;
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
/*      */ public class AbstractSexftpEncodView
/*      */   extends ViewPart
/*      */   implements SrcViewable
/*      */ {
/*      */   public static final String ID = "sexftp.views.SexftpEncodView";
/*   95 */   private static final String[] SUPORT_CHARSET = Cpdetector.SUPORT_CHARSET;
/*      */   
/*      */   protected TreeViewer viewer;
/*      */   
/*      */   private DrillDownAdapter drillDownAdapter;
/*      */   
/*      */   protected Action action1;
/*      */   
/*      */   protected Action applyChanges;
/*      */   
/*      */   private Action filedillAction;
/*      */   
/*      */   protected Action doubleClickAction;
/*      */   
/*      */   protected Action actionCopy;
/*      */   
/*      */   protected Action actionCopyQualifiedName;
/*      */   
/*      */   protected ParentCorCod invisibleRoot;
/*      */   
/*      */ 
/*      */   public class CorCod
/*      */     implements IAdaptable, Comparable<CorCod>
/*      */   {
/*      */     private String name;
/*      */     
/*      */     private AbstractSexftpEncodView.ParentCorCod parent;
/*      */     
/*  123 */     private String parentFolder = "";
/*  124 */     private String endexten = "*.jsp";
/*  125 */     private String oldfileencode = "gbk";
/*  126 */     private String fileencode = "gbk";
/*  127 */     private String eclipseeditorencode = "utf-8";
/*  128 */     private String chengeDes = "";
/*  129 */     private String samples = "";
/*  130 */     private String fileType = "";
/*  131 */     private String descIcon = "";
/*      */     
/*      */     public String getEndexten() {
/*  134 */       return this.endexten;
/*      */     }
/*      */     
/*  137 */     public void setEndexten(String endexten) { this.endexten = endexten; }
/*      */     
/*      */ 
/*      */     public String getFileencode()
/*      */     {
/*  142 */       return this.fileencode;
/*      */     }
/*      */     
/*      */ 
/*      */     public void setFileencode(String fileencode)
/*      */     {
/*  148 */       this.fileencode = fileencode;
/*      */     }
/*      */     
/*      */     public String getOldfileencode() {
/*  152 */       return this.oldfileencode;
/*      */     }
/*      */     
/*      */     public void setOldfileencode(String oldfileencode)
/*      */     {
/*  157 */       this.oldfileencode = oldfileencode;
/*      */     }
/*      */     
/*  160 */     public String getEclipseeditorencode() { return this.eclipseeditorencode; }
/*      */     
/*      */     public void setEclipseeditorencode(String eclipseeditorencode) {
/*  163 */       this.eclipseeditorencode = eclipseeditorencode;
/*      */     }
/*      */     
/*  166 */     public String getChengeDes() { return this.chengeDes; }
/*      */     
/*      */     public void setChengeDes(String chengeDes) {
/*  169 */       this.chengeDes = chengeDes;
/*      */     }
/*      */     
/*  172 */     public String getParentFolder() { return this.parentFolder; }
/*      */     
/*      */     public void setParentFolder(String parentFolder) {
/*  175 */       this.parentFolder = parentFolder;
/*      */     }
/*      */     
/*      */     public String getFileType()
/*      */     {
/*  180 */       return this.fileType;
/*      */     }
/*      */     
/*  183 */     public void setFileType(String fileType) { this.fileType = fileType; }
/*      */     
/*      */     public String getFileencodeText()
/*      */     {
/*  187 */       return getParent().getFileencode();
/*      */     }
/*      */     
/*  190 */     public String getEndextenText() { return new File(getEndexten()).getName(); }
/*      */     
/*      */     public String getOldfileencodeText() {
/*  193 */       return getParent().getOldfileencode();
/*      */     }
/*      */     
/*  196 */     public String getParentFolderText() { return new File(getEndexten()).getParent(); }
/*      */     
/*      */     public String getSamples()
/*      */     {
/*  200 */       return this.samples;
/*      */     }
/*      */     
/*  203 */     public void setSamples(String samples) { this.samples = samples; }
/*      */     
/*      */     public String getDescIcon()
/*      */     {
/*  207 */       return this.descIcon;
/*      */     }
/*      */     
/*  210 */     public void setDescIcon(String descIcon) { this.descIcon = descIcon; }
/*      */     
/*      */ 
/*      */     public int hashCode()
/*      */     {
/*  215 */       int result = 1;
/*  216 */       result = 31 * result + getOuterType().hashCode();
/*  217 */       result = 31 * result + (
/*  218 */         this.chengeDes == null ? 0 : this.chengeDes.hashCode());
/*  219 */       result = 31 * 
/*  220 */         result + (
/*  221 */         this.eclipseeditorencode == null ? 0 : this.eclipseeditorencode
/*  222 */         .hashCode());
/*  223 */       result = 31 * result + (
/*  224 */         this.endexten == null ? 0 : this.endexten.hashCode());
/*  225 */       result = 31 * result + (
/*  226 */         this.fileType == null ? 0 : this.fileType.hashCode());
/*  227 */       result = 31 * result + (
/*  228 */         this.fileencode == null ? 0 : this.fileencode.hashCode());
/*  229 */       result = 31 * result + (
/*  230 */         this.oldfileencode == null ? 0 : this.oldfileencode.hashCode());
/*  231 */       result = 31 * result + (
/*  232 */         this.parentFolder == null ? 0 : this.parentFolder.hashCode());
/*  233 */       return result;
/*      */     }
/*      */     
/*      */     public boolean equals(Object obj) {
/*  237 */       if (this == obj)
/*  238 */         return true;
/*  239 */       if (obj == null)
/*  240 */         return false;
/*  241 */       if (getClass() != obj.getClass())
/*  242 */         return false;
/*  243 */       CorCod other = (CorCod)obj;
/*  244 */       if (!getOuterType().equals(other.getOuterType()))
/*  245 */         return false;
/*  246 */       if (this.chengeDes == null) {
/*  247 */         if (other.chengeDes != null)
/*  248 */           return false;
/*  249 */       } else if (!this.chengeDes.equals(other.chengeDes))
/*  250 */         return false;
/*  251 */       if (this.eclipseeditorencode == null) {
/*  252 */         if (other.eclipseeditorencode != null)
/*  253 */           return false;
/*  254 */       } else if (!this.eclipseeditorencode.equals(other.eclipseeditorencode))
/*  255 */         return false;
/*  256 */       if (this.endexten == null) {
/*  257 */         if (other.endexten != null)
/*  258 */           return false;
/*  259 */       } else if (!this.endexten.equals(other.endexten))
/*  260 */         return false;
/*  261 */       if (this.fileType == null) {
/*  262 */         if (other.fileType != null)
/*  263 */           return false;
/*  264 */       } else if (!this.fileType.equals(other.fileType))
/*  265 */         return false;
/*  266 */       if (this.fileencode == null) {
/*  267 */         if (other.fileencode != null)
/*  268 */           return false;
/*  269 */       } else if (!this.fileencode.equals(other.fileencode))
/*  270 */         return false;
/*  271 */       if (this.oldfileencode == null) {
/*  272 */         if (other.oldfileencode != null)
/*  273 */           return false;
/*  274 */       } else if (!this.oldfileencode.equals(other.oldfileencode))
/*  275 */         return false;
/*  276 */       if (this.parentFolder == null) {
/*  277 */         if (other.parentFolder != null)
/*  278 */           return false;
/*  279 */       } else if (!this.parentFolder.equals(other.parentFolder))
/*  280 */         return false;
/*  281 */       return true;
/*      */     }
/*      */     
/*  284 */     public int compareTo(CorCod o) { return getEndexten().compareTo(o.getEndexten()); }
/*      */     
/*      */     private AbstractSexftpEncodView getOuterType()
/*      */     {
/*  288 */       return AbstractSexftpEncodView.this;
/*      */     }
/*      */     
/*  291 */     public String toSimpleString(int maxlen) { return String.format("%s", new Object[] { this.endexten }); }
/*      */     
/*      */ 
/*      */ 
/*      */     public CorCod(String name)
/*      */     {
/*  297 */       this.name = name;
/*      */     }
/*      */     
/*  300 */     public String getName() { return this.name; }
/*      */     
/*      */     public void setParent(AbstractSexftpEncodView.ParentCorCod parent) {
/*  303 */       this.parent = parent;
/*      */     }
/*      */     
/*  306 */     public AbstractSexftpEncodView.ParentCorCod getParent() { return this.parent; }
/*      */     
/*      */ 
/*      */     public String toString()
/*      */     {
/*  311 */       return 
/*  312 */         String.format(
/*  313 */         "%s\t%s\t%s\t%s\t%s\t%s", new Object[] {
/*  314 */         this.parentFolder, this.endexten, this.oldfileencode, this.fileencode, this.chengeDes, this.samples });
/*      */     }
/*      */     
/*      */ 
/*  318 */     public Object getAdapter(Class key) { return null; }
/*      */   }
/*      */   
/*      */   class NameSorter extends ViewerSorter {
/*      */     NameSorter() {}
/*      */   }
/*      */   
/*      */   public class ParentCorCod extends AbstractSexftpEncodView.CorCod {
/*      */     private List<AbstractSexftpEncodView.CorCod> children;
/*      */     
/*  328 */     public ParentCorCod(String name) { super(name);
/*  329 */       this.children = new ArrayList();
/*      */     }
/*      */     
/*  332 */     public void addChild(AbstractSexftpEncodView.CorCod child) { this.children.add(child);
/*  333 */       child.setParent(this);
/*      */     }
/*      */     
/*  336 */     public void removeChild(AbstractSexftpEncodView.CorCod child) { this.children.remove(child);
/*  337 */       child.setParent(null);
/*      */     }
/*      */     
/*  340 */     public AbstractSexftpEncodView.CorCod[] getChildren() { return (AbstractSexftpEncodView.CorCod[])this.children.toArray(new AbstractSexftpEncodView.CorCod[this.children.size()]); }
/*      */     
/*      */     public boolean hasChildren() {
/*  343 */       return this.children.size() > 0;
/*      */     }
/*      */     
/*      */     public String getFileencodeText() {
/*  347 */       return getFileencode();
/*      */     }
/*      */     
/*  350 */     public String getEndextenText() { return getEndexten(); }
/*      */     
/*      */     public String getOldfileencodeText() {
/*  353 */       return getOldfileencode();
/*      */     }
/*      */     
/*  356 */     public String getParentFolderText() { return getParentFolder(); }
/*      */   }
/*      */   
/*      */   class ViewContentProvider implements IStructuredContentProvider, ITreeContentProvider
/*      */   {
/*      */     ViewContentProvider() {}
/*      */     
/*      */     public void inputChanged(Viewer v, Object oldInput, Object newInput) {}
/*      */     
/*      */     public void dispose() {}
/*      */     
/*      */     public Object[] getElements(Object parent)
/*      */     {
/*  369 */       if (parent.equals(AbstractSexftpEncodView.this.getViewSite())) {
/*  370 */         if (AbstractSexftpEncodView.this.invisibleRoot == null) initialize();
/*  371 */         return getChildren(AbstractSexftpEncodView.this.invisibleRoot);
/*      */       }
/*  373 */       return getChildren(parent);
/*      */     }
/*      */     
/*  376 */     public Object getParent(Object child) { if ((child instanceof AbstractSexftpEncodView.CorCod)) {
/*  377 */         return ((AbstractSexftpEncodView.CorCod)child).getParent();
/*      */       }
/*  379 */       return null;
/*      */     }
/*      */     
/*  382 */     public Object[] getChildren(Object parent) { if ((parent instanceof AbstractSexftpEncodView.ParentCorCod)) {
/*  383 */         return ((AbstractSexftpEncodView.ParentCorCod)parent).getChildren();
/*      */       }
/*  385 */       return new Object[0];
/*      */     }
/*      */     
/*  388 */     public boolean hasChildren(Object parent) { if ((parent instanceof AbstractSexftpEncodView.ParentCorCod))
/*  389 */         return ((AbstractSexftpEncodView.ParentCorCod)parent).hasChildren();
/*  390 */       return false;
/*      */     }
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
/*      */     private void initialize()
/*      */     {
/*  414 */       new AbstractSexftpEncodView.ParentCorCod(AbstractSexftpEncodView.this, "Root");
/*  415 */       AbstractSexftpEncodView.this.invisibleRoot = new AbstractSexftpEncodView.ParentCorCod(AbstractSexftpEncodView.this, "");
/*      */     }
/*      */   }
/*      */   
/*      */   class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
/*      */     ViewLabelProvider() {}
/*      */     
/*  422 */     public String getText(Object obj) { return obj.toString(); }
/*      */     
/*      */     public Image getImage(Object obj) {
/*  425 */       String imageKey = "IMG_OBJ_ELEMENTS";
/*  426 */       if ((obj instanceof AbstractSexftpEncodView.ParentCorCod))
/*  427 */         imageKey = "IMG_OBJ_FOLDER";
/*  428 */       return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
/*      */     }
/*      */     
/*  431 */     public Image getColumnImage(Object obj, int index) { String sexFtpIcon = null;
/*  432 */       if ((obj instanceof AbstractSexftpEncodView.ParentCorCod))
/*      */       {
/*  434 */         if (index == 0)
/*      */         {
/*  436 */           sexFtpIcon = "Orange forum.ico";
/*      */         }
/*      */         
/*      */ 
/*      */       }
/*  441 */       else if (index == 0)
/*      */       {
/*  443 */         sexFtpIcon = "Technorati.ico";
/*      */       }
/*      */       
/*  446 */       if (index == 4)
/*      */       {
/*  448 */         sexFtpIcon = ((AbstractSexftpEncodView.CorCod)obj).getDescIcon();
/*      */       }
/*  450 */       if ((sexFtpIcon != null) && (sexFtpIcon.length() > 0))
/*  451 */         return AbstractUIPlugin.imageDescriptorFromPlugin("sexftp", "/icons/" + sexFtpIcon).createImage();
/*  452 */       return null;
/*      */     }
/*      */     
/*  455 */     public String getColumnText(Object obj, int index) { AbstractSexftpEncodView.CorCod c = (AbstractSexftpEncodView.CorCod)obj;
/*  456 */       if (index == 0)
/*  457 */         return getText(c.getParentFolderText());
/*  458 */       if (index == 1)
/*  459 */         return getText(c.getEndextenText());
/*  460 */       if (index == 2)
/*      */       {
/*  462 */         return getText(c.getOldfileencodeText());
/*      */       }
/*  464 */       if (index == 3)
/*      */       {
/*  466 */         return getText(c.getFileencodeText());
/*      */       }
/*  468 */       if (index == 4)
/*  469 */         return getText(LangUtil.langText(c.getChengeDes()));
/*  470 */       if (index == 5)
/*  471 */         return getText(c.getSamples());
/*  472 */       return getText(obj);
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
/*      */ 
/*      */   public void createPartControl(Composite parent)
/*      */   {
/*  490 */     this.viewer = new TreeViewer(parent, 66306);
/*  491 */     this.drillDownAdapter = new DrillDownAdapter(this.viewer);
/*  492 */     this.viewer.setContentProvider(new ViewContentProvider());
/*  493 */     this.viewer.setLabelProvider(new ViewLabelProvider());
/*  494 */     this.viewer.setSorter(new NameSorter());
/*  495 */     this.viewer.setInput(getViewSite());
/*      */     
/*  497 */     Tree tree = this.viewer.getTree();
/*  498 */     tree.setHeaderVisible(true);
/*  499 */     tree.setLinesVisible(true);
/*      */     
/*  501 */     TableLayout tLayout = new TableLayout();
/*  502 */     tree.setLayout(tLayout);
/*      */     
/*  504 */     tLayout.addColumnData(new ColumnWeightData(50));
/*  505 */     new TreeColumn(tree, 0).setText(LangUtil.langText("Folder"));
/*  506 */     tLayout.addColumnData(new ColumnWeightData(15));
/*  507 */     new TreeColumn(tree, 0).setText(LangUtil.langText("File association"));
/*  508 */     tLayout.addColumnData(new ColumnWeightData(15));
/*  509 */     new TreeColumn(tree, 0).setText(LangUtil.langText("File Encoding"));
/*  510 */     tLayout.addColumnData(new ColumnWeightData(15));
/*  511 */     new TreeColumn(tree, 0).setText(LangUtil.langText("File New Encoding"));
/*  512 */     tLayout.addColumnData(new ColumnWeightData(40));
/*  513 */     new TreeColumn(tree, 0).setText(LangUtil.langText("Description"));
/*  514 */     tLayout.addColumnData(new ColumnWeightData(50));
/*  515 */     new TreeColumn(tree, 0).setText(LangUtil.langText("Samples"));
/*      */     
/*      */ 
/*  518 */     TreeViewerEditor.create(this.viewer, new ColumnViewerEditorActivationStrategy(this.viewer), 1);
/*  519 */     CellEditor[] cellEditors = new CellEditor[5];
/*      */     
/*      */ 
/*  522 */     ComboBoxCellEditor ce = new ComboBoxCellEditor(tree, SUPORT_CHARSET);
/*  523 */     cellEditors[2] = ce;
/*  524 */     cellEditors[3] = ce;
/*  525 */     ce.addPropertyChangeListener(new IPropertyChangeListener()
/*      */     {
/*      */       public void propertyChange(PropertyChangeEvent e) {
/*  528 */         e.getOldValue();
/*      */       }
/*      */       
/*  531 */     });
/*  532 */     this.viewer.setCellEditors(cellEditors);
/*  533 */     this.viewer.setColumnProperties(new String[] { "00", "01", "02", "03", "04", "07" });
/*  534 */     this.viewer.setCellModifier(new ICellModifier()
/*      */     {
/*      */       public void modify(Object arg0, String p, Object val)
/*      */       {
/*      */         try
/*      */         {
/*  540 */           TreeItem ti = (TreeItem)arg0;
/*  541 */           AbstractSexftpEncodView.CorCod c = (AbstractSexftpEncodView.CorCod)ti.getData();
/*  542 */           if (p.equals("01"))
/*      */           {
/*  544 */             c.setEndexten(val);
/*  545 */             ti.setText(1, val);
/*      */           }
/*  547 */           if (p.equals("02"))
/*      */           {
/*  549 */             CCombo cc = (CCombo)AbstractSexftpEncodView.this.viewer.getCellEditors()[2].getControl();
/*  550 */             "test".getBytes(cc.getText());
/*  551 */             AbstractSexftpEncodView.this.oldEncodeChanged(c, c.getOldfileencode(), cc.getText());
/*      */             
/*  553 */             c.setOldfileencode(cc.getText());
/*      */             
/*      */ 
/*  556 */             ti.setText(2, cc.getText());
/*  557 */             if (!c.getOldfileencode().equalsIgnoreCase(c.getFileencode()))
/*      */             {
/*  559 */               c.setChengeDes(String.format("Change: [%s -> %s]", new Object[] { c.getOldfileencode(), c.getFileencode() }));
/*  560 */               c.setDescIcon("run_exc.gif");
/*  561 */               ti.setText(4, c.getChengeDes());
/*      */             }
/*  563 */             else if ((ti.getText(4) != null) && (ti.getText(4).indexOf(" -> ") >= 0))
/*      */             {
/*  565 */               c.setChengeDes("");
/*  566 */               ti.setText(4, "");
/*  567 */               c.setDescIcon("");
/*      */             }
/*      */           }
/*  570 */           if (p.equals("03"))
/*      */           {
/*  572 */             CCombo cc = (CCombo)AbstractSexftpEncodView.this.viewer.getCellEditors()[2].getControl();
/*  573 */             "test".getBytes(cc.getText());
/*  574 */             c.setFileencode(cc.getText());
/*  575 */             ti.setText(3, cc.getText());
/*  576 */             if (!c.getOldfileencode().equalsIgnoreCase(c.getFileencode()))
/*      */             {
/*  578 */               c.setChengeDes(String.format("Change: [%s -> %s]", new Object[] { c.getOldfileencode(), c.getFileencode() }));
/*  579 */               c.setDescIcon("run_exc.gif");
/*  580 */               ti.setText(4, c.getChengeDes());
/*      */ 
/*      */             }
/*  583 */             else if ((ti.getText(4) != null) && (ti.getText(4).indexOf(" -> ") >= 0))
/*      */             {
/*  585 */               c.setChengeDes("");
/*      */               
/*  587 */               ti.setText(4, "");
/*  588 */               c.setDescIcon("");
/*      */             }
/*      */           }
/*      */         }
/*      */         catch (UnsupportedEncodingException localUnsupportedEncodingException) {}
/*      */         
/*  594 */         AbstractSexftpEncodView.this.viewer.refresh(true);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */       public Object getValue(Object arg0, String p)
/*      */       {
/*  602 */         AbstractSexftpEncodView.CorCod c = (AbstractSexftpEncodView.CorCod)arg0;
/*      */         
/*  604 */         if (p.equals("01"))
/*      */         {
/*  606 */           return AbstractSexftpEncodView.CorCod.access$1(c);
/*      */         }
/*  608 */         if (p.equals("02"))
/*      */         {
/*  610 */           CCombo cc = (CCombo)AbstractSexftpEncodView.this.viewer.getCellEditors()[2].getControl();
/*  611 */           for (int i = 0; i < cc.getItemCount(); i++)
/*      */           {
/*  613 */             String item = cc.getItem(i);
/*  614 */             if (item.equals(c.getOldfileencode()))
/*      */             {
/*  616 */               return Integer.valueOf(i);
/*      */             }
/*      */           }
/*  619 */           return Integer.valueOf(0);
/*      */         }
/*  621 */         if (p.equals("03"))
/*      */         {
/*  623 */           CCombo cc = (CCombo)AbstractSexftpEncodView.this.viewer.getCellEditors()[3].getControl();
/*  624 */           for (int i = 0; i < cc.getItemCount(); i++)
/*      */           {
/*  626 */             String item = cc.getItem(i);
/*  627 */             if (item.equals(c.getFileencode()))
/*      */             {
/*  629 */               return Integer.valueOf(i);
/*      */             }
/*      */           }
/*  632 */           return Integer.valueOf(0);
/*      */         }
/*      */         
/*  635 */         return p;
/*      */       }
/*      */       
/*      */       public boolean canModify(Object arg0, String arg1)
/*      */       {
/*  640 */         AbstractSexftpEncodView.CorCod c = (AbstractSexftpEncodView.CorCod)arg0;
/*  641 */         if (c.getFileencode().indexOf("ASCII") >= 0)
/*      */         {
/*  643 */           return false;
/*      */         }
/*  645 */         if ((c instanceof AbstractSexftpEncodView.ParentCorCod))
/*      */         {
/*  647 */           return true;
/*      */         }
/*      */         
/*  650 */         return false;
/*      */ 
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  656 */     });
/*  657 */     PlatformUI.getWorkbench().getHelpSystem().setHelp(this.viewer.getControl(), "sexftp.viewer");
/*  658 */     makeActions();
/*  659 */     hookContextMenu();
/*  660 */     hookDoubleClickAction();
/*  661 */     contributeToActionBars();
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
/*      */   protected void oldEncodeChanged(CorCod c, String encode, String newencode) {}
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
/*  820 */   protected List<ParentCorCod> corcodeList = new ArrayList();
/*      */   
/*      */   public void refreshData(final List<ParentCorCod> corcodeList) {
/*  823 */     Display.getDefault().asyncExec(new Runnable()
/*      */     {
/*      */       public void run() {
/*  826 */         AbstractSexftpEncodView.this.corcodeList = corcodeList;
/*  827 */         AbstractSexftpEncodView.this.invisibleRoot = new AbstractSexftpEncodView.ParentCorCod(AbstractSexftpEncodView.this, "");
/*  828 */         for (AbstractSexftpEncodView.ParentCorCod p : corcodeList)
/*      */         {
/*  830 */           AbstractSexftpEncodView.this.invisibleRoot.addChild(p);
/*      */         }
/*      */         
/*  833 */         AbstractSexftpEncodView.this.viewer.refresh();
/*      */       }
/*      */     }); }
/*      */   
/*      */   private void hookContextMenu()
/*      */   {
/*  839 */     MenuManager menuMgr = new MenuManager("#PopupMenu");
/*  840 */     menuMgr.setRemoveAllWhenShown(true);
/*  841 */     menuMgr.addMenuListener(new IMenuListener() {
/*      */       public void menuAboutToShow(IMenuManager manager) {
/*  843 */         AbstractSexftpEncodView.this.fillContextMenu(manager);
/*      */       }
/*  845 */     });
/*  846 */     Menu menu = menuMgr.createContextMenu(this.viewer.getControl());
/*  847 */     this.viewer.getControl().setMenu(menu);
/*  848 */     getSite().registerContextMenu(menuMgr, this.viewer);
/*      */   }
/*      */   
/*      */   private void contributeToActionBars() {
/*  852 */     IActionBars bars = getViewSite().getActionBars();
/*  853 */     fillLocalPullDown(bars.getMenuManager());
/*  854 */     fillLocalToolBar(bars.getToolBarManager());
/*      */   }
/*      */   
/*      */   private void fillLocalPullDown(IMenuManager manager) {
/*  858 */     manager.add(this.filedillAction);
/*  859 */     manager.add(new Separator());
/*  860 */     manager.add(this.applyChanges);
/*  861 */     manager.add(new Separator());
/*  862 */     manager.add(this.actionCopy);
/*  863 */     manager.add(this.actionCopyQualifiedName);
/*      */   }
/*      */   
/*      */   private void fillContextMenu(IMenuManager manager) {
/*  867 */     manager.add(this.filedillAction);
/*  868 */     manager.add(new Separator());
/*  869 */     manager.add(this.applyChanges);
/*  870 */     manager.add(new Separator());
/*  871 */     manager.add(this.actionCopy);
/*  872 */     manager.add(this.actionCopyQualifiedName);
/*      */     
/*      */ 
/*  875 */     manager.add(new Separator("additions"));
/*      */   }
/*      */   
/*      */   private void fillLocalToolBar(IToolBarManager manager) {
/*  879 */     manager.add(this.filedillAction);
/*  880 */     manager.add(new Separator());
/*  881 */     manager.add(this.applyChanges);
/*  882 */     manager.add(new Separator());
/*  883 */     manager.add(this.actionCopy);
/*  884 */     manager.add(this.actionCopyQualifiedName);
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
/*      */   private void makeActions()
/*      */   {
/*  900 */     this.actionCopy = new SexftpViewAction() {
/*      */       public void run() {
/*      */         try {
/*  903 */           AbstractSexftpEncodView.this.actionCopy_actionPerformed();
/*      */         } catch (Exception e) {
/*  905 */           AbstractSexftpEncodView.this.handleException(e);
/*      */         }
/*      */       }
/*  908 */     };
/*  909 */     this.actionCopy.setText("Copy");
/*  910 */     this.actionCopy.setToolTipText("Copy Name");
/*  911 */     this.actionCopy.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
/*  912 */       .getImageDescriptor("IMG_TOOL_COPY"));
/*      */     
/*  914 */     this.actionCopyQualifiedName = new SexftpViewAction() {
/*      */       public void run() {
/*      */         try {
/*  917 */           AbstractSexftpEncodView.this.actionCopyQualifiedName_actionPerformed();
/*      */         } catch (Exception e) {
/*  919 */           AbstractSexftpEncodView.this.handleException(e);
/*      */         }
/*      */       }
/*  922 */     };
/*  923 */     this.actionCopyQualifiedName.setText("Copy Qualified Name");
/*  924 */     this.actionCopyQualifiedName.setToolTipText("Copy Qualified Name");
/*  925 */     this.actionCopyQualifiedName.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
/*  926 */       .getImageDescriptor("IMG_TOOL_COPY"));
/*      */     
/*      */ 
/*      */ 
/*  930 */     this.filedillAction = new SexftpViewAction() {
/*      */       public void run() {
/*      */         try {
/*  933 */           AbstractSexftpEncodView.this.filedillAction_actionPerformed();
/*      */         } catch (Exception e) {
/*  935 */           AbstractSexftpEncodView.this.handleException(e);
/*      */         }
/*      */         
/*      */       }
/*  939 */     };
/*  940 */     this.filedillAction.setText("Chose Your Folder (&V)");
/*  941 */     this.filedillAction.setToolTipText("Chose Your Folder");
/*  942 */     this.filedillAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
/*  943 */       .getImageDescriptor("IMG_OBJ_FOLDER"));
/*      */     
/*      */ 
/*  946 */     this.applyChanges = new SexftpViewAction() {
/*      */       public void run() {
/*      */         try {
/*  949 */           AbstractSexftpEncodView.this.applyChanges_actionPerformed();
/*      */         } catch (Exception e) {
/*  951 */           AbstractSexftpEncodView.this.handleException(e);
/*      */         }
/*      */       }
/*  954 */     };
/*  955 */     this.applyChanges.setText("Apply Changes(&A)");
/*  956 */     this.applyChanges.setToolTipText("Apply Changes");
/*  957 */     this.applyChanges.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("sexftp", "/icons/run_exc.gif"));
/*  958 */     this.doubleClickAction = new Action() {
/*      */       public void run() {
/*      */         try {
/*  961 */           AbstractSexftpEncodView.this.doubleClick_actionPerformed();
/*      */         } catch (Exception e) {
/*  963 */           AbstractSexftpEncodView.this.handleException(e);
/*      */         }
/*      */       }
/*      */     };
/*      */   }
/*      */   
/*      */   protected void doubleClick_actionPerformed()
/*      */     throws Exception
/*      */   {}
/*      */   
/*      */   protected void applyChanges_actionPerformed()
/*      */     throws Exception
/*      */   {}
/*      */   
/*      */   protected void filedillAction_actionPerformed()
/*      */     throws Exception
/*      */   {}
/*      */   
/*      */   protected void actionCopyQualifiedName_actionPerformed()
/*      */     throws Exception
/*      */   {}
/*      */   
/*      */   protected void actionCopy_actionPerformed()
/*      */     throws Exception
/*      */   {}
/*      */   
/*      */   private void hookDoubleClickAction()
/*      */   {
/*  991 */     this.viewer.addDoubleClickListener(new IDoubleClickListener() {
/*      */       public void doubleClick(DoubleClickEvent event) {
/*  993 */         AbstractSexftpEncodView.this.doubleClickAction.run();
/*      */       }
/*      */     });
/*      */   }
/*      */   
/*  998 */   protected void showMessage(final String message) { Display.getDefault().syncExec(new Runnable()
/*      */     {
/*      */       public void run() {
/* 1001 */         MessageDialog.openInformation(
/* 1002 */           AbstractSexftpEncodView.this.viewer.getControl().getShell(), 
/* 1003 */           LangUtil.langText("Sexftp Charset Encoder"), 
/* 1004 */           LangUtil.langText(message));
/*      */       }
/*      */     }); }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setFocus()
/*      */   {
/* 1014 */     this.viewer.getControl().setFocus();
/*      */   }
/*      */   
/*      */ 
/*      */   public void console(final String str)
/*      */   {
/* 1020 */     Display.getDefault().asyncExec(new Runnable()
/*      */     {
/*      */       public void run() {
/* 1023 */         Console.createConsole("SexFtpConsole", "Twitter bird.ico").console(str);
/*      */       }
/*      */     });
/*      */   }
/*      */   
/* 1028 */   private IWorkbenchPage actPage = null;
/*      */   
/*      */   protected IWorkbenchPage getActPage() {
/* 1031 */     if (this.actPage == null)
/*      */     {
/* 1033 */       IWorkbenchPage activePage = PluginUtil.getActivePage();
/* 1034 */       this.actPage = activePage;
/*      */     }
/* 1036 */     return this.actPage;
/*      */   }
/*      */   
/*      */   public boolean showQuestion(final String message) {
/* 1040 */     Runnable runnable = new Runnable() {
/* 1041 */       private boolean q = false;
/*      */       
/* 1043 */       public void run() { this.q = MessageDialog.openQuestion(
/* 1044 */           AbstractSexftpEncodView.this.viewer.getControl().getShell(), 
/* 1045 */           LangUtil.langText("Sexftp Charset Encoder"), 
/* 1046 */           LangUtil.langText(message));
/*      */       }
/*      */       
/*      */       public boolean isOk()
/*      */       {
/* 1051 */         return this.q;
/*      */       }
/* 1053 */     };
/* 1054 */     Display.getDefault().syncExec(runnable);
/*      */     try {
/* 1056 */       return ((Boolean)runnable.getClass().getMethod("isOk", new Class[0]).invoke(runnable, new Object[0])).booleanValue();
/*      */     } catch (Exception e) {
/* 1058 */       throw new SRuntimeException(e);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void handleException(final Throwable e)
/*      */   {
/* 1065 */     Display.getDefault().asyncExec(new Runnable()
/*      */     {
/*      */       public void run() {
/* 1068 */         if ((e instanceof BizException))
/*      */         {
/* 1070 */           AbstractSexftpEncodView.this.showMessage(e.getMessage());
/*      */         }
/* 1072 */         else if (!(e instanceof AbortException))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1078 */           AbstractSexftpEncodView.this.console(StringUtil.readExceptionDetailInfo(e));
/* 1079 */           LogUtil.error(e.getMessage(), e);
/*      */         }
/*      */       }
/*      */     });
/*      */   }
/*      */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\sexftp\views\AbstractSexftpEncodView.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */