package jsf.managedbean;


import ejb.session.stateless.AdminEntitySessionBeanLocal;
import entity.AdminEntity;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import util.exception.AdminUsernameExistException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

@Named
@ViewScoped

public class AdminManagementManagedBean implements Serializable {

    @EJB
    private AdminEntitySessionBeanLocal adminEntitySessionBeanLocal;

    private AdminEntity newAdminEntity;
    private List<AdminEntity> adminEntities;

    public AdminManagementManagedBean() {
        newAdminEntity = new AdminEntity();
        newAdminEntity.setIsSuperAdmin(Boolean.FALSE);
    }

    @PostConstruct
    public void postConstruct() {
        adminEntities = adminEntitySessionBeanLocal.retrieveAllAdmins();

    }

    public void createNewAdmin(ActionEvent event) {
        try {
            AdminEntity admin = adminEntitySessionBeanLocal.createNewAdmin(newAdminEntity);
            adminEntities.add(admin);
            
            newAdminEntity = new AdminEntity();

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New admin created successfully (Admin ID: " + admin.getUserId() + ")", null));
        } catch (InputDataValidationException | AdminUsernameExistException | UnknownPersistenceException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new customer: " + ex.getMessage(), null));
        }
    }

    public AdminEntity getNewAdminEntity() {
        return newAdminEntity;
    }

    public void setNewAdminEntity(AdminEntity newAdminEntity) {
        this.newAdminEntity = newAdminEntity;
    }
}