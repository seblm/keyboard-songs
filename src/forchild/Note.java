package forchild;

/**
 * http://fr.wikipedia.org/wiki/Désignation_des_notes_de_musique_suivant_la_langue
 * http://newt.phys.unsw.edu.au/jw/notes.html
 */
public enum Note {
    SI_3_BEMOL(58),






    FA_4(65),

    SOL_4(67),

    LA_4(69),
    SI_4_BEMOL(70),
    SI_4(71),
    DO_5(72),
    RE_5_BEMOL(73),
    RE_5(74),
    MI_5_BEMOL(75),
    MI_5(76),
    FA_5(77),

    SOL_5(79),
    LA_5_BEMOL(80),

    SI_5_BEMOL(82),


    RE_6_BEMOL(85),








    SI_6_BEMOL(94),
    ;
    
    public final int number;
    
    private Note(int number) {
        this.number = number;
    }
}
