package GUI;

import City.City;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PathPanel extends JPanel {

    private PathCanvas pathCanvas;

    public PathPanel(){
        this.setPreferredSize(new Dimension(600,600)); // The layout manager will ty to use this size
        this.setLayout(new BorderLayout());

        pathCanvas = new PathCanvas();
        pathCanvas.setLayout(new BorderLayout(10, 10));

        this.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(25, 25, 25, 25), BorderFactory.createLineBorder(Color.black, 6)));
        this.add(pathCanvas);
        repaint();
    }

    public void SetPath(City[] path){
        pathCanvas.SetPath(path);
    }
}
