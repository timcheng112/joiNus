/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import ejb.enums.SlotStatusEnum;
import entity.FacilityEntity;
import entity.TimeSlotEntity;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.CreateNewTimeSlotException;
import util.exception.InputDataValidationException;

/**
 *
 * @author reven
 */
@Stateless
public class EjbTimerSessionBean implements EjbTimerSessionBeanLocal {

    @EJB
    private FacilityEntitySessionBeanLocal facilityEntitySessionBeanLocal;

    @PersistenceContext(unitName = "joiNus-ejbPU")
    private EntityManager em;

    
    @EJB
    private TimeSlotEntitySessionBeanLocal timeSlotEntitySessionBeanLocal;

    @Schedule(dayOfWeek = "*", month = "*", dayOfMonth = "*", hour = "0", minute = "0", info = "timeslotEntityCreationTimer")
    public void timeslotEntityCreationTimer() {
        System.out.println("ejb.session.stateless.EjbTimerSessionBean.timeslotEntityCreationTimer()");

        try {
            List<TimeSlotEntity> timeSlots;

            List<FacilityEntity> facilities = facilityEntitySessionBeanLocal.retrieveAllFacilities();

            for (FacilityEntity facility : facilities) {
//                Calendar c = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("Asia/Singapore")));
//                c.set(Calendar.MINUTE, 0);
//                c.set(Calendar.SECOND, 0);
//                c.set(Calendar.MILLISECOND, 0);
                
                Date date = new Date();
                date.setMinutes(0);
                date.setSeconds(0);
                
                int openingHour = facility.getOpeningHour();
                int closingHour = facility.getClosingHour();
                
                for (int count = 0; count <= 7; count++) { // today + 7 days
                    System.out.println("day " + count);
                    timeSlots = timeSlotEntitySessionBeanLocal.retrieveTimeSlotsByDate(date.getYear(), date.getMonth(), date.getDate(), facility.getFacilityId());

                    if (timeSlots == null || timeSlots.isEmpty()) {
                        System.out.println("****** Timeslot exist for " + date.getDate() + "/" + date.getMonth() + " for " + facility.getFacilityName() + " ******");

                        for (int i = openingHour; i < closingHour; i++) {
                            date.setHours(i);
                            TimeSlotEntity ts = new TimeSlotEntity(date, SlotStatusEnum.AVAILABLE, facility);
                            timeSlotEntitySessionBeanLocal.createNewTimeSlotEntity(ts, facility.getFacilityId());
                            System.out.println("create new timeslot for hour " + i);
                        }
                    } else {
                        System.out.println("****** Timeslot exist for " + date.getDate() + "/" + date.getMonth() + " for " + facility.getFacilityName() + " ******");

                    }
//                    c.set(Calendar.HOUR_OF_DAY, 0);
//                    c.add(Calendar.DATE, 1);
                    
                    date.setHours(0);
                    date.setDate(date.getDate() + 1);
                   
                }

            }
        } catch (CreateNewTimeSlotException | InputDataValidationException ex) {
            ex.printStackTrace();
        }
    }
}
