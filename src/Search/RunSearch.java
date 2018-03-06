package Search;

import City.City;
import DataHandler.LoadFromFile;
import Exceptions.CityNotLoadedException;
import Search.ES.ESSearch;
import Search.GA.GA;
import TravelingSalesMan.TravelingSalesMan;

import java.io.IOException;

public class RunSearch implements Runnable {

    private City[] cities = null;

    private Chromosome bestChromosome = null;

    private int nThreads, nSearches, nIterations;

    private SearchAlgorithm searchAlgorithm = SearchAlgorithm.ES;
    private GA.CrossoverType crossoverType = GA.CrossoverType.PMX;
    private TravelingSalesMan travelingSalesMan;
    private boolean runningSearch = false;


    public enum SearchAlgorithm{
        ES, GA
    }
    public RunSearch(TravelingSalesMan travelingSalesMan){
        this.travelingSalesMan = travelingSalesMan;
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

        if(!runningSearch) {

            travelingSalesMan.getMainFrame().DisableSearch();

            if (searchAlgorithm == SearchAlgorithm.ES) {
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
            if (searchAlgorithm == SearchAlgorithm.GA) {
                GA[] gaSearches = new GA[nThreads];
                Thread[] gaThreads = new Thread[nThreads];

                for (int i = 0; i < nThreads; i++) {
                    gaSearches[i] = new GA(this, i, cities, crossoverType);
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
            }
            travelingSalesMan.getMainFrame().EnableSearch();
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

    public void setCrossoverType(GA.CrossoverType crossoverType){
        this.crossoverType = crossoverType;
    }

    public synchronized Chromosome getBestChromosome() {
        return bestChromosome;
    }

    public synchronized void setBestChromosome(Chromosome bestChromosome) {
        this.bestChromosome = bestChromosome;
//        System.out.println(bestChromosome.GetFitness());
        travelingSalesMan.getMainFrame().getBest().setText(Float.toString(bestChromosome.GetFitness()));
        travelingSalesMan.getMainFrame().getPathPanel().SetPath(bestChromosome.getPath());
        travelingSalesMan.getMainFrame().repaint();
    }
}
