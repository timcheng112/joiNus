/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;

/**
 *
 * @author User
 */
@Entity
public class AdminEntity extends UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(nullable = false, length = 64, unique = true)
    private String club;
    @Column(nullable = false)
    private Boolean isSuperAdmin;

    public AdminEntity() {
        super();
    }

    public AdminEntity(String club, Boolean isSuperAdmin, String username, String password) {
        this();
        this.username = username;
        this.password = password;
        this.club = club;
        this.isSuperAdmin = isSuperAdmin;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userId != null ? userId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AdminEntity)) {
            return false;
        }
        AdminEntity other = (AdminEntity) object;
        if ((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.AdminEntity[ id=" + userId + " ]";
    }

    /**
     * @return the club
     */
    public String getClub() {
        return club;
    }

    /**
     * @param club the club to set
     */
    public void setClub(String club) {
        this.club = club;
    }

    /**
     * @return the isSuperAdmin
     */
    public Boolean getIsSuperAdmin() {
        return isSuperAdmin;
    }

    /**
     * @param isSuperAdmin the isSuperAdmin to set
     */
    public void setIsSuperAdmin(Boolean isSuperAdmin) {
        this.isSuperAdmin = isSuperAdmin;
    }
    
}
