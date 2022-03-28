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
public class TimeSlotNotFoundException extends Exception{

    /**
     * Creates a new instance of <code>TimeSlotNotFoundException</code> without
     * detail message.
     */
    public TimeSlotNotFoundException() {
    }

    /**
     * Constructs an instance of <code>TimeSlotNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public TimeSlotNotFoundException(String msg) {
        super(msg);
    }
}
