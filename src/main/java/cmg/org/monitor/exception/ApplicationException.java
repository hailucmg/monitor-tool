package cmg.org.monitor.exception;

/**
 * @author lamphan
 * @version 1.0
 */
public class ApplicationException extends BaseException {

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

	/**
     * MonitorException constructor.<br>
     */
	public ApplicationException() {
		super();
	}

	public ApplicationException(String message) {

	}

	/**
	 * @param aThrowable
	 * @param aUserMessageKey
	 * @param level
	 * @param errorCode
	 */
	public ApplicationException(Throwable aThrowable, String aUserMessageKey
			) {
		super(aThrowable, aUserMessageKey);
	}
}
