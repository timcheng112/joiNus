/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ActivityEntity;
import entity.CommentEntity;
import entity.ImageEntity;
import entity.NormalUserEntity;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.ActivityNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author User
 */
@Stateless
public class ActivityEntitySessionBean implements ActivityEntitySessionBeanLocal {

    @PersistenceContext(unitName = "joiNus-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public ActivityEntitySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public ActivityEntity createNewActivity(ActivityEntity newActivityEntity) throws UnknownPersistenceException, InputDataValidationException {
        Set<ConstraintViolation<ActivityEntity>> constraintViolations = validator.validate(newActivityEntity);

        if (constraintViolations.isEmpty()) {
            try {
                em.persist(newActivityEntity);
                //set linkages
                em.flush();

                return newActivityEntity;
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    throw new UnknownPersistenceException(ex.getMessage());
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public List<ActivityEntity> retrieveAllActivities() {
        Query query = em.createQuery("SELECT a FROM ActivityEntity a ORDER BY a.activityName ASC");

        return query.getResultList();
    }

    @Override
    public ActivityEntity retrieveActivityByActivityId(Long activityId) throws ActivityNotFoundException {
        ActivityEntity activityEntity = em.find(ActivityEntity.class, activityId);

        if (activityEntity != null) {
            return activityEntity;
        } else {
            throw new ActivityNotFoundException("Activity ID " + activityId + " does not exist!");
        }
    }

    @Override
    public List<ActivityEntity> retrieveActivitiesByActivityName(String activityName) {
        Query query = em.createQuery("SELECT a FROM ActivityEntity a WHERE a.activityName = :activityName");
        query.setParameter("activityName", activityName);
        List<ActivityEntity> activityEntities = query.getResultList();

        for (ActivityEntity activityEntity : activityEntities) {
            activityEntity.getActivityOwner();
            activityEntity.getParticipants().size();
            activityEntity.getCategory();
            activityEntity.getBooking();
            activityEntity.getComments().size();
            activityEntity.getGallery().size();
        }

        return activityEntities;
    }

    @Override
    public List<ActivityEntity> filterActivitiesByCategory(String categoryName) {
        Query query = em.createQuery("SELECT a FROM ActivityEntity a WHERE a.category.categoryName = :categoryName");
        query.setParameter("categoryName", categoryName);
        List<ActivityEntity> activityEntities = query.getResultList();

        for (ActivityEntity activityEntity : activityEntities) {
            activityEntity.getActivityOwner();
            activityEntity.getParticipants().size();
            activityEntity.getCategory();
            activityEntity.getBooking();
            activityEntity.getComments().size();
            activityEntity.getGallery().size();
        }

        return activityEntities;
    }

    @Override
    public void deleteActivity(Long activityId) throws ActivityNotFoundException {

        ActivityEntity activityEntityToRemove = retrieveActivityByActivityId(activityId);

        activityEntityToRemove.getCategory().getActivities().remove(activityEntityToRemove);

        for (NormalUserEntity normalUserEntity : activityEntityToRemove.getParticipants()) {
            normalUserEntity.getActivitiesParticipated().remove(activityEntityToRemove);
        }

        activityEntityToRemove.getActivityOwner().getActivitiesOwned().remove(activityEntityToRemove);

        activityEntityToRemove.getParticipants().clear();

        deleteImages(activityEntityToRemove);
        activityEntityToRemove.getGallery().clear();

        deleteComments(activityEntityToRemove);
        activityEntityToRemove.getComments().clear();

        em.remove(activityEntityToRemove);

    }

    @Override
    public void deleteImages(ActivityEntity activityEntityToRemove) {
        List<ImageEntity> imageEntities = activityEntityToRemove.getGallery();

        for (ImageEntity imageEntity : imageEntities) {
            em.remove(imageEntity);
        }
    }

    @Override
    public void deleteComments(ActivityEntity activityEntityToRemove) {
        List<CommentEntity> commentEntities = activityEntityToRemove.getComments();

        for (CommentEntity commentEntity : commentEntities) {
            em.remove(commentEntity);
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<ActivityEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
