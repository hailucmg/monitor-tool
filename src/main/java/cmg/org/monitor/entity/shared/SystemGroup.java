package cmg.org.monitor.entity.shared;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.gwt.user.client.rpc.IsSerializable;

/** 
	* DOCME
	* 
	* @Creator Hai Lu
	* @author $Author$
	* @version $Revision$
	* @Last changed: $LastChangedDate$
*/
@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class SystemGroup implements IsSerializable  {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
	private String id;
	
	@Persistent
	private String name;

	/** 
	 * @return the name 
	 */
	public String getName() {
		return name;
	}

	/** 
	 * @param name the name to set 
	 */
	
	public void setName(String name) {
		this.name = name;
	}
}
