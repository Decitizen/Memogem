
package memogem.coreapplication;

/**
 * Stores card's type.
 * Two possibilities: 
 * ANSWER: answerable "quiz" card with integer value 0 attached
 * FLASHCARD: basic flashcard (front and back) with integer value 1 attached
 */
public enum CardType {
    ANSWER(0),
    FLASHCARD(1);
    
    private int code;
    
    /**
     * Stores the attached integer code.
     * @param code integer 
     */
    private CardType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
    
}
