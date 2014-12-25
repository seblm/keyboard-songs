package forchild;

public class PartitionElement {
    public final Note[] notes;
    public final Duration duration;

    public PartitionElement(Duration duration, Note... notes) {
        this.notes = notes;
        this.duration = duration;
    }
}
