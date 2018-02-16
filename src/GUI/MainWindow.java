package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class MainWindow extends JFrame implements ActionListener, WindowListener {

    private JLabel filePathLabel;
    private JTextField filePath;
    private JButton browseButton;

    private JLabel bestLabel;
    private JTextField best;

    private JLabel nThreadsLabel;
    private JLabel nThreads;

    private JLabel nSearchesLabel;
    private JTextField nSearches;

    private JLabel nIterationsLabel;
    private JTextField nIterations;

    private PathPanel pathPanel;

    public MainWindow(){
        setSize(1000,562);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 7));

        filePathLabel = new JLabel("File Path", SwingConstants.CENTER);
        filePath = new JTextField();
        browseButton = new JButton("Browse");


        this.add(filePathLabel);
        this.add(filePath);
        this.add(browseButton);

        setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {

    }
    @Override
    public void windowOpened(WindowEvent e) {}
    @Override
    public void windowClosing(WindowEvent e) {}
    @Override
    public void windowClosed(WindowEvent e) {}
    @Override
    public void windowIconified(WindowEvent e) {}
    @Override
    public void windowDeiconified(WindowEvent e) {}
    @Override
    public void windowActivated(WindowEvent e) {}
    @Override
    public void windowDeactivated(WindowEvent e) {}
}
