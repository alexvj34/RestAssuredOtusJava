package api;

import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseApiTest {

  protected final Logger log = LoggerFactory.getLogger(getClass());

  @BeforeAll
  static void checkApiAvailability() {
    Logger log = LoggerFactory.getLogger(BaseApiTest.class);
    log.info("Starting API tests execution");
  }
}