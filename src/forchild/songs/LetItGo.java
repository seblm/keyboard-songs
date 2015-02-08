package forchild.songs;

import forchild.P;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static forchild.Duration.*;
import static forchild.Note.*;

/**
 * http://ekladata.com/qB3Magj2yh-Vpz1kTLDlDXN3B1o/Let-It-Go.pdf
 */
public class LetItGo implements Iterable<P> {
    private static final P[] elements = {
            // BEMOLS : LA SI RE MI
            new P(CROCHE, SOL_5), new P(CROCHE, LA_5_BEMOL), new P(CROCHE, DO_5), new P(NOIRE, SOL_5), new P(NOIRE, LA_5_BEMOL), new P(CROCHE, DO_5),
            new P(CROCHE, LA_5_BEMOL), new P(CROCHE, SOL_5), new P(CROCHE, DO_5), new P(NOIRE, LA_5_BEMOL), new P(NOIRE, SOL_5), new P(CROCHE, SI_4_BEMOL),
            new P(CROCHE, FA_5), new P(CROCHE, SOL_5), new P(CROCHE, SI_4_BEMOL), new P(NOIRE, FA_5), new P(NOIRE, SOL_5), new P(CROCHE, SI_4_BEMOL),
            new P(NOIRE, SI_4_BEMOL, MI_5_BEMOL), new P(NOIRE, SI_6_BEMOL), new P(NOIRE, SI_4_BEMOL, RE_5_BEMOL), new P(NOIRE, RE_7_BEMOL),

            new P(CROCHE, SOL_5), new P(CROCHE, LA_5_BEMOL), new P(CROCHE, DO_5), new P(NOIRE, SOL_5), new P(NOIRE, LA_5_BEMOL), new P(CROCHE, DO_5),
            new P(CROCHE, LA_5_BEMOL), new P(CROCHE, SOL_5), new P(CROCHE, DO_5), new P(NOIRE, LA_5_BEMOL), new P(NOIRE, SOL_5), new P(CROCHE, SI_4_BEMOL),
            new P(CROCHE, FA_5), new P(CROCHE, SOL_5), new P(CROCHE, SI_4_BEMOL), new P(NOIRE, FA_5), new P(NOIRE, SOL_5), new P(CROCHE, SI_4_BEMOL),
            new P(BLANCHE, SI_4_BEMOL, MI_5_BEMOL), new P(NOIRE, SI_4_BEMOL, RE_5), new P(CROCHE, FA_4, SI_4_BEMOL, RE_5, SI_6_BEMOL), new P(CROCHE, SI_3_BEMOL),

            new P(CROCHE, SOL_3, DO_4), new P(CROCHE, LA_3_BEMOL), new P(CROCHE, FA_3, DO_4), new P(NOIRE, SOL_3), new P(NOIRE, DO_4), new P(NOIRE, LA_3_BEMOL), new P(CROCHE, DO_4), new P(CROCHE, FA_3, DO_4),
            new P(CROCHE, LA_3_BEMOL, DO_4), new P(CROCHE, SOL_3), new P(CROCHE, FA_3, SI_3_BEMOL), new P(NOIRE_POINT, DO_4), new P(CROCHE, DO_4), new P(CROCHE, DO_4),
            new P(CROCHE, FA_3, SI_3_BEMOL), new P(CROCHE, SOL_3, SI_3_BEMOL), new P(CROCHE, MI_3_BEMOL), new P(NOIRE, FA_3), new P(CROCHE, LA_3), new P(CROCHE, SOL_3),
            new P(NOIRE_POINT, MI_3_BEMOL, FA_3), new P(CROCHE, SI_3_BEMOL), new P(CROCHE, RE_4_BEMOL), new P(CROCHE, FA_4), new P(CROCHE, LA_4_BEMOL), new P(CROCHE, SI_4_BEMOL),

            new P(CROCHE, SOL_4, DO_5), new P(CROCHE, LA_4_BEMOL, DO_5), new P(CROCHE, FA_4, DO_5), new P(CROCHE, SOL_4, DO_5), new P(NOIRE, SOL_4, DO_5), new P(CROCHE, LA_4_BEMOL, MI_5_BEMOL), new P(CROCHE, FA_4, MI_5_BEMOL),
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
}
