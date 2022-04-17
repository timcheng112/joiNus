/*

NOT DONE NOT DONE NOT DONE NOT DONE


 */
package jsf.managedbean;

import ejb.session.stateless.AdminEntitySessionBeanLocal;
import ejb.session.stateless.FacilityEntitySessionBeanLocal;
import entity.AdminEntity;
import entity.FacilityEntity;
import entity.ImageEntity;
import entity.TimeSlotEntity;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.event.FileUploadEvent;
import util.exception.AdminNotFoundException;
import util.exception.CreateNewFacilityException;
import util.exception.FacilityNameExistException;
import util.exception.FacilityNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateFacilityException;

/**
 *
 * @author wongs
 */
@Named(value = "facilitiyManagedBean")
@ViewScoped
public class FacilitiyManagedBean implements Serializable {

    @EJB(name = "AdminEntitySessionBeanLocal")
    private AdminEntitySessionBeanLocal adminEntitySessionBeanLocal;

    @Inject
    private AdminLoginManagedBean adminLoginManagedBean;

    @EJB
    private FacilityEntitySessionBeanLocal facilityEntitySessionBeanLocal;

    private FacilityEntity newFacilityEntity;
    private FacilityEntity facilityEntityToUpdate;
    private List<TimeSlotEntity> timeSlots;
    private List<FacilityEntity> facilityEntities;
    private String uploadedFilePath;
    private ImageEntity uploadedImage;
    private boolean showImage;
    private int newOpeningHour;
    private int newClosingHour;

    public int getNewClosingHour() {
        return newClosingHour;
    }

    public void setNewClosingHour(int newClosingHour) {
        this.newClosingHour = newClosingHour;
    }

    public int getNewOpeningHour() {
        return newOpeningHour;
    }

    public void setNewOpeningHour(int newOpeningHour) {
        this.newOpeningHour = newOpeningHour;
    }

    /**
     * Creates a new instance of FacilitiyManagedBean
     */
    public FacilitiyManagedBean() {
        newFacilityEntity = new FacilityEntity();
        showImage = false;
    }

    /*
    Create New Facility
    Update Facility
    View All Facilities
    View Facility Details
     */
    @PostConstruct
    public void postConstruct() {
        try {
            AdminEntity admin = adminEntitySessionBeanLocal.retrieveAdminByUsername(adminLoginManagedBean.getUsername());
            setFacilityEntities(facilityEntitySessionBeanLocal.retrieveAllFacilitiesByClub(admin.getClub()));
        } catch (AdminNotFoundException ex) {
            //
        }

        for (FacilityEntity facility : facilityEntities) {
            facility.getTimeSlots();
        }

    }

    public void initialiseState() {
        try {
            AdminEntity admin = adminEntitySessionBeanLocal.retrieveAdminByUsername(adminLoginManagedBean.getUsername());
            setFacilityEntities(facilityEntitySessionBeanLocal.retrieveAllFacilitiesByClub(admin.getClub()));
        } catch (AdminNotFoundException ex) {
            //
        }
    }

    public void createNewFacility() {
        try {
            System.out.println("jsf.managedbean.FacilitiyManagedBean.createNewFacility()");
            FacilityEntity facility = facilityEntitySessionBeanLocal.createNewFacility(newFacilityEntity);

            facilityEntities.add(facility);

            newFacilityEntity = new FacilityEntity();
            uploadedFilePath = "";
            uploadedImage = new ImageEntity();
            showImage = false;

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New Facility created successfully (Facility ID: " + facility.getFacilityId() + ", Facility Name: " + facility.getFacilityName() + ")", null));

        } catch (UnknownPersistenceException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in creating facility UnknownPersistenceException", null));
        } catch (FacilityNameExistException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in creating facility FacilityNameExistException", null));
        } catch (InputDataValidationException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in creating facility InputDataValidationException", null));
        } catch (CreateNewFacilityException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in creating facility CreateNewFacilityException", null));
        }
    }

    public void editExistingFacility() {
        try {
            facilityEntitySessionBeanLocal.updateFacilityNew(facilityEntityToUpdate, newOpeningHour, newClosingHour);

            System.out.println("Updated Facility: " + facilityEntityToUpdate.getFacilityId());
            uploadedFilePath = "";
            uploadedImage = new ImageEntity();
            showImage = false;
//            if (facilityEntityToUpdate.getFacilityImage() == null) {
//                uploadedFilePath = "";
//                uploadedImage = new ImageEntity();
//                showImage = false;
//            } else {
//                uploadedFilePath = facilityEntityToUpdate.getFacilityImage().getImagePath();
//                uploadedImage = facilityEntityToUpdate.getFacilityImage();
//                showImage = true;
//            }
            initialiseState();
            facilityEntityToUpdate = new FacilityEntity();

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucessfully edited facility", null));
        } catch (FacilityNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in editing facility FacilityNotFoundException", null));
        } catch (UpdateFacilityException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in editing facility UpdateFacilityException", null));
        } catch (InputDataValidationException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in editing facility InputDataValidationException", null));
        }

    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            String newFilePath = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("alternatedocroot_1") + System.getProperty("file.separator") + event.getFile().getFileName();

            System.err.println("********** FacilitiyManagedBean.handleFileUpload(): File name: " + event.getFile().getFileName());
            System.err.println("********** FacilitiyManagedBean.handleFileUpload(): newFilePath: " + newFilePath);

            File file = new File(newFilePath);
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            int a;
            int BUFFER_SIZE = 8192;
            byte[] buffer = new byte[BUFFER_SIZE];

            InputStream inputStream = event.getFile().getInputStream();

            while (true) {
                a = inputStream.read(buffer);

                if (a < 0) {
                    break;
                }

                fileOutputStream.write(buffer, 0, a);
                fileOutputStream.flush();
            }

            fileOutputStream.close();
            inputStream.close();

            uploadedFilePath = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("uploadedFilesPath") + "/" + event.getFile().getFileName();
            System.out.println("Photo Path is : " + uploadedFilePath);

            AdminEntity currAdmin = (AdminEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentAdminEntity");

            ImageEntity facilityImage = new ImageEntity(uploadedFilePath, newFacilityEntity.getFacilityName() + " Image", new Date(), currAdmin);
            facilityImage = facilityEntitySessionBeanLocal.persistImage(facilityImage);
            newFacilityEntity.setFacilityImage(facilityImage);
            uploadedImage = facilityImage;
            showImage = true;

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "File uploaded successfully", ""));
        } catch (IOException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "File upload error: " + ex.getMessage(), ""));
        }
    }

    public void handleFileUploadEdit(FileUploadEvent event) {
        try {
            String newFilePath = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("alternatedocroot_1") + System.getProperty("file.separator") + event.getFile().getFileName();

            System.err.println("********** FacilitiyManagedBean.handleFileUpload(): File name: " + event.getFile().getFileName());
            System.err.println("********** FacilitiyManagedBean.handleFileUpload(): newFilePath: " + newFilePath);

            File file = new File(newFilePath);
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            int a;
            int BUFFER_SIZE = 8192;
            byte[] buffer = new byte[BUFFER_SIZE];

            InputStream inputStream = event.getFile().getInputStream();

            while (true) {
                a = inputStream.read(buffer);

                if (a < 0) {
                    break;
                }

                fileOutputStream.write(buffer, 0, a);
                fileOutputStream.flush();
            }

            fileOutputStream.close();
            inputStream.close();

            uploadedFilePath = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("uploadedFilesPath") + "/" + event.getFile().getFileName();
            System.out.println("Photo Path is : " + uploadedFilePath);

            AdminEntity currAdmin = (AdminEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentAdminEntity");

            ImageEntity facilityImage = new ImageEntity(uploadedFilePath, newFacilityEntity.getFacilityName() + " Image", new Date(), currAdmin);
            facilityImage = facilityEntitySessionBeanLocal.persistImage(facilityImage);
            facilityEntityToUpdate.setFacilityImage(facilityImage);
            uploadedImage = facilityImage;
            showImage = true;

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "File uploaded successfully", ""));
        } catch (IOException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "File upload error: " + ex.getMessage(), ""));
        }
    }

    public void clear(ActionEvent event) {
        uploadedFilePath = "";
//        uploadedImage = new ImageEntity();
        showImage = false;
        newFacilityEntity = new FacilityEntity();
    }

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

    public String getUploadedFilePath() {
        return uploadedFilePath;
    }

    public void setUploadedFilePath(String uploadedFilePath) {
        this.uploadedFilePath = uploadedFilePath;
    }

    public ImageEntity getUploadedImage() {
        return uploadedImage;
    }

    public void setUploadedImage(ImageEntity uploadedImage) {
        this.uploadedImage = uploadedImage;
    }

    public boolean isShowImage() {
        return showImage;
    }

    public void setShowImage(boolean showImage) {
        this.showImage = showImage;
    }

    /**
     * @return the facilityEntityToUpdate
     */
    public FacilityEntity getFacilityEntityToUpdate() {
        return facilityEntityToUpdate;
    }

    /**
     * @param facilityEntityToUpdate the facilityEntityToUpdate to set
     */
    public void setFacilityEntityToUpdate(FacilityEntity facilityEntityToUpdate) {
        this.facilityEntityToUpdate = facilityEntityToUpdate;
        this.newOpeningHour = facilityEntityToUpdate.getOpeningHour();
        this.newClosingHour = facilityEntityToUpdate.getClosingHour();
    }

}
