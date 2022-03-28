/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.FacilityEntity;
import entity.TimeSlotEntity;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CreateNewTimeSlotException;
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
    
    public TimeSlotEntitySessionBean(){
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @Override
    public TimeSlotEntity createNewTimeSlotEntity(TimeSlotEntity newTimeSlotEntity, Long facilityId) throws CreateNewTimeSlotException, InputDataValidationException
    {
        Set<ConstraintViolation<TimeSlotEntity>> constraintViolations = validator.validate(newTimeSlotEntity);
        
        if (constraintViolations.isEmpty()) {
            try {
                //get facility by ID
                FacilityEntity facility = facilityEntitySessionBeanLocal.retrieveFacilityByFacilityId(facilityId);
                //connect both sides
                facility.addTimeSlot(newTimeSlotEntity);
                newTimeSlotEntity.setFacility(facility);
                
                entityManager.persist(newTimeSlotEntity);
                entityManager.flush();
                
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
        
        for (TimeSlotEntity timeSlotEntity: timeSlotEntities){
            timeSlotEntity.getFacility();
            timeSlotEntity.getBooking();
        }
        
        return timeSlotEntities;
    }
    
    @Override
    //nothing wrong here
    public TimeSlotEntity retrieveTimeSlotById(Long timeSlotId) throws TimeSlotNotFoundException
    {
        TimeSlotEntity timeSlotEntity = entityManager.find(TimeSlotEntity.class, timeSlotId);
        
        if(timeSlotEntity != null)
        {
            return timeSlotEntity;
        } else {
            throw new TimeSlotNotFoundException("TimeSlot ID " + timeSlotId + " does not exist!");
        }
    }
    
    @Override
    public void updateTimeSlot(TimeSlotEntity timeSlotEntity) throws TimeSlotNotFoundException, InputDataValidationException, UpdateTimeSlotException
    {
        Set<ConstraintViolation<TimeSlotEntity>>constraintViolations = validator.validate(timeSlotEntity);
        
        if (constraintViolations.isEmpty()){
            if (timeSlotEntity.getTimeSlotId() != null) {
                TimeSlotEntity timeSlotEntityToUpdate = retrieveTimeSlotById(timeSlotEntity.getTimeSlotId());
                
                if (timeSlotEntity.getFacility() != timeSlotEntityToUpdate.getFacility())
                {
                    throw new UpdateTimeSlotException("Updated timeslot and existing timeslot has a facility mismatch!");
                } if (timeSlotEntity.getBooking() != null && timeSlotEntity.getBooking() != timeSlotEntityToUpdate.getBooking())
                {
                    throw new UpdateTimeSlotException("TimeSlot is already allotted another booking! Please Cancel previous booking first.");
                }
                
                timeSlotEntityToUpdate.setBooking(timeSlotEntity.getBooking());
                if (timeSlotEntity.getBooking() != null ){
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
    
    /*
    public void deleteTimeSlot(Long timeSlotId) throws TimeSlotNotFoundException
    {
            TimeSlotEntity timeSlotEntityToRemove = retrieveTimeSlotById(timeSlotId);
            
            
    }
    */
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<TimeSlotEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
