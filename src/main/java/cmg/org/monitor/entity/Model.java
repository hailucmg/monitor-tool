package cmg.org.monitor.entity;

import java.io.Serializable;

/**
 * Interface that all our models/JDO entities must implement. Here we will
 * enforce any cross-model logic.
 * 
 * @author lamphan
 * 
 */
public interface Model extends Serializable {
   
	String KEY_FIELD = "encodedKey";
	String KEY_TYPE = "String";

	String getId();

}
