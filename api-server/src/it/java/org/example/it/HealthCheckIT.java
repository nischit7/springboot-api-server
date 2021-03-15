package org.example.it;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.RestAssured;
import io.restassured.config.SSLConfig;

import static org.hamcrest.Matchers.*;

/**
 * Health check related test cases.
 */
public class HealthCheckIT extends AbstractApiIT {

    @Test
    @DisplayName("When health check succeeds")
    public void healthCheckSucceeds() {

        RestAssured.given()
                .config(RestAssured.config().sslConfig(SSLConfig.sslConfig().relaxedHTTPSValidation()))
                    .baseUri(BASE_HEALTH_SERVER_URL)
                .accept("application/json")
                    .get(HEALTH_CHECK_URL)
                    .prettyPeek()
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("status", equalTo("UP"));
    }
}
