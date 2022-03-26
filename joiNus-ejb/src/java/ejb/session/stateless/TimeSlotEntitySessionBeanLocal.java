/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.TimeSlotEntity;
import javax.ejb.Local;
import util.exception.CreateNewTimeSlotException;
import util.exception.InputDataValidationException;

/**
 *
 * @author wongs
 */
@Local
public interface TimeSlotEntitySessionBeanLocal {

    public TimeSlotEntity createNewTimeSlotEntity(TimeSlotEntity newTimeSlotEntity, Long facilityId) throws CreateNewTimeSlotException, InputDataValidationException;
    
}
