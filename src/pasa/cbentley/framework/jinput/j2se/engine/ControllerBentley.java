package pasa.cbentley.framework.jinput.j2se.engine;

import net.java.games.input.Controller;
import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IDLog;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.framework.coreui.src4.interfaces.IExternalDevice;
import pasa.cbentley.framework.jinput.j2se.ctx.JInputCtx;

public class ControllerBentley implements IStringable {

   /**
    * JInput wrapper of the external device
    */
   private final Controller  controller;

   /**
    * Interface to the device properties
    */
   private IExternalDevice   exdevice;

   protected final int       id;

   protected final JInputCtx jic;

   public ControllerBentley(JInputCtx jic, Controller controller, int gamePadID) {
      this.jic = jic;
      //#debug
      jic.toStringCheckNull(controller);
      this.controller = controller;
      this.id = gamePadID;
   }

   public Controller getController() {
      return controller;
   }

   public IExternalDevice getExdevice() {
      return exdevice;
   }

   public int getID() {
      return id;
   }

   public void setExdevice(IExternalDevice exdevice) {
      this.exdevice = exdevice;
   }

   //#mdebug
   public IDLog toDLog() {
      return toStringGetUCtx().toDLog();
   }

   public String toString() {
      return Dctx.toString(this);
   }

   public void toString(Dctx dc) {
      dc.root(this, ControllerBentley.class, "@line5");
      toStringPrivate(dc);
   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, ControllerBentley.class);
      toStringPrivate(dc);
   }

   public UCtx toStringGetUCtx() {
      return jic.getUCtx();
   }

   private void toStringPrivate(Dctx dc) {
      dc.appendVarWithSpace("id", id);
   }

   //#enddebug

}
