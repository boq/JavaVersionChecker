package vs;

import java.io.*;
import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.google.common.collect.Sets;
import com.google.common.collect.TreeMultimap;

public class PathChecker {

    private final VersionReader reader = new VersionReader();

    private final TreeMultimap<Version, String> files = TreeMultimap.create();

    private final SortedSet<String> invalidFiles = Sets.newTreeSet();

    public void checkPath(File target) {
        if (target.isDirectory())
            checkDirectory(target);
        else if (target.getName().endsWith(".jar"))
            checkJar(target);
        else if (target.getName().endsWith(".class"))
            checkClass(target);
    }

    private void checkClass(File target) {
        try {
            Version v = reader.readVersion(target);
            files.put(v, target.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Failed to check version of file " + target.getAbsolutePath());
            e.printStackTrace(System.err);
            invalidFiles.add(target.getAbsolutePath());
        }
    }

    private void checkJar(File target) {
        try {
            try (InputStream is = new FileInputStream(target);
                    ZipInputStream zis = new ZipInputStream(is)) {
                ZipEntry entry;
                while ((entry = zis.getNextEntry()) != null) {
                    final String entryName = entry.getName();
                    if (entryName.endsWith(".class")) {
                        final String id = target.getAbsolutePath() + "!" + entryName;
                        try {
                            byte[] data = new byte[8];
                            zis.read(data);
                            Version v = reader.readVersion(new ByteArrayInputStream(data));
                            files.put(v, id);
                        } catch (IOException e) {
                            System.err.println("Failed to check version of entry " + entryName + " from archive " + target.getAbsolutePath());
                            e.printStackTrace(System.err);
                            invalidFiles.add(id);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to check version of file " + target.getAbsolutePath());
            e.printStackTrace(System.err);
            invalidFiles.add(target.getAbsolutePath());
        }
    }

    private void checkDirectory(File target) {
        System.err.println("Entering dir: " + target);

        for (File child : target.listFiles())
            checkPath(child);
    }

    public SortedSet<Version> getVersions() {
        return Collections.unmodifiableSortedSet(files.keySet());
    }

    public SortedSet<String> getFilesWithVersion(Version v) {
        return Collections.unmodifiableSortedSet(files.get(v));
    }

    public Set<String> getInvalidFiles() {
        return Collections.unmodifiableSortedSet(invalidFiles);
    }
}
