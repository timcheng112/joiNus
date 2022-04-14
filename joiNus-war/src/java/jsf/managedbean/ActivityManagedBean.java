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
import ejb.session.stateless.TimeSlotEntitySessionBeanLocal;
import entity.ActivityEntity;
import entity.AdminEntity;
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
import util.exception.TimeSlotNotFoundException;

/**
 *
 * @author User
 */
@Named(value = "activityManagedBean")
@ViewScoped
public class ActivityManagedBean implements Serializable {

    @EJB(name = "AdminEntitySessionBeanLocal")
    private AdminEntitySessionBeanLocal adminEntitySessionBeanLocal;

    @Inject
    private AdminLoginManagedBean adminLoginManagedBean;

    @EJB(name = "BookingEntitySessionBeanLocal")
    private BookingEntitySessionBeanLocal bookingEntitySessionBeanLocal;

    @EJB(name = "TimeSlotEntitySessionBeanLocal")
    private TimeSlotEntitySessionBeanLocal timeSlotEntitySessionBeanLocal;

    @EJB(name = "ActivityEntitySessionBeanLocal")
    private ActivityEntitySessionBeanLocal activityEntitySessionBeanLocal;

    /**
     * Creates a new instance of ActivityManagedBean
     */
    private List<ActivityEntity> activityEntities;
    private List<ActivityEntity> ongoingActivityEntities;
    private List<ActivityEntity> filteredActivityEntities;

    private ActivityEntity activityEntityToView;

    private Date editedActivityDate;

    private Integer editedActivityTime;

    private List<TimeSlotEntity> timeSlots;

    private Long selectedTimeSlotId;

    public ActivityManagedBean() {
        activityEntityToView = new ActivityEntity();
        activityEntities = new ArrayList<>();
        ongoingActivityEntities = new ArrayList<>();
        filteredActivityEntities = new ArrayList<>();
        timeSlots = new ArrayList<>();
    }

    @PostConstruct
    public void postConstruct() {
        try {
            activityEntities = activityEntitySessionBeanLocal.retrieveAllActivities();
            AdminEntity admin = adminEntitySessionBeanLocal.retrieveAdminByUsername(adminLoginManagedBean.getUsername());
            ongoingActivityEntities = activityEntitySessionBeanLocal.retrieveAllOngoingActivities(admin.getClub());
        } catch (AdminNotFoundException ex) {
            //
        }
    }

//    public void viewActivityDetails(ActionEvent event) throws IOException {
//        Long activityIdToView = (Long) event.getComponent().getAttributes().get("activityId");
//        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("activityIdToView", activityIdToView);
//        FacesContext.getCurrentInstance().getExternalContext().redirect("viewActivityDetails.xhtml");
//    }
    public void initialiseState() {
        editedActivityDate = null;
        editedActivityTime = null;
        timeSlots = new ArrayList<>();
        selectedTimeSlotId = null;
        setActivityEntities(activityEntitySessionBeanLocal.retrieveAllActivities());
        try {
            AdminEntity admin = adminEntitySessionBeanLocal.retrieveAdminByUsername(adminLoginManagedBean.getUsername());
            setOngoingActivityEntities(activityEntitySessionBeanLocal.retrieveAllOngoingActivities(admin.getClub()));
        } catch (AdminNotFoundException ex) {
            //
        }
    }

    public void editActivityDate(ActionEvent event) throws IOException {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.DATE, getEditedActivityDate().getDate());
        c.set(Calendar.MONTH, getEditedActivityDate().getMonth()); // month - 1
        c.set(Calendar.HOUR_OF_DAY, getEditedActivityTime());
        setEditedActivityDate(c.getTime());
    }

    public void deleteActivity(ActionEvent event) {
        try {
            ActivityEntity activityEntityToDelete = (ActivityEntity) event.getComponent().getAttributes().get("activityEntityToDelete");
            activityEntitySessionBeanLocal.deleteActivity(activityEntityToDelete.getActivityId());

            activityEntities.remove(activityEntityToDelete);

            if (filteredActivityEntities != null) {
                filteredActivityEntities.remove(activityEntityToDelete);
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Activity deleted successfully", null));
        } catch (ActivityNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting activity: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public void retrieveTimeslots(ActionEvent event) {
        System.out.println("edited activity date is");
        System.out.println(editedActivityDate.getDate());
        List<TimeSlotEntity> retrievedTimeSlots = timeSlotEntitySessionBeanLocal.retrieveTimeSlotsByDate(editedActivityDate.getYear() + 1900, editedActivityDate.getMonth(), editedActivityDate.getDate() + 1, activityEntityToView.getBooking().getTimeSlot().getFacility().getFacilityId());
        List<TimeSlotEntity> availTimeSlots = new ArrayList<>();
        if (retrievedTimeSlots != null) {
            for (TimeSlotEntity timeSlot : retrievedTimeSlots) {
                System.out.println(timeSlot.getStatus());
                if (timeSlot.getStatus() == SlotStatusEnum.AVAILABLE) {
                    availTimeSlots.add(timeSlot);
                }
            }
        }
        setTimeSlots(availTimeSlots);
        System.out.println(availTimeSlots);
    }

    public void updateActivityTimeSlot(ActionEvent event) {
        try {
            bookingEntitySessionBeanLocal.updateBookingTiming(activityEntityToView.getBooking().getBookingId(), selectedTimeSlotId);
            initialiseState();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Booking Date & Time updated successfully", null));
        } catch (BookingNotFoundException | TimeSlotNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public List<ActivityEntity> getActivityEntities() {
        return activityEntities;
    }

    public void setActivityEntities(List<ActivityEntity> activityEntities) {
        this.activityEntities = activityEntities;
    }

    public List<ActivityEntity> getFilteredActivityEntities() {
        return filteredActivityEntities;
    }

    public void setFilteredActivityEntities(List<ActivityEntity> filteredActivityEntities) {
        this.filteredActivityEntities = filteredActivityEntities;
    }

    /**
     * @return the activityEntityToView
     */
    public ActivityEntity getActivityEntityToView() {
        return activityEntityToView;
    }

    /**
     * @param activityEntityToView the activityEntityToView to set
     */
    public void setActivityEntityToView(ActivityEntity activityEntityToView) {
        this.activityEntityToView = activityEntityToView;
    }

    /**
     * @return the editedActivityDate
     */
    public Date getEditedActivityDate() {
        return editedActivityDate;
    }

    /**
     * @param editedActivityDate the editedActivityDate to set
     */
    public void setEditedActivityDate(Date editedActivityDate) {
        this.editedActivityDate = editedActivityDate;
    }

    /**
     * @return the editedActivityTime
     */
    public Integer getEditedActivityTime() {
        return editedActivityTime;
    }

    /**
     * @param editedActivityTime the editedActivityTime to set
     */
    public void setEditedActivityTime(Integer editedActivityTime) {
        this.editedActivityTime = editedActivityTime;
    }

    /**
     * @return the timeSlots
     */
    public List<TimeSlotEntity> getTimeSlots() {
        return timeSlots;
    }

    /**
     * @param timeSlots the timeSlots to set
     */
    public void setTimeSlots(List<TimeSlotEntity> timeSlots) {
        this.timeSlots = timeSlots;
    }

    /**
     * @return the selectedTimeSlotId
     */
    public Long getSelectedTimeSlotId() {
        return selectedTimeSlotId;
    }

    /**
     * @param selectedTimeSlotId the selectedTimeSlotId to set
     */
    public void setSelectedTimeSlotId(Long selectedTimeSlotId) {
        this.selectedTimeSlotId = selectedTimeSlotId;
    }

    /**
     * @return the ongoingActivityEntities
     */
    public List<ActivityEntity> getOngoingActivityEntities() {
        return ongoingActivityEntities;
    }

    /**
     * @param ongoingActivityEntities the ongoingActivityEntities to set
     */
    public void setOngoingActivityEntities(List<ActivityEntity> ongoingActivityEntities) {
        this.ongoingActivityEntities = ongoingActivityEntities;
    }

}
