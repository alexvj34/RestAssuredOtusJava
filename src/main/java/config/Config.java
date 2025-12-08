package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

  private static final Properties properties = new Properties();

  static {
    loadProperties();
  }

  private static void loadProperties() {
    try (InputStream input = Config.class.getClassLoader()
        .getResourceAsStream("config/application.properties")) {
      if (input == null) {
        System.out.println("Sorry, unable to find application.properties");
        // Set default values
        setDefaultProperties();
        return;
      }
      properties.load(input);
    } catch (IOException e) {
      System.out.println("Failed to load configuration file, using defaults: " + e.getMessage());
      setDefaultProperties();
    }
  }

  private static void setDefaultProperties() {
    properties.setProperty("dev.api.url", "https://petstore.swagger.io/v2/");
    properties.setProperty("api.response.timeout", "10000");
    properties.setProperty("api.connection.timeout", "15000");
    properties.setProperty("api.retry.attempts", "3");
    properties.setProperty("api.retry.delay", "2000");
  }

  public static String getBaseUrl() {
    return properties.getProperty("dev.api.url", "https://petstore.swagger.io/v2/");
  }

  public static long getResponseTimeout() {
    return Long.parseLong(properties.getProperty("api.response.timeout", "10000"));
  }

  public static long getConnectionTimeout() {
    return Long.parseLong(properties.getProperty("api.connection.timeout", "15000"));
  }

  public static int getRetryAttempts() {
    return Integer.parseInt(properties.getProperty("api.retry.attempts", "3"));
  }

  public static long getRetryDelay() {
    return Long.parseLong(properties.getProperty("api.retry.delay", "2000"));
  }

  public static String getAuthToken() {
    return properties.getProperty("api.token", "special-key");
  }
}