package Search;

public class EASearch {
    private float[][] adjacencyMatrix;

    public EASearch(float[][] adjacencyMatrix){
        this.adjacencyMatrix = adjacencyMatrix;
    }

    public void Search(){
        int[] ranPath = GenerateRandomPath(adjacencyMatrix.length);

        //Search
    }

    private int[] GenerateRandomPath(int size){
        int[] path = new int[size];

        return path;
    }
}
