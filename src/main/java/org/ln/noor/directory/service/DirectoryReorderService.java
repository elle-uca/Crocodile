package org.ln.noor.directory.service;

import java.nio.file.Path;
import java.util.Objects;

/**
 * Pure domain service.
 * Decides WHICH directory must be moved and WHERE,
 * according to reorder semantics.
 *
 * No Swing.
 * No filesystem operations.
 */
public class DirectoryReorderService {

    /**
     * Result of a reorder planning.
     *
     * @param operatedDir directory that will be physically moved
     * @param targetDir   final absolute target path of operatedDir
     */
    public record ReorderPlan(
            Path operatedDir,
            Path targetDir
    ) {}

    /**
     * Computes a reorder plan.
     *
     * Semantics:
     * - Reorder ALWAYS acts on the directory identified by the reference segment
     * - Never on the selected leaf alone
     *
     * @param operationRoot   root dir chosen by the user (e.g. /home/luke/uno)
     * @param selectedPath    path selected in the table (may be deep)
     * @param reference       segment chosen in the combo (e.g. "uno", "due")
     * @param inserted        name of the directory to insert
     * @param insertBefore    true = before reference, false = after reference
     *
     * @return ReorderPlan describing WHAT to move and WHERE
     *
     * @throws IllegalArgumentException if parameters are inconsistent
     */
    public ReorderPlan planReorder(
            Path operationRoot,
            Path selectedPath,
            String reference,
            String inserted,
            boolean insertBefore
    ) {

        Objects.requireNonNull(operationRoot, "operationRoot");
        Objects.requireNonNull(selectedPath, "selectedPath");
        Objects.requireNonNull(reference, "reference");
        Objects.requireNonNull(inserted, "inserted");

        if (inserted.isBlank()) {
            throw new IllegalArgumentException("Inserted segment is empty");
        }

        operationRoot = operationRoot.normalize().toAbsolutePath();
        selectedPath  = selectedPath.normalize().toAbsolutePath();

        if (!selectedPath.startsWith(operationRoot)) {
            throw new IllegalArgumentException(
                    "Selected path is not under operation root");
        }

        String rootName = operationRoot.getFileName().toString();

        /*
         * CASE 1
         * Reference == root operativa (es: "uno")
         * → si sposta l'intera root operativa
         */
        if (reference.equals(rootName)) {

            Path operatedDir = operationRoot;

            Path parent = operationRoot.getParent();
            if (parent == null) {
                throw new IllegalArgumentException(
                        "Operation root has no parent");
            }

            Path targetDir = insertBefore
                    ? parent.resolve(inserted).resolve(rootName)
                    : parent.resolve(rootName).resolve(inserted);

            return new ReorderPlan(
                    operatedDir,
                    targetDir.normalize().toAbsolutePath()
            );
        }

        /*
         * CASE 2
         * Reference è un segmento sotto la root operativa
         * → si sposta l'intero ramo identificato da reference
         */

        // Path relativo alla root operativa
        Path rel = operationRoot.relativize(selectedPath);

        // Trova l'indice del segmento di riferimento
        int refIndex = indexOf(rel, reference);
        if (refIndex < 0) {
            throw new IllegalArgumentException(
                    "Reference segment not found: " + reference);
        }

        // Directory che verrà realmente spostata
        Path operatedDir = operationRoot.resolve(
                rel.subpath(0, refIndex + 1)
        ).normalize().toAbsolutePath();

        Path operatedParent = operatedDir.getParent();
        if (operatedParent == null) {
            throw new IllegalArgumentException(
                    "Cannot determine parent of operated directory");
        }

        Path targetDir = insertBefore
                ? operatedParent.resolve(inserted)
                                  .resolve(operatedDir.getFileName())
                : operatedDir.resolve(inserted);

        return new ReorderPlan(
                operatedDir,
                targetDir.normalize().toAbsolutePath()
        );
    }

    /* ----------------------------------------------------- */

    private int indexOf(Path rel, String segment) {
        for (int i = 0; i < rel.getNameCount(); i++) {
            if (rel.getName(i).toString().equals(segment)) {
                return i;
            }
        }
        return -1;
    }
}
