package Search;

public class Permutations {
    private int permIndex;
    private int[][] permList;


    public Permutations(int[] arr){
        permIndex = 0;
        permList = new int[Factorial(arr.length)][];

        Permute(arr.clone(), 0);
    }

    public int[][] GetPermutationList(){
        return permList;
    }

    private void Permute(int[] arr, int index){
        if(index >= arr.length - 1){ //If we are at the last element - nothing left to permute
            //Print the array
//            System.out.print("[");
//            for(int i = 0; i < arr.length - 1; i++){
//                System.out.print(arr[i] + ", ");
//            }
//            if(arr.length > 0)
//                System.out.print(arr[arr.length - 1]);
//            System.out.println("]");

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

    private int Factorial(int num){
        int fac = 1;

        for (int i = 1; i <= num; i++) {
            fac = fac * i;
        }
        return fac;
    }
}
