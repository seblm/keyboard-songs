package forchild;

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
import java.util.Iterator;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.System.exit;
import static java.lang.Thread.sleep;
import static java.util.Arrays.stream;
import static java.util.concurrent.Executors.newSingleThreadExecutor;

public class UseKeyboard extends JPanel implements KeyListener {
    private final MidiChannel channel;
    private final Iterator<P> song;
    private final Executor executor;
    private final ReentrantLock lock;

    public UseKeyboard() {
        addImage();
        channel = initializeSynthesizer().getChannels()[0];
        song = new PetitPapaNoel().infiniteIterator();
        executor = newSingleThreadExecutor();
        lock = new ReentrantLock();
    }

    private Synthesizer initializeSynthesizer() {
        Synthesizer synthesizer;
        try {
            synthesizer = MidiSystem.getSynthesizer();
            synthesizer.open();
        } catch (MidiUnavailableException e) {
            throw new RuntimeException(e);
        }
        synthesizer.loadInstrument(synthesizer.getDefaultSoundbank().getInstruments()[0]);
        return synthesizer;
    }

    private void addImage() {
        try {
            add(new JLabel(new ImageIcon(ImageIO.read(new File("santaclaus.jpg")))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    private void play(P partitionElement) {
        stream(partitionElement.notes).forEach(note -> channel.noteOn(note.number, 64));
    }

    public static void main(String[] args) {
        final UseKeyboard useKeyboard = new UseKeyboard();
        JFrame f = new JFrame("Petit papa Noël");
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                exit(0);
            }
        });
        f.setLayout(new FlowLayout());
        f.getContentPane().add(useKeyboard);
        f.pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int w = 500;
        int h = 293;
        f.setLocation(screenSize.width / 2 - w / 2, screenSize.height / 2 - h / 2);
        f.setSize(w, h);
        f.addKeyListener(useKeyboard);

        f.setVisible(true);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (lock.tryLock()) {
            executor.execute(() -> {
                lock.lock();
                P partitionElement = song.next();
                play(partitionElement);
                try {
                    sleep(3 * (partitionElement.duration.millis / 4));
                } catch (InterruptedException exception) {
                    throw new RuntimeException(exception);
                } finally {
                    lock.unlock();
                }
            });
            lock.unlock();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
