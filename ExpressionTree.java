import java.util.Stack;

public class ExpressionTree {
    public static void main(String[] args) {
        ExpressionTree tree = new ExpressionTree();
        tree.buildTree("2 4 + -3.5 *");
    }

    private BST<String> tree;

    public ExpressionTree() {
        tree = new BST<String>();
    }

    public void buildTree(String expression) {
        // main logic for method goes here.
        String[] tokens = expression.trim().split(" +");
        // System.out.println(tokens);

        Stack<BTNode<String>> tokenStack = new Stack<>();

        for (String token : tokens) {
            // Turn the token into a node.
            BTNode<String> tokenNode = new BTNode<>(token);

            if (Utils.isDigit(token)) {
                // Always push digits to the stack.
                tokenStack.push(tokenNode);

            } else if (Utils.isUnaryOperator(token)) {
                tokenNode.setLeft(tokenStack.pop());
                tokenStack.push(tokenNode);
            } else {
                tokenNode.setRight(tokenStack.pop());
                tokenNode.setLeft(tokenStack.pop());
                tokenStack.push(tokenNode);
            }
        }
        // Construction is done, now make it a BST.
        tree.setRoot(tokenStack.pop());
        System.out.println(tree.inOrder(true));
        // System.out.println(tree.inOrder(false));
    }

}

class Utils {
    public static boolean isDigit(String token) {
        return token.matches("-?\\d+\\.?\\d*");
    }

    public static boolean isUnaryOperator(String token) {
        return token.matches("[\\!\\^]");
    }
}