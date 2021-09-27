public class Node {
    private String value;
    private SIRState state;
    private Node nextNode;

    Node(String value) {
        this.value = value;
        this.state = SIRState.S;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Node getNext() {
        return nextNode;
    }

    public void setNext(Node nextNode) {
        this.nextNode = nextNode;
    }

    public SIRState getState() {
        return state;
    }

    public void setState(SIRState state) {
        this.state = state;
    }
}