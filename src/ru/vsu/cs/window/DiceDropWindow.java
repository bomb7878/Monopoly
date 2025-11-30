package ru.vsu.cs.window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class DiceDropWindow extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JLabel diceDropLabel;

    private boolean isConfirmed = false;

    public DiceDropWindow() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        diceDropLabel.setFont(new Font("Times New Roman", Font.ITALIC, 24));
        diceDropLabel.setPreferredSize(new Dimension(200, 50));

        this.setTitle("Бросок Костей");
        this.setLocationRelativeTo(null);
        setSize(250,150);

        Random RND = new Random();
        int diceDrop = RND.nextInt(2, 12);
        diceDropLabel.setText(diceDrop + "");

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        contentPane.registerKeyboardAction(
                e -> onCancel(),
                KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
        );

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                onCancel();
            }
        });
    }

    private void onCancel() {
        this.isConfirmed = true;
        setVisible(false);
    }

    private void onOK() {
        this.isConfirmed = true;
        setVisible(false);
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public int getDiceDrop() {
        return Integer.parseInt(diceDropLabel.getText());
    }
}
