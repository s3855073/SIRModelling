import java.io.PrintWriter;

/**
 * Incidence matrix implementation for the GraphInterface interface.
 *
 * Your task is to complete the implementation of this class.  You may add methods, but ensure your modified class compiles and runs.
 *
 * @author Jeffrey Chan, 2021.
 */
public class IncidenceMatrix extends AbstractGraph
{

	/**
	 * Contructs empty graph.
	 */
    Node[][] incidenceMatrix;

    public IncidenceMatrix() {
        this.incidenceMatrix = null;
    } // end of IncidenceMatrix()


    public void addVertex(String vertLabel) {
        if (!containsVertex(vertLabel)) {
            Node newVertNode = new Node(vertLabel);

            // Initialise new incidence matrix
            if (incidenceMatrix == null) {
                incidenceMatrix = new Node[2][1];
                
                incidenceMatrix[1][0] = newVertNode;
            } else {
                // Create temp incidence matrix to be read into
                Node[][] tempIncidenceMatrix = new Node[incidenceMatrix.length + 1][incidenceMatrix[0].length];

                // Read all original nodes into temp
                for (int row = 0; row < incidenceMatrix.length; row++) {
                    for (int col = 0; col < incidenceMatrix[0].length; col++) {
                        if (row != 0 || col != 0) {
                            tempIncidenceMatrix[row][col] = incidenceMatrix[row][col];
                        }
                    }
                }

                // Input new vertex node
                tempIncidenceMatrix[incidenceMatrix.length][0] = newVertNode;

                // Update edges for all vertices
                for (int col = 1; col < incidenceMatrix[0].length; col++) {
                    Node newVertEdgeNode = new Node("0");

                    tempIncidenceMatrix[incidenceMatrix.length][col] = newVertEdgeNode;
                }

                // Set original matrix as temp
                incidenceMatrix = tempIncidenceMatrix;
            }
        }
    } // end of addVertex()


    public void addEdge(String srcLabel, String tarLabel) {
        if (!containsEdge(srcLabel + tarLabel) && !containsEdge(tarLabel + srcLabel)) {
            Node newEdgeNode = new Node(srcLabel + tarLabel);

            // Create new temp matrix and read into it the original nodes
            Node[][] tempIncidenceMatrix = new Node[incidenceMatrix.length][incidenceMatrix[0].length + 1];

            for (int row = 0; row < incidenceMatrix.length; row++) {
                for (int col = 0; col < incidenceMatrix[0].length; col++) {
                    if (row != 0 || col != 0) {
                        tempIncidenceMatrix[row][col] = incidenceMatrix[row][col];
                    }
                }
            }

            tempIncidenceMatrix[0][incidenceMatrix[0].length] = newEdgeNode;
            
            // Set edges for all rows
            for (int row = 1; row < incidenceMatrix.length; row++) {
                String edgeValue = (incidenceMatrix[row][0].getValue().equals(srcLabel) 
                                || incidenceMatrix[row][0].getValue().equals(tarLabel)) 
                                ? "1" 
                                : "0";
                Node newVertEdgeNode = new Node(edgeValue);

                tempIncidenceMatrix[row][incidenceMatrix[0].length] = newVertEdgeNode;
            }

            // Set original matrix as new matrix
            incidenceMatrix = tempIncidenceMatrix;
        }
    } // end of addEdge()


    public void toggleVertexState(String vertLabel) {
        Node vertNode = null;

        for (int row = 1; row < incidenceMatrix.length; row++) {
            if (incidenceMatrix[row][0].getValue().equals(vertLabel)) {
                vertNode = incidenceMatrix[row][0];
                
                // Set new state of vertex
                SIRState newState = (vertNode.getState() == SIRState.S) ? SIRState.I : SIRState.R;
                vertNode.setState(newState);
            }
        }
    } // end of toggleVertexState()


    public void deleteEdge(String srcLabel, String tarLabel) {
        if (containsEdge(srcLabel + tarLabel) || containsEdge(tarLabel + srcLabel)) {
            int edgeIndex = 0;

            // Get the index of the edge in the matrix to be deleted
            for (int col = 1; col < incidenceMatrix[0].length; col++) {
                if (incidenceMatrix[0][col].getValue().equals(srcLabel + tarLabel) 
                        || incidenceMatrix[0][col].getValue().equals(tarLabel + srcLabel))
                    edgeIndex = col;
            }

            // Update original matrix
            Node[][] tempIncidenceMatrix = new Node[incidenceMatrix.length][incidenceMatrix[1].length - 1];
            int tempColCount = 1;

            for (int row = 0; row < incidenceMatrix.length; row++) {
                for (int col = 0; col < incidenceMatrix[0].length; col++) {
                    if ((row != 0 || col != 0) && col != edgeIndex) {
                        tempIncidenceMatrix[row][tempColCount] = incidenceMatrix[row][col];

                        tempColCount++;
                    }
                }

                tempColCount = 0;
            }

            incidenceMatrix = tempIncidenceMatrix;
        }
    } // end of deleteEdge()


    public void deleteVertex(String vertLabel) {
        if (containsVertex(vertLabel)) {
            int edgeCount = 0;

            // Get the amount of edges to vertex
            for (int col = 1; col < incidenceMatrix[0].length; col++) {
                if (incidenceMatrix[0][col].getValue().substring(0, vertLabel.length()).equals(vertLabel)
                    || incidenceMatrix[0][col].getValue().substring(vertLabel.length()).equals(vertLabel))
                    edgeCount++;
            }

            // Update original matrix
            Node[][] tempIncidenceMatrix = new Node[incidenceMatrix.length - 1][incidenceMatrix[0].length - edgeCount];
            int tempRowCount = 0;
            int tempColCount = 1;

            for (int row = 0; row < incidenceMatrix.length; row++) {
                if (incidenceMatrix[row][0] == null || !incidenceMatrix[row][0].getValue().equals(vertLabel)) {
                    for (int col = 0; col < incidenceMatrix[0].length; col++) {
                        if ((row != 0 || col != 0) 
                                && (incidenceMatrix[0][col] == null 
                                        || (!incidenceMatrix[0][col].getValue().substring(0, vertLabel.length()).equals(vertLabel)
                                                && !incidenceMatrix[0][col].getValue().substring(vertLabel.length()).equals(vertLabel)))) {
                            tempIncidenceMatrix[tempRowCount][tempColCount] = incidenceMatrix[row][col];
                            
                            tempColCount++;
                        }
                    }

                    tempRowCount++;
                    tempColCount = 0;
                }
            }

            incidenceMatrix = tempIncidenceMatrix;
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
                
                // If node hasn't already been visited
                if (visitedVertexes.search(currNode.getValue()) == -1) {
                    for (int row = 1; row < incidenceMatrix.length; row++) {
                        if (incidenceMatrix[row][0].getValue().equals(currNode.getValue())) {

                            for (int col = 1; col < incidenceMatrix[row].length; col++) {
                                if (incidenceMatrix[row][col].getValue().equals("1")) {

                                    String vertValue = incidenceMatrix[row][0].getValue();
                                    String edgeValue = incidenceMatrix[0][col].getValue();
                                    String srcLabel = edgeValue.substring(0, vertValue.length());
                                    String tarLabel = edgeValue.substring(vertValue.length());

                                    String newAdjacentVertex = vertValue.equals(tarLabel) 
                                        ? srcLabel 
                                        : tarLabel;

                                    // Add node to neighbouring nodes
                                    if (nodesList.search(newAdjacentVertex) == -1) {
                                        nodesList.add(newAdjacentVertex);
                                    }
                                }
                            }
                        }
                    }
                    
                    // Add currNode to visited nodes
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

        for (int row = 1; row < incidenceMatrix.length; row++) {
            outputString += "(" + incidenceMatrix[row][0].getValue() 
                        + ", " + incidenceMatrix[row][0].getState() 
                        + ") ";
        }

        os.printf(outputString);
        os.println();
        os.flush();
    } // end of printVertices()


    public void printEdges(PrintWriter os) {
        String outputString = "";

        for (int row = 1; row < incidenceMatrix.length; row++) {
            for (int col = 1; col < incidenceMatrix[0].length; col++) {
                if (incidenceMatrix[row][col].getValue().equals("1")) {
                    String vertValue = incidenceMatrix[row][0].getValue();
                    String edgeValue = incidenceMatrix[0][col].getValue();
                    String srcLabel = edgeValue.substring(0, vertValue.length());
                    String tarLabel = edgeValue.substring(vertValue.length());

                    outputString = vertValue + " ";
                    
                    outputString += vertValue.equals(tarLabel) 
                        ? srcLabel 
                        : tarLabel;

                    os.printf(outputString);
                    os.println();
                }
            }
        }

        os.flush();
    } // end of printEdges()

    public boolean containsVertex(String vertex) {
        boolean containsVertex = false;

        if (incidenceMatrix != null) {
            for (int row = 1; row < incidenceMatrix.length; row++) {
                if (incidenceMatrix[row][0].getValue().equals(vertex)) {
                    containsVertex = true;
                }
            }
        }

        return containsVertex;
    }

    public boolean containsEdge(String edge) {
        boolean containsEdge = false;

        if (incidenceMatrix != null) {
            for (int col = 1; col < incidenceMatrix[0].length; col++) {
                if (incidenceMatrix[0][col].getValue().equals(edge)) {
                    containsEdge = true;
                }
            }
        }

        return containsEdge;
    }

} // end of class IncidenceMatrix
