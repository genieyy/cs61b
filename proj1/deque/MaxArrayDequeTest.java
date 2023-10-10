package deque;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Comparator;
import java.util.Optional;

public class MaxArrayDequeTest {
    @Test
    public void maxtest() {
        MaxArrayDeque<Integer> m = new MaxArrayDeque<>(new ComperatorInt());
        m.addFirst(5);
        m.addFirst(6);
        assertEquals(6, (int) m.max());
    }
}
