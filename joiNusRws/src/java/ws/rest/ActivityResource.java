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
import java.util.Date;
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
import ws.datamodel.CreateGalleryPostReq;
import ws.datamodel.CreateNewNoFacilityActivityReq;
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

                if (activity.getCategory() != null) {
                    activity.getCategory().setSubCategories(null);
                    if (activity.getCategory().getParentCategory() != null) {
                        if (activity.getCategory().getParentCategory().getSubCategories() != null) {
                            activity.getCategory().getParentCategory().getSubCategories().clear();
                        }
                        activity.getCategory().getParentCategory().setParentCategory(null);
                        activity.getCategory().getParentCategory().setActivities(null);
                    }
                    activity.getCategory().setActivities(null);
                }

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

    @Path("retrieveAllActivitiesIP/{userId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllActivitiesIP(@PathParam("userId") Long userId) {
        System.out.println("ws.rest.ActivityResource.retrieveAllActivitiesIP()");
        try {
            List<ActivityEntity> activityEntities = activityEntitySessionBeanLocal.retrieveAllActivitiesIP(userId);

            for (ActivityEntity activity : activityEntities) {
                System.out.println(activity.getActivityId());
                System.out.println(activity.getActivityName());
                System.out.println(activity.getActivityDescription());

                System.out.println("test a");
                activity.getActivityOwner().setInterests(null);
                activity.getActivityOwner().setActivitiesParticipated(null);
                activity.getActivityOwner().setActivitiesOwned(null);

                System.out.println("test b");
                if (activity.getParticipants() != null) {
                    for (NormalUserEntity participant : activity.getParticipants()) {
                        participant.setInterests(null);
                        participant.setActivitiesParticipated(null);
                        participant.setActivitiesOwned(null);
                    }
                }
                System.out.println("test c");

                if (activity.getCategory() != null) {
                    activity.getCategory().setSubCategories(null);
                    if (activity.getCategory().getParentCategory() != null) {
                        if (activity.getCategory().getParentCategory().getSubCategories() != null) {
                            activity.getCategory().getParentCategory().getSubCategories().clear();
                        }
                        activity.getCategory().getParentCategory().setParentCategory(null);
                        activity.getCategory().getParentCategory().setActivities(null);
                    }
                    activity.getCategory().setActivities(null);
                }

                System.out.println("test d");

                if (activity.getBooking() != null) {
                    activity.getBooking().setActivity(null);
                    if (activity.getBooking().getTimeSlot() != null) {
                        activity.getBooking().getTimeSlot().setBooking(null);
                        activity.getBooking().getTimeSlot().getFacility().getTimeSlots().clear();
                    }
                }
                System.out.println("test e");

//                for (CommentEntity comment : activity.getComments()) {
//                    comment.setCommentOwner(null);
//                }
                if (activity.getGallery() != null) {
                    for (ImageEntity image : activity.getGallery()) {
                        image.setPostedBy(null);
                    }
                }
                System.out.println("test f");
            }
            System.out.println("reached end of for loop");
            
            System.out.println("######################");
            for (ActivityEntity ac : activityEntities) {
                System.out.println(ac.getActivityName());
            }
            GenericEntity<List<ActivityEntity>> genericEntity = new GenericEntity<List<ActivityEntity>>(activityEntities) {
            };
            System.out.println(genericEntity.getEntity());
            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            System.out.println("CAUGHT ERROR HERE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
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
            System.out.println("Activity ID: " + activityId);

            ActivityEntity activity = activityEntitySessionBeanLocal.retrieveActivityByActivityId(activityId);

            System.out.println("Activity Entity: " + activity);
            System.out.println("test a");
            activity.getActivityOwner().setInterests(null);
            activity.getActivityOwner().setActivitiesParticipated(null);
            activity.getActivityOwner().setActivitiesOwned(null);

            System.out.println("test b");
            if (activity.getParticipants() != null) {
                for (NormalUserEntity participant : activity.getParticipants()) {
                    participant.setInterests(null);
                    participant.setActivitiesParticipated(null);
                    participant.setActivitiesOwned(null);
                }
            }
            System.out.println("test c");

            if (activity.getCategory() != null) {
                activity.getCategory().setSubCategories(null);
                if (activity.getCategory().getParentCategory() != null) {
                    if (activity.getCategory().getParentCategory().getSubCategories() != null) {
                        activity.getCategory().getParentCategory().getSubCategories().clear();
                    }
                    activity.getCategory().getParentCategory().setParentCategory(null);
                    activity.getCategory().getParentCategory().setActivities(null);
                }
                activity.getCategory().setActivities(null);
            }

            System.out.println("test d");

            if (activity.getBooking() != null) {
                activity.getBooking().setActivity(null);
                if (activity.getBooking().getTimeSlot() != null) {
                    activity.getBooking().getTimeSlot().setBooking(null);
                    activity.getBooking().getTimeSlot().getFacility().getTimeSlots().clear();
                }
            }
            System.out.println("test e");

//            for (CommentEntity comment : activity.getComments()) {
//                comment.setCommentOwner(null);
//            }
            if (activity.getGallery() != null) {
                for (ImageEntity image : activity.getGallery()) {
                    image.getPostedBy();
                }
            }

            System.out.println("5");
            System.out.println(activity);
            return Response.status(Status.OK).entity(activity).build();
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
                activityEntity.setTags(createActivityReq.getActivityTags());
                activityEntity.setActivityOwner(normalUserEntity);

                activityEntity = activityEntitySessionBeanLocal.createNewActivity(activityEntity, createActivityReq.getCategoryId(), createActivityReq.getTimeSlotId(), null);
                
                System.out.println("END OF METHOD ws.rest.ActivityResource.createNewActivity()");
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

    @Path("createNewNoFacilityActivity")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewNoFacilityActivity(CreateNewNoFacilityActivityReq createNewNoFacilityActivityReq) {
        if (createNewNoFacilityActivityReq != null) {
            try {
                NormalUserEntity normalUserEntity = normalUserEntitySessionBeanLocal.normalUserLogin(createNewNoFacilityActivityReq.getUsername(), createNewNoFacilityActivityReq.getPassword());
                System.out.println("********** ActivityResource.createNoFacilityActivity(): NormalUser " + normalUserEntity.getUsername() + " login remotely via web service");
                ActivityEntity activityEntity = new ActivityEntity();
                activityEntity.setActivityName(createNewNoFacilityActivityReq.getActivityName());
                activityEntity.setActivityDescription(createNewNoFacilityActivityReq.getActivityDescription());
                activityEntity.setMaxParticipants(createNewNoFacilityActivityReq.getActivityMaxParticipants());
                if (createNewNoFacilityActivityReq.getActivityTags() != null) {
                    System.out.println("Tags is Not Null");
                } else {
                    System.out.println("Empty");
                }
                activityEntity.setTags(createNewNoFacilityActivityReq.getActivityTags());
                activityEntity.setActivityOwner(normalUserEntity);

                Date activityDate = new Date(createNewNoFacilityActivityReq.getActivityYear() - 1900, createNewNoFacilityActivityReq.getActivityMonth() - 1, createNewNoFacilityActivityReq.getActivityDay(), createNewNoFacilityActivityReq.getActivityHour(), createNewNoFacilityActivityReq.getActivityMinute());
                System.out.println("test");
                activityEntity = activityEntitySessionBeanLocal.createNewActivity(activityEntity, createNewNoFacilityActivityReq.getCategoryId(), null, activityDate);

                System.out.println("END OF METHOD ws.rest.ActivityResource.createNewNoFacilityActivity()");
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

    @Path("createGalleryPost")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createGalleryPost(CreateGalleryPostReq createGalleryPostReq) {
        if (createGalleryPostReq != null) {
            try {
                NormalUserEntity normalUserEntity = normalUserEntitySessionBeanLocal.normalUserLogin(createGalleryPostReq.getUsername(), createGalleryPostReq.getPassword());
                System.out.println("********** ActivityResource.createGalleryPost(): NormalUser " + normalUserEntity.getUsername() + " login remotely via web service");
                ImageEntity imageEntity = new ImageEntity(createGalleryPostReq.getImagePath(), createGalleryPostReq.getImageDescription(), createGalleryPostReq.getDatePosted(), normalUserEntity);
                Long imageEntityId = activityEntitySessionBeanLocal.createImage(imageEntity, createGalleryPostReq.getActivityId());

                return Response.status(Response.Status.OK).entity(imageEntityId).build();
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
                ActivityEntity activity = activityEntitySessionBeanLocal.retrieveActivityByActivityId(addCommentReq.getActivityId());
//                    return Response.status(Response.Status.OK).entity(obj).build();

                //dealing w cyclic reference issue
                activity.getActivityOwner().setInterests(null);
                activity.getActivityOwner().setActivitiesParticipated(null);
                activity.getActivityOwner().setActivitiesOwned(null);

                if (activity.getParticipants() != null) {
                    for (NormalUserEntity participant : activity.getParticipants()) {
                        participant.setInterests(null);
                        participant.setActivitiesParticipated(null);
                        participant.setActivitiesOwned(null);
                    }
                }

                if (activity.getCategory() != null) {
                    activity.getCategory().setSubCategories(null);
                    if (activity.getCategory().getParentCategory() != null) {
                        if (activity.getCategory().getParentCategory().getSubCategories() != null) {
                            activity.getCategory().getParentCategory().getSubCategories().clear();
                        }
                    }
                    activity.getCategory().setActivities(null);
                }

                if (activity.getBooking() != null) {
                    activity.getBooking().setActivity(null);
                    if (activity.getBooking().getTimeSlot() != null) {
                        activity.getBooking().getTimeSlot().setBooking(null);
                        activity.getBooking().getTimeSlot().getFacility().getTimeSlots().clear();
                    }
                }

                if (activity.getComments() != null) {
                    for (CommentEntity comment : activity.getComments()) {
                        comment.setCommentOwner(null);
                    }
                }

                if (activity.getGallery() != null) {
                    for (ImageEntity image : activity.getGallery()) {
                        image.setPostedBy(null);
                    }
                }
                GenericEntity<ActivityEntity> genericEntity = new GenericEntity<ActivityEntity>(activity) {
                };

                System.out.println(genericEntity.getEntity());
                return Response.status(Status.OK).entity(commentEntityId).build();
//                return Response.status(Response.Status.OK).entity(commentEntityId).build();
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
//                    return Response.status(Response.Status.OK).entity(activityEntity.getActivityId()).build();
                    ActivityEntity activity = activityEntitySessionBeanLocal.retrieveActivityByActivityId(signUpForActivityReq.getActivityId());
//                    return Response.status(Response.Status.OK).entity(obj).build();

                    //dealing w cyclic reference issue
                    activity.getActivityOwner().setInterests(null);
                    activity.getActivityOwner().setActivitiesParticipated(null);
                    activity.getActivityOwner().setActivitiesOwned(null);

                    if (activity.getParticipants() != null) {
                        for (NormalUserEntity participant : activity.getParticipants()) {
                            participant.setInterests(null);
                            participant.setActivitiesParticipated(null);
                            participant.setActivitiesOwned(null);
                        }
                    }

                    if (activity.getCategory() != null) {
                        activity.getCategory().setSubCategories(null);
                        if (activity.getCategory().getParentCategory() != null) {
                            if (activity.getCategory().getParentCategory().getSubCategories() != null) {
                                activity.getCategory().getParentCategory().getSubCategories().clear();
                            }
                        }
                        activity.getCategory().setActivities(null);
                    }

                    if (activity.getBooking() != null) {
                        activity.getBooking().setActivity(null);
                        if (activity.getBooking().getTimeSlot() != null) {
                            activity.getBooking().getTimeSlot().setBooking(null);
                            activity.getBooking().getTimeSlot().getFacility().getTimeSlots().clear();
                        }
                    }

                    if (activity.getGallery() != null) {
                        for (ImageEntity image : activity.getGallery()) {
                            image.setPostedBy(null);
                        }
                    }
                    GenericEntity<ActivityEntity> genericEntity = new GenericEntity<ActivityEntity>(activity) {
                    };

                    System.out.println(genericEntity.getEntity());
                    return Response.status(Status.OK).entity(genericEntity).build();
                } catch (NormalUserNotFoundException | ActivityNotFoundException ex) {
                    JsonObjectBuilder obj = Json.createObjectBuilder().add("msg", "user can't be found");
                    return Response.status(Response.Status.NOT_FOUND).entity(obj).build();
                } catch (NormalUserAlreadySignedUpException ex) {
                    JsonObjectBuilder obj = Json.createObjectBuilder().add("msg", "user already signed up");
                    return Response.status(Response.Status.BAD_REQUEST).entity(obj).build();
                } catch (MaxParticipantsExceededException ex) {
                    JsonObjectBuilder obj = Json.createObjectBuilder().add("msg", "no space for anymore participants");
                    return Response.status(Response.Status.BAD_REQUEST).entity(obj).build();
                } catch (InsufficientBookingTokensException ex) {
                    JsonObjectBuilder obj = Json.createObjectBuilder().add("msg", "insufficient tokens");
                    return Response.status(Response.Status.BAD_REQUEST).entity(obj).build();
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
