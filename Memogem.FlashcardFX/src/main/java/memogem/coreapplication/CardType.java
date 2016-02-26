
package memogem.coreapplication;

/**
 * Stores card's type. 
 */
public enum CardType {
    ANSWER(0),
    FLASHCARD(1);
    
    private int code;

    private CardType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
    
}
