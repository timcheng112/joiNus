/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author User
 */
public class BookingNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>BookingNotFoundException</code> without
     * detail message.
     */
    public BookingNotFoundException() {
    }

    /**
     * Constructs an instance of <code>BookingNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public BookingNotFoundException(String msg) {
        super(msg);
    }
}
