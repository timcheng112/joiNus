/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.enums.SlotStatusEnum;
import ejb.session.stateless.ActivityEntitySessionBeanLocal;
import ejb.session.stateless.AdminEntitySessionBeanLocal;
import ejb.session.stateless.BookingEntitySessionBeanLocal;
import ejb.session.stateless.CategoryEntitySessionBeanLocal;
import ejb.session.stateless.FacilityEntitySessionBeanLocal;
import ejb.session.stateless.NormalUserEntitySessionBeanLocal;
import ejb.session.stateless.TimeSlotEntitySessionBeanLocal;
import entity.ActivityEntity;
import entity.AdminEntity;
import entity.BookingEntity;
import entity.CategoryEntity;
import entity.CommentEntity;
import entity.FacilityEntity;
import entity.NormalUserEntity;
import entity.TimeSlotEntity;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.ActivityNotFoundException;
import util.exception.AdminNotFoundException;
import util.exception.AdminUsernameExistException;
import util.exception.CategoryNotFoundException;
import util.exception.CreateNewCategoryException;
import util.exception.CreateNewFacilityException;
import util.exception.CreateNewTimeSlotException;
import util.exception.FacilityNameExistException;
import util.exception.InputDataValidationException;
import util.exception.InsufficientBookingTokensException;
import util.exception.NormalUserNameExistException;
import util.exception.TimeSlotNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author User
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {

    @EJB
    private NormalUserEntitySessionBeanLocal normalUserEntitySessionBeanLocal;
    @EJB
    private BookingEntitySessionBeanLocal bookingEntitySessionBeanLocal;
    @EJB
    private ActivityEntitySessionBeanLocal activityEntitySessionBeanLocal;
    @EJB
    private CategoryEntitySessionBeanLocal categoryEntitySessionBeanLocal;
    @EJB
    private TimeSlotEntitySessionBeanLocal timeSlotEntitySessionBeanLocal;
    @EJB
    private FacilityEntitySessionBeanLocal facilityEntitySessionBeanLocal;
    @EJB
    private AdminEntitySessionBeanLocal adminEntitySessionBeanLocal;

    @PersistenceContext(unitName = "joiNus-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PostConstruct
    public void postConstruct() {
        try {
//            initializeTimeSlots(); // moved functionality to EJBTimer
            adminEntitySessionBeanLocal.retrieveAdminByUsername("superadmin1");
        } catch (AdminNotFoundException ex) {
            initializeData();
        }
    }

    private void initializeData() {
        System.out.println("ejb.session.singleton.DataInitSessionBean.initializeData()");
        System.out.println("Data Initialization Started");
        try {
            // create superadmin
            adminEntitySessionBeanLocal.createNewAdmin(new AdminEntity("superadmin1", "password", null, Boolean.TRUE));

            // create normal user
            NormalUserEntity normalUser = normalUserEntitySessionBeanLocal.createNewNormalUser(new NormalUserEntity("email@email.com", "name", 420, 420, "user", "password"));

            // create facility
            FacilityEntity fac = facilityEntitySessionBeanLocal.createNewFacility(new FacilityEntity("USC Bouldering Wall", "USC", 5, 30, "USC, 2 Sports Drive, Singapore 117288", 10, 18));

            Date date = new Date();
            date.setMinutes(0);
            date.setHours(12);
            date.setSeconds(0);
            date.setDate(12);
            date.setMonth(3);

            TimeSlotEntity ts = new TimeSlotEntity(date, SlotStatusEnum.UNAVAILABLE, fac);
            timeSlotEntitySessionBeanLocal.createNewTimeSlotEntity(ts, fac.getFacilityId());

            // create booking
            BookingEntity booking = bookingEntitySessionBeanLocal.createNewBooking(new BookingEntity(SlotStatusEnum.AVAILABLE, date, null, ts));

            date.setHours(13);
            TimeSlotEntity ts2 = new TimeSlotEntity(date, SlotStatusEnum.AVAILABLE, fac);
            timeSlotEntitySessionBeanLocal.createNewTimeSlotEntity(ts2, fac.getFacilityId());

            // create category
            CategoryEntity cat = categoryEntitySessionBeanLocal.createNewCategoryEntity(new CategoryEntity("Sports"), null);
            categoryEntitySessionBeanLocal.createNewCategoryEntity(new CategoryEntity("Bouldering"), cat.getCategoryId());

            // create activity
            ActivityEntity activity = activityEntitySessionBeanLocal.createNewActivity(new ActivityEntity("Activity One", "Activity One Description", 5, new ArrayList<>(), normalUser, new ArrayList<>(), cat, null, date), cat.getCategoryId(), ts.getTimeSlotId());
            bookingEntitySessionBeanLocal.associateBookingWithActivity(booking.getBookingId(), activity.getActivityId());

            CommentEntity comment = new CommentEntity("Will anyone be bringing any equipment?", normalUser, date);
            activityEntitySessionBeanLocal.addComment(comment, activity.getActivityId());
            System.out.println("Data Initialization Ended");
        } catch (AdminUsernameExistException | CreateNewFacilityException | CreateNewCategoryException | CreateNewTimeSlotException | FacilityNameExistException | UnknownPersistenceException | InputDataValidationException | NormalUserNameExistException | ActivityNotFoundException | CategoryNotFoundException | TimeSlotNotFoundException | InsufficientBookingTokensException ex) {
            ex.printStackTrace();
        }

    }

    public void initializeTimeSlots() {
        System.out.println("ejb.session.singleton.DataInitSessionBean.initializeTimeSlots()");
        try {
            List<TimeSlotEntity> timeSlots;

            List<FacilityEntity> facilities = facilityEntitySessionBeanLocal.retrieveAllFacilities();

            for (FacilityEntity facility : facilities) {
                Calendar c = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("Asia/Singapore")));
                c.set(Calendar.MINUTE, 0);
                c.set(Calendar.SECOND, 0);
                c.set(Calendar.MILLISECOND, 0);
                int openingHour = facility.getOpeningHour();
                int closingHour = facility.getClosingHour();

                for (int count = 0; count <= 7; count++) { // today + 7 days
                    System.out.println("day " + count);
                    timeSlots = timeSlotEntitySessionBeanLocal.retrieveTimeSlotsByDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), facility.getFacilityId());

                    if (timeSlots == null || timeSlots.isEmpty()) {
                        System.out.println("****** Timeslot exist for " + c.get(Calendar.DATE) + "/" + c.get(Calendar.MONTH) + " for " + facility.getFacilityName() + " ******");

                        for (int i = openingHour; i < closingHour; i++) {
                            c.set(Calendar.HOUR_OF_DAY, i);
                            TimeSlotEntity ts = new TimeSlotEntity(c.getTime(), SlotStatusEnum.AVAILABLE, facility);
                            timeSlotEntitySessionBeanLocal.createNewTimeSlotEntity(ts, facility.getFacilityId());
                            System.out.println("create new timeslot for hour " + i);
                        }
                    } else {
                        System.out.println("****** Timeslot exist for " + c.get(Calendar.DATE) + "/" + c.get(Calendar.MONTH) + " for " + facility.getFacilityName() + " ******");

                    }
                    c.set(Calendar.HOUR_OF_DAY, 0);
                    c.add(Calendar.DATE, 1);
                }

            }
        } catch (CreateNewTimeSlotException | InputDataValidationException ex) {
            ex.printStackTrace();
        }
    }
}
