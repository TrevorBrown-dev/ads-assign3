public class BST<E> {
    public static void main(String[] args) {
        BTNode<Integer> root = new BTNode(5);
        BTNode<Integer> n1 = new BTNode(1);
        BTNode<Integer> n2 = new BTNode(3);
        BTNode<Integer> n3 = new BTNode(4);
        BTNode<Integer> n4 = new BTNode(4);

        root.setLeft(n1);
        root.setRight(n2);
        n2.setLeft(n3);
        n3.setLeft(n4);

        BST<Integer> tree = new BST<>(root);

        System.out.println(tree.getHeight(tree.getRoot()));

    }

    BTNode<E> root;

    public BST() {
        root = null;
    }

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

    public String inOrder(boolean parenthesized) {
        StringBuilder sb = new StringBuilder();
        inOrder(sb, root, parenthesized);
        return sb.toString();
    }

    private void inOrder(StringBuilder sb, BTNode<E> root, boolean parenthesized) {

        if (root == null)
            return;

        if (parenthesized)
            sb.append("( ");

        inOrder(sb, root.getLeft(), parenthesized);
        sb.append(root.toString() + " ");
        inOrder(sb, root.getRight(), parenthesized);

        if (parenthesized)
            sb.append(") ");
    }

    // TODO: implement printing the tree here.
    private void printTree(BTNode<E> root, int space) {
        int COUNT = 5;
        // Base case
        if (root == null)
            return;

        // Increase distance between levels
        space += COUNT;

        // Process right child first
        printTree(root.getRight(), space);

        // Print current node after space
        // count
        System.out.print("\n");
        for (int i = COUNT; i < space; i++)
            System.out.print(" ");
        System.out.print(root.getData() + "\n");

        // Process left child
        printTree(root.getLeft(), space);

    }

    public void printTree() {
        printTree(this.root, 0);
    }

    public int getHeight(BTNode<E> node) {
        return (node == null) ? -1 : 1 + Math.max(getHeight(node.getLeft()), getHeight(node.getRight()));
    }

    public int getDepth(BTNode<E> root) {
        return getDepth(root, getHeight(this.root));
    }

    private int getDepth(BTNode<E> root, int height) {
        return height - getHeight(root);
    }

    @Override
    public String toString() {
        return inOrder(false);
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
    public String toString() {
        return data.toString();
    }

}
