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
public class UpdateFacilityException extends Exception {

    /**
     * Creates a new instance of <code>UpdateFacilityException</code> without
     * detail message.
     */
    public UpdateFacilityException() {
    }

    /**
     * Constructs an instance of <code>UpdateFacilityException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public UpdateFacilityException(String msg) {
        super(msg);
    }
}
