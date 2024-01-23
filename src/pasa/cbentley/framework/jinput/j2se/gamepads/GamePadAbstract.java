package pasa.cbentley.framework.jinput.j2se.gamepads;

import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Event;
import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IDLog;
import pasa.cbentley.core.src4.structs.IntToStrings;
import pasa.cbentley.framework.coreui.src4.ctx.CoreUiCtx;
import pasa.cbentley.framework.coreui.src4.event.DeviceEvent;
import pasa.cbentley.framework.coreui.src4.interfaces.IExternalDevice;
import pasa.cbentley.framework.coreui.src4.tech.ITechCodes;
import pasa.cbentley.framework.coreui.src4.tech.IInput;
import pasa.cbentley.framework.jinput.j2se.ctx.JInputCtx;
import pasa.cbentley.framework.jinput.j2se.engine.ControllerBentley;

public abstract class GamePadAbstract implements IExternalDevice {

   protected final JInputCtx jic;

   public GamePadAbstract(JInputCtx jic) {
      this.jic = jic;
   }

   /**
    * 
    * @param id
    * @return
    */
   public IntToStrings getControllerData() {
      return null;
   }

   public CoreUiCtx getCoreUiCtx() {
      return jic.getCAC().getCUC();
   }

   protected int getDeviceButton(Component c) {
      //we have to map component event to a Bentley Framework value
      Identifier id = c.getIdentifier();
      if (id == Component.Identifier.Button._0) {
         return ITechCodes.PAD_BUTTON_0;
      } else if (id == Component.Identifier.Button._1) {
         return ITechCodes.PAD_BUTTON_1;
      } else if (id == Component.Identifier.Button._2) {
         return ITechCodes.PAD_BUTTON_2;
      } else if (id == Component.Identifier.Button._3) {
         return ITechCodes.PAD_BUTTON_3;
      } else if (id == Component.Identifier.Button._4) {
         return ITechCodes.PAD_BUTTON_4;
      } else if (id == Component.Identifier.Button._5) {
         return ITechCodes.PAD_BUTTON_5;
      } else if (id == Component.Identifier.Button._6) {
         return ITechCodes.PAD_BUTTON_6;
      } else if (id == Component.Identifier.Button._7) {
         return ITechCodes.PAD_BUTTON_7;
      } else if (id == Component.Identifier.Button._8) {
         return ITechCodes.PAD_BUTTON_8;
      } else if (id == Component.Identifier.Button._9) {
         return ITechCodes.PAD_BUTTON_9;
      }
      return 0;
   }

   /**
    * 
    * @param event
    * @param ei
    * @return null if {@link Event} does not represent is valid event
    */
   public abstract DeviceEvent getEvent(Event event, ControllerBentley ei);

   public int getType() {
      return IInput.DEVICE_2_GAMEPAD;
   }

   //#mdebug
   public IDLog toDLog() {
      return toStringGetUCtx().toDLog();
   }

   public String toString() {
      return Dctx.toString(this);
   }

   public void toString(Dctx dc) {
      dc.root(this, GamePadAbstract.class, "@line82");
      toStringPrivate(dc);
   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, GamePadAbstract.class);
      toStringPrivate(dc);
   }

   public UCtx toStringGetUCtx() {
      return jic.getUCtx();
   }

   private void toStringPrivate(Dctx dc) {

   }

   //#enddebug

}
