
package memogem.coreapplication;
/**
*This enumeration holds the different types of connections that are realized as
*proper connections in the Connection-class.
*/
public enum LogicalConnection {
    /**
     *  type Cause-relationship, card carrying this has a linear
     *  causal relationship with the other card.
     */
    CAUSE(2), 
    /**
     *  type Effect-relationship, card carrying this has a reverse
     *  causal relationship with the other card.
     */
    EFFECT(2),
    /**
     *  type correlative-relationship, card carrying this has a positive 
     *  linear (but rather weak) relationship with the other card.
     */
    CORRELATION(1);
    
    private int code;
    /**
     * Code attached to the connection, can be used to
     * turn connection's value into a numbered value.
     * @param code integer
     */
    private LogicalConnection(int code) {
        this.code = code;
    }
    
    public int getCode() {
        return code;
    }
}