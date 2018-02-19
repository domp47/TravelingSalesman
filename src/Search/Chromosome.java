package Search;

import City.City;

public class Chromosome {
    private City[] path;
    private float fitness;

    public Chromosome(City[] path){
        this.path = path;
        fitness = FindFitness();
    }

    private float FindFitness() {
        float distance = 0;

        for (int i = 0; i < path.length; i++) {
            distance += path[i].GetDistance(path[(i+1)%path.length]);
        }

        return distance;
    }

    public City[] getPath() {
        return path;
    }

    public float GetFitness() {
        return fitness;
    }
}
