package DataHandler;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;

public class LoadFromFile {
    /**
     * @return
     * @throws IOException
     */
    public static float[][] LoadVertices() throws IOException{
        File file = chooseFile("File to Load");

        if(file==null||!file.exists())
            throw new IOException();

        return LoadVertices(file);
    }

    /**
     * @param path
     * @return
     * @throws IOException
     */
    public static float[][] LoadVertices(String path) throws IOException {

        File file = new File(path);

        if(!file.exists())
            throw new IOException();

        return LoadVertices(file);
    }

    /**
     * @param file
     * @return
     * @throws IOException
     */
    public static float[][] LoadVertices(File file) throws IOException {
        float[][] vertices;


        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String line;

        line = bufferedReader.readLine();

        int nVertices = Integer.parseInt(line);

        //if there is less than one vertex then we cannot continue
        if(nVertices < 1){
            System.out.println("Less than one vertex, not a graph");
            return null;
        }

        vertices = new float[nVertices][];

        for(int i = 0; i < vertices.length; i++){
            line = bufferedReader.readLine();

            if(line==null){
                System.out.println("Error: File Format Not Recognized - Line Number Error");
                return null;
            }

            String[] splitString = line.split("\\s+");

            if(splitString.length!=3){
                System.out.println("Error: File Format Not Recognized - Vertex Number Error");
                return null;
            }

            float[] vertex = new float[2];
            vertex[0] = Float.parseFloat(splitString[1]);
            vertex[1] = Float.parseFloat(splitString[2]);

            vertices[i] = vertex;
        }

        return null;
    }

    /**
     * @param title
     * @return
     */
    private static File chooseFile(String title) {// method to open up a file dialog and return the
        // file. returns null if no file is chosen
        File f = null;
        JFileChooser fc = new JFileChooser();// opens file dialog using swing
        fc.setDialogTitle(title);
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
