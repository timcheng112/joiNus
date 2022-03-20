/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author User
 */
@Entity
public class NormalUserEntity extends UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(nullable = false, length = 64, unique = true)
    @NotNull(message = "Email cannot be null")
    @Size(min = 1, max = 64, message = "Email must be between 1 and 64 characters")
    @Email(message = "Email should be valid")
//  @Pattern(regexp = "^(\\D)+(\\w)*((\\.(\\w)+)?)+@(\\D)+(\\w)*((\\.(\\D)+(\\w)*)+)?(\\.)[a-z]{2,}$")
    private String email;
    @Column(nullable = false, length = 64)
    @NotNull(message = "Name cannot be null")
    @Size(min = 1, max = 64, message = "Name must be between 1 and 64 characters")
    private String name;
    @Column(nullable = false)
    @Min(value = 0, message = "Social Credits should not be less than 0")
    @Max(value = 99999, message = "Social Credits should not be greater than 99999")
    private Integer socialCredits;
    @Column(nullable = false)
    @Min(value = 0, message = "Booking Tokens should not be less than 0")
    @Max(value = 99999, message = "Booking Tokens should not be more than 99999")
    private Integer bookingTokens;
    
    //@OneToMany(fetch = FetchType.LAZY, cascade={})
    //protected List<Category> interests;
    //@ManyToMany(mappedBy="participants", fetch = FetchType.LAZY)
    //protected List<Activity> activitiesParticipated;
    //@OneToMany(mappedBy="activityOwner", fetch = FetchType.LAZY)
    //protected List<Activity> activitiesOwned;
    
    public NormalUserEntity() {
        super();
        //interests = new ArrayList<>();
        //activitiesParticipated = new ArrayList<>();
        //activitiesOwned = new ArrayList<>();
    }

    public NormalUserEntity(String email, String name, Integer socialCredits, Integer bookingTokens, String username, String password) {
        this();
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.socialCredits = socialCredits;
        this.bookingTokens = bookingTokens;
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
        if (!(object instanceof NormalUserEntity)) {
            return false;
        }
        NormalUserEntity other = (NormalUserEntity) object;
        if ((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.NormalUserEntity[ id=" + userId + " ]";
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the socialCredits
     */
    public Integer getSocialCredits() {
        return socialCredits;
    }

    /**
     * @param socialCredits the socialCredits to set
     */
    public void setSocialCredits(Integer socialCredits) {
        this.socialCredits = socialCredits;
    }

    /**
     * @return the bookingTokens
     */
    public Integer getBookingTokens() {
        return bookingTokens;
    }

    /**
     * @param bookingTokens the bookingTokens to set
     */
    public void setBookingTokens(Integer bookingTokens) {
        this.bookingTokens = bookingTokens;
    }


}
