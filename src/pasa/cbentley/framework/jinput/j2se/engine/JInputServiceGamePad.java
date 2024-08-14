package pasa.cbentley.framework.jinput.j2se.engine;

import com.sun.org.apache.bcel.internal.generic.PUTFIELD;

import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Controller;
import net.java.games.input.Controller.Type;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;
import pasa.cbentley.core.src4.event.ILifeContext;
import pasa.cbentley.core.src4.helpers.StringBBuilder;
import pasa.cbentley.core.src4.interfaces.IExecutor;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.core.ui.src4.event.DeviceEvent;
import pasa.cbentley.framework.core.ui.src4.interfaces.IExternalDevice;
import pasa.cbentley.framework.core.ui.src4.tech.ITechCodes;
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
public class JInputServiceGamePad extends JInputServiceAbstract {

   private int failure;

   public JInputServiceGamePad() {

   }

   public void lifePaused(ILifeContext context) {
      //#debug
      toDLog().pFlow("", this, JInputServiceGamePad.class, "lifePaused@54", LVL_05_FINE, true);

   }

   public void lifeResumed(ILifeContext context) {
      //#debug
      toDLog().pFlow("", this, JInputServiceGamePad.class, "lifeResumed@70", LVL_05_FINE, true);

   }

   public void lifeStarted(ILifeContext context) {
      //#debug
      toDLog().pFlow("", this, JInputServiceGamePad.class, "lifeStarted@66", LVL_05_FINE, true);
   }

   public void lifeStopped(ILifeContext context) {
      //#debug
      toDLog().pFlow("", this, JInputServiceGamePad.class, "lifeStopped@70", LVL_05_FINE, true);
      stopService(0, null);
   }

   /**
    * We get events as long as window is active.
    * <br>
    * DeviceID... We want to ID pads based on their type configs. Gamecubes and Megadrive are different pads.
    * <br>
    * @param ca
    */
   public void poll(ControllerBentley controllerBentley) {
      Controller controller = controllerBentley.getController();
      if (controller.getType() == Type.GAMEPAD || controller.getType() == Type.STICK) {
         boolean isPolled = controller.poll();
         if (isPolled) {
            EventQueue eventQueue = controller.getEventQueue();
            Event event = new Event();
            while (eventQueue.getNextEvent(event)) {
               float value = event.getValue();

               //#mdebug
               StringBBuilder sb = new StringBBuilder(jic.getUC());
               sb.append("for ");
               sb.append(controller.getName());
               sb.append(" ");
               sb.append(" -> ");
               sb.append(event.getComponent().getName(), 10);
               sb.append(' ');
               sb.append(" id=");
               sb.append(event.getComponent().getIdentifier().getName(), 10);
               sb.append(" value=");
               sb.appendPretty(value, 3);
               sb.append(" time=");
               long nanos = event.getNanos();
               sb.append(String.valueOf(nanos));

               if (Math.abs(value) > 0.03f) {
                  toDLog().pEvent(sb.toString(), null, JInputServiceGamePad.class, "poll@110");
               }
               //#enddebug

               GamePadAbstract gamePad = (GamePadAbstract) controllerBentley.getExternalDevice();
               final DeviceEvent de = gamePad.getEvent(event, controllerBentley);
               if (de != null) {
                  IExternalDevice ed = controllerBentley.getExternalDevice();
                  if (ed == null) {
                     toDLog().pNull("Null IExternalDevice", this, JInputServiceGamePad.class, "poll@114", LVL_10_SEVERE, true);
                  }
                  de.setParamO1(ed);
                  //publish event on the active canvas?
                  TaskPublish taskPub = new TaskPublish(jic, de);
                  //we are in the jinput thread, so we have to call serially
                  IExecutor executor = jic.getCoreUiCtx().getExecutor();
                  executor.executeMainLater(taskPub);
               }
            }
         } else {
            //#debug
            toDLog().pEvent1("Poll failed for " + controller.getName(), null, JInputServiceGamePad.class, "poll@110");

            //issue with controllers.. restart poll task
            throw new IllegalStateException();
         }
      }
   }

   public void toString(Dctx dc) {
      dc.root(this, JInputServiceGamePad.class, 145);
      super.toString(dc.sup());
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, JInputServiceGamePad.class);
      super.toString1Line(dc.sup1Line());
   }

}
