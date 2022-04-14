/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.NormalUserEntitySessionBeanLocal;
import entity.ActivityEntity;
import entity.CategoryEntity;
import entity.NormalUserEntity;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.exception.InvalidLoginCredentialException;
import ws.datamodel.CreateNormalUserReq;

/**
 * REST Web Service
 *
 * @author Jeremy
 */
@Path("NormalUser")
public class NormalUserResource {

    NormalUserEntitySessionBeanLocal normalUserEntitySessionBeanLocal = lookupNormalUserEntitySessionBeanLocal();

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of NormalUserResource
     */
    public NormalUserResource() {
    }

    @Path("normalUserLogin")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response normalUserLogin(@QueryParam("username") String username, @QueryParam("password") String password) {
        try {
            NormalUserEntity normalUserEntity = normalUserEntitySessionBeanLocal.normalUserLogin(username, password);
            System.out.println("********** NormalUserResource.normalUserLogin(): User " + normalUserEntity.getUsername() + " login remotely via web service");

//            normalUserEntity.setPassword(null);
//            normalUserEntity.setSalt(null);
            
            normalUserEntity.getActivitiesOwned().clear();
            normalUserEntity.getActivitiesParticipated().clear();
            normalUserEntity.getInterests().clear();

            return Response.status(Status.OK).entity(normalUserEntity).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("retrieveAllNormalUsers")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllNormalUsers() {
        try {
            List<NormalUserEntity> normalUserEntities = normalUserEntitySessionBeanLocal.retrieveAllNormalUser();

            for (NormalUserEntity user : normalUserEntities) {
                user.getActivitiesOwned().clear();
                user.getActivitiesParticipated().clear();
                user.getInterests().clear();
            }

            GenericEntity<List<NormalUserEntity>> genericEntity = new GenericEntity<List<NormalUserEntity>>(normalUserEntities) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    @Path("retrieveLeaderboard")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveLeaderboard() {
        try {
            System.out.println("ws.rest.NormalUserResource.retrieveLeaderboard()");
            List<NormalUserEntity> leaderboard = normalUserEntitySessionBeanLocal.retrieveLeaderboard();

            for (NormalUserEntity user : leaderboard) {
                user.getActivitiesOwned().clear();
                user.getActivitiesParticipated().clear();
                user.getInterests().clear();
            }

            GenericEntity<List<NormalUserEntity>> genericEntity = new GenericEntity<List<NormalUserEntity>>(leaderboard) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    @Path("retrieveNormalUsersByName")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveNormalUsersByName(@QueryParam("name") String name) {
        try {
            List<NormalUserEntity> normalUserEntities = normalUserEntitySessionBeanLocal.retrieveNormalUsersByName(name);

            for (NormalUserEntity user : normalUserEntities) {
                user.getActivitiesOwned().clear();
                user.getActivitiesParticipated().clear();
                user.getInterests().clear();
            }
            GenericEntity<List<NormalUserEntity>> genericEntity = new GenericEntity<List<NormalUserEntity>>(normalUserEntities) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("retrieveNormalUserByUsername")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveNormalUserByUsername(@QueryParam("username") String username) {
        try {
            NormalUserEntity normalUserEntity = normalUserEntitySessionBeanLocal.retrieveNormalUserByUsername(username);

            normalUserEntity.getActivitiesOwned().clear();
            normalUserEntity.getActivitiesParticipated().clear();
            normalUserEntity.getInterests().clear();

            GenericEntity<NormalUserEntity> genericEntity = new GenericEntity<NormalUserEntity>(normalUserEntity) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("retrieveNormalUserRank/{userId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveNormalUserRank(@PathParam("userId") Long normalUserId) {
        try {
            NormalUserEntity normalUserEntity = normalUserEntitySessionBeanLocal.retrieveNormalUserByUserId(normalUserId);
//            System.out.println(normalUserEntity.getUserId());
            Integer rank = normalUserEntitySessionBeanLocal.retrieveLeaderboardRank(normalUserEntity);
            GenericEntity<Integer> genericEntity = new GenericEntity<Integer>(rank) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("retrieveNormalUserById/{userId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveNormalUserById(@PathParam("userId") Long normalUserId) {
        try {
            NormalUserEntity normalUserEntity = normalUserEntitySessionBeanLocal.retrieveNormalUserByUserId(normalUserId);
            
            normalUserEntity.getActivitiesOwned().clear();
            normalUserEntity.getActivitiesParticipated().clear();
            normalUserEntity.getInterests().clear();
            
            GenericEntity<NormalUserEntity> genericEntity = new GenericEntity<NormalUserEntity>(normalUserEntity) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("createNewNormalUser")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewNormalUser(CreateNormalUserReq req) {
        System.out.println("ws.rest.NormalUserResource.createNewNormalUser()");
        System.out.println(req);
        if (req != null) {
            try {
                Long newNormalUserId = normalUserEntitySessionBeanLocal.createNewNormalUser(req.getNormalUser()).getUserId();

                return Response.status(Response.Status.OK).entity(newNormalUserId).build();
            } catch (Exception ex) {
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new record request").build();
        }
    }
    
    @Path("editNormalUser")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response editNormalUser(CreateNormalUserReq req) {
        System.out.println("ws.rest.NormalUserResource.editNormalUser()");
        System.out.println(req);
        if (req != null) {
            try {
                Long newNormalUserId = normalUserEntitySessionBeanLocal.editNormalUser(req.getNormalUser());

                return Response.status(Response.Status.OK).entity(newNormalUserId).build();
            } catch (Exception ex) {
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new record request").build();
        }
    }
    
    private NormalUserEntitySessionBeanLocal lookupNormalUserEntitySessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (NormalUserEntitySessionBeanLocal) c.lookup("java:global/joiNus/joiNus-ejb/NormalUserEntitySessionBean!ejb.session.stateless.NormalUserEntitySessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

}
