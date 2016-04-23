/*     */ package sexftp.views;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.eclipse.jface.action.Action;
/*     */ import org.eclipse.jface.viewers.ISelection;
/*     */ import org.eclipse.jface.viewers.IStructuredSelection;
/*     */ import org.eclipse.jface.viewers.TreeExpansionEvent;
/*     */ import org.eclipse.jface.viewers.TreeViewer;
/*     */ import org.sexftp.core.ftp.bean.FtpConf;
/*     */ import org.sexftp.core.ftp.bean.FtpUploadConf;
/*     */ import org.sexftp.core.ftp.bean.FtpUploadPro;
/*     */ import sexftp.uils.PluginUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SexftpSyncView
/*     */   extends SexftpLocalView
/*     */ {
/*     */   protected void actionEnableHandle()
/*     */   {
/*  26 */     super.actionEnableHandle();
/*  27 */     this.actionDirectSLocal.setEnabled(true);
/*  28 */     Object[] selectOs = getSelectionObjects(true);
/*  29 */     boolean hasFtpUploadPro = false;
/*  30 */     boolean hasNoFtpUploadPro = false;
/*  31 */     Object[] arrayOfObject1; int j = (arrayOfObject1 = selectOs).length; for (int i = 0; i < j; i++) { Object object = arrayOfObject1[i];
/*  32 */       if ((object instanceof FtpUploadPro))
/*     */       {
/*  34 */         hasFtpUploadPro = true;
/*     */       }
/*     */       else
/*     */       {
/*  38 */         hasNoFtpUploadPro = true;
/*     */       }
/*  40 */       if ((hasFtpUploadPro) && (hasNoFtpUploadPro)) {
/*     */         break;
/*     */       }
/*     */     }
/*  44 */     if (!hasFtpUploadPro)
/*     */     {
/*  46 */       this.actionUpload.setEnabled(false);
/*  47 */       this.actionCompare.setEnabled(false);
/*     */     }
/*     */     
/*     */ 
/*  51 */     if (hasNoFtpUploadPro)
/*     */     {
/*  53 */       this.actionCompare.setEnabled(false);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void doubleClickAction_actionPerformed() throws Exception {
/*  58 */     ISelection selection = this.viewer.getSelection();
/*  59 */     Object obj = ((IStructuredSelection)selection).getFirstElement();
/*  60 */     if ((obj instanceof AbstractSexftpView.TreeParent)) {
/*  61 */       super.doubleClickAction_actionPerformed();
/*     */     }
/*     */     else
/*     */     {
/*  65 */       actionCompare_actionPerformed();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public List getHiddenActions()
/*     */   {
/*  73 */     return Arrays.asList(new Object[] {
/*  74 */       this.actionRefreshFile });
/*     */   }
/*     */   
/*     */   protected void actionDirectSLocal_actionPerformed()
/*     */     throws Exception
/*     */   {
/*  80 */     Object[] os = getSelectionObjects();
/*  81 */     AbstractSexftpView.TreeParent selectObj = getSelectFtpUploadConfNodes()[0];
/*  82 */     SexftpLocalView mv = PluginUtil.findAndShowLocalView(PluginUtil.getActivePage());
/*  83 */     if ((os.length == 1) && ((os[0] instanceof FtpUploadPro)))
/*     */     {
/*  85 */       FtpUploadPro fupro = (FtpUploadPro)os[0];
/*  86 */       String client = fupro.getFtpUploadConf().getClientPath();
/*  87 */       AbstractSexftpView.TreeParent[] allFtpUploadConfNodes = getAllFtpUploadConfNodes();
/*  88 */       for (int i = 0; i < allFtpUploadConfNodes.length; i++)
/*     */       {
/*  90 */         if (allFtpUploadConfNodes[i] == selectObj)
/*     */         {
/*  92 */           mv.directTo(client, Integer.valueOf(i));
/*  93 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void actionUpload_actionPerformed() throws Exception {
/* 100 */     innerUpload_actionPerformed(getSelectionObjects(true));
/*     */   }
/*     */   
/*     */   protected void actionCompare_actionPerformed() throws Exception
/*     */   {
/* 105 */     innerCompare_actionPerformed(getSelectionObjects(true));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void doAfterSelectAndAddChildUploadPro(DoAfterSelectAndAddChildUploadPro run)
/*     */     throws Exception
/*     */   {
/* 129 */     super.doAfterSelectAndAddChildUploadPro(run);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 135 */   protected boolean copyTreeNodeIndoAfterSelectAndAddChildUploadPro() { return true; }
/*     */   
/* 137 */   private boolean treeVisibleCtrl = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void treeExpanded_actionPerformed(TreeExpansionEvent e)
/*     */     throws Exception
/*     */   {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void treeCollapsed_actionPerformed(TreeExpansionEvent e)
/*     */     throws Exception
/*     */   {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void refreshTreeViewData()
/*     */   {
/* 163 */     super.refreshTreeViewData();
/*     */     AbstractSexftpView.TreeObject[] arrayOfTreeObject1;
/* 165 */     int j = (arrayOfTreeObject1 = this.invisibleRoot.getChildren()).length; for (int i = 0; i < j; i++) { AbstractSexftpView.TreeObject ftpstart = arrayOfTreeObject1[i];
/*     */       AbstractSexftpView.TreeObject[] arrayOfTreeObject2;
/* 167 */       int m = (arrayOfTreeObject2 = ((AbstractSexftpView.TreeParent)ftpstart).getChildren()).length; for (int k = 0; k < m; k++) { AbstractSexftpView.TreeObject ftpConfNode = arrayOfTreeObject2[k];
/*     */         
/* 169 */         ftpConfNode.setVisible(false);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void actionPrepareServUpload_actionPerformed()
/*     */     throws Exception
/*     */   {
/* 180 */     super.actionPrepareServUpload_actionPerformed();
/*     */   }
/*     */   
/*     */   public void showDifView(AbstractSexftpView.TreeParent[] viewtps, Map<String, String> cusImgMap) {
/* 184 */     actionRefreshSexftp_actionPerformed();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 192 */     this.customizedImgMap = cusImgMap;
/*     */     AbstractSexftpView.TreeObject[] arrayOfTreeObject1;
/* 194 */     int j = (arrayOfTreeObject1 = this.invisibleRoot.getChildren()).length; for (int i = 0; i < j; i++) { AbstractSexftpView.TreeObject ftpstart = arrayOfTreeObject1[i];
/*     */       AbstractSexftpView.TreeObject[] arrayOfTreeObject2;
/* 196 */       int m = (arrayOfTreeObject2 = ((AbstractSexftpView.TreeParent)ftpstart).getChildren()).length; for (int k = 0; k < m; k++) { AbstractSexftpView.TreeObject ftpConfNode = arrayOfTreeObject2[k];
/*     */         
/* 198 */         AbstractSexftpView.TreeParent ftpConfNodeIn = (AbstractSexftpView.TreeParent)ftpConfNode;
/* 199 */         AbstractSexftpView.TreeObject[] arrayOfTreeObject3; int i1 = (arrayOfTreeObject3 = ftpConfNodeIn.getChildren()).length; for (int n = 0; n < i1; n++) { AbstractSexftpView.TreeObject ftpUploadConfNode = arrayOfTreeObject3[n];
/*     */           
/* 201 */           AbstractSexftpView.TreeParent ftpUpNode = (AbstractSexftpView.TreeParent)ftpUploadConfNode;
/* 202 */           if ((ftpUpNode.getO() instanceof FtpUploadConf))
/*     */           {
/* 204 */             if (this.treeVisibleCtrl)
/* 205 */               ftpUpNode.setVisible(false);
/* 206 */             AbstractSexftpView.TreeParent[] arrayOfTreeParent; int i3 = (arrayOfTreeParent = viewtps).length; for (int i2 = 0; i2 < i3; i2++) { AbstractSexftpView.TreeParent viewtp = arrayOfTreeParent[i2];
/*     */               
/* 208 */               if ((((FtpUploadConf)ftpUpNode.getO()).getClientPath().equals(((FtpUploadConf)viewtp.getO()).getClientPath())) && 
/* 209 */                 (((FtpConf)ftpUpNode.getParent().getO()).getName().equals(((FtpConf)viewtp.getParent().getO()).getName())))
/*     */               {
/* 211 */                 ftpConfNodeIn.setVisible(true);
/* 212 */                 ftpConfNodeIn.removeChild(ftpUpNode);
/* 213 */                 ftpConfNodeIn.addChild(viewtp);
/* 214 */                 refreshTreeView(ftpConfNodeIn);
/* 215 */                 refreshTreeView(viewtp);
/* 216 */                 break;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 223 */         this.viewer.refresh(ftpConfNodeIn);
/*     */       }
/*     */     }
/* 226 */     this.treeVisibleCtrl = true;
/*     */   }
/*     */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\sexftp\views\SexftpSyncView.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */