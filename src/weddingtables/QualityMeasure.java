/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package weddingtables;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author C. Levallois
 */
public class QualityMeasure<T> {

    float currentQuality;

    public QualityMeasure() {
        this.currentQuality = 0f;
    }

    public void computeGlobalQuality(List<Set<Integer>> tables, Map<String, Float> map) {
        for (Set<Integer> table : tables) {
            currentQuality = currentQuality + computeQualityInATable(table, map);
        }
    }

    public Float computeQualityInATable(Set<Integer> table, Map<String, Float> map) {
        String pairOfGuests = "-1";
        Float qualityInTable = 0f;

        List<Integer> listObjects = new ArrayList();
        listObjects.addAll(table);

        Iterator<Integer> listIterator1 = listObjects.listIterator();
        Iterator<Integer> listIterator2;
        int count = 1;
        int guest1;
        int guest2;
        Float qualityPairGuests;

        while (listIterator1.hasNext()) {
            guest1 = listIterator1.next();
            listIterator2 = listObjects.listIterator(count++);
            while (listIterator2.hasNext()) {
                guest2 = listIterator2.next();
                if (guest1 < guest2) {
                    pairOfGuests = guest1+","+guest2;
                } else {
                    pairOfGuests = guest2+","+guest1;
                }
                qualityPairGuests = map.get(pairOfGuests);

                //check: in the case where there is no similarity at all between two guests, their pair won't appear in the map of similarities
                if (qualityPairGuests != null) {
                    qualityInTable = qualityInTable + qualityPairGuests;
                }
            }
        }
        return qualityInTable;

    }
}
