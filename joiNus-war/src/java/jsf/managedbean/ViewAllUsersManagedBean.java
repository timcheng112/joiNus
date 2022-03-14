/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.AdminEntitySessionBeanLocal;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author User
 */
@Named(value = "viewAllUsersManagedBean")
@RequestScoped
public class ViewAllUsersManagedBean {

    @EJB
    private AdminEntitySessionBeanLocal adminEntitySessionBeanLocal;

    /**
     * Creates a new instance of ViewAllUsersManagedBean
     */
    @PostConstruct
    public void postConstruct() {
    }

    public ViewAllUsersManagedBean() {
    }

}
