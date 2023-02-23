package de.example.accounts;

import de.example.BcryptPasswordMatcher;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.restassured.http.ContentType;
import org.hamcrest.CoreMatchers;
import org.jboss.resteasy.reactive.RestResponse;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;
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
public class AccountResourceAPITestIT {

    @Test
    @Order(1)
    public void itListsNoUsers() {
        given()
                .when().get("/account/users")
                .then()
                .statusCode(RestResponse.StatusCode.OK)
                .body("", is(empty()));
    }

    @Test
    @Order(2)
    public void itCreatesAUser() {
        given().contentType(ContentType.JSON)
                .body(aJSONAccount())
                .when().post("/account/users")
                .then().statusCode(RestResponse.StatusCode.CREATED)
                .header("Location", endsWith("1"));
    }

    @Test
    @Order(3)
    public void itListsASingleUser() {
        given()
                .when().get("/account/users")
                .then()
                .statusCode(RestResponse.StatusCode.OK)
                .body("", hasSize(1),
                        "[0].id", is(1),
                        "[0].firstname", is("Chuck"),
                        "[0].lastname", is("Norris"),
                        "[0].email", is("chuck@norris.com"),
                        "[0].birthday", is("1940-03-10"),
                        "[0].password", CoreMatchers.is(BcryptPasswordMatcher.bcryptHashOf("TopSecret")));
    }

    @Test
    @Order(4)
    public void itGetsAnExistingUser() {
        given()
                .when().get("/account/users/1")
                .then()
                .statusCode(RestResponse.StatusCode.OK)
                .body("id", is(1),
                        "firstname", is("Chuck"),
                        "lastname", is("Norris"),
                        "email", is("chuck@norris.com"),
                        "birthday", is("1940-03-10"),
                        "password", CoreMatchers.is(BcryptPasswordMatcher.bcryptHashOf("TopSecret")));

    }

    @Test
    @Order(5)
    public void itUpdatesAnExistingUser() {
        given().contentType(ContentType.JSON)
                .body(anotherJSONAccount())
                .when().put("/account/users/1")
                .then()
                .statusCode(RestResponse.StatusCode.OK);
    }

    @Test
    @Order(6)
    public void itListsTwoUsers() {
        createAccount(aJSONAccount());

        given()
                .when().get("/account/users")
                .then()
                .statusCode(RestResponse.StatusCode.OK)
                .body("", hasSize(2),
                        "[0].id", is(1),
                        "[0].firstname", is("Linda"),
                        "[0].lastname", is("Hamilton"),
                        "[0].email", is("linda@hamilton.com"),
                        "[0].birthday", is("1956-09-26"),
                        "[0].password", CoreMatchers.is(BcryptPasswordMatcher.bcryptHashOf("UltraViolet")),
                        "[1].id", is(2),
                        "[1].firstname", is("Chuck"),
                        "[1].lastname", is("Norris"),
                        "[1].email", is("chuck@norris.com"),
                        "[1].birthday", is("1940-03-10"),
                        "[1].password", CoreMatchers.is(BcryptPasswordMatcher.bcryptHashOf("TopSecret")));
    }

    @Test
    @Order(7)
    public void itReportsNotFoundForAMissingUser() {
        given()
                .when().get("/account/users/123")
                .then()
                .statusCode(RestResponse.StatusCode.NOT_FOUND);
    }

    @Test
    @Order(8)
    public void itReportsNotFoundWhenUpdatingAMissingUser() {
        given().contentType(ContentType.JSON)
                .body(aJSONAccount())
                .when().put("/account/users/123")
                .then()
                .statusCode(RestResponse.StatusCode.NOT_FOUND);
    }

    @Test
    @Order(9)
    public void itRejectsNewUsersWithNonMatchingPasswords() {
        given().contentType(ContentType.JSON)
                .body(aJSONAccountWithNonMatchingPasswords())
                .when().post("/account/users")
                .then().statusCode(RestResponse.StatusCode.BAD_REQUEST);
    }

    private void createAccount(String json) {
        given().contentType(ContentType.JSON)
                .body(json)
                .when().post("/account/users")
                .then().statusCode(RestResponse.StatusCode.CREATED)
                .header("Location", is(notNullValue()));
    }

    private String aJSONAccount() {
        return "{" +
                "\"firstname\": \"Chuck\"," +
                "\"lastname\": \"Norris\"," +
                "\"email\": \"chuck@norris.com\"," +
                "\"birthday\": \"1940-03-10\"," +
                "\"password\": \"TopSecret\"," +
                "\"passwordRepetition\": \"TopSecret\"" +
                "}";
    }

    private String anotherJSONAccount() {
        return "{" +
                "\"firstname\": \"Linda\"," +
                "\"lastname\": \"Hamilton\"," +
                "\"email\": \"linda@hamilton.com\"," +
                "\"birthday\": \"1956-09-26\"," +
                "\"password\": \"UltraViolet\"," +
                "\"passwordRepetition\": \"UltraViolet\"" +
                "}";
    }

    private String aJSONAccountWithNonMatchingPasswords() {
        return "{" +
                "\"firstname\": \"Chuck\"," +
                "\"lastname\": \"Norris\"," +
                "\"email\": \"INVALID_MAIL\"," +
                "\"birthday\": \"1940-03-10\"," +
                "\"password\": \"TopSecret\"," +
                "\"passwordRepetition\": \"WrongPassword\"" +
                "}";
    }
}