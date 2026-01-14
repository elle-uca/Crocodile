package org.ln.noor.directory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.SwingWorker;

public class DirectoryScannerWorker
	extends SwingWorker<List<DirectoryScanResult>, DirectoryScanResult> {

	private final Path root;

	public DirectoryScannerWorker(Path root) {
		this.root = root;
	}

	@Override
	protected List<DirectoryScanResult> doInBackground() throws Exception {

		Map<Path, DirectoryScanResult> map = new HashMap<>();

		Files.walk(root).forEach(p -> {
			if (isCancelled()) return;

			try {
				if (Files.isDirectory(p)) {
					if (!p.equals(root)) {
						map.putIfAbsent(p, new DirectoryScanResult(p));
					}
				} else {
					Path parent = p.getParent();
					DirectoryScanResult r = map.get(parent);
					if (r != null) r.files++;
				}

				Path parent = p.getParent();
				if (parent != null && map.containsKey(parent) && Files.isDirectory(p)) {
					map.get(parent).subDirs++;
				}

			} catch (Exception ignored) {}
		});

		return new ArrayList<>(map.values());
	}
}
