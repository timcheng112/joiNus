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
public class DeleteNormalUserException extends Exception{

    /**
     * Creates a new instance of <code>DeleteNormalUserException</code> without
     * detail message.
     */
    public DeleteNormalUserException() {
    }

    /**
     * Constructs an instance of <code>DeleteNormalUserException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public DeleteNormalUserException(String msg) {
        super(msg);
    }
}
