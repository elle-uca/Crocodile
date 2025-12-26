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
     * Root operativa dell'app (es: /home/luke/ren)
     */
    private final Path operationRoot;

    /**
     * Directory selezionata nella tabella
     * (es: /home/luke/ren/primaditest)
     */
    private final Path selectedDir;

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
     * Costruisce i segmenti RIORDINABILI:
     *
     * operationRoot = /home/luke/ren
     * selectedDir   = /home/luke/ren/primaditest/xyz
     *
     * Combo:
     *   ren
     *   primaditest
     *   xyz
     */
    private String[] buildSegments() {

        if (!selectedDir.startsWith(operationRoot)) {
            return new String[0];
        }

        List<String> segments = new ArrayList<>();

        // 1) SEMPRE il nome della root operativa (ren)
        segments.add(operationRoot.getFileName().toString());

        // 2) Segmenti sotto la root operativa
        if (!selectedDir.equals(operationRoot)) {
            Path rel = operationRoot.relativize(selectedDir);
            for (int i = 0; i < rel.getNameCount(); i++) {
                segments.add(rel.getName(i).toString());
            }
        }

        return segments.toArray(String[]::new);
    }

    // ---- getters ----

    public int getReturnStatus() {
        return returnStatus;
    }

    public String getInsertedSegment() {
        return insertField.getText().trim();
    }

    public boolean isInsertBefore() {
        return beforeBtn.isSelected();
    }

    public String getReferenceSegment() {
        return (String) segmentBox.getSelectedItem();
    }
}
