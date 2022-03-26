/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AdminEntity;
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
import util.exception.AdminNotFoundException;
import util.exception.AdminUsernameExistException;
import util.exception.DeleteAdminException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;
import util.security.CryptographicHelper;

/**
 *
 * @author User
 */
@Stateless
public class AdminEntitySessionBean implements AdminEntitySessionBeanLocal {

    @PersistenceContext(unitName = "joiNus-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public AdminEntitySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @Override
    public Long createNewAdmin(AdminEntity newAdminEntity) throws AdminUsernameExistException, UnknownPersistenceException, InputDataValidationException
    {
        Set<ConstraintViolation<AdminEntity>>constraintViolations = validator.validate(newAdminEntity);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                em.persist(newAdminEntity);
                em.flush();

                return newAdminEntity.getUserId();
            }
            catch(PersistenceException ex)
            {
                if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
                {
                    if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                    {
                        throw new AdminUsernameExistException();
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
    public List<AdminEntity> retrieveAllAdmins()
    {
        Query query = em.createQuery("SELECT a FROM AdminEntity a");
        
        return query.getResultList();
    }
    
    @Override
    public AdminEntity retrieveAdminByUserId(Long userId) throws AdminNotFoundException
    {
        AdminEntity adminEntity = em.find(AdminEntity.class, userId);
        
        if(adminEntity != null)
        {
            return adminEntity;
        }
        else
        {
            throw new AdminNotFoundException("Admin ID " + userId + " does not exist!");
        }
    }
    
    @Override
    public AdminEntity retrieveAdminByUsername(String username) throws AdminNotFoundException
    {
        Query query = em.createQuery("SELECT a FROM AdminEntity a WHERE a.username = :inUsername");
        query.setParameter("inUsername", username);
        
        try
        {
            return (AdminEntity)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new AdminNotFoundException("Admin Username " + username + " does not exist!");
        }
    }
    
    @Override
    public AdminEntity adminLogin(String username, String password) throws InvalidLoginCredentialException
    {
        try
        {
            AdminEntity adminEntity = retrieveAdminByUsername(username);
            String passwordHash = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + adminEntity.getSalt()));
            
            if(adminEntity.getPassword().equals(passwordHash))
            {             
                return adminEntity;
            }
            else
            {
                throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
            }
        }
        catch(AdminNotFoundException ex)
        {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        }
    }
    
//  only need change password method
    
    @Override
    public void deleteAdmin(Long adminId) throws AdminNotFoundException, DeleteAdminException
    {
        AdminEntity adminEntityToRemove = retrieveAdminByUserId(adminId);
        
        if(!adminEntityToRemove.getIsSuperAdmin())
        {
            em.remove(adminEntityToRemove);
        }
        else
        {
            // New in v4.1 to prevent deleting staff with existing sale transaction(s)
            throw new DeleteAdminException("Admin ID " + adminId + " is a Super Admin and cannot be deleted!");
        }
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<AdminEntity>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
}
