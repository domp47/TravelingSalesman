package DataHandler;

import City.City;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.util.List;

public class LoadFromFile {
    /**
     * Loads Vertices from Java File
     *
     * @param path of file for data
     * @return float[][] vertices
     * @throws IOException e
     */
    public static City[] LoadCities(String path) throws IOException {
        File file = new File(path);

        if(!file.exists())
            throw new IOException();

        City[] cities;

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

        cities = new City[nVertices];

        for(int i = 0; i < cities.length; i++){
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

            City city = new City(Float.parseFloat(splitString[1]), Float.parseFloat(splitString[2]));

            cities[i] = city;
        }

        return cities;
    }
}
