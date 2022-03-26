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
public class CreateNewFacilityException extends Exception {

    /**
     * Creates a new instance of <code>CreateNewFacilityException</code> without
     * detail message.
     */
    public CreateNewFacilityException() {
    }

    /**
     * Constructs an instance of <code>CreateNewFacilityException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public CreateNewFacilityException(String msg) {
        super(msg);
    }
}
