/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ActivityEntity;
import entity.BookingEntity;
import entity.CategoryEntity;
import entity.CommentEntity;
import entity.ImageEntity;
import entity.NormalUserEntity;
import entity.TimeSlotEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.ActivityNotFoundException;
import util.exception.BookingNotFoundException;
import util.exception.CategoryNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InsufficientBookingTokensException;
import util.exception.MaxParticipantsExceededException;
import util.exception.NormalUserAlreadySignedUpException;
import util.exception.NormalUserNotFoundException;
import util.exception.TimeSlotNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author User
 */
@Stateless
public class ActivityEntitySessionBean implements ActivityEntitySessionBeanLocal {

    @EJB(name = "TimeSlotEntitySessionBeanLocal")
    private TimeSlotEntitySessionBeanLocal timeSlotEntitySessionBeanLocal;

    @EJB(name = "FacilityEntitySessionBeanLocal")
    private FacilityEntitySessionBeanLocal facilityEntitySessionBeanLocal;

    @EJB(name = "CategoryEntitySessionBeanLocal")
    private CategoryEntitySessionBeanLocal categoryEntitySessionBeanLocal;

    @EJB(name = "NormalUserEntitySessionBeanLocal")
    private NormalUserEntitySessionBeanLocal normalUserEntitySessionBeanLocal;

    @EJB(name = "BookingEntitySessionBeanLocal")
    private BookingEntitySessionBeanLocal bookingEntitySessionBeanLocal;

    @PersistenceContext(unitName = "joiNus-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public ActivityEntitySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public ActivityEntity createNewActivity(ActivityEntity newActivityEntity, Long categoryId, Long timeSlotId) throws UnknownPersistenceException, InputDataValidationException, CategoryNotFoundException, TimeSlotNotFoundException, InsufficientBookingTokensException {
        Set<ConstraintViolation<ActivityEntity>> constraintViolations = validator.validate(newActivityEntity);

        CategoryEntity categoryEntity = categoryEntitySessionBeanLocal.retrieveCategoryByCategoryId(categoryId);

        if (constraintViolations.isEmpty()) {
            try {
                if (timeSlotId != null) {
                    TimeSlotEntity timeSlotEntity = timeSlotEntitySessionBeanLocal.retrieveTimeSlotById(timeSlotId);
                    BookingEntity newBookingEntity = new BookingEntity();
                    newBookingEntity = bookingEntitySessionBeanLocal.createNewBooking(newBookingEntity);
                    newBookingEntity.setActivity(newActivityEntity);
                    newBookingEntity.setTimeSlot(timeSlotEntity);
                    timeSlotEntity.setBooking(newBookingEntity);
                    newActivityEntity.setBooking(newBookingEntity);
                    normalUserEntitySessionBeanLocal.deductTokens(Boolean.TRUE, newActivityEntity.getActivityOwner());
                }
                //set linkages
                newActivityEntity.setCategory(categoryEntity);
                em.persist(newActivityEntity);
                em.flush();

                return newActivityEntity;
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    throw new UnknownPersistenceException(ex.getMessage());
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public List<ActivityEntity> retrieveAllActivities() {
        Query query = em.createQuery("SELECT a FROM ActivityEntity a ORDER BY a.activityName ASC");

        return query.getResultList();
    }

    @Override
    public List<ActivityEntity> retrieveMyActivities(Long userId
    ) {
        System.out.println("ejb.session.stateless.ActivityEntitySessionBean.retrieveMyActivities()");
        NormalUserEntity user = em.find(NormalUserEntity.class, userId);
        List<ActivityEntity> activities = new ArrayList<>();
        for (ActivityEntity activity : user.getActivitiesOwned()) {
            activities.add(activity);
        }
        for (ActivityEntity activity : user.getActivitiesParticipated()) {
            activities.add(activity);
        }
        return activities;
    }

    @Override
    public ActivityEntity retrieveActivityByActivityId(Long activityId) throws ActivityNotFoundException {
        ActivityEntity activityEntity = em.find(ActivityEntity.class, activityId);

        if (activityEntity != null) {
            return activityEntity;
        } else {
            throw new ActivityNotFoundException("Activity ID " + activityId + " does not exist!");
        }
    }

    @Override
    public List<ActivityEntity> retrieveActivitiesByActivityName(String activityName
    ) {
        Query query = em.createQuery("SELECT a FROM ActivityEntity a WHERE a.activityName = :activityName");
        query.setParameter("activityName", activityName);
        List<ActivityEntity> activityEntities = query.getResultList();

        for (ActivityEntity activityEntity : activityEntities) {
            activityEntity.getActivityOwner();
            activityEntity.getParticipants().size();
            activityEntity.getCategory();
            activityEntity.getBooking();
            activityEntity.getComments().size();
            activityEntity.getGallery().size();
        }

        return activityEntities;
    }

    @Override
    public List<ActivityEntity> filterActivitiesByCategory(String categoryName
    ) {
        Query query = em.createQuery("SELECT a FROM ActivityEntity a WHERE a.category.categoryName = :categoryName");
        query.setParameter("categoryName", categoryName);
        List<ActivityEntity> activityEntities = query.getResultList();

        for (ActivityEntity activityEntity : activityEntities) {
            activityEntity.getActivityOwner();
            activityEntity.getParticipants().size();
            activityEntity.getCategory();
            activityEntity.getBooking();
            activityEntity.getComments().size();
            activityEntity.getGallery().size();
        }

        return activityEntities;
    }

    @Override
    public void deleteActivity(Long activityId) throws ActivityNotFoundException, BookingNotFoundException {

        ActivityEntity activityEntityToRemove = retrieveActivityByActivityId(activityId);

        activityEntityToRemove.getCategory().getActivities().remove(activityEntityToRemove);

        for (NormalUserEntity normalUserEntity : activityEntityToRemove.getParticipants()) {
            normalUserEntity.getActivitiesParticipated().remove(activityEntityToRemove);
        }

        Integer tokensToBeRefunded = activityEntityToRemove.getBooking().getTimeSlot().getFacility().getTokenCost();
        activityEntityToRemove.getActivityOwner().setBookingTokens(activityEntityToRemove.getActivityOwner().getBookingTokens() + tokensToBeRefunded);
        tokensToBeRefunded = Math.floorDiv(tokensToBeRefunded, 2);
        for (NormalUserEntity participant : activityEntityToRemove.getParticipants()) {
            participant.setBookingTokens(participant.getBookingTokens() + tokensToBeRefunded);
        }

        activityEntityToRemove.getActivityOwner().getActivitiesOwned().remove(activityEntityToRemove);

        activityEntityToRemove.getParticipants().clear();

        deleteImages(activityEntityToRemove);
        activityEntityToRemove.getGallery().clear();

        deleteComments(activityEntityToRemove);
        activityEntityToRemove.getComments().clear();

        bookingEntitySessionBeanLocal.deleteBooking(activityEntityToRemove.getBooking().getBookingId());

        em.remove(activityEntityToRemove);

    }

    @Override
    public void deleteImages(ActivityEntity activityEntityToRemove
    ) {
        List<ImageEntity> imageEntities = activityEntityToRemove.getGallery();

        for (ImageEntity imageEntity : imageEntities) {
            em.remove(imageEntity);
        }
    }

    @Override
    public Long addComment(CommentEntity commentEntity, Long activityId) throws ActivityNotFoundException {
        ActivityEntity activity = retrieveActivityByActivityId(activityId);
        activity.getComments().add(commentEntity);
        em.persist(commentEntity);
        em.flush();
        return commentEntity.getCommentId();
    }

    @Override
    public void deleteComments(ActivityEntity activityEntityToRemove
    ) {
        List<CommentEntity> commentEntities = activityEntityToRemove.getComments();

        for (CommentEntity commentEntity : commentEntities) {
            em.remove(commentEntity);
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<ActivityEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

    @Override
    public void absentPunishment(Long activityId, List<Long> absenteeIds) {
        System.out.println("ejb.session.stateless.ActivityEntitySessionBean.absentPunishment()");
        System.out.println("ActivityId: " + activityId);
        System.out.println("absenteeIds: " + absenteeIds.toString());
        ActivityEntity activity = em.find(ActivityEntity.class, activityId);

        if (activity != null) {
            List<Long> existingIds = activity.getAbsentIds();
            List<Long> existingIds2 = activity.getAbsentIds();

            System.out.println("activity not null");

            for (Long aId : absenteeIds) { // punish user

                if (!existingIds2.contains(aId)) {
                    System.out.println("need to punish user " + aId);
                    normalUserEntitySessionBeanLocal.punishUser(aId);
                } else {
                    System.out.println("no need to punish user " + aId);
                }
            }

            for (Long aId : absenteeIds) { // add them to the list
                if (!existingIds2.contains(aId)) {
                    existingIds.add(aId);
                }
            }
            activity.setAbsentIds(existingIds);
        }

        System.out.println("absentPunishment method end");
    }

    @Override
    public void signUpForActivity(Long activityId, Long userId) throws NormalUserNotFoundException, NormalUserAlreadySignedUpException, InsufficientBookingTokensException, MaxParticipantsExceededException {
        System.out.println("ejb.session.stateless.ActivityEntitySessionBean.signUpForActivity()");
        ActivityEntity activity = em.find(ActivityEntity.class, activityId);
        NormalUserEntity user = normalUserEntitySessionBeanLocal.retrieveNormalUserByUserId(userId);

        List<NormalUserEntity> participants = activity.getParticipants();
        if (participants.contains(user) || activity.getActivityOwner() == user) {
            throw new NormalUserAlreadySignedUpException("User ID error");
        }
        if (activity.getNumberOfParticipants() >= activity.getMaxParticipants()) {
            throw new MaxParticipantsExceededException("No space for anymore participants!");
        }

        normalUserEntitySessionBeanLocal.deductTokens(Boolean.FALSE, user);
        participants.add(user);
        activity.setParticipants(participants);
    }
}
