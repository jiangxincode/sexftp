/*    */ package sexftp.editors;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.HashMap;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ import org.eclipse.swt.graphics.Color;
/*    */ import org.eclipse.swt.graphics.RGB;
/*    */ import org.eclipse.swt.widgets.Display;
/*    */ 
/*    */ public class ColorManager
/*    */ {
/* 13 */   protected Map fColorTable = new HashMap(10);
/*    */   
/*    */   public void dispose() {
/* 16 */     Iterator e = this.fColorTable.values().iterator();
/* 17 */     while (e.hasNext())
/* 18 */       ((Color)e.next()).dispose();
/*    */   }
/*    */   
/* 21 */   public Color getColor(RGB rgb) { Color color = (Color)this.fColorTable.get(rgb);
/* 22 */     if (color == null) {
/* 23 */       color = new Color(Display.getCurrent(), rgb);
/* 24 */       this.fColorTable.put(rgb, color);
/*    */     }
/* 26 */     return color;
/*    */   }
/*    */ }


/* Location:              C:\Users\jiang\Desktop\sexftp-2012.0.0\sexftp_2012.0.0.201202120942.jar!\sexftp\editors\ColorManager.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */