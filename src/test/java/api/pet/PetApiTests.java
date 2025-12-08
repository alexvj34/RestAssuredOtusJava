package api.pet;

import api.BaseApiTest;
import api.dto.CategoryDto;
import api.dto.PetDto;
import api.dto.TagDto;
import api.specifications.ResponseSpecs;
import api.utils.TestDataGenerator;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Tags({
    @Tag("api"),
    @Tag("pet"),
    @Tag("smoke")
})
@DisplayName("Pet API Tests - Working Version")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PetApiTests extends BaseApiTest {

  private static Long petId;
  private static PetDto testPet;

  @BeforeAll
  static void setup() {
    // Правильное использование логгера в статическом методе
    Logger log = LoggerFactory.getLogger(PetApiTests.class);
    petId = TestDataGenerator.generateSafeId();

    CategoryDto category = new CategoryDto(1L, "Dogs");

    List<TagDto> tags = Arrays.asList(
        new TagDto(1L, "friendly"),
        new TagDto(2L, "trained")
    );

    List<String> photoUrls = Arrays.asList(
        "https://example.com/photo1.jpg",
        "https://example.com/photo2.jpg"
    );

    testPet = new PetDto(
        petId,
        category,
        "Buddy_" + petId,
        photoUrls,
        tags,
        PetDto.Status.AVAILABLE
    );

    log.info("Test pet created with ID: {}", petId);
  }

  @Test
  @Order(1)
  @DisplayName("POST /v2/pet - Create new pet with valid data")
  void shouldCreateNewPet() {
    log.info("Creating new pet with ID: {}", petId);

    given()
        .baseUri("https://petstore.swagger.io")
        .basePath("/v2/pet")
        .contentType("application/json")
        .body(testPet)
        .when()
        .post()
        .then()
        .spec(ResponseSpecs.successSpec()) // Используем спецификацию ответа
        .body("id", equalTo(petId.intValue()))
        .body("name", equalTo(testPet.getName()))
        .body("status", equalTo("AVAILABLE"))
        .body("category.name", equalTo("Dogs"))
        .body("tags", hasSize(2));

    log.info("Successfully created pet with ID: {}", petId);
  }

  @Test
  @Order(2)
  @DisplayName("GET /v2/pet/{petId} - Retrieve created pet by ID")
  void shouldGetPetById() {
    log.info("Retrieving pet with ID: {}", petId);

    given()
        .baseUri("https://petstore.swagger.io")
        .basePath("/v2/pet")
        .when()
        .get("/{petId}", petId)
        .then()
        .spec(ResponseSpecs.successSpec())
        .body("id", equalTo(petId.intValue()))
        .body("name", equalTo(testPet.getName()))
        .body("status", equalTo("AVAILABLE"))
        .body("tags[0].name", equalTo("friendly"));

    log.info("Successfully retrieved pet with ID: {}", petId);
  }

  @Test
  @Order(3)
  @DisplayName("PUT /v2/pet - Update existing pet information")
  void shouldUpdatePet() {
    log.info("Updating pet with ID: {}", petId);

    // Обновляем данные питомца
    testPet.setName("Updated_" + testPet.getName());
    testPet.setStatus(PetDto.Status.SOLD);

    given()
        .baseUri("https://petstore.swagger.io")
        .basePath("/v2/pet")
        .contentType("application/json")
        .body(testPet)
        .when()
        .put()
        .then()
        .spec(ResponseSpecs.successSpec())
        .body("id", equalTo(petId.intValue()))
        .body("name", equalTo(testPet.getName()))
        .body("status", equalTo("SOLD"));

    log.info("Successfully updated pet with ID: {}", petId);
  }

  @Test
  @Order(4)
  @DisplayName("GET /v2/pet/findByStatus - Find pets by sold status")
  void shouldFindPetsByStatus() {
    log.info("Searching for pets with status: sold");

    given()
        .baseUri("https://petstore.swagger.io")
        .basePath("/v2/pet")
        .queryParam("status", "sold")
        .when()
        .get("/findByStatus")
        .then()
        .spec(ResponseSpecs.successSpec())
        .body("$", hasSize(greaterThanOrEqualTo(0)));

    log.debug("Found pets with sold status");
  }

  @Test
  @Order(5)
  @DisplayName("DELETE /v2/pet/{petId} - Delete existing pet")
  void shouldDeletePet() {
    log.info("Deleting pet with ID: {}", petId);

    given()
        .baseUri("https://petstore.swagger.io")
        .basePath("/v2/pet")
        .when()
        .delete("/{petId}", petId)
        .then()
        .spec(ResponseSpecs.successSpec())
        .body("code", equalTo(200))
        .body("type", equalTo("unknown"))
        .body("message", equalTo(petId.toString()));

    log.info("Successfully deleted pet with ID: {}", petId);

    // Проверяем, что питомец действительно удален
    log.info("Verifying pet deletion for ID: {}", petId);

    given()
        .baseUri("https://petstore.swagger.io")
        .basePath("/v2/pet")
        .when()
        .get("/{petId}", petId)
        .then()
        .statusCode(404)
        .body("type", equalTo("error"))
        .body("message", equalTo("Pet not found"));

    log.info("Verified pet deletion for ID: {}", petId);
  }

  @Test
  @DisplayName("GET /v2/pet/{petId} - Return 404 for non-existent pet")
  void shouldReturn404ForNonExistentPet() {
    Long nonExistentId = 999999L;
    log.info("Testing 404 response for non-existent pet ID: {}", nonExistentId);

    given()
        .baseUri("https://petstore.swagger.io")
        .basePath("/v2/pet")
        .when()
        .get("/{petId}", nonExistentId)
        .then()
        .statusCode(404)
        .body("type", equalTo("error"))
        .body("message", equalTo("Pet not found"));

    log.info("Successfully received 404 for non-existent pet ID: {}", nonExistentId);
  }

  @Test
  @DisplayName("POST /v2/pet - Create pet with minimal required fields")
  void shouldCreatePetWithMinimalData() {
    Long minimalPetId = TestDataGenerator.generateSafeId();
    log.info("Creating minimal pet with ID: {}", minimalPetId);

    PetDto minimalPet = new PetDto(
        minimalPetId,
        null,
        "MinimalPet",
        Arrays.asList("https://example.com/default.jpg"),
        null,
        null
    );

    given()
        .baseUri("https://petstore.swagger.io")
        .basePath("/v2/pet")
        .contentType("application/json")
        .body(minimalPet)
        .when()
        .post()
        .then()
        .statusCode(200)
        .body("id", equalTo(minimalPetId.intValue()))
        .body("name", equalTo("MinimalPet"))
        .body("photoUrls", hasSize(1));

    log.info("Successfully created minimal pet with ID: {}", minimalPetId);

    // Cleanup
    log.info("Cleaning up minimal pet with ID: {}", minimalPetId);

    given()
        .baseUri("https://petstore.swagger.io")
        .basePath("/v2/pet")
        .delete("/{petId}", minimalPetId);

    log.info("Cleanup completed for minimal pet with ID: {}", minimalPetId);
  }
}