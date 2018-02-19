package Search;

import City.City;
import DataHandler.LoadFromFile;
import Exceptions.CityNotLoadedException;
import Search.ES.ESSearch;
import Search.GA.GA;

import java.io.IOException;

public class RunSearch implements Runnable {

    private City[] cities = null;

    private Chromosome bestChromosome = null;

    private int nThreads, nSearches, nIterations;

    private SearchAlgorithm searchAlgorithm = SearchAlgorithm.ES;



    public enum SearchAlgorithm{
        ES, GA
    }
    public RunSearch(){

    }

    public void LoadMatrix(String filePath, int nThreads, int nSearches, int nIterations) throws IOException {

        cities = LoadFromFile.LoadCities(filePath);

        this.nThreads = nThreads;
        this.nSearches = nSearches;
        this.nIterations = nIterations;
    }

    private void Search() throws CityNotLoadedException {

        if(cities==null)
            throw new CityNotLoadedException();

        if(searchAlgorithm == SearchAlgorithm.ES){
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
        if(searchAlgorithm == SearchAlgorithm.GA){
            GA[] gaSearches = new GA[nThreads];
            Thread[] gaThreads = new Thread[nThreads];

            for (int i = 0; i < nThreads; i++) {
                gaSearches[i] = new GA(this, i,cities);
                gaThreads[i] = new Thread(gaSearches[i]);
                gaThreads[i].start();
            }

            for (int i = 0; i < nThreads; i++) {
                try {
                    gaThreads[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(bestChromosome.getPath().length);
        }

    }

    @Override
    public void run() {
        try {
            Search();
        } catch (CityNotLoadedException e) {
            e.printStackTrace();
        }
    }

    public void SetSearchAlgorithm(SearchAlgorithm searchAlgorithm) {
        this.searchAlgorithm = searchAlgorithm;
    }

    public synchronized Chromosome getBestChromosome() {
        return bestChromosome;
    }

    public synchronized void setBestChromosome(Chromosome bestChromosome) {
        this.bestChromosome = bestChromosome;
    }
}
