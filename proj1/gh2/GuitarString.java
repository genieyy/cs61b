package gh2;

import deque.ArrayDeque;
import deque.Deque;
import deque.LinkedListDeque;
import edu.princeton.cs.algs4.StdAudio;

// import deque.Deque;


//Note: This file will not compile until you complete the Deque implementations
public class GuitarString {
    /**
     * Constants. Do not change. In case you're curious, the keyword final
     * means the values cannot be changed at runtime. We'll discuss this and
     * other topics in lecture on Friday.
     */
    private static final int SR = 44100;      // Sampling Rate
    private static final double DECAY = .996; // energy decay factor

    /* Buffer for storing sound data. */

    private Deque<Double> buffer;

    /* Create a guitar string of the given frequency.  */
    public GuitarString(double frequency) {

        buffer = new LinkedListDeque<>();
        int size = (int) Math.round(SR / frequency);
        for (int i = 0; i < size; ++i) {
            buffer.addFirst(0.0);
        }


    }


    /* Pluck the guitar string by replacing the buffer with white noise. */
    public void pluck() {
        int size = buffer.size();
        while (!buffer.isEmpty()) {
            buffer.removeFirst();
        }
        for (int i = 0; i < size; ++i) {
            buffer.addFirst(Math.random() - 0.5);
        }

        //       Make sure that your random numbers are different from each
        //       other. This does not mean that you need to check that the numbers
        //       are different from each other. It means you should repeatedly call
        //       Math.random() - 0.5 to generate new random numbers for each array index.
    }

    /* Advance the simulation one time step by performing one iteration of
     * the Karplus-Strong algorithm.
     */
    public void tic() {

        buffer.addLast((buffer.get(0) + buffer.get(1)) / 2 * DECAY);
        buffer.removeFirst();

    }

    /* Return the double at the front of the buffer. */
    public double sample() {

        return buffer.get(0);
    }
}

