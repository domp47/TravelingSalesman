package Search;

import java.util.Random;

public class Mutate {

    private static final int N_INTERCHANGES = 3;


    /**
     *
     * Mutates array to interchange 3 of the vertices
     *
     * @param array to mutate
     */
    public static void Mutate(int[] array){
        Random r = new Random();

        int[] ranIndexes = new int[N_INTERCHANGES];

        for(int i = 0; i < N_INTERCHANGES; i++){
            ranIndexes[i] = -1;
        }

        for(int i = 0; i < N_INTERCHANGES;){
            int ranIndex = r.nextInt(array.length);

            if(!Contains(ranIndexes,ranIndex)){
                ranIndexes[i] = ranIndex;
                i++;
            }
        }

        Permutations permutations = new Permutations(ranIndexes);
        int[][] listPermutations = permutations.GetPermutationList();

        int ranPermIndex = r.nextInt(listPermutations.length-1)+1;

        int[] ranPermutation = listPermutations[ranPermIndex];
        int[] origVals = array.clone();


        for(int i = 0; i < ranPermutation.length; i++){
            array[ranPermutation[i]] = origVals[ranIndexes[i]];
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
    private static boolean Contains(int[] array, int val){
        for(int i: array){
            if(i == val)
                return true;
        }
        return false;
    }
}
