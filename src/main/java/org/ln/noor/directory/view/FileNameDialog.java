package org.ln.noor.directory.view;

import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import net.miginfocom.swing.MigLayout;

/**
 * Modal dialog that prompts the user for a filename.
 *
 * @author Luca Noale
 */
@SuppressWarnings("serial")
public class FileNameDialog extends JDialog {

	/**
	 * A return status code - returned if Cancel button has been pressed
	 */
	public static final int RET_CANCEL = 0;
	/**
	 * A return status code - returned if OK button has been pressed
	 */
	public static final int RET_OK = 1;

	private JButton cancelButton;
	private JLabel jLabel1;
	private JButton okButton;
	private JTextField textField;

	private int returnStatus = RET_CANCEL;

        /**
         * Creates a new dialog centered on the parent frame and pre-fills the input field.
         *
         * @param parent owner frame used for modality and positioning
         * @param text   initial text to populate in the filename field
         */
        public FileNameDialog(Frame parent, String text) {
                super(parent, true);
                initComponents();
                textField.setText(text);

                // Close the dialog when Esc is pressed
                String cancelName = "cancel";
                InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
                inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), cancelName);
		ActionMap actionMap = getRootPane().getActionMap();
		actionMap.put(cancelName, new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				doClose(RET_CANCEL);
			}
		});
		setLocationRelativeTo(parent);
	}

        /**
         * Returns the user choice for this dialog.
         *
         * @return {@link #RET_OK} when confirmed or {@link #RET_CANCEL} when cancelled
         */
        public int getReturnStatus() {
                return returnStatus;
        }



	private void initComponents() {
		okButton = new JButton();
		cancelButton = new JButton();
		jLabel1 = new JLabel();
		textField = new JTextField();
		jLabel1.setText("File name");

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				closeDialog(evt);
			}
		});
		getContentPane().setLayout(new GridBagLayout());

		okButton.setText("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				okButtonActionPerformed(evt);
			}
		});

		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				textFieldActionPerformed(evt);
			}
		});

		cancelButton.setText("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				cancelButtonActionPerformed(evt);
			}
		});


		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("", "[][grow]", "20[][][]20"));
		panel.add(jLabel1, 		"cell 0 0, wrap");
		panel.add(textField,	"cell 0 1 2 1, growx, wrap, w :250:");
		panel.add(okButton, 	"cell 0 2, align right");
		panel.add(cancelButton, "cell 1 2, align right");

		getContentPane().add(panel);
		pack();
	}      

	private void okButtonActionPerformed(ActionEvent evt) {                                         
		doClose(RET_OK);
	}                                        

	private void cancelButtonActionPerformed(ActionEvent evt) {                                             
		doClose(RET_CANCEL);
	}                                            

        /**
         * Closes the dialog when the window close event is triggered.
         */
        private void closeDialog(WindowEvent evt) {
                doClose(RET_CANCEL);
        }

        private void textFieldActionPerformed(ActionEvent evt) {
                // No additional handling required when Enter is pressed in the text field
        }

	private void doClose(int retStatus) {
		returnStatus = retStatus;
		setVisible(false);
		dispose();

	}

        /**
         * Retrieves the text currently entered in the dialog field.
         *
         * @return filename typed by the user
         */
        public String getText() {
                return textField.getText();
        }


}
