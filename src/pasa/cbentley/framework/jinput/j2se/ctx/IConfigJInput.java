package pasa.cbentley.framework.jinput.j2se.ctx;

public interface IConfigJInput {

   /**
    * Number of beats after which a refresh of available controller is
    * @return
    */
   public int getBeatsForRefresh();

   /**
    * When false, plugin in new controllers won't automatically be picked up.
    * @return
    */
   public boolean isRefreshControllers();

   /**
    * Number of milliseconds between polling
    * @return
    */
   public int getHeartBeatMilliSeconds();

   public String getPathLibrary();
}
