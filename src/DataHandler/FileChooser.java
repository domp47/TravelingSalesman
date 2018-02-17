package DataHandler;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class FileChooser {
    /**
     * JFile Chooser for choosing file to read from
     * @param title for window
     * @return string of file chosen
     */
    public static String chooseFilePath(String title) {// method to open up a file dialog and return the
        // file. returns null if no file is chosen
        File workingDirectory = new File(System.getProperty("user.dir"));

        File f = null;
        JFileChooser fc = new JFileChooser();// opens file dialog using swing
        fc.setDialogTitle(title);
        fc.setCurrentDirectory(workingDirectory);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt", "text");// filter to choose which files
        // to look for
        fc.setFileFilter(filter);
        int rVal = fc.showOpenDialog(null);// gets the button pressed
        if (rVal == JFileChooser.APPROVE_OPTION) {// if the button pressed is
            // the open button
            f = fc.getSelectedFile();// set the file to the file chosen
        }

        if(f!=null)
            return f.getAbsolutePath();
        else
            return "";
    }
    /**
     * JFileChooser for choosing file to load
     *
     * @param title Title of file chooser
     * @return File file to load
     */
    public static File chooseFile(String title) {// method to open up a file dialog and return the
        // file. returns null if no file is chosen
        File workingDirectory = new File(System.getProperty("user.dir"));

        File f = null;
        JFileChooser fc = new JFileChooser();// opens file dialog using swing
        fc.setDialogTitle(title);
        fc.setCurrentDirectory(workingDirectory);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt", "text");// filter to choose which files
        // to look for
        fc.setFileFilter(filter);
        int rVal = fc.showOpenDialog(null);// gets the button pressed
        if (rVal == JFileChooser.APPROVE_OPTION) {// if the button pressed is
            // the open button
            f = fc.getSelectedFile();// set the file to the file chosen
        }
        return f;
    }
}
