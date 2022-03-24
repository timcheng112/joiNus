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
import javax.persistence.JoinColumn;
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

    @OneToMany(mappedBy = "parentCategory", fetch = FetchType.LAZY)
    private List<CategoryEntity> subCategories;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private CategoryEntity parentCategory;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<ActivityEntity> activities;

    public CategoryEntity() {
        this.subCategories = new ArrayList<>();
        this.activities = new ArrayList<>();
    }

    public CategoryEntity(String categoryName, CategoryEntity parentCategory, CategoryEntity subCategory, ActivityEntity activities) {
        this.categoryName = categoryName;
        this.parentCategory = parentCategory;
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
        if (this.parentCategory != null) {
            if (this.parentCategory.getSubCategories().contains(this)) {
                this.parentCategory.getSubCategories().remove(this);
            }
        }

        this.parentCategory = parentCategory;

        if (this.parentCategory != null) {
            if (!this.parentCategory.getSubCategories().contains(this)) {
                this.parentCategory.getSubCategories().add(this);
            }
        }
    }

    /**
     * @return the subCategories
     */
    public List<CategoryEntity> getSubCategories() {
        return subCategories;
    }

    /**
     * @param subCategories the subCategories to set
     */
    public void setSubCategories(List<CategoryEntity> subCategories) {
        this.subCategories = subCategories;
    }

    /**
     * @param subCategory add a subCategory to set
     */
    public void addSubCategory(CategoryEntity subCategory) {
        this.subCategories.add(subCategory);
    }

    /**
     * @return the activities
     */
    public List<ActivityEntity> getActivities() {
        return activities;
    }

    /**
     * @param activities the activities to set
     */
    public void setActivities(List<ActivityEntity> activities) {
        this.activities = activities;
    }

    /**
     * @param activity add an activity to set
     */
    public void addSubCategory(ActivityEntity activity) {
        this.activities.add(activity);
    }
}
