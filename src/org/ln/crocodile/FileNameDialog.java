package org.ln.crocodile;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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
 *
 * @author luke
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
     * Creates new form FileNameDialog
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
     * @return the return status of this dialog - one of RET_OK or RET_CANCEL
     */
    public int getReturnStatus() {
        return returnStatus;
    }


    
    private void initComponents() {
        GridBagConstraints gridBagConstraints;

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
//        gridBagConstraints = new GridBagConstraints();
//        gridBagConstraints.gridx = 1;
//        gridBagConstraints.gridy = 1;
//        gridBagConstraints.ipadx = 4;
//        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
//        gridBagConstraints.insets = new Insets(63, 106, 6, 0);
//        getContentPane().add(okButton, gridBagConstraints);
//        getRootPane().setDefaultButton(okButton);
//
//
//        gridBagConstraints = new GridBagConstraints();
//        gridBagConstraints.gridx = 2;
//        gridBagConstraints.gridy = 1;
//        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
//        gridBagConstraints.insets = new Insets(63, 6, 6, 6);
//        getContentPane().add(cancelButton, gridBagConstraints);
//
//       
//        gridBagConstraints = new GridBagConstraints();
//        gridBagConstraints.gridx = 0;
//        gridBagConstraints.gridy = 0;
//        gridBagConstraints.ipadx = 38;
//        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
//        gridBagConstraints.insets = new Insets(63, 6, 0, 0);
//        getContentPane().add(jLabel1, gridBagConstraints);
//
//
//        gridBagConstraints = new GridBagConstraints();
//        gridBagConstraints.gridx = 1;
//        gridBagConstraints.gridy = 0;
//        gridBagConstraints.gridwidth = 2;
//        gridBagConstraints.ipadx = 188;
//        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
//        gridBagConstraints.insets = new Insets(57, 12, 0, 6);
//        getContentPane().add(textField, gridBagConstraints);
        
        JPanel panel = new JPanel();
        
        panel.setLayout(new MigLayout("", "[][grow]", "20[][][]20"));
        panel.add(jLabel1, 		"cell 0 0, wrap");
        panel.add(textField,	"cell 0 1 2 1, growx, wrap, w :150:");
        panel.add(okButton, 	"cell 0 2, align right");
        panel.add(cancelButton, "cell 1 2, align right");

        getContentPane().add(panel);
        pack();
    }// </editor-fold>                        

    private void okButtonActionPerformed(ActionEvent evt) {                                         
        doClose(RET_OK);
    }                                        

    private void cancelButtonActionPerformed(ActionEvent evt) {                                             
        doClose(RET_CANCEL);
    }                                            

    /**
     * Closes the dialog
     */
    private void closeDialog(WindowEvent evt) {                             
        doClose(RET_CANCEL);
    }                            

    private void textFieldActionPerformed(ActionEvent evt) {                                          
        // TODO add your handling code here:
    }                                         
    
    private void doClose(int retStatus) {
        returnStatus = retStatus;
       // System.out.println("ret1    "+returnStatus);
       // System.out.println("text    "+textField.getText());
        setVisible(false);
        dispose();
       
    }

	/**
	 * @return the textField
	 */
	public String getText() {
		return textField.getText();
	}


}
