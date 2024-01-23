package pasa.cbentley.framework.jinput.j2se.gamepads;

import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Event;
import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.coreui.src4.event.DeviceEvent;
import pasa.cbentley.framework.coreui.src4.interfaces.IExternalDevice;
import pasa.cbentley.framework.coreui.src4.tech.ITechCodes;
import pasa.cbentley.framework.coreui.src4.tech.IInput;
import pasa.cbentley.framework.jinput.j2se.ctx.JInputCtx;
import pasa.cbentley.framework.jinput.j2se.engine.ControllerBentley;

public class GamePadSega6Buttons extends GamePadAbstract {

   public GamePadSega6Buttons(JInputCtx fc) {
      super(fc);
   }

   public String getName() {
      return "SegaPad";
   }

   public String getButton(int button) {
      switch (button) {
         case ITechCodes.PAD_BUTTON_0:
            return "A";
         case ITechCodes.PAD_BUTTON_1:
            return "B";
         case ITechCodes.PAD_BUTTON_2:
            return "C";
         case ITechCodes.PAD_BUTTON_3:
            return "Start";
         case ITechCodes.PAD_BUTTON_4:
            return "X";
         case ITechCodes.PAD_BUTTON_5:
            return "Y";
         case ITechCodes.PAD_BUTTON_6:
            return "Z";
         case ITechCodes.PAD_BUTTON_7:
            return "Mode";
         case ITechCodes.PAD_UP:
            return "Up";
         case ITechCodes.PAD_DOWN:
            return "Down";
         case ITechCodes.PAD_LEFT:
            return "Left";
         case ITechCodes.PAD_RIGHT:
            return "Right";
         default:
            return "UnknownSegaButton" + button;
      }
   }

   public int getPadValueVertical(float data) {
      //data being 1 is only valid for digital axis
      if (data == 1.0) {
         return ITechCodes.PAD_DOWN;
      } else if (data == -1.0) {
         return ITechCodes.PAD_UP;
      } else {
         return 0;
      }
   }

   /**
    * Whats the value for 
    * Down/Right
    * Down 
    * @param value
    * @return
    */
   public int getPadValue(float value) {
      if (value == 1.0) {
         return ITechCodes.PAD_RIGHT;
      }
      if (value == -1.0) {
         return ITechCodes.PAD_LEFT;
      }
      return 0;

   }

   public DeviceEvent getEvent(Event event, ControllerBentley ei) {
      int deviceType = IInput.DEVICE_2_GAMEPAD;

      int deviceID = ei.getID(); //id of gamepad
      int deviceButton = 0;
      Component c = event.getComponent();
      float data = event.getValue();
      int mode = IInput.MOD_0_PRESSED;
      if (c.isAnalog()) {
         Identifier id = c.getIdentifier();
         if (id == Component.Identifier.Axis.X) {
            if (data == 1.0) {
               deviceButton = ITechCodes.PAD_RIGHT;
            } else if (data == -1.0) {
               deviceButton = ITechCodes.PAD_LEFT;
            } else {
               mode = IInput.MOD_1_RELEASED;
               deviceButton = ITechCodes.AXIS_X;
            }
         } else if (id == Component.Identifier.Axis.Y) {
            //data being 1 is only valid for digital axis
            if (data == 1.0) {
               deviceButton = ITechCodes.PAD_DOWN;
            } else if (data == -1.0) {
               deviceButton = ITechCodes.PAD_UP;
            } else {
               mode = IInput.MOD_1_RELEASED;
               deviceButton = ITechCodes.AXIS_Y;
            }
         }
      } else {
         float v = event.getValue();
         if (v == 0) {
            mode = IInput.MOD_1_RELEASED;
         }
         deviceButton = super.getDeviceButton(c);
      }
      IExternalDevice ed = ei.getExdevice();
      //create event use touc
      final DeviceEvent de = new DeviceEvent(getCoreUiCtx(), deviceType, deviceID, mode, deviceButton);
      de.setParamO1(ed);
      return de;
   }

   public boolean isStart(int button) {
      return button == ITechCodes.PAD_BUTTON_3;
   }

   public String getName(int button) {
      return getButton(button);
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, GamePadSega6Buttons.class, "@line5");
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   private void toStringPrivate(Dctx dc) {

   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, GamePadSega6Buttons.class);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   //#enddebug

}
