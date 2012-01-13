package cmg.org.monitor.exception;


public class ErrorLevels {
	
	/** Archive errorLevel. */
    @SuppressWarnings("unused")
    private int errorLevel = 0;

    /** Archive INFO. */
    public static final ErrorLevels INFO = new ErrorLevels(0);

    /** Archive WARNING. */
    public static final ErrorLevels WARNING = new ErrorLevels(1);

    /** Archive ERROR. */
    public static final ErrorLevels ERROR = new ErrorLevels(2);

    /** Archive FATAL. */
    public static final ErrorLevels FATAL = new ErrorLevels(3);

    /**
     * ErrorLevel constructor.<br>
     */
    public ErrorLevels() {
    }

    /**
     * ErrorLevel constructor with parameters.<br>
     * 
     * @param errLevel error type.
     */
    private ErrorLevels(int errLevel) {
        errorLevel = errLevel;
    }
}
