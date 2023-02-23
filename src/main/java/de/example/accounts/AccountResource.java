package de.example.accounts;

import de.example.accounts.validation.EqualPasswords;
import org.jboss.resteasy.reactive.RestPath;
import org.jboss.resteasy.reactive.RestResponse;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;


@Path("/account/users")
public class AccountResource {
    @Inject
    AccountRepository accountRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Account> listUsers() {
        return accountRepository.listAll();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Account listUsers(@RestPath Long id) {
        return accountRepository.get(id);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public RestResponse updateUser(@RestPath Long id,
                                   @Context UriInfo uriInfo,
                                   Account user) {
        accountRepository.update(id, user);

        return RestResponse.ResponseBuilder.ok().build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public RestResponse createUser(@Context UriInfo uriInfo,
                                   @Valid NewAccount newAccount) {
        Long id = accountRepository.create(newAccount.firstname,
                newAccount.lastname,
                newAccount.email,
                newAccount.birthday,
                newAccount.password
        );

        URI location = uriInfo.getAbsolutePathBuilder().path(id.toString()).build();

        return RestResponse.ResponseBuilder.created(location).build();
    }

    @EqualPasswords
    public static class NewAccount {
        public String firstname;
        public String lastname;
        public String email;
        public LocalDate birthday;
        public String password;
        public String passwordRepetition;
    }
}
