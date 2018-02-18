package Search.GA;

import City.City;

public class Chromosome {
    private City[] path;
    private float fitness;

    public Chromosome(City[] path){
        this.path = path;
        fitness = GetFitness();
    }

    private float GetFitness() {
        float distance = 0;

        for (int i = 0; i < path.length; i++) {
            distance += path[i].GetDistance(path[(i+1)%path.length]);
        }

        return distance;
    }

    public City[] getPath() {
        return path;
    }

    public float getFitness() {
        return fitness;
    }
}
