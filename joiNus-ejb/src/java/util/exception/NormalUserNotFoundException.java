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
public class NormalUserNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>NormalUserNotFoundException</code>
     * without detail message.
     */
    public NormalUserNotFoundException() {
    }

    /**
     * Constructs an instance of <code>NormalUserNotFoundException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public NormalUserNotFoundException(String msg) {
        super(msg);
    }
}
