package de.example.pc;

import de.example.pc.validation.ValidLevel;
import org.jboss.resteasy.reactive.RestPath;
import org.jboss.resteasy.reactive.RestResponse;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;


@Path("/players/characters")
public class PlayerCharacterResource {
    @Inject
    PlayerCharacterRepository playerCharacterRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<PlayerCharacter> listUsers() {
        return playerCharacterRepository.listAll();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public PlayerCharacter listUsers(@RestPath Long id) {
        return playerCharacterRepository.get(id);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public RestResponse updateUser(@RestPath Long id,
                                   @Context UriInfo uriInfo,
                                   PlayerCharacter user) {
        playerCharacterRepository.update(id, user);

        return RestResponse.ResponseBuilder.ok().build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public RestResponse createUser(@Context UriInfo uriInfo,
                                   @Valid PlayerCharacterResource.NewCharacter newCharacter) {
        Long id = playerCharacterRepository.create(newCharacter.name,
                newCharacter.level,
                newCharacter.creationDate,
                newCharacter.pcClass
        );

        URI location = uriInfo.getAbsolutePathBuilder().path(id.toString()).build();

        return RestResponse.ResponseBuilder.created(location).build();
    }

    @ValidLevel
    public static class NewCharacter {
        @NotEmpty
        public String name;

        @Min(1)
        public Integer level;
        public LocalDate creationDate;
        public PCClass pcClass;
    }
}
