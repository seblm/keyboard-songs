package forchild;

public enum Duration {
    BLANCHE_POINT(1000),
    BLANCHE(1000),
    NOIRE_POINT(7500),
    NOIRE(500),
    CROCHE(250);
    
    final long millis;

    Duration(long millis) {
        this.millis = millis;
    }
}
