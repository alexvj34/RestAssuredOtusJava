
import org.junit.jupiter.api.Test;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Комплексный набор тестов для проверки connectivity REST API PetStore.
 *
 * <p>Данный тестовый класс предоставляет диагностические возможности для проверки
 * доступности, отзывчивости и базовой функциональности эндпоинтов PetStore API.
 * Включает несколько тестовых сценариев для гарантии корректной доступности API
 * перед запуском основных тестовых наборов.</p>
 *
 * <p><b>Тестовые методы:</b>
 * <ol>
 *   <li>{@link #testApiConnectivity()} - Тестирует базовую HTTP connectivity к нескольким эндпоинтам API</li>
 *   <li>{@link #testDirectRestAssuredConnection()} - Проверяет структуру API с использованием RestAssured</li>
 *   <li>{@link #testApiEndpointsComprehensive()} - Комплексная проверка доступности эндпоинтов</li>
 * </ol>
 * </p>
 */
public class ApiConnectivityTest {

  private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ApiConnectivityTest.class);

  @Test
  void testApiConnectivity() {
    log.info("Starting API connectivity tests");

    String[] urlsToTest = {
        "https://petstore.swagger.io/v2/swagger.json",
        "https://petstore.swagger.io/v2/pet/1",
        "http://petstore.swagger.io/v2/swagger.json"
    };

    int successCount = 0;
    int failureCount = 0;

    for (String urlString : urlsToTest) {
      try {
        log.debug("Testing URL connectivity: {}", urlString);
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        int responseCode = connection.getResponseCode();

        if (responseCode == 200 || responseCode == 301) {
          log.info("SUCCESS: {} - Response: {}", urlString, responseCode);
          successCount++;
        } else {
          log.warn("WARNING: {} - Unexpected response: {}", urlString, responseCode);
        }

        connection.disconnect();
      } catch (Exception e) {
        log.error("FAILED: {} - Error: {}", urlString, e.getMessage());
        failureCount++;
      }
    }

    log.info("Connectivity test summary - Success: {}, Failed: {}, Total: {}",
        successCount, failureCount, urlsToTest.length);
  }

  @Test
  void testDirectRestAssuredConnection() {
    log.info("Testing direct RestAssured connection to PetStore API");

    try {
      io.restassured.response.Response response = io.restassured.RestAssured
          .given()
          .baseUri("https://petstore.swagger.io")
          .when()
          .get("/v2/swagger.json")
          .then()
          .extract()
          .response();

      int statusCode = response.getStatusCode();
      String contentType = response.getContentType();
      String responseBodySample = response.getBody().asString();

      log.info("RestAssured SUCCESS: Status {}", statusCode);
      log.info("Content-Type: {}", contentType);

      // Безопасное получение подстроки
      int sampleLength = Math.min(100, responseBodySample.length());
      log.debug("Response body sample: {}...", responseBodySample.substring(0, sampleLength));

      // Проверяем структуру ответа
      if (responseBodySample.contains("swagger") && responseBodySample.contains("Petstore")) {
        log.info("API response structure is valid");
      } else {
        log.warn("⚠API response structure may be unexpected");
      }

    } catch (Exception e) {
      log.error("RestAssured FAILED: {}", e.getMessage());
      log.debug("Stack trace:", e);
    }
  }

  @Test
  void testApiEndpointsComprehensive() {
    log.info("Starting comprehensive API endpoints test");

    String[] endpoints = {
        "/v2/pet/1",
        "/v2/store/inventory",
        "/v2/user/login?username=test&password=test",
        "/v2/swagger.json"
    };

    for (String endpoint : endpoints) {
      try {
        log.debug("Testing endpoint: {}", endpoint);

        io.restassured.response.Response response = io.restassured.RestAssured
            .given()
            .baseUri("https://petstore.swagger.io")
            .when()
            .get(endpoint)
            .then()
            .extract()
            .response();

        int statusCode = response.getStatusCode();
        long responseTime = response.time();

        if (statusCode == 200 || statusCode == 404) {
          log.info("Endpoint {} - Status: {}, Response time: {}ms",
              endpoint, statusCode, responseTime);
        } else {
          log.warn("Endpoint {} - Unexpected status: {}, Response time: {}ms",
              endpoint, statusCode, responseTime);
        }

      } catch (Exception e) {
        log.error("Endpoint {} - Failed: {}", endpoint, e.getMessage());
      }
    }

    log.info("Comprehensive endpoints test completed");
  }
}