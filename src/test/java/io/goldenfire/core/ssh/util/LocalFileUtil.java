package io.goldenfire.core.ssh.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

public class LocalFileUtil {
    private LocalFileUtil() {
        throw new AssertionError("constructor of an utility class");
    }

    public static Path createDirectories(Path path) {
        try {
            return Files.createDirectories(path);
        } catch (IOException e) {
            throw new IllegalArgumentException("failed to create " + path + " because of " + e.getMessage());
        }
    }

    public static void deleteDirectories(Path path) {
        try {
            Files.walk(path.normalize()).sorted(Collections.reverseOrder())
                    .forEach(LocalFileUtil::deleteFile);
        } catch (IOException e) {
            throw new IllegalArgumentException("failed to create " + path + " because of " + e.getMessage());
        }
    }

    private static void deleteFile(Path file) {
        try {
            Files.delete(file);
        } catch (IOException e) {
            throw new IllegalArgumentException("failed to create " + file.getFileName() + " because of " + e.getMessage());
        }
    }
}
