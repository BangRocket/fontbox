package net.afterlifelochie.io;

import java.util.Set;
import java.util.TreeSet;

/**
 * A stream of integers with certain numbers excluded
 */
public class IntegerExclusionStream {
    private volatile int ptr;
    private final int min;
    private final int max;
    private Set<Integer> exclusions = new TreeSet<>();

    public IntegerExclusionStream(int min, int max) {
        this.min = min;
        this.ptr = min - 1;
        this.max = max;
    }

    public void exclude(int i) {
        exclusions.add(i);
    }

    /**
     * Exclude a range of integers
     * Both values are included in the range
     *
     * @param a left bound
     * @param b right bound
     */
    public void excludeRange(int a, int b) {
        if (a == b) {
            exclude(a);
        } else {
            for (int n = Math.min(a, b), p = Math.max(a, b); n <= p; n++) {
                exclude(n);
            }
        }
    }

    /**
     * @return the start ptr of the largest gap
     */
    public int largest() {
        int size = 0, bestStart = 0, bestSize = 0;
        for (int start = min; start <= max; start++) {
            if (exclusions.contains(start)) {
                if (size > bestSize) {
                    bestStart = start - size;
                    bestSize = size;
                }
                size = 0;
            } else {
                size++;
            }
        }
        if (size > bestSize) {
            bestStart = max - size;
            bestSize = size;
        }
        return Math.max(0, bestStart);
    }

    /**
     * @return the next integer in the stream
     */
    public int next() {
        while (true) {
            ptr++;
            if (!exclusions.contains(ptr))
                break;
        }
        return ptr;
    }

    /**
     * @return the previous integer in the stream
     */
    public int previous() {
        while (true) {
            ptr--;
            if (!exclusions.contains(ptr))
                break;
        }
        return ptr;
    }

}
