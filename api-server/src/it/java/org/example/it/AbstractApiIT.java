package org.example.it;

import org.example.app.ApiApplication;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Base class for all integration test cases.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        classes = ApiApplication.class,
        properties = "spring.config.location=classpath:myservice-api-it.properties")
public abstract class AbstractApiIT {

    public static final String CONTEXT_PATH = "/sample";
    public static final String MANAGE_CONTEXT = CONTEXT_PATH + "/manage";

    public static final String HEALTH_CHECK_URL = MANAGE_CONTEXT + "/health";

    public static final String BASE_HEALTH_SERVER_URL = "https://localhost:8443";
    public static final String BASE_SERVER_URL = "https://localhost:8443" + CONTEXT_PATH;
}
