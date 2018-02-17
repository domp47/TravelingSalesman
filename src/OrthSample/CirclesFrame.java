package OrthSample;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import java.awt.event.WindowEvent;

// This class extends JFrame so it is a window. This class creates a GUI for drawing random circles.

public class CirclesFrame extends JFrame implements ActionListener, WindowListener{
    private JButton draw, clear;      // Buttons to draw the circles and clear the drawing area
    private JTextArea textArea;       // TextArea for entering the number of circles
    private JPanel buttonPanel;       // Will hold the buttons and text area
    private CirclesPanel drawPanel;   // Jpanel will be used for drawing on
    private int count;                // The number of random circles to draw

    public CirclesFrame(){
        draw = new JButton("Draw");     // Create the buttons
        clear = new JButton("Clear");
        draw.addActionListener(this);   // Add listeners to the buttons. This pointer is a listener because it implements ActionListener
        clear.addActionListener(this);  // by overriding the ActionPerformed method.
        textArea = new JTextArea(1,10); // Add the text area large enough for at least 1 row and 10 columns
        textArea.setBorder(BorderFactory.createTitledBorder("Number of Circles"));
        textArea.setEditable(true);     // Probably true by default but it doesn't hurt to add this

        drawPanel = new CirclesPanel(); // Create the draw panel

        // Putting the buttons and text area in a panel rather than directly on the JFrame
        // helps to ensure they will stay grouped in the GUI and the layout manager
        // will not move them where we don't want them.

        buttonPanel = new JPanel();     // Create a panel for holding the buttons
        buttonPanel.setLayout(new GridLayout(3,1)); // Puts the components in a rectangular grid
        buttonPanel.add(textArea);      // Add the buttons and text area to the button panel
        buttonPanel.add(draw);
        buttonPanel.add(clear);
        add(drawPanel, BorderLayout.EAST);   // Add the panels to the JFrame
        add(buttonPanel, BorderLayout.WEST);

        this.setPreferredSize(new Dimension(620, 425));  // Layout manager will try to use this size
        this.pack(); // Causes this Window to be sized to fit the preferred size and layouts of its subcomponents
        this.setVisible(true);  // Probably true by default but does not hurt to add it

        addWindowListener(this);       // Close the window when user clicks X
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);   // Places the window in the centre of the screen
    }

    //Run this method when the window closes
    public void windowClosing(WindowEvent e) {
        dispose();
        System.exit(0);
    }

    //Run this method when an action event is detected by one of the button listeners
    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == draw){          // Check which button was clicked
            this.drawPanel.setClear(false);
            count =Integer.parseInt(textArea.getText());  // Get the number of cirlces
            this.drawPanel.setCount(count);   // Set the number of cirlces in the draw panel
            this.drawPanel.repaint();         // Repaint calls the paintComponent() method in DrawPanel
        }
        if(e.getSource() == clear){         // If the clear button was clicked
            this.drawPanel.setClear(true);
            this.drawPanel.repaint();
        }
    }

    // The following null implementations are necessary to satisfy the WindowListener Interface
    // If we had not extended JFrame we could have extended WindowAdapter and this would not
    // be necessary.

    public void windowOpened(WindowEvent e) {}
    public void windowActivated(WindowEvent e) {}
    public void windowIconified(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    public void windowDeactivated(WindowEvent e) {}
    public void windowClosed(WindowEvent e) {}


    // Main
    public static void main(String[]args){
        CirclesFrame myCF = new CirclesFrame();
    }
}