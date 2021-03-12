package hashtrees;

import java.util.Optional;

/**
 * This class represents an inner node of a Merkle-Tree.
 * 
 * @param <V> the type of elements stored in a leaf node
 */
public class MerkleInnerNode<V> extends MerkleNode<V> {

  /**
   * The left child of this node.
   */
  private MerkleNode<V> left;

  /**
   * The right child of this node.
   */
  private MerkleNode<V> right;

  /**
   * Creates a new {@code MerkleInnerNode}. Should only be used to create the root node of a
   * Merkle-Tree.
   */
  public MerkleInnerNode() {
    this.hash = Optional.empty();
  }

  /**
   * Creates a new {@code MerkleInnerNode}. Should only be used to create the root node of an
   * {@code UnmodifiableMerkleTree}.
   * 
   * @param hash the hash of this node (can be {@code null})
   */
  public MerkleInnerNode(Long hash) {
    this.hash = Optional.ofNullable(hash);
  }

  /**
   * Creates a new {@code MerkleInnerNode}.
   * 
   * @param parent the parent-node of this node
   */
  public MerkleInnerNode(MerkleInnerNode<V> parent) {
    this.parent = parent;
    this.hash = Optional.empty();
  }

  /**
   * Recalculates the hash of a node if possible, and updates the next higher node.
   */
  @Override
  void update() {
    if ((parent == null && hasHash()) || right == null) {
      // once the root hash is set it cannot be altered
      return;
    } else if (right.hasHash() && left.hasHash()) {
      hash = Optional.of(hashFunction(left.getStoredHash(), right.getStoredHash()));
      if (parent != null) {
        parent.update();
      }
    } else {
      hash = Optional.empty();
    }
  }

  @Override
  void setHash(Long hash) {
    this.hash = Optional.ofNullable(hash);
    if (parent != null) {
      parent.update();
    }
  }

  /** Returns the left child. */
  MerkleNode<V> getLeft() {
    return left;
  }

  /** Sets the left child. */
  void setLeft(MerkleNode<V> left) {
    this.left = left;
  }

  /** Returns the right child. */
  MerkleNode<V> getRight() {
    return right;
  }

  /** Sets the right child. */
  void setRight(MerkleNode<V> right) {
    this.right = right;
  }

  /** Sets the parent node. */
  void setParent(MerkleInnerNode<V> parent) {
    this.parent = parent;
  }

  /** Checks if the hash stored in this node is correct. */
  boolean checkHash() {
    return hash.get() == (hashFunction(right.getStoredHash(), left.getStoredHash()));
  }

  /** Hash function for inner nodes. */
  private static long hashFunction(long leftHash, long rightHash) {
    return leftHash * rightHash;
  }

  /**
   * Returns a textual representation of this node and (if existing) its subtree. The levels of a
   * subtree are indicated by surrounding parentheses.
   */
  @Override
  public String toString() {
    StringBuilder res = new StringBuilder();
    res.append("(");
    if (hasHash()) {
      res.append(hash.get().toString() + " ");
    } else {
      res.append("* ");
    }
    res.append(left.toString() + " ");
    res.append(right.toString() + ")");
    return res.toString();
  }

}
