/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.NormalUserEntity;
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
import util.exception.DeleteNormalUserException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.NormalUserNameExistException;
import util.exception.NormalUserNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateNormalUserException;
import util.security.CryptographicHelper;

/**
 *
 * @author wongs
 */
@Stateless
public class NormalUserEntitySessionBean implements NormalUserEntitySessionBeanLocal {

    @PersistenceContext(unitName = "joiNus-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public NormalUserEntitySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public void persist(Object object) {
        em.persist(object);
    }

    @Override
    public NormalUserEntity createNewNormalUser(NormalUserEntity newNormalUser) throws UnknownPersistenceException, InputDataValidationException, NormalUserNameExistException {
        System.out.println("ejb.session.stateless.NormalUserEntitySessionBean.createNewNormalUser()");
        Set<ConstraintViolation<NormalUserEntity>> constraintViolations = validator.validate(newNormalUser);

        System.err.println("violation clear");
        if (constraintViolations.isEmpty()) {
            System.err.println("violation empty");
            try {
                em.persist(newNormalUser);
                //set linkages
                em.flush();

                return newNormalUser;
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new NormalUserNameExistException();
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public List<NormalUserEntity> retrieveAllNormalUser() {
        Query query = em.createQuery("SELECT f FROM NormalUserEntity f ORDER BY f.name ASC");
        List<NormalUserEntity> normalUserEntities = query.getResultList();

        for (NormalUserEntity f : normalUserEntities) {
            f.getActivitiesOwned().size();
            f.getActivitiesParticipated().size();
            f.getInterests().size();
        }

        return query.getResultList();
    }
    
    @Override
    public List<NormalUserEntity> getLeaderBoard() {
        Query query = em.createQuery("Select f FROM NormalUserEntity f ORDER BY f.socialCredits DESC").setMaxResults(10);
        List<NormalUserEntity> leaderboard = query.getResultList();
        
        for (NormalUserEntity f : leaderboard) {
            f.getActivitiesOwned().size();
            f.getActivitiesParticipated().size();
            f.getInterests().size();
        }
        
        return query.getResultList();
    }

    @Override
    public NormalUserEntity retrieveNormalUserByUserId(Long normalUserId) throws NormalUserNotFoundException {
        NormalUserEntity normalUserEntity = em.find(NormalUserEntity.class, normalUserId);

        if (normalUserEntity != null) {
            normalUserEntity.getActivitiesOwned().size();
            normalUserEntity.getActivitiesParticipated().size();
            normalUserEntity.getInterests().size();
            return normalUserEntity;
        } else {
            throw new NormalUserNotFoundException("User ID =  " + normalUserId + " does not exist!");
        }
    }
    
    @Override
    public NormalUserEntity retrieveNormalUserByUsername(String username) throws NormalUserNotFoundException
    {
        Query query = em.createQuery("SELECT nu FROM NormalUserEntity nu WHERE nu.username = :inUsername");
        query.setParameter("inUsername", username);
        
        try
        {
            return (NormalUserEntity)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new NormalUserNotFoundException("Username " + username + " does not exist!");
        }
    }
    
    @Override
    public List<NormalUserEntity> retrieveNormalUsersByName(String name) throws NormalUserNotFoundException
    {
        Query query = em.createQuery("SELECT u FROM NormalUserEntity u WHERE u.name LIKE '%:inName%' OR u.username LIKE '%:inName2%'");
        query.setParameter("inName", name);
        query.setParameter("inName2", name);
        
        return query.getResultList();
    }

    @Override
    public void updateNormalUser(NormalUserEntity normalUserEntity) throws NormalUserNotFoundException, UpdateNormalUserException, InputDataValidationException {
        if (normalUserEntity != null && normalUserEntity.getUserId() != null) {
            Set<ConstraintViolation<NormalUserEntity>> constraintViolations = validator.validate(normalUserEntity);

            if (constraintViolations.isEmpty()) {
                NormalUserEntity normalUserEntityToUpdate = retrieveNormalUserByUserId(normalUserEntity.getUserId());

                if (normalUserEntityToUpdate.getUserId().equals(normalUserEntity.getUserId())) {
                    normalUserEntityToUpdate.setActivitiesOwned(normalUserEntity.getActivitiesOwned());
                    normalUserEntityToUpdate.setActivitiesParticipated(normalUserEntity.getActivitiesParticipated());
                    normalUserEntityToUpdate.setBookingTokens(normalUserEntity.getBookingTokens());
                    normalUserEntityToUpdate.setEmail(normalUserEntity.getEmail());
                    normalUserEntityToUpdate.setInterests(normalUserEntity.getInterests());
                    normalUserEntityToUpdate.setName(normalUserEntity.getName());
                    normalUserEntityToUpdate.setSocialCredits(normalUserEntity.getSocialCredits());

                } else {
                    throw new UpdateNormalUserException("User ID error");
                }
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            throw new NormalUserNotFoundException("User ID not provided for user to be updated");
        }
    }

    @Override
    public void deleteNormal(Long normalUserId) throws NormalUserNotFoundException, DeleteNormalUserException {
        NormalUserEntity userEntityToRemove = retrieveNormalUserByUserId(normalUserId);

        //check dependencies
        if (true) {
            em.remove(userEntityToRemove);
        } else {
            throw new DeleteNormalUserException("User ID " + normalUserId + " cannot be deleted!");
        }
    }
    
    @Override
    public NormalUserEntity normalUserLogin(String username, String password) throws InvalidLoginCredentialException
    {
        try
        {
            NormalUserEntity normalUserEntity = retrieveNormalUserByUsername(username);
            String passwordHash = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + normalUserEntity.getSalt()));
            
            if(normalUserEntity.getPassword().equals(passwordHash))
            {             
                return normalUserEntity;
            }
            else
            {
                throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
            }
        }
        catch(NormalUserNotFoundException ex)
        {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        }
    }

    @Override
    public void punishUser(Long userId) {
        NormalUserEntity user = em.find(NormalUserEntity.class, userId);

        int credit = user.getSocialCredits();
        credit = credit - 55;
        user.setSocialCredits(credit);
        System.out.println("User " + user.getUsername() + " punished, - 25 net social credit points");
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<NormalUserEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
    //retrieve id, retreive all, delete, update

}
