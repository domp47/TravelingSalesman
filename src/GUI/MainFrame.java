package GUI;

import DataHandler.FileChooser;

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

import Search.Chromosome;
import Search.GA.GA;
import Search.RunSearch;
import TravelingSalesMan.TravelingSalesMan;

/**
 * Main Frame holding the GUI
 */
public class MainFrame extends JFrame implements ActionListener, WindowListener {

    //panels and buttons for the GUI
    private JButton browseButton, runButton;
    private JLabel filePathLabel, bestLabel, nThreadsLabel, nSearchesLabel, nIterationsLabel, populationSizeLabel, maxGenLabel;
    private JTextField filePath, best, nThreads, nSearches, nIterations, populationSize, maxGen;
    private JRadioButton ESButton, GAButton, UOXButton, PMXButton;
    private ButtonGroup algorithmButtons, crossOverButtons;

    private JPanel firstRow, secondRow, algorithmChoicePanel, algorithmInputsPanel, crossOverPanel;
    private PathPanel pathPanel;

    private RunSearch.SearchAlgorithm searchAlgorithm = RunSearch.SearchAlgorithm.ES;

    private TravelingSalesMan travelingSalesMan;

    /**
     * Creates the Main Frame
     * @param travelingSalesMan controller for controlling the GUI and Search Algorithm
     */
    public MainFrame(TravelingSalesMan travelingSalesMan){
        this.travelingSalesMan = travelingSalesMan;

        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

        //initialize buttons and text fields and show it on screen
        Init();
        CreateLayout();

        addWindowListener(this);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    /**
     * Initialize all the widgets on GUI
     */
    private void Init(){
        //file specification
        filePathLabel = new JLabel("File Path");
        filePath = new JTextField(20);
        browseButton = new JButton("Browse");
        browseButton.addActionListener(this);

        //widget for shortest path
        bestLabel = new JLabel("Current Best");
        best = new JTextField(8);
        best.setEditable(false);

        //widget for number of threads to run
        nThreadsLabel = new JLabel("Number of Threads");
        nThreads = new JTextField(3);
        nThreads.setDocument(CreateNumberDocument());
        nThreads.setText("1");

        //widget for number of searches to run
        nSearchesLabel = new JLabel("Number of Searches");
        nSearches = new JTextField(10);
        nSearches.setDocument(CreateNumberDocument());
        nSearches.setText("30");

        //widget for number of iterations to run
        nIterationsLabel = new JLabel("Number of Iterations");
        nIterations = new JTextField(10);
        nIterations.setDocument(CreateNumberDocument());
        nIterations.setText("1000000");

        //radio buttons for choosing crossover type
        PMXButton = new JRadioButton("PMX");
        UOXButton = new JRadioButton("UOX");

        //widget for size of population to use
        populationSizeLabel = new JLabel("Population Size");
        populationSize = new JTextField(5);
        populationSize.setDocument(CreateNumberDocument());
        populationSize.setText("70");

        //widget for max number of generations to run for
        maxGenLabel = new JLabel("Max Generations");
        maxGen = new JTextField(7);
        maxGen.setDocument(CreateNumberDocument());
        maxGen.setText("750");

        //widget for starting search
        runButton = new JButton("Run Search");
        runButton.addActionListener(this);
    }

    /**
     * Wipes Main Frame and adds widgets to it
     */
    private void CreateLayout(){
        this.getContentPane().removeAll();

        //adds first row labels that are used of both algorithms
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


        //adds second row based on algorithm selection
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

            crossOverPanel = new JPanel();
            crossOverPanel.setLayout(new FlowLayout());

            crossOverButtons = new ButtonGroup();
            crossOverButtons.add(UOXButton);
            crossOverButtons.add(PMXButton);

            UOXButton.addActionListener(this);
            PMXButton.addActionListener(this);

            crossOverPanel.add(UOXButton);
            crossOverPanel.add(PMXButton);
            PMXButton.setSelected(true);

            algorithmInputsPanel.add(crossOverPanel);
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

    /**
     * disposes GUI on exit of window
     * @param e Window Event
     */
    public void windowClosing(WindowEvent e) {
        System.out.println("Closing Window");
        dispose();
        System.exit(0);
    }

    /**
     *  Lets only numbers be input in threads, searches, iterations, population size and max generations

     * @return new Document to restrict JTextFields
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

    /**
     * This method is run when any buttons are pressed and handles what should be done when a button is pressed
     * @param e Action Event
     */
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == browseButton){ //if browse button was pressed then we need to open a file chooser

            String fullPath = FileChooser.chooseFilePath("File to Read From");

            if(!fullPath.equals("")){ //check to make sure the file is valid
                filePath.setText(fullPath);
                this.revalidate();
            }
        }
        if(e.getSource() == runButton){ // if the run button is pressed then we want to run a new search

            //make sure all the necessary data fields are filled in depending on what algorithm we are using
            if(searchAlgorithm == RunSearch.SearchAlgorithm.ES) {
                if (filePath.getText().equals("") || nThreads.getText().equals("") || nSearches.getText().equals("") || nIterations.getText().equals("")) {
                    JOptionPane.showMessageDialog(this, "Data Fields Missing");
                    return;
                }
            }
            if(searchAlgorithm == RunSearch.SearchAlgorithm.GA){
                if (filePath.getText().equals("") || nThreads.getText().equals("") || populationSize.getText().equals("") || maxGen.getText().equals("")) {
                    JOptionPane.showMessageDialog(this, "Data Fields Missing");
                    return;
                }
            }
            //Try to load the data from given file into a city array
            try {
                travelingSalesMan.getRunSearch().LoadCities(filePath.getText(),Integer.parseInt(nThreads.getText()), Integer.parseInt(nSearches.getText()),
                        Integer.parseInt(nIterations.getText()), Integer.parseInt(populationSize.getText()),Integer.parseInt(maxGen.getText()));
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(this, "Error Loading Data From File Specified");
                return;
            }

            //start search in a separate thread so we have real time updating on the screen
            Thread searchThread = new Thread(travelingSalesMan.getRunSearch());
            searchThread.start();
        }
        if(e.getSource() == ESButton){
            //set the search algorithm to Evolutionary Strategy
            this.searchAlgorithm = RunSearch.SearchAlgorithm.ES;
            travelingSalesMan.getRunSearch().SetSearchAlgorithm(RunSearch.SearchAlgorithm.ES);
            CreateLayout();
            Chromosome bestPath = travelingSalesMan.getRunSearch().getBestChromosome();
            if(bestPath!=null)
                pathPanel.SetPath(bestPath.getPath());
        }
        if(e.getSource() == GAButton){
            //set the search algorithm to Genetic Algorithm
            this.searchAlgorithm = RunSearch.SearchAlgorithm.GA;
            travelingSalesMan.getRunSearch().SetSearchAlgorithm(RunSearch.SearchAlgorithm.GA);
            CreateLayout();
            Chromosome bestPath = travelingSalesMan.getRunSearch().getBestChromosome();
            if(bestPath!=null)
                pathPanel.SetPath(bestPath.getPath());
        }
        if(e.getSource() == PMXButton){
            //sets crossover type to partially mapped crossover
            travelingSalesMan.getRunSearch().setCrossoverType(GA.CrossoverType.PMX);
        }
        if(e.getSource() == UOXButton){
            //sets crossover type to partially mapped crossover
            travelingSalesMan.getRunSearch().setCrossoverType(GA.CrossoverType.UOX);
        }
    }

    public void windowOpened(WindowEvent e) {}
    public void windowActivated(WindowEvent e) {}
    public void windowIconified(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    public void windowDeactivated(WindowEvent e) {}
    public void windowClosed(WindowEvent e) {}

    /**
     * Gets path panel
     * @return path panel
     */
    public PathPanel getPathPanel(){
        return pathPanel;
    }

    /**
     * Gets the TextField for shortest path
     * @return shortest path text field
     */
    public JTextField getBest() {
        return best;
    }

    /**
     * Enable search button
     */
    public void EnableSearch(){
        this.runButton.setVisible(true);
    }

    /**
     * Disable search button
     */
    public void DisableSearch(){
        this.runButton.setVisible(false);
    }
}
