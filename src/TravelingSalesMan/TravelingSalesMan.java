package TravelingSalesMan;

import GUI.MainFrame;
import Search.RunSearch;

public class TravelingSalesMan {
    private MainFrame mainFrame;
    private RunSearch runSearch;

    public TravelingSalesMan(){
        mainFrame = new MainFrame(this);
        runSearch = new RunSearch(this);
    }

    public MainFrame getMainFrame() {
        return mainFrame;
    }

    public RunSearch getRunSearch() {
        return runSearch;
    }
}
