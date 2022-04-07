/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.FacilityEntity;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import javax.annotation.PostConstruct;

/**
 *
 * @author wongs
 */
@Named(value = "viewFacilityManagedBean")
@ViewScoped
public class ViewFacilityManagedBean implements Serializable {

    FacilityEntity facilityEntityToView;
    /**
     * Creates a new instance of ViewFacilityManagedBean
     */
    public ViewFacilityManagedBean() {
        facilityEntityToView = new FacilityEntity();
    }

    @PostConstruct
    public void postConstruct()
    {
        
    }
    public FacilityEntity getFacilityEntityToView() {
        return facilityEntityToView;
    }

    public void setFacilityEntityToView(FacilityEntity facilityEntityToView) {
        this.facilityEntityToView = facilityEntityToView;
    }
    
}
