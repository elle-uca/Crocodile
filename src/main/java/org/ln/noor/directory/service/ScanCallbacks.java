package org.ln.noor.directory.service;

import java.nio.file.Path;

public interface ScanCallbacks {

    /** Chiamata quando una directory passa il filtro */
    void onDirectory(Path dir);

    /** Chiamata periodicamente per report */
    void onProgress(int scannedCount);

    /** Chiamata a fine scansione */
    void onDone();

    /** Chiamata in caso di errore */
    default void onError(Exception ex) {}
}
