/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eazycommon.exception;

/**
 *
 * @author DuongLTD
 */
public class EazyException extends Exception {
    private int errorCode;

    public EazyException(int message){
        super(String.valueOf(message));
        this.errorCode = message;
    }

    
    public EazyException(Throwable cause) {
        super(cause);
    }
    
    /**
     * @return the errorCode
     */
    public int getErrorCode() {
        return errorCode;
    }



}
