package GUI;

import DataHandler.FileChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class MainFrame extends JFrame implements ActionListener, WindowListener {

    private JButton browseButton, runButton;
    private JLabel filePathLabel, bestLabel, nThreadsLabel, nSearchesLabel, nIterationsLabel;
    private JTextField filePath, best, nThreads, nSearches, nIterations;
    private JPanel firstRow, secondRow;
    private PathPanel pathPanel;


    public MainFrame(){

        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

        filePathLabel = new JLabel("File Path");
        filePath = new JTextField(20);
        browseButton = new JButton("Browse");
        browseButton.addActionListener(this);

        bestLabel = new JLabel("Current Best");
        best = new JTextField(8);
        best.setEditable(false);

        nThreadsLabel = new JLabel("Number of Threads");
        nThreads = new JTextField(3);

        nSearchesLabel = new JLabel("Number of Searches");
        nSearches = new JTextField(10);

        nIterationsLabel = new JLabel("Number of Iterations");
        nIterations = new JTextField(10);

        runButton = new JButton("Run Search");
        runButton.addActionListener(this);

        firstRow = new JPanel();
        firstRow.setLayout(new FlowLayout());
        firstRow.add(filePathLabel);
        firstRow.add(filePath);
        firstRow.add(browseButton);
        firstRow.add(bestLabel);
        firstRow.add(best);
        firstRow.add(nThreadsLabel);
        firstRow.add(nThreads);
        add(firstRow);

        secondRow = new JPanel();
        secondRow.setLayout(new FlowLayout());
        secondRow.add(nSearchesLabel);
        secondRow.add(nSearches);
        secondRow.add(nIterationsLabel);
        secondRow.add(nIterations);
        secondRow.add(runButton);
        add(secondRow);

        pathPanel = new PathPanel();
        add(pathPanel);

        this.setPreferredSize(new Dimension(1000, 850));
        this.pack();
        this.setVisible(true);

        addWindowListener(this);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    //Run this method when the window closes
    public void windowClosing(WindowEvent e) {
        System.out.println("Closing Window");
        dispose();
        System.exit(0);
    }

    //Run this method when an action event is detected by one of the button listeners
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == browseButton){
            String fullPath = FileChooser.chooseFilePath("File to Read From");

            if(!fullPath.equals("")){
                filePath.setText(fullPath);
                this.revalidate();
            }
        }
        if(e.getSource() == runButton){
            System.out.println("Run Stuff Now");
        }
    }

    public void windowOpened(WindowEvent e) {}
    public void windowActivated(WindowEvent e) {}
    public void windowIconified(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    public void windowDeactivated(WindowEvent e) {}
    public void windowClosed(WindowEvent e) {}
}
