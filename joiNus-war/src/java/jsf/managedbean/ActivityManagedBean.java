/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.ActivityEntitySessionBeanLocal;
import entity.ActivityEntity;
import java.io.IOException;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import util.exception.ActivityNotFoundException;

/**
 *
 * @author User
 */
@Named(value = "activityManagedBean")
@ViewScoped
public class ActivityManagedBean implements Serializable {

    @EJB(name = "ActivityEntitySessionBeanLocal")
    private ActivityEntitySessionBeanLocal activityEntitySessionBeanLocal;

    /**
     * Creates a new instance of ActivityManagedBean
     */
    private List<ActivityEntity> activityEntities;
    private List<ActivityEntity> filteredActivityEntities;

    private ActivityEntity activityEntityToView;

    public ActivityManagedBean() {
        activityEntityToView = new ActivityEntity();
    }

    @PostConstruct
    public void postConstruct() {
        activityEntities = activityEntitySessionBeanLocal.retrieveAllActivities();
    }
    
    public void viewActivityDetails(ActionEvent event) throws IOException
    {
        Long activityIdToView = (Long)event.getComponent().getAttributes().get("activityId");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("activityIdToView", activityIdToView);
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewActivityDetails.xhtml");
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

}
