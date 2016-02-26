
package memogem.coreapplication;
import java.util.ArrayList;
import java.util.List;

/**
 * Class is used to timestamp Cards and particular study-times. This is used in order to calculate
 * best possible study times for the material.
 */
public class Timestamp {
    private long startTime;
    private long endTime;
    
    public Timestamp() {
        startTime = 0;
        endTime = 0;
    }
    /**
     * Tells the timer to begin measuring time.
     */
    public void start() {
        startTime = System.nanoTime();
    }
    /**
     * Tells the timer to stop.
     */
    public void stop() {
        endTime = System.nanoTime();
    }

    /**
     * Returns the time used to study the card.
     * @return Time as positive integer (in milliseconds).
     */
    public int calculateSpeed() {
        return (int) (endTime - startTime) / 1000000;
    }
    
    
    
}
