package pasa.cbentley.framework.jinput.j2se.engine;

import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Controller;
import net.java.games.input.Controller.Type;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;
import pasa.cbentley.core.src4.event.ILifeContext;
import pasa.cbentley.core.src4.helpers.StringBBuilder;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.coreui.src4.event.DeviceEvent;
import pasa.cbentley.framework.coreui.src4.tech.ITechCodes;
import pasa.cbentley.framework.jinput.j2se.gamepads.GamePadAbstract;

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

   private int failure;

   public JInputGamePadService() {

   }

   public void lifePaused(ILifeContext context) {
      //#debug
      toDLog().pFlow("", this, JInputGamePadService.class, "lifePaused", LVL_05_FINE, true);

   }

   public void lifeResumed(ILifeContext context) {
      //#debug
      toDLog().pFlow("", this, JInputGamePadService.class, "lifeResumed", LVL_05_FINE, true);

   }

   public void lifeStarted(ILifeContext context) {

   }

   public void lifeStopped(ILifeContext context) {
      //#debug
      toDLog().pFlow("", this, JInputGamePadService.class, "lifeStopped", LVL_05_FINE, true);
      stopService(0);
   }

   /**
    * We get events as long as window is active.
    * <br>
    * DeviceID... We want to ID pads based on their type configs. Gamecubes and Megadrive are different pads.
    * <br>
    * @param ca
    */
   public void poll(ControllerBentley exInput) {
      Controller controller = exInput.getController();
      if (controller.getType() == Type.GAMEPAD || controller.getType() == Type.STICK) {
         boolean isPolled = controller.poll();
         if (isPolled) {
            EventQueue eventQueue = controller.getEventQueue();
            Event event = new Event();
            while (eventQueue.getNextEvent(event)) {
               //#mdebug
               StringBBuilder sb = new StringBBuilder(jic.getUCtx());
               sb.append("for ");
               sb.append(controller.getName());
               sb.append(" ");
               sb.append(" ->");
               sb.append(event.getComponent().getName(),7);
               sb.append(' ');
               sb.append(" value=");
               sb.appendPretty(event.getValue(),3);
               toDLog().pEvent(sb.toString(), null, JInputGamePadService.class, "poll");
               //#enddebug
               GamePadAbstract gp = (GamePadAbstract) exInput.getExdevice();
               final DeviceEvent de = gp.getEvent(event, exInput);
               if (de != null) {
                  //publish event on the active canvas?
                  TaskPublish taskPub = new TaskPublish(jic, de);
                  //we are in the jinput thread, so we have to call serially
                  jic.getCoreUiCtx().runGUI(taskPub);
               }
            }
         } else {
            //#debug
            toDLog().pEvent1("Poll failed for " + controller.getName(), null, JInputGamePadService.class, "poll@line107");

            //issue with controllers.. restart poll task
            throw new IllegalStateException();
         }
      }
   }

   public void toString(Dctx dc) {
      dc.root(this, JInputGamePadService.class, 145);
      super.toString(dc.sup());
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, JInputGamePadService.class);
      super.toString1Line(dc.sup1Line());
   }

}
