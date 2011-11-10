package cmg.org.monitor.entity.shared;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * @author admin
 *
 */

@SuppressWarnings("serial")
@PersistenceCapable
public class GroupMonitor implements Model {
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
	private String encodedKey;

	@Persistent
	private String name;

	public GroupMonitor(String name) {
		
		this.name = name;
	}

	@Override
    public String getId() {
        return encodedKey;
    }

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
}
