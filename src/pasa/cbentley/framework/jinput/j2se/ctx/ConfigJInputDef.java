package pasa.cbentley.framework.jinput.j2se.ctx;

public class ConfigJInputDef implements IConfigJInput {

   public int getBeatsForRefresh() {
      return 200; //200 * 20 = 4 seconds
   }

   public boolean isRefreshControllers() {
      return false;
   }

   public int getHeartBeatMilliSeconds() {
      return 20;
   }

}
