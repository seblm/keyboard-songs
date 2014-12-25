package forchild.songs;

import forchild.PartitionElement;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static forchild.Duration.*;
import static forchild.Note.*;

/**
 * http://www.holvoet.org/partitions/pdf/ppnoel.pdf
 */
public class PetitPapaNoel implements Iterable<PartitionElement> {
    private static final PartitionElement[] elements = {
            new PartitionElement(NOIRE, SOL_4),
            new PartitionElement(NOIRE, DO_5), new PartitionElement(NOIRE, DO_5), new PartitionElement(NOIRE, DO_5), new PartitionElement(NOIRE, RE_5),
            new PartitionElement(BLANCHE_POINT, DO_5), new PartitionElement(CROCHE, DO_5), new PartitionElement(CROCHE, RE_5),
            new PartitionElement(NOIRE, MI_5), new PartitionElement(NOIRE, MI_5), new PartitionElement(NOIRE, MI_5), new PartitionElement(NOIRE, FA_5),
            new PartitionElement(BLANCHE_POINT, MI_5), new PartitionElement(NOIRE, RE_5),

            new PartitionElement(NOIRE_POINT, DO_5), new PartitionElement(CROCHE, DO_5), new PartitionElement(CROCHE, DO_5), new PartitionElement(CROCHE, DO_5), new PartitionElement(CROCHE, SI_4), new PartitionElement(CROCHE, LA_4),
            new PartitionElement(BLANCHE_POINT, SOL_4), new PartitionElement(CROCHE, SOL_4), new PartitionElement(CROCHE, SOL_4),
            new PartitionElement(BLANCHE, DO_5), new PartitionElement(CROCHE, DO_5), new PartitionElement(CROCHE, DO_5), new PartitionElement(CROCHE, SI_4), new PartitionElement(CROCHE, DO_5),
            new PartitionElement(BLANCHE_POINT, RE_5), new PartitionElement(NOIRE, SOL_4),

            new PartitionElement(NOIRE, SOL_4),
            new PartitionElement(NOIRE, DO_5), new PartitionElement(NOIRE, DO_5), new PartitionElement(NOIRE, DO_5), new PartitionElement(NOIRE, RE_5),
            new PartitionElement(BLANCHE_POINT, DO_5), new PartitionElement(CROCHE, DO_5), new PartitionElement(CROCHE, RE_5),
            new PartitionElement(NOIRE, MI_5), new PartitionElement(NOIRE, MI_5), new PartitionElement(NOIRE, MI_5), new PartitionElement(NOIRE, FA_5),
            new PartitionElement(BLANCHE_POINT, MI_5), new PartitionElement(NOIRE, RE_5),
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
