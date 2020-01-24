package pasa.cbentley.framework.jinput.j2se.engine;

import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Controller;
import net.java.games.input.Controller.Type;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.coreui.src4.event.DeviceEvent;
import pasa.cbentley.framework.coreui.src4.tech.IBCodes;
import pasa.cbentley.framework.jinput.j2se.gamepads.GamePad;

/**
 * Engine is started by Host Launcher?
 * <br>
 * Engine gets a reference to the Host container???
 * <br>
 * <br>
 * TODO Host container must be able to send the event to the right Canvas? How?
 * 
 * TODO what is 2 application run in the same process... we have 2 InputState ?
 * which one is active? Depends on the focused Canvas. if appli takes a whole canvas.
 * but if appli is inside the 
 * InputState
 * 
 * <br>
 * <br>
 * When an appli is master but located inside another appli, and that nested appli wants JInput..
 * It will recieve events only if the appli has device focus. When at least one device is focused on an appli
 * Appli has focus.
 * 
 * Several Appli use one or several JInputEngine? JInputEngine must be a singleton in a Java process.
 * 
 * @author Charles Bentley
 *
 */
public class JInputGamePadService extends JInputEngineAbstract {

   public JInputGamePadService() {

   }

   /**
    * Where do we map the pad type? shouldn't we create a class for each pad... plug and play for devs.
    * 
    * <li> Associate a Controller File with {@link GamePad} implementation.
    * 
    * 
    * Each gamepad implements the bentley framework ExternalDevice which allows to query Button Names,
    * Calibration numbers for the events generated. minimum sensibility for analog switches.
    * 
    * A reference to the ExternalDevice
    * 
    * 
    * @param c
    * @return
    */
   private int getDeviceButton(Component c, float data) {
      //we have to map component event to a Bentley Framework value
      Identifier id = c.getIdentifier();
      if (id == Component.Identifier.Axis.X) {
         if (data == 1.0) {
            return IBCodes.PAD_RIGHT;
         } else if (data == -1.0) {
            return IBCodes.PAD_LEFT;
         } else {
            return 0;
         }
      } else if (id == Component.Identifier.Axis.Y) {
         //data being 1 is only valid for digital axis
         if (data == 1.0) {
            return IBCodes.PAD_DOWN;
         } else if (data == -1.0) {
            return IBCodes.PAD_UP;
         } else {
            return 0;
         }
      }
      if (id == Component.Identifier.Button._0) {
         return IBCodes.PAD_BUTTON_0;
      } else if (id == Component.Identifier.Button._1) {
         return IBCodes.PAD_BUTTON_1;
      } else if (id == Component.Identifier.Button._2) {
         return IBCodes.PAD_BUTTON_2;
      } else if (id == Component.Identifier.Button._3) {
         return IBCodes.PAD_BUTTON_3;
      } else if (id == Component.Identifier.Button._4) {
         return IBCodes.PAD_BUTTON_4;
      } else if (id == Component.Identifier.Button._5) {
         return IBCodes.PAD_BUTTON_5;
      } else if (id == Component.Identifier.Button._6) {
         return IBCodes.PAD_BUTTON_6;
      } else if (id == Component.Identifier.Button._7) {
         return IBCodes.PAD_BUTTON_7;
      } else if (id == Component.Identifier.Button._8) {
         return IBCodes.PAD_BUTTON_8;
      } else if (id == Component.Identifier.Button._9) {
         return IBCodes.PAD_BUTTON_9;
      }
      return 0;
   }

   /**
    * We get events as long as window is active.
    * <br>
    * DeviceID... We want to ID pads based on their type configs. Gamecubes and Megadrive are different pads.
    * <br>
    * @param ca
    */
   public void poll(ExternalInput ei) {
      Controller ca = ei.controller;
      if (ca.getType() == Type.GAMEPAD || ca.getType() == Type.STICK) {
         if (!ca.poll()) {
            //could not poll controller
            toDLog().pEvent1("Poll Disabled for" + ca.getName(), null, JInputGamePadService.class, "poll");
         } else {
            //check if it is the first mouse
            //fc.getUI().dLog().ptEvent1("Poll " + ca.getName(), null, JInputGamePadService.class, "poll");
            EventQueue eventQueue = ca.getEventQueue();
            Event event = new Event();
            while (eventQueue.getNextEvent(event)) {
               //#debug
               toDLog().pBridge1("Poll " + ca.getName() + " " + event.getValue(), null, JInputGamePadService.class, "poll");
               GamePad gp = (GamePad) ei.exdevice;
               final DeviceEvent de = gp.getEvent(event, ei);
               //we are in the jinput thread, so we have to call serially
               //publish event on the active canvas?
               jic.getCoreUiCtx().runGUI(new Runnable() {

                  public void run() {
                     //System.out.println("Publish" + de.toString1Line());
                     //this gamepad manager has no idea which canvas has the focus
                     //other services with mouse focus tracking.. can find the canvashost and canvasappli
                     jic.getCoreUiCtx().publishEvent(de, null);
                  }
               });

            }
         }
      }
   }

   public void toString(Dctx dc) {
      dc.root(this, "JInputGamePadService");
      super.toString(dc.sup());
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "JInputGamePadService");
      super.toString1Line(dc.sup1Line());
   }

}
