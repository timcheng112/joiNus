/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import ejb.enums.SlotStatusEnum;
import entity.ActivityEntity;
import entity.BookingEntity;
import entity.CategoryEntity;
import entity.CommentEntity;
import entity.FacilityEntity;
import entity.ImageEntity;
import entity.NormalUserEntity;
import entity.TimeSlotEntity;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.ActivityNotFoundException;
import util.exception.BookingNotFoundException;
import util.exception.CategoryNotFoundException;
import util.exception.CreateNewTimeSlotException;
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
    public ActivityEntity createNewActivity(ActivityEntity newActivityEntity, Long categoryId, Long timeSlotId, Date activityDate) throws UnknownPersistenceException, InputDataValidationException, CategoryNotFoundException, TimeSlotNotFoundException, InsufficientBookingTokensException, CreateNewTimeSlotException {
        Set<ConstraintViolation<ActivityEntity>> constraintViolations = validator.validate(newActivityEntity);

        CategoryEntity categoryEntity = categoryEntitySessionBeanLocal.retrieveCategoryByCategoryId(categoryId);
        if (constraintViolations.isEmpty()) {
            try {
                TimeSlotEntity timeSlotEntity = new TimeSlotEntity();
                if (timeSlotId != null) {
                    timeSlotEntity = timeSlotEntitySessionBeanLocal.retrieveTimeSlotById(timeSlotId);
                } else {
                    Query query = em.createQuery("SELECT f FROM FacilityEntity f WHERE f.facilityName='Non-NUS Facility'");
                    FacilityEntity facility = (FacilityEntity) query.getSingleResult();
                    System.out.println(facility);
                    timeSlotEntity = new TimeSlotEntity(activityDate, SlotStatusEnum.AVAILABLE, facility);
                    timeSlotEntity = timeSlotEntitySessionBeanLocal.createNewTimeSlotEntity(timeSlotEntity, facility.getFacilityId());
                }
                //set linkages
                BookingEntity newBookingEntity = new BookingEntity();
                newBookingEntity = bookingEntitySessionBeanLocal.createNewBooking(newBookingEntity);
                newBookingEntity.setActivity(newActivityEntity);
                newBookingEntity.setTimeSlot(timeSlotEntity);

                timeSlotEntity.setBooking(newBookingEntity);

                newActivityEntity.setBooking(newBookingEntity);

                normalUserEntitySessionBeanLocal.deductTokens(Boolean.TRUE, newActivityEntity.getActivityOwner());

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
            System.out.println("dafuq");
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public List<ActivityEntity> retrieveAllActivities() {
        Query query = em.createQuery("SELECT a FROM ActivityEntity a ORDER BY a.activityName ASC");

        return query.getResultList();
    }
    
    @Override
    public List<ActivityEntity> retrieveAllOngoingActivities() {
        Query query = em.createQuery("SELECT a FROM ActivityEntity a WHERE a.activityOver = false ORDER BY a.activityName ASC");

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

    @Override
    public List<ActivityEntity> retrieveActivitiesByDateForTimer(Date date) {
        System.out.println("ejb.session.stateless.ActivityEntitySessionBean.retrieveActivitiesByDate()");
        Calendar c = Calendar.getInstance();

        Date nowDate = new Date();
        int month = nowDate.getMonth();
        int year = nowDate.getYear() + 1900;
        int day = 6;
        int hour = nowDate.getHours();

        Date dateXD;

        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DATE, day);
        c.set(Calendar.HOUR_OF_DAY, hour);

        List<ActivityEntity> allActivities = retrieveAllActivities();

        for (ActivityEntity activity : allActivities) {
            System.out.println("Activity facility id is " + activity.getBooking().getTimeSlot().getFacility().getFacilityId());
            System.out.println("Activity facility timeslot id is " + activity.getBooking().getTimeSlot().getTimeSlotId());

            Query query = em.createQuery("SELECT ts FROM TimeSlotEntity ts WHERE ts.facility.facilityId = :inFacility AND ts.timeSlotTime BETWEEN :inStart AND :inEnd ORDER BY ts.timeSlotId ASC");
            c.add(Calendar.SECOND, -1);
            dateXD = c.getTime();
            query.setParameter("inStart", dateXD, TemporalType.TIMESTAMP);
            System.out.println("Starting date of check is " + dateXD);
            c.add(Calendar.MINUTE, 1);
            dateXD = c.getTime();
            query.setParameter("inEnd", dateXD, TemporalType.TIMESTAMP);
            System.out.println("End date of check is " + dateXD);
            query.setParameter("inFacility", activity.getBooking().getTimeSlot().getFacility().getFacilityId());

            List<TimeSlotEntity> timeSlots = query.getResultList();
            System.out.println(timeSlots);

            if (!timeSlots.isEmpty()) {
                System.out.println("Timeslots found");
                for (TimeSlotEntity ts : timeSlots) {
                    System.out.println("Timeslot ID is " + ts.getTimeSlotId());
                    System.out.println("Timeslot Time is " + ts.getTimeSlotTime());

                    activity.setActivityOver(Boolean.TRUE);

                    for (NormalUserEntity user : activity.getParticipants()) {
                        user.setSocialCredits(user.getSocialCredits() + 30); // add social credits +30
                    }
                    activity.getActivityOwner().setSocialCredits(activity.getActivityOwner().getSocialCredits() + 40); // add social credit to owner +40

                    activity.getBooking().setBookingStatus(SlotStatusEnum.UNAVAILABLE);
                }

            } else {
                System.out.println("No timeslots");
            }

        }

        System.out.println("End of method");
        return null;
    }
}
