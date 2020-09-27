import java.util.Stack;

public class ExpressionTree {
    public static void main(String[] args) {

    }

    private BST<String> tree;

    public ExpressionTree() {
        tree = new BST<String>();
    }

    public void buildTree(String expression) {
        // main logic for method goes here.
        String[] tokens = expression.trim().split(" +");
        System.out.println(tokens);

        Stack<BTNode<String>> nodeStack = new Stack<>();
        // Always push numbers
    }
}
