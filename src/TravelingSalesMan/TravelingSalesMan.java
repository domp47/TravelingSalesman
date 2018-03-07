package TravelingSalesMan;

import GUI.MainFrame;
import Search.RunSearch;

public class TravelingSalesMan {
    private MainFrame mainFrame;
    private RunSearch runSearch;

    /**
     * Constructs controller
     */
    public TravelingSalesMan(){
        mainFrame = new MainFrame(this);
        runSearch = new RunSearch(this);
    }

    /**
     * Gets GUI instance
     * @return MainFrame
     */
    public MainFrame getMainFrame() {
        return mainFrame;
    }

    /**
     * Gets Search Controller instance
     * @return RunSearch
     */
    public RunSearch getRunSearch() {
        return runSearch;
    }
}
