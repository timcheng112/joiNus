/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date activityCreationDate;

    @ManyToOne
    @JoinColumn(nullable = false)
    private NormalUserEntity activityOwner;

    @ManyToMany
    private List<NormalUserEntity> participants;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private CategoryEntity category;

    @OneToOne(mappedBy = "activity")
    private BookingEntity booking;

    @OneToMany
    private List<CommentEntity> comments;

    @OneToMany
    private List<ImageEntity> gallery;
    
    private List<Long> absentIds;
    private List<Long> commenterIds;
    private List<Long> imagePosterIds;
    
    private Boolean activityOver;

    public ActivityEntity() {
        this.comments = new ArrayList<>();
        this.participants = new ArrayList<>();
        this.gallery = new ArrayList<>();
        this.participants = new ArrayList<>();
        this.absentIds = new ArrayList<>();
        this.commenterIds = new ArrayList<>();
        this.imagePosterIds = new ArrayList<>();
        this.activityOver = false;
        this.activityCreationDate = new Date();
    }

    public ActivityEntity(String activityName, String activityDescription, Integer maxParticipants, List<String> tags, NormalUserEntity activityOwner, List<NormalUserEntity> participants, CategoryEntity category, BookingEntity booking, Date activityCreationDate) {
        this();
        this.activityName = activityName;
        this.activityDescription = activityDescription;
        this.maxParticipants = maxParticipants;
        this.tags = tags;
        this.activityOwner = activityOwner;
        this.category = category;
        this.booking = booking;
        this.activityCreationDate = activityCreationDate;
    }

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
     * @return the maxParticipants
     */
    public Integer getMaxParticipants() {
        return maxParticipants;
    }

    /**
     * @param maxParticipants the maxParticipants to set
     */
    public void setMaxParticipants(Integer maxParticipants) {
        this.maxParticipants = maxParticipants;
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
     * @param tag add a tag to set
     */
    public void addTag(String tag) {
        this.tags.add(tag);
    }

    /**
     * @return the activityOwner
     */
    public NormalUserEntity getActivityOwner() {
        return activityOwner;
    }

    /**
     * @param activityOwner the activityOwner to set
     */
    public void setActivityOwner(NormalUserEntity activityOwner) {
        this.activityOwner = activityOwner;
    }

    /**
     * @return the category
     */
    public CategoryEntity getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(CategoryEntity category) {
        this.category = category;
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

    /**
     * @return the comments
     */
    public List<CommentEntity> getComments() {
        return comments;
    }

    /**
     * @param comments the comments to set
     */
    public void setComments(List<CommentEntity> comments) {
        this.comments = comments;
    }

    /**
     * @param comment add a comment to set
     */
    public void addComment(CommentEntity comment) {
        this.comments.add(comment);
    }

    /**
     * @return the participants
     */
    public List<NormalUserEntity> getParticipants() {
        return participants;
    }

    /**
     * @param participants the participants to set
     */
    public void setParticipants(List<NormalUserEntity> participants) {
        this.participants = participants;
    }

    /**
     * @param user add a user to set
     */
    public void addParticipant(NormalUserEntity user) {
        this.participants.add(user);
    }

    /**
     * @return the activityCreationDate
     */
    public Date getActivityCreationDate() {
        return activityCreationDate;
    }

    /**
     * @return the gallery
     */
    public List<ImageEntity> getGallery() {
        return gallery;
    }

    /**
     * @param gallery the gallery to set
     */
    public void setGallery(List<ImageEntity> gallery) {
        this.gallery = gallery;
    }

    /**
     * @return the numberOfParticipants
     */
    public Integer getNumberOfParticipants() {
        return 1 + participants.size();
    }

//    @JsonbTransient
    public List<Long> getAbsentIds() {
        return absentIds;
    }

    public void setAbsentIds(List<Long> absentIds) {
        this.absentIds = absentIds;
    }

//    @JsonbTransient
    public List<Long> getCommenterIds() {
        return commenterIds;
    }

    public void setCommenterIds(List<Long> commenterIds) {
        this.commenterIds = commenterIds;
    }

//    @JsonbTransient
    public List<Long> getImagePosterIds() {
        return imagePosterIds;
    }

    public void setImagePosterIds(List<Long> imagePosterIds) {
        this.imagePosterIds = imagePosterIds;
    }

//    @JsonbTransient
    public Boolean getActivityOver() {
        return activityOver;
    }

    public void setActivityOver(Boolean activityOver) {
        this.activityOver = activityOver;
    }

    /**
     * @param activityCreationDate the activityCreationDate to set
     */
    public void setActivityCreationDate(Date activityCreationDate) {
        this.activityCreationDate = activityCreationDate;
    }
    
}
