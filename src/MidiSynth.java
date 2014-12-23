import static java.util.Arrays.stream;
import static javax.swing.JTable.AUTO_RESIZE_OFF;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.event.*;
import javax.sound.midi.*;
import java.util.Vector;

import java.io.File;
import java.io.IOException;

/**
 * Illustrates general MIDI melody instruments and MIDI controllers.
 */
public class MidiSynth extends JPanel implements ControlContext {
    private static final int PROGRAM = 192;
    private static final int NOTEON = 144;
    private static final int NOTEOFF = 128;
    private static final int SUSTAIN = 64;
    private static final int REVERB = 91;
    private static final Color jfcBlue = new Color(204, 204, 255);
    private static final Color pink = new Color(255, 175, 175);

    private Sequencer sequencer;
    private Sequence sequence;
    private Synthesizer synthesizer;
    private Instrument instruments[];
    private ChannelData channels[];
    private ChannelData currentChannel;
    private JCheckBox mouseOverCB = new JCheckBox("mouseOver", true);
    private JSlider veloS, presS, bendS, revbS;
    private JCheckBox soloCB, monoCB, muteCB;
    private Vector<Key> keys = new Vector<>();
    private Vector<Key> whiteKeys = new Vector<>();
    private JTable table;
    private boolean record;
    private Track track;
    private long startTime;
    private RecordFrame recordFrame;

    MidiSynth() {
        setLayout(new BorderLayout());

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        EmptyBorder eb = new EmptyBorder(5, 5, 5, 5);
        BevelBorder bb = new BevelBorder(BevelBorder.LOWERED);
        CompoundBorder cb = new CompoundBorder(eb, bb);
        p.setBorder(new CompoundBorder(cb, eb));
        JPanel pp = new JPanel(new BorderLayout());
        pp.setBorder(new EmptyBorder(10, 20, 10, 5));
        pp.add(new Piano());
        p.add(pp);
        p.add(new Controls());
        p.add(new InstrumentsTable());

        add(p);
    }

    @Override
    public void open() {
        try {
            if (synthesizer == null) {
                if ((synthesizer = MidiSystem.getSynthesizer()) == null) {
                    System.out.println("getSynthesizer() failed!");
                    return;
                }
            }
            synthesizer.open();
            sequencer = MidiSystem.getSequencer();
            sequence = new Sequence(Sequence.PPQ, 10);
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

        Soundbank sb = synthesizer.getDefaultSoundbank();
        if (sb != null) {
            instruments = sb.getInstruments();
            synthesizer.loadInstrument(instruments[0]);
        }
        MidiChannel midiChannels[] = synthesizer.getChannels();
        channels = new ChannelData[midiChannels.length];
        for (int i = 0; i < channels.length; i++) {
            channels[i] = new ChannelData(midiChannels[i], i);
        }
        currentChannel = channels[0];

        ListSelectionModel lsm = table.getSelectionModel();
        lsm.setSelectionInterval(0, 0);
        lsm = table.getColumnModel().getSelectionModel();
        lsm.setSelectionInterval(0, 0);
    }

    @Override
    public void close() {
        if (synthesizer != null) {
            synthesizer.close();
        }
        if (sequencer != null) {
            sequencer.close();
        }
        sequencer = null;
        synthesizer = null;
        instruments = null;
        channels = null;
        if (recordFrame != null) {
            recordFrame.dispose();
            recordFrame = null;
        }
    }

    /**
     * given 120 bpm:
     * (120 bpm) / (60 seconds per minute) = 2 beats per second
     * 2 / 1000 beats per millisecond
     * (2 * resolution) ticks per second
     * (2 * resolution)/1000 ticks per millisecond, or
     * (resolution / 500) ticks per millisecond
     * ticks = milliseconds * resolution / 500
     */
    private void createShortEvent(int type, int num) {
        ShortMessage message = new ShortMessage();
        try {
            long millis = System.currentTimeMillis() - startTime;
            long tick = millis * sequence.getResolution() / 500;
            message.setMessage(type + currentChannel.num, num, currentChannel.velocity);
            MidiEvent event = new MidiEvent(message, tick);
            track.add(event);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private enum KeyState {ON, OFF}

    /**
     * Black and white keys or notes on the piano.
     */
    private class Key extends Rectangle {
        private final int kNum;
        private KeyState noteState;

        private Key(int x, int y, int width, int height, int num) {
            super(x, y, width, height);
            kNum = num;
            noteState = KeyState.OFF;
        }

        private boolean isNoteOn() {
            return noteState == KeyState.ON;
        }

        private void on() {
            noteState = KeyState.ON;
            currentChannel.channel.noteOn(kNum, currentChannel.velocity);
            if (record) {
                createShortEvent(NOTEON, kNum);
            }
        }

        private void off() {
            noteState = KeyState.OFF;
            currentChannel.channel.noteOff(kNum, currentChannel.velocity);
            if (record) {
                createShortEvent(NOTEOFF, kNum);
            }
        }

        private void noteToOff() {
            noteState = KeyState.OFF;
        }
    }

    /**
     * Piano renders black & white keys and plays the notes for a MIDI
     * channel.
     */
    private class Piano extends JPanel implements MouseListener {
        private Vector<Key> blackKeys = new Vector<>();
        private Key prevKey;
        private static final int kw = 16, kh = 80;

        private Piano() {
            setLayout(new BorderLayout());
            setPreferredSize(new Dimension(42 * kw, kh + 1));
            int transpose = 24;
            int whiteIDs[] = {0, 2, 4, 5, 7, 9, 11};

            for (int i = 0, x = 0; i < 6; i++) {
                for (int j = 0; j < 7; j++, x += kw) {
                    int keyNum = i * 12 + whiteIDs[j] + transpose;
                    whiteKeys.add(new Key(x, 0, kw, kh, keyNum));
                }
            }
            for (int i = 0, x = 0; i < 6; i++, x += kw) {
                int keyNum = i * 12 + transpose;
                blackKeys.add(new Key((x += kw) - 4, 0, kw / 2, kh / 2, keyNum + 1));
                blackKeys.add(new Key((x += kw) - 4, 0, kw / 2, kh / 2, keyNum + 3));
                x += kw;
                blackKeys.add(new Key((x += kw) - 4, 0, kw / 2, kh / 2, keyNum + 6));
                blackKeys.add(new Key((x += kw) - 4, 0, kw / 2, kh / 2, keyNum + 8));
                blackKeys.add(new Key((x += kw) - 4, 0, kw / 2, kh / 2, keyNum + 10));
            }
            keys.addAll(blackKeys);
            keys.addAll(whiteKeys);

            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    if (mouseOverCB.isSelected()) {
                        Key key = getKey(e.getPoint());
                        if (prevKey != null && prevKey != key) {
                            prevKey.off();
                        }
                        if (key != null && prevKey != key) {
                            key.on();
                        }
                        prevKey = key;
                        repaint();
                    }
                }
            });
            addMouseListener(this);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            prevKey = getKey(e.getPoint());
            if (prevKey != null) {
                prevKey.on();
                repaint();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (prevKey != null) {
                prevKey.off();
                repaint();
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (prevKey != null) {
                prevKey.off();
                repaint();
                prevKey = null;
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        private Key getKey(Point point) {
            for (Key key : keys) {
                if (key.contains(point)) {
                    return key;
                }
            }
            return null;
        }

        @Override
        public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            Dimension d = getSize();

            g2.setBackground(getBackground());
            g2.clearRect(0, 0, d.width, d.height);

            g2.setColor(Color.white);
            g2.fillRect(0, 0, 42 * kw, kh);

            for (Key whiteKey : whiteKeys) {
                if (whiteKey.isNoteOn()) {
                    g2.setColor(record ? pink : jfcBlue);
                    g2.fill(whiteKey);
                }
                g2.setColor(Color.black);
                g2.draw(whiteKey);
            }
            for (Key blackKey : blackKeys) {
                if (blackKey.isNoteOn()) {
                    g2.setColor(record ? pink : jfcBlue);
                    g2.fill(blackKey);
                    g2.setColor(Color.black);
                    g2.draw(blackKey);
                } else {
                    g2.setColor(Color.black);
                    g2.fill(blackKey);
                }
            }
        }
    }

    /**
     * Stores MidiChannel information.
     */
    private class ChannelData {

        private final MidiChannel channel;
        private final int num;

        private boolean solo, mono, mute, sustain;
        private int velocity, pressure, bend, reverb;
        private int row, col;

        private ChannelData(MidiChannel channel, int num) {
            this.channel = channel;
            this.num = num;
            velocity = pressure = bend = reverb = 64;
        }

        private void setComponentStates() {
            table.setRowSelectionInterval(row, row);
            table.setColumnSelectionInterval(col, col);

            soloCB.setSelected(solo);
            monoCB.setSelected(mono);
            muteCB.setSelected(mute);

            JSlider slider[] = {veloS, presS, bendS, revbS};
            int v[] = {velocity, pressure, bend, reverb};
            for (int i = 0; i < slider.length; i++) {
                TitledBorder tb = (TitledBorder) slider[i].getBorder();
                String s = tb.getTitle();
                tb.setTitle(s.substring(0, s.indexOf('=') + 1) + String.valueOf(v[i]));
                slider[i].repaint();
            }
        }
    }

    /**
     * Table for 128 general MIDI melody instuments.
     */
    private class InstrumentsTable extends JPanel {

        public InstrumentsTable() {
            setLayout(new BorderLayout());

            final String[] names = {
                    "Piano", "Chromatic Perc.", "Organ", "Guitar",
                    "Bass", "Strings", "Ensemble", "Brass",
                    "Reed", "Pipe", "Synth Lead", "Synth Pad",
                    "Synth Effects", "Ethnic", "Percussive", "Sound Effects"};
            final int nCols = names.length; // just show 128 instruments
            final int nRows = 8;

            TableModel dataModel = new AbstractTableModel() {
                public int getColumnCount() {
                    return nCols;
                }

                public int getRowCount() {
                    return nRows;
                }

                public Object getValueAt(int r, int c) {
                    if (instruments != null) {
                        return instruments[c * nRows + r].getName();
                    } else {
                        return Integer.toString(c * nRows + r);
                    }
                }

                public String getColumnName(int c) {
                    return names[c];
                }

                public Class getColumnClass(int c) {
                    return getValueAt(0, c).getClass();
                }

                public boolean isCellEditable(int r, int c) {
                    return false;
                }

                public void setValueAt(Object obj, int r, int c) {
                }
            };

            table = new JTable(dataModel);
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            // Listener for row changes
            ListSelectionModel lsm = table.getSelectionModel();
            lsm.addListSelectionListener(event -> {
                ListSelectionModel sm = (ListSelectionModel) event.getSource();
                if (!sm.isSelectionEmpty()) {
                    currentChannel.row = sm.getMinSelectionIndex();
                }
                programChange(currentChannel.col * nRows + currentChannel.row);
            });

            // Listener for column changes
            lsm = table.getColumnModel().getSelectionModel();
            lsm.addListSelectionListener(event -> {
                ListSelectionModel sm = (ListSelectionModel) event.getSource();
                if (!sm.isSelectionEmpty()) {
                    currentChannel.col = sm.getMinSelectionIndex();
                }
                programChange(currentChannel.col * nRows + currentChannel.row);
            });

            table.setPreferredScrollableViewportSize(new Dimension(nCols * 110, 200));
            table.setCellSelectionEnabled(true);
            table.setColumnSelectionAllowed(true);
            for (String name : names) {
                TableColumn column = table.getColumn(name);
                column.setPreferredWidth(110);
            }
            table.setAutoResizeMode(AUTO_RESIZE_OFF);

            JScrollPane sp = new JScrollPane(table);
            sp.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_NEVER);
            sp.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_ALWAYS);
            add(sp);
        }

        public Dimension getPreferredSize() {
            return new Dimension(800, 170);
        }

        public Dimension getMaximumSize() {
            return new Dimension(800, 170);
        }

        private void programChange(int program) {
            if (instruments != null) {
                synthesizer.loadInstrument(instruments[program]);
            }
            currentChannel.channel.programChange(program);
            if (record) {
                createShortEvent(PROGRAM, program);
            }
        }
    }

    /**
     * A collection of MIDI controllers.
     */
    private class Controls extends JPanel implements ActionListener, ChangeListener, ItemListener {

        public JButton recordB;

        public Controls() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBorder(new EmptyBorder(5, 10, 5, 10));

            JPanel p = new JPanel();
            p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));

            veloS = createSlider("Velocity", p);
            presS = createSlider("Pressure", p);
            revbS = createSlider("Reverb", p);

            // create a slider with a 14-bit range of values for pitch-bend
            bendS = create14BitSlider("Bend", p);

            p.add(Box.createHorizontalStrut(10));
            add(p);

            p = new JPanel();
            p.setBorder(new EmptyBorder(10, 0, 10, 0));
            p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));

            JComboBox<String> combo = new JComboBox<>();
            combo.setPreferredSize(new Dimension(120, 25));
            combo.setMaximumSize(new Dimension(120, 25));
            for (int i = 1; i <= 16; i++) {
                combo.addItem("Channel " + String.valueOf(i));
            }
            combo.addItemListener(this);
            p.add(combo);
            p.add(Box.createHorizontalStrut(20));

            muteCB = createCheckBox("Mute", p);
            soloCB = createCheckBox("Solo", p);
            monoCB = createCheckBox("Mono", p);
            //sustCB = createCheckBox("Sustain", p);

            createButton("All Notes Off", p);
            p.add(Box.createHorizontalStrut(10));
            p.add(mouseOverCB);
            p.add(Box.createHorizontalStrut(10));
            recordB = createButton("Record...", p);
            add(p);
        }

        public JButton createButton(String name, JPanel p) {
            JButton b = new JButton(name);
            b.addActionListener(this);
            p.add(b);
            return b;
        }

        private JCheckBox createCheckBox(String name, JPanel p) {
            JCheckBox cb = new JCheckBox(name);
            cb.addItemListener(this);
            p.add(cb);
            return cb;
        }

        private JSlider createSlider(String name, JPanel p) {
            JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 127, 64);
            slider.addChangeListener(this);
            TitledBorder tb = new TitledBorder(new EtchedBorder());
            tb.setTitle(name + " = 64");
            slider.setBorder(tb);
            p.add(slider);
            p.add(Box.createHorizontalStrut(5));
            return slider;
        }

        private JSlider create14BitSlider(String name, JPanel p) {
            JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 16383, 8192);
            slider.addChangeListener(this);
            TitledBorder tb = new TitledBorder(new EtchedBorder());
            tb.setTitle(name + " = 8192");
            slider.setBorder(tb);
            p.add(slider);
            p.add(Box.createHorizontalStrut(5));
            return slider;
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            JSlider slider = (JSlider) e.getSource();
            int value = slider.getValue();
            TitledBorder tb = (TitledBorder) slider.getBorder();
            String s = tb.getTitle();
            tb.setTitle(s.substring(0, s.indexOf('=') + 1) + String.valueOf(value));
            if (s.startsWith("Velocity")) {
                currentChannel.velocity = value;
            } else if (s.startsWith("Pressure")) {
                currentChannel.channel.setChannelPressure(currentChannel.pressure = value);
            } else if (s.startsWith("Bend")) {
                currentChannel.channel.setPitchBend(currentChannel.bend = value);
            } else if (s.startsWith("Reverb")) {
                currentChannel.channel.controlChange(REVERB, currentChannel.reverb = value);
            }
            slider.repaint();
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getSource() instanceof JComboBox) {
                JComboBox combo = (JComboBox) e.getSource();
                currentChannel = channels[combo.getSelectedIndex()];
                currentChannel.setComponentStates();
            } else {
                JCheckBox cb = (JCheckBox) e.getSource();
                String name = cb.getText();
                if (name.startsWith("Mute")) {
                    currentChannel.channel.setMute(currentChannel.mute = cb.isSelected());
                } else if (name.startsWith("Solo")) {
                    currentChannel.channel.setSolo(currentChannel.solo = cb.isSelected());
                } else if (name.startsWith("Mono")) {
                    currentChannel.channel.setMono(currentChannel.mono = cb.isSelected());
                } else if (name.startsWith("Sustain")) {
                    currentChannel.sustain = cb.isSelected();
                    currentChannel.channel.controlChange(SUSTAIN, currentChannel.sustain ? 127 : 0);
                }
            }
        }

        public void actionPerformed(ActionEvent e) {
            String text = ((JButton) e.getSource()).getText();
            if (text.startsWith("All")) {
                stream(channels).forEach(channel -> channel.channel.allNotesOff());
                keys.stream().forEach(Key::noteToOff);
            } else if (text.startsWith("Record")) {
                if (recordFrame != null) {
                    recordFrame.toFront();
                } else {
                    recordFrame = new RecordFrame();
                }
            }
        }
    }

    /**
     * A frame that allows for midi capture & saving the captured data.
     */
    private class RecordFrame extends JFrame implements ActionListener, MetaEventListener {
        private JButton recordB, playB, saveB;
        private Vector<TrackData> tracks = new Vector<>();
        private TableModel dataModel;
        private JTable table;

        public RecordFrame() {
            super("Midi Capture");
            addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    recordFrame = null;
                }
            });

            sequencer.addMetaEventListener(this);
            try {
                sequence = new Sequence(Sequence.PPQ, 10);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            JPanel panel = new JPanel();
            panel.setBorder(new EmptyBorder(5, 5, 5, 5));
            panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

            recordB = createButton("Record", panel, true);
            playB = createButton("Play", panel, false);
            saveB = createButton("Save...", panel, false);

            getContentPane().add("North", panel);

            final String[] names = {"Channel #", "Instrument"};

            dataModel = new AbstractTableModel() {
                @Override
                public int getColumnCount() {
                    return names.length;
                }

                @Override
                public int getRowCount() {
                    return tracks.size();
                }

                @Override
                public Object getValueAt(int row, int col) {
                    if (col == 0) {
                        return tracks.get(row).chanNum;
                    } else if (col == 1) {
                        return tracks.get(row).name;
                    }
                    return null;
                }

                @Override
                public String getColumnName(int col) {
                    return names[col];
                }

                @Override
                public Class getColumnClass(int c) {
                    return getValueAt(0, c).getClass();
                }

                @Override
                public boolean isCellEditable(int row, int col) {
                    return false;
                }

                @Override
                public void setValueAt(Object val, int row, int col) {
                    if (col == 0) {
                        tracks.get(row).chanNum = (Integer) val;
                    } else if (col == 1) {
                        tracks.get(row).name = (String) val;
                    }
                }
            };

            table = new JTable(dataModel);
            TableColumn col = table.getColumn("Channel #");
            col.setMaxWidth(65);
            table.sizeColumnsToFit(0);

            JScrollPane scrollPane = new JScrollPane(table);
            EmptyBorder eb = new EmptyBorder(0, 5, 5, 5);
            scrollPane.setBorder(new CompoundBorder(eb, new EtchedBorder()));

            getContentPane().add("Center", scrollPane);
            pack();
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            int w = 210;
            int h = 160;
            setLocation(d.width / 2 - w / 2, d.height / 2 - h / 2);
            setSize(w, h);
            setVisible(true);
        }

        private JButton createButton(String name, JPanel p, boolean state) {
            JButton b = new JButton(name);
            b.setFont(new Font("serif", Font.PLAIN, 10));
            b.setEnabled(state);
            b.addActionListener(this);
            p.add(b);
            return b;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();
            if (button.equals(recordB)) {
                record = recordB.getText().startsWith("Record");
                if (record) {
                    track = sequence.createTrack();
                    startTime = System.currentTimeMillis();

                    // add a program change right at the beginning of 
                    // the track for the current instrument
                    createShortEvent(PROGRAM, currentChannel.col * 8 + currentChannel.row);

                    recordB.setText("Stop");
                    playB.setEnabled(false);
                    saveB.setEnabled(false);
                } else {
                    String name;
                    if (instruments != null) {
                        name = instruments[currentChannel.col * 8 + currentChannel.row].getName();
                    } else {
                        name = Integer.toString(currentChannel.col * 8 + currentChannel.row);
                    }
                    tracks.add(new TrackData(currentChannel.num + 1, name, track));
                    table.tableChanged(new TableModelEvent(dataModel));
                    recordB.setText("Record");
                    playB.setEnabled(true);
                    saveB.setEnabled(true);
                }
            } else if (button.equals(playB)) {
                if (playB.getText().startsWith("Play")) {
                    try {
                        sequencer.open();
                        sequencer.setSequence(sequence);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    sequencer.start();
                    playB.setText("Stop");
                    recordB.setEnabled(false);
                } else {
                    sequencer.stop();
                    playB.setText("Play");
                    recordB.setEnabled(true);
                }
            } else if (button.equals(saveB)) {
                try {
                    File file = new File(System.getProperty("user.dir"));
                    JFileChooser fc = new JFileChooser(file);
                    fc.setFileFilter(new javax.swing.filechooser.FileFilter() {
                        public boolean accept(File f) {
                            return f.isDirectory();
                        }

                        public String getDescription() {
                            return "Save as .mid file.";
                        }
                    });
                    if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                        saveMidiFile(fc.getSelectedFile());
                    }
                } catch (SecurityException ex) {
                    JavaSound.showInfoDialog();
                    ex.printStackTrace();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        public void meta(MetaMessage message) {
            if (message.getType() == 47) {  // 47 is end of track
                playB.setText("Play");
                recordB.setEnabled(true);
            }
        }

        public void saveMidiFile(File file) {
            try {
                int[] fileTypes = MidiSystem.getMidiFileTypes(sequence);
                if (fileTypes.length == 0) {
                    System.out.println("Can't save sequence");
                } else {
                    if (MidiSystem.write(sequence, fileTypes[0], file) == -1) {
                        throw new IOException("Problems writing to file");
                    }
                }
            } catch (SecurityException ex) {
                JavaSound.showInfoDialog();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        class TrackData {
            int chanNum;
            String name;
            Track track;

            public TrackData(int chanNum, String name, Track track) {
                this.chanNum = chanNum;
                this.name = name;
                this.track = track;
            }
        } // End class TrackData
    }

    public static void main(String args[]) {
        final MidiSynth midiSynth = new MidiSynth();
        midiSynth.open();
        JFrame f = new JFrame("Midi Synthesizer");
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        f.getContentPane().add("Center", midiSynth);
        f.pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int w = 760;
        int h = 470;
        f.setLocation(screenSize.width / 2 - w / 2, screenSize.height / 2 - h / 2);
        f.setSize(w, h);
        f.setVisible(true);
    }
} 
