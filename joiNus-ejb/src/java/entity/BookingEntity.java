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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author wongs
 */
@Entity
public class BookingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @Column(nullable = false)
    private SlotStatusEnum bookingStatus;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @OneToOne(optional = false)
    private ActivityEntity activity;

    @OneToOne(optional = false)
    private TimeSlotEntity timeSlot;

    public BookingEntity() {
    }

    public BookingEntity(SlotStatusEnum bookingStatus, Date creationDate, ActivityEntity activity, TimeSlotEntity timeSlot) {
        this.bookingStatus = bookingStatus;
        this.creationDate = creationDate;
        this.activity = activity;
        this.timeSlot = timeSlot;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bookingId != null ? bookingId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the bookingId fields are not set
        if (!(object instanceof BookingEntity)) {
            return false;
        }
        BookingEntity other = (BookingEntity) object;
        if ((this.bookingId == null && other.bookingId != null) || (this.bookingId != null && !this.bookingId.equals(other.bookingId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.BookingEntity[ id=" + bookingId + " ]";
    }

    /**
     * @return the bookingStatus
     */
    public SlotStatusEnum getBookingStatus() {
        return bookingStatus;
    }

    /**
     * @param bookingStatus the bookingStatus to set
     */
    public void setBookingStatus(SlotStatusEnum bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    /**
     * @return the creationDate
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * @param creationDate the creationDate to set
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * @return the activity
     */
    public ActivityEntity getActivity() {
        return activity;
    }

    /**
     * @param activity the activity to set
     */
    public void setActivity(ActivityEntity activity) {
        this.activity = activity;
    }

    /**
     * @return the timeSlot
     */
    public TimeSlotEntity getTimeSlot() {
        return timeSlot;
    }

    /**
     * @param timeSlot the timeSlot to set
     */
    public void setTimeSlot(TimeSlotEntity timeSlot) {
        this.timeSlot = timeSlot;
    }

}
