/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.FacilityEntity;
import entity.NormalUserEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.DeleteNormalUserException;
import util.exception.InputDataValidationException;
import util.exception.InsufficientBookingTokensException;
import util.exception.InvalidLoginCredentialException;
import util.exception.NormalUserNameExistException;
import util.exception.NormalUserNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateNormalUserException;

/**
 *
 * @author wongs
 */
@Local
public interface NormalUserEntitySessionBeanLocal {

    public void deleteNormal(Long normalUserId) throws NormalUserNotFoundException, DeleteNormalUserException;

    public void updateNormalUser(NormalUserEntity normalUserEntity) throws NormalUserNotFoundException, UpdateNormalUserException, InputDataValidationException;

    public NormalUserEntity retrieveNormalUserByUserId(Long normalUserId) throws NormalUserNotFoundException;

    public List<NormalUserEntity> retrieveAllNormalUser();

    public NormalUserEntity createNewNormalUser(NormalUserEntity newNormalUser) throws UnknownPersistenceException, InputDataValidationException, NormalUserNameExistException;
    
    public void punishUser(Long userId);

    public NormalUserEntity retrieveNormalUserByUsername(String username) throws NormalUserNotFoundException;

    public NormalUserEntity normalUserLogin(String username, String password) throws InvalidLoginCredentialException;

    public List<NormalUserEntity> retrieveLeaderboard();

    public List<NormalUserEntity> retrieveNormalUsersByName(String name) throws NormalUserNotFoundException;

    public int retrieveLeaderboardRank(NormalUserEntity currUser);

    public void creditTokens(List<NormalUserEntity> users);

    public Long editNormalUser(NormalUserEntity normalUserEntity) throws NormalUserNotFoundException, UpdateNormalUserException, InputDataValidationException;

    public void deductTokens(Boolean isHosting, NormalUserEntity user, FacilityEntity facilityEntity) throws InsufficientBookingTokensException;

    public Long changePassword(NormalUserEntity normalUserEntity, String newPassword) throws NormalUserNotFoundException, UpdateNormalUserException, InputDataValidationException;

}
