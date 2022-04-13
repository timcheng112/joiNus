/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import java.util.Date;
import java.util.List;

/**
 *
 * @author User
 */
public class CreateActivityReq {

    private String username;
    private String password;
//    private ActivityEntity newActivity;
    private String activityName;
    private String activityDescription;
    private Integer activityMaxParticipants;
    private List<String> tags;
    private Long categoryId;
    private Long timeSlotId;

    public CreateActivityReq() {
    }

    public CreateActivityReq(String username, String password, String activityName, String activityDescription, Integer activityMaxParticipants, List<String> tags, Long categoryId, Long timeSlotId) {
        this.username = username;
        this.password = password;
        this.activityName = activityName;
        this.activityDescription = activityDescription;
        this.activityMaxParticipants = activityMaxParticipants;
        this.tags = tags;
        this.categoryId = categoryId;
        this.timeSlotId = timeSlotId;
    }

//    public CreateActivityReq(String username, String password, ActivityEntity newActivity, Long categoryId, Long timeSlotId) {
//        this.username = username;
//        this.password = password;
//        this.newActivity = newActivity;
//        this.categoryId = categoryId;
//        this.timeSlotId = timeSlotId;
//    }
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

//    /**
//     * @return the newActivity
//     */
//    public ActivityEntity getNewActivity() {
//        return newActivity;
//    }
//
//    /**
//     * @param newActivity the newActivity to set
//     */
//    public void setNewActivity(ActivityEntity newActivity) {
//        this.newActivity = newActivity;
//    }
    /**
     * @return the categoryId
     */
    public Long getCategoryId() {
        return categoryId;
    }

    /**
     * @param categoryId the categoryId to set
     */
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * @return the timeSlotId
     */
    public Long getTimeSlotId() {
        return timeSlotId;
    }

    /**
     * @param timeSlotId the timeSlotId to set
     */
    public void setTimeSlotId(Long timeSlotId) {
        this.timeSlotId = timeSlotId;
    }

    /**
     * @return the activityName
     */
    public String getActivityName() {
        return activityName;
    }

    /**
     * @param activityName the activityName to set
     */
    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    /**
     * @return the activityDescription
     */
    public String getActivityDescription() {
        return activityDescription;
    }

    /**
     * @param activityDescription the activityDescription to set
     */
    public void setActivityDescription(String activityDescription) {
        this.activityDescription = activityDescription;
    }

    /**
     * @return the tags
     */
    public List<String> getTags() {
        return tags;
    }

    /**
     * @param tags the tags to set
     */
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    /**
     * @return the activityMaxParticipants
     */
    public Integer getActivityMaxParticipants() {
        return activityMaxParticipants;
    }

    /**
     * @param activityMaxParticipants the activityMaxParticipants to set
     */
    public void setActivityMaxParticipants(Integer activityMaxParticipants) {
        this.activityMaxParticipants = activityMaxParticipants;
    }

}
