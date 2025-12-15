package api.store;


import api.BaseApiTest;
import api.dto.OrderDto;
import api.dto.PetDto;
import api.utils.TestDataGenerator;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Tags({
    @Tag("api"),
    @Tag("store"),
    @Tag("smoke")
})
@DisplayName("Store API Tests - Working Version")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StoreApiTests extends BaseApiTest {

  private static Long orderId;
  private static Long petId;

  @BeforeAll
  static void setup() {
    Logger log = LoggerFactory.getLogger(StoreApiTests.class);
    orderId = TestDataGenerator.generateSafeId();
    petId = createTestPet();
    log.info("Test setup completed - Order ID: {}, Pet ID: {}", orderId, petId);
  }

  private static Long createTestPet() {
    Logger log = LoggerFactory.getLogger(StoreApiTests.class);

    PetDto pet = new PetDto(
        TestDataGenerator.generateSafeId(),
        null,
        "StoreTestPet_" + orderId,
        Arrays.asList("https://example.com/photo.jpg"),
        null,
        PetDto.Status.AVAILABLE
    );

    log.debug("Creating test pet for store order");

    PetDto response = given()
        .baseUri("https://petstore.swagger.io")
        .basePath("/v2/pet")
        .contentType("application/json")
        .body(pet)
        .when()
        .post()
        .then()
        .extract()
        .as(PetDto.class);

    log.info("Test pet created with ID: {}", response.getId());
    return response.getId();
  }

  @Test
  @Order(1)
  @DisplayName("POST /v2/store/order - Create new order with valid data")
  void shouldCreateNewOrder() {
    log.info("Creating new order with ID: {} for pet ID: {}", orderId, petId);

    OrderDto order = new OrderDto(
        orderId,
        petId,
        1,
        LocalDateTime.now().plusDays(1).format(DateTimeFormatter.ISO_DATE_TIME),
        OrderDto.Status.PLACED,
        false
    );

    given()
        .baseUri("https://petstore.swagger.io")
        .basePath("/v2/store")
        .contentType("application/json")
        .body(order)
        .when()
        .post("/order")
        .then()
        .statusCode(200)
        .body("id", equalTo(orderId.intValue()))
        .body("petId", equalTo(petId.intValue()))
        .body("quantity", equalTo(1))
        .body("status", equalTo("PLACED"))
        .body("complete", equalTo(false));

    log.info("Successfully created order with ID: {}", orderId);
  }

  @Test
  @Order(2)
  @DisplayName("GET /v2/store/order/{orderId} - Retrieve order by ID")
  void shouldGetOrderById() {
    log.info("Retrieving order with ID: {}", orderId);

    given()
        .baseUri("https://petstore.swagger.io")
        .basePath("/v2/store")
        .when()
        .get("/order/{orderId}", orderId)
        .then()
        .statusCode(200)
        .body("id", equalTo(orderId.intValue()))
        .body("petId", equalTo(petId.intValue()))
        .body("status", equalTo("PLACED"));

    log.info("Successfully retrieved order with ID: {}", orderId);
  }

  @Test
  @Order(3)
  @DisplayName("GET /v2/store/inventory - Retrieve store inventory")
  void shouldGetStoreInventory() {
    log.info("Retrieving store inventory");

    given()
        .baseUri("https://petstore.swagger.io")
        .basePath("/v2/store")
        .when()
        .get("/inventory")
        .then()
        .statusCode(200)
        .body("available", notNullValue())
        .body("pending", notNullValue())
        .body("sold", notNullValue());

    log.debug("Store inventory retrieved successfully");
  }

  @Test
  @Order(4)
  @DisplayName("DELETE /v2/store/order/{orderId} - Delete order")
  void shouldDeleteOrder() {
    log.info("Deleting order with ID: {}", orderId);

    given()
        .baseUri("https://petstore.swagger.io")
        .basePath("/v2/store")
        .when()
        .delete("/order/{orderId}", orderId)
        .then()
        .statusCode(200)
        .body("code", equalTo(200))
        .body("type", equalTo("unknown"))
        .body("message", equalTo(orderId.toString()));

    log.info("Successfully deleted order with ID: {}", orderId);

    // Cleanup - delete test pet
    log.info("Cleaning up test pet with ID: {}", petId);

    given()
        .baseUri("https://petstore.swagger.io")
        .basePath("/v2/pet")
        .delete("/{petId}", petId);

    log.info("Cleanup completed for pet with ID: {}", petId);
  }

  @Test
  @DisplayName("GET /v2/store/order/{orderId} - Return 404 for non-existent order")
  void shouldReturn404ForNonExistentOrder() {
    Long nonExistentOrderId = 999999L;
    log.info("Testing 404 response for non-existent order ID: {}", nonExistentOrderId);

    given()
        .baseUri("https://petstore.swagger.io")
        .basePath("/v2/store")
        .when()
        .get("/order/{orderId}", nonExistentOrderId)
        .then()
        .statusCode(404)
        .body("code", equalTo(1))
        .body("type", equalTo("error"))
        .body("message", equalTo("Order not found"));

    log.info("Successfully received 404 for non-existent order ID: {}", nonExistentOrderId);
  }
}