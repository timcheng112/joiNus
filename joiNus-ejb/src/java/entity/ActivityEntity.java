/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author Jeremy
 */
@Entity
public class ActivityEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long activityId;
    @Column(nullable = false, length = 32)
    private String activityName;
    @Column(nullable = false)
    private String activityDescription;
    @Column(nullable = false, length = 2)
    private Integer maxParticipants;
    @Column
    private List<String> tags;
    
    @ManyToOne
    private NormalUserEntity activityOwner;
    @ManyToMany
    private NormalUserEntity participants;
    @ManyToOne
    private CategoryEntity category;
    //@OneToOne(mappedBy = "activity")
    //private BookingEntity booking;
    @OneToMany
    private CommentEntity comments;
    
    //Make Constructor + Refactor Fields

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (activityId != null ? activityId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the activityId fields are not set
        if (!(object instanceof ActivityEntity)) {
            return false;
        }
        ActivityEntity other = (ActivityEntity) object;
        if ((this.activityId == null && other.activityId != null) || (this.activityId != null && !this.activityId.equals(other.activityId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ActivityEntity[ id=" + activityId + " ]";
    }
    
}
