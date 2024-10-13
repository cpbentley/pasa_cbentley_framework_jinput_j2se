package pasa.cbentley.framework.jinput.j2se.ctx;

import pasa.cbentley.core.src4.ctx.ConfigAbstract;
import pasa.cbentley.core.src4.logging.Dctx;

public class ConfigJInputDef extends ConfigAbstract implements IConfigJInput {

   public int getBeatsForRefresh() {
      return 200; //200 * 20 = 4 seconds
   }

   public boolean isRefreshControllers() {
      return false;
   }

   public int getHeartBeatMilliSeconds() {
      return 20;
   }

   public String getPathLibrary() {
      return "C:\\Java\\lib\\jinput";
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, ConfigJInputDef.class, 25);
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, ConfigJInputDef.class, 25);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   private void toStringPrivate(Dctx dc) {
      
   }
   //#enddebug
   

}
