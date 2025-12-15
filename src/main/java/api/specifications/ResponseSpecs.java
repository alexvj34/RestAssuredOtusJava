package api.specifications;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.Matchers;

import java.util.concurrent.TimeUnit;

import static config.Config.getResponseTimeout;
import static org.hamcrest.Matchers.equalTo;

public class ResponseSpecs {

  private ResponseSpecs() {
  }

  public static ResponseSpecification successSpec() {
    return new ResponseSpecBuilder()
        .expectContentType(ContentType.JSON)
        .expectResponseTime(Matchers.lessThan(getResponseTimeout()), TimeUnit.MILLISECONDS)
        .expectStatusCode(200)
        .build();
  }

  public static ResponseSpecification createdSpec() {
    return new ResponseSpecBuilder()
        .expectStatusCode(201)
        .build();
  }

  public static ResponseSpecification notFoundSpec() {
    return new ResponseSpecBuilder()
        .expectContentType(ContentType.JSON)
        .expectStatusCode(404)
        .expectBody("type", equalTo("error"))
        .build();
  }

  public static ResponseSpecification badRequestSpec() {
    return new ResponseSpecBuilder()
        .expectContentType(ContentType.JSON)
        .expectStatusCode(400)
        .build();
  }
}