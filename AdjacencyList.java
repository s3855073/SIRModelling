import java.io.PrintWriter;
// import java.util.*;

/**
 * Adjacency list implementation for the AssociationGraph interface.
 *
 * Your task is to complete the implementation of this class.  You may add methods, but ensure your modified class compiles and runs.
 *
 * @author Jeffrey Chan, 2019.
 */
public class AdjacencyList extends AbstractGraph {
    
    private SimpleLinkedList[] adjacencyList;

    /**
	 * Contructs empty graph.
	 */
    public AdjacencyList() {
        adjacencyList = null;
    } // end of AdjacencyList()

    public void addVertex(String vertLabel) {
        SimpleLinkedList newVertexList = new SimpleLinkedList();
        newVertexList.add(vertLabel);

        // Initialise new adjacency list
        if (adjacencyList == null) {
            adjacencyList = new SimpleLinkedList[1];

            adjacencyList[0] = newVertexList;
        } else {
            // Create new temp array
            SimpleLinkedList[] tempAdjacencyList = new SimpleLinkedList[adjacencyList.length + 1];

            // Copy original nodes into new temp array
            System.arraycopy(adjacencyList, 0, tempAdjacencyList, 0, adjacencyList.length);

            // Set last element as new vetex
            tempAdjacencyList[adjacencyList.length] = newVertexList;

            // Set original list as temp
            adjacencyList = tempAdjacencyList;
        }
    } // end of addVertex()

    public void addEdge(String srcLabel, String tarLabel) {
    
        for (int row = 0; row < adjacencyList.length; row++) {
            // Add edge from src to tar and from tar to src
            if (adjacencyList[row].get((adjacencyList[row].getLength() - 1)).getValue().equals(srcLabel))
                adjacencyList[row].add(tarLabel);
            if (adjacencyList[row].get((adjacencyList[row].getLength() - 1)).getValue().equals(tarLabel))
                adjacencyList[row].add(srcLabel);
        }
    } // end of addEdge()


    public void toggleVertexState(String vertLabel) {
        Node vertNode = null;

        for (int row = 0; row < adjacencyList.length; row++) {
            if (adjacencyList[row].get(adjacencyList[row].getLength() - 1).getValue().equals(vertLabel)) {
                vertNode = adjacencyList[row].get(adjacencyList[row].getLength() - 1);
                
                // Get new state of vertex
                SIRState newState = (vertNode.getState() == SIRState.S) ? SIRState.I : SIRState.R;
                vertNode.setState(newState);
            }
        }
    } // end of toggleVertexState()


    public void deleteEdge(String srcLabel, String tarLabel) {
        for (int row = 0; row < adjacencyList.length; row++) {
            
            // Delete edge from both src vertex and tar vertex
            if (adjacencyList[row].get(adjacencyList[row].getLength() - 1).getValue().equals(srcLabel) 
                    || adjacencyList[row].get(adjacencyList[row].getLength() - 1).getValue().equals(tarLabel)) {

                for (int edge = 0; edge < adjacencyList[row].getLength() - 1; edge++) {

                    // Remove edge
                    if (adjacencyList[row].get(edge).getValue().equals(tarLabel) 
                            || adjacencyList[row].get(edge).getValue().equals(srcLabel))
                        adjacencyList[row].removeByIndex(edge);
                }
            }
        }
    } // end of deleteEdge()


    public void deleteVertex(String vertLabel) {
        if (adjacencyList != null && adjacencyList.length > 0) {
            // Create a new temp list to be update
            SimpleLinkedList[] tempAdjacencyList = new SimpleLinkedList[adjacencyList.length - 1];

            int tempRowCount = 0;
    
            // Read in original list into temp list
            // Ignore the deleted vertex list
            for (int row = 0; row < adjacencyList.length; row++) {
                if (!adjacencyList[row].get(adjacencyList[row].getLength() - 1).getValue().equals(vertLabel)) {
                    tempAdjacencyList[tempRowCount] = adjacencyList[row];
                    tempAdjacencyList[tempRowCount].remove(vertLabel);
    
                    tempRowCount++;
                }
            }

            // Set original list as temp list
            adjacencyList = tempAdjacencyList;
        } else {
            adjacencyList = null;
        }
    } // end of deleteVertex()


    public String[] kHopNeighbours(int k, String vertLabel) {
        // Implement BFS algorithm
        SimpleLinkedList nodesList = new SimpleLinkedList();
        SimpleLinkedList visitedVertexes = new SimpleLinkedList();

        nodesList.add(vertLabel);

        int times = k;
        int nodesListSize = nodesList.getLength();

        while (times > 0) {
            for (int i = 0; i < nodesListSize; i++) {
                Node currNode = nodesList.get(i);

                // Check if node hasn't been visited already
                if (visitedVertexes.search(currNode.getValue()) == -1) {
                    for (int row = 0; row < adjacencyList.length; row++) {
                        if (adjacencyList[row].get(adjacencyList[row].getLength() - 1).getValue().equals(currNode.getValue())) {

                            for (int col = 0; col < adjacencyList[row].getLength() - 1; col++) {
                                // Get neighbouring node
                                String newAdjacentVertex = adjacencyList[row].get(col).getValue();

                                // Add adjacent node to nodes list of neighbours
                                if (nodesList.search(newAdjacentVertex) == -1) {
                                    nodesList.add(newAdjacentVertex);
                                }
                            }
                        }
                    }

                    // Add node to visited vertexes
                    visitedVertexes.add(currNode.getValue());
                }
            }

            // Avoid nullptr error
            nodesListSize = nodesList.getLength();
            times--;
        }

        String[] vertNeighbours = new String[nodesList.getLength() - 1];

        if (k > 0) {
            for (int i = 0; i < nodesList.getLength() - 1; i++) {
                vertNeighbours[i] = nodesList.get(i).getValue();
            }
        }
        return vertNeighbours;   
    } // end of kHopNeighbours()


    public void printVertices(PrintWriter os) {
        String outputString = "";

        for (int row = 0; row < adjacencyList.length; row++) {
            outputString += "(" + adjacencyList[row].get(adjacencyList[row].getLength() - 1).getValue() 
                        + ", " + adjacencyList[row].get(adjacencyList[row].getLength() - 1).getState() 
                        + ") ";
        }

        os.printf(outputString);
        os.println();
        os.flush();
    } // end of printVertices()


    public void printEdges(PrintWriter os) {
        String outputString = "";

        for (int row = 0; row < adjacencyList.length; row++) {
            for (int edge = 0; edge < adjacencyList[row].getLength() - 1; edge++) {
                outputString = adjacencyList[row].get(adjacencyList[row].getLength() - 1).getValue() 
                            + " " 
                            + adjacencyList[row].get(edge).getValue();

                os.printf(outputString);
                os.println();
            } 
        }

        os.flush();
    } // end of printEdges()

} // end of class AdjacencyList
