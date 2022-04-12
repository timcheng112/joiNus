/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

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
public class ImageEntity implements Serializable {
    //attributes

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    @Column(nullable = false)
    private String imagePath;

    @Column(nullable = true)
    private String imageDescription;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date datePosted;

    //relationship
    @OneToOne(optional = false)
    private UserEntity postedBy;

    public ImageEntity() {
    }

    public ImageEntity(String imagePath, String imageDescription, Date datePosted, UserEntity postedBy) {
        this.imagePath = imagePath;
        this.imageDescription = imageDescription;
        this.datePosted = datePosted;
        this.postedBy = postedBy;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (imageId != null ? imageId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the imageId fields are not set
        if (!(object instanceof ImageEntity)) {
            return false;
        }
        ImageEntity other = (ImageEntity) object;
        if ((this.imageId == null && other.imageId != null) || (this.imageId != null && !this.imageId.equals(other.imageId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ImageEntity[ id=" + imageId + " ]";
    }

    /**
     * @return the postedBy
     */
    public UserEntity getPostedBy() {
        return postedBy;
    }

    /**
     * @param postedBy the postedBy to set
     */
    public void setPostedBy(UserEntity postedBy) {
        this.postedBy = postedBy;
    }

    /**
     * @return the imageDescription
     */
    public String getImageDescription() {
        return imageDescription;
    }

    /**
     * @param imageDescription the imageDescription to set
     */
    public void setImageDescription(String imageDescription) {
        this.imageDescription = imageDescription;
    }

    /**
     * @return the datePosted
     */
    public Date getDatePosted() {
        return datePosted;
    }

    /**
     * @param datePosted the datePosted to set
     */
    public void setDatePosted(Date datePosted) {
        this.datePosted = datePosted;
    }

}
