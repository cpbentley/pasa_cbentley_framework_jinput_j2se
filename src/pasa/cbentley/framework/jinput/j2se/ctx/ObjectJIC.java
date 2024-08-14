package pasa.cbentley.framework.jinput.j2se.ctx;

import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IDLog;
import pasa.cbentley.core.src4.logging.IStringable;

public class ObjectJIC implements IStringable {

   protected final JInputCtx jic;

   public ObjectJIC(JInputCtx jic) {
      this.jic = jic;
   }

   //#mdebug
   public IDLog toDLog() {
      return toStringGetUCtx().toDLog();
   }

   public String toString() {
      return Dctx.toString(this);
   }

   public void toString(Dctx dc) {
      dc.root(this, ObjectJIC.class, 26);
      toStringPrivate(dc);
   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   private void toStringPrivate(Dctx dc) {

   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, ObjectJIC.class);
      toStringPrivate(dc);
   }

   public UCtx toStringGetUCtx() {
      return jic.getUC();
   }

   //#enddebug

}
