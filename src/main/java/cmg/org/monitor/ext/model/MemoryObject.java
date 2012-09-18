package cmg.org.monitor.ext.model;

import java.io.Serializable;

/**
 * @author Lamphan
 *
 */
public class MemoryObject implements Serializable {
	private static final long serialVersionUID = 1L;


    private FreeObject mem;
    private FreeObject swap;
    private String ram;
    
    @Override public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" {");
        sb.append("Mem: " + mem.toString());
        sb.append(", Swap: " + swap.toString());
        sb.append(", RAM: " + ram);
        sb.append("} ");

        return sb.toString();
    }

    /**
     * Gets property.
     *
     * @return  the return value
     */
    public String getRam() {
        return ram;
    }

    /**
     * Sets property.
     *
     * @param  ram  the parameter
     */
    public void setRam(String ram) {
        this.ram = ram;
    }

    /**
     * Method description.
     *
     * @return  the return value
     */
    public FreeObject getMem() {
        return mem;
    }

    /**
     * Method description.
     *
     * @param  mem  the parameter
     */
    public void setMem(FreeObject mem) {
        this.mem = mem;
    }

    /**
     * Method description.
     *
     * @return  the return value
     */
    public FreeObject getSwap() {
        return swap;
    }

    /**
     * Method description.
     *
     * @param  swap  the parameter
     */
    public void setSwap(FreeObject swap) {
        this.swap = swap;
    }
}
