package org.ln.noor.directory;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.ln.noor.directory.view.DirectoryTableModel;

public class NetworkDirectoryScanner
extends SwingWorker<Void, DirectoryScanResult> {

	private final Path root;
	private final DirectoryTableModel model;
	private int scanned = 0;
	private long lastReportUiMs = 0;  
	private final java.util.function.Consumer<String> onReport;

	public NetworkDirectoryScanner(Path root, DirectoryTableModel model,
			java.util.function.Consumer<String> onReport) {
		this.root = root;
		this.model = model;
		this.onReport = onReport;
	}


	@Override
	 protected Void doInBackground() {
        Deque<Path> queue = new ArrayDeque<>();
        queue.add(root);

        while (!queue.isEmpty() && !isCancelled()) {
            Path dir = queue.poll();
            scanned++;

            DirectoryScanResult r = new DirectoryScanResult(dir);
            publish(r); // inserisce riga subito

            try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
                for (Path p : stream) {
                    if (Files.isDirectory(p)) {
                        r.subDirs++;
                        queue.add(p);
                    } else {
                        r.files++;
                    }
                }
            } catch (Exception ignored) {
                // rete/permessi: ignora e continua
            }

            r.completed = true;
            publish(r); // aggiorna numeri
        }
        return null;
    }

    @Override
    protected void process(List<DirectoryScanResult> chunk) {
        // upsert righe
        for (DirectoryScanResult r : chunk) {
            model.upsert(r);
        }

        // report throttled (max 5/sec)
        long now = System.currentTimeMillis();
        if (onReport != null && now - lastReportUiMs > 200) {
            onReport.accept("Scansionate " + scanned + " directory...");
            lastReportUiMs = now;
        }
    }

    @Override
    protected void done() {
        if (onReport != null) {
            onReport.accept("DONE");
        }
    }
}