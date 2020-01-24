package pasa.cbentley.framework.jinput.j2se.gamepads;

import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Event;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.coreui.src4.event.DeviceEvent;
import pasa.cbentley.framework.coreui.src4.event.SenseEvent;
import pasa.cbentley.framework.coreui.src4.interfaces.IExternalDevice;
import pasa.cbentley.framework.coreui.src4.interfaces.ISenses;
import pasa.cbentley.framework.coreui.src4.tech.IBCodes;
import pasa.cbentley.framework.coreui.src4.tech.IInput;
import pasa.cbentley.framework.jinput.j2se.ctx.JInputCtx;
import pasa.cbentley.framework.jinput.j2se.engine.ExternalInput;

public class GameCubePad extends GamePad {
   public GameCubePad(JInputCtx fc) {
      super(fc);
   }

   public boolean isStart(int button) {
      return button == IBCodes.PAD_BUTTON_9;
   }

   public String getName(int button) {
      return getButton(button);
   }

   public String getName() {
      return "GameCubePad";
   }

   public String getButton(int button) {
      switch (button) {
         case IBCodes.PAD_BUTTON_0:
            return "X";
         case IBCodes.PAD_BUTTON_1:
            return "A";
         case IBCodes.PAD_BUTTON_2:
            return "B";
         case IBCodes.PAD_BUTTON_3:
            return "Y";
         case IBCodes.PAD_BUTTON_4:
            return "Left";
         case IBCodes.PAD_BUTTON_5:
            return "Right";
         case IBCodes.PAD_BUTTON_7:
            return "Z";
         case IBCodes.PAD_BUTTON_9:
            return "Start";
         case IBCodes.PAD_BUTTON_12:
            return "CrossUp";
         case IBCodes.PAD_BUTTON_13:
            return "CrossRight";
         case IBCodes.PAD_BUTTON_14:
            return "CrossDown";
         case IBCodes.PAD_BUTTON_15:
            return "CrossLeft";
         default:
            break;
      }
      return null;
   }

   /**
    * 
    * @return
    */
   public int getCalibrationMove() {
      return 0;
   }

   public DeviceEvent getEvent(Event event, ExternalInput ei) {
      int deviceType = IInput.DEVICE_2_GAMEPAD;

      int deviceID = ei.id; //id of gamepad
      int deviceButton = 0;
      Component c = event.getComponent();
      float data = event.getValue();
      int mode = IInput.MOD_0_PRESSED;
      IExternalDevice externalDevice = ei.exdevice;
      if (c.isAnalog()) {
         //we generate full events when above threhold. otherwise a move event
         Identifier id = c.getIdentifier();
         if (id == Component.Identifier.Axis.X) {
            if (data >= 0.7) {
               deviceButton = IBCodes.PAD_RIGHT;
            } else if (data <= 0.7) {
               deviceButton = IBCodes.PAD_LEFT;
            } else if (data < 0.01 || data > -0.01) {
               mode = IInput.MOD_1_RELEASED;
               deviceButton = IBCodes.AXIS_X;
            } else {
               int sensorType = ISenses.SENSOR_TYPE_04_AXIS;
               int sensorSubType = IBCodes.AXIS_X;
               final SenseEvent de = new SenseEvent(getCoreUiCtx(), sensorType);
               de.setSubType(sensorSubType);
               de.setValue(data);
               de.setParamO1(externalDevice);
               return de;
            }
         } else if (id == Component.Identifier.Axis.Y) {
            //data being 1 is only valid for digital axis
            if (data == 1.0) {
               deviceButton = IBCodes.PAD_DOWN;
            } else if (data == -1.0) {
               deviceButton = IBCodes.PAD_UP;
            } else {
               mode = IInput.MOD_1_RELEASED;
               deviceButton = IBCodes.AXIS_Y;
            }
         }
      } else {
         float v = event.getValue();
         if (v == 0) {
            mode = IInput.MOD_1_RELEASED;
         }
         deviceButton = super.getDeviceButton(c);
      }
      //create event use touc
      final DeviceEvent de = new DeviceEvent(getCoreUiCtx(), deviceType, deviceID, mode, deviceButton);
      de.setParamO1(externalDevice);

      return de;
   }
   
   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, "Sega6Buttons");
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "Sega6Buttons");
   }
   //#enddebug
}
