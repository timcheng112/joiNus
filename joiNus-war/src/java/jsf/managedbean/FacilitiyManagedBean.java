/*

NOT DONE NOT DONE NOT DONE NOT DONE


 */
package jsf.managedbean;

import ejb.session.stateless.FacilityEntitySessionBeanLocal;
import entity.FacilityEntity;
import entity.TimeSlotEntity;
import java.awt.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import util.exception.FacilityNotFoundException;

/**
 *
 * @author wongs
 */
@Named(value = "facilitiyManagedBean")
@ViewScoped
public class FacilitiyManagedBean implements Serializable {

    @EJB
    private FacilityEntitySessionBeanLocal facilityEntitySessionBeanLocal;
    
    private FacilityEntity newFacilityEntity;
    private List<TimeSlotEntity> timeSlots;
    private List<FacilityEntity> facilityEntities;
    
    /**
     * Creates a new instance of FacilitiyManagedBean
     */
    public FacilitiyManagedBean() {
        newFacilityEntity = new FacilityEntity();
    }
    
    /*
    Create New Facility
    Update Facility
    View All Facilities
    View Facility Details
    */
    
    @PostConstruct
    public void postConstruct(){
        setFacilityEntities(facilityEntitySessionBeanLocal.retrieveAllFacilities());
        
        for (FacilityEntity facility : facilityEntities) {
            facility.getTimeSlots();
        }
        
//        try {
//            setTimeSlots(facilityEntitySessionBeanLocal.retrieveTimeSlotsByFacility(getNewFacilityEntity().getFacilityId()));
//        } catch (FacilityNotFoundException ex) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Facility Entry: " + ex.getMessage(), null));
//        }
    }
    /*
    public void createNewFacility(ActionEvent event)
    {
        if()
    }
    */

    /**
     * @return the newFacilityEntity
     */
    public FacilityEntity getNewFacilityEntity() {
        return newFacilityEntity;
    }

    /**
     * @param newFacilityEntity the newFacilityEntity to set
     */
    public void setNewFacilityEntity(FacilityEntity newFacilityEntity) {
        this.newFacilityEntity = newFacilityEntity;
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
     * @return the facilityEntities
     */
    public List<FacilityEntity> getFacilityEntities() {
        return facilityEntities;
    }

    /**
     * @param facilityEntities the facilityEntities to set
     */
    public void setFacilityEntities(List<FacilityEntity> facilityEntities) {
        this.facilityEntities = facilityEntities;
    }
}
