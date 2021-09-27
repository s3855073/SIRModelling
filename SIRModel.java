import java.io.PrintWriter;

/**
 * SIR model.
 *
 * @author Jeffrey Chan, 2021.
 */
public class SIRModel {

    /**
     * Default constructor, modify as needed.
     */
    public SIRModel() {

    } // end of SIRModel()


    /**
     * Run the SIR epidemic model to completion, i.e., until no more changes to the states of the vertices for a whole iteration.
     *
     * @param graph Input contracts graph.
     * @param seedVertices Set of seed, infected vertices.
     * @param infectionProb Probability of infection.
     * @param recoverProb Probability that a vertex can become recovered.
     * @param sirModelOutWriter PrintWriter to output the necessary information per iteration (see specs for details).
     */
    public void runSimulation(ContactsGraph graph, String[] seedVertices,
        float infectionProb, float recoverProb, PrintWriter sirModelOutWriter)
    {
        // IMPLEMENT ME!
        String[] infectedNodes = new String[seedVertices.length];

        for (int i = 0; i < seedVertices.length; i++) {
            graph.toggleVertexState(seedVertices[i]);
            infectedNodes[i] = seedVertices[i];
        }

        boolean running = true;

        double randomProb = 0;
        int iterations = 1;
        int noChangeIterations = 0;

        while (running) {
            // newly infected
            String[] newlyInfectedNodes = getNewlyInfectedNodes(graph, randomProb, infectionProb, infectedNodes, sirModelOutWriter);

            // newly recovered
            String[] newlyRecoveredNodes = getNewlyRecoveredNodes(graph, randomProb, infectionProb, infectedNodes, sirModelOutWriter);
            
            // // update infected
            infectedNodes = updateInfectedNodes(newlyInfectedNodes, newlyRecoveredNodes, infectedNodes, sirModelOutWriter);

            sirModelOutWriter.printf(String.valueOf(iterations) + ": [ ");
            for (int i = 0; i < newlyInfectedNodes.length; i++) {
                sirModelOutWriter.printf(newlyInfectedNodes[i] + " ");
            }
            sirModelOutWriter.printf("] : [ ");
            for (int i = 0; i < newlyRecoveredNodes.length; i++) {
                sirModelOutWriter.printf(newlyRecoveredNodes[i] + " ");
            }
            sirModelOutWriter.printf("]");
            sirModelOutWriter.println();

            if (newlyInfectedNodes.length == 0 && newlyRecoveredNodes.length == 0) {
                noChangeIterations++;
            } else {
                noChangeIterations = 0;
            }

            running = updateStop(newlyInfectedNodes, newlyRecoveredNodes, infectedNodes, noChangeIterations);

            iterations++;
        }

        sirModelOutWriter.flush();
    } // end of runSimulation()

    private String[] getNewlyInfectedNodes(ContactsGraph graph, double randomProb, float infectionProb, String[] infectedNodes, PrintWriter sirModelOutWriter) {
        String[] newlyInfectedNodes = new String[0];
        int newlyInfectedNodesCount = 0;

        for (int i = 0; i < infectedNodes.length; i++) {
            String[] immediateNeighbours = graph.kHopNeighbours(1, infectedNodes[i]);

            randomProb = Math.random();

            if (randomProb < infectionProb) {
                for (int j = 0; j < immediateNeighbours.length; j++) {
                    String immediateNeighbour = immediateNeighbours[j];
                    boolean newlyInfected = false;

                    for (int n = 0; n < newlyInfectedNodes.length; n++) {
                        String newlyInfectedNode = newlyInfectedNodes[n];
                        if (newlyInfectedNode.equals(immediateNeighbour)) {
                            newlyInfected = true;
                        }
                    }

                    boolean alreadyInfected = false;
    
                    for (int n = 0; n < infectedNodes.length; n++) {
                        String alreadyInfectedNode = infectedNodes[n];

                        if (alreadyInfectedNode.equals(immediateNeighbour)) {
                            alreadyInfected = true;
                        }
                    }

                    if (!alreadyInfected && !newlyInfected) {
                        graph.toggleVertexState(immediateNeighbour);
                        newlyInfectedNodesCount++;

                        String[] tempNewlyInfectedNodes = new String[newlyInfectedNodesCount];

                        for (int n = 0; n < newlyInfectedNodes.length; n++) {
                            String newlyInfectedNode = newlyInfectedNodes[n];

                            tempNewlyInfectedNodes[n] = newlyInfectedNode;
                        }

                        tempNewlyInfectedNodes[newlyInfectedNodesCount - 1] = immediateNeighbour;

                        newlyInfectedNodes = tempNewlyInfectedNodes;
                    }
                }
            }
        }

        return newlyInfectedNodes;
    }

    private String[] getNewlyRecoveredNodes(ContactsGraph graph, double randomProb, float recoverProb, String[] infectedNodes, PrintWriter sirModelOutWriter) {
        String[] newlyRecoveredNodes = new String[0];

        for (int i = 0; i < infectedNodes.length; i++) {
            randomProb = Math.random();

            if (randomProb < recoverProb) {
                graph.toggleVertexState(infectedNodes[i]);

                String[] tempNewlyRecoveredNodes = new String[newlyRecoveredNodes.length + 1];

                for (int j = 0; j < newlyRecoveredNodes.length; j++) {
                    tempNewlyRecoveredNodes[j] = newlyRecoveredNodes[j];
                }

                tempNewlyRecoveredNodes[newlyRecoveredNodes.length] = infectedNodes[i];

                newlyRecoveredNodes = tempNewlyRecoveredNodes;
            }
        }

        return newlyRecoveredNodes;
    }

    private String[] updateInfectedNodes(String[] newlyInfectedNodes, String[] newlyRecoveredNodes, String[] infectedNodes, PrintWriter sirModelOutWriter) {
        String[] updatedInfectedNodes = new String[infectedNodes.length];

        for (int i = 0; i < infectedNodes.length; i++) {
            updatedInfectedNodes[i] = infectedNodes[i];
        }

        for (int i = 0; i < newlyInfectedNodes.length; i++) {
            String[] tempInfectedNodes = new String[updatedInfectedNodes.length + 1];

            for (int j = 0; j < updatedInfectedNodes.length; j++) {
                tempInfectedNodes[j] = updatedInfectedNodes[j];
            }

            tempInfectedNodes[updatedInfectedNodes.length] = newlyInfectedNodes[i];

            updatedInfectedNodes = tempInfectedNodes;
        }

        for (int i = 0; i < newlyRecoveredNodes.length; i++) {
            String newlyRecoveredNode = newlyRecoveredNodes[i];

            for (int j = 0; j < updatedInfectedNodes.length; j++) {
                String infectedNode = updatedInfectedNodes[j];

                if (infectedNode.equals(newlyRecoveredNode)) {
                    String[] tempInfectedNodes = new String[updatedInfectedNodes.length - 1];
                    int tempInfectedNodesCount = 0;

                    for (int n = 0; n < updatedInfectedNodes.length; n++) {
                        String copyInfectedNode = updatedInfectedNodes[n];

                        if (!copyInfectedNode.equals(newlyRecoveredNode)) {
                            tempInfectedNodes[tempInfectedNodesCount] = copyInfectedNode;
                            tempInfectedNodesCount++;
                        }
                    }

                    updatedInfectedNodes = tempInfectedNodes;
                }
            }
        }

        return updatedInfectedNodes;
    }

    private boolean updateStop(String[] newlyInfectedNodes, String[] newlyRecoveredNodes, String[] infectedNodes, int noChangeIterations) {
        boolean running = true;
        
        if ((newlyInfectedNodes.length == 0 && newlyRecoveredNodes.length == 0)
                && infectedNodes.length == 0
                        && noChangeIterations >= 10) {
            running = false;
        }

        return running;
    }
} // end of class SIRModel
