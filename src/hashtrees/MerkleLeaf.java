package hashtrees;

import java.util.Optional;

/**
 * This class represents a leaf node of a Merkle-Tree. Unlike inner nodes leaf nodes do not have
 * child nodes, but a value.
 * 
 * @param <V> the type of elements in this node
 */
public class MerkleLeaf<V> extends MerkleNode<V> {

  private Optional<V> value;

  /**
   * Creates a new {@code MerkleLeaf}.
   * 
   * @param parent the parent of this node
   */
  public MerkleLeaf(MerkleInnerNode<V> parent) {
    this.value = Optional.empty();
    this.parent = parent;
    this.hash = Optional.empty();
  }

  /** Sets the value for this node. */
  void setValue(V value) {
    this.value = Optional.ofNullable(value);
    if (value != null) {
      hash = Optional.of(Long.valueOf(hashFunction(value)));
    } else {
      hash = Optional.empty();
    }
    update();
  }

  /**
   * Tells the parent of this leaf to update.
   */
  @Override
  void update() {
    parent.update();
  }

  /**
   * {@inheritDoc}
   * 
   * @throws IllegalArgumentException if this leaf has a value thus the hash would manipulate the
   *         actual hash
   */
  @Override
  void setHash(Long hash) {
    if (!value.isPresent()) {
      this.hash = Optional.of(hash);
      update();
    } else {
      throw new IllegalArgumentException();
    }
  }

  /** Hash function for leaf nodes. */
  private long hashFunction(V value) {
    return value.toString().hashCode();
  }

  /**
   * Returns a textual representation of this node. Values are indicated by surrounding quotes, no
   * value is represented by an asterisk. Hashes are indicated by no surrounding quotes.
   */
  @Override
  public String toString() {
    if (value.isPresent()) {
      return "(\"" + value.get().toString() + "\")";
    } else if (hasHash()) {
      return hash.get().toString();
    } else {
      return "(*)";
    }
  }

}
