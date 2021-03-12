package hashtrees;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class provides a skeletal implementation for all classes implementing the HashTree
 * interface. It also adds implementations for isConsistent and getMissing. Furthermore it provides
 * a search algorithm to search nodes by indices calculated by a breadth-first-traversal. This
 * search is based on a depth-first-search and is guaranteed to run in O(log n).
 *
 * @param <V> the type of elements stored in the tree
 */
abstract class AbstractHashTree<V> implements HashTree<V> {

  /**
   * Root node of the HashTree.
   */
  private MerkleInnerNode<V> root;

  /**
   * Initializes the root.
   */
  protected AbstractHashTree() {
    this.root = new MerkleInnerNode<V>();
  }

  /**
   * Initializes the root.
   * 
   * @param hash the hash for the root
   */
  protected AbstractHashTree(Long hash) {
    this.root = new MerkleInnerNode<V>(hash);
  }

  @Override
  public boolean isConsistent() {
    return root.checkHash();
  }

  @Override
  public List<Integer> getMissing() {
    final Set<Integer> missingNodes = getIndicesWithoutHashes(0);
    filterUnnecessaryNodes(missingNodes);
    final List<Integer> result = new ArrayList<Integer>();
    result.addAll(missingNodes);
    Collections.sort(result);
    return result;
  }

  /** Helper method for {@link HashTree#getMissing()}. */
  private Set<Integer> filterUnnecessaryNodes(Set<Integer> nodesWithoutHash) {
    final int lastInnerNodeIndex = calculateLastInnerNodeIndex(calculateHeight());
    for (int currentNodeIndex = lastInnerNodeIndex; currentNodeIndex >= 0; currentNodeIndex--) {
      final int leftChildIndex = 2 * currentNodeIndex + 1;
      final int rightChildIndex = 2 * currentNodeIndex + 2;

      final boolean nodeHasHash = !nodesWithoutHash.contains(currentNodeIndex);
      final boolean bothChildrenHaveNoHash =
          nodesWithoutHash.contains(rightChildIndex) && nodesWithoutHash.contains(leftChildIndex);

      if (nodeHasHash) {
        // the current node has a hash, thus all children are unnecessary
        nodesWithoutHash.remove(Integer.valueOf(leftChildIndex));
        nodesWithoutHash.remove(Integer.valueOf(rightChildIndex));
      } else if (bothChildrenHaveNoHash) {
        // the current node is sufficient if both children do not have hashes i.e. they are present
        // in the set so they are unnecessary
        nodesWithoutHash.remove(Integer.valueOf(leftChildIndex));
        nodesWithoutHash.remove(Integer.valueOf(rightChildIndex));
      } else {
        // the current node has at least one child with a hash that means the current node must be
        // removed to ensure that no nodes with values or hashes are skipped
        nodesWithoutHash.remove(Integer.valueOf(currentNodeIndex));
      }

    }
    if (nodesWithoutHash.contains(0)) {
      // special case: empty tree
      // here result = [0], this behaviour is not intended, instead
      // a list with all leaf indices is returned
      nodesWithoutHash.remove(0);
      for (int i = lastInnerNodeIndex + 1; i <= Math.pow(2, calculateHeight() + 1) - 2; i++) {
        nodesWithoutHash.add(i);
      }
    }
    return nodesWithoutHash;
  }

  /** Returns the indices of the nodes that do not have a hash. */
  private Set<Integer> getIndicesWithoutHashes(int index) {
    final Set<Integer> missingNodes = new HashSet<Integer>();
    final MerkleNode<V> indexNode = depthFirstSearch(index);
    final int leftChildIndex = 2 * index + 1;
    final int rightChildIndex = 2 * index + 2;

    if (indexNode instanceof MerkleInnerNode) {
      missingNodes.addAll(getIndicesWithoutHashes(leftChildIndex));
      missingNodes.addAll(getIndicesWithoutHashes(rightChildIndex));
    }
    if (!indexNode.hasHash() || index == 0) {
      missingNodes.add(index);
    }
    return missingNodes;
  }

  /**
   * Searches and returns a node by a given index. The index follows a breadth-first traversal, the
   * actual search is done by a depth-first-search.
   */
  protected MerkleNode<V> depthFirstSearch(int index) {
    // the last index in the current tree
    double highestBfsIndex = Math.pow(2, calculateHeight() + 1) - 2;
    if (index == 0) {
      return root;
    } else if (index > highestBfsIndex) {
      throw new IndexOutOfBoundsException();
    }
    ArrayDeque<Integer> pathToNode = findPath(index);
    MerkleNode<V> currentParent = root;
    int childIndex = pathToNode.pollLast();

    while (childIndex < index) {
      // invariant: children of currentParent are an instance of MerkleInnerNode
      if ((childIndex % 2) == 0) {
        // right childs always have an even index
        currentParent = ((MerkleInnerNode<V>) currentParent).getRight();
      } else {
        // left childs always have an odd index
        currentParent = ((MerkleInnerNode<V>) currentParent).getLeft();
      }
      childIndex = pathToNode.pollLast();
    }

    if ((index % 2) == 0) {
      return ((MerkleInnerNode<V>) currentParent).getRight();
    } else {
      return ((MerkleInnerNode<V>) currentParent).getLeft();
    }
  }

  /**
   * Helper method to search a node by index. This is a purely mathematical approach and does not
   * work on the current tree. Calculates the path to a node by an index calculation and stores it
   * in a deque.
   * 
   * <p>Note: As this method does not work on the current tree it will return paths to nodes that do
   * not exist if the index is not given properly. Each method must handle this exception before
   * calling this method.
   */
  private static ArrayDeque<Integer> findPath(int index) {
    ArrayDeque<Integer> pathToNode = new ArrayDeque<Integer>();
    while (index > 0) {
      pathToNode.add(index);
      // calculation to get index of current node:
      // currentIndex = (2 * parentIndex) + 1
      // so parentIndex is calculated by:
      // parentIndex = (currentIndex - 1) / 2
      index = (index - 1) / 2;
    }
    return pathToNode;
  }

  @Override
  public String toString() {
    return root.toString();
  }

  /** Returns the root of the tree. */
  protected MerkleInnerNode<V> getRoot() {
    return root;
  }

  private int calculateHeight() {
    MerkleNode<V> current = root;
    int height = 0;
    while (current instanceof MerkleInnerNode) {
      height++;
      current = ((MerkleInnerNode<V>) current).getRight();
    }
    return height;
  }

  private static int calculateLastInnerNodeIndex(int height) {
    return (int) (Math.pow(2, height + 1) - 4) / 2;
  }
}
