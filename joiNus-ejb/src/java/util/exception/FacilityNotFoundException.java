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
public class FacilityNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>FacilityNotFoundException</code> without
     * detail message.
     */
    public FacilityNotFoundException() {
    }

    /**
     * Constructs an instance of <code>FacilityNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public FacilityNotFoundException(String msg) {
        super(msg);
    }
}
