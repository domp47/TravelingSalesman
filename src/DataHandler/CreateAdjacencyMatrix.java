package DataHandler;

public class CreateAdjacencyMatrix {
//    /**
//     * @param vertices list of vertices and their location on a euclidean plane
//     * @return adjacency matrix for given vertices
//     */
//    public static float[][] CreateAdjacencyMatrix(float[][] vertices){
//        float[][] adjacencyMatrix = new float[vertices.length][vertices.length];
//
//        for(int y = 0; y < adjacencyMatrix.length; y++){
//            for(int x = 0; x < y; x++){
//                float distance = (float) Math.sqrt((vertices[x][0]-vertices[y][0])*(vertices[x][0]-vertices[y][0]) + (vertices[x][1]-vertices[y][1])*(vertices[x][1]-vertices[y][1]));
//
//                adjacencyMatrix[y][x] = distance;
//                adjacencyMatrix[x][y] = distance;
//            }
//            adjacencyMatrix[y][y] = 0;
//        }
//
//        return adjacencyMatrix;
//    }
//
//    public static void PrintMatrix(float[][] adjacencyMatrix){
//        for (int y = 0; y < adjacencyMatrix.length; y++) {
//            for (int x = 0; x < adjacencyMatrix.length; x++) {
//                if(x!=0)
//                    System.out.print(" | ");
//                System.out.print(String.format("%05.2f", adjacencyMatrix[y][x]));
//            }
//            System.out.println();
//
//            for(float f: adjacencyMatrix[y]){
//                System.out.print("--------");
//            }
//
//            System.out.println();
//        }
//    }
}
