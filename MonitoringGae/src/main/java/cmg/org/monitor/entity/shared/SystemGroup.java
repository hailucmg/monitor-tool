package cmg.org.monitor.entity.shared;

import java.util.ArrayList;
import java.util.List;

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
	
	@Persistent
	private String description;
	
	@Persistent
	private String[] userIDs;
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

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

	/** 
	 * @return the id 
	 */
	public String getId() {
		return id;
	}

	/** 
	 * @param id the id to set 
	 */
	
	public void setId(String id) {
		this.id = id;
	}

	/** 
	 * @return the userIDs 
	 */
	public List<String> getUserIDs() {
		if (userIDs == null) {
			return null;
		} else {
			List<String> list = new ArrayList<String>();
			for (int i = 0; i < userIDs.length; i++) {
				list.add(userIDs[i]);
			}
			return list;
		}		
	}

	/** 
	 * @param userIDs the userIDs to set 
	 */
	
	public void setUserIDs(List<String> listUserIDs) {
		if (listUserIDs != null && listUserIDs.size() > 0) {
			userIDs = new String[listUserIDs.size()];
			listUserIDs.toArray(userIDs);
		}
	}
}
