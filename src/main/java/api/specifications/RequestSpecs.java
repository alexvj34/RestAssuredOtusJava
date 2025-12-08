package api.specifications;

import config.Config;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;

import static io.restassured.http.ContentType.JSON;

public class RequestSpecs {

  private RequestSpecs() {
  }

  private static RestAssuredConfig getConfig() {
    return RestAssuredConfig.config()
        .httpClient(HttpClientConfig.httpClientConfig()
            .setParam("http.connection.timeout", (int) Config.getConnectionTimeout())
            .setParam("http.socket.timeout", (int) Config.getResponseTimeout()));
  }

  public static RequestSpecification baseSpec() {
    return new RequestSpecBuilder()
        .setBaseUri(Config.getBaseUrl().replace("/v2/", ""))
        .setContentType(JSON)
        .setAccept(JSON)
        .setConfig(getConfig())
        .log(LogDetail.URI)
        .log(LogDetail.METHOD)
        .build();
  }

  public static RequestSpecification petSpec() {
    return new RequestSpecBuilder()
        .setBasePath("/v2/pet")
        .build();
  }

  public static RequestSpecification storeSpec() {
    return new RequestSpecBuilder()
        .setBasePath("/v2/store")
        .build();
  }

  public static RequestSpecification userSpec() {
    return new RequestSpecBuilder()
        .setBasePath("/v2/user")
        .build();
  }
}