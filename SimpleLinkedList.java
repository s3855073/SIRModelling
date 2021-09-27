
/**
 * Singly linked list.
 *
 * @author Jeffrey Chan, RMIT
 */
public class SimpleLinkedList implements MyList
{
    /** Reference to head node. */
    protected Node mHead;

    /** Length of list. */
    protected int mLength;

    /**
     * Default constructor.
     */
    public SimpleLinkedList() {
        mHead = null;
        mLength = 0;
    } // end of SimpleLinkedList()


    /**
     * Add a new value to the start of the list.
     *
     * @param newValue Value to add to list.
     */
    public void add(String newValue) {
        Node newNode = new Node(newValue);

        // If head is empty, then list is empty and head reference need to be initialised.
        if (mHead == null) {
            mHead = newNode;
        }
        // otherwise, add node to the head of list.
        else {
            newNode.setNext(mHead);
            mHead = newNode;
        }

        mLength++;
    } // end of add()

    /**
     * Returns the value stored in node at position 'index' of list.
     *
     * @param index Position in list to get new value for.
     * @return Value of element at specified position in list.
     *
     * @throws IndexOutOfBoundsException Index is out of bounds.
     */
    public Node get(int index) throws IndexOutOfBoundsException {
        if (index >= mLength || index < 0) {
            throw new IndexOutOfBoundsException("Supplied index is invalid.");
        }

        Node currNode = mHead;
        
        for (int i = 0; i < index; ++i) {
            currNode = currNode.getNext();
        }

        return currNode;
    } // end of get()


    /**
     * Searches for the index that contains value.  If value is not present,
     * method returns -1 (NOT_IN_ARRAY).
     * If there are multiple values that could be returned, return the one with
     * the smallest index.
     *
     * @param value Value to search for.
     * @return Index where value is located, otherwise returns -1 (NOT_IN_ARRAY).
     */
    public int search(String value) {
        Node currNode = mHead;

        int returnIndex = NOT_IN_ARRAY;

        for (int i = 0; i < mLength; ++i) {
            if (currNode.getValue().equals(value)) {
                returnIndex = i;
            }
            currNode = currNode.getNext();
        }

        return returnIndex;
    } // end of search()



    /**
     * Delete given value from list (delete first instance found).
     *
     * @param value Value to remove.
     * @return True if deletion was successful, otherwise false.
     */
    public void remove(String value) {
        // IMPLEMENT ME!
    	
    	//boolean removed = false;
    	
    	Node currNode = mHead;
    	
    	if (currNode != null) {
	    	boolean searching = true;
	    	
	    	while (searching == true) {
	    		if (currNode.getValue().equals(value)){
	    			mHead = currNode.getNext();
	    			
	    			currNode = null;
	    			searching = false;
	    			//removed = true;
	    			mLength--;
	    		} else if (currNode.getNext() == null) {
	    			searching = false;
	    		} else if (currNode.getNext().getValue().equals(value)) {
	    			Node removeNode = currNode.getNext();
	    			
	    			currNode.setNext(removeNode.getNext());
	    			
	    			removeNode = null;
	    			searching = false;
	    			//removed = true;
	    			mLength--;
	    		} else {
		    		currNode = currNode.getNext();
	    		}
	    	}
    	}
    	
    	//return removed;
    } // end of remove()


    /**
     * Delete value (and corresponding node) at position 'index'.  Indices start at 0.
     *
     * @param index Position in list to get new value for.
     * @param dummy Dummy variable, serves no use apart from distinguishing overloaded methods.
     * @return Value of node that was deleted.
     *
     * @throws IndexOutOfBoundsException Index is out of bounds.
     */
    public String removeByIndex(int index) throws IndexOutOfBoundsException{
        if (index >= mLength || index < 0) {
            throw new IndexOutOfBoundsException("Supplied index is invalid.");
        }

        // IMPLEMENT ME!
        int nodeIndex = 0;
        String removedNodeValue = "";
        
        Node currNode = mHead;
        
        if (currNode != null) {
        	boolean searching = true;
        	
        	while (searching) {
	        	if (nodeIndex == index && currNode == mHead) {
	        		mHead = currNode.getNext();
	        		currNode = null;
	        		searching = false;
	        		mLength--;
	        	} else if (currNode.getNext() == null) {
	        		searching = false;
	        	} else if (nodeIndex == index - 1) {
	        		Node removeNode = currNode.getNext();
	        		
	        		currNode.setNext(removeNode.getNext());
	        		removedNodeValue = removeNode.getValue();
	        		
	        		removeNode = null;
	        		searching = false;
	        		mLength--;
	        	} else {
		        	nodeIndex++;
		        	currNode = currNode.getNext();
	        	}
	        }
        }
        // PLACEHOLDER: UPDATE (THIS IS DUMMY VALUE)
        return removedNodeValue;
    } // end of remove()

    public int getLength() {
        return mLength;
    }


} // end of class SimpleLinkedList
