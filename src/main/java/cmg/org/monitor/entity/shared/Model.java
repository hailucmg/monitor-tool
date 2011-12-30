package cmg.org.monitor.entity.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Interface that all our models/JDO entities must implement. Here we will
 * enforce any cross-model logic.
 * 
 * @author lamphan
 * 
 */
public interface Model extends IsSerializable {
   
	String KEY_FIELD = "encodedKey";
	String KEY_TYPE = "String";

	String getId();

}
