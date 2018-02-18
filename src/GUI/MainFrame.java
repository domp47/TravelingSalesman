package GUI;

import DataHandler.FileChooser;
import Search.RunSearch;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

public class MainFrame extends JFrame implements ActionListener, WindowListener {

    private JButton browseButton, runButton;
    private JLabel filePathLabel, bestLabel, nThreadsLabel, nSearchesLabel, nIterationsLabel;
    private JTextField filePath, best, nThreads, nSearches, nIterations;
    private JPanel firstRow, secondRow;
    private PathPanel pathPanel;

    private RunSearch runSearch;

    public MainFrame(RunSearch runSearch){
        this.runSearch = runSearch;

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
        nThreads.setDocument(CreateNumberDocument());
        nThreads.setText("1");

        nSearchesLabel = new JLabel("Number of Searches");
        nSearches = new JTextField(10);
        nSearches.setDocument(CreateNumberDocument());
        nSearches.setText("30");

        nIterationsLabel = new JLabel("Number of Iterations");
        nIterations = new JTextField(10);
        nIterations.setDocument(CreateNumberDocument());
        nIterations.setText("1000000");

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

    /**
     *  Lets only numbers be input in threads, searches, and iterations

     * @return new Document to restring JTextFields
     */
    private PlainDocument CreateNumberDocument(){
        PlainDocument doc = new PlainDocument();
        doc.setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int off, String str, AttributeSet attr)
                    throws BadLocationException
            {
                fb.insertString(off, str.replaceAll("\\D++", ""), attr);  // remove non-digits
            }
            @Override
            public void replace(FilterBypass fb, int off, int len, String str, AttributeSet attr)
                    throws BadLocationException
            {
                fb.replace(off, len, str.replaceAll("\\D++", ""), attr);  // remove non-digits
            }
        });
        return doc;
    }

    //Run this method when an action event is detected by one of the button listeners
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == browseButton){

            //TODO DELETE MEEEEEE
//            try {
//                runSearch.LoadMatrix("/home/dom/Projects/TravelingSalesman/berlin52.txt",1,30,1000000);
//            } catch (IOException e1) {
//                e1.printStackTrace();
//            }
//
//            Thread searchThread = new Thread(runSearch);
//            searchThread.start();
//
//            runButton.setEnabled(false);
//
//            try {
//                searchThread.join();
//            } catch (InterruptedException e1) {
//                e1.printStackTrace();
//            }
//            runButton.setEnabled(true);
//
//            pathPanel.SetPath(runSearch.getShortestPath(),runSearch.GetVertices());


            String fullPath = FileChooser.chooseFilePath("File to Read From");

            if(!fullPath.equals("")){
                filePath.setText(fullPath);
                this.revalidate();
            }
        }
        if(e.getSource() == runButton){
            if( filePath.getText().equals("") || nThreads.getText().equals("") || nSearches.getText().equals("") || nIterations.getText().equals("") ){
                JOptionPane.showMessageDialog(this, "Data Fields Missing");
                return;
            }

            try {
                runSearch.LoadMatrix(filePath.getText(),Integer.parseInt(nThreads.getText()), Integer.parseInt(nSearches.getText()),Integer.parseInt(nIterations.getText()));
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(this, "Error Loading Data From File Specified");
                return;
            }

            Thread searchThread = new Thread(runSearch);
            searchThread.start();

            runButton.setEnabled(false);

            try {
                searchThread.join();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            runButton.setEnabled(true);

            best.setText(Float.toString(runSearch.GetShortestDistance()));
            this.revalidate();

            pathPanel.SetPath(runSearch.getShortestPath(), runSearch.GetCities());
            pathPanel.repaint();
        }
    }

    public void windowOpened(WindowEvent e) {}
    public void windowActivated(WindowEvent e) {}
    public void windowIconified(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    public void windowDeactivated(WindowEvent e) {}
    public void windowClosed(WindowEvent e) {}
}
