/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CategoryEntitySessionBean;
import ejb.session.stateless.CategoryEntitySessionBeanLocal;
import entity.ActivityEntity;
import entity.CategoryEntity;
import java.io.IOException;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import util.exception.CategoryNotFoundException;
import util.exception.CreateNewCategoryException;
import util.exception.DeleteCategoryException;
import util.exception.InputDataValidationException;

/**
 *
 * @author Jeremy
 */
@Named(value = "categoryManagedBean")
@ViewScoped
public class CategoryManagedBean implements Serializable {

    @EJB
    private CategoryEntitySessionBeanLocal categoryEntitySessionBeanLocal;

    private List<CategoryEntity> categoryEntities;
    private List<CategoryEntity> filteredCategoryEntities;
    private List<CategoryEntity> leafCategories;
    private List<CategoryEntity> rootCategories;
    
    private CategoryEntity newCategoryEntity;
    private CategoryEntity newParentCategory;
    private Long parentCategoryIdNew;
    
    private CategoryEntity categoryUpdate;
    private boolean categoryUpdateHasActivities;
    private Long categoryIdUpdate;
    private String categoryNameUpdate;
    private Long parentCategoryIdUpdate;
    
    private CategoryEntity categoryDelete;
    private boolean categoryDeleteHasActivities;
    private Long categoryIdDelete;
    private String categoryNameDelete;
    
    private CategoryEntity categoryEntityToView;
    
    /**
     * Creates a new instance of CategoryManagedBean
     */
    public CategoryManagedBean() {
        newCategoryEntity = new CategoryEntity();
        newParentCategory = new CategoryEntity();
    }
    
    @PostConstruct
    public void postConstruct()
    {
        rootCategories = categoryEntitySessionBeanLocal.retrieveAllRootCategories();
        leafCategories = categoryEntitySessionBeanLocal.retrieveAllLeafCategories();
        setCategoryEntities(getCategoryEntitySessionBeanLocal().retrieveAllCategories());
        for (CategoryEntity category : categoryEntities) {
            category.getSubCategories();
            category.getActivities();
        }
    }
    
    public void viewCategoryDetails(ActionEvent event) throws IOException
    {
        Long categoryIdToView = (Long)event.getComponent().getAttributes().get("categoryId");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("categoryIdToView", categoryIdToView);
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewCategoryDetails.xhtml");
    }
    
    public void createNewCategory(ActionEvent event)
    {        
        if(getParentCategoryIdNew() == 0 || getParentCategoryIdNew() == null)
        {
            setParentCategoryIdNew(null);
        }                
        
        try
        {
            CategoryEntity newCategory = categoryEntitySessionBeanLocal.createNewCategoryEntity(newCategoryEntity, parentCategoryIdNew);
            
            getCategoryEntities().add(newCategory);
            
            if(getFilteredCategoryEntities() != null)
            {
                getFilteredCategoryEntities().add(newCategory);
            }
            
            setNewCategoryEntity(new CategoryEntity());
            parentCategoryIdNew = null;
            

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New category created successfully (Category ID: " + newCategory.getCategoryId() + ")", null));
        }
        catch(InputDataValidationException | CreateNewCategoryException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new category: " + ex.getMessage(), null));
        }
    }
    
    public void updateCategory(ActionEvent event)
    {        
        if(getParentCategoryIdUpdate()  == 0)
        {
            setParentCategoryIdUpdate(null);
        }                
        
        try
        {
            categoryEntitySessionBeanLocal.updateCategoryById(categoryIdUpdate, categoryUpdate.getCategoryName(), parentCategoryIdUpdate);
            
            for(CategoryEntity ce:getCategoryEntities())
            {
                if(ce.getCategoryId().equals(getParentCategoryIdUpdate()))
                {
                    getCategoryUpdate().setParentCategory(ce);
                    break;
                }                
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Category updated successfully", null));
        }
        catch(CategoryNotFoundException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating category: " + ex.getMessage(), null));
        }
        catch(Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }
    
    public void deleteCategory(ActionEvent event)
    {
        try
        {
            CategoryEntity categoryEntityToDelete = categoryEntitySessionBeanLocal.retrieveCategoryByCategoryId(getCategoryIdDelete());
            getCategoryEntitySessionBeanLocal().deleteCategory(categoryEntityToDelete.getCategoryId());
            
            getCategoryEntities().remove(categoryEntityToDelete);
            
            if(getFilteredCategoryEntities() != null)
            {
                getFilteredCategoryEntities().remove(categoryEntityToDelete);
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Category deleted successfully", null));
        }
        catch(CategoryNotFoundException | DeleteCategoryException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting category: " + ex.getMessage(), null));
        }
        catch(Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }
    
    //GETTERS & SETTERS
    
    public CategoryEntitySessionBeanLocal getCategoryEntitySessionBeanLocal() {
        return categoryEntitySessionBeanLocal;
    }

    public void setCategoryEntitySessionBeanLocal(CategoryEntitySessionBeanLocal categoryEntitySessionBeanLocal) {
        this.categoryEntitySessionBeanLocal = categoryEntitySessionBeanLocal;
    }

    public List<CategoryEntity> getCategoryEntities() {
        return categoryEntities;
    }

    public void setCategoryEntities(List<CategoryEntity> categoryEntities) {
        this.categoryEntities = categoryEntities;
    }

    public List<CategoryEntity> getFilteredCategoryEntities() {
        return filteredCategoryEntities;
    }

    public void setFilteredCategoryEntities(List<CategoryEntity> filteredCategoryEntities) {
        this.filteredCategoryEntities = filteredCategoryEntities;
    }

    public CategoryEntity getNewCategoryEntity() {
        return newCategoryEntity;
    }

    public void setNewCategoryEntity(CategoryEntity newCategoryEntity) {
        this.newCategoryEntity = newCategoryEntity;
    }

    public Long getParentCategoryIdNew() {
        return parentCategoryIdNew;
    }

    public void setParentCategoryIdNew(Long parentCategoryIdNew) {
        
        
        this.parentCategoryIdNew = parentCategoryIdNew;
        
        
    }

    public CategoryEntity getCategoryUpdate() {
        return categoryUpdate;
    }

    public void setCategoryUpdate(CategoryEntity categoryUpdate) {
        categoryIdUpdate = categoryUpdate.getCategoryId();
        categoryUpdateHasActivities = !categoryUpdate.getActivities().isEmpty();
        this.categoryUpdate = categoryUpdate;
    }

    public String getCategoryNameUpdate() {
        return categoryNameUpdate;
    }

    public void setCategoryNameUpdate(String categoryNameUpdate) {
        this.categoryNameUpdate = categoryNameUpdate;
    }

    public Long getParentCategoryIdUpdate() {
        return parentCategoryIdUpdate;
    }

    public void setParentCategoryIdUpdate(Long parentCategoryIdUpdate) {
        this.parentCategoryIdUpdate = parentCategoryIdUpdate;
    }

    public CategoryEntity getCategoryEntityToView() {
        return categoryEntityToView;
    }
    
    public void setCategoryEntityToView(CategoryEntity categoryEntityToView) {
        this.categoryEntityToView = categoryEntityToView;
    }

    public List<CategoryEntity> getLeafCategories() {
        return leafCategories;
    }

    public void setLeafCategories(List<CategoryEntity> leafCategories) {
        this.leafCategories = leafCategories;
    }

    public List<CategoryEntity> getRootCategories() {
        return rootCategories;
    }

    public void setRootCategories(List<CategoryEntity> rootCategories) {
        this.rootCategories = rootCategories;
    }

    /**
     * @return the newParentCategory
     */
    public CategoryEntity getNewParentCategory() {
        return newParentCategory;
    }

    /**
     * @param newParentCategory the newParentCategory to set
     */
    public void setNewParentCategory(CategoryEntity newParentCategory) {
        if (newParentCategory == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Category not found: ", null));
            this.newParentCategory = null;
        } else {
            this.newParentCategory = newParentCategory;
        }
        
    }

    /**
     * @return the categoryIdUpdate
     */
    public Long getCategoryIdUpdate() {
        return categoryIdUpdate;
    }

    /**
     * @param categoryIdUpdate the categoryIdUpdate to set
     */
    public void setCategoryIdUpdate(Long categoryIdUpdate) {
        this.categoryIdUpdate = categoryIdUpdate;
    }

    /**
     * @return the categoryUpdateHasActivities
     */
    public boolean isCategoryUpdateHasActivities() {
        return categoryUpdateHasActivities;
    }

    /**
     * @param categoryUpdateHasActivities the categoryUpdateHasActivities to set
     */
    public void setCategoryUpdateHasActivities(boolean categoryUpdateHasActivities) {
        this.categoryUpdateHasActivities = categoryUpdateHasActivities;
    }

    /**
     * @return the categoryDelete
     */
    public CategoryEntity getCategoryDelete() {
        return categoryDelete;
    }

    /**
     * @param categoryDelete the categoryDelete to set
     */
    public void setCategoryDelete(CategoryEntity categoryDelete) {
        categoryIdDelete = categoryDelete.getCategoryId();
        setCategoryNameDelete(categoryDelete.getCategoryName());
        categoryDeleteHasActivities = !categoryDelete.getActivities().isEmpty();
        this.categoryDelete = categoryDelete;
    }

    /**
     * @return the categoryDeleteHasActivities
     */
    public boolean isCategoryDeleteHasActivities() {
        return categoryDeleteHasActivities;
    }

    /**
     * @param categoryDeleteHasActivities the categoryDeleteHasActivities to set
     */
    public void setCategoryDeleteHasActivities(boolean categoryDeleteHasActivities) {
        this.categoryDeleteHasActivities = categoryDeleteHasActivities;
    }

    /**
     * @return the categoryIdDelete
     */
    public Long getCategoryIdDelete() {
        return categoryIdDelete;
    }

    /**
     * @param categoryIdDelete the categoryIdDelete to set
     */
    public void setCategoryIdDelete(Long categoryIdDelete) {
        this.categoryIdDelete = categoryIdDelete;
    }

    /**
     * @return the categoryNameDelete
     */
    public String getCategoryNameDelete() {
        return categoryNameDelete;
    }

    /**
     * @param categoryNameDelete the categoryNameDelete to set
     */
    public void setCategoryNameDelete(String categoryNameDelete) {
        this.categoryNameDelete = categoryNameDelete;
    }
    
    
    
}
