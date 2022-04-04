/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.enums.SlotStatusEnum;
import ejb.session.stateless.AdminEntitySessionBeanLocal;
import ejb.session.stateless.CategoryEntitySessionBeanLocal;
import ejb.session.stateless.FacilityEntitySessionBeanLocal;
import ejb.session.stateless.TimeSlotEntitySessionBeanLocal;
import entity.AdminEntity;
import entity.CategoryEntity;
import entity.FacilityEntity;
import entity.TimeSlotEntity;
import java.io.Console;
import java.util.Calendar;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.AdminNotFoundException;
import util.exception.AdminUsernameExistException;
import util.exception.CreateNewCategoryException;
import util.exception.CreateNewFacilityException;
import util.exception.CreateNewTimeSlotException;
import util.exception.FacilityNameExistException;
import util.exception.InputDataValidationException;
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

            // create facility
            FacilityEntity fac = facilityEntitySessionBeanLocal.createNewFacility(new FacilityEntity("USC Bouldering Wall", "USC", 5, 30, "2 Sports Drive, Singapore 117288"));

            // create timeslots from 10am to 2pm
            Date date = new Date();
//            Calendar c = Calendar.getInstance();
            date.setHours(10);
            date.setMinutes(0);
            date.setSeconds(0);
//            c.setTime(date);
//            c.set(c.getTime().getYear(), c.getTime().getMonth(), c.getTime().getDate(), c.getTime().getHours(), 0, 0);
            TimeSlotEntity ts = new TimeSlotEntity(date, SlotStatusEnum.AVAILABLE, fac);
            timeSlotEntitySessionBeanLocal.createNewTimeSlotEntity(ts, fac.getFacilityId());

            date.setHours(11);
            ts = new TimeSlotEntity(date, SlotStatusEnum.AVAILABLE, fac);
            timeSlotEntitySessionBeanLocal.createNewTimeSlotEntity(ts, fac.getFacilityId());
            
            date.setHours(12);
            ts = new TimeSlotEntity(date, SlotStatusEnum.AVAILABLE, fac);
            timeSlotEntitySessionBeanLocal.createNewTimeSlotEntity(ts, fac.getFacilityId());
            
            date.setHours(13);
            ts = new TimeSlotEntity(date, SlotStatusEnum.AVAILABLE, fac);
            timeSlotEntitySessionBeanLocal.createNewTimeSlotEntity(ts, fac.getFacilityId());
            
            date.setHours(14);
            ts = new TimeSlotEntity(date, SlotStatusEnum.AVAILABLE, fac);
            timeSlotEntitySessionBeanLocal.createNewTimeSlotEntity(ts, fac.getFacilityId());

            // create category
            CategoryEntity cat = categoryEntitySessionBeanLocal.createNewCategoryEntity(new CategoryEntity("Sports"), null);
            categoryEntitySessionBeanLocal.createNewCategoryEntity(new CategoryEntity("Bouldering"), cat.getCategoryId());

            System.out.println("Data Initialization Ended");
        } catch (AdminUsernameExistException | CreateNewFacilityException | CreateNewCategoryException | CreateNewTimeSlotException | FacilityNameExistException | UnknownPersistenceException | InputDataValidationException ex) {
            ex.printStackTrace();
        }

    }

}
