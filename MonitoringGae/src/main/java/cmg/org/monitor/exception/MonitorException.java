package cmg.org.monitor.exception;

/**
 * @author lamphan
 * 
 */
public class MonitorException extends BaseException {

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

	/**
     * MonitorException constructor.<br>
     */
	public MonitorException() {
		super();
	}

	public MonitorException(String message) {

	}

	/**
	 * @param aThrowable
	 * @param aUserMessageKey
	 * @param level
	 * @param errorCode
	 */
	public MonitorException(Throwable aThrowable, String aUserMessageKey
			) {
		super(aThrowable, aUserMessageKey);
	}
}
