package api.user;

import api.BaseApiTest;
import api.dto.UserDto;
import api.utils.TestDataGenerator;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Tags({
    @Tag("api"),
    @Tag("user"),
    @Tag("smoke")
})
@DisplayName("User API Tests - Working Version")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserApiTests extends BaseApiTest {

  private static UserDto testUser;
  private static String username;

  @BeforeAll
  static void setup() {
    Logger log = LoggerFactory.getLogger(UserApiTests.class);
    testUser = TestDataGenerator.generateUser();
    username = testUser.getUsername();
    log.info("Test user created with username: {}", username);
  }

  @Test
  @Order(1)
  @DisplayName("POST /v2/user - Create new user with valid data")
  void shouldCreateNewUser() {
    log.info("Creating new user with username: {}", username);

    given()
        .baseUri("https://petstore.swagger.io")
        .basePath("/v2/user")
        .contentType("application/json")
        .body(testUser)
        .when()
        .post()
        .then()
        .statusCode(200)
        .body("code", equalTo(200))
        .body("type", equalTo("unknown"))
        .body("message", notNullValue());

    log.info("Successfully created user with username: {}", username);
  }

  @Test
  @Order(2)
  @DisplayName("GET /v2/user/{username} - Retrieve user by username")
  void shouldGetUserByUsername() {
    log.info("Retrieving user with username: {}", username);

    given()
        .baseUri("https://petstore.swagger.io")
        .basePath("/v2/user")
        .when()
        .get("/{username}", username)
        .then()
        .statusCode(200)
        .body("id", equalTo(testUser.getId().intValue()))
        .body("username", equalTo(username))
        .body("email", equalTo(testUser.getEmail()))
        .body("userStatus", equalTo(1));

    log.info("Successfully retrieved user with username: {}", username);
  }

  @Test
  @Order(3)
  @DisplayName("GET /v2/user/login - User login with valid credentials")
  void shouldLoginUser() {
    log.info("Testing login for user: {}", username);

    given()
        .baseUri("https://petstore.swagger.io")
        .basePath("/v2/user")
        .queryParam("username", username)
        .queryParam("password", testUser.getPassword())
        .when()
        .get("/login")
        .then()
        .statusCode(200)
        .body("code", equalTo(200))
        .body("type", equalTo("unknown"))
        .body("message", containsString("logged in user session:"));

    log.info("Successfully logged in user: {}", username);
  }

  @Test
  @Order(4)
  @DisplayName("PUT /v2/user/{username} - Update user information")
  void shouldUpdateUser() {
    log.info("Updating user information for: {}", username);

    // Обновляем данные пользователя
    testUser.setEmail("updated." + testUser.getEmail());
    testUser.setPhone("+9876543210");

    given()
        .baseUri("https://petstore.swagger.io")
        .basePath("/v2/user")
        .contentType("application/json")
        .body(testUser)
        .when()
        .put("/{username}", username)
        .then()
        .statusCode(200)
        .body("code", equalTo(200))
        .body("type", equalTo("unknown"));

    log.info("Successfully updated user: {}", username);
  }

  @Test
  @Order(5)
  @DisplayName("GET /v2/user/logout - User logout")
  void shouldLogoutUser() {
    log.info("Testing logout for user: {}", username);

    given()
        .baseUri("https://petstore.swagger.io")
        .basePath("/v2/user")
        .when()
        .get("/logout")
        .then()
        .statusCode(200)
        .body("code", equalTo(200))
        .body("type", equalTo("unknown"))
        .body("message", equalTo("ok"));

    log.info("Successfully logged out user: {}", username);
  }

  @Test
  @Order(6)
  @DisplayName("DELETE /v2/user/{username} - Delete user")
  void shouldDeleteUser() {
    log.info("Deleting user: {}", username);

    given()
        .baseUri("https://petstore.swagger.io")
        .basePath("/v2/user")
        .when()
        .delete("/{username}", username)
        .then()
        .statusCode(200)
        .body("code", equalTo(200))
        .body("type", equalTo("unknown"))
        .body("message", equalTo(username));

    log.info("Successfully deleted user: {}", username);

    // Проверяем, что пользователь удален
    log.info("Verifying user deletion for: {}", username);

    given()
        .baseUri("https://petstore.swagger.io")
        .basePath("/v2/user")
        .when()
        .get("/{username}", username)
        .then()
        .statusCode(404)
        .body("code", equalTo(1))
        .body("type", equalTo("error"))
        .body("message", equalTo("User not found"));

    log.info("Verified user deletion for: {}", username);
  }

  @Test
  @DisplayName("GET /v2/user/login - PetStore API returns 200 even for invalid credentials")
  void shouldReturn200ForInvalidCredentials() {
    log.info("Testing login with invalid credentials");

    // PetStore API особенность: всегда возвращает 200 даже для невалидных credentials
    given()
        .baseUri("https://petstore.swagger.io")
        .basePath("/v2/user")
        .queryParam("username", "nonexistentuser")
        .queryParam("password", "wrongpassword")
        .when()
        .get("/login")
        .then()
        .statusCode(200) // PetStore всегда возвращает 200
        .body("code", equalTo(200))
        .body("type", equalTo("unknown"));

    log.debug("Received expected 200 response for invalid credentials");
  }

  @Test
  @DisplayName("GET /v2/user/{username} - Return 404 for non-existent user")
  void shouldReturn404ForNonExistentUser() {
    String nonExistentUsername = "nonexistentuser123";
    log.info("Testing 404 response for non-existent user: {}", nonExistentUsername);

    given()
        .baseUri("https://petstore.swagger.io")
        .basePath("/v2/user")
        .when()
        .get("/{username}", nonExistentUsername)
        .then()
        .statusCode(404)
        .body("code", equalTo(1))
        .body("type", equalTo("error"))
        .body("message", equalTo("User not found"));

    log.info("Successfully received 404 for non-existent user: {}", nonExistentUsername);
  }
}