/**
 * Interface for the two lists implementation we will develop in this lab.
 * Note, as we do not seek to implement all the functionality in the built-in
 * List interface, we have develop our own interface.
 *
 * @author Jeffrey Chan, RMIT
 */
public interface MyList
{
    /** Default value to return for search when value isn't found in array. */
    public static final int NOT_IN_ARRAY = -1;


    /**
     * Add a new value to the start of the list.
     *
     * @param newValue Value to add to list.
     */
    public abstract void add(String newValue);

    /**
     * Returns the value stored in node at position 'index' of list.
     *
     * @param index Position in list to get new value for.
     * @return Value of element at specified position in list.
     *
     * @throws IndexOutOfBoundsException Index is out of bounds.
     */
    public abstract Node get(int index) throws IndexOutOfBoundsException;


    /**
     * Searches for the index that contains value.  If value is not present,
     * method returns -1 (NOT_IN_ARRAY).
     * If there are multiple values that could be returned, return the one with
     * the smallest index.
     *
     * @param value Value to search for.
     * @return Index where value is located, otherwise returns -1 (NOT_IN_ARRAY).
     */
    public abstract int search(String value);


    /**
     * Delete given value from list (delete first instance found).
     *
     * @param value Value to remove.
     * @return True if deletion was successful, otherwise false.
     */
    public abstract void remove(String value);


    /**
     * Delete value (and corresponding node) at position 'index'.  Indices start at 0.
     *
     * @param index Position in list to get new value for.
     * @param dummy Dummy variable, serves no use apart from distinguishing overloaded methods.
     * @return Value of node that was deleted.
     *
     * @throws IndexOutOfBoundsException Index is out of bounds.
     */
    public abstract String removeByIndex(int index) throws IndexOutOfBoundsException;

    public int getLength();
} // end of interface MyList
