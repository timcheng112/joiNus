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
public class UpdateNormalUserException extends Exception{

    /**
     * Creates a new instance of <code>UpdateNormalUserException</code> without
     * detail message.
     */
    public UpdateNormalUserException() {
    }

    /**
     * Constructs an instance of <code>UpdateNormalUserException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public UpdateNormalUserException(String msg) {
        super(msg);
    }
}
