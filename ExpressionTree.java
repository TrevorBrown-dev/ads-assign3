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

        Stack<BTNode<String>> tokenStack = new Stack<>();

        for (String token : tokens) {
            if (isDigit(token)) {
                tokenStack.push(new BTNode<String>(token));
            } else if (isUnaryOperator(token)) {
                BTNode<String> root = new BTNode<>(token);
                root.setLeft(tokenStack.pop());
                tokenStack.push(root);
            } else {
                BTNode<String> root = new BTNode<>(token);
                root.setRight(tokenStack.pop());
                root.setLeft(tokenStack.pop());
                tokenStack.push(root);
            }
        }
        // Always push numbers
    }

    public boolean isDigit(String token) {
        return token.matches("-?\\d+");
    }

    public boolean isUnaryOperator(String token) {
        return token.matches("[\\!\\^]");
    }
}
