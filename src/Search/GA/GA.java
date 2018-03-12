package Search.GA;

import City.City;
import Search.Chromosome;
import Search.Mutate;
import Search.RunSearch;

import java.util.*;

public class GA implements Runnable{

    //Settings for the GA
    private RunSearch runSearch;

    private double crossoverRate, mutationRate;

    private int populationSize, maxGen, nFittest, nRandomSelection;
    private Random random;
    private CrossoverType crossoverType;
    private City[] listOfCities;
    private LinkedList<Chromosome> previousGenerationsFittest;
    private Chromosome fittestChromosome;
    private double convergenceRate;
    private int failureToEvolveCounter = 0;

    /**
     * Universal Order Crossover, Partial Mapped Crossover
     */
    public enum CrossoverType{
        UOX, PMX
    }

    /**
     * Run the GA for 30 different initial populations
     */
    @Override
    public void run() {
        for (int i = 0; i < 30; i++) {
            Vector<Chromosome> initPopulation = GeneratePopulation(listOfCities);
            Evolve(initPopulation, 1);
        }
    }

    /**
     * Run GA with default Settings
     * @param runSearch search controller
     * @param threadIndex the thread index
     * @param cities list of original cities
     * @param crossoverType crossover type to run
     */
    public GA(RunSearch runSearch, long threadIndex, City[] cities, CrossoverType crossoverType){
        this(runSearch,threadIndex, cities, crossoverType, 70, 750);
    }

    /**
     * Run GA with Semi manual settings
     * @param runSearch search controller
     * @param threadIndex the thread index
     * @param cities list of original cities
     * @param crossoverType crossover type to run
     * @param populationSize population size to run
     * @param maxGen max generations to run
     */
    public GA(RunSearch runSearch, long threadIndex, City[] cities, CrossoverType crossoverType, int populationSize, int maxGen){
        this(runSearch, threadIndex, cities, crossoverType, populationSize, maxGen,1,0.1,3,3);
    }

    /**
     * Manual control of settings
     *
     * @param runSearch search controller
     * @param threadIndex the thread index
     * @param cities list of original cities
     * @param crossoverType crossover type to run
     * @param populationSize population size to run
     * @param maxGen max generations to run
     * @param crossoverRate crossover rate
     * @param mutationRate mutation rate
     * @param nFittest number of fittest to choose to evolve
     * @param nRandomSelection number of randoms to select
     */
    public GA(RunSearch runSearch, long threadIndex, City[] cities, CrossoverType crossoverType, int populationSize, int maxGen, double crossoverRate, double mutationRate, int nFittest, int nRandomSelection){

        this.runSearch = runSearch;
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
        this.populationSize = populationSize;
        this.maxGen = maxGen;
        this.nFittest = nFittest;
        this.nRandomSelection = nRandomSelection;
        random = new Random(System.currentTimeMillis() * (1 + threadIndex));
        this.crossoverType = crossoverType;
        this.convergenceRate = convergenceRate;
        this.fittestChromosome = null;
        this.previousGenerationsFittest = new LinkedList<>();
        this.listOfCities = cities;
    }

    /**
     * Generate a random population to start with
     * @param path list of cities to generate from
     * @return list of random solutions
     */
    private Vector<Chromosome> GeneratePopulation(City[] path){
        Vector<Chromosome> population = new Vector<>();

        //shuffle a clone of the cities and add it to the list
        for (int i = 0; i < populationSize; i++) {
            population.addElement(Shuffle(path.clone()));
        }

        return population;
    }

    /**
     * Shuffles the array randomly
     *
     * @param ar to shuffle
     */
    private Chromosome Shuffle(City[] ar) {
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = random.nextInt(i + 1);
            // Simple swap
            City a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
        return new Chromosome(ar);
    }

    /**
     * Evolve the generation
     * @param population population to evolve
     * @param nthGeneration number of this generation
     */
    private void Evolve(Vector<Chromosome> population, int nthGeneration){
        Vector<Chromosome> nextGeneration = new Vector<>();

        //get the set number of fittest from last gen and add them to next gen
        Chromosome[] fittest = GetFittest(population);

        for(Chromosome fit : fittest){
            nextGeneration.addElement(fit);
        }

        SetFittestChromosome(fittest);

        //fill the rest of the population with evolved children
        while(nextGeneration.size() < populationSize) {

            //choose 2 randomly to be the parents
            Chromosome parent1 = ChooseRandom(population);
            Chromosome parent2 = ChooseRandom(population);

            Chromosome child1 = null;
            Chromosome child2 = null;

            //randomly crossover based on crossover rate
            if (random.nextDouble() < crossoverRate) {
                if (crossoverType == CrossoverType.PMX) {
                    Chromosome[] children = PerformPMX(parent1, parent2);
                    child1 = children[0];
                    child2 = children[1];
                }
                if (crossoverType == CrossoverType.UOX) {
                    Chromosome[] children = PerformUOX(parent1, parent2);
                    child1 = children[0];
                    child2 = children[1];
                }
            } else {
                child1 = new Chromosome(parent1.getPath().clone());
                child2 = new Chromosome(parent2.getPath().clone());
            }

            //randomly mutate based on mutation rate
            if (random.nextDouble() < mutationRate) {
                child1 = Mutate(child1);
            }
            if (random.nextDouble() < mutationRate) {
                child2 = Mutate(child2);
            }


            nextGeneration.addElement(child1);

            if(nextGeneration.size() < populationSize)
                nextGeneration.addElement(child2);
        }


        if(nthGeneration < maxGen) {
            Evolve(nextGeneration, nthGeneration + 1);
        }else{
            SetFittestChromosome(fittest);
        }
    }

    /**
     * Checks and sets the shortest path if necessary
     * @param fittest chromosome to try and insert
     */
    private void SetFittestChromosome(Chromosome[] fittest) {
        //if local fittest is null set it
        if(fittestChromosome==null){
            fittestChromosome = fittest[0];

            //set the global shortest path if this is the shortest global path
            runSearch.CheckSetBestChromosome(fittestChromosome);
        }
        //check if this path is the shortest path we've seen in this thread
        else if(fittest[0].GetFitness() < fittestChromosome.GetFitness()){
            fittestChromosome = fittest[0];

            //set the global shortest path if this is the shortest global path
            runSearch.CheckSetBestChromosome(fittestChromosome);
        }
    }

    /**
     * Mutates the child
     * @param child to mutate
     * @return mutated child
     */
    private Chromosome Mutate(Chromosome child) {

        City[] path = child.getPath();

        new Mutate().Mutate(path,random);

        return new Chromosome(path);
    }

    /**
     * Performs Universal Ordered Crossover on the 2 parents
     * @param parent1 first parent
     * @param parent2 second parent
     * @return array containing child1 and child2
     */
    @SuppressWarnings("Duplicates")
    private Chromosome[] PerformUOX(Chromosome parent1, Chromosome parent2) {
        Chromosome[] children = new Chromosome[2];

        City[] child1Path = new City[parent1.getPath().length];
        City[] child2Path = new City[parent1.getPath().length];
        HashSet<City> child1Contents = new HashSet<>();
        HashSet<City> child2Contents = new HashSet<>();

        //randomly chose whether to keep the city in the child or not
        for (int i = 0; i < parent1.getPath().length; i++) {
            if(random.nextBoolean()){
                child2Path[i] = parent2.getPath()[i];
                child2Contents.add(parent2.getPath()[i]);

                child1Path[i] = parent1.getPath()[i];
                child1Contents.add(parent1.getPath()[i]);
            }
        }

        //for all the ones not moved over from child select it from the other parent
        for(int i = 0; i<parent1.getPath().length; i++){
            if(child1Path[i]== null){
                for(int j=0; j<parent1.getPath().length; j++){
                    City attempt = parent2.getPath()[j];
                    if(!child1Contents.contains(attempt)){
                        child1Path[i]= attempt;
                        child1Contents.add(attempt);
                        break;
                    }
                }
            }

            if(child2Path[i]== null){
                for(int j=0; j<parent2.getPath().length; j++){
                    City attempt = parent1.getPath()[j];
                    if(!child2Contents.contains(attempt)){
                        child2Path[i]= attempt;
                        child2Contents.add(attempt);
                        break;
                    }
                }
            }
        }

        children[0] = new Chromosome(child1Path);
        children[1] = new Chromosome(child2Path);
        return children;
    }

    /**
     * Performs Partial Mapped Crossover on the 2 parents
     * @param parent1 first parent
     * @param parent2 second parent
     * @return array containing child1 and child2
     */
    @SuppressWarnings("Duplicates")
    private Chromosome[] PerformPMX(Chromosome parent1, Chromosome parent2) {
        Chromosome[] children = new Chromosome[2];

        City[] child1 = new City[parent1.getPath().length];
        City[] child2 = new City[parent1.getPath().length];

        HashSet<City> child1Contents = new HashSet<>();
        HashSet<City> child2Contents = new HashSet<>();

        int swathLength = random.nextInt(parent1.getPath().length);
        int swathIndex = random.nextInt(parent1.getPath().length - swathLength);

        //copy swaths over
        for (int i = swathIndex; i <= swathIndex + swathLength; i++) {
            child1[i] = parent1.getPath()[i];
            child1Contents.add(child1[i]);

            child2[i] = parent2.getPath()[i];
            child2Contents.add(child2[i]);
        }

        //fill child1 from parent 2
        for (int i = swathIndex; i <= swathIndex + swathLength; i++) {
            if(!child1Contents.contains(parent2.getPath()[i])){
                //find the index to insert for given value
                int indexToInsert = FindIndexForInsertion(parent1.getPath(), parent2.getPath(), swathIndex, swathIndex + swathLength, i);

                if(indexToInsert != -1){
                    child1[indexToInsert] = parent2.getPath()[i];
                    child1Contents.add(parent2.getPath()[i]);
                }
            }
        }
        for (int i = 0; i < parent2.getPath().length; i++) {
            if(child1[i] == null){
                child1[i] = parent2.getPath()[i];
            }
        }

        //fill child2 from parent 1
        for (int i = swathIndex; i <= swathIndex + swathLength; i++) {
            if(!child2Contents.contains(parent1.getPath()[i])){
                //find the index to insert for given value
                int indexToInsert = FindIndexForInsertion(parent2.getPath(), parent1.getPath(), swathIndex, swathIndex + swathLength, i);

                if(indexToInsert != -1){
                    child2[indexToInsert] = parent1.getPath()[i];
                    child2Contents.add(parent1.getPath()[i]);
                }
            }
        }
        for (int i = 0; i < parent1.getPath().length; i++) {
            if(child2[i] == null){
                child2[i] = parent1.getPath()[i];
            }
        }

        children[0] = new Chromosome(child1);
        children[1] = new Chromosome(child2);

        return children;


    }

    /**
     * Find the index to insert into using PMX
     *
     * @param swathSource source parent
     * @param otherParent other parent
     * @param swathStartingIndex starting index of swath
     * @param swathEndingIndex end index of swath
     * @param index index to search
     * @return index to insert into
     */
    private int FindIndexForInsertion(City[] swathSource, City[] otherParent, int swathStartingIndex, int swathEndingIndex, int index) {

        City Val = swathSource[index];

        //search otherParent for the value at the index in the swathSource
        for (int i = 0; i < otherParent.length; i++) {
            if (Val == otherParent[i]) {
                //if this value is in swatch continue till its not then return that
                if (i >= swathStartingIndex && i <= swathEndingIndex) {
                    return FindIndexForInsertion(swathSource, otherParent, swathStartingIndex, swathEndingIndex, i);
                } else {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Gets the n fittest chromosomes
     * @param population to search in
     * @return array of fittest chromosomes
     */
    private Chromosome[] GetFittest(Vector<Chromosome> population) {
        Chromosome[] fittest = new Chromosome[nFittest];

        //insert the chromosomes in order but limit it to only n long so we get only the n fittest
        for (int i = 0; i < population.size(); i++) {
            Chromosome chromosome = population.elementAt(i);

            for (int j = 0; j < fittest.length; j++) {
                if(fittest[j]==null){
                    fittest[j] = chromosome;
                    break;
                }
                else{
                    if(chromosome.GetFitness() < fittest[j].GetFitness()){
                        Chromosome temp = fittest[j];
                        fittest[j] = chromosome;
                        chromosome = temp;
                    }
                }
            }
        }
        return fittest;
    }

    /**
     * Chooses the best chromosome from n random chromosomes from the population
     * @param population to search in
     * @return Random Chromosome
     */
    private Chromosome ChooseRandom(Vector<Chromosome> population) {
        Chromosome bestChoice = null;

        //loop through all of n random selections and chose the best of the randoms
        for (int i = 0; i < nRandomSelection; i++) {

            int randomIndex = random.nextInt(population.size());

            Chromosome chromosome = population.elementAt(randomIndex);

            if(bestChoice == null){
                bestChoice = chromosome;
            }
            else if(chromosome.GetFitness() < bestChoice.GetFitness()){
                bestChoice = chromosome;
            }

        }
        return bestChoice;
    }

}
