/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package IO;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author C. Levallois
 */
public class ImportMapSimilarities {

    public static Map<String, Float> execute() {

        String array1 = "1,2";
        String array2 = "1,3";
        String array3 = "1,4";
        String array4 = "2,3";
        String array5 = "2,4";
        String array6 = "3,4";

        Map<String, Float> mapSimilarities = new HashMap();

        mapSimilarities.put(array1, 1f);
        mapSimilarities.put(array2, 0.1f);
        mapSimilarities.put(array3, 0f);
        mapSimilarities.put(array4, 0f);
        mapSimilarities.put(array5, 0f);
        mapSimilarities.put(array6, 0f);
        return mapSimilarities;
    }
}
