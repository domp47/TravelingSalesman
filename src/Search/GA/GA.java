package Search.GA;

import City.City;
import Search.Chromosome;
import Search.Mutate;
import Search.RunSearch;

import java.util.*;

public class GA implements Runnable{

    private RunSearch runSearch;

    private double crossoverRate, mutationRate;

    private int populationSize, maxGen, nFittest, nRandomSelection;
    private Random random;
    private CrossoverType crossoverType;

    private Vector<Chromosome> initPopulation;

    private Chromosome fittestChromosome;

    @Override
    public void run() {
        Evolve(initPopulation, 1);
    }

    public enum CrossoverType{
        UOX, PMX
    }

    public GA(RunSearch runSearch, long threadIndex, City[] cities){
        this(runSearch,70,750, threadIndex, cities);
    }

    /**
     * Default constructor
     *
     * @param threadIndex
     * @param cities
     */
    public GA(RunSearch runSearch, int populationSize, int maxGen, long threadIndex, City[] cities){
        this(runSearch,1, 0.1, populationSize, maxGen, 3, 3, threadIndex, cities, CrossoverType.UOX);
    }

    /**
     * Manual Control of GA
     *
     * @param populationSize
     * @param maxGen
     * @param nFittest
     * @param nRandomSelection
     * @param threadIndex
     * @param cities
     * @param crossoverType
     */
    public GA(RunSearch runSearch, double crossoverRate, double mutationRate, int populationSize, int maxGen, int nFittest, int nRandomSelection, long threadIndex, City[] cities, CrossoverType crossoverType){

        this.runSearch = runSearch;

        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
        this.populationSize = populationSize;
        this.maxGen = maxGen;
        this.nFittest = nFittest;
        this.nRandomSelection = nRandomSelection;
        random = new Random(System.currentTimeMillis() * (1 + threadIndex));
//        random = new Random(1234567890);
        this.crossoverType = crossoverType;

        this.fittestChromosome = null;

        this.initPopulation = GeneratePopulation(cities);
    }

    private Vector<Chromosome> GeneratePopulation(City[] path){
        Vector<Chromosome> population = new Vector<>();

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

    private void Evolve(Vector<Chromosome> population, int nthGeneration){
        Vector<Chromosome> nextGeneration = new Vector<>();

        Chromosome[] fittest = GetFittest(population);

        int gkfd = 0;

        for(Chromosome fit : fittest){
            nextGeneration.addElement(fit);

            if(duplicates(fit.getPath())){
                System.out.println("Fittest at " + gkfd + "duplicates");
            }
            gkfd++;
        }

        SetFittestChromosome(fittest);

        while(nextGeneration.size() < populationSize) {

            Chromosome parent1 = ChooseRandom(population);
            boolean b = duplicates(parent1.getPath());
            Chromosome parent2 = ChooseRandom(population);
            boolean b1 = duplicates(parent2.getPath());

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
            boolean b2 = duplicates(child1.getPath());
            boolean b3 = duplicates(child2.getPath());

            //randomly mutate based on mutation rate
            if (random.nextDouble() < mutationRate) {
                child1 = Mutate(child1);
            }
            if (random.nextDouble() < mutationRate) {
                child2 = Mutate(child2);
            }

            boolean b4 = duplicates(child1.getPath());
            boolean b5 = duplicates(child2.getPath());

            nextGeneration.addElement(child1);

            if(nextGeneration.size() < populationSize)
                nextGeneration.addElement(child2);
        }

        for(int i = 0; i < nextGeneration.size(); i++){
            if(duplicates(nextGeneration.elementAt(i).getPath())){
                System.out.println("Duplicate at Index "+i);
            }
        }

        //TODO check to find if converged
        if(nthGeneration < maxGen)
            Evolve(nextGeneration, nthGeneration+1);
        else{
            SetFittestChromosome(fittest);
        }
    }

    private void SetFittestChromosome(Chromosome[] fittest) {
        if(fittestChromosome==null){
            fittestChromosome = fittest[0];

            if(runSearch.getBestChromosome() == null){
                runSearch.setBestChromosome(fittestChromosome);
            }
            else if(fittestChromosome.GetFitness() < runSearch.getBestChromosome().GetFitness()){
                runSearch.setBestChromosome(fittestChromosome);
            }
        }else if(fittest[0].GetFitness() < fittestChromosome.GetFitness()){
            fittestChromosome = fittest[0];

            if(runSearch.getBestChromosome() == null){
                runSearch.setBestChromosome(fittestChromosome);
            }
            else if(fittestChromosome.GetFitness() < runSearch.getBestChromosome().GetFitness()){
                runSearch.setBestChromosome(fittestChromosome);
            }
        }
    }

    private Chromosome Mutate(Chromosome child) {

        City[] path = child.getPath();

        new Mutate().Mutate(path,random);

        return new Chromosome(path);

//        City[] path = child.getPath();
//        int index1 = random.nextInt(path.length);
//        int index2 = random.nextInt(path.length);
//        City temp = path[index1];
//        path[index1] = path[index2];
//        path[index2] = temp;
//        return new Chromosome(path);

    }

    private Chromosome[] PerformUOX(Chromosome parent1, Chromosome parent2) {
        Chromosome[] children = new Chromosome[2];

        City[] child1Path = new City[parent1.getPath().length];
        City[] child2Path = new City[parent1.getPath().length];

        for (int i = 0; i < parent1.getPath().length; i++) {
            if(random.nextDouble() < 0.5){
                child2Path[i] = parent1.getPath()[i];
                child1Path[i] = parent2.getPath()[i];
            }else{
                child1Path[i] = parent1.getPath()[i];
                child2Path[i] = parent2.getPath()[i];
            }
        }
        children[0] = new Chromosome(child1Path);
        children[1] = new Chromosome(child2Path);
        return children;
    }






    @SuppressWarnings("Duplicates")
    private Chromosome[] PerformPMX(Chromosome parent1, Chromosome parent2) {
        Chromosome[] children = new Chromosome[2];

        City[] child1 = new City[parent1.getPath().length];
        City[] child2 = new City[parent1.getPath().length];

        HashSet<City> child1Contents = new HashSet<>();
        HashSet<City> child2Contents = new HashSet<>();

        int swathLength = random.nextInt(parent1.getPath().length);
        int swathIndex = random.nextInt(parent1.getPath().length - swathLength);

        for (int i = swathIndex; i <= swathIndex + swathLength; i++) {
            child1[i] = parent1.getPath()[i];
            child1Contents.add(child1[i]);

            child2[i] = parent2.getPath()[i];
            child2Contents.add(child2[i]);
        }

        //fill child1 from parent 2
        for (int i = swathIndex; i <= swathIndex + swathLength; i++) {
            if(!child1Contents.contains(parent2.getPath()[i])){
                int indexToInsert = FindIndexForInsertion(parent1.getPath(), parent2.getPath(), swathIndex, swathIndex + swathLength, i, child1);

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
                int indexToInsert = FindIndexForInsertion(parent2.getPath(), parent1.getPath(), swathIndex, swathIndex + swathLength, i, child2);

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

    private int FindIndexForInsertion(City[] swathsource, City[] otherParent,int swathStartingIndex, int swathEndingIndex, int index, City[] child) {

        City Val = swathsource[index];

        //search otherParent for the value at the index in the swathSource
        for (int i = 0; i < otherParent.length; i++) {
            if (Val == otherParent[i]) {
                if (i >= swathStartingIndex && i <= swathEndingIndex) {
                    return FindIndexForInsertion(swathsource, otherParent, swathStartingIndex, swathEndingIndex, i, child);
                } else {
                    return i;
                }
            }
        }
        return -1;
    }



    boolean duplicates(final City[] cities){
        Set<City> lump = new HashSet<>();
        for (int i = 0; i < cities.length; i++)
        {
            if (lump.contains(cities[i])){
                System.out.println(i);
                return true;
            }
            lump.add(cities[i]);
        }
        return false;
    }

    /*private Chromosome[] PerformPMX(Chromosome parent1, Chromosome parent2) {
        Chromosome[] children = new Chromosome[2];

        City[] child1 = new City[parent1.getPath().length];
        City[] child2 = new City[parent1.getPath().length];

        HashSet<City> child1Contents = new HashSet<>();
        HashSet<City> child2Contents = new HashSet<>();

        int swathLength = random.nextInt(parent1.getPath().length);
        int swathIndex = random.nextInt(parent1.getPath().length - swathLength);

        for (int i = swathIndex; i < swathIndex + swathLength; i++) {
            child1[i] = parent1.getPath()[i];
            child1Contents.add(child1[i]);

            child2[i] = parent2.getPath()[i];
            child2Contents.add(child2[i]);
        }

        //fill child1 from parent 2
        for (int i = swathIndex; i < swathIndex + swathLength; i++) {
            Function(parent1, parent2, child1, child1Contents, swathLength, swathIndex, i);
            Function(parent2, parent1, child2, child2Contents, swathLength, swathIndex, i);
        }
        for (int i = 0; i < parent2.getPath().length; i++) {
            if(child1[i] == null){
                child1[i] = parent2.getPath()[i];
            }
            if(child2[i] == null){
                child2[i] = parent1.getPath()[i];
            }
        }

        children[0] = new Chromosome(child1);
        children[1] = new Chromosome(child2);

        return children;
    }

    private void Function(Chromosome parent1, Chromosome parent2, City[] child1, HashSet<City> child1Contents, int swathLength, int swathIndex, int i) {
        if(!child1Contents.contains(parent2.getPath()[i])){
            int indexToInsert = FindIndexForInsertion(parent1.getPath(), parent2.getPath(), swathIndex, swathIndex + swathLength, i);

            if(indexToInsert != -1){
                child1[indexToInsert] = parent2.getPath()[i];
                child1Contents.add(parent2.getPath()[i]);
            }
        }
    }*/

    private Chromosome[] GetFittest(Vector<Chromosome> population) {
        Chromosome[] fittest = new Chromosome[nFittest];

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

    private Chromosome ChooseRandom(Vector<Chromosome> population) {
        Chromosome bestChoice = null;

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

    public Chromosome GetFittestChromosome() {
        return fittestChromosome;
    }

}
