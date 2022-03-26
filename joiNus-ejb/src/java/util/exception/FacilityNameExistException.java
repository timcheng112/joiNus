/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author Jeremy
 */
public class FacilityNameExistException extends Exception {

    /**
     * Creates a new instance of <code>FacilityNameExistException</code> without
     * detail message.
     */
    public FacilityNameExistException() {
    }

    /**
     * Constructs an instance of <code>FacilityNameExistException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public FacilityNameExistException(String msg) {
        super(msg);
    }
}
