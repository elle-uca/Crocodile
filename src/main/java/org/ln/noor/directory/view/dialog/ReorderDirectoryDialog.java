package org.ln.noor.directory.view.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 * Dialog used to insert a new directory before or after a selected path segment.
 *
 * @author Luca Noale
 */
@SuppressWarnings("serial")
public class ReorderDirectoryDialog extends JDialog {

    public static final int RET_OK = 1;
    public static final int RET_CANCEL = 0;

    private int returnStatus = RET_CANCEL;

    private final JTextField insertField = new JTextField(20);
    private final JRadioButton beforeBtn = new JRadioButton("Prima di", true);
    private final JRadioButton afterBtn  = new JRadioButton("Dopo");
    private final JComboBox<String> segmentBox;

    /**
     * Root directory configured for operations (e.g. /home/luke/ren).
     */
    private final Path operationRoot;

    /**
     * Directory selected in the table (e.g. /home/luke/ren/primaditest).
     */
    private final Path selectedDir;

    /**
     * Builds a modal dialog that allows the user to insert a directory relative to another.
     *
     * @param owner          parent frame used for modality and positioning
     * @param operationRoot  base directory of the reordering operation
     * @param selectedDir    directory currently selected by the user
     */
    public ReorderDirectoryDialog(
            JFrame owner,
            Path operationRoot,
            Path selectedDir
    ) {
        super(owner, "Riorganizza directory", true);
        if (!selectedDir.startsWith(operationRoot)) {
            throw new IllegalArgumentException(
                "selectedDir must be under operationRoot"
            );
        }

        this.operationRoot = operationRoot.normalize().toAbsolutePath();
        this.selectedDir   = selectedDir.normalize().toAbsolutePath();

        this.segmentBox = new JComboBox<>(buildSegments());

        initUI();
    }

    private void initUI() {

        JPanel form = new JPanel(new BorderLayout(5, 5));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Nome directory da inserire:"));
        top.add(insertField);

        JPanel middle = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ButtonGroup g = new ButtonGroup();
        g.add(beforeBtn);
        g.add(afterBtn);
        middle.add(new JLabel("Inserire:"));
        middle.add(beforeBtn);
        middle.add(afterBtn);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.add(new JLabel("Directory di riferimento:"));
        bottom.add(segmentBox);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton ok = new JButton("OK");
        JButton cancel = new JButton("Annulla");

        ok.addActionListener(e -> {
            returnStatus = RET_OK;
            dispose();
        });

        cancel.addActionListener(e -> dispose());

        buttons.add(ok);
        buttons.add(cancel);

        form.add(top, BorderLayout.NORTH);
        form.add(middle, BorderLayout.CENTER);
        form.add(bottom, BorderLayout.SOUTH);

        add(form, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(getOwner());
    }

    /**
     * Builds the list of reorderable path segments relative to the operation root.
     *
     * For example:
     * operationRoot = /home/luke/ren
     * selectedDir   = /home/luke/ren/primaditest/xyz
     * produces the combo entries: ren, primaditest, xyz.
     */
    private String[] buildSegments() {

        if (!selectedDir.startsWith(operationRoot)) {
            return new String[0];
        }

        List<String> segments = new ArrayList<>();

        // Always include the operation root name first (e.g. "ren")
        segments.add(operationRoot.getFileName().toString());

        // Then include each segment under the operation root
        if (!selectedDir.equals(operationRoot)) {
            Path rel = operationRoot.relativize(selectedDir);
            for (int i = 0; i < rel.getNameCount(); i++) {
                segments.add(rel.getName(i).toString());
            }
        }

        return segments.toArray(String[]::new);
    }

    // ---- getters ----

    /**
     * Returns the dialog result after user confirmation or cancellation.
     *
     * @return {@link #RET_OK} if confirmed, otherwise {@link #RET_CANCEL}
     */
    public int getReturnStatus() {
        return returnStatus;
    }

    /**
     * Returns the directory name the user wants to insert.
     *
     * @return trimmed directory name to insert
     */
    public String getInsertedSegment() {
        return insertField.getText().trim();
    }

    /**
     * Indicates whether the new directory should be placed before the reference segment.
     *
     * @return {@code true} if insertion should occur before, otherwise after
     */
    public boolean isInsertBefore() {
        return beforeBtn.isSelected();
    }

    /**
     * Returns the segment selected as the placement reference.
     *
     * @return selected reference segment
     */
    public String getReferenceSegment() {
        return (String) segmentBox.getSelectedItem();
    }
}
