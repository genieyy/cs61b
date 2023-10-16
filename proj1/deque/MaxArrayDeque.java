package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator<T> comparator;

    public MaxArrayDeque(Comparator<T> c) {
        comparator = c;
    }

    public T max() {
        if (size() == 0) return null;
        T max = get(0);
        for (int i = 0; i < size(); ++i) {
            if (comparator.compare(get(i), max) == 1) {
                max = get(i);
            }
        }
        return max;
    }

    public T max(Comparator<T> c) {
        if (size() == 0) return null;
        T max = get(0);
        for (int i = 0; i < size(); ++i) {
            if (c.compare(get(i), max) == 1) {
                max = get(i);
            }
        }
        return max;
    }

}
