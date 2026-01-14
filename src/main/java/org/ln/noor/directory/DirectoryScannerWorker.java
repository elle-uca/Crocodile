package org.ln.noor.directory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import javax.swing.SwingWorker;

/**
 * Scans a directory tree asynchronously and reports found directories
 * and progress using callbacks.
 */
public class DirectoryScannerWorker extends SwingWorker<Void, Path> {

    private final Path root;

    private final Consumer<Path> onDirFound;
    private final Consumer<Integer> onProgress;
    private final Consumer<List<Path>> onDone;
    private final Consumer<Exception> onError;

    private final List<Path> all = new ArrayList<>();

    public DirectoryScannerWorker(
            Path root,
            Consumer<Path> onDirFound,
            Consumer<Integer> onProgress,
            Consumer<List<Path>> onDone,
            Consumer<Exception> onError) {

        this.root = root;
        this.onDirFound = onDirFound;
        this.onProgress = onProgress;
        this.onDone = onDone;
        this.onError = onError;
    }

    @Override
    protected Void doInBackground() {
        try (Stream<Path> stream = Files.walk(root)) {

            List<Path> dirs = stream
                    .filter(Files::isDirectory)
                    .filter(p -> !p.equals(root))
                    .toList();

            int total = dirs.size();
            int count = 0;

            for (Path p : dirs) {
                if (isCancelled()) {
                    break;
                }

                all.add(p);
                publish(p);

                count++;
                int percent = (int) ((count * 100.0) / total);
                setProgress(percent);
            }

        } catch (Exception ex) {
            if (onError != null) {
                onError.accept(ex);
            }
        }

        return null;
    }

    @Override
    protected void process(List<Path> chunk) {
        if (onDirFound != null) {
            for (Path p : chunk) {
                onDirFound.accept(p);
            }
        }
    }

    @Override
    protected void done() {
        if (onDone != null) {
            onDone.accept(all);
        }
    }

    /**
     * Attach this worker to a Swing progress listener.
     */
    public void attachProgressListener() {
        addPropertyChangeListener(e -> {
            if ("progress".equals(e.getPropertyName()) && onProgress != null) {
                onProgress.accept((Integer) e.getNewValue());
            }
        });
    }
}
