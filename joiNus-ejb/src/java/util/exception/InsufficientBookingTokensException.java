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
public class InsufficientBookingTokensException extends Exception {

    /**
     * Creates a new instance of <code>InsufficientBookingTokensException</code>
     * without detail message.
     */
    public InsufficientBookingTokensException() {
    }

    /**
     * Constructs an instance of <code>InsufficientBookingTokensException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public InsufficientBookingTokensException(String msg) {
        super(msg);
    }
}
