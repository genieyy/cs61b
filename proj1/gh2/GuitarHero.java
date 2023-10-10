package gh2;

import edu.princeton.cs.algs4.StdAudio;
import edu.princeton.cs.algs4.StdDraw;

public class GuitarHero {
    public static void main(String[] args) {
        /* create two guitar strings, for concert A and C */
        String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";
        GuitarString[] G = new GuitarString[37];

        for (int i = 0; i < 37; ++i) {
            G[i] = new GuitarString(440 * Math.pow(2, (i - 24.0) / 12.0));
        }

        while (true) {
            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                int index = keyboard.indexOf(key);
                G[index].pluck();
            }
            double sample = 0.0;
            for (int i = 0; i < keyboard.length(); i += 1) {
                sample += G[i].sample();
            }

            /* play the sample on standard audio */
            StdAudio.play(sample);

            /* advance the simulation of each guitar string by one step */
            for (int i = 0; i < keyboard.length(); i += 1) {
                G[i].tic();
            }
        }

    }
}
