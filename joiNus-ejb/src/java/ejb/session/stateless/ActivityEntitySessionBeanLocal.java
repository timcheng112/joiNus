/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ActivityEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.ActivityNotFoundException;
import util.exception.BookingNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author User
 */
@Local
public interface ActivityEntitySessionBeanLocal {

    public List<ActivityEntity> retrieveAllActivities();

    public ActivityEntity retrieveActivityByActivityId(Long activityId) throws ActivityNotFoundException;

    public List<ActivityEntity> retrieveActivitiesByActivityName(String activityName);

    public List<ActivityEntity> filterActivitiesByCategory(String categoryName);

    public void deleteActivity(Long activityId) throws ActivityNotFoundException, BookingNotFoundException;

    public void deleteImages(ActivityEntity activityEntityToRemove);

    public void deleteComments(ActivityEntity activityEntityToRemove);

    public ActivityEntity createNewActivity(ActivityEntity newActivityEntity) throws UnknownPersistenceException, InputDataValidationException;

    public List<ActivityEntity> retrieveMyActivities(Long userId);

}
