package vs;

import java.io.File;
import java.util.Set;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

public class Main {

    public static void main(String[] argv) {
        OptionParser parser = new OptionParser();
        parser.nonOptions().ofType(File.class).describedAs("file").representsNonOptions();
        parser.accepts("full");
        OptionSet args = parser.parse(argv);

        for (Object a : args.nonOptionArguments())
            checkFile((File)a, args.has("full"));
    }

    private static void checkFile(File target, boolean full) {
        if (!target.exists()) {
            System.err.println(String.format("File %s does not exists", target.getAbsolutePath()));
            return;
        }

        final PathChecker pathChecker = new PathChecker();
        pathChecker.checkPath(target);

        System.out.println("Results for : " + target.getAbsolutePath());

        for (Version v : pathChecker.getVersions()) {
            if (full) {
                System.out.println(String.format("\tVersion: %s (%s)",
                        v, JavaVersions.getMajorVersion(v.major)));

                for (String entry : pathChecker.getFilesWithVersion(v)) {
                    System.out.println("\t\t" + entry);
                }
            } else {
                System.out.println(String.format("\tVersion: %s (%s) -> %d",
                        v, JavaVersions.getMajorVersion(v.major), pathChecker.getFilesWithVersion(v).size()));
            }
        }

        final Set<String> invalidFiles = pathChecker.getInvalidFiles();
        if (!invalidFiles.isEmpty()) {
            System.out.println("\tInvalid files: ");
            for (String entry : invalidFiles) {
                System.out.println("\t\t" + entry);
            }
        }
    }

}
