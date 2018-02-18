package Search;

import City.City;
import DataHandler.LoadFromFile;
import Exceptions.CityNotLoadedException;
import Search.ES.ESSearch;

import java.io.IOException;

public class RunSearch implements Runnable {

    private City[] cities = null;

    private int[] shortestPath;
    private float shortestDistance = Float.MAX_VALUE;

    private int nThreads, nSearches, nIterations;

    public RunSearch(){
    }

    public void LoadMatrix(String filePath, int nThreads, int nSearches, int nIterations) throws IOException {

        cities = LoadFromFile.LoadCities(filePath);

        this.nThreads = nThreads;
        this.nSearches = nSearches;
        this.nIterations = nIterations;

        shortestPath = new int[0];
        shortestDistance = Float.MAX_VALUE;
    }

    public void ESSearch() throws CityNotLoadedException {

        if(cities==null)
            throw new CityNotLoadedException();

        ESSearch[] esSearches = new ESSearch[nThreads];
        Thread[] esThreads = new Thread[nThreads];

        for (int i = 0; i < nThreads; i++) {
            esSearches[i] = new ESSearch(this, cities, nIterations, nSearches, i);
            esThreads[i] = new Thread(esSearches[i]);
            esThreads[i].start();
        }

        for (int i = 0; i < nThreads; i++) {
            try {
                esThreads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public City[] GetCities(){
        return cities;
    }

    public synchronized void SetShortest(int[] path, float distance){
        shortestPath = path;
        shortestDistance = distance;
    }

    public synchronized float GetShortestDistance(){
        return shortestDistance;
    }

    public synchronized int[] getShortestPath() {
        return shortestPath;
    }

    @Override
    public void run() {
        try {
            ESSearch();
        } catch (CityNotLoadedException e) {
            e.printStackTrace();
        }
    }
}
