package org.ln.noor.directory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import javax.swing.SwingWorker;

import org.ln.noor.directory.view.DirectoryTableModel;

public class NetworkDirectoryScanner
extends SwingWorker<Void, DirectoryScanResult> {

	private final Path root;
	private final DirectoryTableModel model;

	public NetworkDirectoryScanner(Path root, DirectoryTableModel model) {
		this.root = root;
		this.model = model;
	}

	@Override
	protected Void doInBackground() throws Exception {
		Deque<Path> queue = new ArrayDeque<>();
		queue.add(root);

		while (!queue.isEmpty() && !isCancelled()) {
			Path dir = queue.poll();

			DirectoryScanResult r = new DirectoryScanResult(dir);
			publish(r);                 // appare SUBITO nella JTable

			try (var stream = Files.newDirectoryStream(dir)) {
				for (Path p : stream) {
					if (Files.isDirectory(p)) {
						r.subDirs++;
						queue.add(p);
					} else {
						r.files++;
					}
				}
			} catch (Exception ignored) {}

			r.completed = true;
			publish(r);                // aggiorna i numeri
		}
		return null;
	}

	@Override
	protected void process(List<DirectoryScanResult> chunk) {
		for (DirectoryScanResult r : chunk) {
			model.upsert(r);
		}
	}
}
