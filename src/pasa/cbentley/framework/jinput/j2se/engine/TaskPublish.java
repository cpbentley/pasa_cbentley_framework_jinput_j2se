package pasa.cbentley.framework.jinput.j2se.engine;

import pasa.cbentley.framework.coreui.src4.event.DeviceEvent;
import pasa.cbentley.framework.jinput.j2se.ctx.JInputCtx;

public class TaskPublish implements Runnable {

   protected final JInputCtx   jic;

   protected final DeviceEvent de;

   public TaskPublish(JInputCtx jic, DeviceEvent de) {
      this.jic = jic;
      this.de = de;

   }

   public void run() {
      jic.getCoreUiCtx().publishEvent(de);
   }

}
