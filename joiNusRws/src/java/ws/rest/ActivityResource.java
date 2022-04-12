/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.ActivityEntitySessionBeanLocal;
import entity.ActivityEntity;
import entity.CommentEntity;
import entity.ImageEntity;
import entity.NormalUserEntity;
import entity.TimeSlotEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.exception.NormalUserAlreadySignedUpException;
import util.exception.NormalUserNotFoundException;

/**
 * REST Web Service
 *
 * @author User
 */
@Path("Activity")
public class ActivityResource {

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

                activity.getBooking().setActivity(null);

                if (activity.getBooking().getTimeSlot() != null) {
                    activity.getBooking().getTimeSlot().setBooking(null);
                    activity.getBooking().getTimeSlot().getFacility().getTimeSlots().clear();
                }

                for (CommentEntity comment : activity.getComments()) {
                    comment.setCommentOwner(null);
                }

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

    @Path("createNewActivity")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewActivity(ActivityEntity newActivity
    ) {
        if (newActivity != null) {
            try {
                Long newActivityId = activityEntitySessionBeanLocal.createNewActivity(newActivity).getActivityId();
                
                return Response.status(Response.Status.OK).entity(newActivityId).build();
            } catch (Exception ex) {
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new record request").build();
        }
    }

    @Path("punishUsers")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response punishUsers(@QueryParam("activityId") Long activityId,
            @QueryParam("absenteeIds") List<Long> absenteeIds
    ) {
        System.out.println("ws.rest.ActivityResource.punishUsers()");
        System.out.println("ActivityId: " + activityId);
        System.out.println("absenteeIds: " + absenteeIds.toString());
        if (activityId == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("activityID is empty, can't punish").build();
        } else if (absenteeIds.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("absenteeIds is empty, no one to punish").build();
        } else {
            activityEntitySessionBeanLocal.absentPunishment(activityId, absenteeIds);
            return Response.status(Response.Status.OK).entity("Punishment done").build();
        }
    }

    @Path("signUpForActivity")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signUpForActivity(@QueryParam("activityId") Long activityId, @QueryParam("userId") Long userId) {
        System.out.println("ws.rest.ActivityResource.signUpForActivity()");
        System.out.println("activityId is " + activityId);
        if (activityId == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("activityID is empty, can't add").build();
        } else if (userId == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("userId is empty, no one to add").build();
        } else {
            try {
                activityEntitySessionBeanLocal.signUpForActivity(activityId, userId);
                return Response.status(Response.Status.OK).entity("signed up").build();
            } catch (NormalUserNotFoundException ex) {
                return Response.status(Response.Status.NOT_FOUND).entity("user can't be found").build();
            } catch (NormalUserAlreadySignedUpException ex) {
                return Response.status(Response.Status.BAD_REQUEST).entity("user already signed up").build();
            }
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
}
