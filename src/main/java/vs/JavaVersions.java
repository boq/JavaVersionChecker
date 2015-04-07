package vs;

import java.util.Map;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;

public class JavaVersions {

    public static final Map<Integer, String> majorVersions = ImmutableMap.<Integer, String>builder()
            .put(52, "J2SE 8")
            .put(51, "J2SE 7")
            .put(50, "J2SE 6.0")
            .put(49, "J2SE 5.0")
            .put(48, "JDK 1.4")
            .put(47, "JDK 1.3")
            .put(46, "JDK 1.2")
            .put(45, "JDK 1.1")
            .build();

    public static String getMajorVersion(int version) {
        return Objects.firstNonNull(majorVersions.get(version), "unknown");
    }

}
