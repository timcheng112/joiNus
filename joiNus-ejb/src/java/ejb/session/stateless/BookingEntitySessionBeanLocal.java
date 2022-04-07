/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.BookingEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.BookingNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author User
 */
@Local
public interface BookingEntitySessionBeanLocal {

    public BookingEntity createNewBooking(BookingEntity newBookingEntity) throws UnknownPersistenceException, InputDataValidationException;

    public List<BookingEntity> retrieveBookingByActivity(Long activityId);

    public void associateBookingWithActivity(Long bookingId, Long activityId);

    public void deleteBooking(Long bookingId) throws BookingNotFoundException;
    
}
