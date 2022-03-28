/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AdminEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.AdminNotFoundException;
import util.exception.AdminUsernameExistException;
import util.exception.DeleteAdminException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author User
 */
@Local
public interface AdminEntitySessionBeanLocal {

    public AdminEntity createNewAdmin(AdminEntity newAdminEntity) throws AdminUsernameExistException, UnknownPersistenceException, InputDataValidationException;

    public List<AdminEntity> retrieveAllAdmins();

    public AdminEntity retrieveAdminByUserId(Long userId) throws AdminNotFoundException;

    public AdminEntity retrieveAdminByUsername(String username) throws AdminNotFoundException;

    public AdminEntity adminLogin(String username, String password) throws InvalidLoginCredentialException;

    public void deleteAdmin(Long adminId) throws AdminNotFoundException, DeleteAdminException;
}
