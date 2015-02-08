package forchild;

public enum Duration {
    RONDE(4000),
    BLANCHE_POINT(1500),
    BLANCHE(1000),
    NOIRE_POINT(750),
    NOIRE(500),
    CROCHE(250);
    
    final long millis;

    Duration(long millis) {
        this.millis = millis;
    }
}
