package org.ln.noor.directory.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FilesystemService {

    public void move(Path from, Path to) throws IOException {
        Path parent = to.getParent();
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
        }
        Files.move(from, to);
    }
}
	