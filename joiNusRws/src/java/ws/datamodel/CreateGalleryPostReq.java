/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import entity.NormalUserEntity;
import java.util.Date;

/**
 *
 * @author User
 */
public class CreateGalleryPostReq {
    
    private String username;
    private String password;
    private String imagePath;
    private String imageDescription;
    private Date datePosted;
    private NormalUserEntity postedBy;
    private Long activityId;

    public CreateGalleryPostReq() {
    }

    public CreateGalleryPostReq(String username, String password, String imagePath, String imageDescription, Date datePosted, NormalUserEntity postedBy, Long activityId) {
        this.username = username;
        this.password = password;
        this.imagePath = imagePath;
        this.imageDescription = imageDescription;
        this.datePosted = datePosted;
        this.postedBy = postedBy;
        this.activityId = activityId;
    }


    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the imagePath
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * @param imagePath the imagePath to set
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     * @return the imageDescription
     */
    public String getImageDescription() {
        return imageDescription;
    }

    /**
     * @param imageDescription the imageDescription to set
     */
    public void setImageDescription(String imageDescription) {
        this.imageDescription = imageDescription;
    }

    /**
     * @return the datePosted
     */
    public Date getDatePosted() {
        return datePosted;
    }

    /**
     * @param datePosted the datePosted to set
     */
    public void setDatePosted(Date datePosted) {
        this.datePosted = datePosted;
    }

    /**
     * @return the postedBy
     */
    public NormalUserEntity getPostedBy() {
        return postedBy;
    }

    /**
     * @param postedBy the postedBy to set
     */
    public void setPostedBy(NormalUserEntity postedBy) {
        this.postedBy = postedBy;
    }

    /**
     * @return the activityId
     */
    public Long getActivityId() {
        return activityId;
    }

    /**
     * @param activityId the activityId to set
     */
    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }
    
    
    
}
