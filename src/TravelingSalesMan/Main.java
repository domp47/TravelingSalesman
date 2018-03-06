package TravelingSalesMan;

public class Main {
    public static void main(String[] args){

        new TravelingSalesMan();

//        Scanner scanner =  new Scanner(System.in);
//        System.out.print("Enter 'e' to type file Path, or enter anything else to open file chooser: ");
//        if(scanner.next().toLowerCase().equals("e")){
//            System.out.print("Enter File Path: ");
//            try {
//                vertices = LoadFromFile.LoadVertices(scanner.next());
//            } catch (IOException e) {
//                e.printStackTrace();
//                exit(1);
//            }
//        }else{
//            try {
//                //"If on windows might need to Alt-Tab to see swing window, It sometimes doesn't pops up for me on windows."
//                vertices = LoadFromFile.LoadVertices();
//            } catch (IOException e) {
//                exit(1);
//            }
//        }
//
//        if(vertices == null){
//            System.out.println("Error: loading vertices from file");
//            exit(2);
//        }

//        float[][] adjacencyMatrix = CreateAdjacencyMatrix.CreateAdjacencyMatrix(vertices);

//        CreateAdjacencyMatrix.PrintMatrix(adjacencyMatrix);

//        ESSearch search = new ESSearch(adjacencyMatrix);
//        search.Search();

//        System.out.println("Shortest Distance is: "+search.getShortestDistance());
    }
}
