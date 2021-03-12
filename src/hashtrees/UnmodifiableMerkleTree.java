package hashtrees;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Represents an unmodifiable Merkle-Tree. A Merkle-Tree is a hash tree in which every leaf node is
 * labelled with the hash of a data block and every non-leaf node is labelled with the cryptographic
 * hash of the labels of its child nodes. After its creation this tree cannot be modified in any way
 * all operations that do so are unsupported an throw an Exception.
 * 
 * @param <V> the type of elements stored in this tree
 */
public class UnmodifiableMerkleTree<V> extends AbstractHashTree<V> implements HashTree<V> {

  /**
   * Creates a new {@code UnmodifiableMerkleTree}.
   * 
   * @param height the height of the tree
   * @param rootHash the root hash of the tree ({@code null} if not existing)
   * @param data the elements to be inserted
   */
  public UnmodifiableMerkleTree(int height, Long rootHash, Queue<V> data) {
    super(rootHash);
    Queue<V> dataCopy = new LinkedList<V>(data);
    create(getRoot(), height, dataCopy);
  }

  /** Helper method to create an UnmodifiableMerkleTree from an already existing tree. */
  private void create(MerkleInnerNode<V> parent, int height, Queue<V> data) {
    height--;
    if (height > 0) {
      MerkleInnerNode<V> temp = parent;

      // create left subtree
      parent.setLeft(new MerkleInnerNode<V>(parent));
      temp = (MerkleInnerNode<V>) parent.getLeft();
      create(temp, height, data);

      // create right subtree
      parent.setRight(new MerkleInnerNode<V>(parent));
      temp = (MerkleInnerNode<V>) parent.getRight();
      create(temp, height, data);
    } else {
      // height == 0, that means last level
      MerkleLeaf<V> leftChild = new MerkleLeaf<V>(parent);
      parent.setLeft(leftChild);
      MerkleLeaf<V> rightChild = new MerkleLeaf<V>(parent);
      parent.setRight(rightChild);
      // insert data
      leftChild.setValue(data.poll());
      rightChild.setValue(data.poll());
    }
  }

  /**
   * Guaranteed to throw an exception and leave the tree unmodified.
   *
   * @throws UnsupportedOperationException always
   */
  @Override
  public void setHash(int position, long hash) {
    throw new UnsupportedOperationException();
  }

  /**
   * Guaranteed to throw an exception and leave the tree unmodified.
   *
   * @throws UnsupportedOperationException always
   */
  @Override
  public void setValue(int position, V value) {
    throw new UnsupportedOperationException();
  }

  /**
   * Guaranteed to throw an exception and leave the tree unmodified.
   *
   * @throws UnsupportedOperationException always
   */
  @Override
  public void clear() {
    throw new UnsupportedOperationException();

  }

}
