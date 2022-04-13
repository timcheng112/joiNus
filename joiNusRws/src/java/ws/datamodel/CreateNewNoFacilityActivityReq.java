/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import java.util.List;

/**
 *
 * @author User
 */
public class CreateNewNoFacilityActivityReq {

    private String username;
    private String password;
    private String activityName;
    private String activityDescription;
    private Integer activityMaxParticipants;
    private List<String> tags;
    private Long categoryId;
    private Integer activityYear;
    private Integer activityMonth;
    private Integer activityDay;
    private Integer activityHour;
    private Integer activityMinute;

    public CreateNewNoFacilityActivityReq() {
    }

    public CreateNewNoFacilityActivityReq(String username, String password, String activityName, String activityDescription, Integer activityMaxParticipants, List<String> tags, Long categoryId, Integer activityYear, Integer activityMonth, Integer activityDay, Integer activityHour, Integer activityMinute) {
        this.username = username;
        this.password = password;
        this.activityName = activityName;
        this.activityDescription = activityDescription;
        this.activityMaxParticipants = activityMaxParticipants;
        this.tags = tags;
        this.categoryId = categoryId;
        this.activityYear = activityYear;
        this.activityMonth = activityMonth;
        this.activityDay = activityDay;
        this.activityHour = activityHour;
        this.activityMinute = activityMinute;
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
     * @return the activityYear
     */
    public Integer getActivityYear() {
        return activityYear;
    }

    /**
     * @param activityYear the activityYear to set
     */
    public void setActivityYear(Integer activityYear) {
        this.activityYear = activityYear;
    }

    /**
     * @return the activityMonth
     */
    public Integer getActivityMonth() {
        return activityMonth;
    }

    /**
     * @param activityMonth the activityMonth to set
     */
    public void setActivityMonth(Integer activityMonth) {
        this.activityMonth = activityMonth;
    }

    /**
     * @return the activityDay
     */
    public Integer getActivityDay() {
        return activityDay;
    }

    /**
     * @param activityDay the activityDay to set
     */
    public void setActivityDay(Integer activityDay) {
        this.activityDay = activityDay;
    }

    /**
     * @return the activityHour
     */
    public Integer getActivityHour() {
        return activityHour;
    }

    /**
     * @param activityHour the activityHour to set
     */
    public void setActivityHour(Integer activityHour) {
        this.activityHour = activityHour;
    }

    /**
     * @return the activityMinute
     */
    public Integer getActivityMinute() {
        return activityMinute;
    }

    /**
     * @param activityMinute the activityMinute to set
     */
    public void setActivityMinute(Integer activityMinute) {
        this.activityMinute = activityMinute;
    }

}
