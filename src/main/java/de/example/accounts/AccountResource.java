package de.example.accounts;

import org.jboss.resteasy.reactive.RestPath;
import org.jboss.resteasy.reactive.RestResponse;

import javax.inject.Inject;
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
    AccountService accountService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Account> listUsers() {
        return accountService.listAll();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Account listUsers(@RestPath Long id) {
        return accountService.get(id);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public RestResponse updateUser(@RestPath Long id,
                                   @Context UriInfo uriInfo,
                                   Account user) {
        accountService.update(id, user);

        return RestResponse.ResponseBuilder.ok().build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public RestResponse createUser(@Context UriInfo uriInfo,
                                   NewAccount newAccount) {
        Long id = accountService.create(newAccount.firstname,
                newAccount.lastname,
                newAccount.email,
                newAccount.birthday,
                newAccount.password
        );

        URI location = uriInfo.getAbsolutePathBuilder().path(id.toString()).build();

        return RestResponse.ResponseBuilder.created(location).build();
    }

    private static class NewAccount {
        public String firstname;
        public String lastname;
        public String email;
        public LocalDate birthday;
        public String password;
    }
}
