package hashtrees;

/**
 * Represents a mutable Merkle-Tree. A Merkle-Tree is a hash tree in which every leaf node is
 * labelled with the hash of a data block and every non-leaf node is labelled with the cryptographic
 * hash of the labels of its child nodes.
 * 
 * @param <V> the type of elements stored in this tree
 */
public class MutableMerkleTree<V> extends AbstractHashTree<V> implements HashTree<V> {

  /**
   * Creates a new Merkle-Tree.
   * 
   * @param leavesNeeded the number of leaves needed in this tree
   */
  public MutableMerkleTree(int leavesNeeded) {
    super();
    int height = getExponentOfNextPowerOfTwo(leavesNeeded);
    create(getRoot(), height);
  }

  /** Helper method to create a MutableMerkleTree from a given height. */
  private void create(MerkleInnerNode<V> parent, int height) {
    height--;
    if (height > 0) {
      MerkleInnerNode<V> temp = parent;

      // create left subtree
      parent.setLeft(new MerkleInnerNode<V>(parent));
      temp = (MerkleInnerNode<V>) parent.getLeft();
      create(temp, height);

      // create right subtree
      parent.setRight(new MerkleInnerNode<V>(parent));
      temp = (MerkleInnerNode<V>) parent.getRight();
      create(temp, height);
    } else {
      // height == 0, that means last level
      parent.setLeft(new MerkleLeaf<V>(parent));
      parent.setRight(new MerkleLeaf<V>(parent));
    }
  }

  @Override
  public void setHash(int position, long hash) {
    MerkleNode<V> changeNode = depthFirstSearch(position);
    changeNode.setHash(hash);
  }

  @Override
  public void setValue(int position, V value) {
    int bfsIndex = (int) Math.pow(2, calculateHeight()) + position - 1;
    MerkleNode<V> changeNode = depthFirstSearch(bfsIndex);
    if (changeNode instanceof MerkleLeaf) {
      ((MerkleLeaf<V>) changeNode).setValue(value);
    } else {
      throw new IndexOutOfBoundsException();
    }
  }

  /** Returns the height of the tree. */
  private int calculateHeight() {
    MerkleNode<V> current = getRoot();
    int height = 0;
    while (current instanceof MerkleInnerNode) {
      height++;
      current = ((MerkleInnerNode<V>) current).getRight();
    }
    return height;
  }

  @Override
  public void clear() {
    deleteLeafValues(getRoot());
  }

  /** Helper method for deleting all values i.e. clearing the tree. */
  private void deleteLeafValues(MerkleNode<V> nd) {
    if (nd instanceof MerkleInnerNode) {
      deleteLeafValues(((MerkleInnerNode<V>) nd).getLeft());
      deleteLeafValues(((MerkleInnerNode<V>) nd).getRight());
    } else {
      ((MerkleLeaf<V>) nd).setValue(null);
    }
  }

  /** Returns the exponent of the next power of a given number. */
  static int getExponentOfNextPowerOfTwo(int x) {
    return (int) Math.ceil(log2(x));
  }

  /** Returns the binary logarithm (base 2) of an {@code int} value. */
  private static double log2(int x) {
    return (Math.log(x) / Math.log(2));
  }

}
