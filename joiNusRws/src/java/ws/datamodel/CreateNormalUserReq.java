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

}
