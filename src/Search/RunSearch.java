package Search;

import City.City;
import DataHandler.LoadFromFile;
import Exceptions.CityNotLoadedException;
import Search.ES.ESSearch;
import Search.GA.GA;
import TravelingSalesMan.TravelingSalesMan;

import java.io.IOException;

/**
 * Controller for running searches
 */
public class RunSearch implements Runnable {

    private City[] cities = null;

    private Chromosome bestChromosome = null;

    private int nThreads, nSearches, nIterations, popSize, maxGen;

    private SearchAlgorithm searchAlgorithm = SearchAlgorithm.ES;
    private GA.CrossoverType crossoverType = GA.CrossoverType.PMX;
    private TravelingSalesMan travelingSalesMan;

    /**
     * Evolutionary Strategy, Genetic Algorithm
     */
    public enum SearchAlgorithm{
        ES, GA
    }

    /**
     * Controller for running searches
     * @param travelingSalesMan controller
     */
    public RunSearch(TravelingSalesMan travelingSalesMan){
        this.travelingSalesMan = travelingSalesMan;
    }

    /**
     * Loads the list of cities from text file
     * @param filePath file to load
     * @param nThreads number of threads to use
     * @param nSearches number of searches to run
     * @param nIterations number of iterations to run
     * @param popSize population size to use
     * @param maxGen max number of generations to create
     * @throws IOException loading file error
     */
    public void LoadCities(String filePath, int nThreads, int nSearches, int nIterations, int popSize, int maxGen) throws IOException {

        cities = LoadFromFile.LoadCities(filePath);

        this.nThreads = nThreads;
        this.nSearches = nSearches;
        this.nIterations = nIterations;
        this.popSize = popSize;
        this.maxGen = maxGen;
    }

    /**
     * Search with the given settings
     * @throws CityNotLoadedException cities not loaded
     */
    private void Search() throws CityNotLoadedException {

        //if the cities weren't loaded we can search
        if(cities==null)
            throw new CityNotLoadedException();

        //disable search button so you cant run multiple searches
        travelingSalesMan.getMainFrame().DisableSearch();

        //run ES if that was chosen
        if (searchAlgorithm == SearchAlgorithm.ES) {
            ESSearch[] esSearches = new ESSearch[nThreads];
            Thread[] esThreads = new Thread[nThreads];

            //create n thread instances of ES Search
            for (int i = 0; i < nThreads; i++) {
                esSearches[i] = new ESSearch(this, cities, nIterations, nSearches, i);
                esThreads[i] = new Thread(esSearches[i]);
                esThreads[i].start();
            }

            //wait for them all to finish
            for (int i = 0; i < nThreads; i++) {
                try {
                    esThreads[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        //run GA if that was chosen
        if (searchAlgorithm == SearchAlgorithm.GA) {
            GA[] gaSearches = new GA[nThreads];
            Thread[] gaThreads = new Thread[nThreads];

            //create n thread instances of GA Search
            for (int i = 0; i < nThreads; i++) {
                gaSearches[i] = new GA(this, i, cities, crossoverType);
                gaThreads[i] = new Thread(gaSearches[i]);
                gaThreads[i].start();
            }

            //wait for them all to finish
            for (int i = 0; i < nThreads; i++) {
                try {
                    gaThreads[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        //re enable the search button so you can search again now that this one is done
        travelingSalesMan.getMainFrame().EnableSearch();

    }

    /**
     * Start Searching
     */
    @Override
    public void run() {
        try {
            Search();
        } catch (CityNotLoadedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the Search Algorithm, GA or ES
     * @param searchAlgorithm to use
     */
    public void SetSearchAlgorithm(SearchAlgorithm searchAlgorithm) {
        this.searchAlgorithm = searchAlgorithm;
    }

    /**
     * Sets the Crossover Type, UOX or PMX
     * @param crossoverType to use
     */
    public void setCrossoverType(GA.CrossoverType crossoverType){
        this.crossoverType = crossoverType;
    }

    /**
     * Gets the global shortest path
     * @return shortest path
     */
    public synchronized Chromosome getBestChromosome() {
        return bestChromosome;
    }

    /**
     * Sets the shortest path and draws it on canvas
     * @param bestChromosome shortest path
     */
    public synchronized void setBestChromosome(Chromosome bestChromosome) {
        this.bestChromosome = bestChromosome;
        travelingSalesMan.getMainFrame().getBest().setText(Float.toString(bestChromosome.GetFitness()));
        travelingSalesMan.getMainFrame().getPathPanel().SetPath(bestChromosome.getPath());
        travelingSalesMan.getMainFrame().repaint();
    }
}
