/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.FacilityEntity;
import entity.ImageEntity;
import entity.TimeSlotEntity;
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

    public FacilityEntity createNewFacility(FacilityEntity newFacilityEntity) throws FacilityNameExistException, UnknownPersistenceException, InputDataValidationException, CreateNewFacilityException;

    public List<FacilityEntity> retrieveAllFacilities();

    public FacilityEntity retrieveFacilityByFacilityId(Long facilityId) throws FacilityNotFoundException;

    public FacilityEntity retrieveFacilityByFacilityName(String facilityName) throws FacilityNotFoundException;

    public void updateFacility(FacilityEntity facilityEntity) throws FacilityNotFoundException, UpdateFacilityException, InputDataValidationException;

    public void deleteFacility(Long facilityId) throws FacilityNotFoundException, DeleteFacilityException;

    public List<TimeSlotEntity> retrieveTimeSlotsByFacility(Long facilityId) throws FacilityNotFoundException;

    public List<TimeSlotEntity> retrieveAvailableTimeSlotsByFacility(Long facilityId) throws FacilityNotFoundException;

    public ImageEntity persistImage(ImageEntity img);

    public void updateFacilityNew(FacilityEntity facilityEntity, int newOpening, int newClosing) throws FacilityNotFoundException, UpdateFacilityException, InputDataValidationException;

    public List<FacilityEntity> retrieveAllFacilitiesByClub(String club);

    
}
