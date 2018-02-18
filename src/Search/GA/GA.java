package Search.GA;

import City.City;

import java.util.Random;

public class GA {
    private double crpsspverRate, mutationRate;

    private int populationSize, maxGen, nCities, nElites, nFittest;
    private Random random;
    private City[] cities;
    private CrossoverType xoverType;

    private enum CrossoverType{
        UOX, PMX
    }

    public GA(){
        
    }
}
