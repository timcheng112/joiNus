/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.enums.SlotStatusEnum;
import ejb.session.stateless.ActivityEntitySessionBeanLocal;
import ejb.session.stateless.AdminEntitySessionBeanLocal;
import ejb.session.stateless.BookingEntitySessionBeanLocal;
import ejb.session.stateless.FacilityEntitySessionBeanLocal;
import ejb.session.stateless.NormalUserEntitySessionBeanLocal;
import ejb.session.stateless.TimeSlotEntitySessionBeanLocal;
import entity.ActivityEntity;
import entity.AdminEntity;
import entity.FacilityEntity;
import entity.NormalUserEntity;
import entity.TimeSlotEntity;
import java.io.IOException;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import util.exception.ActivityNotFoundException;
import util.exception.AdminNotFoundException;
import util.exception.BookingNotFoundException;
import util.exception.CreateNewTimeSlotException;
import util.exception.InputDataValidationException;
import util.exception.TimeSlotNotFoundException;

/**
 *
 * @author User
 */
@Named(value = "indexDemoManagedBean")
@ViewScoped
public class IndexDemoManagedBean implements Serializable {

    @EJB(name = "TimeSlotEntitySessionBeanLocal")
    private TimeSlotEntitySessionBeanLocal timeSlotEntitySessionBeanLocal;

    @EJB(name = "FacilityEntitySessionBeanLocal")
    private FacilityEntitySessionBeanLocal facilityEntitySessionBeanLocal;

    @EJB(name = "NormalUserEntitySessionBeanLocal")
    private NormalUserEntitySessionBeanLocal normalUserEntitySessionBeanLocal;

    @EJB(name = "ActivityEntitySessionBeanLocal")
    private ActivityEntitySessionBeanLocal activityEntitySessionBeanLocal;

    

    public IndexDemoManagedBean() {

    }

    @PostConstruct
    public void postConstruct() {
        
    }

    public void generateTimeslots() {
        System.out.println("jsf.managedbean.IndexDemoManagedBean.generateTimeslots()");
        
        try {
            List<TimeSlotEntity> timeSlots;

            List<FacilityEntity> facilities = facilityEntitySessionBeanLocal.retrieveAllFacilities();

            for (FacilityEntity facility : facilities) {
                Date date = new Date();
                date.setMinutes(0);
                date.setSeconds(0);

                int openingHour = facility.getOpeningHour();
                int closingHour = facility.getClosingHour();

                for (int count = 0; count <= 14; count++) { // today + 7 days
                    System.out.println("day " + count);
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
    
    public void creditTokens() {
        System.out.println("jsf.managedbean.IndexDemoManagedBean.creditTokens()");
        List<NormalUserEntity> normalUsers = normalUserEntitySessionBeanLocal.retrieveAllNormalUser();
        normalUserEntitySessionBeanLocal.creditTokens(normalUsers);
    }
    
    public void completeActivities() {
        System.out.println("jsf.managedbean.IndexDemoManagedBean.completeActivities()");
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

}
