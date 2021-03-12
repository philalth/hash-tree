package hashtrees;

import java.util.List;

/**
 * Interface for HashTrees.
 * 
 * @param <V> the type of elements stored in the tree
 */
public interface HashTree<V> {

  /**
   * Changes the hash at a given position.
   * 
   * @param position index of the node calculated by a breadth-first-traversal
   * @param hash the new hash
   * @throws IndexOutOfBoundsException if the index is out of range
   */
  public void setHash(int position, long hash);

  /**
   * Changes the value at a given position. The node at this position must be a leaf node as it is
   * not intended to set values for inner nodes. Unlike inner node indices leaf indices are counted
   * from left to right instead of a breadth-first-traversal.
   * 
   * @param position index of the node counted from left to right
   * @param value new value
   * @throws IndexOutOfBoundsException if the index is out of range
   */
  public void setValue(int position, V value);

  /**
   * Checks if the given root hash is matching with the expected root hash calculated by the hash
   * function.
   * 
   * @return {@code true} if the root hash is correct
   */
  public boolean isConsistent();

  /**
   * Determines which nodes are needed to calculate the root hash.
   * 
   * @return a {@code List<Integer>} containing the indices of the nodes needed
   */
  public List<Integer> getMissing();

  /**
   * Deletes all existing values, thus the tree will be empty after this call returns.
   */
  public void clear();

}
