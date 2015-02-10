package forchild.songs;

import forchild.P;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static forchild.Duration.*;
import static forchild.Note.*;

/**
 * http://www.holvoet.org/partitions/pdf/ppnoel.pdf
 */
public class PetitPapaNoel implements Iterable<P> {
    private static final P[] elements = {
            new P(NOIRE, SOL_4),                                                            // Pe
            new P(NOIRE, DO_5), new P(NOIRE, DO_5), new P(NOIRE, DO_5), new P(NOIRE, RE_5), // tit papa No
            new P(BLANCHE_POINT, DO_5), new P(CROCHE, DO_5), new P(CROCHE, RE_5),           // ël, Quand tu
            new P(NOIRE, MI_5), new P(NOIRE, MI_5), new P(NOIRE, MI_5), new P(NOIRE, FA_5), // descendra du
            new P(BLANCHE_POINT, MI_5), new P(NOIRE, RE_5),                                 // ciel, A

            new P(NOIRE_POINT, DO_5), new P(CROCHE, DO_5), new P(CROCHE, DO_5), new P(CROCHE, DO_5), new P(CROCHE, SI_4), new P(CROCHE, LA_4), // vec tes jouets par mil
            new P(BLANCHE_POINT, SOL_4), new P(CROCHE, SOL_4), new P(CROCHE, SOL_4),                                                           // liers N'oublie
            new P(BLANCHE, DO_5), new P(CROCHE, DO_5), new P(CROCHE, DO_5), new P(CROCHE, SI_4), new P(CROCHE, DO_5),                          // pas mon petit sou
            new P(BLANCHE_POINT, RE_5), new P(NOIRE, SOL_4),                                                                                   // lier Mais

            new P(NOIRE, DO_5), new P(NOIRE, DO_5), new P(NOIRE, DO_5), new P(NOIRE, RE_5), // avant de par
            new P(BLANCHE_POINT, DO_5), new P(CROCHE, DO_5), new P(CROCHE, RE_5),           // tir, Il fau
            new P(NOIRE, MI_5), new P(NOIRE, MI_5), new P(NOIRE, MI_5), new P(NOIRE, FA_5), // dra bien te cou
            new P(BLANCHE_POINT, MI_5), new P(NOIRE, RE_5),                                 // vir De

            new P(NOIRE_POINT, DO_5), new P(CROCHE, DO_5), new P(CROCHE, DO_5), new P(CROCHE, DO_5), new P(CROCHE, SI_4), new P(CROCHE, LA_4), // hors tu vas avoir si
            new P(BLANCHE_POINT, SOL_4), new P(CROCHE, SOL_4), new P(CROCHE, SOL_4),                                                           // froid. C'est un
            new P(BLANCHE, DO_5), new P(CROCHE, DO_5), new P(CROCHE, DO_5), new P(CROCHE, RE_5), new P(CROCHE, RE_5),                          // peu à cause de
            new P(RONDE, DO_5),                                                                                                                // moi.

            new P(CROCHE, LA_4), new P(CROCHE, LA_4), new P(CROCHE, LA_4), new P(CROCHE, LA_4), new P(NOIRE, LA_4), new P(CROCHE, LA_4), new P(CROCHE, SI_4), // Il me tarde tant que le
            new P(NOIRE_POINT, DO_5), new P(CROCHE, LA_4), new P(NOIRE, LA_4), new P(NOIRE, SOL_4),                                                           // jour se lève
            new P(CROCHE, DO_5), new P(CROCHE, DO_5), new P(CROCHE, DO_5), new P(CROCHE, DO_5), new P(NOIRE, DO_5), new P(CROCHE, SI_4), new P(CROCHE, DO_5), // Pour voir si tu m'as appor
            new P(RONDE, RE_5),                                                                                                                               // té

            new P(CROCHE, MI_5_BEMOL), new P(CROCHE, MI_5_BEMOL), new P(CROCHE, MI_5_BEMOL), new P(CROCHE, MI_5_BEMOL), new P(NOIRE, MI_5_BEMOL), new P(CROCHE, RE_5), new P(CROCHE, MI_5_BEMOL), // tous les beaux joujoux que je
            new P(NOIRE_POINT, FA_5), new P(CROCHE, RE_5), new P(NOIRE, DO_5), new P(NOIRE, SI_4_BEMOL),                                                                                          // vois en rêve
            new P(CROCHE, MI_5_BEMOL), new P(CROCHE, MI_5_BEMOL), new P(CROCHE, MI_5_BEMOL), new P(CROCHE, MI_5_BEMOL), new P(NOIRE, FA_5), new P(CROCHE, FA_5), new P(CROCHE, FA_5),             // et que je t'ai comman
            new P(BLANCHE_POINT, SOL_5),                                                                                                                                                          // dé.
    };

    @Override
    public Iterator<P> iterator() {
        return new Iterator<P>() {
            private int cursor = 0;

            @Override
            public boolean hasNext() {
                return cursor < elements.length;
            }

            @Override
            public P next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return elements[cursor++];
            }
        };
    }
    
    public Iterator<P> infiniteIterator() {
        return new Iterator<P>() {
            private int cursor = 0;

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public P next() {
                return elements[cursor++ % elements.length];
            }
        };
    }
}
