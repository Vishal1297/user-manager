package org.fretron.user.manager.resource

import com.fasterxml.jackson.databind.ObjectMapper
import org.fretron.user.manager.model.User
import org.fretron.user.manager.service.UserServiceImpl
import javax.inject.Inject
import javax.inject.Named
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/user/v1")
class UserResource @Inject constructor(
    @Named("objectMapper") private val objectMapper: ObjectMapper,
    @Named("userService") private val userService: UserServiceImpl
) {

    @GET
    @Path("/status")
    @Produces(MediaType.TEXT_PLAIN)
    fun status(): Response {
        return Response.status(Response.Status.ACCEPTED).entity("User API Running...").build()
    }


    @POST
    @Path("/user")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    fun addUser(user: String): Response {
        val person = objectMapper.readValue(user, User::class.java)
        return if (userService.addUser(person)) Response.ok("User Added!!").build()
        else Response.status(Response.Status.INTERNAL_SERVER_ERROR).build()
    }

    @GET
    @Path("/user")
    @Produces(MediaType.APPLICATION_JSON)
    fun getUser(@QueryParam("id") id: String): Response {
        val person = userService.getUser(id) ?: return Response.status(Response.Status.NOT_FOUND).build()
        return Response.ok(person.toString()).build()
    }

    @GET
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    fun getAllUsers(): Response {
        val personsList = userService.getAllUsers()
        return Response.ok(personsList.toString()).build()
    }

    @PUT
    @Path("/user")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    fun updateUser(@QueryParam("id") id: String, user: String): Response {
        val person = objectMapper.readValue(user, User::class.java)
        return if (person != null) {
            if (userService.updateUser(id, person)) Response.ok("User Updated!!").build()
            else Response.status(Response.Status.INTERNAL_SERVER_ERROR).build()
        } else {
            Response.status(Response.Status.NOT_MODIFIED).build()
        }
    }

    @DELETE
    @Path("/user")
    @Produces(MediaType.TEXT_PLAIN)
    fun deleteUser(@QueryParam("id") id: String): Response {
        return if (userService.deleteUser(id)) Response.ok("User Deleted!!").build()
        else Response.status(Response.Status.NOT_MODIFIED).build()
    }


}