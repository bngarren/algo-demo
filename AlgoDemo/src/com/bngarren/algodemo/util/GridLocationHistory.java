package com.bngarren.algodemo.util;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Optional;

/**
 * A Deque that holds a current and previous IGridLocation(s), up to a total size of 'size'.
 */
public class GridLocationHistory {

    private final int size;
    private final Deque<IGridLocation> deque;

    public GridLocationHistory() {
        this(2);
    }

    public GridLocationHistory(int size) {
        this.size = size;
        this.deque = new LinkedList<>();
    }

    /**
     * Adds a new location to the deque, moving the current location to previous
     * and keeping only a maximum of 'size' number of locations.
     *
     * @param location The new IGridLocation to be added.
     */
    public void push(IGridLocation location) {
        if (deque.size() == size) {
            deque.pollFirst(); // Remove the oldest location if the deque already has 'size' number of elements
        }
        deque.offerLast(location); // Add the new location to the deque
    }

    /**
     * Retrieves the current location (the most recently added).
     *
     * @return An Optional containing the current location, or an empty Optional if the deque is empty.
     */
    public Optional<IGridLocation> getCurrent() {
        return deque.isEmpty() ? Optional.empty() : Optional.of(deque.getLast());
    }

    /**
     * Retrieves the previous location (the one before the most recently added).
     *
     * @return An Optional containing the previous location, or an empty Optional if there are fewer than 2 locations in the deque.
     */
    public Optional<IGridLocation> getPrevious() {
        return deque.size() > 1 ? Optional.of(deque.getFirst()) : Optional.empty();
    }

    /**
     * Clears all locations from the deque
     */
    public void clear() {
        deque.clear();
    }
}
