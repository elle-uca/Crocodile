package org.ln.noor.directory.service;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Moves all items from a directory into its parent, resolving conflicts by strategy.
 *
 * @author Luca Noale
 */
public class DirectoryFlattenService {

    /**
     * Strategies to use when a file name conflict occurs during flattening.
     *
     * @author Luca Noale
     */
    public enum ConflictStrategy {
        ABORT,
        SKIP,
        RENAME
    }

    /**
     * Flattens a directory into its parent directory.
     *
     * @param dir       directory to flatten
     * @param strategy  conflict resolution strategy when names already exist
     * @throws IOException              if moving or deleting items fails
     * @throws IllegalArgumentException if the provided path is invalid
     */
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
                            // Skip moving this entry when skipping conflicts
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

    /**
     * Resolves a new path by appending incremental suffixes until a free name is found.
     *
     * @param target original conflicting target
     * @return path with a non-conflicting name
     */
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
