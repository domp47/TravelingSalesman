package OrthSample;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JTextArea;

// This class is a JPanel for drawing random cirlces on

public class CirclesPanel extends JPanel{

    private int count;      // The number of random circles
    private boolean clear;  // Clear the drawing area if true

    // Constructor. this pointers are not necessary, I just put them there so it is clear where methods come from.
    public CirclesPanel(){
        this.setPreferredSize(new Dimension(450,400)); // The layout manager will ty to use this size
        this.setBackground(Color.WHITE);
        this.clear = true;
        repaint();       // Repaint() calls paintComponent() which is overridden. This will clear the draw panel.
    }

    // Sets the number of random circles to draw
    public void setCount(int inCount){
        this.count = inCount;
    }

    // Sets whether or not to clear the draw panel
    public void setClear(boolean cl){
        this.clear = cl;
    }

    // This method will be run when repaint() is called.
    public void paintComponent(Graphics g) {

        if(this.clear){
            super.paintComponent(g);      // This will clear the draw area. Super is necessary here.
            return;
        }
        else{
            super.paintComponent(g);   // You must clear before drawing or weird stuff will happen. (Ghost buttons get drawn)
            Random generator = new Random();
            int x, y, diameter;
            for(int i = 0; i < count; i++){
                x = generator.nextInt(300);   // Generate random x,y coordinates
                y = generator.nextInt(300);
                diameter = generator.nextInt(100);  // Generate random diameter
                g.drawOval(x, y, diameter, diameter);  // Draw the circle
            }  // end for
        } // end else
    } // end paintComponent

} // end of class CirclesPanel