/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package IO;

import java.util.List;
import java.util.Set;

/**
 *
 * @author C. Levallois
 */
public class ExportResultsToScreen {

    public static void execute(List<Set<Integer>> tables) {

        int countTables = 0;
        for (Set<Integer> table : tables) {
            countTables++;
            System.out.println("Table " + countTables);
            System.out.println("List of guests:");
            for (int guest : table) {
                System.out.print(guest + ", ");
            }
            System.out.println("");
            System.out.println("");
        }
    }
}
