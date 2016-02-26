
package memogem.coreapplication;
/**
*This enumeration holds the different types of connections that are realized as
*proper connections in the Connection-class.
*/
public enum LogicalConnection {
    CAUSE(2), 
    EFFECT(2),
    CORRELATION(1);
    
    private int code;
    
    private LogicalConnection(int code) {
        this.code = code;
    }
    
    public int getCode() {
        return code;
    }
}