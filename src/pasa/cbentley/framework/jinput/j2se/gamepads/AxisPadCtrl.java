package pasa.cbentley.framework.jinput.j2se.gamepads;

import net.java.games.input.Component;
import net.java.games.input.Event;
import pasa.cbentley.framework.core.ui.src4.event.DeviceEvent;
import pasa.cbentley.framework.core.ui.src4.tech.ITechInput;
import pasa.cbentley.framework.jinput.j2se.ctx.JInputCtx;
import pasa.cbentley.framework.jinput.j2se.ctx.ObjectJIC;
import pasa.cbentley.framework.jinput.j2se.engine.ControllerBentley;

public class AxisPadCtrl extends ObjectJIC {

   private int     codeNegative;

   private int     codePositive;

   private boolean isPressedPadDown;

   private boolean isPressedPadUp;

   private float   tDownRight = 0.70f;

   private float   tUpLeft    = -0.70f;

   public AxisPadCtrl(JInputCtx jic, int codeNegative, int codePositive) {
      super(jic);
      this.codeNegative = codeNegative;
      this.codePositive = codePositive;
   }

   public void setButtonEdge(float f) {
      this.tDownRight = f;
      this.tUpLeft = -f;
   }

   DeviceEvent getDigitalFromAnalogAxis(Event event, Component component, ControllerBentley ei) {
      float data = event.getValue();
      int mode = 0;
      int deviceButton = 0;
      int deviceID = ei.getDeviceID(); //id of gamepad
      int deviceType = ITechInput.DEVICE_2_GAMEPAD;
      if (data < 0) {
         if (data <= tUpLeft) {
            mode = ITechInput.MOD_0_PRESSED;
            if (isPressedPadUp) {
               //already pressed
               return null;
            } else {
               isPressedPadUp = true;
               deviceButton = codeNegative;
            }
         } else {
            mode = ITechInput.MOD_1_RELEASED;
            if (isPressedPadUp) {
               isPressedPadUp = false;
               deviceButton = codeNegative;
            } else {
               return null;
            }
         }
      } else {
         //data is > 0
         if (data >= tDownRight) {
            mode = ITechInput.MOD_0_PRESSED;
            if (isPressedPadDown) {
               //already pressed
               return null;
            } else {
               isPressedPadDown = true;
               deviceButton = codePositive;
            }
         } else {
            mode = ITechInput.MOD_1_RELEASED;
            if (isPressedPadDown) {
               isPressedPadDown = false;
               deviceButton = codePositive;
            } else {
               return null;
            }
         }
      }
      DeviceEvent de = new DeviceEvent(jic.getCoreUiCtx(), deviceType, deviceID, mode, deviceButton);
      return de;
   }

}
