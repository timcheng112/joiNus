/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.FacilityEntity;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CreateNewFacilityException;
import util.exception.DeleteFacilityException;
import util.exception.FacilityNameExistException;
import util.exception.FacilityNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateFacilityException;

/**
 *
 * @author Jeremy
 */
@Stateless
public class FacilityEntitySessionBean implements FacilityEntitySessionBeanLocal {

    @PersistenceContext(unitName = "joiNus-ejbPU")
    private EntityManager em; 
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    
    
    public FacilityEntitySessionBean()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    
    @Override
    public FacilityEntity createNewFacility(FacilityEntity newFacilityEntity, Long facilityId) throws FacilityNameExistException, UnknownPersistenceException, InputDataValidationException, CreateNewFacilityException
    {
        Set<ConstraintViolation<FacilityEntity>>constraintViolations = validator.validate(newFacilityEntity);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                em.persist(newFacilityEntity);
                //set linkages
                em.flush();

                return newFacilityEntity;
            }
            catch(PersistenceException ex)
            {
                if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
                {
                    if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                    {
                        throw new FacilityNameExistException();
                    }
                    else
                    {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                }
                else
                {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    
    
    @Override
    public List<FacilityEntity> retrieveAllFacilities()
    {
        Query query = em.createQuery("SELECT f FROM FacilityEntity f ORDER BY f.facilityName ASC");
        
        return query.getResultList();
    }
    
    
    
    @Override
    public FacilityEntity retrieveFacilityByFacilityId(Long facilityId) throws FacilityNotFoundException
    {
        FacilityEntity facilityEntity = em.find(FacilityEntity.class, facilityId);
        
        if(facilityEntity != null)
        {
            return facilityEntity;
        }
        else
        {
            throw new FacilityNotFoundException("Facility ID " + facilityId + " does not exist!");
        }               
    }
    
    
    
    @Override
    public FacilityEntity retrieveFacilityByFacilityName(String facilityName) throws FacilityNotFoundException
    {
        Query query = em.createQuery("SELECT f FROM FacilityEntity f WHERE f.facilityName = :facilityName");
        query.setParameter("facilityName", facilityName);
        
        try
        {
            return (FacilityEntity)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new FacilityNotFoundException("Facility name " + facilityName + " does not exist!");
        }
    }
    
    @Override
    public void updateFacility(FacilityEntity facilityEntity) throws FacilityNotFoundException, UpdateFacilityException, InputDataValidationException
    {
        if(facilityEntity != null && facilityEntity.getFacilityId()!= null)
        {
            Set<ConstraintViolation<FacilityEntity>>constraintViolations = validator.validate(facilityEntity);
        
            if(constraintViolations.isEmpty())
            {
                FacilityEntity facilityEntityToUpdate = retrieveFacilityByFacilityId(facilityEntity.getFacilityId());

                if(facilityEntityToUpdate.getFacilityName().equals(facilityEntity.getFacilityName()))
                {
                    facilityEntityToUpdate.setFacilityName(facilityEntity.getFacilityName());
                    facilityEntityToUpdate.setClub(facilityEntity.getClub());
                    facilityEntityToUpdate.setTokenCost(facilityEntity.getTokenCost());
                    facilityEntityToUpdate.setCapacity(facilityEntity.getCapacity());
                    facilityEntityToUpdate.setTimeSlots(facilityEntity.getTimeSlots());
                }
                else
                {
                    throw new UpdateFacilityException("Name of facility to be updated does not match the existing record");
                }
            }
            else
            {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        }
        else
        {
            throw new FacilityNotFoundException("Facility ID not provided for facility to be updated");
        }
    }
    
    @Override
    public void deleteFacility(Long facilityId) throws FacilityNotFoundException, DeleteFacilityException
    {
        FacilityEntity facilityEntityToRemove = retrieveFacilityByFacilityId(facilityId);
        
        //check dependencies
        
        if(true)
        {
            em.remove(facilityEntityToRemove);
        }
        else
        {
            throw new DeleteFacilityException("Facility ID " + facilityId + " cannot be deleted!");
        }
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<FacilityEntity>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
    
}
