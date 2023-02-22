package de.example.accounts;

import de.example.BcryptPasswordMatcher;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.restassured.http.ContentType;
import org.hamcrest.CoreMatchers;
import org.jboss.resteasy.reactive.RestResponse;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.StringEndsWith.endsWith;

@QuarkusIntegrationTest
@QuarkusTestResource(H2DatabaseTestResource.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountResourceIT {

    @Test
    @Order(1)
    public void itHasInitiallyNoUsers() {
        given()
                .when().get("/account/users")
                .then()
                .statusCode(RestResponse.StatusCode.OK)
                .body("", is(empty()));
    }

    @Test
    @Order(2)
    public void itCreatesTheFirstUser() {
        given().contentType(ContentType.JSON)
                .body(aJSONAccount())
                .when().post("/account/users")
                .then().statusCode(RestResponse.StatusCode.CREATED)
                .header("Location", endsWith("1"));
    }

    @Test
    @Order(2)
    public void itCreatesTheSecondUser() {
        given().contentType(ContentType.JSON)
                .body(anotherJSONAccount())
                .when().post("/account/users")
                .then().statusCode(RestResponse.StatusCode.CREATED)
                .header("Location", endsWith("2"));
    }

    @Test
    @Order(3)
    public void itListsBothUsers() {
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

    private String aJSONAccount() {
        return "{" +
                "\"firstname\": \"Chuck\"," +
                "\"lastname\": \"Norris\"," +
                "\"email\": \"chuck@norris.com\"," +
                "\"birthday\": \"1940-03-10\"," +
                "\"password\": \"TopSecret\"" +
                "}";
    }

    private String anotherJSONAccount() {
        return "{" +
                "\"firstname\": \"Linda\"," +
                "\"lastname\": \"Hamilton\"," +
                "\"email\": \"linda@hamilton.com\"," +
                "\"birthday\": \"1956-09-26\"," +
                "\"password\": \"UltraViolet\"" +
                "}";
    }
}