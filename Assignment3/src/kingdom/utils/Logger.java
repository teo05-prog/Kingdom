package kingdom.utils;

public class Logger
{
  //singleton
  private static Logger instance;

  private Logger()
  {
  }

  public static synchronized Logger getInstance()
  {
    if (instance == null)
    {
      instance = new Logger();
    }
    return instance;
  }

  public synchronized void log(String message)
  {
    System.out.println("[LOG] " + message);
  }

  public void logAction(String actorName, String action, String details)
  {
    String message = actorName + " " + action;
    if (details != null && !details.isEmpty())
    {
      message += ": " + details;
    }
    log(message);
  }
}
