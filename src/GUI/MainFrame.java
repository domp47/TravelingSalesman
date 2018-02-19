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
    private JLabel filePathLabel, bestLabel, nThreadsLabel, nSearchesLabel, nIterationsLabel, populationSizeLabel, maxGenLabel;
    private JTextField filePath, best, nThreads, nSearches, nIterations, populationSize, maxGen;
    private JRadioButton ESButton, GAButton;
    private ButtonGroup algorithmButtons;

    private JPanel firstRow, secondRow, algorithmChoicePanel, algorithmInputsPanel;
    private PathPanel pathPanel;

    private RunSearch.SearchAlgorithm searchAlgorithm = RunSearch.SearchAlgorithm.ES;

    private RunSearch runSearch;

    public MainFrame(RunSearch runSearch){
        this.runSearch = runSearch;

        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

        Init();
        CreateLayout();

        addWindowListener(this);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void Init(){
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

        populationSizeLabel = new JLabel("Population Size");
        populationSize = new JTextField(5);
        populationSize.setDocument(CreateNumberDocument());
        populationSize.setText("70");

        maxGenLabel = new JLabel("Max Generations");
        maxGen = new JTextField(7);
        maxGen.setDocument(CreateNumberDocument());
        maxGen.setText("750");

        runButton = new JButton("Run Search");
        runButton.addActionListener(this);
    }

    private void CreateLayout(){
        this.getContentPane().removeAll();

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

        algorithmChoicePanel = new JPanel();
        algorithmChoicePanel.setLayout(new FlowLayout());
        ESButton = new JRadioButton("ES Search");
        GAButton = new JRadioButton("GA Search");

        algorithmButtons = new ButtonGroup();
        algorithmButtons.add(ESButton);
        algorithmButtons.add(GAButton);

        ESButton.addActionListener(this);
        GAButton.addActionListener(this);

        algorithmChoicePanel.add(ESButton);
        algorithmChoicePanel.add(GAButton);

        algorithmInputsPanel = new JPanel();
        algorithmInputsPanel.setLayout(new FlowLayout());

        if(searchAlgorithm == RunSearch.SearchAlgorithm.ES){
            ESButton.setSelected(true);

            algorithmInputsPanel.add(nSearchesLabel);
            algorithmInputsPanel.add(nSearches);
            algorithmInputsPanel.add(nIterationsLabel);
            algorithmInputsPanel.add(nIterations);
        }
        if(searchAlgorithm == RunSearch.SearchAlgorithm.GA){
            GAButton.setSelected(true);

            algorithmInputsPanel.add(populationSizeLabel);
            algorithmInputsPanel.add(populationSize);
            algorithmInputsPanel.add(maxGenLabel);
            algorithmInputsPanel.add(maxGen);
        }

        secondRow.add(algorithmChoicePanel);
        secondRow.add(algorithmInputsPanel);
        secondRow.add(runButton);
        add(secondRow);

        pathPanel = new PathPanel();
        add(pathPanel);

        this.setPreferredSize(new Dimension(1000, 850));
        this.pack();
        this.setVisible(true);
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

            best.setText(Float.toString(runSearch.getBestChromosome().GetFitness()));
            this.revalidate();

            pathPanel.SetPath(runSearch.getBestChromosome().getPath());
            pathPanel.repaint();
        }
        if(e.getSource() == ESButton){
            this.searchAlgorithm = RunSearch.SearchAlgorithm.ES;
            runSearch.SetSearchAlgorithm(RunSearch.SearchAlgorithm.ES);
            CreateLayout();
        }
        if(e.getSource() == GAButton){
            this.searchAlgorithm = RunSearch.SearchAlgorithm.GA;
            runSearch.SetSearchAlgorithm(RunSearch.SearchAlgorithm.GA);
            CreateLayout();
        }
    }

    public void windowOpened(WindowEvent e) {}
    public void windowActivated(WindowEvent e) {}
    public void windowIconified(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    public void windowDeactivated(WindowEvent e) {}
    public void windowClosed(WindowEvent e) {}
}
