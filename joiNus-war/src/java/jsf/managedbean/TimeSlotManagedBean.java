/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.enums.SlotStatusEnum;
import ejb.session.stateless.FacilityEntitySessionBeanLocal;
import ejb.session.stateless.TimeSlotEntitySessionBeanLocal;
import entity.FacilityEntity;
import entity.TimeSlotEntity;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import util.exception.CreateNewTimeSlotException;
import util.exception.FacilityNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.TimeSlotNotFoundException;
import util.exception.UpdateTimeSlotException;

/**
 *
 * @author wongs
 */
@Named(value = "timeSlotManagedBean")
@ViewScoped
public class TimeSlotManagedBean implements Serializable {

    @EJB(name = "TimeSlotEntitySessionBeanLocal")
    private TimeSlotEntitySessionBeanLocal timeSlotEntitySessionBeanLocal;

    @EJB(name = "FacilityEntitySessionBeanLocal")
    private FacilityEntitySessionBeanLocal facilityEntitySessionBeanLocal;

    private FacilityEntity currentFacility;
    private Long currFacilityId;
    private List<Integer> possibleTimeSlots;
    private Date selectedDate;
    private List<TimeSlotEntity> currTimeslots;
    private List<Integer> timeSlotsToAdd;
    private TimeSlotEntity timeSlotToEdit;
    private List<SlotStatusEnum> enums;

    /**
     * Creates a new instance of TimeSlotManagedBean
     */
    public TimeSlotManagedBean() {
        enums = new ArrayList<>();
        enums.add(SlotStatusEnum.AVAILABLE);
        enums.add(SlotStatusEnum.CANCELLED);
        enums.add(SlotStatusEnum.UNAVAILABLE);
        currentFacility = null;
        possibleTimeSlots = new ArrayList<>();
        currTimeslots = new ArrayList<>();
        timeSlotsToAdd = new ArrayList<>();
    }

    @PostConstruct
    public void postConstruct() {
        try {
            currFacilityId = Long.valueOf(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("currFacilityId"));
            currentFacility = facilityEntitySessionBeanLocal.retrieveFacilityByFacilityId(currFacilityId);
        } catch (NumberFormatException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Facility ID not provided", null));
        } catch (FacilityNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Facility " + currFacilityId + " not found", null));
        }
        int start = currentFacility.getOpeningHour();
        int end = currentFacility.getClosingHour();
        while (start < end) {
            possibleTimeSlots.add(start);
            start++;
        }
    }

    public void addTimeSlots() {
        System.out.println("addTimeSlots: " + timeSlotsToAdd);
        for (Integer i : timeSlotsToAdd) {
            Boolean alreadyExists = false;
            System.err.println("addtimeslots- currentTimeslot = " + currTimeslots);
            if (!currTimeslots.isEmpty()) {
                for (TimeSlotEntity j : currTimeslots) {
                    if (j.getTimeSlotTime().getHours() == i) {
                        alreadyExists = true;
                    }
                }
            }
            if (alreadyExists){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Timeslot Already Exists for " + i + "00", null));
            }
            if (!alreadyExists) {
                System.out.println("Selected Date:" + selectedDate);
                selectedDate.setHours(i);
                try {
                    timeSlotEntitySessionBeanLocal.createNewTimeSlotEntity(new TimeSlotEntity(selectedDate, SlotStatusEnum.AVAILABLE, currentFacility), currFacilityId);
                } catch (CreateNewTimeSlotException ex) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in creating timeslot", null));
                } catch (InputDataValidationException ex) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in creating timeslot, bad input", null));
                }
            }
        }
        setDatesTimeslots();
    }
    
    public void editTimeSlotStatus() {
        try {
            timeSlotEntitySessionBeanLocal.updateTimeSlot(timeSlotToEdit);
        } catch (TimeSlotNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in editing timeslot", null));
        } catch (InputDataValidationException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in editing timeslot", null));
        } catch (UpdateTimeSlotException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in editing timeslot", null));
        }
    }

    public void setDatesTimeslots() {
        //fill the currTimeslots array with the ones selected by datepicker, display the table.
        //retrieveTimeSlotsByDate(int year, int month, int date, Long facilityId)
        int day = selectedDate.getDate() +1 ; //xd wtf
        System.out.println("day = " + day);
        int month = selectedDate.getMonth();
        int year = selectedDate.getYear() + 1900;
        System.out.println("selected date" + selectedDate);
        
        List<TimeSlotEntity> temp = timeSlotEntitySessionBeanLocal.retrieveTimeSlotsByDate(year, month, day, currFacilityId);
        if (temp == null){
            setCurrTimeslots(new ArrayList<>());
        } else {
            setCurrTimeslots(temp);
        }
        for (TimeSlotEntity t: currTimeslots){
            System.out.println("timeslot time = " + t.getTimeSlotTime());
        }
        System.out.println("SetDatesTImeslots" + currTimeslots);
    }

    public FacilityEntity getCurrentFacility() {
        return currentFacility;
    }

    public void setCurrentFacility(FacilityEntity currentFacility) {
        this.currentFacility = currentFacility;
    }

    /**
     * @return the possibleTimeSlots
     */
    public List<Integer> getPossibleTimeSlots() {
        return possibleTimeSlots;
    }

    /**
     * @param possibleTimeSlots the possibleTimeSlots to set
     */
    public void setPossibleTimeSlots(List<Integer> possibleTimeSlots) {
        this.possibleTimeSlots = possibleTimeSlots;
    }

    /**
     * @return the selectedDate
     */
    public Date getSelectedDate() {
        return selectedDate;
    }

    /**
     * @param selectedDate the selectedDate to set
     */
    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = selectedDate;
    }

    /**
     * @return the currTimeslots
     */
    public List<TimeSlotEntity> getCurrTimeslots() {
        return currTimeslots;
    }

    /**
     * @param currTimeslots the currTimeslots to set
     */
    public void setCurrTimeslots(List<TimeSlotEntity> currTimeslots) {
        this.currTimeslots = currTimeslots;
    }

    /**
     * @return the timeSlotsToAdd
     */
    public List<Integer> getTimeSlotsToAdd() {
        return timeSlotsToAdd;
    }

    /**
     * @param timeSlotsToAdd the timeSlotsToAdd to set
     */
    public void setTimeSlotsToAdd(List<Integer> timeSlotsToAdd) {
        this.timeSlotsToAdd = timeSlotsToAdd;
    }

    public List<SlotStatusEnum> getEnums() {
        return enums;
    }

    public void setEnums(List<SlotStatusEnum> enums) {
        this.enums = enums;
    }

    /**
     * @return the timeSlotToEdit
     */
    public TimeSlotEntity getTimeSlotToEdit() {
        return timeSlotToEdit;
    }

    /**
     * @param timeSlotToEdit the timeSlotToEdit to set
     */
    public void setTimeSlotToEdit(TimeSlotEntity timeSlotToEdit) {
        this.timeSlotToEdit = timeSlotToEdit;
    }
}
