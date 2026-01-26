package org.ln.noor.directory.service;


import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Predicate;

public class DirectoryScannerCore implements Runnable {

    private final Path root;
    private final Predicate<Path> filter;
    private final ScanCallbacks callbacks;

    private volatile boolean cancelled = false;
    private int scanned = 0;
    private long lastReportMs = 0;

    public DirectoryScannerCore(
            Path root,
            Predicate<Path> filter,
            ScanCallbacks callbacks) {

        this.root = root;
        this.filter = filter;
        this.callbacks = callbacks;
    }

    public void cancel() {
        cancelled = true;
    }

    @Override
    public void run() {

        Deque<Path> queue = new ArrayDeque<>();
        queue.add(root);

        while (!queue.isEmpty() && !cancelled) {

            Path dir = queue.poll();
            scanned++;

            try (DirectoryStream<Path> stream =
                         Files.newDirectoryStream(dir)) {

                for (Path p : stream) {
                    if (Files.isDirectory(p)) {
                        queue.add(p); // BFS completo
                    }
                }

            } catch (Exception ex) {
                callbacks.onError(ex);
            }

            // filtro di visibilità
            if (filter == null || filter.test(dir)) {
                callbacks.onDirectory(dir);
            }

            // progress throttled
            long now = System.currentTimeMillis();
            if (now - lastReportMs > 200) {
                callbacks.onProgress(scanned);
                lastReportMs = now;
            }
        }

        callbacks.onDone();
    }
}

