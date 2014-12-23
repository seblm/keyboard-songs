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
            new PartitionElement(SOL_4, NOIRE),
            new PartitionElement(DO_5, NOIRE), new PartitionElement(DO_5, NOIRE), new PartitionElement(DO_5, NOIRE), new PartitionElement(RE_5, NOIRE),
            new PartitionElement(DO_5, BLANCHE_POINT), new PartitionElement(DO_5, CROCHE), new PartitionElement(RE_5, CROCHE),
            new PartitionElement(MI_5, NOIRE), new PartitionElement(MI_5, NOIRE), new PartitionElement(MI_5, NOIRE), new PartitionElement(FA_5, NOIRE),
            new PartitionElement(MI_5, BLANCHE_POINT), new PartitionElement(RE_5, NOIRE),

            new PartitionElement(DO_5, NOIRE_POINT), new PartitionElement(DO_5, CROCHE), new PartitionElement(DO_5, CROCHE), new PartitionElement(DO_5, CROCHE), new PartitionElement(SI_4, CROCHE), new PartitionElement(LA_4, CROCHE),
            new PartitionElement(SOL_4, BLANCHE_POINT), new PartitionElement(SOL_4, CROCHE), new PartitionElement(SOL_4, CROCHE),
            new PartitionElement(DO_5, BLANCHE), new PartitionElement(DO_5, CROCHE), new PartitionElement(DO_5, CROCHE), new PartitionElement(SI_4, CROCHE), new PartitionElement(DO_5, CROCHE),
            new PartitionElement(RE_5, BLANCHE_POINT), new PartitionElement(SOL_4, NOIRE),

            new PartitionElement(SOL_4, NOIRE),
            new PartitionElement(DO_5, NOIRE), new PartitionElement(DO_5, NOIRE), new PartitionElement(DO_5, NOIRE), new PartitionElement(RE_5, NOIRE),
            new PartitionElement(DO_5, BLANCHE_POINT), new PartitionElement(DO_5, CROCHE), new PartitionElement(RE_5, CROCHE),
            new PartitionElement(MI_5, NOIRE), new PartitionElement(MI_5, NOIRE), new PartitionElement(MI_5, NOIRE), new PartitionElement(FA_5, NOIRE),
            new PartitionElement(MI_5, BLANCHE_POINT), new PartitionElement(RE_5, NOIRE),
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
