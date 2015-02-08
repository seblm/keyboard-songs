package forchild;

public class P {
    public final Note[] notes;
    public final Duration duration;

    public P(Duration duration, Note... notes) {
        this.notes = notes;
        this.duration = duration;
    }
}
