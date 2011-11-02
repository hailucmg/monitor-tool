package cmg.org.monitor.ext.model;

import java.io.Serializable;

/**
 * @author lamphan
 * @version 1.0
 * 
 */
public class URLPageObject implements Serializable {

	/** Default static final UID value */
	private static final long serialVersionUID = 1L;
	
    private CPUObject cpu;
    private MemoryObject mem;


    /**
     * documents description.
     *
     * @return  the return value
     */
    @Override public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\n " + cpu.toString());
        sb.append("\n " + mem.toString());
        sb.append("}");

        return sb.toString();
    }

    

    /**
     * Gets property.
     *
     * @return  the return value
     */
    public CPUObject getCpu() {
        return cpu;
    }

    /**
     * Sets property.
     *
     * @param  cpu  the parameter
     */
    public void setCpu(CPUObject cpu) {
        this.cpu = cpu;
    }

    

    /**
     * Gets property.
     *
     * @return  the return value
     */
    public MemoryObject getMem() {
        return mem;
    }

    /**
     * Sets property.
     *
     * @param  mem  the parameter
     */
    public void setMem(MemoryObject mem) {
        this.mem = mem;
    }
	
}
