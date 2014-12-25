package forchild;

import forchild.songs.LetItGo;
import forchild.songs.PetitPapaNoel;

import javax.imageio.ImageIO;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import static java.lang.System.exit;
import static java.lang.Thread.sleep;
import static java.util.Arrays.stream;

public class MusicPlayer extends JPanel {
    private final MidiChannel channel;

    private Iterator<PartitionElement> song;

    public MusicPlayer() {
        try {
            Synthesizer synthesizer = MidiSystem.getSynthesizer();
            synthesizer.open();
            synthesizer.loadInstrument(synthesizer.getDefaultSoundbank().getInstruments()[0]);

            channel = synthesizer.getChannels()[0];
            song = new LetItGo().iterator();
            add(new JLabel(new ImageIcon(ImageIO.read(new File("santaclaus.jpg")))));
        } catch (MidiUnavailableException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void play() {
        song.forEachRemaining(this::play);
    }
    
    private void play(PartitionElement partitionElement) {
        stream(partitionElement.notes).forEach(note -> channel.noteOn(note.number, 64));
        try {
            sleep(partitionElement.duration.millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        final MusicPlayer musicPlayer = new MusicPlayer();
        JFrame f = new JFrame("Music player");
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                exit(0);
            }
        });
        f.setLayout(new FlowLayout());
        f.getContentPane().add(musicPlayer);
        f.pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int w = 500;
        int h = 293;
        f.setLocation(screenSize.width / 2 - w / 2, screenSize.height / 2 - h / 2);
        f.setSize(w, h);

        f.setVisible(true);
        musicPlayer.play();
    }
}
