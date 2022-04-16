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
public class CreateNormalUserReq {

    private NormalUserEntity normalUser;
    private String newPassword;

    public CreateNormalUserReq() {
    }

    public CreateNormalUserReq(NormalUserEntity normalUser) {
        this.normalUser = normalUser;
    }

    public NormalUserEntity getNormalUser() {
        return normalUser;
    }

    public void setNormalUser(NormalUserEntity normalUser) {
        this.normalUser = normalUser;
    }

    /**
     * @return the newPassword
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * @param newPassword the newPassword to set
     */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

}
