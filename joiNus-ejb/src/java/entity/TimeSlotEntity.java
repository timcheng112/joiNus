/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import ejb.enums.SlotStatusEnum;
import java.io.Serializable;
import java.util.Date;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author reven
 */
@Entity
public class TimeSlotEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long timeSlotId;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeSlotTime;

    @Enumerated(EnumType.STRING)
    private SlotStatusEnum status;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private FacilityEntity facility;

    @OneToOne(optional = true)
    @JoinColumn(nullable = true)
    private BookingEntity booking;

    public TimeSlotEntity() {
    }

    public TimeSlotEntity(Date time, SlotStatusEnum status, FacilityEntity facility) {
        this.timeSlotTime = time;
        this.status = status;
        this.facility = facility;
    }

    public Long getTimeSlotId() {
        return timeSlotId;
    }

    public void setTimeSlotId(Long timeSlotId) {
        this.timeSlotId = timeSlotId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (timeSlotId != null ? timeSlotId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the timeslotId fields are not set
        if (!(object instanceof TimeSlotEntity)) {
            return false;
        }
        TimeSlotEntity other = (TimeSlotEntity) object;
        if ((this.timeSlotId == null && other.timeSlotId != null) || (this.timeSlotId != null && !this.timeSlotId.equals(other.timeSlotId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.TimeSlotEntity[ id=" + timeSlotId + " ]";
    }

    /**
     * @return the timeSlotTime
     */
    public Date getTimeSlotTime() {
        return timeSlotTime;
    }

    /**
     * @param timeSlotTime the timeSlotTime to set
     */
    public void setTimeSlotTime(Date timeSlotTime) {
        this.timeSlotTime = timeSlotTime;
    }

    /**
     * @return the facility
     */
    public FacilityEntity getFacility() {
        return facility;
    }

    /**
     * @param facility the facility to set
     */
    public void setFacility(FacilityEntity facility) {
        this.facility = facility;
    }

    /**
     * @return the status
     */
    public SlotStatusEnum getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(SlotStatusEnum status) {
        this.status = status;
    }

    /**
     * @return the booking
     */
    public BookingEntity getBooking() {
        return booking;
    }

    /**
     * @param booking the booking to set
     */
    public void setBooking(BookingEntity booking) {
        this.booking = booking;
    }

}
