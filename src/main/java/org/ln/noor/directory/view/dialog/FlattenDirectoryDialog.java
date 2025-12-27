package org.ln.noor.directory.view.dialog;

import javax.swing.*;

import org.ln.noor.directory.service.DirectoryFlattenService.ConflictStrategy;

@SuppressWarnings("serial")
public class FlattenDirectoryDialog extends JDialog {

    public static final int RET_OK = 1;
    public static final int RET_CANCEL = 0;

    private int status = RET_CANCEL;

    private final JRadioButton abortBtn = new JRadioButton("Annulla se ci sono duplicati", true);
    private final JRadioButton skipBtn  = new JRadioButton("Salta elementi duplicati");
    private final JRadioButton renameBtn= new JRadioButton("Rinomina automaticamente");

    public FlattenDirectoryDialog(JFrame owner, String dirName, String parentName) {
        super(owner, "Cancella directory intermedia", true);

        JLabel label = new JLabel(
                "<html>La directory <b>" + dirName + "</b> verrà eliminata.<br>" +
                "Il suo contenuto sarà spostato in <b>" + parentName + "</b>.</html>"
        );

        ButtonGroup g = new ButtonGroup();
        g.add(abortBtn);
        g.add(skipBtn);
        g.add(renameBtn);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.add(label);
        center.add(abortBtn);
        center.add(skipBtn);
        center.add(renameBtn);

        JButton ok = new JButton("OK");
        JButton cancel = new JButton("Annulla");

        ok.addActionListener(e -> {
            status = RET_OK;
            dispose();
        });

        cancel.addActionListener(e -> dispose());

        JPanel south = new JPanel();
        south.add(ok);
        south.add(cancel);

        add(center, "Center");
        add(south, "South");

        pack();
        setLocationRelativeTo(owner);
    }

    public int getReturnStatus() {
        return status;
    }

    public ConflictStrategy getStrategy() {
        if (renameBtn.isSelected()) return ConflictStrategy.RENAME;
        if (skipBtn.isSelected()) return ConflictStrategy.SKIP;
        return ConflictStrategy.ABORT;
    }
}
