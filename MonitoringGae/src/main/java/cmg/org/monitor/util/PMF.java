/**
 * 
 */
package cmg.org.monitor.util;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

/**
 * Helper class to retrieve PersistenceManager instances.<br>
 * 
 * @author lamphan
 *
 */
public class PMF {
	private static final PersistenceManagerFactory pmfInstance = JDOHelper
            .getPersistenceManagerFactory("transactions-optional");

    private PMF() {
    }

    public static PersistenceManagerFactory get() {
        return pmfInstance;
    }
}
