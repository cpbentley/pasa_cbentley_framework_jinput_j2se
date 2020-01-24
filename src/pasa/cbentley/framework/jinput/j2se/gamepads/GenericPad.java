package pasa.cbentley.framework.jinput.j2se.gamepads;

import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Event;
import pasa.cbentley.framework.coreui.src4.event.DeviceEvent;
import pasa.cbentley.framework.coreui.src4.interfaces.IExternalDevice;
import pasa.cbentley.framework.coreui.src4.tech.IBCodes;
import pasa.cbentley.framework.coreui.src4.tech.IInput;
import pasa.cbentley.framework.jinput.j2se.ctx.JInputCtx;
import pasa.cbentley.framework.jinput.j2se.engine.ExternalInput;

public class GenericPad extends GamePad {

   public GenericPad(JInputCtx jic) {
      super(jic);
   }

   public boolean isStart(int button) {
      return false;
   }
   public String getName() {
      return "GenericPad";
   }
   
   public String getName(int button) {
      return "Button"+button;
   }

   public DeviceEvent getEvent(Event event, ExternalInput ei) {
      int deviceType = IInput.DEVICE_2_GAMEPAD;

      int deviceID = ei.id; //id of gamepad
      int deviceButton = 0;
      Component c = event.getComponent();
      float data = event.getValue();
      int mode = IInput.MOD_0_PRESSED;
      if (c.isAnalog()) {
         Identifier id = c.getIdentifier();
         if (id == Component.Identifier.Axis.X) {
            if (data == 1.0) {
               deviceButton = IBCodes.PAD_RIGHT;
            } else if (data == -1.0) {
               deviceButton = IBCodes.PAD_LEFT;
            } else {
               mode = IInput.MOD_1_RELEASED;
               deviceButton =  IBCodes.AXIS_X;
            }
         } else if (id == Component.Identifier.Axis.Y) {
            //data being 1 is only valid for digital axis
            if (data == 1.0) {
               deviceButton = IBCodes.PAD_DOWN;
            } else if (data == -1.0) {
               deviceButton = IBCodes.PAD_UP;
            } else {
               mode = IInput.MOD_1_RELEASED;
               deviceButton =  IBCodes.AXIS_X;
            }
         }
      } else {
         float v = event.getValue();
         if (v == 0) {
            mode = IInput.MOD_1_RELEASED;
         }
         deviceButton = super.getDeviceButton(c);
      }
      IExternalDevice ed = ei.exdevice;
      //create event use touc
      final DeviceEvent de = new DeviceEvent(getCoreUiCtx(), deviceType, deviceID, mode, deviceButton);
      de.setParamO1(ed);
      return de;
   }


}
