package org.ln.noor.directory;

import java.nio.file.Path;

public class DirectoryScanResult {
    public final Path dir;
    public int files = 0;
    public int subDirs = 0;
    public boolean completed = false;

    public DirectoryScanResult(Path dir) {
        this.dir = dir;
    }
}