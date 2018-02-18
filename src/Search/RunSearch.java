package Search;

import DataHandler.CreateAdjacencyMatrix;
import DataHandler.LoadFromFile;
import Exceptions.MatrixNotLoadedException;

import java.io.IOException;

public class RunSearch implements Runnable {
    private float[][] vertices;
    private float[][] adjacencyMatrix;

    private int[] shortestPath;
    private float shortestDistance = Float.MAX_VALUE;

    private int nThreads, nSearches, nIterations;

    public RunSearch(){
    }

    public void LoadMatrix(String filePath, int nThreads, int nSearches, int nIterations) throws IOException {
        vertices = LoadFromFile.LoadVertices(filePath);

        if(vertices ==null){
            throw new IOException();
        }

        adjacencyMatrix = CreateAdjacencyMatrix.CreateAdjacencyMatrix(vertices);

        if(adjacencyMatrix==null){
            throw new IOException();
        }

        this.nThreads = nThreads;
        this.nSearches = nSearches;
        this.nIterations = nIterations;

        shortestPath = new int[0];
        shortestDistance = Float.MAX_VALUE;
    }

    public void ESSearch() throws MatrixNotLoadedException {

        if(adjacencyMatrix==null || vertices==null)
            throw new MatrixNotLoadedException();

        ESSearch[] esSearches = new ESSearch[nThreads];
        Thread[] esThreads = new Thread[nThreads];

        for (int i = 0; i < nThreads; i++) {
            esSearches[i] = new ESSearch(this, adjacencyMatrix, nIterations, nSearches, i);
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

    public float[][] GetVertices(){
        return vertices;
    }

    public float[][] GetMatrix(){
        return adjacencyMatrix;
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
        } catch (MatrixNotLoadedException e) {
            e.printStackTrace();
        }
    }
}
