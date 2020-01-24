package pasa.cbentley.framework.jinput.j2se.ctx;

import pasa.cbentley.core.src4.ctx.ACtx;
import pasa.cbentley.framework.core.j2se.ctx.CoreJ2seCtx;
import pasa.cbentley.framework.coreui.src4.ctx.CoreUiCtx;

public class JInputCtx extends ACtx {

   private CoreJ2seCtx jcac;

   public JInputCtx(CoreJ2seCtx cac) {
      super(cac.getUCtx());
      jcac = cac;
   }

   public CoreJ2seCtx getCAC() {
      return jcac;
   }

   public void setJ2SECanvasCtx(CoreJ2seCtx jcac) {
      this.jcac = jcac;
   }

   public CoreUiCtx getCoreUiCtx() {
      return jcac.getCUC();
   }
}
