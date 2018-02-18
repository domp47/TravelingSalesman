package Search.ES;

import City.City;
import Search.RunSearch;

import java.util.Random;
//import java.util.concurrent.ThreadLocalRandom;

public class  ESSearch implements Runnable{

    private int N_ITERATIONS;
    private int N_EPOCHS;

    private RunSearch runSearch;

    private City[] cities;
    private int[] shortestPath;
    private float shortestDistance = Float.MAX_VALUE;
    private Random rnd;

    public ESSearch(RunSearch runSearch, City[] cities, int nIterations, int nEpochs, int threadIndex)
    {
        this.runSearch = runSearch;

        this.cities = cities;
        this.N_ITERATIONS = nIterations;
        this.N_EPOCHS = nEpochs;
        rnd = new Random( System.currentTimeMillis()*(1 + threadIndex));
    }

    public void Search(){

        for (int epochs = 0; epochs < N_EPOCHS; epochs++) {
            int[] ranPath = GenerateRandomPath(cities.length);

            for (int i = 0; i < N_ITERATIONS; i++) {

                float distance = GetDistance(ranPath);

                if(distance<shortestDistance){
//                    System.out.println("EPOCH #:" + epochs +" - New Shortest Distance: "+distance);
                    shortestPath = ranPath.clone();
                    shortestDistance = distance;

                    if(shortestDistance < runSearch.GetShortestDistance()){
                        runSearch.SetShortest(shortestPath.clone(), shortestDistance);
                    }
                }
                new Mutate().Mutate(ranPath, rnd);
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
       double xDistance = 0;
       double yDistance = 0;

       for (int i = 0; i < path.length; i++) {
           xDistance += Math.abs(cities[path[i]].getX() - cities[path[(i+1)%path.length]].getX());
           yDistance += Math.abs(cities[path[i]].getY() - cities[path[(i+1)%path.length]].getY());
       }

       float distance = 0;

        for (int i = 0; i < path.length; i++) {
            distance += cities[path[i]].GetDistance(cities[path[(i+1)%cities.length]]);
        }

        float trevsDistance = (float) Math.sqrt((xDistance*xDistance) + (yDistance+yDistance));
        float myDistance = distance;

        System.out.println(trevsDistance);
        System.out.println(myDistance);
        System.out.println("----------------------------------");

       return trevsDistance;
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

    /**
     * Runs the Search method on start of thread
     */
    @Override
    public void run() {
        Search();
    }
}
