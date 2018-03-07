package Search;

import Search.ES.Permutations;
import City.City;
import java.util.Random;

public class Mutate {

    private static final int N_INTERCHANGES = 3;

    public Mutate(){}

    /**
     * Mutates array to interchange N_INTERCHANGES of the vertices
     * @param array to mutate
     * @param r random to use
     */
    public int[] Mutate(int[] array, Random r){

        int[] newArray = array.clone();

        //get unique indexes
        int[] ranIndexes = new int[N_INTERCHANGES];

        for(int i = 0; i < N_INTERCHANGES; i++){
            ranIndexes[i] = -1;
        }

        for(int i = 0; i < N_INTERCHANGES;){
            int ranIndex = r.nextInt(newArray.length);

            if(!Contains(ranIndexes,ranIndex)){
                ranIndexes[i] = ranIndex;
                i++;
            }
        }

        //get all permutations and choose a random one
        Permutations permutations = new Permutations(ranIndexes);
        int[][] listPermutations = permutations.GetPermutationList();

        int ranPermIndex = r.nextInt(listPermutations.length-1)+1;

        int[] ranPermutation = listPermutations[ranPermIndex];
        int[] origVals = newArray.clone();

        //swap the values to mutate
        for(int i = 0; i < ranPermutation.length; i++){
            newArray[ranPermutation[i]] = origVals[ranIndexes[i]];
        }
        return newArray;
    }

    /**
     * Mutates array to interchange N_INTERCHANGES of the vertices
     * @param path to mutate
     * @param r random to use
     */
    public void Mutate(City[] path, Random r){

        //get unique indexes
        int[] ranIndexes = new int[N_INTERCHANGES];

        for(int i = 0; i < N_INTERCHANGES; i++){
            ranIndexes[i] = -1;
        }

        for(int i = 0; i < N_INTERCHANGES;){
            int ranIndex = r.nextInt(path.length);

            if(!Contains(ranIndexes,ranIndex)){
                ranIndexes[i] = ranIndex;
                i++;
            }
        }

        //get all permutations and choose a random one
        Permutations permutations = new Permutations(ranIndexes);
        int[][] listPermutations = permutations.GetPermutationList();

        int ranPermIndex = r.nextInt(listPermutations.length-1)+1;

        int[] ranPermutation = listPermutations[ranPermIndex];
        City[] origVals = path.clone();

        //swap the values to mutate
        for (int i = 0; i < ranPermutation.length; i++) {
            path[ranPermutation[i]] = origVals[ranIndexes[i]];
        }
    }

    /**
     *
     * Searches array for instance of val
     *
     * @param array to search in
     * @param val to search for
     * @return true if array contains val
     */
    private boolean Contains(int[] array, int val){
        for(int i: array){
            if(i == val)
                return true;
        }
        return false;
    }
}
