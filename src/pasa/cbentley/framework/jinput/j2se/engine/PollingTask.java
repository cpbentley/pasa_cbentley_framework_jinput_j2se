package pasa.cbentley.framework.jinput.j2se.engine;

import java.util.Iterator;
import java.util.List;

import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.framework.jinput.j2se.ctx.JInputCtx;
import pasa.cbentley.framework.jinput.j2se.ctx.ObjectJIC;

public class PollingTask extends ObjectJIC implements Runnable, IStringable {

   protected final JInputServiceAbstract engine;

   private List<ControllerBentley>      exs;

   private ControllerBentley failure;

   private volatile boolean             isRunning = true;

   public PollingTask(JInputCtx jic, JInputServiceAbstract engine) {
      super(jic);
      this.engine = engine;
   }

   public boolean isRunning() {
      return isRunning;
   }

   public void run() {
      exs = jic.getListGamePadSticks();
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
            if (failure != null) {
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

   public void setRunning(boolean isRunning) {
      this.isRunning = isRunning;
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, PollingTask.class, 85);
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, PollingTask.class, 85);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   private void toStringPrivate(Dctx dc) {

   }
   //#enddebug

}
