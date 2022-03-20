/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author Jeremy
 */
@Entity
public class CategoryEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;
    @Column(nullable = false, length = 24)
    private String categoryName;
    @OneToMany(mappedBy = "subCategory", fetch = FetchType.LAZY)
    private CategoryEntity parentCategory;
    @ManyToOne(fetch = FetchType.LAZY)
    private CategoryEntity subCategory;
    @ManyToOne
    private ActivityEntity activities;


    public CategoryEntity(String categoryName, CategoryEntity parentCategory, CategoryEntity subCategory, ActivityEntity activities) {
        this.categoryName = categoryName;
        this.parentCategory = parentCategory;
        this.subCategory = subCategory;
        this.activities = activities;
    }
    

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (categoryId != null ? categoryId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the categoryId fields are not set
        if (!(object instanceof CategoryEntity)) {
            return false;
        }
        CategoryEntity other = (CategoryEntity) object;
        if ((this.categoryId == null && other.categoryId != null) || (this.categoryId != null && !this.categoryId.equals(other.categoryId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CategoryEntity[ id=" + categoryId + " ]";
    }

    /**
     * @return the categoryName
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * @param categoryName the categoryName to set
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    /**
     * @return the parentCategory
     */
    public CategoryEntity getParentCategory() {
        return parentCategory;
    }

    /**
     * @param parentCategory the parentCategory to set
     */
    public void setParentCategory(CategoryEntity parentCategory) {
        this.parentCategory = parentCategory;
    }

    /**
     * @return the subCategory
     */
    public CategoryEntity getSubCategory() {
        return subCategory;
    }

    /**
     * @param subCategory the subCategory to set
     */
    public void setSubCategory(CategoryEntity subCategory) {
        this.subCategory = subCategory;
    }

    /**
     * @return the activities
     */
    public ActivityEntity getActivities() {
        return activities;
    }

    /**
     * @param activities the activities to set
     */
    public void setActivities(ActivityEntity activities) {
        this.activities = activities;
    }
    
}
