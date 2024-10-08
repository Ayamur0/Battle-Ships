package com.battleships.gui.particles;

import java.util.List;

/**
 * Standard implementation of an Insertion Sort.
 *
 * @author Tim Staudenmaier
 */
public class InsertionSort {

    /**
     * Sorts a list of particles so that the particles with the highest distance
     * from the camera are first, and the particles with the shortest distance
     * are last.
     *
     * @param list the list of particles to sort.
     */
    public static void sortHighToLow(List<Particle> list) {
        for (int i = 1; i < list.size(); i++) {
            Particle item = list.get(i);
            if (item.getDistance() > list.get(i - 1).getDistance()) {
                sortUpHighToLow(list, i);
            }
        }
    }

    /**
     * Sort item at index i of the list to the right position.
     *
     * @param list List to sort.
     * @param i    Index of item to sort into right position.
     */
    private static void sortUpHighToLow(List<Particle> list, int i) {
        Particle item = list.get(i);
        int attemptPos = i - 1;
        while (attemptPos != 0 && list.get(attemptPos - 1).getDistance() < item.getDistance()) {
            attemptPos--;
        }
        list.remove(i);
        list.add(attemptPos, item);
    }
}
