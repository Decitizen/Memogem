
package memogem.coreapplication;

import java.util.Comparator;

public class OccurrenceComparator implements Comparator<Card> {
   
    @Override
    public int compare(Card o1, Card o2) {
        return o1.getStats().getPracticeTimes() - o2.getStats().getPracticeTimes();
    }

}
