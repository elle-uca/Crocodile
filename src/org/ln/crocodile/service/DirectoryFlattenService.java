package org.ln.crocodile.service;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class DirectoryFlattenService {

    public enum ConflictStrategy {
        ABORT,
        SKIP,
        RENAME
    }

    public void flatten(Path dir, ConflictStrategy strategy) throws IOException {

        if (dir == null || !Files.isDirectory(dir)) {
            throw new IllegalArgumentException("Not a directory: " + dir);
        }

        Path parent = dir.getParent();
        if (parent == null) {
            throw new IllegalArgumentException("Cannot flatten root directory");
        }

        try (DirectoryStream<Path> contents = Files.newDirectoryStream(dir)) {

            itemLoop:
            for (Path item : contents) {

                Path target = parent.resolve(item.getFileName());

                if (Files.exists(target)) {
                    switch (strategy) {
                        case ABORT -> throw new IOException(
                                "Name conflict: " + target.getFileName()
                        );
                        case SKIP -> {
                            continue itemLoop;
                        }
                        case RENAME -> {
                            target = resolveRename(target);
                        }
                    }
                }

                Files.move(item, target);
            }
        }

        Files.delete(dir);
    }

    private Path resolveRename(Path target) {
        Path parent = target.getParent();
        String name = target.getFileName().toString();

        int i = 1;
        Path candidate;
        do {
            candidate = parent.resolve(name + "_" + i);
            i++;
        } while (Files.exists(candidate));

        return candidate;
    }
}
