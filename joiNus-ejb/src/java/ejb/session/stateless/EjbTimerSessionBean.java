/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import ejb.enums.SlotStatusEnum;
import entity.ActivityEntity;
import entity.FacilityEntity;
import entity.NormalUserEntity;
import entity.TimeSlotEntity;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.CreateNewTimeSlotException;
import util.exception.InputDataValidationException;
import util.exception.NormalUserNotFoundException;

/**
 *
 * @author reven
 */
@Stateless
public class EjbTimerSessionBean implements EjbTimerSessionBeanLocal {

    @EJB(name = "NormalUserEntitySessionBeanLocal")
    private NormalUserEntitySessionBeanLocal normalUserEntitySessionBeanLocal;

    @EJB(name = "ActivityEntitySessionBeanLocal")
    private ActivityEntitySessionBeanLocal activityEntitySessionBeanLocal;

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
                System.out.println("Generating for Facility : " + facility.getFacilityName());
                Date date = new Date();
                date.setMinutes(0);
                date.setSeconds(0);

                int openingHour = facility.getOpeningHour();
                int closingHour = facility.getClosingHour();

                for (int count = 0; count <= 14; count++) { // today + 14 days
                    System.out.println("Day Counter" + count);
                    timeSlots = timeSlotEntitySessionBeanLocal.retrieveTimeSlotsByDate(date.getYear(), date.getMonth(), date.getDate(), facility.getFacilityId());

                    if (timeSlots == null || timeSlots.isEmpty()) {
                        System.out.println("****** Timeslot does not exist for " + date.getDate() + "/" + date.getMonth() + " for " + facility.getFacilityName() + " ******");

                        for (int i = openingHour; i < closingHour; i++) {
                            date.setHours(i);
                            TimeSlotEntity ts = new TimeSlotEntity(date, SlotStatusEnum.AVAILABLE, facility);
                            timeSlotEntitySessionBeanLocal.createNewTimeSlotEntity(ts, facility.getFacilityId());
                            System.out.println("create new timeslot for hour " + i);
                        }
                    } else {
                        System.out.println("****** Timeslot exist for " + date.getDate() + "/" + date.getMonth() + " for " + facility.getFacilityName() + " ******");

                    }

                    date.setHours(0);
                    date.setDate(date.getDate() + 1);

                }

            }
        } catch (CreateNewTimeSlotException | InputDataValidationException ex) {
            ex.printStackTrace();
        }
    }

    @Schedule(dayOfWeek = "*", month = "*", dayOfMonth = "1", hour = "8", minute = "0", info = "tokenCreditorTimer")
    public void tokenCreditorTimer() {
        System.out.println("ejb.session.stateless.EjbTimerSessionBean.tokenCreditorTimer()");
        normalUserEntitySessionBeanLocal.creditTokens();
    }

    @Schedule(dayOfWeek = "*", month = "*", dayOfMonth = "*", hour = "*", minute = "0", info = "activityCompleteTimer") // run every hour
    public void activityCompleteTimer() {
        System.out.println("ejb.session.stateless.EjbTimerSessionBean.activityCompleteTimer()");

        try {
            /* this is for the actual code */
            Date date = new Date();
            date.setMinutes(0);
            date.setSeconds(0);

            /* this is for testing code 
            Date date = new Date();
            date.setMinutes(0);
            date.setSeconds(0);
            date.setHours(12);
            date.setDate(13);
             */
            System.out.println("Date sending in to check is " + date);
            List<ActivityEntity> activitiesToComplete = activityEntitySessionBeanLocal.markActivitiesCompletedByDateForTimer(date);

            System.out.println("Activities Checked");
            for (ActivityEntity activity : activitiesToComplete) {
                System.out.println(activity.getActivityName());
                System.out.println(activity.getActivityOver());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

//    @Schedule(dayOfWeek = "*", month = "*", dayOfMonth = "*", hour = "*", minute = "*", info = "testTimer") // run every min
//    public void testTimer() {
//        System.out.println("ejb.session.stateless.EjbTimerSessionBean.testTimer()");
//
//        List<NormalUserEntity> users = normalUserEntitySessionBeanLocal.retrieveAllNormalUser();
//
//        for (NormalUserEntity user : users) {
//            System.out.println("name");
//
//            System.out.println(user.getName());
//            System.out.println("own");
//
//            System.out.println(user.getActivitiesOwned());
//            System.out.println("part");
//
//            System.out.println(user.getActivitiesParticipated());
//        }
//    }
}
