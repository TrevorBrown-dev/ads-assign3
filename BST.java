public class BST<E> {
    BTNode<E> root;

    public BST(E data) {
        root = new BTNode<E>(data);
    }

    public BST(BTNode<E> root) {
        this.root = root;
    }

    public BTNode<E> getRoot() {
        return root;
    }

    public void setRoot(BTNode<E> root) {
        this.root = root;
    }
}

class BTNode<E> {

    private E data;
    private BTNode<E> left;
    private BTNode<E> right;

    public BTNode(E data) {
        this.data = data;
    }

    // #region Getters and Setters
    public E getData() {
        return data;
    }

    public BTNode<E> getLeft() {
        return left;
    }

    public BTNode<E> getRight() {
        return right;
    }

    public void setData(E data) {
        this.data = data;
    }

    public void setLeft(BTNode<E> left) {
        this.left = left;
    }

    public void setRight(BTNode<E> right) {
        this.right = right;
    }
    // #endregion
}
