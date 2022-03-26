/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.FacilityEntity;
import java.util.List;
import javax.ejb.Local;
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
@Local
public interface FacilityEntitySessionBeanLocal {

    public FacilityEntity createNewFacility(FacilityEntity newFacilityEntity, Long facilityId) throws FacilityNameExistException, UnknownPersistenceException, InputDataValidationException, CreateNewFacilityException;

    public List<FacilityEntity> retrieveAllFacilities();

    public FacilityEntity retrieveFacilityByFacilityId(Long facilityId) throws FacilityNotFoundException;

    public FacilityEntity retrieveFacilityByFacilityName(String facilityName) throws FacilityNotFoundException;

    public void updateFacility(FacilityEntity facilityEntity) throws FacilityNotFoundException, UpdateFacilityException, InputDataValidationException;

    public void deleteFacility(Long facilityId) throws FacilityNotFoundException, DeleteFacilityException;

    
}
