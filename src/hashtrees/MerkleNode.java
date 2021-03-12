package hashtrees;

import java.util.Optional;

/**
 * This class is a skeleton for all nodes of a Merkle-Tree. It provides methods for checking if a
 * hash is present and getting the stored hash.
 *
 * @param <V> the type of elements in the node
 */
abstract class MerkleNode<V> {

  /**
   * The node's parent node.
   */
  protected MerkleInnerNode<V> parent;

  /**
   * The node's hash.
   */
  protected Optional<Long> hash;

  /**
   * Returns {@code true}, if the node has a hash.
   */
  public boolean hasHash() {
    return hash.isPresent();
  }

  /**
   * Returns the stored hash of this node. Returns {@code null} if there is no hash.
   */
  public Long getStoredHash() {
    if (hasHash()) {
      return hash.get();
    } else {
      return null;
    }
  }

  abstract void update();

  /**
   * Sets the hash for this node updates the parent node to recalculate its hash.
   * 
   * @param hash the new hash
   */
  abstract void setHash(Long hash);

}
