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
public class DeleteTimeSlotException extends Exception{

    /**
     * Creates a new instance of <code>DeleteTimeSlotException</code> without
     * detail message.
     */
    public DeleteTimeSlotException() {
    }

    /**
     * Constructs an instance of <code>DeleteTimeSlotException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public DeleteTimeSlotException(String msg) {
        super(msg);
    }
}
