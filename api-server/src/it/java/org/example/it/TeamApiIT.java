package org.example.it;

import java.time.Instant;
import java.util.Date;

import org.example.security.JwtHelper;
import org.example.util.JsonUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.restassured.RestAssured;
import io.restassured.config.SSLConfig;

import static org.hamcrest.Matchers.*;

/**
 * Integration tests for teams functionality.
 */
public class TeamApiIT extends AbstractApiIT {

    @Autowired
    private JwtHelper jwtHelper;

    @Test
    @DisplayName("When team creation succeeds")
    public void createTeamSucceeds() {
        createTeamSucceeds("teamCreateSuccess-it.json");
        deleteTeamSucceeds("myteamid");
    }

    @Test
    @DisplayName("When team creation fails for bad payload")
    public void createTeamFailsBadPayload() {
        RestAssured.given()
                .config(RestAssured.config().sslConfig(SSLConfig.sslConfig().relaxedHTTPSValidation()))
                .baseUri(BASE_SERVER_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, buildAuthHeader())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                    .body(JsonUtils.convertFileToJsonString("teamCreateFailsBadPayload-it.json"))
                .log()
                    .all(true)
                .post(ApiUrls.API_CONTEXT_PATH + ApiUrls.TEAMS_API_URI)
                    .prettyPeek()
                .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("When team creation fails for incorrect content type")
    public void createTeamFailsIncorrectContentType() {
        RestAssured.given()
                .config(RestAssured.config().sslConfig(SSLConfig.sslConfig().relaxedHTTPSValidation()))
                .baseUri(BASE_SERVER_URL)
                .header(HttpHeaders.AUTHORIZATION, buildAuthHeader())
                .contentType(MediaType.APPLICATION_RSS_XML_VALUE)
                .body(JsonUtils.convertFileToJsonString("teamCreateFailsBadPayload-it.json"))
                .log()
                    .all(true)
                .post(ApiUrls.API_CONTEXT_PATH + ApiUrls.TEAMS_API_URI)
                    .prettyPeek()
                .then()
                    .statusCode(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
    }

    @Test
    @DisplayName("When team retrieval succeeds")
    public void getTeamSucceeds() {
        createTeamSucceeds("teamGetSuccess-it.json");
        RestAssured.given()
                .config(RestAssured.config().sslConfig(SSLConfig.sslConfig().relaxedHTTPSValidation()))
                    .baseUri(BASE_SERVER_URL)
                .header(HttpHeaders.AUTHORIZATION, buildAuthHeader())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                    .log()
                    .all(true)
                .get(ApiUrls.API_CONTEXT_PATH + ApiUrls.TEAMS_API_URI + "/getteamid")
                    .prettyPeek()
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("teamId", equalTo("getteamid"));
        deleteTeamSucceeds("getteamid");
    }

    @Test
    @DisplayName("When a team is not found")
    public void getTeamFailsAsItIsNotFound() {
        RestAssured.given()
                .config(RestAssured.config().sslConfig(SSLConfig.sslConfig().relaxedHTTPSValidation()))
                .baseUri(BASE_SERVER_URL)
                .header(HttpHeaders.AUTHORIZATION, buildAuthHeader())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .log()
                    .all(true)
                .get(ApiUrls.API_CONTEXT_PATH + ApiUrls.TEAMS_API_URI + "/notFoundTeam")
                    .prettyPeek()
                .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("When a team retrieval fails as accept header is invalid")
    public void getTeamFailsAcceptHeaderInvalid() {
        RestAssured.given()
                .config(RestAssured.config().sslConfig(SSLConfig.sslConfig().relaxedHTTPSValidation()))
                .baseUri(BASE_SERVER_URL)
                .header(HttpHeaders.AUTHORIZATION, buildAuthHeader())
                .accept(MediaType.APPLICATION_PDF_VALUE)
                .log()
                .all(true)
                .get(ApiUrls.API_CONTEXT_PATH + ApiUrls.TEAMS_API_URI + "/invalidAcceptHeader")
                .prettyPeek()
                .then()
                .statusCode(HttpStatus.NOT_ACCEPTABLE.value());
    }

    public void createTeamSucceeds(final String payloadFileName) {
        RestAssured.given()
                .config(RestAssured.config().sslConfig(SSLConfig.sslConfig().relaxedHTTPSValidation()))
                .baseUri(BASE_SERVER_URL)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header(HttpHeaders.AUTHORIZATION, buildAuthHeader())
                    .body(JsonUtils.convertFileToJsonString(payloadFileName))
                    .log()
                        .all(true)
                .post(ApiUrls.API_CONTEXT_PATH + ApiUrls.TEAMS_API_URI)
                    .prettyPeek()
                .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());
    }

    public void deleteTeamSucceeds(final String teamId) {
        RestAssured.given()
                .config(RestAssured.config().sslConfig(SSLConfig.sslConfig().relaxedHTTPSValidation()))
                .baseUri(BASE_SERVER_URL)
                .header(HttpHeaders.AUTHORIZATION, buildAuthHeader())
                .log()
                    .all(true)
                .delete(ApiUrls.API_CONTEXT_PATH + ApiUrls.TEAMS_API_URI + "/" + teamId)
                    .prettyPeek()
                .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());
    }

    private String buildAuthHeader() {
        final String jwtToken = Jwts.builder()
                .setSubject("username")
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusMillis(14400000)))
                .signWith(SignatureAlgorithm.HS512, "12345678901234567890")
                .compact();

        return "Bearer " + jwtToken;
    }
}
