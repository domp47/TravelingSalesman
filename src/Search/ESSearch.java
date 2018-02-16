package Search;

import java.util.Random;
//import java.util.concurrent.ThreadLocalRandom;

public class ESSearch {

    private final int N_ITERATIONS = 1000000;
    private final int N_EPOCHS = 30;

    private float[][] adjacencyMatrix;
    private int[] shortestPath;
    private float shortestDistance = Float.MAX_VALUE;

    public ESSearch(float[][] adjacencyMatrix){
        this.adjacencyMatrix = adjacencyMatrix;
    }

    public void Search(){

        for (int epochs = 0; epochs < N_EPOCHS; epochs++) {
            int[] ranPath = GenerateRandomPath(adjacencyMatrix.length);

            for (int i = 0; i < N_ITERATIONS; i++) {

                float distance = GetDistance(ranPath);

                if(distance<shortestDistance){
                    System.out.println("EPOCH #:" + epochs +" - New Shortest Distance: "+distance);
                    shortestPath = ranPath.clone();
                    shortestDistance = distance;
                }
                Mutate.Mutate(ranPath);
            }
        }

    }

    /**
     *
     * Gets the total distance of given path
     *
     * @param path to step over
     * @return total distance of path
     */
    private float GetDistance(int[] path){
       float distance = 0;

       for (int i = 0; i < path.length-1; i++) {
           distance += adjacencyMatrix[path[i]][path[i+1]];
       }

       return distance;
    }

    /**
     * Generates random path of size n
     *
     * @param size of path to generate
     * @return random path
     */
    private int[] GenerateRandomPath(int size){
        int[] path = new int[size];

        for(int i = 0; i < size; i++){
            path[i] = i;
        }

        Shuffle(path);
        return path;
    }

    /**
     * Shuffles the array randomly
     *
     * @param ar to shuffle
     */
    private void Shuffle(int[] ar) {
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    /**
     * @return shortest path
     */
    public int[] getShortestPath() {
        return shortestPath;
    }

    /**
     * @return shortest distance
     */
    public float getShortestDistance() {
        return shortestDistance;
    }
}
