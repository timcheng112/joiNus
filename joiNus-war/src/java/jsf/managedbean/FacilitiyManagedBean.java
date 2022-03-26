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
import javax.annotation.PostConstruct;
import javax.ejb.EJB;

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
        //timeSlots = timeSlotEntitySessionBeanLocal.retrieveAllTimeSlots();
    }
    /*
    public void createNewFacility(ActionEvent event)
    {
        if()
    }
    */
}
