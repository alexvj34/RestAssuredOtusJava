package api.utils;

import api.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class TestDataGenerator {

  private static final Logger log = LoggerFactory.getLogger(TestDataGenerator.class);

  private TestDataGenerator() {
  }

  public static PetDto generatePet() {
    Long petId = generateSafeId();

    CategoryDto category = CategoryDto.builder()
        .id(1L)
        .name("Dogs")
        .build();

    List<TagDto> tags = Arrays.asList(
        TagDto.builder().id(1L).name("friendly").build(),
        TagDto.builder().id(2L).name("trained").build()
    );

    List<String> photoUrls = Arrays.asList(
        "https://example.com/photo1.jpg",
        "https://example.com/photo2.jpg"
    );

    PetDto pet = PetDto.builder()
        .id(petId)
        .category(category)
        .name("Buddy_" + petId)
        .photoUrls(photoUrls)
        .tags(tags)
        .status(PetDto.Status.AVAILABLE)
        .build();

    log.debug("Generated test pet with ID: {}", petId);
    return pet;
  }

  public static OrderDto generateOrder(Long petId) {
    Long orderId = generateSafeId();

    OrderDto order = new OrderDto(
        orderId,
        petId,
        1,
        LocalDateTime.now().plusDays(1).format(java.time.format.DateTimeFormatter.ISO_DATE_TIME),
        OrderDto.Status.PLACED,
        false
    );

    log.debug("Generated test order with ID: {} for pet ID: {}", orderId, petId);
    return order;
  }

  public static UserDto generateUser() {
    String timestamp = String.valueOf(System.currentTimeMillis());
    Long userId = generateSafeId();

    UserDto user = UserDto.builder()
        .id(userId)
        .username("testuser_" + timestamp.substring(timestamp.length() - 6))
        .firstName("John")
        .lastName("Doe")
        .email("john.doe." + timestamp.substring(timestamp.length() - 6) + "@example.com")
        .password("password123")
        .phone("+1234567890")
        .userStatus(1)
        .build();

    log.debug("Generated test user with ID: {} and username: {}", userId, user.getUsername());
    return user;
  }

  public static Long generateSafeId() {
    Long id = (long) ThreadLocalRandom.current().nextInt(1000, 100000);
    log.trace("Generated safe ID: {}", id);
    return id;
  }
}