/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import entity.NormalUserEntity;

/**
 *
 * @author User
 */
public class AddCommentReq {

    private String username;
    private String password;
    private String text;
    private Long activityId;

    public AddCommentReq() {
    }

    public AddCommentReq(String username, String password, String text, Long activityId) {
        this.username = username;
        this.password = password;
        this.text = text;
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
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
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
