package cmg.org.monitor.ext.model;

import java.io.Serializable;

public class FreeObject implements Serializable {
	
	/** Default static final UUID */
	private static final long serialVersionUID = 1L;


    private String typeName;
    private String total;
    private String used;
    private String free;
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * documents description.
     *
     * @return  the return value
     */
    @Override public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" [");
        sb.append("Typename: " + typeName);
        sb.append(", Total: " + total);
        sb.append(", Used: " + used);
        sb.append(", Free: " + free);
        sb.append("] ");

        return sb.toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  typeName  DOCUMENT ME!
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    /**
     * Gets total memory.
     *
     * @return  DOCUMENT ME!
     */
    public String getTotal() {
        return total;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  total  DOCUMENT ME!
     */
    public void setTotal(String total) {
        this.total = total;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getUsed() {
        return used;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  used  DOCUMENT ME!
     */
    public void setUsed(String used) {
        this.used = used;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getFree() {
        return free;
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @param free
	 *            DOCUMENT ME!
	 */
    public void setFree(String free) {
        this.free = free;
    }
}
