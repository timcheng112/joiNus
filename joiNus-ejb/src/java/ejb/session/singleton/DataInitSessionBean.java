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
import entity.CategoryEntity;
import entity.CommentEntity;
import entity.FacilityEntity;
import entity.NormalUserEntity;
import entity.TimeSlotEntity;
import java.util.ArrayList;
import java.util.Date;
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
//            adminEntitySessionBeanLocal.createNewAdmin(new AdminEntity("nusccadmin1", "password", "NUS Climbing Club", Boolean.FALSE));

            // create normal user
            NormalUserEntity normalUser = normalUserEntitySessionBeanLocal.createNewNormalUser(new NormalUserEntity("email@email.com", "MR CODER JEREMY", 420, 20, "user0", "password"));
            NormalUserEntity normalUser2 = normalUserEntitySessionBeanLocal.createNewNormalUser(new NormalUserEntity("email1@email.com", "Sir Elton John", 200, 20, "user1", "password"));
            NormalUserEntity normalUser3 = normalUserEntitySessionBeanLocal.createNewNormalUser(new NormalUserEntity("JohnWong@gmail.com", "Prof Tan Wee Kek", 6969, 20, "user2", "password"));
            NormalUserEntity normalUser4 = normalUserEntitySessionBeanLocal.createNewNormalUser(new NormalUserEntity("email3@email.com", "Hedgehog", 421, 20, "user3", "password"));
            normalUserEntitySessionBeanLocal.createNewNormalUser(new NormalUserEntity("email4@email.com", "Prof Lu Wei Qian", -99, 20, "user4", "password"));
            NormalUserEntity normalUser5 = normalUserEntitySessionBeanLocal.createNewNormalUser(new NormalUserEntity("email5@email.com", "Joe Biden", 46, 20, "user5", "password"));
            normalUserEntitySessionBeanLocal.createNewNormalUser(new NormalUserEntity("email6@email.com", "Joe Mama", 902, 20, "user6", "password"));
            normalUserEntitySessionBeanLocal.createNewNormalUser(new NormalUserEntity("email7@email.com", "Mrs Ligma", 738, 20, "user7", "password"));
            normalUserEntitySessionBeanLocal.createNewNormalUser(new NormalUserEntity("email8@email.com", "Prof Chong Ket Fah", 3000, 20, "user8", "password"));
            NormalUserEntity normalUser6 = normalUserEntitySessionBeanLocal.createNewNormalUser(new NormalUserEntity("email9@email.com", "Prof Zhou Li Feng", 3001, 20, "user9", "password"));
            NormalUserEntity normalUser7 = normalUserEntitySessionBeanLocal.createNewNormalUser(new NormalUserEntity("email10@email.com", "He Who Must Not Be Named", 777, 20, "user10", "password"));

            // create facility
            FacilityEntity fac = facilityEntitySessionBeanLocal.createNewFacility(new FacilityEntity("USC Bouldering Wall", "NUS Climbing Club", 5, 20, "USC, 2 Sports Drive, Singapore 117288", 10, 23));
            FacilityEntity imaginary = facilityEntitySessionBeanLocal.createNewFacility(new FacilityEntity("Non-NUS Facility", "NUS", 5, 100, "Nil", 0, 23));
            FacilityEntity fac2 = facilityEntitySessionBeanLocal.createNewFacility(new FacilityEntity("UTown Dance Studio 1", "NUS Dance Club", 5, 25, "Stephen Riady Centre, 2 College Ave West, Singapore 138607", 16, 22));
            FacilityEntity fac3 = facilityEntitySessionBeanLocal.createNewFacility(new FacilityEntity("USC Gym", "NUS", 2, 10, "USC, 2 Sports Drive, Singapore 117288", 8, 22));
            FacilityEntity fac4 = facilityEntitySessionBeanLocal.createNewFacility(new FacilityEntity("UTown Jam Room 1", "NUS", 4, 6, "Stephen Riady Centre, 2 College Ave West, Singapore 138607", 12, 20));

            Date date = new Date();
            date.setMinutes(0);
            date.setSeconds(0);
            // NEED TO CHANGE THIS FOR DATA INIT
            date.setHours(16);
            date.setDate(16);
            date.setMonth(3); // need to - 1 from current date
            // NEED TO CHANGE THIS FOR DATA INIT

            TimeSlotEntity ts = new TimeSlotEntity(date, SlotStatusEnum.UNAVAILABLE, fac);
            timeSlotEntitySessionBeanLocal.createNewTimeSlotEntity(ts, fac.getFacilityId());

            // NEED TO CHANGE THIS FOR DATA INIT
            date.setHours(12);
            // NEED TO CHANGE THIS FOR DATA INIT
            date.setDate(17);

            // create booking
//            BookingEntity booking = bookingEntitySessionBeanLocal.createNewBooking(new BookingEntity(SlotStatusEnum.AVAILABLE, date, null, ts));
//            System.out.println("booking first " + booking.getBookingId());
//            date.setHours(date.getHours() + 1);
            // timeslots for bouldering wall today - fac
            TimeSlotEntity ts2 = new TimeSlotEntity(date, SlotStatusEnum.AVAILABLE, fac);
            timeSlotEntitySessionBeanLocal.createNewTimeSlotEntity(ts2, fac.getFacilityId()); //12
            date.setHours(date.getHours() + 1);
            TimeSlotEntity ts3 = new TimeSlotEntity(date, SlotStatusEnum.AVAILABLE, fac);
            timeSlotEntitySessionBeanLocal.createNewTimeSlotEntity(ts3, fac.getFacilityId()); //13
            date.setHours(date.getHours() + 1);
            TimeSlotEntity ts4 = new TimeSlotEntity(date, SlotStatusEnum.AVAILABLE, fac);
            timeSlotEntitySessionBeanLocal.createNewTimeSlotEntity(ts4, fac.getFacilityId()); //14
            date.setHours(date.getHours() + 1);
            TimeSlotEntity ts5 = new TimeSlotEntity(date, SlotStatusEnum.AVAILABLE, fac);
            timeSlotEntitySessionBeanLocal.createNewTimeSlotEntity(ts5, fac.getFacilityId()); //15
            date.setHours(date.getHours() + 1);
            TimeSlotEntity ts6 = new TimeSlotEntity(date, SlotStatusEnum.AVAILABLE, fac);
            timeSlotEntitySessionBeanLocal.createNewTimeSlotEntity(ts6, fac.getFacilityId()); //16

            // timeslots for dance tomorrow - fac2
            date.setDate(date.getDate() + 1);
            date.setHours(18);
            TimeSlotEntity ts7 = new TimeSlotEntity(date, SlotStatusEnum.UNAVAILABLE, fac2);
            timeSlotEntitySessionBeanLocal.createNewTimeSlotEntity(ts7, fac2.getFacilityId());
            date.setHours(date.getHours() + 1);
            TimeSlotEntity ts8 = new TimeSlotEntity(date, SlotStatusEnum.AVAILABLE, fac2);
            timeSlotEntitySessionBeanLocal.createNewTimeSlotEntity(ts8, fac2.getFacilityId());
            date.setHours(date.getHours() + 1);
            TimeSlotEntity ts9 = new TimeSlotEntity(date, SlotStatusEnum.AVAILABLE, fac2);
            timeSlotEntitySessionBeanLocal.createNewTimeSlotEntity(ts9, fac2.getFacilityId());

            // timeslots for gym today - fac3
            date.setDate(date.getDate() - 1);
            date.setHours(15);
            TimeSlotEntity ts10 = new TimeSlotEntity(date, SlotStatusEnum.AVAILABLE, fac3);
            timeSlotEntitySessionBeanLocal.createNewTimeSlotEntity(ts10, fac3.getFacilityId());
            date.setHours(date.getHours() + 1);
            TimeSlotEntity ts11 = new TimeSlotEntity(date, SlotStatusEnum.AVAILABLE, fac3);
            timeSlotEntitySessionBeanLocal.createNewTimeSlotEntity(ts11, fac3.getFacilityId());
            TimeSlotEntity ts12 = new TimeSlotEntity(date, SlotStatusEnum.AVAILABLE, fac3);
            timeSlotEntitySessionBeanLocal.createNewTimeSlotEntity(ts12, fac3.getFacilityId());
            
            // timeslots for music today - fac4
            date.setHours(12);
            TimeSlotEntity ts13 = new TimeSlotEntity(date, SlotStatusEnum.AVAILABLE, fac4);
            timeSlotEntitySessionBeanLocal.createNewTimeSlotEntity(ts13, fac4.getFacilityId());
            date.setHours(date.getHours() + 1);
            TimeSlotEntity ts14 = new TimeSlotEntity(date, SlotStatusEnum.AVAILABLE, fac4);
            timeSlotEntitySessionBeanLocal.createNewTimeSlotEntity(ts14, fac4.getFacilityId());
            TimeSlotEntity ts15 = new TimeSlotEntity(date, SlotStatusEnum.AVAILABLE, fac4);
            timeSlotEntitySessionBeanLocal.createNewTimeSlotEntity(ts15, fac4.getFacilityId());

            // create category
            CategoryEntity cat = categoryEntitySessionBeanLocal.createNewCategoryEntity(new CategoryEntity("Sports"), null);
            CategoryEntity cat2 = categoryEntitySessionBeanLocal.createNewCategoryEntity(new CategoryEntity("Bouldering"), cat.getCategoryId());
            CategoryEntity cat4 = categoryEntitySessionBeanLocal.createNewCategoryEntity(new CategoryEntity("Squash"), cat.getCategoryId());
            CategoryEntity cat7 = categoryEntitySessionBeanLocal.createNewCategoryEntity(new CategoryEntity("Gym"), cat.getCategoryId());

            CategoryEntity cat3 = categoryEntitySessionBeanLocal.createNewCategoryEntity(new CategoryEntity("Arts"), null);
            CategoryEntity cat5 = categoryEntitySessionBeanLocal.createNewCategoryEntity(new CategoryEntity("Dance"), cat3.getCategoryId());
            CategoryEntity cat6 = categoryEntitySessionBeanLocal.createNewCategoryEntity(new CategoryEntity("Music"), cat3.getCategoryId());

            Date creationDate = new Date();
            // create activity
            // boulder
            ActivityEntity activity = activityEntitySessionBeanLocal.createNewActivity(new ActivityEntity("Activity One", "Activity One Description - Bouldering", 15, new ArrayList<>(), normalUser, new ArrayList<>(), cat2, null, creationDate), cat2.getCategoryId(), ts.getTimeSlotId(), null);
            activity.setActivityOver(Boolean.TRUE);
            //dance
            ActivityEntity activity2 = activityEntitySessionBeanLocal.createNewActivity(new ActivityEntity("Activity Two", "Activity Two Description - Dance Practice 1", 25, new ArrayList<>(), normalUser2, new ArrayList<>(), cat5, null, creationDate), cat5.getCategoryId(), ts7.getTimeSlotId(), null);
            //boulder past
            ActivityEntity activity3 = activityEntitySessionBeanLocal.createNewActivity(new ActivityEntity("Activity Three", "Activity Three Description - Bouldering Cohesion Group 1", 20, new ArrayList<>(), normalUser, new ArrayList<>(), cat2, null, creationDate), cat2.getCategoryId(), ts4.getTimeSlotId(), null); // this one just passed
            //boulder coming
            ActivityEntity activity4 = activityEntitySessionBeanLocal.createNewActivity(new ActivityEntity("Activity Four", "Activity Four Description - Bouldering Cohesion Group 2", 20, new ArrayList<>(), normalUser, new ArrayList<>(), cat2, null, creationDate), cat2.getCategoryId(), ts5.getTimeSlotId(), null);
            //dance
            ActivityEntity activity5 = activityEntitySessionBeanLocal.createNewActivity(new ActivityEntity("Activity Five", "Activity Five Description - Dance Practice 2", 25, new ArrayList<>(), normalUser2, new ArrayList<>(), cat5, null, creationDate), cat5.getCategoryId(), ts8.getTimeSlotId(), null);
            //music
            ActivityEntity activity6 = activityEntitySessionBeanLocal.createNewActivity(new ActivityEntity("Activity Six", "Activity Six Description - Music Jams", 6, new ArrayList<>(), normalUser4, new ArrayList<>(), cat6, null, creationDate), cat6.getCategoryId(), ts14.getTimeSlotId(), null);
            // music
            ActivityEntity activity7 = activityEntitySessionBeanLocal.createNewActivity(new ActivityEntity("Activity Seven", "Activity Seven Description - Jam-athon", 4, new ArrayList<>(), normalUser5, new ArrayList<>(), cat6, null, creationDate), cat6.getCategoryId(), ts15.getTimeSlotId(), null);
            // gym
            ActivityEntity activity8 = activityEntitySessionBeanLocal.createNewActivity(new ActivityEntity("Activity Eight", "Activity Eight Description - GYM GANG", 12, new ArrayList<>(), normalUser6, new ArrayList<>(), cat7, null, creationDate), cat7.getCategoryId(), ts10.getTimeSlotId(), null);

            CommentEntity comment = new CommentEntity("Will anyone be bringing chalk?", normalUser, date);
            activityEntitySessionBeanLocal.addComment(comment, activity.getActivityId());

            // signup for activity
            activityEntitySessionBeanLocal.signUpForActivity(activity.getActivityId(), normalUser2.getUserId());
            activityEntitySessionBeanLocal.signUpForActivity(activity.getActivityId(), normalUser3.getUserId());
            activityEntitySessionBeanLocal.signUpForActivity(activity.getActivityId(), normalUser4.getUserId());

            activityEntitySessionBeanLocal.signUpForActivity(activity2.getActivityId(), normalUser.getUserId());
            activityEntitySessionBeanLocal.signUpForActivity(activity2.getActivityId(), normalUser3.getUserId());
            activityEntitySessionBeanLocal.signUpForActivity(activity2.getActivityId(), normalUser7.getUserId());

            activityEntitySessionBeanLocal.signUpForActivity(activity3.getActivityId(), normalUser5.getUserId());
            activityEntitySessionBeanLocal.signUpForActivity(activity3.getActivityId(), normalUser6.getUserId());
            activityEntitySessionBeanLocal.signUpForActivity(activity4.getActivityId(), normalUser7.getUserId());

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
