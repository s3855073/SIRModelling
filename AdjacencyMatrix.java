import java.io.PrintWriter;

/**
 * Adjacency matrix implementation for the GraphInterface interface.
 *
 * Your task is to complete the implementation of this class.  You may add methods, but ensure your modified class compiles and runs.
 *
 * @author Jeffrey Chan, 2021.
 */
public class AdjacencyMatrix extends AbstractGraph {

	/**
	 * Contructs empty graph.
	 */

    private Node[][] adjacencyMatrix;
    
    public AdjacencyMatrix() {
        this.adjacencyMatrix = null;
    } // end of AdjacencyMatrix()


    public void addVertex(String vertLabel) {
        Node newVertNode = new Node(vertLabel);
        Node newEdgeNode = new Node("0");

        // Initialise matrix
        if (adjacencyMatrix == null) {
            adjacencyMatrix = new Node[2][2];

            adjacencyMatrix[0][1] = newVertNode;
            adjacencyMatrix[1][0] = newVertNode;
            adjacencyMatrix[1][1] = newEdgeNode;
        } else {
            // Update original matrix
            Node[][] tempAdjacencyMatrix = new Node[adjacencyMatrix.length + 1][adjacencyMatrix.length + 1];

            for (int row = 0; row < adjacencyMatrix.length; row++) {
                for (int col = 0; col < adjacencyMatrix.length; col++) {
                    if (row != 0 || col != 0) {
                        tempAdjacencyMatrix[row][col] = adjacencyMatrix[row][col];
                    }
                }
            }

            tempAdjacencyMatrix[adjacencyMatrix.length][0] = newVertNode;
            tempAdjacencyMatrix[0][adjacencyMatrix.length] = newVertNode;

            // Update all edges
            for (int col = 1; col < tempAdjacencyMatrix.length - 1; col++) {
                Node newVertEdgeNode = new Node("0");

                tempAdjacencyMatrix[col][tempAdjacencyMatrix.length - 1] = newVertEdgeNode;
                tempAdjacencyMatrix[tempAdjacencyMatrix.length - 1][col] = newVertEdgeNode;
            }

            tempAdjacencyMatrix[tempAdjacencyMatrix.length - 1][tempAdjacencyMatrix.length - 1] = newEdgeNode;

            adjacencyMatrix = tempAdjacencyMatrix;
        }
    } // end of addVertex()


    public void addEdge(String srcLabel, String tarLabel) {
        int srcIndex = 0;
        int tarIndex = 0;

        // Find edge between src and tar nodes
        for (int col = 1; col < adjacencyMatrix[0].length; col++) {
            if (adjacencyMatrix[0][col].getValue().equals(srcLabel))
                srcIndex = col;
            if (adjacencyMatrix[0][col].getValue().equals(tarLabel))
                tarIndex = col;
        }

        // Update edges
        if (srcIndex > 0 && tarIndex > 0) {
            adjacencyMatrix[srcIndex][tarIndex].setValue("1");
            adjacencyMatrix[tarIndex][srcIndex].setValue("1");
        }
    } // end of addEdge()


    public void toggleVertexState(String vertLabel) {
        Node vertNode = null;

        for (int col = 1; col < adjacencyMatrix.length; col++) {
            if (adjacencyMatrix[0][col].getValue().equals(vertLabel)) {
                vertNode = adjacencyMatrix[0][col];
                
                // Set new state of node
                SIRState newState = (vertNode.getState() == SIRState.S) ? SIRState.I : SIRState.R;
                vertNode.setState(newState);
            }
        }
    } // end of toggleVertexState()


    public void deleteEdge(String srcLabel, String tarLabel) {
        int srcIndex = 0;
        int tarIndex = 0;

        // Get index of src and tar vertices
        for (int col = 1; col < adjacencyMatrix.length; col++) {
            if (adjacencyMatrix[0][col].getValue().equals(srcLabel))
                srcIndex = col;
            if (adjacencyMatrix[0][col].getValue().equals(tarLabel))
                tarIndex = col;
        }

        // Delete edges
        if (srcIndex > 0 && tarIndex > 0) {
            adjacencyMatrix[srcIndex][tarIndex].setValue("0");
            adjacencyMatrix[tarIndex][srcIndex].setValue("0");
        }
    } // end of deleteEdge()


    public void deleteVertex(String vertLabel) {
        if (adjacencyMatrix != null) {
            if (adjacencyMatrix.length == 1 && adjacencyMatrix[0][1].getValue().equals(vertLabel)) {
                adjacencyMatrix = null;
            } else {
                int vertIndex = 0;

                // Get vertex index to be deleted
                for (int col = 1; col < adjacencyMatrix.length; col++) {
                    if (adjacencyMatrix[0][col].getValue().equals(vertLabel))
                        vertIndex = col;
                }

                // Update original matrix
                Node[][] tempAdjacencyMatrix = new Node[adjacencyMatrix.length - 1][adjacencyMatrix.length - 1];

                int tempRowCount = 0;
                int tempColCount = 1;

                for (int row = 0; row < adjacencyMatrix.length; row++) {
                    if (row != vertIndex) {
                        for (int col = 0; col < adjacencyMatrix.length; col++) {
                            if (col != vertIndex) {
                                if (row != 0 || col != 0) {
                                    tempAdjacencyMatrix[tempRowCount][tempColCount] = adjacencyMatrix[row][col];
                                    tempColCount++;
                                }
                            }
                        }

                        tempRowCount++;
                    }    

                    tempColCount = 0;
                }

                adjacencyMatrix = tempAdjacencyMatrix;
            }
        }
    } // end of deleteVertex()


    public String[] kHopNeighbours(int k, String vertLabel) {
        // Implement BFS
        SimpleLinkedList nodesList = new SimpleLinkedList();
        SimpleLinkedList visitedVertexes = new SimpleLinkedList();

        nodesList.add(vertLabel);

        int times = k;
        int nodesListSize = nodesList.getLength();

        while (times > 0) {
            for (int i = 0; i < nodesListSize; i++) {
                Node currNode = nodesList.get(i);

                // If node hasn't been visited
                if (visitedVertexes.search(currNode.getValue()) == -1) {
                    for (int row = 1; row < adjacencyMatrix.length; row++) {
                        if (adjacencyMatrix[row][0].getValue().equals(currNode.getValue())) {

                            for (int col = 1; col < adjacencyMatrix[row].length; col++) {
                                if (adjacencyMatrix[row][col].getValue().equals("1")) {
                                    String newAdjacentVertex = adjacencyMatrix[0][col].getValue();

                                    // Add adjacent node to neighbours list
                                    if (nodesList.search(newAdjacentVertex) == -1) {
                                        nodesList.add(newAdjacentVertex);
                                    }
                                }
                            }
                        }
                    }

                    // Add currNode to visited vertexes
                    visitedVertexes.add(currNode.getValue());
                }
            }

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

        for (int row = 1; row < adjacencyMatrix.length; row++) {
            outputString += "(" + adjacencyMatrix[row][0].getValue() 
                        + ", " + adjacencyMatrix[row][0].getState() 
                        + ") ";
        }

        os.printf(outputString);
        os.println();
        os.flush();
    } // end of printVertices()


    public void printEdges(PrintWriter os) {
        String outputString = "";

        for (int row = 1; row < adjacencyMatrix.length; row++) {
            for (int col = 1; col < adjacencyMatrix[0].length; col++) {
                if (adjacencyMatrix[row][col].getValue().equals("1")) {
                    outputString = adjacencyMatrix[row][0].getValue() 
                                + " " 
                                + adjacencyMatrix[0][col].getValue();

                    os.printf(outputString);
                    os.println();
                }
            }
        }

        os.flush();
    } // end of printEdges()

} // end of class AdjacencyMatrix
