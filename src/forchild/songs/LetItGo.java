package forchild.songs;

import forchild.Duration;
import forchild.Note;
import forchild.PartitionElement;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static forchild.Duration.BLANCHE;
import static forchild.Duration.CROCHE;
import static forchild.Duration.NOIRE;
import static forchild.Note.*;

/**
 * http://ekladata.com/qB3Magj2yh-Vpz1kTLDlDXN3B1o/Let-It-Go.pdf
 */
public class LetItGo implements Iterable<PartitionElement> {
    private static final PartitionElement[] elements = {
            // BEMOLS : LA SI RE MI
            new PartitionElement(CROCHE, SOL_5), new PartitionElement(CROCHE, LA_5_BEMOL), new PartitionElement(CROCHE, DO_5), new PartitionElement(NOIRE, SOL_5), new PartitionElement(NOIRE, LA_5_BEMOL), new PartitionElement(CROCHE, DO_5),
            new PartitionElement(CROCHE, LA_5_BEMOL), new PartitionElement(CROCHE, SOL_5), new PartitionElement(CROCHE, DO_5), new PartitionElement(NOIRE, LA_5_BEMOL), new PartitionElement(NOIRE, SOL_5), new PartitionElement(CROCHE, SI_4_BEMOL),
            new PartitionElement(CROCHE, FA_5), new PartitionElement(CROCHE, SOL_5), new PartitionElement(CROCHE, SI_4_BEMOL), new PartitionElement(NOIRE, FA_5), new PartitionElement(NOIRE, SOL_5), new PartitionElement(CROCHE, SI_4_BEMOL),
            new PartitionElement(NOIRE, SI_4_BEMOL, MI_5_BEMOL), new PartitionElement(NOIRE, SI_5_BEMOL), new PartitionElement(NOIRE, SI_4_BEMOL, RE_5_BEMOL), new PartitionElement(NOIRE, RE_6_BEMOL),

            new PartitionElement(CROCHE, SOL_5), new PartitionElement(CROCHE, LA_5_BEMOL), new PartitionElement(CROCHE, DO_5), new PartitionElement(NOIRE, SOL_5), new PartitionElement(NOIRE, LA_5_BEMOL), new PartitionElement(CROCHE, DO_5),
            new PartitionElement(CROCHE, LA_5_BEMOL), new PartitionElement(CROCHE, SOL_5), new PartitionElement(CROCHE, DO_5), new PartitionElement(NOIRE, LA_5_BEMOL), new PartitionElement(NOIRE, SOL_5), new PartitionElement(CROCHE, SI_4_BEMOL),
            new PartitionElement(CROCHE, FA_5), new PartitionElement(CROCHE, SOL_5), new PartitionElement(CROCHE, SI_4_BEMOL), new PartitionElement(NOIRE, FA_5), new PartitionElement(NOIRE, SOL_5), new PartitionElement(CROCHE, SI_4_BEMOL),
            new PartitionElement(BLANCHE, SI_4_BEMOL, MI_5_BEMOL), new PartitionElement(NOIRE, SI_4_BEMOL, RE_5), new PartitionElement(CROCHE, FA_4, SI_4_BEMOL, RE_5, SI_6_BEMOL), new PartitionElement(CROCHE, SI_3_BEMOL),
    };

    @Override
    public Iterator<PartitionElement> iterator() {
        return new Iterator<PartitionElement>() {
            private int cursor = 0;

            @Override
            public boolean hasNext() {
                return cursor < elements.length;
            }

            @Override
            public PartitionElement next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return elements[cursor++];
            }
        };
    }
}
