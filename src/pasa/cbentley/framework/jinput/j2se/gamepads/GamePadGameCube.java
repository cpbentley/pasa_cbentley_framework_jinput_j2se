package pasa.cbentley.framework.jinput.j2se.gamepads;

import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Event;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.core.ui.src4.event.DeviceEvent;
import pasa.cbentley.framework.core.ui.src4.event.DeviceEventFloat;
import pasa.cbentley.framework.core.ui.src4.event.DeviceEventXY;
import pasa.cbentley.framework.core.ui.src4.event.SenseEvent;
import pasa.cbentley.framework.core.ui.src4.interfaces.ITechSenses;
import pasa.cbentley.framework.core.ui.src4.tech.IInput;
import pasa.cbentley.framework.core.ui.src4.tech.ITechCodes;
import pasa.cbentley.framework.jinput.j2se.ctx.JInputCtx;
import pasa.cbentley.framework.jinput.j2se.engine.ControllerBentley;

public class GamePadGameCube extends GamePadAbstract {
   private boolean     isDigital         = true;

   /**
    * In Analog mode,
    * Are axis sensor event. Else we use {@link DeviceEventXY}
    */
   private boolean     isSensor;

   private float       minimalSensiblity = 0.02f;

   private AxisPadCtrl axisPadMainVertical;

   private AxisPadCtrl axisPadMainHoriz;

   private AxisPadCtrl axisPadCVertical;

   private AxisPadCtrl axisPadCHoriz;

   public GamePadGameCube(JInputCtx fc) {
      super(fc);

      axisPadMainVertical = new AxisPadCtrl(jic, ITechCodes.PAD_UP, ITechCodes.PAD_DOWN);
      axisPadMainHoriz = new AxisPadCtrl(jic, ITechCodes.PAD_LEFT, ITechCodes.PAD_RIGHT);

      axisPadCVertical = new AxisPadCtrl(jic, ITechCodes.PAD_C_UP, ITechCodes.PAD_C_DOWN);
      axisPadCHoriz = new AxisPadCtrl(jic, ITechCodes.PAD_C_LEFT, ITechCodes.PAD_C_RIGHT);

      axisPadCVertical.setButtonEdge(0.6f);
      axisPadCHoriz.setButtonEdge(0.6f);
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
            return "Cross_Up";
         case ITechCodes.PAD_BUTTON_13:
            return "Cross_Right";
         case ITechCodes.PAD_BUTTON_14:
            return "Cross_Down";
         case ITechCodes.PAD_BUTTON_15:
            return "Cross_Left";
         case ITechCodes.PAD_UP:
            return "Pad_Up";
         case ITechCodes.PAD_DOWN:
            return "Pad_Down";
         case ITechCodes.PAD_LEFT:
            return "Pad_Left";
         case ITechCodes.PAD_RIGHT:
            return "Pad_Right";
         case ITechCodes.PAD_C_UP:
            return "C_Up";
         case ITechCodes.PAD_C_DOWN:
            return "C_Down";
         case ITechCodes.PAD_C_LEFT:
            return "C_Left";
         case ITechCodes.PAD_C_RIGHT:
            return "C_Right";
         case ITechCodes.PAD_POV:
            return "pov";
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

   /**
    * null if event value is insignificant/noise
    * @param event
    * @param component
    * @param ei
    * @return
    */
   private DeviceEvent getDigitalFromAnalog(Event event, Component component, ControllerBentley ei) {
      //we generate full events when above threhold. otherwise a move event
      Identifier id = component.getIdentifier();
      if (id == Component.Identifier.Axis.X) {
         return axisPadMainHoriz.getDigitalFromAnalogAxis(event, component, ei);
      } else if (id == Component.Identifier.Axis.Y) {
         return axisPadMainVertical.getDigitalFromAnalogAxis(event, component, ei);
      } else if (id == Component.Identifier.Axis.Z) {
         return axisPadCVertical.getDigitalFromAnalogAxis(event, component, ei);
      } else if (id == Component.Identifier.Axis.RZ) {
         return axisPadCHoriz.getDigitalFromAnalogAxis(event, component, ei);
      } else if (id == Component.Identifier.Axis.RX) {
         return getDigitalFromAnalogRotation(event, component, ei, ITechCodes.PAD_BUTTON_4);
      } else if (id == Component.Identifier.Axis.RY) {
         return getDigitalFromAnalogRotation(event, component, ei, ITechCodes.PAD_BUTTON_5);
      } else {
         //#debug
         toDLog().pEvent("Uncoded GameCube Component Id name=" + id.getName(), this, GamePadGameCube.class, "getDigitalFromAnalog@108", LVL_05_FINE, true);
         return null;
      }
   }

   private DeviceEvent getDigitalFromAnalogRotation(Event event, Component component, ControllerBentley ei, int deviceButton) {
      float data = event.getValue();
      int deviceType = IInput.DEVICE_2_GAMEPAD;
      int deviceID = ei.getDeviceID(); //id of gamepad

      if (data >= 0.02 || data <= -0.02) {
         //#debug
         toDLog().pFlow("data=" + data + " deviceID=" + deviceID, this, GamePadGameCube.class, "getDigitalFromAnalogRotation@149", LVL_05_FINE, true);
         DeviceEventFloat de = new DeviceEventFloat(getCoreUiCtx(), deviceType, deviceID, deviceButton, data);
         return de;
      } else {
         return null;
      }
   }

  

   public DeviceEvent getEvent(Event event, ControllerBentley ei) {
      int deviceType = IInput.DEVICE_2_GAMEPAD;

      int deviceID = ei.getDeviceID(); //id of gamepad
      int deviceButton = 0;
      Component component = event.getComponent();
      int mode = IInput.MOD_0_PRESSED;
      DeviceEvent de = null;

      Identifier identifier = component.getIdentifier();

      if (identifier == Identifier.Axis.POV) {

         float data = event.getValue();
         deviceButton = ITechCodes.PAD_POV;
         //that special cross
         DeviceEventFloat d = new DeviceEventFloat(getCoreUiCtx(), IInput.DEVICE_2_GAMEPAD, deviceID, deviceButton, data);
         de = d;

      } else if (component.isAnalog()) {
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
      return de;
   }

   public String getName() {
      return "GameCubePad";
   }

   public String getNameButton(int button) {
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
