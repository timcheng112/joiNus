/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author wongs
 */
public class UpdateTimeSlotException extends Exception {

    /**
     * Creates a new instance of <code>UpdateTimeSlotException</code> without
     * detail message.
     */
    public UpdateTimeSlotException() {
    }

    /**
     * Constructs an instance of <code>UpdateTimeSlotException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public UpdateTimeSlotException(String msg) {
        super(msg);
    }
}
