/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import ejb.enums.SlotStatusEnum;
import entity.FacilityEntity;
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
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CreateNewTimeSlotException;
import util.exception.DeleteTimeSlotException;
import util.exception.FacilityNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.TimeSlotNotFoundException;
import util.exception.UpdateTimeSlotException;

/**
 *
 * @author wongs
 */
@Stateless
public class TimeSlotEntitySessionBean implements TimeSlotEntitySessionBeanLocal {

    @PersistenceContext(unitName = "joiNus-ejbPU")
    private EntityManager entityManager;

    @EJB
    private FacilityEntitySessionBeanLocal facilityEntitySessionBeanLocal;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public TimeSlotEntitySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public TimeSlotEntity createNewTimeSlotEntity(TimeSlotEntity newTimeSlotEntity, Long facilityId) throws CreateNewTimeSlotException, InputDataValidationException {
        Set<ConstraintViolation<TimeSlotEntity>> constraintViolations = validator.validate(newTimeSlotEntity);

        if (constraintViolations.isEmpty()) {
            try {
                //get facility by ID
                entityManager.persist(newTimeSlotEntity);
                entityManager.flush();
                FacilityEntity facility = facilityEntitySessionBeanLocal.retrieveFacilityByFacilityId(facilityId);
                //connect both sides
                facility.addTimeSlot(newTimeSlotEntity);
//                newTimeSlotEntity.setFacility(facility);

                return newTimeSlotEntity;

            } catch (FacilityNotFoundException ex) {
                //TO BE DONE
                throw new CreateNewTimeSlotException("Cannot find the facility you entered!");
            } catch (Exception ex) {
                throw new CreateNewTimeSlotException("An unexpected error has occurred: " + ex.getMessage());
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public List<TimeSlotEntity> retrieveAllTimeSlots() {
        Query query = entityManager.createQuery("SELECT c FROM TimeSlotEntity c");
        List<TimeSlotEntity> timeSlotEntities = query.getResultList();

        for (TimeSlotEntity timeSlotEntity : timeSlotEntities) {
            timeSlotEntity.getFacility();
            timeSlotEntity.getBooking();
        }

        return timeSlotEntities;
    }

    @Override
    public List<TimeSlotEntity> retrieveAllAvailableTimeSlots() {
        List<TimeSlotEntity> timeSlotEntities = retrieveAllTimeSlots();
        List<TimeSlotEntity> availTimeSlots = new ArrayList<>();

        for (TimeSlotEntity timeSlotEntity : timeSlotEntities) {
            if (timeSlotEntity.getStatus() == SlotStatusEnum.AVAILABLE) {
                availTimeSlots.add(timeSlotEntity);
            }
        }
        return availTimeSlots;
    }

    @Override
    //nothing wrong here
    public TimeSlotEntity retrieveTimeSlotById(Long timeSlotId) throws TimeSlotNotFoundException {
        TimeSlotEntity timeSlotEntity = entityManager.find(TimeSlotEntity.class, timeSlotId);

        if (timeSlotEntity != null) {
            return timeSlotEntity;
        } else {
            throw new TimeSlotNotFoundException("TimeSlot ID " + timeSlotId + " does not exist!");
        }
    }

    @Override
    public List<TimeSlotEntity> retrieveTimeSlotsByDate(int year, int month, int date, Long facilityId) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DATE, date-1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        Date dateXD = c.getTime();

        Query query = entityManager.createQuery("SELECT ts FROM TimeSlotEntity ts WHERE ts.facility.facilityId = :inFacility AND ts.timeSlotTime BETWEEN :inStart AND :inEnd ORDER BY ts.timeSlotId ASC");
        c.add(Calendar.SECOND, -1);
        dateXD = c.getTime();
        query.setParameter("inStart", dateXD, TemporalType.TIMESTAMP);
        System.out.println("Starting date of check is " + dateXD);
        c.add(Calendar.DATE, 1);
        dateXD = c.getTime();
        query.setParameter("inEnd", dateXD, TemporalType.TIMESTAMP);
        System.out.println("End date of check is " + dateXD);
        query.setParameter("inFacility", facilityId);

        List<TimeSlotEntity> timeSlots = query.getResultList();
        System.out.println(timeSlots);

        if (!timeSlots.isEmpty()) {
            return timeSlots;
        } else {
            return null;
        }
    }

    /**
     * Commented out because not sure if we need it.
     *
     * @Override public List<TimeSlotEntity> retrieveTimeSlotsByFacility(Long
     * facilityId) throws FacilityNotFoundException { FacilityEntity
     * facilityEntity = entityManager.find(FacilityEntity.class, facilityId);
     *
     * if (facilityEntity == null) { throw new FacilityNotFoundException("Unable
     * to find facility with provided id"); }
     *
     * List<TimeSlotEntity> timeSlots = facilityEntity.getTimeSlots(); for
     * (TimeSlotEntity timeSlot : timeSlots) { timeSlot.getBooking();
     * timeSlot.getFacility(); }
     *
     * return timeSlots; }
     *
     * @Override public List<TimeSlotEntity>
     * retrieveAvailableTimeSlotsByFacility(Long facilityId) throws
     * FacilityNotFoundException { FacilityEntity facilityEntity =
     * entityManager.find(FacilityEntity.class, facilityId);
     *
     * if (facilityEntity == null) { throw new FacilityNotFoundException("Unable
     * to find facility with provided id"); }
     *
     * Query query = entityManager.createQuery("SELECT ts FROM TimeSlotEntity ts
     * WHERE ts.facility = :inFacility AND ts.status = :inStatus");
     * query.setParameter("inFacility", facilityEntity);
     * query.setParameter("inStatus", SlotStatusEnum.AVAILABLE);
     *
     * List<TimeSlotEntity> timeSlots = query.getResultList();
     *
     * for (TimeSlotEntity timeSlot : timeSlots) { timeSlot.getBooking();
     * timeSlot.getFacility(); }
     *
     * return timeSlots; }
     *
     */
    @Override
    public void updateTimeSlot(TimeSlotEntity timeSlotEntity) throws TimeSlotNotFoundException, InputDataValidationException, UpdateTimeSlotException {
        Set<ConstraintViolation<TimeSlotEntity>> constraintViolations = validator.validate(timeSlotEntity);
        if (constraintViolations.isEmpty()) {
            if (timeSlotEntity.getTimeSlotId() != null) {
                TimeSlotEntity timeSlotEntityToUpdate = retrieveTimeSlotById(timeSlotEntity.getTimeSlotId());

                if (!(timeSlotEntity.getFacility().getFacilityId().equals(timeSlotEntityToUpdate.getFacility().getFacilityId()))) {
                    throw new UpdateTimeSlotException("Updated timeslot and existing timeslot has a facility mismatch!");
                }
                if (timeSlotEntity.getBooking() != null && timeSlotEntity.getBooking() != timeSlotEntityToUpdate.getBooking()) {
                    throw new UpdateTimeSlotException("TimeSlot is already allotted another booking! Please Cancel previous booking first.");
                }
                
                timeSlotEntityToUpdate.setBooking(timeSlotEntity.getBooking());
                if (timeSlotEntity.getBooking() != null) {
                    timeSlotEntity.getBooking().setTimeSlot(timeSlotEntity);
                }
                //probably no need to update facility ever right?
                //timeSlotEntityToUpdate.setFacility(facility);
                timeSlotEntityToUpdate.setTimeSlotTime(timeSlotEntity.getTimeSlotTime());
                timeSlotEntityToUpdate.setStatus(timeSlotEntity.getStatus());
            } else {
                throw new TimeSlotNotFoundException("Time Slot ID not provided for category to be updated");
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public void deleteTimeSlot(Long timeSlotId) throws TimeSlotNotFoundException, DeleteTimeSlotException {
        TimeSlotEntity timeSlotEntityToRemove = retrieveTimeSlotById(timeSlotId);

        if (timeSlotEntityToRemove.getBooking() != null) {
            throw new DeleteTimeSlotException("The Time Slot to be deleted has a booking!");
        }

        try {
            timeSlotEntityToRemove.getFacility().removeTimeSlot(timeSlotEntityToRemove);
            entityManager.remove(timeSlotEntityToRemove);
        } catch (Exception ex) {
            throw new DeleteTimeSlotException("Failed to remove both sides of facility-timeslot relationship!");
        }

    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<TimeSlotEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
