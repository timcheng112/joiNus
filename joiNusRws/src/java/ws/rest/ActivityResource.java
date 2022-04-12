/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.ActivityEntitySessionBeanLocal;
import ejb.session.stateless.NormalUserEntitySessionBeanLocal;
import entity.ActivityEntity;
import entity.CommentEntity;
import entity.ImageEntity;
import entity.NormalUserEntity;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.exception.ActivityNotFoundException;
import util.exception.CategoryNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InsufficientBookingTokensException;
import util.exception.InvalidLoginCredentialException;
import util.exception.MaxParticipantsExceededException;
import util.exception.NormalUserAlreadySignedUpException;
import util.exception.NormalUserNotFoundException;
import util.exception.TimeSlotNotFoundException;
import ws.datamodel.AddCommentReq;
import ws.datamodel.CreateActivityReq;
import ws.datamodel.SignUpForActivityReq;
import ws.datamodel.PunishReq;

/**
 * REST Web Service
 *
 * @author User
 */
@Path("Activity")
public class ActivityResource {

    NormalUserEntitySessionBeanLocal normalUserEntitySessionBeanLocal = lookupNormalUserEntitySessionBeanLocal();

    ActivityEntitySessionBeanLocal activityEntitySessionBeanLocal = lookupActivityEntitySessionBeanLocal();

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of ActivityResource
     */
    public ActivityResource() {
    }

    @Path("retrieveAllActivities")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllActivities() {

        try {
            List<ActivityEntity> activityEntities = activityEntitySessionBeanLocal.retrieveAllActivities();

            for (ActivityEntity activity : activityEntities) {
                System.out.println(activity.getActivityId());
                System.out.println(activity.getActivityName());
                System.out.println(activity.getActivityDescription());

                activity.getActivityOwner().setInterests(null);
                activity.getActivityOwner().setActivitiesParticipated(null);
                activity.getActivityOwner().setActivitiesOwned(null);

                for (NormalUserEntity participant : activity.getParticipants()) {
                    participant.setInterests(null);
                    participant.setActivitiesParticipated(null);
                    participant.setActivitiesOwned(null);
                }

                activity.getCategory().setSubCategories(null);
                activity.getCategory().setParentCategory(null);
                activity.getCategory().setActivities(null);

                if (activity.getBooking() != null) {
                    activity.getBooking().setActivity(null);
                    if (activity.getBooking().getTimeSlot() != null) {
                        activity.getBooking().getTimeSlot().setBooking(null);
                        activity.getBooking().getTimeSlot().getFacility().getTimeSlots().clear();
                    }
                }

//                for (CommentEntity comment : activity.getComments()) {
//                    comment.setCommentOwner(null);
//                }
                for (ImageEntity image : activity.getGallery()) {
                    image.setPostedBy(null);
                }

            }

            GenericEntity<List<ActivityEntity>> genericEntity = new GenericEntity<List<ActivityEntity>>(activityEntities) {
            };

            System.out.println(genericEntity.getEntity());
            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("retrieveActivity/{activityId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveActivity(@QueryParam("username") String username,
            @QueryParam("password") String password,
            @PathParam("activityId") Long activityId) {
        try {
            NormalUserEntity normalUserEntity = normalUserEntitySessionBeanLocal.normalUserLogin(username, password);
            System.out.println("********** ActivityResource.retrieveProduct(): NormalUser " + normalUserEntity.getUsername() + " login remotely via web service");

            ActivityEntity activityEntity = activityEntitySessionBeanLocal.retrieveActivityByActivityId(activityId);

            activityEntity.getActivityOwner().getInterests().clear();
            activityEntity.getActivityOwner().getActivitiesParticipated().clear();
            activityEntity.getActivityOwner().getActivitiesOwned().clear();

            for (NormalUserEntity participant : activityEntity.getParticipants()) {
                participant.getInterests().clear();
                participant.getActivitiesParticipated().clear();
                participant.getActivitiesOwned().clear();
            }

            activityEntity.getCategory().getSubCategories().clear();
            activityEntity.getCategory().setParentCategory(null);
            activityEntity.getCategory().getActivities().clear();

            if (activityEntity.getBooking() != null) {
                activityEntity.getBooking().setActivity(null);
                if (activityEntity.getBooking().getTimeSlot() != null) {
                    activityEntity.getBooking().getTimeSlot().setBooking(null);
                    activityEntity.getBooking().getTimeSlot().getFacility().getTimeSlots().clear();
                }
            }

            for (ImageEntity image : activityEntity.getGallery()) {
                image.setPostedBy(null);
            }

            return Response.status(Status.OK).entity(activityEntity).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (ActivityNotFoundException ex) {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("createNewActivity")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewActivity(CreateActivityReq createActivityReq) {
        if (createActivityReq != null) {
            try {
                NormalUserEntity normalUserEntity = normalUserEntitySessionBeanLocal.normalUserLogin(createActivityReq.getUsername(), createActivityReq.getPassword());
                System.out.println("********** ActivityResource.createActivity(): NormalUser " + normalUserEntity.getUsername() + " login remotely via web service");

                ActivityEntity activityEntity = new ActivityEntity();
                activityEntity.setActivityName(createActivityReq.getActivityName());
                activityEntity.setActivityDescription(createActivityReq.getActivityDescription());
                activityEntity.setMaxParticipants(createActivityReq.getActivityMaxParticipants());
                activityEntity.setTags(createActivityReq.getTags());
                activityEntity.setActivityOwner(normalUserEntity);

                activityEntity = activityEntitySessionBeanLocal.createNewActivity(activityEntity, createActivityReq.getCategoryId(), createActivityReq.getTimeSlotId());

                return Response.status(Response.Status.OK).entity(activityEntity.getActivityId()).build();
            } catch (CategoryNotFoundException | InputDataValidationException | TimeSlotNotFoundException ex) {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            } catch (InvalidLoginCredentialException ex) {
                return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
            } catch (Exception ex) {
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new activity request").build();
        }
    }

    @Path("addComment")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addComment(AddCommentReq addCommentReq) {
        if (addCommentReq != null) {
            try {
                NormalUserEntity normalUserEntity = normalUserEntitySessionBeanLocal.normalUserLogin(addCommentReq.getUsername(), addCommentReq.getPassword());
                System.out.println("********** ActivityResource.addComment(): NormalUser " + normalUserEntity.getUsername() + " login remotely via web service");

                CommentEntity commentEntity = new CommentEntity();
                commentEntity.setText(addCommentReq.getText());
                commentEntity.setCommentOwner(normalUserEntity);
                System.out.println(addCommentReq.getText());

                Long commentEntityId = activityEntitySessionBeanLocal.addComment(commentEntity, addCommentReq.getActivityId());

                return Response.status(Response.Status.OK).entity(commentEntityId).build();
            } catch (ActivityNotFoundException ex) {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            } catch (InvalidLoginCredentialException ex) {
                return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
            } catch (Exception ex) {
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new activity request").build();
        }
    }

    @Path("punishUsers")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response punishUsers(PunishReq punishReq
    ) {
        System.out.println("ws.rest.ActivityResource.punishUsers()");
        System.out.println("ActivityId: " + punishReq.getActivityId());
        System.out.println("absenteeIds: " + punishReq.getAbsenteeIds().toString());
        if (punishReq.getActivityId() == null) {
            JsonObjectBuilder obj = Json.createObjectBuilder().add("message", "activityID is empty, can't punish");

            return Response.status(Response.Status.BAD_REQUEST).entity(obj).build();
        } else if (punishReq.getAbsenteeIds().isEmpty()) {
            JsonObjectBuilder obj = Json.createObjectBuilder().add("message", "absenteeIds is empty, no one to punish");
            return Response.status(Response.Status.BAD_REQUEST).entity(obj).build();
        } else {
            activityEntitySessionBeanLocal.absentPunishment(punishReq.getActivityId(), punishReq.getAbsenteeIds());
            JsonObjectBuilder obj = Json.createObjectBuilder().add("message", "Punishment Dealt");

            return Response.status(Response.Status.OK).entity(obj).build();

        }
    }

    @Path("signUpForActivity")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signUpForActivity(SignUpForActivityReq signUpForActivityReq) {
        try {
            NormalUserEntity normalUserEntity = normalUserEntitySessionBeanLocal.normalUserLogin(signUpForActivityReq.getUsername(), signUpForActivityReq.getPassword());
            System.out.println("********** ActivityResource.addComment(): NormalUser " + normalUserEntity.getUsername() + " login remotely via web service");

            System.out.println("ws.rest.ActivityResource.signUpForActivity()");
            System.out.println("activityId is " + signUpForActivityReq.getActivityId());
            if (signUpForActivityReq.getActivityId() == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity("activityID is empty, can't add").build();
            } else if (normalUserEntity.getUserId() == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity("userId is empty, no one to add").build();
            } else {
                try {
                    activityEntitySessionBeanLocal.signUpForActivity(signUpForActivityReq.getActivityId(), normalUserEntity.getUserId());
                    JsonObjectBuilder obj = Json.createObjectBuilder().add("msg", "signed up");
                    return Response.status(Response.Status.OK).entity(obj).build();
                } catch (NormalUserNotFoundException ex) {
                    JsonObjectBuilder obj = Json.createObjectBuilder().add("msg", "user can't be found");
                    return Response.status(Response.Status.NOT_FOUND).entity("user can't be found").build();
                } catch (NormalUserAlreadySignedUpException ex) {
                    JsonObjectBuilder obj = Json.createObjectBuilder().add("msg", "user already signed up");
                    return Response.status(Response.Status.BAD_REQUEST).entity("user already signed up").build();
                } catch (MaxParticipantsExceededException ex) {
                    JsonObjectBuilder obj = Json.createObjectBuilder().add("msg", "no space for anymore participants");
                    return Response.status(Response.Status.BAD_REQUEST).entity("user already signed up").build();
                }catch (InsufficientBookingTokensException ex) {
                    JsonObjectBuilder obj = Json.createObjectBuilder().add("msg", "insufficient tokens");
                    return Response.status(Response.Status.BAD_REQUEST).entity("insufficient tokens!").build();
                }
            }
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid Login credentials!").build();
        }
    }

    private ActivityEntitySessionBeanLocal lookupActivityEntitySessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (ActivityEntitySessionBeanLocal) c.lookup("java:global/joiNus/joiNus-ejb/ActivityEntitySessionBean!ejb.session.stateless.ActivityEntitySessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
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
