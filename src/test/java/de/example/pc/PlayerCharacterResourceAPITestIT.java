package de.example.pc;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.restassured.http.ContentType;
import org.jboss.resteasy.reactive.RestResponse;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.StringEndsWith.endsWith;

@QuarkusIntegrationTest
@QuarkusTestResource(H2DatabaseTestResource.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PlayerCharacterResourceAPITestIT {

    @Test
    @Order(1)
    public void itListsNoCharacters() {
        given()
                .when().get("/players/characters")
                .then()
                .statusCode(RestResponse.StatusCode.OK)
                .body("", is(empty()));
    }

    @Test
    @Order(2)
    public void itCreatesACharacter() {
        given().contentType(ContentType.JSON)
                .body(aCharacter())
                .when().post("/players/characters")
                .then().statusCode(RestResponse.StatusCode.CREATED)
                .header("Location", endsWith("1"));
    }

    @Test
    @Order(3)
    public void itListsASingleCharacter() {
        given()
                .when().get("/players/characters")
                .then()
                .statusCode(RestResponse.StatusCode.OK)
                .body("", hasSize(1),
                        "[0].id", is(1),
                        "[0].name", is("Orgrim Doomhammer"),
                        "[0].level", is(42),
                        "[0].creationDate", is("2022-01-22"),
                        "[0].pcClass", is("FIGHTER"));
    }

    @Test
    @Order(4)
    public void itGetsAnExistingCharacter() {
        given()
                .when().get("/players/characters/1")
                .then()
                .statusCode(RestResponse.StatusCode.OK)
                .body("id", is(1),
                        "name", is("Orgrim Doomhammer"),
                        "level", is(42),
                        "creationDate", is("2022-01-22"),
                        "pcClass", is("FIGHTER"));

    }

    @Test
    @Order(5)
    public void itUpdatesAnExistingCharacter() {
        given().contentType(ContentType.JSON)
                .body(anotherCharacter())
                .when().put("/players/characters/1")
                .then()
                .statusCode(RestResponse.StatusCode.OK);
    }

    @Test
    @Order(6)
    public void itListsTwoCharacters() {
        createAccount(aCharacter());

        given()
                .when().get("/players/characters")
                .then()
                .statusCode(RestResponse.StatusCode.OK)
                .body("", hasSize(2),
                        "[0].id", is(1),
                        "[0].name", is("Jaina Proudmoore"),
                        "[0].level", is(42),
                        "[0].creationDate", is("2023-01-23"),
                        "[0].pcClass", is("WIZARD"),
                        "[1].id", is(2),
                        "[1].name", is("Orgrim Doomhammer"),
                        "[1].level", is(42),
                        "[1].creationDate", is("2022-01-22"),
                        "[1].pcClass", is("FIGHTER"));
    }

    @Test
    @Order(7)
    public void itReportsNotFoundForAMissingCharacter() {
        given()
                .when().get("/players/characters/123")
                .then()
                .statusCode(RestResponse.StatusCode.NOT_FOUND);
    }

    @Test
    @Order(8)
    public void itReportsNotFoundWhenUpdatingAMissingCharacter() {
        given().contentType(ContentType.JSON)
                .body(aCharacter())
                .when().put("/players/characters/123")
                .then()
                .statusCode(RestResponse.StatusCode.NOT_FOUND);
    }

    @Test
    @Order(9)
    public void itRejectsANewCharacterWithLevelTooHigh() {
        given().contentType(ContentType.JSON)
                .body(aCharacterWithLevelToHigh())
                .when().post("/players/characters")
                .then().statusCode(RestResponse.StatusCode.BAD_REQUEST);
    }

    @Test
    @Order(10)
    public void itRejectsANewCharacterWithEmptyName() {
        given().contentType(ContentType.JSON)
                .body(aCharacterWithNoName())
                .when().post("/players/characters")
                .then().statusCode(RestResponse.StatusCode.BAD_REQUEST);
    }

    @Test
    @Order(11)
    public void itRejectsANewCharacterWithLevelZero() {
        given().contentType(ContentType.JSON)
                .body(aCharacterWithLevelZero())
                .when().post("/players/characters")
                .then().statusCode(RestResponse.StatusCode.BAD_REQUEST);
    }
    private void createAccount(String json) {
        given().contentType(ContentType.JSON)
                .body(json)
                .when().post("/players/characters")
                .then().statusCode(RestResponse.StatusCode.CREATED)
                .header("Location", is(notNullValue()));
    }

    private String aCharacter() {
        return "{" +
                "\"name\": \"Orgrim Doomhammer\"," +
                "\"level\": 42," +
                "\"creationDate\": \"2022-01-22\"," +
                "\"pcClass\": \"FIGHTER\"" +
                "}";
    }

    private String anotherCharacter() {
        return "{" +
                "\"name\": \"Jaina Proudmoore\"," +
                "\"level\": 42," +
                "\"creationDate\": \"2023-01-23\"," +
                "\"pcClass\": \"WIZARD\"" +
                "}";
    }

    private String aCharacterWithLevelToHigh() {
        return "{" +
                "\"name\": \"Sylvanas Windrunner\"," +
                "\"level\": 200," +
                "\"creationDate\": \"2023-03-03\"," +
                "\"pcClass\": \"WIZARD\"" +
                "}";
    }

    private String aCharacterWithNoName() {
        return "{" +
                "\"level\": 1," +
                "\"creationDate\": \"2023-03-03\"," +
                "\"pcClass\": \"CLERIC\"" +
                "}";
    }

    private String aCharacterWithLevelZero() {
        return "{" +
                "\"name\": \"Tiny Fin\"," +
                "\"level\": 0," +
                "\"creationDate\": \"2023-04-04\"," +
                "\"pcClass\": \"FIGHTER\"" +
                "}";
    }
}