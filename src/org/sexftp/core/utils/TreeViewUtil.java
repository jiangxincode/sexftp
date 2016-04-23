/*    */ package org.sexftp.core.utils;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.eclipse.jface.viewers.TreePath;
/*    */ import sexftp.views.AbstractSexftpView.TreeObject;
/*    */ import sexftp.views.AbstractSexftpView.TreeParent;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TreeViewUtil
/*    */ {
/*    */   public static class ThisYourFind
/*    */   {
/*    */     private boolean isThisYourFind;
/*    */     private boolean findChild;
/*    */     
/*    */     public ThisYourFind(boolean isThisYourFind, boolean findChild)
/*    */     {
/* 26 */       this.isThisYourFind = isThisYourFind;
/* 27 */       this.findChild = findChild;
/*    */     }
/*    */     
/* 30 */     public boolean isThisYourFind() { return this.isThisYourFind; }
/*    */     
/*    */     public void setThisYourFind(boolean isThisYourFind) {
/* 33 */       this.isThisYourFind = isThisYourFind;
/*    */     }
/*    */     
/* 36 */     public boolean isFindChild() { return this.findChild; }
/*    */     
/*    */     public void setFindChild(boolean findChild) {
/* 39 */       this.findChild = findChild;
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public static void serchTreeDatas(AbstractSexftpView.TreeObject p, SearchCallback c, List<AbstractSexftpView.TreeObject> yourLis)
/*    */   {
/* 46 */     ThisYourFind thisYourFind = c.isThisYourFind(p);
/* 47 */     if (thisYourFind.isThisYourFind())
/*    */     {
/* 49 */       yourLis.add(p);
/*    */     }
/*    */     
/* 52 */     if (((p instanceof AbstractSexftpView.TreeParent)) && (thisYourFind.isFindChild())) {
/*    */       AbstractSexftpView.TreeObject[] arrayOfTreeObject;
/* 54 */       int j = (arrayOfTreeObject = ((AbstractSexftpView.TreeParent)p).getChildren()).length; for (int i = 0; i < j; i++) { AbstractSexftpView.TreeObject to = arrayOfTreeObject[i];
/*    */         
/* 56 */         serchTreeDatas(to, c, yourLis);
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public static AbstractSexftpView.TreeObject serchTreeData(AbstractSexftpView.TreeObject p, SearchCallback c)
/*    */   {
/* 63 */     List<AbstractSexftpView.TreeObject> yourLis = new ArrayList();
/* 64 */     serchTreeDatas(p, c, yourLis);
/* 65 */     if (yourLis.size() > 0) return (AbstractSexftpView.TreeObject)yourLis.get(0);
/* 66 */     return null;
/*    */   }
/*    */   
/*    */   public static TreePath changeTreePath(AbstractSexftpView.TreeObject to)
/*    */   {
/* 71 */     List<AbstractSexftpView.TreeObject> tLlist = new ArrayList();
/* 72 */     AbstractSexftpView.TreeObject c = to;
/* 73 */     for (int i = 0; i < 50; i++)
/*    */     {
/* 75 */       tLlist.add(c);
/* 76 */       c = c.getParent();
/* 77 */       if (c == null) break;
/*    */     }
/* 79 */     Object[] tps = new Object[tLlist.size()];
/* 80 */     int i = tLlist.size() - 1; for (int j = 0; i >= 0; j++)
/*    */     {
/* 82 */       tps[j] = tLlist.get(i);i--;
/*    */     }
/* 84 */     return new TreePath(tps);
/*    */   }
/*    */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\org\sexftp\core\utils\TreeViewUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */