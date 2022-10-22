// You can experiment here, it won’t be checked

public class Task {
  public static void main(String[] args) {
    AppController controller = AppController.getInstance();
    controller.config.timeout = 2000;

    AppController anotherOne = AppController.getInstance();
    controller.config = new AppConfig(anotherOne.config.timeout + 100, "https://hyperskill.org/topics/knowledge-graph");

    System.out.println(anotherOne.config.timeout + " " + controller.config.serviceUrl);
  }
}


class AppController {

  private static AppController instance;

  public AppConfig config;

  private AppController(AppConfig config) {
    this.config = config;
  }

  public static AppController getInstance() {
    if (instance == null) {
      instance = new AppController(loadConfig());
    }
    return instance;
  }

  private static AppConfig loadConfig() {
    return new AppConfig(1000, "https://hyperskill.org");
  }
}

class AppConfig {

  public int timeout;
  public String serviceUrl;

  public AppConfig(int timeout, String serviceUrl) {
    this.timeout = timeout;
    this.serviceUrl = serviceUrl;
  }
}