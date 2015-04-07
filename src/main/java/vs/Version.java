package vs;

public class Version implements Comparable<Version> {
    public final short major;

    public final short minor;

    public Version(short major, short minor) {
        this.major = major;
        this.minor = minor;
    }

    @Override
    public int compareTo(Version o) {
        int result = Short.compare(this.major, o.major);
        if (result != 0)
            return result;
        return Short.compare(this.minor, o.minor);
    }

    @Override
    public String toString() {
        return major + "." + minor;
    }

}
