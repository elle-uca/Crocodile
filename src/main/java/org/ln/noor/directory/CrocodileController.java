package org.ln.noor.directory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import org.ln.noor.directory.service.DirectoryFlattenService;
import org.ln.noor.directory.service.DirectoryReorderService;
import org.ln.noor.directory.service.DirectoryReorderService.ReorderPlan;
import org.ln.noor.directory.service.DirectoryStatsService;
import org.ln.noor.directory.service.FilesystemService;
import org.ln.noor.directory.util.DirectoryUtils;
import org.ln.noor.directory.view.CrocodileView;
import org.ln.noor.directory.view.FileNameDialog;
import org.ln.noor.directory.view.dialog.FlattenDirectoryDialog;
import org.ln.noor.directory.view.dialog.ReorderDirectoryDialog;

public class CrocodileController {

    private final CrocodileView crocodileView;
    private final DirectoryStatsService statsService;

    private static final Preferences prefs =
            Preferences.userRoot().node("Crocodile");
    private static final String LAST_DIR_KEY = "lastDir";

    private final DirectoryReorderService reorderService =
            new DirectoryReorderService();

    private final FilesystemService filesystemService =
            new FilesystemService();


    public CrocodileController(CrocodileView crocodileView) {
        this.crocodileView = crocodileView;
        this.statsService = new DirectoryStatsService();
    }

    /* -------------------------------------------------
     *  TABLE / DIRECTORY SCAN
     * ------------------------------------------------- */

    void refreshTable() {
        Path root = crocodileView.getSelectedDir();
        if (root == null) return;

        crocodileView.getDirList().clear();
        displayDirectory(root);
        crocodileView.getModel().setDirectories(crocodileView.getDirList());
    }

    void displayDirectory(Path root) {
        try {
            Files.walk(root)
                 .filter(Files::isDirectory)
                 .filter(p -> !p.equals(root))
                 .forEach(crocodileView.getDirList()::add);
        } catch (IOException ex) {
            showError("Errore lettura directory", ex);
        }
    }

    void displayDirectorySearch(Path root) {
        String searchName = crocodileView.getSearchDir();
        crocodileView.getDirList().clear();

        try {
            Files.walk(root)
                 .filter(Files::isDirectory)
                 .filter(p -> p.getFileName().toString().equals(searchName))
                 .forEach(crocodileView.getDirList()::add);
        } catch (IOException ex) {
            showError("Errore ricerca directory", ex);
        }
    }

    /* -------------------------------------------------
     *  ACTIONS
     * ------------------------------------------------- */


    
    public void refreshSearch() {
        if (crocodileView.getSelectedDir() == null) return;

        crocodileView.setSearchDir(
                crocodileView.getSearchDirField().getText());

        displayDirectorySearch(crocodileView.getSelectedDir());
        crocodileView.getModel().setDirectories(crocodileView.getDirList());
        crocodileView.getActionButton().setEnabled(true);
    }

    public void chooseRootDirectory() {
        String lastPath = prefs.get(LAST_DIR_KEY, null);

        JFileChooser fc = (lastPath != null)
                ? new JFileChooser(new File(lastPath))
                : new JFileChooser();

        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (fc.showOpenDialog(crocodileView) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        Path root = fc.getSelectedFile().toPath();
        crocodileView.setSelectedDir(root);
        crocodileView.getRootDirField().setText(root.toString());
        prefs.put(LAST_DIR_KEY, root.toString());

        refreshTable();
        crocodileView.getSearchDirButton().setEnabled(true);
    }

    public void executeMainAction() {

        if (!confirm("Sei sicuro di procedere?")) return;

        List<Path> list = crocodileView.getModel().getDirectories();

        if (list != null && !list.isEmpty()) {
            delete(list);
        } else {
            processAllDirectoriesByName();
        }
    }

    /* -------------------------------------------------
     *  DELETE / EMPTY
     * ------------------------------------------------- */

    public void delete(List<Path> list) {

        boolean deleteDir = crocodileView.getCancelButton().isSelected();
        boolean emptyDir  = crocodileView.getEmptyButton().isSelected();

        for (Path dir : list) {
            try {
                if (deleteDir) {
                    DirectoryUtils.deleteDirectoryRecursively(dir);
                } else if (emptyDir) {
                    DirectoryUtils.emptyDirectory(dir);
                }
            } catch (Exception ex) {
                showError("Errore su directory:\n" + dir, ex);
                return;
            }
        }

        refreshTable();
    }

    public void processAllDirectoriesByName() {

        Path root = crocodileView.getSelectedDir();
        String name = crocodileView.getSearchDir();

        if (root == null || name == null || name.isBlank()) return;

        try {
            if (crocodileView.getCancelButton().isSelected()) {
                DirectoryUtils.deleteAllDirectoriesNamed(root, name);
            } else {
                DirectoryUtils.emptyAllDirectoriesNamed(root, name);
            }
        } catch (Exception ex) {
            showError("Errore operazione globale", ex);
            return;
        }

        refreshTable();
    }

    /* -------------------------------------------------
     *  MOVE
     * ------------------------------------------------- */

    public void moveFilesFromSelectedDir() {

        int row = crocodileView.getSelectedRow();
        if (row < 0) return;

        Path source = crocodileView.getModel().getDirectoryAt(row);

        Object[] options = {
                "Solo file",
                "Solo directory",
                "File + directory"
        };

        int choice = JOptionPane.showOptionDialog(
                crocodileView,
                "Cosa vuoi spostare?",
                "Modalità spostamento",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice < 0) return;

        FileMode mode = switch (choice) {
            case 0 -> FileMode.FILES_ONLY;
            case 1 -> FileMode.DIRS_ONLY;
            case 2 -> FileMode.FILES_AND_DIRS;
            default -> throw new IllegalStateException();
        };

        JFileChooser fc = new JFileChooser(source.toFile());
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (fc.showOpenDialog(crocodileView) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        Path target = fc.getSelectedFile().toPath();
        if (source.equals(target)) {
            showWarning("Origine e destinazione coincidono");
            return;
        }

        if (!confirm("Confermi lo spostamento?")) return;

        try {
            switch (mode) {
                case FILES_ONLY -> DirectoryUtils.moveFiles(source, target);
                case DIRS_ONLY -> DirectoryUtils.moveDirectories(source, target);
                case FILES_AND_DIRS -> DirectoryUtils.moveAll(source, target);
            }
        } catch (Exception ex) {
            showError("Errore spostamento", ex);
            return;
        }

        refreshTable();
    }
    
    private boolean isDirectoryEmpty(Path dir) {
        try (var stream = Files.list(dir)) {
            return stream.findAny().isEmpty();
        } catch (IOException e) {
            return true; // prudenziale
        }
    }

    public void updateMoveMenuState() {

        JMenuItem moveItem = crocodileView.getMenuItemMoveFiles();

        int row = crocodileView.getSelectedRow();
        if (row < 0) {
            moveItem.setEnabled(false);
            return;
        }

        Path dir = crocodileView.getModel().getDirectoryAt(row);
        boolean enabled = !isDirectoryEmpty(dir);

        moveItem.setEnabled(enabled);
    }

    /* -------------------------------------------------
     *  DELETE SINGLE DIRECTORY (popup)
     * ------------------------------------------------- */

   public  void deleteSelectedDirectory() {

        int row = crocodileView.getSelectedRow();
        if (row < 0) {
            showWarning("Seleziona una directory.");
            return;
        }

        Path dir = crocodileView.getModel().getDirectoryAt(row);

        // Conferma 1: cancellazione directory
        int confirmDir = JOptionPane.showConfirmDialog(
                crocodileView,
                "Vuoi cancellare la directory?\n\n" + dir.toAbsolutePath(),
                "Conferma cancellazione",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirmDir != JOptionPane.YES_OPTION) {
            return;
        }

        // Calcolo statistiche SOLO se non è vuota
        DirectoryStatsService.DirStats stats;
        try {
            stats = statsService.countRecursive(dir);
        } catch (IOException ex) {
            showError("Errore nel conteggio del contenuto", ex);
            return;
        }

        // Conferma 2: contenuto
        if (stats.files > 0 || stats.directories > 0) {

            int confirmContent = JOptionPane.showConfirmDialog(
                    crocodileView,
                    "ATTENZIONE: la directory non è vuota.\n\n" +
                    "Verranno eliminati:\n" +
                    "(" + stats.files + " file, " +
                    stats.directories + " directory)\n\n" +
                    "Vuoi cancellare ANCHE tutto il contenuto?",
                    "Conferma cancellazione contenuto",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (confirmContent != JOptionPane.YES_OPTION) {
                return;
            }
        }

        // Esecuzione reale
        try {
            DirectoryUtils.deleteDirectoryRecursively(dir);
        } catch (Exception ex) {
            showError("Errore durante la cancellazione", ex);
            return;
        }

        refreshTable();
        crocodileView.getReportLabel().setText("Directory cancellata");
    }

   public void flattenSelectedDirectory() {

	    int row = crocodileView.getSelectedRow();
	    if (row < 0) {
	        showWarning("Seleziona una directory.");
	        return;
	    }

	    Path dir = crocodileView.getModel().getDirectoryAt(row);
	    Path parent = dir.getParent();

	    if (parent == null) {
	        showWarning("Impossibile cancellare la directory root.");
	        return;
	    }

	    FlattenDirectoryDialog dlg =
	            new FlattenDirectoryDialog(
	                    crocodileView,
	                    dir.getFileName().toString(),
	                    parent.getFileName().toString()
	            );

	    dlg.setVisible(true);

	    if (dlg.getReturnStatus() != FlattenDirectoryDialog.RET_OK) {
	        return;
	    }

	    DirectoryFlattenService service = new DirectoryFlattenService();

	    try {
	        service.flatten(dir, dlg.getStrategy());
	    } catch (Exception ex) {
	        showError("Errore durante l'operazione", ex);
	        return;
	    }

	    refreshTable();
	    crocodileView.getReportLabel().setText("Directory intermedia eliminata");
	}


   public void reorderSelectedDirectory() {

	    int row = crocodileView.getSelectedRow();
	    if (row < 0) {
	        showWarning("Seleziona una directory.");
	        return;
	    }

	    // Directory selezionata
	    Path selectedPath = crocodileView.getModel()
	            .getDirectoryAt(row)
	            .normalize()
	            .toAbsolutePath();

	    // Root operativa dell'app (campo "Root dir")
	    Path operationRoot = crocodileView.getSelectedDir()
	            .normalize()
	            .toAbsolutePath();

	    // Sanity check: la directory deve stare sotto la root operativa
	    if (!selectedPath.startsWith(operationRoot)) {
	        showWarning(
	                "La directory selezionata non è sotto la Root dir:\n" +
	                operationRoot
	        );
	        return;
	    }

	    // Dialog SOLO UI
	    ReorderDirectoryDialog dlg =
	            new ReorderDirectoryDialog(
	                    crocodileView,
	                    operationRoot,
	                    selectedPath
	            );

	    dlg.setVisible(true);

	    if (dlg.getReturnStatus() != ReorderDirectoryDialog.RET_OK) {
	        return;
	    }

	    // Pianificazione semantica (QUI si decide cosa spostare DAVVERO)
	    ReorderPlan plan;
	    try {
	        plan = reorderService.planReorder(
	                operationRoot,
	                selectedPath,
	                dlg.getReferenceSegment(),
	                dlg.getInsertedSegment(),
	                dlg.isInsertBefore()
	        );
	    } catch (IllegalArgumentException ex) {
	        showWarning(ex.getMessage());
	        return;
	    }

	    // Sicurezza: non uscire dalla home utente
	    Path securityRoot = Path.of(System.getProperty("user.home"))
	            .normalize()
	            .toAbsolutePath();

	    if (!plan.targetDir().startsWith(securityRoot)) {
	        showWarning(
	                "Operazione non consentita:\n" +
	                "la directory deve restare sotto:\n" +
	                securityRoot
	        );
	        return;
	    }

	    // La destinazione non deve esistere
	    if (Files.exists(plan.targetDir())) {
	        showWarning(
	                "La directory di destinazione esiste già:\n" +
	                plan.targetDir()
	        );
	        return;
	    }

	    // Preview onesta
	    int confirm = JOptionPane.showConfirmDialog(
	            crocodileView,
	            "Verrà spostata la directory:\n\n" +
	            plan.operatedDir() +
	            "\n\nNuovo percorso:\n\n" +
	            plan.targetDir() +
	            "\n\nConfermi lo spostamento?",
	            "Conferma riorganizzazione",
	            JOptionPane.YES_NO_OPTION,
	            JOptionPane.WARNING_MESSAGE
	    );

	    if (confirm != JOptionPane.YES_OPTION) {
	        return;
	    }

	    // Esecuzione reale
	    try {
	        filesystemService.move(
	                plan.operatedDir(),
	                plan.targetDir()
	        );
	    } catch (IOException ex) {
	        showError("Errore durante lo spostamento", ex);
	        return;
	    }

	    // 🔁 Se è stata spostata la Root operativa, aggiorno la Root dell'app
	    if (plan.operatedDir().equals(operationRoot)) {
	        crocodileView.setSelectedDir(plan.targetDir());
	        crocodileView.getRootDirField().setText(plan.targetDir().toString());
	    }

	    refreshTable();
	}








    /* -------------------------------------------------
     *  UTIL
     * ------------------------------------------------- */

    private boolean confirm(String msg) {
        return JOptionPane.showConfirmDialog(
                crocodileView,
                msg,
                "Conferma",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        ) == JOptionPane.YES_OPTION;
    }

    private void showError(String title, Exception ex) {
        JOptionPane.showMessageDialog(
                crocodileView,
                title + "\n\n" + ex.getMessage(),
                "Errore",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private void showWarning(String msg) {
        JOptionPane.showMessageDialog(
                crocodileView,
                msg,
                "Attenzione",
                JOptionPane.WARNING_MESSAGE
        );
    }

//    private static class DirCount {
//        int files;
//        int dirs;
//    }
//
//    private DirCount countFilesAndDirs(Path root) throws IOException {
//        DirCount c = new DirCount();
//        Files.walk(root).forEach(p -> {
//            if (p.equals(root)) return;
//            if (Files.isDirectory(p)) c.dirs++;
//            else c.files++;
//        });
//        return c;
//    }




    public void addNewDir() {
        int row = crocodileView.getSelectedRow();
        if (row < 0) {
            showWarning("Seleziona una directory.");
            return;
        }

        Path parentDir = crocodileView.getModel().getDirectoryAt(row);

        FileNameDialog dialog = new FileNameDialog(crocodileView, "");
        dialog.setVisible(true);

        if (dialog.getReturnStatus() == FileNameDialog.RET_CANCEL) {
            return;
        }

        String name = dialog.getText();
        if (name == null || name.isBlank()) {
            showWarning("Nome directory non valido.");
            return;
        }

        Path newDir = parentDir.resolve(name);

        try {
            Files.createDirectory(newDir);
        } catch (IOException ex) {
            showError("Errore creazione directory", ex);
            return;
        }

        refreshTable();
    }

    public void renameCurrentDir() {

        int row = crocodileView.getSelectedRow();
        if (row < 0) {
            showWarning("Seleziona una directory.");
            return;
        }

        Path dir = crocodileView.getModel().getDirectoryAt(row);
        Path parent = dir.getParent();

        if (parent == null) {
            showWarning("Impossibile rinominare la directory root.");
            return;
        }

        FileNameDialog dialog =
                new FileNameDialog(crocodileView, dir.getFileName().toString());
        dialog.setVisible(true);

        if (dialog.getReturnStatus() == FileNameDialog.RET_CANCEL) {
            return;
        }

        String newName = dialog.getText();
        if (newName == null || newName.isBlank()) {
            showWarning("Nome directory non valido.");
            return;
        }

        Path target = parent.resolve(newName);

        if (Files.exists(target)) {
            showWarning("Esiste già una directory con questo nome.");
            return;
        }

        try {
            Files.move(dir, target);
        } catch (IOException ex) {
            showError("Errore rinomina directory", ex);
            return;
        }

        refreshTable();
    }

    public void onDirectorySelected() {

        int row = crocodileView.getSelectedRow();
        if (row < 0) {
            clearDirectoryInfo();
            return;
        }

        Path dir = crocodileView.getModel().getDirectoryAt(row);

        // Label 1: info directory
        crocodileView.getReportLabel()
                .setText("Directory selezionata: " + dir.toAbsolutePath());

        // Label 2: statistiche
        try {
            DirectoryStatsService.DirStats stats =
                    statsService.countRecursive(dir);

            crocodileView.getInfoLabel().setText(
                    "Contenuto: " +
                    stats.files + " file, " +
                    stats.directories + " directory"
            );

        } catch (IOException ex) {
            crocodileView.getInfoLabel()
                    .setText("Errore lettura contenuto");
        }

        // opzionale: aggiorna menu Move Files
       // updateMoveFilesAvailability();
    }

    private void clearDirectoryInfo() {
        crocodileView.getReportLabel().setText("");
        crocodileView.getInfoLabel().setText("");
    }

}
