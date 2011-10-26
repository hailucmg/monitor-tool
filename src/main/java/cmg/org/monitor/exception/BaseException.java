package cmg.org.monitor.exception;

public class BaseException {
	private static final String INF_MESSAGE = "Info Message: ID - ";

	private static final String WARN_MESSAGE = "Warn Message: ID - ";

	private static final String ERROR_MESSAGE = "Error Message: ID - ";

	private static final String FATAL_MESSAGE = "Fatal Message: ID - ";

	private Throwable throwable = null;

	private String userMessageKey = null;

	/** Status log. */
	private boolean logged = false;


	/**
	 * BaseException constructor.<br>
	 */
	public BaseException() {
	}

	/**
	 * BaseException constructor.<br>
	 * 
	 * <pre>
	 * Constructor for all class variables.
	 * </pre>
	 * 
	 * @param aThrowable
	 *            Exception passing.
	 * @param aUserMessageKey
	 *            Key message .
	 *            
	 */
	public BaseException(Throwable aThrowable, String aUserMessageKey) {
		this.throwable = aThrowable;
		this.userMessageKey = aUserMessageKey;

	}
	
    public String getUserMessageKey() {
        return userMessageKey;
    }

    public boolean isLogged() {
        return logged;
    }

}
