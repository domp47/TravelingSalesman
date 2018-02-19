package Search.ES;

import City.City;
import Search.Chromosome;
import Search.Mutate;
import Search.RunSearch;

import java.util.Random;
//import java.util.concurrent.ThreadLocalRandom;

public class  ESSearch implements Runnable{

    private int N_ITERATIONS;
    private int N_EPOCHS;

    private RunSearch runSearch;

    private City[] cities;

    private Chromosome bestChromosome;

    private Random rnd;
    public ESSearch(RunSearch runSearch, City[] cities, int nIterations, int nEpochs, int threadIndex)
    {
        this.runSearch = runSearch;

        this.cities = cities;
        this.N_ITERATIONS = nIterations;
        this.N_EPOCHS = nEpochs;

        bestChromosome = null;

        rnd = new Random( System.currentTimeMillis()*(1 + threadIndex));
    }

    public void Search(){

        for (int epochs = 0; epochs < N_EPOCHS; epochs++) {
            int[] ranPath = GenerateRandomPath(cities.length);

            for (int i = 0; i < N_ITERATIONS; i++) {

                float distance = GetDistance(ranPath);

                if(bestChromosome == null){
                    bestChromosome = new Chromosome(GetCityPath(ranPath));

                    if(runSearch.getBestChromosome() == null){
                        runSearch.setBestChromosome(bestChromosome);
                    }
                    else if(bestChromosome.GetFitness() < runSearch.getBestChromosome().GetFitness()){
                        runSearch.setBestChromosome(bestChromosome);
                    }
                }
                else if(distance<bestChromosome.GetFitness()){
                    bestChromosome = new Chromosome(GetCityPath(ranPath));

                    if(runSearch.getBestChromosome() == null){
                        runSearch.setBestChromosome(bestChromosome);
                    }
                    else if(bestChromosome.GetFitness() < runSearch.getBestChromosome().GetFitness()){
                        runSearch.setBestChromosome(bestChromosome);
                    }
                }
                new Mutate().Mutate(ranPath, rnd);
            }
        }

    }

    private City[] GetCityPath(int[] path){
        City[] cityPath = new City[path.length];

        for (int i = 0; i < path.length; i++) {
            cityPath[i] = cities[path[i]];
        }

        return cityPath;
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

        for (int i = 0; i < path.length; i++) {
            distance += cities[path[i]].GetDistance(cities[path[(i+1)%cities.length]]);
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
     * Runs the Search method on start of thread
     */
    @Override
    public void run() {
        Search();
    }

    public Chromosome GetBestChromosome() {
        return bestChromosome;
    }
}
