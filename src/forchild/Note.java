package forchild;

/**
 * http://fr.wikipedia.org/wiki/Désignation_des_notes_de_musique_suivant_la_langue
 * http://newt.phys.unsw.edu.au/jw/notes.html
 */
public enum Note {

    DO_8(108),        // C8

    SI_7(107),        // B7
    SI_7_BEMOL(106),
    LA_7(105),        // A7
    LA_7_BEMOL(104),
    SOL_7(103),       // G7
    SOL_7_BEMOL(102),
    FA_7(101),        // F7
    MI_7(100),        // E7
    MI_7_BEMOL(99),
    RE_7_BEMOL(97),
    RE_7(98),         // D7
    DO_7(96),         // C7

    SI_6(95),         // B6
    SI_6_BEMOL(94),
    LA_6(93),         // A6
    LA_6_BEMOL(92),
    SOL_6(91),        // G6
    SOL_6_BEMOL(90),
    FA_6(89),         // F6
    MI_6(88),         // E6
    MI_6_BEMOL(87),
    RE_6(86),         // D6
    RE_6_BEMOL(85),
    DO_6(84),         // C6

    SI_5(83),         // B5
    SI_5_BEMOL(82),
    LA_5(81),         // A5
    LA_5_BEMOL(80),
    SOL_5(79),        // G5
    SOL_5_BEMOL(78),
    FA_5(77),         // F5
    MI_5(76),         // E5
    MI_5_BEMOL(75),
    RE_5(74),         // D5
    RE_5_BEMOL(73),
    DO_5(72),         // C5

    SI_4(71),         // B4
    SI_4_BEMOL(70),
    LA_4(69),         // A4
    LA_4_BEMOL(68),
    SOL_4(67),        // G4 <- Clé de sol
    SOL_4_BEMOL(66),
    FA_4(65),         // F4
    MI_4(64),         // E4
    MI_4_BEMOL(63),
    RE_4(62),         // D4
    RE_4_BEMOL(61),
    DO_4(60),         // C4

    SI_3(59),         // B3
    SI_3_BEMOL(58),
    LA_3(57),         // A3
    LA_3_BEMOL(56),
    SOL_3(55),        // G3
    SOL_3_BEMOL(54),
    FA_3(53),         // F3 <- Clé de fa
    MI_3(52),         // E3
    MI_3_BEMOL(51),
    RE_3(50),         // D3
    RE_3_BEMOL(49),
    DO_3(48),         // C3

    SI_2(47),         // B2
    SI_2_BEMOL(46),
    LA_2(45),         // A2
    LA_2_BEMOL(44),
    SOL_2(43),        // G2
    SOL_2_BEMOL(42),
    FA_2(41),         // F2
    MI_2(40),         // E2
    MI_2_BEMOL(39),
    RE_2(38),         // D2
    RE_2_BEMOL(37),
    DO_2(36),         // C2

    SI_1(35),         // B1
    SI_1_BEMOL(34),
    LA_1(33),         // A1
    LA_1_BEMOL(32),
    SOL_1(31),        // G1
    SOL_1_BEMOL(30),
    FA_1(29),         // F1
    MI_1(28),         // E1
    MI_1_BEMOL(27),
    RE_1(26),         // D1
    RE_1_BEMOL(25),
    DO_1(24),         // C1

    SI_0(23),         // B0
    SI_0_BEMOL(22),
    LA_0(21),         // A0
;

    public final int number;

    private Note(int number) {
        this.number = number;
    }
}
