package DataHandler;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;

public class LoadFromFile {
    /**
     * Loads Vertices from a file chooser
     *
     * @return float[][] vertices
     * @throws IOException e
     */
    public static float[][] LoadVertices() throws IOException{
        File file = FileChooser.chooseFile("File to Load");

        if(file==null||!file.exists())
            throw new IOException();

        return LoadVertices(file);
    }

    /**
     * Loads Vertices from full file path
     *
     * @param path of file
     * @return float[][] vertices
     * @throws IOException e
     */
    public static float[][] LoadVertices(String path) throws IOException {

        File file = new File(path);

        if(!file.exists())
            throw new IOException();

        return LoadVertices(file);
    }

    /**
     * Loads Vertices from Java File
     *
     * @param file of data
     * @return float[][] vertices
     * @throws IOException e
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

        return vertices;
    }
}
