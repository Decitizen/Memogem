
package memogem.coreapplication;

import java.util.Comparator;

/**
 * This comparator class can be used in order to sort cards
 * due to their AVGdifficulty-ratings.
 */
public class DifficultyComparator implements Comparator<Card> {

    @Override
    public int compare(Card t, Card t1) {
        return (int)(((t.getStats().calculateAVGDifficulty() + 2) - (t1.getStats().calculateAVGDifficulty() + 2) * 100));
    }
}
