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
public class CreateNewTimeSlotException extends Exception{

    /**
     * Creates a new instance of <code>CreateNewTimeSlotException</code> without
     * detail message.
     */
    public CreateNewTimeSlotException() {
    }

    /**
     * Constructs an instance of <code>CreateNewTimeSlotException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public CreateNewTimeSlotException(String msg) {
        super(msg);
    }
}
