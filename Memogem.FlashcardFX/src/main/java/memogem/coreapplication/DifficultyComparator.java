
package memogem.coreapplication;

import java.util.Comparator;

public class DifficultyComparator implements Comparator<Card> {

    @Override
    public int compare(Card t, Card t1) {
        return (int)(((t.getStats().calculateAVGDifficulty() + 2) - (t1.getStats().calculateAVGDifficulty() + 2) * 100));
    }
}
