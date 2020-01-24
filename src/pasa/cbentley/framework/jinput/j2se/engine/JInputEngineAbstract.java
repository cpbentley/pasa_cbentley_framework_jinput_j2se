package pasa.cbentley.framework.jinput.j2se.engine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.java.games.input.Controller;
import net.java.games.input.Controller.Type;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.ControllerEvent;
import net.java.games.input.ControllerListener;
import pasa.cbentley.core.src4.ctx.ACtx;
import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IDLog;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.framework.core.j2se.ctx.CoreJ2seCtx;
import pasa.cbentley.framework.core.src4.interfaces.IAPIService;
import pasa.cbentley.framework.coreui.src4.ctx.CoreUiCtx;
import pasa.cbentley.framework.coreui.src4.event.SenseEvent;
import pasa.cbentley.framework.coreui.src4.interfaces.IExternalDevice;
import pasa.cbentley.framework.coreui.src4.interfaces.ISenses;
import pasa.cbentley.framework.coreui.src4.tech.IInput;
import pasa.cbentley.framework.jinput.j2se.ctx.JInputCtx;
import pasa.cbentley.framework.jinput.j2se.gamepads.GameCubePad;
import pasa.cbentley.framework.jinput.j2se.gamepads.GenericPad;
import pasa.cbentley.framework.jinput.j2se.gamepads.Sega6Buttons;

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
 * <br>
 * <br>
 * <br>
 * <br>
 * User wants to configure a controller, ask number of keys and which keys. It requests the {@link IExternalDevice}
 * @author Charles Bentley
 *
 */
public abstract class JInputEngineAbstract implements IAPIService, IInput, IStringable, ControllerListener {

   protected static final long   DEVICE_CHECK      = 4000;

   protected static final long   HEARTBEATMS       = 20;

   /**
    * Iterate faster on active controllers
    */
   List<Controller>              activeControllers = new ArrayList();

   /**
    * 
    */
   protected List<ExternalInput> exs               = new ArrayList();

   private boolean               isRunning         = false;

   protected JInputCtx           jic;

   private Thread                serviceThread;

   public JInputEngineAbstract() {
      ControllerEnvironment ce = ControllerEnvironment.getDefaultEnvironment();
      ce.addControllerListener(this);
   }

   /**
    * Called at the start and when an event asks for it
    * <li> Windows New Device
    * <li> User Command with refresh hook
    * <br>
    * This method should work and generate events when a GamePAD is disconnected or when it is connected
    */
   public void cmdRefreshControllerList() {

      //#debug
      jic.toDLog().pEvent1("Start", this, JInputEngineAbstract.class, "cmdRefreshControllerList");

      ControllerEnvironment ce = ControllerEnvironment.getDefaultEnvironment();

      //list of current controllers
      Controller[] ca = ce.getControllers();
      List<ExternalInput> newexs = new ArrayList();
      int gamePadID = 1;
      for (int i = 0; i < ca.length; i++) {
         Controller c = ca[i];
         Type t = c.getType();
         if (t == Controller.Type.GAMEPAD || t == Controller.Type.STICK) {
            //check if its already in our list.
            ExternalInput eiFound = isListed(c);
            if (eiFound != null) {
               //remove it and put it in the new list
               newexs.add(eiFound);
               exs.remove(eiFound);
            } else {
               ExternalInput ei = new ExternalInput();
               ei.controller = c;

               //find a matching 
               String name = c.getName();
               if (name.indexOf("GameCube") != -1) {
                  ei.exdevice = new GameCubePad(jic);
               } else if (name.indexOf("Sega") != -1) {
                  ei.exdevice = new Sega6Buttons(jic);
               } else if (name.indexOf("DB9") != -1) {
                  ei.exdevice = new Sega6Buttons(jic);
               } else {
                  //generic not supported
                  ei.exdevice = new GenericPad(jic);
                  continue;
               }
               ei.id = gamePadID;
               gamePadID++;
               newexs.add(ei);
               deviceConnectionEvent(ei, true);

            }
         }
      }
      //send disconnect for others
      for (Iterator ite = exs.iterator(); ite.hasNext();) {
         ExternalInput ei = (ExternalInput) ite.next();
         deviceConnectionEvent(ei, false);
      }
      exs = newexs;

      //#debug
      jic.toDLog().pEvent1("End", this, JInputEngineAbstract.class, "cmdRefreshControllerList");

   }

   public void controllerAdded(ControllerEvent ce) {
      Controller c = ce.getController();
      //#debug
      jic.toDLog().pEvent1("Name=" + c.getName(), this, JInputEngineAbstract.class, "controllerAdded");

   }

   public void controllerRemoved(ControllerEvent ce) {
      Controller c = ce.getController();
      //#debug
      jic.toDLog().pEvent1("Name=" + c.getName(), this, JInputEngineAbstract.class, "controllerRemoved");
   }

   public void deviceConnectionEvent(ExternalInput ei, boolean isConnect) {
      int deviceButton = 0;
      if (isConnect) {
         deviceButton = ISenses.DEVICE_CONNECTED;
      } else {
         deviceButton = ISenses.DEVICE_DISCONNECTED;
      }
      final SenseEvent se = new SenseEvent(jic.getCoreUiCtx(), ISenses.GESTURE_TYPE_12_DEVICE, deviceButton, ei.id);
      //final DeviceEvent de = new DeviceEvent(fc, DEVICE_2_GAMEPAD, ei.id, MOD_4_SENSED, deviceButton);
      se.setParamO1(ei.exdevice);

      CoreUiCtx cac = jic.getCoreUiCtx();
      //we are in the jinput thread, so we have to call serially
      //publish event on the active canvas?
      cac.runGUI(new Runnable() {

         public void run() {
            //this gamepad manager has no idea which canvas has the focus
            //other services with mouse focus tracking.. can find the canvashost and canvasappli
            cac.publishEvent(se, null);
         }
      });
   }

   private ExternalInput isListed(Controller c) {
      for (Iterator ite = exs.iterator(); ite.hasNext();) {
         ExternalInput cw = (ExternalInput) ite.next();
         if (cw.controller == c) {
            return cw;
         }
      }
      return null;
   }

   public boolean isServiceRunning(int id) {
      return serviceThread != null;
   }

   /**
    * We get events as long as window is active
    * @param ca
    */
   public abstract void poll(ExternalInput ca);

   public void setCtx(ACtx context) {
      if(context instanceof CoreJ2seCtx) {
         this.jic = new JInputCtx((CoreJ2seCtx)context);
      } else {
         throw new IllegalArgumentException();
      }
   }

   /**
    * Listen for events and listen for controllers.
    * TODO send signals to stop / start listening for new controllers.
    */
   public boolean startService(int id) {
      isRunning = true;
      cmdRefreshControllerList();
      serviceThread = new Thread(new Runnable() {
         public void run() {
            try {
               int counterForDeviceCheck = 0;
               while (isRunning) {
                  for (Iterator i = exs.iterator(); i.hasNext();) {
                     try {
                        ExternalInput cw = (ExternalInput) i.next();
                        poll(cw);
                     } catch (Exception e) {
                        e.printStackTrace();
                     }
                  }
                  counterForDeviceCheck += HEARTBEATMS;
                  if (counterForDeviceCheck >= DEVICE_CHECK) {
                     counterForDeviceCheck = 0;
                     //cmdRefreshControllerList();
                  }
                  Thread.sleep(HEARTBEATMS);
               }
            } catch (Exception e) {
               e.printStackTrace();
            }
         }
      });
      serviceThread.start();
      return true;
   }

   public boolean stopService(int id) {
      isRunning = false;
      serviceThread = null;
      return true;
   }

   public IDLog toDLog() {
      return jic.toDLog();
   }

   //#mdebug
   public String toString() {
      return Dctx.toString(this);
   }

   public void toString(Dctx dc) {
      dc.root(this, "JInputEngineAbstract");
      dc.appendVarWithSpace("isRunning", isRunning);
      dc.appendVarWithSpace("HEARTBEATMS", HEARTBEATMS);
      dc.appendVarWithSpace("#controllers", exs.size());
      for (Iterator i = exs.iterator(); i.hasNext();) {
         try {
            ExternalInput cw = (ExternalInput) i.next();
            dc.nl();
            dc.append(cw.controller.getName());
            dc.nlLvl("Pad #" + cw.id, cw.exdevice);
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "JInputEngineAbstract");
      if (isRunning) {
         dc.appendWithSpace("Running");
      } else {
         dc.appendWithSpace("Stopped");
      }
      dc.appendVarWithSpace("#controllers", exs.size());
   }
   //#enddebug

   public UCtx toStringGetUCtx() {
      return jic.getUCtx();
   }
}
