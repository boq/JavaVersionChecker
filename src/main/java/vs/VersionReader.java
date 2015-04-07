package vs;

import java.io.*;

public class VersionReader {

    public static class NotClassFile extends RuntimeException {
        private static final long serialVersionUID = -3260007944350700878L;
    }

    public Version readVersion(File file) throws IOException {
        try (InputStream input = new FileInputStream(file)) {
            return readVersion(input);
        }
    }

    public Version readVersion(InputStream is) throws IOException {
        DataInputStream input = new DataInputStream(is);

        int magic = input.readInt();
        if (magic != 0xCAFEBABE)
            throw new NotClassFile();

        short minor = input.readShort();
        short major = input.readShort();

        return new Version(major, minor);
    }

}
