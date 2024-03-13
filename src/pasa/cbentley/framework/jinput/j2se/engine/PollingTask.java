package pasa.cbentley.framework.jinput.j2se.engine;

import java.util.Iterator;
import java.util.List;

import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IDLog;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.framework.jinput.j2se.ctx.JInputCtx;

public class PollingTask implements Runnable, IStringable {

   protected final JInputCtx            jic;

   protected final JInputEngineAbstract engine;

   private volatile boolean             isRunning = true;

   private List<ControllerBentley>          exs;

   public PollingTask(JInputCtx jc, JInputEngineAbstract engine) {
      this(jc, engine, jc.getListGamePadSticks());
   }

   private ControllerBentley failure;
   
   public PollingTask(JInputCtx jc, JInputEngineAbstract engine, List<ControllerBentley> devices) {
      this.jic = jc;
      this.engine = engine;
      exs = devices;
   }

   public void run() {
      try {
         int counterForDeviceCheck = 0;
         while (isRunning()) {
            for (Iterator i = exs.iterator(); i.hasNext();) {
               ControllerBentley cw = (ControllerBentley) i.next();
               try {
                  engine.poll(cw);
               } catch (IllegalStateException e) {
                  //break from loop.. update like
                  failure = cw;
                  break;
               } catch (Exception e) {
                  e.printStackTrace();
               }
            }
            long beats = jic.getHeartBeatMilliSeconds();
            counterForDeviceCheck += beats;
            if(failure != null) {
               exs = jic.getListGamePadSticks();
              boolean isRemoved = exs.remove(failure); //make sure its removed, if hardware scan failed
              //#debug
               toDLog().pTest("msg", this, PollingTask.class, "run", LVL_05_FINE, true);
            }
            if (jic.isRefreshingDeviceList()) {
               if (counterForDeviceCheck >= jic.getBeatsForRefresh()) {
                  counterForDeviceCheck = 0;
                  //
                  exs = jic.getListGamePadSticks();
               }
            }
            Thread.sleep(beats);
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public boolean isRunning() {
      return isRunning;
   }

   public void setRunning(boolean isRunning) {
      this.isRunning = isRunning;
   }

   //#mdebug
   public IDLog toDLog() {
      return toStringGetUCtx().toDLog();
   }

   public String toString() {
      return Dctx.toString(this);
   }

   public void toString(Dctx dc) {
      dc.root(this, PollingTask.class, "@line5");
      toStringPrivate(dc);
   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   private void toStringPrivate(Dctx dc) {

   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, PollingTask.class);
      toStringPrivate(dc);
   }

   public UCtx toStringGetUCtx() {
      return jic.getUC();
   }

   //#enddebug
   

}
