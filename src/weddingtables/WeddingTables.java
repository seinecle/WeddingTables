/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package weddingtables;

import IO.ExportResultsToScreen;
import IO.ImportMapSimilarities;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author C. Levallois
 * www.clementlevallois.net
 * 
 * Let's imagine we have 180 guests at a wedding.
 * 
 * How to group them in tables to maximize the similar between guests for each table? (let's assume that's desirable!)
 * 
 * => This algorithm takes a size of table (sitsPerTable), and similarities between all guests (mapSmilarities), as inputs
 * 
 * => It returns a list of tables with guests sited in a way that maximizes the similarity per table
 * 
 * 
 * Thanks to @vtraag for providing the logic of this clustering technique.
 * 
 * 
 */
public class WeddingTables {

    private static Set<Integer> guests;
    private static Map<String, Float> mapSimilarities;
    //what is mapSmilarities?
    //the String is a pair of guests, comma separated. Such as: "1,2" for the pair of guests 1 and 2
    // the Float value is their similarity, as provided externally (not determined here).
    
    private static List<Set<Integer>> tables;
    private static Set<Integer> table;
    private static int sitsPerTable = 3;
    private static QualityMeasure qm;
    private static int maxNumberOfIterationsWithoutImprovement = 200;

    WeddingTables() {
    }

    public static void main(String[] args) {

        //import a map of similarities
        mapSimilarities = ImportMapSimilarities.execute();

        //create a set of guests
        guests = new HashSet();
        for (String pairOfGuests : mapSimilarities.keySet()) {
            guests.add(Integer.parseInt(pairOfGuests.split(",")[0]));
            guests.add(Integer.parseInt(pairOfGuests.split(",")[1]));
        }


        //we will keep track of the quality of the groupings
        qm = new QualityMeasure();

        //init with a first random distribution of guests to tables
        init();

        //computation of the quality of this first distribution
        qm.computeGlobalQuality(tables, mapSimilarities);

        //start of the permutations of pairs of guests, until the quality stops improving
        permuteGuests();

        //export the results to screen
        ExportResultsToScreen.execute(tables);

    }

    private static void init() {

        tables = new ArrayList();

        //randomly assign guests to tables

        table = new HashSet();
        for (int guest : guests) {
            if (table.size() == sitsPerTable) {
                tables.add(table);
                table = new HashSet();
            }
            table.add(guest);
        }

        //dealing with a possible last table with a few guests
        //if these guests can be spread across existing tables, we do

        if (!table.isEmpty()) {
            if (table.size() <= tables.size()) {
                int indexTables = 0;
                for (int guest : table) {
                    tables.get(indexTables).add(guest);
                    indexTables++;
                }
            } else {
                tables.add(table);
            }
        }
        table = null;
    }

    private static void permuteGuests() {

        Set<Integer> permutedTable1;
        Set<Integer> permutedTable2;

        Iterator<Integer> setIterator1;
        Iterator<Integer> setIterator2;

        int guest1;
        int guest2;

        Float qualityOriginalTable1;
        Float qualityOriginalTable2;
        Float qualityPermutedTable1;
        Float qualityPermutedTable2;
        long timeSinceLastImprovement = 0;
        long permutationsCounter = 0;

        int table1Index = 0;
        int table2Index = 0;

        //looping through all tables
        for (Set<Integer> table1 : tables) {
            qualityOriginalTable1 = qm.computeQualityInATable(table1, mapSimilarities);
            setIterator1 = table1.iterator();

            //looping through guests of the current table
            while (setIterator1.hasNext()) {
                guest1 = setIterator1.next();

                //looping on the remaining tables;
                for (Set<Integer> table2 : tables) {
                    qualityOriginalTable2 = qm.computeQualityInATable(table2, mapSimilarities);
                    setIterator2 = table2.iterator();

                    //looping through the guests of the current remaining table
                    while (setIterator2.hasNext()) {
                        guest2 = setIterator2.next();

                        //increment the counter of all permutations
                        permutationsCounter++;

                        //permutting guest1 and guest 2 from their respective tables.
                        permutedTable1 = new HashSet();
                        permutedTable1.addAll(table1);
                        permutedTable1.remove(guest1);
                        permutedTable1.add(guest2);

                        permutedTable2 = new HashSet();
                        permutedTable2.addAll(table2);
                        permutedTable2.remove(guest2);
                        permutedTable2.add(guest1);

                        //compute the quality for these two tables after permutation;
                        qualityPermutedTable1 = qm.computeQualityInATable(permutedTable1, mapSimilarities);
                        qualityPermutedTable2 = qm.computeQualityInATable(permutedTable2, mapSimilarities);

                        //increment the count of permutations that have been tried
                        timeSinceLastImprovement++;

                        //if the quality of the tables with permutation is higher than the quality of the original tables,
                        //save the permutted tables as the new original tables
                        if (qualityPermutedTable1 + qualityPermutedTable2 > qualityOriginalTable1 + qualityOriginalTable2) {
                            tables.set(table1Index, permutedTable1);
                            tables.set(table2Index, permutedTable2);

                            //reset the count last time since a permutation brought an improvement in quality
                            timeSinceLastImprovement = 0;
                        }
                    }
                    table2Index++;
                }
            }
            table1Index++;
        }

        //the loop is now over. We relaunch the process if we find that improvements were still being reached less than n permutations ago.
        if (timeSinceLastImprovement < Math.min(permutationsCounter, maxNumberOfIterationsWithoutImprovement)) {
            permuteGuests();
        }
    }
}