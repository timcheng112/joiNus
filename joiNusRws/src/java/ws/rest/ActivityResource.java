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
import java.util.Date;
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
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

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
                activity.getBooking().getTimeSlot().getFacility();
//                activity.getBooking().setTimeSlot(null);
                activity.getBooking().getTimeSlot().getFacility().setTimeSlots(null);
                
                for (CommentEntity comment : activity.getComments()) {
                    comment.getCommentOwner();
                }

                for (ImageEntity image : activity.getGallery()) {
                    image.setPostedBy(null);
                }

            }

            GenericEntity<List<ActivityEntity>> genericEntity = new GenericEntity<List<ActivityEntity>>(activityEntities) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("retrieveMyActivities")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveMyActivities(Long userId) {
        try {
            List<ActivityEntity> activityEntities = activityEntitySessionBeanLocal.retrieveMyActivities(userId);

            for (ActivityEntity activity : activityEntities) {

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

                for (CommentEntity comment : activity.getComments()) {
                    comment.setCommentOwner(null);
                }

                for (ImageEntity image : activity.getGallery()) {
                    image.setPostedBy(null);
                }
                
                System.out.println(activityEntities);

            }

            GenericEntity<List<ActivityEntity>> genericEntity = new GenericEntity<List<ActivityEntity>>(activityEntities) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewActivity(ActivityEntity newActivity) {
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

//    @PUT
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response addComment(String comment, NormalUserEntity commentOwner, Date commentDate) {
//        CommentEntity newComment = new CommentEntity(comment, commentOwner, commentDate);
//        if (newComment != null) {
//            try {
//                Long newCommentId = activityEntitySessionBeanLocal.addComment(newComment);
//
//                return Response.status(Response.Status.OK).entity(newCommentId).build();
//            } catch (Exception ex) {
//                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
//            }
//        } else {
//            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid add new comment request").build();
//        }
//    }

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
