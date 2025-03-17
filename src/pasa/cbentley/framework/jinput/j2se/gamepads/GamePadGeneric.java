package pasa.cbentley.framework.jinput.j2se.gamepads;

import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Event;
import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.core.ui.src4.event.DeviceEvent;
import pasa.cbentley.framework.core.ui.src4.interfaces.IExternalDevice;
import pasa.cbentley.framework.core.ui.src4.tech.ITechInput;
import pasa.cbentley.framework.core.ui.src4.tech.ITechCodes;
import pasa.cbentley.framework.jinput.j2se.ctx.JInputCtx;
import pasa.cbentley.framework.jinput.j2se.engine.ControllerBentley;

public class GamePadGeneric extends GamePadAbstract {

   public GamePadGeneric(JInputCtx jic) {
      super(jic);
   }

   public boolean isStart(int button) {
      return false;
   }

   public String getName() {
      return "GenericPad";
   }

   public String getNameButton(int button) {
      return "Button" + button;
   }

   public DeviceEvent getEvent(Event event, ControllerBentley ei) {
      int deviceType = ITechInput.DEVICE_2_GAMEPAD;

      int deviceID = ei.getDeviceID(); //id of gamepad
      int deviceButton = 0;
      Component c = event.getComponent();
      float data = event.getValue();
      int mode = ITechInput.MOD_0_PRESSED;
      if (c.isAnalog()) {
         Identifier id = c.getIdentifier();
         if (id == Component.Identifier.Axis.X) {
            if (data == 1.0) {
               deviceButton = ITechCodes.PAD_RIGHT;
            } else if (data == -1.0) {
               deviceButton = ITechCodes.PAD_LEFT;
            } else {
               mode = ITechInput.MOD_1_RELEASED;
               deviceButton = ITechCodes.AXIS_X;
            }
         } else if (id == Component.Identifier.Axis.Y) {
            //data being 1 is only valid for digital axis
            if (data == 1.0) {
               deviceButton = ITechCodes.PAD_DOWN;
            } else if (data == -1.0) {
               deviceButton = ITechCodes.PAD_UP;
            } else {
               mode = ITechInput.MOD_1_RELEASED;
               deviceButton = ITechCodes.AXIS_X;
            }
         }
      } else {
         float v = event.getValue();
         if (v == 0) {
            mode = ITechInput.MOD_1_RELEASED;
         }
         deviceButton = super.getDeviceButton(c);
      }
      //create event use touc
      final DeviceEvent de = new DeviceEvent(getCoreUiCtx(), deviceType, deviceID, mode, deviceButton);
      return de;
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, GamePadGeneric.class, "@line5");
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   private void toStringPrivate(Dctx dc) {

   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, GamePadGeneric.class);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   //#enddebug

}
