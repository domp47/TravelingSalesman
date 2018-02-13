package Search;

import java.util.concurrent.ThreadLocalRandom;

public class EASearch {

    private final int N_ITERATIONS = 1000000;
    private final int N_EPOCHS = 30;
    private final int N_INTERCHANGES = 3;

    private float[][] adjacencyMatrix;
    private int[] shortestPath;
    private float shortestDistance = Float.MAX_VALUE;

    public EASearch(float[][] adjacencyMatrix){
        this.adjacencyMatrix = adjacencyMatrix;
    }

    public void Search(){
        for (int epochs = 0; epochs < N_EPOCHS; epochs++) {
            int[] ranPath = GenerateRandomPath(adjacencyMatrix.length);
            for (int i = 0; i < N_ITERATIONS; i++) {

                float distance = GetDistance(ranPath);

                if(distance<shortestDistance){
                    shortestPath = ranPath.clone();
                    shortestDistance = distance;
                }
                Mutate(ranPath);
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
     *
     * Mutates array to interchange 3 of the vertices
     *
     * @param array to mutate
     */
    private void Mutate(int[] array){
        int[] ranIndexes = new int[N_INTERCHANGES];

        for(int i = 0; i < N_INTERCHANGES; i++){
            ranIndexes[i] = -1;
        }

        for(int i = 0; i < N_INTERCHANGES;){
            int ranIndex = ThreadLocalRandom.current().nextInt(array.length);

                if(!Contains(ranIndexes,ranIndex)){
                    ranIndexes[i] = ranIndex;
                    i++;
                }
        }

        int[] ranPermutation = ranIndexes.clone();
        Shuffle(ranPermutation);

        int[] origVals = array.clone();

        for(int i = 0; i < ranPermutation.length; i++){
             array[ranPermutation[i]] = origVals[ranIndexes[i]];
        }
    }

    /**
     *
     * Searches array for instance of val
     *
     * @param array to search in
     * @param val to search for
     * @return true if array contains val
     */
    private boolean Contains(int[] array, int val){
        for(int i: array){
            if(i == val)
                return true;
        }
        return false;
    }

    /**
     * Shuffles the array randomly
     *
     * @param array to shuffle
     */
    private void Shuffle(int[] array) {
        int n = array.length;
        // Loop over array.
        for (int i = 0; i < array.length; i++) {
            // Get a random index of the array past the current index.
            int randomValue = i + ThreadLocalRandom.current().nextInt(n - i);
            // Swap the random element with the present element.
            int randomElement = array[randomValue];
            array[randomValue] = array[i];
            array[i] = randomElement;
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
