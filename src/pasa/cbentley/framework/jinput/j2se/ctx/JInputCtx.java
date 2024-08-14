package pasa.cbentley.framework.jinput.j2se.ctx;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.java.games.input.Controller;
import net.java.games.input.Controller.Type;
import net.java.games.input.ControllerEnvironment;
import pasa.cbentley.core.src4.ctx.ACtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.core.j2se.ctx.CoreFrameworkJ2seCtx;
import pasa.cbentley.framework.core.ui.src4.ctx.CoreUiCtx;
import pasa.cbentley.framework.core.ui.src4.interfaces.IExternalDevice;
import pasa.cbentley.framework.core.ui.src4.tech.ITechHostUI;
import pasa.cbentley.framework.jinput.j2se.engine.ControllerBentley;
import pasa.cbentley.framework.jinput.j2se.engine.JInputServiceAbstract;
import pasa.cbentley.framework.jinput.j2se.engine.JInputServiceGamePad;
import pasa.cbentley.framework.jinput.j2se.engine.PollingTask;
import pasa.cbentley.framework.jinput.j2se.gamepads.GamePadGameCube;
import pasa.cbentley.framework.jinput.j2se.gamepads.GamePadGeneric;
import pasa.cbentley.framework.jinput.j2se.gamepads.GamePadSega6Buttons;

/**
 * This module integrates JInput libraries into the Bentley Contextual Framework
 * 
 * @author Charles Bentley
 *
 */
public class JInputCtx extends ACtx {

   protected final IConfigJInput config;

   protected long                DEVICE_CHECK = 4000;

   protected long                HEARTBEATMS  = 20;

   private boolean               isRefreshingDeviceList;

   private CoreFrameworkJ2seCtx  jcac;

   public JInputCtx(IConfigJInput config, CoreFrameworkJ2seCtx cac) {
      super(cac.getUC());
      this.jcac = cac;

      this.config = config;
      HEARTBEATMS = config.getHeartBeatMilliSeconds();
      DEVICE_CHECK = config.getBeatsForRefresh();
      isRefreshingDeviceList = config.isRefreshControllers();

      //#debug
      toDLog().pInit("", this, JInputCtx.class, "Created@53", LVL_04_FINER, true);

   }

   public ControllerEnvironment createDefaultEnvironment() throws ReflectiveOperationException {

      // Find constructor (class is package private, so we can't access it directly)
      Class<?> defcClass = Class.forName("net.java.games.input.DefaultControllerEnvironment");
      Constructor<ControllerEnvironment> constructor = (Constructor<ControllerEnvironment>) defcClass.getDeclaredConstructors()[0];

      // Constructor is package private, so we have to deactivate access control checks
      constructor.setAccessible(true);

      // Create object with default constructor
      return constructor.newInstance();
   }

   public long getBeatsForRefresh() {
      return DEVICE_CHECK;
   }

   public CoreFrameworkJ2seCtx getCAC() {
      return jcac;
   }

   public ControllerBentley getContollerListed(Controller c, List<ControllerBentley> list) {
      for (Iterator ite = list.iterator(); ite.hasNext();) {
         ControllerBentley externalInput = (ControllerBentley) ite.next();
         if (externalInput.getController() == c) {
            return externalInput;
         }
      }
      return null;
   }

   /**
    * TODO fix
    * https://stackoverflow.com/questions/17413690/java-jinput-rescan-reload-controllers
    * 
    * https://jinput.github.io/jinput/
    * @return
    */
   public Controller[] getControllers() {
      Controller[] rawsCon;
      //generates errors
      //      DirectAndRawInputEnvironmentPlugin directEnv = new DirectAndRawInputEnvironmentPlugin();
      //      if (directEnv.isSupported()) {
      //         rawsCon = directEnv.getControllers();
      //      } else {
      //         rawsCon = ControllerEnvironment.getDefaultEnvironment().getControllers();
      //      }
      rawsCon = ControllerEnvironment.getDefaultEnvironment().getControllers();
      return rawsCon;
   }

   public CoreUiCtx getCoreUiCtx() {
      return jcac.getCUC();
   }

   public int getCtxID() {
      return 1001;
   }

   public long getHeartBeatMilliSeconds() {
      return HEARTBEATMS;
   }

   /**
    * Called at the start and when an event asks for it
    * 
    * <li> Windows New Device
    * <li> User Command with refresh hook
    * 
    * This method should work and generate events when a GamePAD is disconnected or when it is connected.
    * 
    * <p>
    * Called by {@link PollingTask} when
    * <li> running for the first time.
    * <li> after a failure
    * </p>
    */
   public List<ControllerBentley> getListGamePadSticks() {

      //#debug
      toDLog().pFlow("Start", this, JInputCtx.class, "getListGamePadSticks@125", LVL_04_FINER, true);

      //list of current controllers
      Controller[] controllersList = getControllers();
      List<ControllerBentley> list = new ArrayList<ControllerBentley>();
      int gamePadID = 0;
      for (int i = 0; i < controllersList.length; i++) {
         Controller controller = controllersList[i];
         Type type = controller.getType();

         if (type == Controller.Type.GAMEPAD || type == Controller.Type.STICK) {
            gamePadID++;

            IExternalDevice ex = null;
            //find a matching 
            String name = controller.getName();
            //#debug
            toDLog().pInit("Controller Name=" + name + " Type=" + type.toString(), this, JInputCtx.class, "getListGamePadSticks@142", LVL_05_FINE, true);
            if (name.indexOf("GameCube") != -1) {
               ex = new GamePadGameCube(this);
            } else if (name.indexOf("Sega") != -1) {
               ex = new GamePadSega6Buttons(this);
            } else if (name.indexOf("DB9") != -1) {
               ex = new GamePadSega6Buttons(this);
            } else if (name.indexOf("Multi") != -1) {
               ex = new GamePadSega6Buttons(this);
            } else {
               //generic not supported
               ex = new GamePadGeneric(this);
            }
            ControllerBentley ei = new ControllerBentley(this, controller, ex, gamePadID);
            list.add(ei);
         }
      }
      //#debug
      toDLog().pEvent1("Found " + list.size(), this, JInputServiceAbstract.class, "getListGamePadSticks");

      return list;

   }

   public boolean isContollerListed(Controller c, List<ControllerBentley> list) {
      return getContollerListed(c, list) != null;
   }

   public boolean isRefreshingDeviceList() {
      return isRefreshingDeviceList;
   }

   public void killThread() {
      final Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
      for (final Thread thread : threadSet) {
         final String name = thread.getClass().getName();
         if (name.equals("net.java.games.input.RawInputEventQueue$QueueThread")) {
            thread.interrupt();
            try {
               thread.join();
            } catch (final InterruptedException e) {
               thread.interrupt();
            }
         }
      }
   }

   public void loadLibrary() {
      // something like "C:\\Java\\lib\\jinput";
      String path = config.getPathLibrary();
      //#debug
      toDLog().pInit("Loading JInput from " + path, this, JInputCtx.class, "loadLibrary@208", LVL_05_FINE, true);
      File nativeDir = new File(path);
      System.setProperty("net.java.games.input.librarypath", nativeDir.getAbsolutePath());
      String className = JInputServiceGamePad.class.getName();
      int id = ITechHostUI.SUP_ID_38_GAMEPADS;
      jcac.getApiManager().setAPIService(id, className, this);
   }

   public void setJ2SECanvasCtx(CoreFrameworkJ2seCtx jcac) {
      this.jcac = jcac;
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, JInputCtx.class, 200);
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, JInputCtx.class, 200);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   private void toStringPrivate(Dctx dc) {
      dc.appendVarWithSpace("HEARTBEATMS", HEARTBEATMS);
   }
   //#enddebug

}
