package cmg.org.monitor.exception;

import java.io.Serializable;

/**
 * @author lamphan
 * @version 1.0
 */
public class MonitorException extends Exception implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
     * Creates a new MonitorException object.
     */
    public MonitorException() {
      
    }
    
	/**
     * Creates a new MonitorException object.
     *
     * @param  message  DOCUMENT ME!
     */
    public MonitorException(String message) {
        super(message);
       
    }
    
    /**
     * @param message
     * @param cause
     */
    public MonitorException(String message, Throwable cause) {
        super(message, cause);
      }
    
    /**
     * @param cause
     */
    public MonitorException(Throwable cause) {
        super(cause);
      }
}
