package forchild;

/**
 * http://fr.wikipedia.org/wiki/Désignation_des_notes_de_musique_suivant_la_langue
 * http://newt.phys.unsw.edu.au/jw/notes.html
 */
public enum Note {
    SOL_4(67),
    LA_4(69),
    SI_4(71),
    DO_5(72),
    RE_5(74),
    MI_5(76),
    FA_5(77);
    
    public final int number;
    
    private Note(int number) {
        this.number = number;
    }
}
