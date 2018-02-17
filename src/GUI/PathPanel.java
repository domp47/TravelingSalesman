package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PathPanel extends JPanel {
    public PathPanel(){
        this.setPreferredSize(new Dimension(600,600)); // The layout manager will ty to use this size

        this.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(25, 25, 25, 25), BorderFactory.createLineBorder(Color.black, 6)));

//        this.setBorder(BorderFactory.createLineBorder(Color.black));
        repaint();       // Repaint() calls paintComponent() which is overridden. This will clear the draw panel.
    }
}
