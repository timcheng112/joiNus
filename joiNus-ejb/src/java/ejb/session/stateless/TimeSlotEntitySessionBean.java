/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CategoryEntity;
import entity.FacilityEntity;
import entity.TimeSlotEntity;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    
    public List<TimeSlotEntity> retrieveAllTimeSlots() {
        Query query = entityManager.createQuery("SELECT c FROM TimeSlotEntity c");
        List<TimeSlotEntity> timeSlotEntities = query.getResultList();
        
        for (TimeSlotEntity timeSlotEntity: timeSlotEntities){
            timeSlotEntity.getFacility();
            timeSlotEntity.getBooking();
        }
        
        return timeSlotEntities;
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<TimeSlotEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
