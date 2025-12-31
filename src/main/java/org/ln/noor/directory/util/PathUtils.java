package org.ln.noor.directory.util;

import java.nio.file.Path;
import java.util.Objects;

/**
 * Utilities for Path manipulation.
 *
 * @author Luca Noale
 */
public final class PathUtils {

    private PathUtils() {}

    /**
     * Inserts a directory segment immediately after a given segment in a Path.
     *
     * Example:
     *  original: pippo/pluto/minni
     *  after:    pippo
     *  insert:   topolino
     *  result:   pippo/topolino/pluto/minni
     *
     * @param original original path
     * @param afterSegment segment after which to insert
     * @param segmentToInsert segment to insert
     * @return new Path with inserted segment
     * @throws IllegalArgumentException if afterSegment is not found
     */
    public static Path insertAfter(
            Path original,
            String afterSegment,
            String segmentToInsert
    ) {
        Objects.requireNonNull(original, "original");
        Objects.requireNonNull(afterSegment, "afterSegment");
        Objects.requireNonNull(segmentToInsert, "segmentToInsert");

        if (afterSegment.isBlank() || segmentToInsert.isBlank()) {
            throw new IllegalArgumentException("Segments cannot be blank");
        }

        Path root = original.getRoot(); // null if relative
        int count = original.getNameCount();

        int insertIndex = -1;
        for (int i = 0; i < count; i++) {
            if (original.getName(i).toString().equals(afterSegment)) {
                insertIndex = i;
                break;
            }
        }

        if (insertIndex < 0) {
            throw new IllegalArgumentException(
                    "Segment not found: " + afterSegment + " in " + original
            );
        }

        Path result = (root != null) ? root : Path.of("");

        // before + matched segment
        for (int i = 0; i <= insertIndex; i++) {
            result = result.resolve(original.getName(i));
        }

        // inserted segment
        result = result.resolve(segmentToInsert);

        // rest of the path
        for (int i = insertIndex + 1; i < count; i++) {
            result = result.resolve(original.getName(i));
        }

        return result;
    }
    
    /**
     * Inserts a directory segment immediately BEFORE a given segment in a Path.
     *
     * Example:
     *  original: pippo/pluto/minni
     *  before:   pluto
     *  insert:   topolino
     *  result:   pippo/topolino/pluto/minni
     *
     * @param original original path
     * @param beforeSegment segment before which to insert
     * @param segmentToInsert segment to insert
     * @return new Path with inserted segment
     * @throws IllegalArgumentException if beforeSegment is not found
     */
    public static Path insertBefore(
            Path original,
            String beforeSegment,
            String segmentToInsert
    ) {
        Objects.requireNonNull(original, "original");
        Objects.requireNonNull(beforeSegment, "beforeSegment");
        Objects.requireNonNull(segmentToInsert, "segmentToInsert");

        if (beforeSegment.isBlank() || segmentToInsert.isBlank()) {
            throw new IllegalArgumentException("Segments cannot be blank");
        }

        Path root = original.getRoot(); // null if relative
        int count = original.getNameCount();

        int insertIndex = -1;
        for (int i = 0; i < count; i++) {
            if (original.getName(i).toString().equals(beforeSegment)) {
                insertIndex = i;
                break;
            }
        }

        if (insertIndex < 0) {
            throw new IllegalArgumentException(
                    "Segment not found: " + beforeSegment + " in " + original
            );
        }

        Path result = (root != null) ? root : Path.of("");

        // names before the target segment
        for (int i = 0; i < insertIndex; i++) {
            result = result.resolve(original.getName(i));
        }

        // inserted segment
        result = result.resolve(segmentToInsert);

        // target segment and the rest
        for (int i = insertIndex; i < count; i++) {
            result = result.resolve(original.getName(i));
        }

        return result;
    }

}
