package hashtrees;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Used to construct and operate on a {@code MutableMerkleTree}.
 * 
 * @param <V> the type of elements stored in the tree
 */
public class MerkleTreeBuilder<V> {

  /**
   * The height of the current tree.
   */
  private int height;

  /**
   * The values stored in the current tree.
   */
  private Queue<V> leafData;

  /**
   * The current tree.
   */
  private MutableMerkleTree<V> tree;

  /**
   * Creates a new {@code MerkleTreeBuilder}.
   * 
   * @param leavesNeeded the number of leaves needed for the Merkle-Tree
   */
  public MerkleTreeBuilder(int leavesNeeded) {
    height = MutableMerkleTree.getExponentOfNextPowerOfTwo(leavesNeeded);
    leafData = new LinkedList<V>();
    this.tree = new MutableMerkleTree<V>(leavesNeeded);
  }

  /**
   * Inserts a value at the next possible leaf.
   * 
   * @param element the element to be inserted
   * @return this builder
   */
  public MerkleTreeBuilder<V> push(V element) {
    int maxNumberOfLeaves = (int) Math.pow(2, height);
    int currentLeafIndex = leafData.size();

    if (maxNumberOfLeaves == currentLeafIndex) {
      // the tree is full so it has to be extended by one level i.e. it has twice as much leaf nodes
      MutableMerkleTree<V> nextHigherTree = new MutableMerkleTree<V>(leafData.size() + 2);
      nextHigherTree.getRoot().setLeft(tree.getRoot());
      tree.getRoot().setParent(nextHigherTree.getRoot());
      tree = nextHigherTree;
      height++;
    }
    tree.setValue(currentLeafIndex, element);
    leafData.add(element);
    return this;
  }

  /**
   * Constructs an {@code UnmodifiableMerkleTree} from the current tree.
   * 
   * @return an {@code UnmodifiableMerkleTree} contentwise equal to the current tree
   */
  public HashTree<V> build() {
    Long rootHash = tree.getRoot().getStoredHash();
    return new UnmodifiableMerkleTree<V>(height, rootHash, leafData);
  }

  /**
   * Deletes all existing values in this tree.
   * 
   * @see HashTree#clear()
   */
  public void clear() {
    tree.clear();
    tree.getRoot().setHash(null);
    leafData.clear();
  }

}
