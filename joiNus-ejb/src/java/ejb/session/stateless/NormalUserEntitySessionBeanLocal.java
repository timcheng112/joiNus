/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.NormalUserEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.DeleteNormalUserException;
import util.exception.InputDataValidationException;
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
    
}