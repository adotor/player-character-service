package de.example.accounts;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.ws.rs.NotFoundException;
import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.StringEndsWith.endsWith;
import static org.jboss.resteasy.reactive.RestResponse.StatusCode;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;

@QuarkusTest
public class AccountResourceTest {

    @InjectMock
    AccountService accountService;

    @Test
    public void itListsNoUsers() {
        Mockito.when(accountService.listAll()).thenReturn(emptyList());

        given()
                .when().get("/account/users")
                .then()
                .statusCode(StatusCode.OK)
                .body("", is(empty()));

        Mockito.verify(accountService).listAll();
    }

    @Test
    public void itListsASingleUser() {
        Mockito.when(accountService.listAll()).thenReturn(singletonList(anAccount()));

        given()
                .when().get("/account/users")
                .then()
                .statusCode(StatusCode.OK)
                .body("", hasSize(1),
                        "[0].id", is(0),
                        "[0].firstname", is("Chuck"),
                        "[0].lastname", is("Norris"),
                        "[0].email", is("chuck@norris.com"),
                        "[0].birthday", is("1940-03-10"),
                        "[0].password", is("TopSecret"));

        Mockito.verify(accountService).listAll();
    }

    @Test
    public void itListsTwoUsers() {
        Mockito.when(accountService.listAll()).thenReturn(asList(anAccount(), anotherAccount()));

        given()
                .when().get("/account/users")
                .then()
                .statusCode(StatusCode.OK)
                .body("", hasSize(2),
                        "[0].id", is(0),
                        "[0].firstname", is("Chuck"),
                        "[0].lastname", is("Norris"),
                        "[0].email", is("chuck@norris.com"),
                        "[0].birthday", is("1940-03-10"),
                        "[0].password", is("TopSecret"),
                        "[1].id", is(1),
                        "[1].firstname", is("Linda"),
                        "[1].lastname", is("Hamilton"),
                        "[1].email", is("linda@hamilton.com"),
                        "[1].birthday", is("1956-09-26"),
                        "[1].password", is("UltraViolet"));

        Mockito.verify(accountService).listAll();
    }

    @Test
    public void itGetsAnExistingUser() {
        Mockito.when(accountService.get(0L)).thenReturn(anAccount());

        given()
                .when().get("/account/users/0")
                .then()
                .statusCode(StatusCode.OK)
                .body("id", is(0),
                        "firstname", is("Chuck"),
                        "lastname", is("Norris"),
                        "email", is("chuck@norris.com"),
                        "birthday", is("1940-03-10"),
                        "password", is("TopSecret"));

        Mockito.verify(accountService).get(0L);
    }

    @Test
    public void itReportsNotFoundForAMissingUser() {
        Mockito.when(accountService.get(123L)).thenThrow(new NotFoundException());

        given()
                .when().get("/account/users/123")
                .then()
                .statusCode(StatusCode.NOT_FOUND);

        Mockito.verify(accountService).get(123L);
    }

    @Test
    public void itUpdatesAnExistingUser() {
        Mockito.doNothing().when(accountService).update(eq(0L), isA(Account.class));

        given().contentType(ContentType.JSON)
                .body(aJSONAccount())
                .when().put("/account/users/0")
                .then()
                .statusCode(StatusCode.OK);

        Mockito.verify(accountService).update(eq(0L), isA(Account.class));
    }

    @Test
    public void itReportsNotFoundWhenUpdatingAMissingUser() {
        Mockito.doThrow(new NotFoundException()).when(accountService).update(eq(123L), isA(Account.class));

        given().contentType(ContentType.JSON)
                .body(aJSONAccount())
                .when().put("/account/users/123")
                .then()
                .statusCode(StatusCode.NOT_FOUND);

        Mockito.verify(accountService).update(eq(123L), isA(Account.class));
    }

    @Test
    public void itCreatesAUser() {
        given().contentType(ContentType.JSON)
                .body(aJSONAccount())
                .when().post("/account/users")
                .then().statusCode(StatusCode.CREATED)
                .header("Location", endsWith("0"));

        Mockito.verify(accountService).create("Chuck",
                "Norris",
                "chuck@norris.com",
                LocalDate.parse("1940-03-10"),
                "TopSecret");
    }

    private Account anAccount() {
        return anAccountWith(0L,
                "Chuck",
                "Norris",
                "chuck@norris.com",
                "1940-03-10",
                "TopSecret");
    }

    private Account anotherAccount() {
        return anAccountWith(1L,
                "Linda",
                "Hamilton",
                "linda@hamilton.com",
                "1956-09-26",
                "UltraViolet");
    }

    private Account anAccountWith(Long id, String firstname, String lastname, String email, String birthday, String password) {
        Account account = new Account();
        account.id = id;
        account.firstname = firstname;
        account.lastname = lastname;
        account.email = email;
        account.birthday = LocalDate.parse(birthday);
        account.password = password;
        return account;
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
}