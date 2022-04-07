/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.TimeSlotEntity;
import java.util.List;
import javax.ejb.Local;
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
@Local
public interface TimeSlotEntitySessionBeanLocal {

    public TimeSlotEntity createNewTimeSlotEntity(TimeSlotEntity newTimeSlotEntity, Long facilityId) throws CreateNewTimeSlotException, InputDataValidationException;

    public TimeSlotEntity retrieveTimeSlotById(Long timeSlotId) throws TimeSlotNotFoundException;

    public List<TimeSlotEntity> retrieveAllTimeSlots();

    public void updateTimeSlot(TimeSlotEntity timeSlotEntity) throws TimeSlotNotFoundException, InputDataValidationException, UpdateTimeSlotException;

    public void deleteTimeSlot(Long timeSlotId) throws TimeSlotNotFoundException, DeleteTimeSlotException;

    //public List<TimeSlotEntity> retrieveTimeSlotsByFacility(Long facilityId) throws FacilityNotFoundException;

    //public List<TimeSlotEntity> retrieveAvailableTimeSlotsByFacility(Long facilityId) throws FacilityNotFoundException;

    public List<TimeSlotEntity> retrieveTimeSlotsByDate(int year, int month, int date, Long facilityId) ;
    
}
