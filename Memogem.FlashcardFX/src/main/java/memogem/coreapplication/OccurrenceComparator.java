
package memogem.coreapplication;

import java.util.Comparator;

/**
 * This comparator class can be used in order to sort cards
 * due to the amount of times they have been studied.
 */
public class OccurrenceComparator implements Comparator<Card> {
   
    @Override
    public int compare(Card o1, Card o2) {
        return o1.getStats().getPracticeTimes() - o2.getStats().getPracticeTimes();
    }

}
