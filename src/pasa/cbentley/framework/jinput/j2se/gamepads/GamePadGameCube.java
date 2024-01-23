package pasa.cbentley.framework.jinput.j2se.gamepads;

import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Event;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.coreui.src4.event.DeviceEvent;
import pasa.cbentley.framework.coreui.src4.event.DeviceEventFloat;
import pasa.cbentley.framework.coreui.src4.event.DeviceEventXY;
import pasa.cbentley.framework.coreui.src4.event.SenseEvent;
import pasa.cbentley.framework.coreui.src4.interfaces.IExternalDevice;
import pasa.cbentley.framework.coreui.src4.interfaces.ITechSenses;
import pasa.cbentley.framework.coreui.src4.tech.ITechCodes;
import pasa.cbentley.framework.coreui.src4.tech.IInput;
import pasa.cbentley.framework.jinput.j2se.ctx.JInputCtx;
import pasa.cbentley.framework.jinput.j2se.engine.ControllerBentley;

public class GamePadGameCube extends GamePadAbstract {
   private boolean isDigital = true;

   /**
    * In Analog mode,
    * Are axis sensor event. Else we use {@link DeviceEventXY}
    */
   private boolean isSensor;

   public GamePadGameCube(JInputCtx fc) {
      super(fc);
   }

   public String getButton(int button) {
      switch (button) {
         case ITechCodes.PAD_BUTTON_0:
            return "X";
         case ITechCodes.PAD_BUTTON_1:
            return "A";
         case ITechCodes.PAD_BUTTON_2:
            return "B";
         case ITechCodes.PAD_BUTTON_3:
            return "Y";
         case ITechCodes.PAD_BUTTON_4:
            return "L";
         case ITechCodes.PAD_BUTTON_5:
            return "R";
         case ITechCodes.PAD_BUTTON_7:
            return "Z";
         case ITechCodes.PAD_BUTTON_9:
            return "Start";
         case ITechCodes.PAD_BUTTON_12:
            return "CrossUp";
         case ITechCodes.PAD_BUTTON_13:
            return "CrossRight";
         case ITechCodes.PAD_BUTTON_14:
            return "CrossDown";
         case ITechCodes.PAD_BUTTON_15:
            return "CrossLeft";
         case ITechCodes.PAD_UP:
            return "PadUp";
         case ITechCodes.PAD_DOWN:
            return "PadDown";
         case ITechCodes.PAD_LEFT:
            return "PadLeft";
         case ITechCodes.PAD_RIGHT:
            return "PadRight";
         case ITechCodes.PAD_UPZ:
            return "Upz";
         case ITechCodes.PAD_DOWNZ:
            return "Downz";
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

   private boolean isPressedPadRight;

   private boolean isPressedPadLeft;

   private boolean isPressedPadUp;

   private boolean isPressedPadDown;

   private boolean isPressedPadUpZ;

   private boolean isPressedPadDownZ;

   private DeviceEvent getDigitalFromAnalogY(Event event, Component component, ControllerBentley ei) {
      float data = event.getValue();
      int deviceButton = 0;
      int mode = 0;
      int deviceID = ei.getID(); //id of gamepad
      int deviceType = IInput.DEVICE_2_GAMEPAD;
      if (data >= 0.7) {
         mode = IInput.MOD_0_PRESSED;
         if (isPressedPadUp) {
            //already pressed
            return null;
         } else {
            isPressedPadUp = true;
            deviceButton = ITechCodes.PAD_UP;
         }
      } else if (data > 0) {
         mode = IInput.MOD_1_RELEASED;
         if (isPressedPadUp) {
            isPressedPadUp = false;
            deviceButton = ITechCodes.PAD_UP;
         } else {
            return null;
         }
      } else if (data <= -0.7) {
         mode = IInput.MOD_0_PRESSED;
         if (isPressedPadDown) {
            //already pressed
            return null;
         } else {
            isPressedPadDown = true;
            deviceButton = ITechCodes.PAD_DOWN;
         }
      } else {
         mode = IInput.MOD_1_RELEASED;
         if (isPressedPadDown) {
            isPressedPadDown = false;
            deviceButton = ITechCodes.PAD_DOWN;
         } else {
            return null;
         }
      }
      DeviceEvent de = new DeviceEvent(getCoreUiCtx(), deviceType, deviceID, mode, deviceButton);
      return de;
   }

   private DeviceEvent getDigitalFromAnalogX(Event event, Component component, ControllerBentley ei) {
      float data = event.getValue();
      int deviceButton = 0;
      int mode = 0;
      int deviceID = ei.getID(); //id of gamepad
      int deviceType = IInput.DEVICE_2_GAMEPAD;
      if (data >= 0.7) {
         mode = IInput.MOD_0_PRESSED;
         if (isPressedPadRight) {
            //already pressed
            return null;
         } else {
            isPressedPadRight = true;
            deviceButton = ITechCodes.PAD_RIGHT;
         }
      } else if (data > 0) {
         mode = IInput.MOD_1_RELEASED;
         if (isPressedPadRight) {
            isPressedPadRight = false;
            deviceButton = ITechCodes.PAD_RIGHT;
         } else {
            return null;
         }
      } else if (data <= -0.7) {
         mode = IInput.MOD_0_PRESSED;
         if (isPressedPadLeft) {
            //already pressed
            return null;
         } else {
            isPressedPadLeft = true;
            deviceButton = ITechCodes.PAD_LEFT;
         }
      } else {
         mode = IInput.MOD_1_RELEASED;
         if (isPressedPadLeft) {
            isPressedPadLeft = false;
            deviceButton = ITechCodes.PAD_LEFT;
         } else {
            return null;
         }
      }
      DeviceEvent de = new DeviceEvent(getCoreUiCtx(), deviceType, deviceID, mode, deviceButton);
      return de;
   }

   private DeviceEvent getDigitalFromAnalogZ(Event event, Component component, ControllerBentley ei) {
      float data = event.getValue();
      int deviceButton = 0;
      int mode = 0;
      int deviceID = ei.getID(); //id of gamepad
      int deviceType = IInput.DEVICE_2_GAMEPAD;
      if (data >= 0.7) {
         mode = IInput.MOD_0_PRESSED;
         if (isPressedPadUpZ) {
            //already pressed
            return null;
         } else {
            isPressedPadUpZ = true;
            deviceButton = ITechCodes.PAD_UPZ;
         }
      } else if (data > 0) {
         mode = IInput.MOD_1_RELEASED;
         if (isPressedPadUpZ) {
            isPressedPadUpZ = false;
            deviceButton = ITechCodes.PAD_UPZ;
         } else {
            return null;
         }
      } else if (data <= -0.7) {
         mode = IInput.MOD_0_PRESSED;
         if (isPressedPadDownZ) {
            //already pressed
            return null;
         } else {
            isPressedPadDownZ = true;
            deviceButton = ITechCodes.PAD_DOWNZ;
         }
      } else {
         mode = IInput.MOD_1_RELEASED;
         if (isPressedPadDownZ) {
            isPressedPadDownZ = false;
            deviceButton = ITechCodes.PAD_DOWNZ;
         } else {
            return null;
         }
      }
      DeviceEvent de = new DeviceEvent(getCoreUiCtx(), deviceType, deviceID, mode, deviceButton);
      return de;
   }

   private DeviceEvent getDigitalFromAnalog(Event event, Component component, ControllerBentley ei) {
      //we generate full events when above threhold. otherwise a move event
      Identifier id = component.getIdentifier();
      if (id == Component.Identifier.Axis.X) {
         return getDigitalFromAnalogX(event, component, ei);
      } else if (id == Component.Identifier.Axis.Y) {
         return getDigitalFromAnalogY(event, component, ei);
      } else if (id == Component.Identifier.Axis.Z) {
         return getDigitalFromAnalogZ(event, component, ei);
      } else {
         //#debug
         toDLog().pEvent("Unknown Component", this, GamePadGameCube.class, "getDigitalFromAnalog", LVL_05_FINE, true);
         return null;
      }
   }

   private float minimalSensiblity = 0.02f;

   public DeviceEvent getEvent(Event event, ControllerBentley ei) {
      int deviceType = IInput.DEVICE_2_GAMEPAD;

      int deviceID = ei.getID(); //id of gamepad
      int deviceButton = 0;
      Component component = event.getComponent();
      int mode = IInput.MOD_0_PRESSED;
      DeviceEvent de = null;
      IExternalDevice externalDevice = ei.getExdevice();
      if (component.isAnalog()) {
         if (isDigital()) {
            de = getDigitalFromAnalog(event, component, ei);
         } else {
            //
            float data = event.getValue();
            //guard against parasitic events
            boolean dataPositive = false;
            if (data > 0) {
               dataPositive = true;
               if (data < minimalSensiblity) {
                  return null;
               }
            } else {
               if (data > -minimalSensiblity) {
                  return null;
               }
            }
            Identifier id = component.getIdentifier();
            if (isSensor) {
               int sensorType = ITechSenses.SENSOR_TYPE_04_AXIS;
               int sensorSubType = ITechCodes.AXIS_X;
               if (id == Component.Identifier.Axis.X) {
                  sensorSubType = ITechCodes.AXIS_X;
               } else if (id == Component.Identifier.Axis.Y) {
                  sensorSubType = ITechCodes.AXIS_Y;
               } else if (id == Component.Identifier.Axis.Z) {
                  sensorSubType = ITechCodes.AXIS_Z;
               }
               final SenseEvent senseEvent = new SenseEvent(getCoreUiCtx(), sensorType, sensorSubType, deviceID);
               senseEvent.setValue(data);
               de = senseEvent;
            } else {
               if (id == Component.Identifier.Axis.X) {
                  if (dataPositive) {
                     deviceButton = ITechCodes.THROTTLE_A;
                  } else {
                     deviceButton = ITechCodes.THROTTLE_B;
                  }
               }
               DeviceEventFloat d = new DeviceEventFloat(getCoreUiCtx(), IInput.DEVICE_2_GAMEPAD, deviceID, deviceButton, data);
               de = d;
            }

         }
      } else {
         //not analog
         float v = event.getValue();
         if (v == 0) {
            mode = IInput.MOD_1_RELEASED;
         } else {
            mode = IInput.MOD_0_PRESSED;
         }
         deviceButton = super.getDeviceButton(component);
         de = new DeviceEvent(getCoreUiCtx(), deviceType, deviceID, mode, deviceButton);
      }
      if (externalDevice == null) {
         //#debug
         toDLog().pNull("", this, GamePadGameCube.class, "getEvent", LVL_05_FINE, true);
      }
      //create event use touc
      if (de != null) {
         de.setParamO1(externalDevice);
      }
      return de;
   }

   public String getName() {
      return "GameCubePad";
   }

   public String getName(int button) {
      return getButton(button);
   }

   /**
    * True when analog stick don't register anything but ON/OFF.
    * This class becomes a state memory for the analog sticks
    * @return
    */
   public boolean isDigital() {
      return isDigital;
   }

   public boolean isStart(int button) {
      return button == ITechCodes.PAD_BUTTON_9;
   }

   public void setDigital(boolean isDigital) {
      this.isDigital = isDigital;
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, GamePadGameCube.class, "@line5");
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, GamePadGameCube.class);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   private void toStringPrivate(Dctx dc) {

   }

   //#enddebug

}
