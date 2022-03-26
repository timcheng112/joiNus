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
public class DeleteFacilityException extends Exception {

    /**
     * Creates a new instance of <code>DeleteFacilityException</code> without
     * detail message.
     */
    public DeleteFacilityException() {
    }

    /**
     * Constructs an instance of <code>DeleteFacilityException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public DeleteFacilityException(String msg) {
        super(msg);
    }
}
