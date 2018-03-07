package Search;

import City.City;

public class Chromosome {
    private City[] path;
    private float fitness;

    /**
     * Create Chromosome from given path
     * @param path
     */
    public Chromosome(City[] path){
        this.path = path;
        fitness = FindFitness();
    }

    /**
     * Calculates the total path distance
     * @return distance for total path
     */
    private float FindFitness() {
        float distance = 0;

        for (int i = 0; i < path.length; i++) {
            distance += path[i].GetDistance(path[(i+1)%path.length]); //add all the distances between cities
        }

        return distance;
    }

    /**
     * Gets Path for Chromosome
     * @return path
     */
    public City[] getPath() {
        return path;
    }

    /**
     * Get Distance of Path
     * @return fitness
     */
    public float GetFitness() {
        return fitness;
    }
}
