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
public class MaxParticipantsExceededException extends Exception {

    /**
     * Creates a new instance of <code>MaxParticipantsExceededException</code>
     * without detail message.
     */
    public MaxParticipantsExceededException() {
    }

    /**
     * Constructs an instance of <code>MaxParticipantsExceededException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public MaxParticipantsExceededException(String msg) {
        super(msg);
    }
}
