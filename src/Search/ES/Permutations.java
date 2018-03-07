package Search.ES;

public class Permutations {
    private int permIndex;
    private int[][] permList;


    /**
     * Calculates all the permutations of a given array of numbers
     * @param arr
     */
    public Permutations(int[] arr){
        permIndex = 0;
        permList = new int[Factorial(arr.length)][]; // the number of possible permutations of n is n!

        Permute(arr.clone(), 0); //find permutations
    }

    /**
     * Gets the list of permutations
     * @return list of permutations
     */
    public int[][] GetPermutationList(){
        return permList;
    }

    /**
     * Calculate permutations and add to list
     * @param arr array to permute
     * @param index number of which permutation
     */
    private void Permute(int[] arr, int index){
        if(index >= arr.length - 1){ //If we are at the last element - nothing left to permute
             permList[permIndex] = arr.clone();
            permIndex++;
            return;
        }

        for(int i = index; i < arr.length; i++){ //For each index in the sub array arr[index...end]

            //Swap the elements at indices index and i
            int t = arr[index];
            arr[index] = arr[i];
            arr[i] = t;

            //Recurse on the sub array arr[index+1...end]
            Permute(arr, index+1);

            //Swap the elements back
            t = arr[index];
            arr[index] = arr[i];
            arr[i] = t;
        }
    }

    /**
     * Calculates the Factorial of a number
     * @param num to calcualate
     * @return factorial of input
     */
    private int Factorial(int num){
        int fac = 1;

        for (int i = 1; i <= num; i++) {
            fac = fac * i;
        }
        return fac;
    }
}
