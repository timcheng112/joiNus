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
public class NormalUserAlreadySignedUpException extends Exception{

    /**
     * Creates a new instance of <code>NormalUserNameExistException</code>
     * without detail message.
     */
    public NormalUserAlreadySignedUpException() {
    }

    /**
     * Constructs an instance of <code>NormalUserNameExistException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public NormalUserAlreadySignedUpException(String msg) {
        super(msg);
    }
}
