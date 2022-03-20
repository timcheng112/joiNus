/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author reven
 */
@Entity
public class FacilityEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long facilityId;
    
    @Column(nullable = false, length = 64, unique = true)
    private String facilityName;
    
    @Column(nullable = false, length = 64)
    private String club;
    
    @Column(nullable = false, length = 3)
    private Integer tokenCost;
    
    @Column(nullable = false, length = 3)
    private Integer capacity;
    
    @OneToMany(mappedBy="facility", fetch = FetchType.LAZY)
    private List<TimeSlotEntity> timeSlots;

    public FacilityEntity() {
        this.timeSlots = new ArrayList<>();
    }

    public FacilityEntity(String facilityName, String club, Integer tokenCost, Integer capacity, String address) {
        this();
        this.facilityName = facilityName;
        this.club = club;
        this.tokenCost = tokenCost;
        this.capacity = capacity;
        this.address = address;
    }
    
    @Column(nullable = false, length = 128, unique = true)
    private String address;

    public Long getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(Long facilityId) {
        this.facilityId = facilityId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (facilityId != null ? facilityId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the facilityId fields are not set
        if (!(object instanceof FacilityEntity)) {
            return false;
        }
        FacilityEntity other = (FacilityEntity) object;
        if ((this.facilityId == null && other.facilityId != null) || (this.facilityId != null && !this.facilityId.equals(other.facilityId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.FacilityEntity[ id=" + facilityId + " ]";
    }

    /**
     * @return the facilityName
     */
    public String getFacilityName() {
        return facilityName;
    }

    /**
     * @param facilityName the facilityName to set
     */
    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
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
     * @return the tokenCost
     */
    public Integer getTokenCost() {
        return tokenCost;
    }

    /**
     * @param tokenCost the tokenCost to set
     */
    public void setTokenCost(Integer tokenCost) {
        this.tokenCost = tokenCost;
    }

    /**
     * @return the capacity
     */
    public Integer getCapacity() {
        return capacity;
    }

    /**
     * @param capacity the capacity to set
     */
    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }
    
}
