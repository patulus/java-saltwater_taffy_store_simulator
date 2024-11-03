package com.patulus.taffystore.gui;

import javax.swing.*;
import java.awt.*;

public class ProgressDialog extends JDialog {
    public ProgressDialog(Frame frame, String message) {
        super(frame, "진행 중", true);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(new JLabel(message, SwingConstants.CENTER), BorderLayout.CENTER);

        setContentPane(panel);
        setSize(200, 100);
        setLocationRelativeTo(frame);
    }
}
