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
import util.exception.MaxParticipantsExceededException;
import util.exception.NormalUserAlreadySignedUpException;
import util.exception.NormalUserNameExistException;
import util.exception.NormalUserNotFoundException;
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
            adminEntitySessionBeanLocal.createNewAdmin(new AdminEntity("nusccadmin1", "password", "NUS Climbing Club", Boolean.FALSE));

            // create normal user
            NormalUserEntity normalUser = normalUserEntitySessionBeanLocal.createNewNormalUser(new NormalUserEntity("email@email.com", "MR CODER JEREMY", 420, 420, "user0", "password"));
            normalUserEntitySessionBeanLocal.createNewNormalUser(new NormalUserEntity("email1@email.com", "Sir Elton John", 200, 420, "User1", "password"));
            normalUserEntitySessionBeanLocal.createNewNormalUser(new NormalUserEntity("JohnWong@gmail.com", "Prof Tan Wee Kek", 6969, 420, "user2", "password"));
            normalUserEntitySessionBeanLocal.createNewNormalUser(new NormalUserEntity("email3@email.com", "Hedgehog", 421, 420, "user3", "password"));
            normalUserEntitySessionBeanLocal.createNewNormalUser(new NormalUserEntity("email4@email.com", "Prof Lu Wei Qian", -99, 420, "user4", "password"));
            NormalUserEntity normalUser3 = normalUserEntitySessionBeanLocal.createNewNormalUser(new NormalUserEntity("email5@email.com", "Joe Biden", 46, 420, "user5", "password"));
            normalUserEntitySessionBeanLocal.createNewNormalUser(new NormalUserEntity("email6@email.com", "Joe Mama", 902, 420, "user6", "password"));
            normalUserEntitySessionBeanLocal.createNewNormalUser(new NormalUserEntity("email7@email.com", "Mrs Ligma", 738, 420, "user7", "password"));
            normalUserEntitySessionBeanLocal.createNewNormalUser(new NormalUserEntity("email8@email.com", "Prof Chong Ket Fah", 3000, 420, "user8", "password"));
            normalUserEntitySessionBeanLocal.createNewNormalUser(new NormalUserEntity("email9@email.com", "Prof Zhou Li Feng", 3001, 420, "user9", "password"));
            NormalUserEntity normalUser2 = normalUserEntitySessionBeanLocal.createNewNormalUser(new NormalUserEntity("email10@email.com", "He Who Must Not Be Named", 777, 420, "user10", "password"));

            // create facility
            FacilityEntity fac = facilityEntitySessionBeanLocal.createNewFacility(new FacilityEntity("USC Bouldering Wall", "NUS Climbing Club", 5, 30, "USC, 2 Sports Drive, Singapore 117288", 10, 18));
            FacilityEntity imaginary = facilityEntitySessionBeanLocal.createNewFacility(new FacilityEntity("Non-NUS Facility", "Nil", 5, 100, "Nil", 0, 23));
            FacilityEntity fac2 = facilityEntitySessionBeanLocal.createNewFacility(new FacilityEntity("USC Squash Court 1", "NUS Squash Club", 5, 30, "USC, 2 Sports Drive, Singapore 117288", 16, 22));

            Date date = new Date();
            date.setMinutes(0);
            date.setHours(12);
            date.setSeconds(0);
            date.setDate(13);
            date.setMonth(3);

            TimeSlotEntity ts = new TimeSlotEntity(date, SlotStatusEnum.UNAVAILABLE, fac);
            timeSlotEntitySessionBeanLocal.createNewTimeSlotEntity(ts, fac.getFacilityId());

            // create booking
//            BookingEntity booking = bookingEntitySessionBeanLocal.createNewBooking(new BookingEntity(SlotStatusEnum.AVAILABLE, date, null, ts));
//            System.out.println("booking first " + booking.getBookingId());
            date.setHours(13);
            TimeSlotEntity ts2 = new TimeSlotEntity(date, SlotStatusEnum.AVAILABLE, fac);
            timeSlotEntitySessionBeanLocal.createNewTimeSlotEntity(ts2, fac.getFacilityId());

            date.setHours(14);
            TimeSlotEntity ts3 = new TimeSlotEntity(date, SlotStatusEnum.AVAILABLE, fac);
            timeSlotEntitySessionBeanLocal.createNewTimeSlotEntity(ts3, fac.getFacilityId());

            date.setHours(15);
            TimeSlotEntity ts4 = new TimeSlotEntity(date, SlotStatusEnum.AVAILABLE, fac);
            timeSlotEntitySessionBeanLocal.createNewTimeSlotEntity(ts4, fac.getFacilityId());

            date.setHours(16);
            TimeSlotEntity ts5 = new TimeSlotEntity(date, SlotStatusEnum.AVAILABLE, fac);
            timeSlotEntitySessionBeanLocal.createNewTimeSlotEntity(ts5, fac.getFacilityId());

            date.setHours(17);
            TimeSlotEntity ts6 = new TimeSlotEntity(date, SlotStatusEnum.AVAILABLE, fac);
            timeSlotEntitySessionBeanLocal.createNewTimeSlotEntity(ts6, fac.getFacilityId());

            date.setDate(14);
            date.setHours(18);
            TimeSlotEntity ts7 = new TimeSlotEntity(date, SlotStatusEnum.UNAVAILABLE, fac2);
            timeSlotEntitySessionBeanLocal.createNewTimeSlotEntity(ts7, fac2.getFacilityId());

            date.setHours(19);
            TimeSlotEntity ts8 = new TimeSlotEntity(date, SlotStatusEnum.AVAILABLE, fac2);
            timeSlotEntitySessionBeanLocal.createNewTimeSlotEntity(ts8, fac2.getFacilityId());

            date.setHours(20);
            TimeSlotEntity ts9 = new TimeSlotEntity(date, SlotStatusEnum.AVAILABLE, fac2);
            timeSlotEntitySessionBeanLocal.createNewTimeSlotEntity(ts9, fac2.getFacilityId());

            // create category
            CategoryEntity cat = categoryEntitySessionBeanLocal.createNewCategoryEntity(new CategoryEntity("Sports"), null);
            CategoryEntity cat2 = categoryEntitySessionBeanLocal.createNewCategoryEntity(new CategoryEntity("Bouldering"), cat.getCategoryId());
            CategoryEntity cat4 = categoryEntitySessionBeanLocal.createNewCategoryEntity(new CategoryEntity("Squash"), cat.getCategoryId());
            categoryEntitySessionBeanLocal.createNewCategoryEntity(new CategoryEntity("Volleyball"), cat.getCategoryId());

            CategoryEntity cat3 = categoryEntitySessionBeanLocal.createNewCategoryEntity(new CategoryEntity("Arts"), null);
            categoryEntitySessionBeanLocal.createNewCategoryEntity(new CategoryEntity("Dance"), cat3.getCategoryId());
            categoryEntitySessionBeanLocal.createNewCategoryEntity(new CategoryEntity("Music"), cat3.getCategoryId());

            // create activity
            ActivityEntity activity = activityEntitySessionBeanLocal.createNewActivity(new ActivityEntity("Activity One", "Activity One Description", 15, new ArrayList<>(), normalUser, new ArrayList<>(), cat2, null, date), cat2.getCategoryId(), ts.getTimeSlotId(), date);
            activity.setActivityOver(Boolean.TRUE);
//            bookingEntitySessionBeanLocal.associateBookingWithActivity(booking.getBookingId(), activity.getActivityId());

            ActivityEntity activity2 = activityEntitySessionBeanLocal.createNewActivity(new ActivityEntity("Activity Two", "Activity Two Description", 4, new ArrayList<>(), normalUser2, new ArrayList<>(), cat4, null, date), cat4.getCategoryId(), ts7.getTimeSlotId(), date);

            CommentEntity comment = new CommentEntity("Will anyone be bringing any equipment?", normalUser, date);
            activityEntitySessionBeanLocal.addComment(comment, activity.getActivityId());

            // signup for activity
            activityEntitySessionBeanLocal.signUpForActivity(activity.getActivityId(), normalUser2.getUserId());
            activityEntitySessionBeanLocal.signUpForActivity(activity.getActivityId(), normalUser3.getUserId());

            activityEntitySessionBeanLocal.signUpForActivity(activity2.getActivityId(), normalUser.getUserId());
            activityEntitySessionBeanLocal.signUpForActivity(activity2.getActivityId(), normalUser3.getUserId());

            System.out.println("Data Initialization Ended");
        } catch (AdminUsernameExistException | CreateNewFacilityException | CreateNewCategoryException | CreateNewTimeSlotException | FacilityNameExistException | UnknownPersistenceException | InputDataValidationException | NormalUserNameExistException | MaxParticipantsExceededException | NormalUserAlreadySignedUpException | NormalUserNotFoundException | ActivityNotFoundException | CategoryNotFoundException | TimeSlotNotFoundException | InsufficientBookingTokensException ex) {
            ex.printStackTrace();
        }

    }

//    public void initializeTimeSlots() {
//        System.out.println("ejb.session.singleton.DataInitSessionBean.initializeTimeSlots()");
//        try {
//            List<TimeSlotEntity> timeSlots;
//
//            List<FacilityEntity> facilities = facilityEntitySessionBeanLocal.retrieveAllFacilities();
//
//            for (FacilityEntity facility : facilities) {
//                Calendar c = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("Asia/Singapore")));
//                c.set(Calendar.MINUTE, 0);
//                c.set(Calendar.SECOND, 0);
//                c.set(Calendar.MILLISECOND, 0);
//                int openingHour = facility.getOpeningHour();
//                int closingHour = facility.getClosingHour();
//
//                for (int count = 0; count <= 7; count++) { // today + 7 days
//                    System.out.println("day " + count);
//                    timeSlots = timeSlotEntitySessionBeanLocal.retrieveTimeSlotsByDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), facility.getFacilityId());
//
//                    if (timeSlots == null || timeSlots.isEmpty()) {
//                        System.out.println("****** Timeslot exist for " + c.get(Calendar.DATE) + "/" + c.get(Calendar.MONTH) + " for " + facility.getFacilityName() + " ******");
//
//                        for (int i = openingHour; i < closingHour; i++) {
//                            c.set(Calendar.HOUR_OF_DAY, i);
//                            TimeSlotEntity ts = new TimeSlotEntity(c.getTime(), SlotStatusEnum.AVAILABLE, facility);
//                            timeSlotEntitySessionBeanLocal.createNewTimeSlotEntity(ts, facility.getFacilityId());
//                            System.out.println("create new timeslot for hour " + i);
//                        }
//                    } else {
//                        System.out.println("****** Timeslot exist for " + c.get(Calendar.DATE) + "/" + c.get(Calendar.MONTH) + " for " + facility.getFacilityName() + " ******");
//
//                    }
//                    c.set(Calendar.HOUR_OF_DAY, 0);
//                    c.add(Calendar.DATE, 1);
//                }
//
//            }
//        } catch (CreateNewTimeSlotException | InputDataValidationException ex) {
//            ex.printStackTrace();
//        }
//    }
}
