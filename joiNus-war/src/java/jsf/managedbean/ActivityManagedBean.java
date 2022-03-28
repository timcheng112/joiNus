/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;

/**
 *
 * @author User
 */
@Named(value = "activityManagedBean")
@ViewScoped
public class ActivityManagedBean implements Serializable {

    /**
     * Creates a new instance of ActivityManagedBean
     */
    public ActivityManagedBean() {
    }
    
}
