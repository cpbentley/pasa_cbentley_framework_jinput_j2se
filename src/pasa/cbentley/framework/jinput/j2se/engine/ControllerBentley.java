package pasa.cbentley.framework.jinput.j2se.engine;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.framework.core.ui.src4.interfaces.IExternalDevice;
import pasa.cbentley.framework.jinput.j2se.ctx.JInputCtx;
import pasa.cbentley.framework.jinput.j2se.ctx.ObjectJIC;

public class ControllerBentley extends ObjectJIC implements IStringable {

   /**
    * JInput wrapper of the external device
    */
   private final Controller controller;

   /**
    * Interface to the device properties
    */
   private IExternalDevice  exdevice;

   protected final int      id;

   public ControllerBentley(JInputCtx jic, Controller controller, IExternalDevice exDevice, int deviceID) {
      super(jic);
      //#debug
      jic.toStringCheckNull(controller);
      //#debug
      jic.toStringCheckNull(exDevice);

      this.controller = controller;
      this.exdevice = exDevice;
      this.id = deviceID;
   }

   public Controller getController() {
      return controller;
   }

   /**
    * Must have been set
    * @return
    */
   public IExternalDevice getExternalDevice() {
      return exdevice;
   }

   public int getDeviceID() {
      return id;
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, ControllerBentley.class, 55);
      toStringPrivate(dc);
      super.toString(dc.sup());
      dc.nlLvl(exdevice, "exdevice");

      dc.append("net.java.games.input.Controller");
      dc.appendVarWithNewLine("name", controller.getName());
      ;
      dc.appendVarWithNewLine("portnumber", controller.getPortNumber());
      Component[] components = controller.getComponents();
      dc.appendVarWithNewLine("#components", components.length);
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, ControllerBentley.class, 55);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   private void toStringPrivate(Dctx dc) {
      dc.appendVarWithSpace("id", id);
   }
   //#enddebug

}
