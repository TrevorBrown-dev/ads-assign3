import java.util.Scanner;
import java.util.Stack;
import java.util.Hashtable;

public class TrevorBrown {

    public static void eval(String expression) {
        // Convert to postfix
        InfixConverter c;
        try {
            c = new InfixConverter(expression);

            String infix = c.getExpression();
            String postfix = c.Convert();

            System.out.printf("Infix: %s\n", infix);
            System.out.printf("Postfix: %s\n", postfix);

            // build the tree.
            ExpressionTree tree = new ExpressionTree();

            // Print fully parenthesized
            System.out.println("Fully parenthesized expression: ");
            tree.buildTree(postfix);

            postfix += "$";
            // Print the answer
            ExpressionEvaluator e = new ExpressionEvaluator(postfix);
            e.evaluate();

            // Print the tree
            System.out.println("Tree diagram:\n\n");
            tree.printTree();
        } catch (Exception e) {
            System.out.println("\n\nERROR: Please enter a valid expression!\n\n");
        }

    }

    public static void main(String[] args) {

        Scanner kb = new Scanner(System.in);
        System.out.println("Welcome to the ultimate expression evaluator!\n"
                + "Please enter your expression in infix notation. and end the expression with a '$'\n"
                + "Ex: '( 2 + 5 ) * 3 ^ 5 ^ 5 $'\n" + "Type 'exit at any time to terminate the program!");

        String input = "";
        while (!input.toLowerCase().equals("exit")) {
            System.out.print("Input: ");
            input = kb.nextLine();

            if (!input.toLowerCase().equals("exit")) {
                // input = input.substring(0, input.indexOf("$") + 1);
                eval(input);
            }

        }
        System.out.println("Goodbye!");
    }
}

class ExpressionEvaluator {
    private String expression;

    public ExpressionEvaluator() {
        this("");
    }

    public ExpressionEvaluator(String expression) {
        this.expression = expression;
    }

    // #region Accessors/Mutators
    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }
    // #endregion

    public void evaluate() {
        expression = (expression.indexOf("$") == -1) ? expression
                : expression.substring(0, expression.indexOf("$") + 1);
        System.out.println("The expression to be evaluated is: " + expression);
        String[] tokens = expression.split(" +");

        tokens = assignVariables(tokens);
        int result = calculate(tokens);

        System.out.println("The value of this expression is " + result + ".\n");
    }

    private long factorial(long n) {
        if (n <= 1)
            return 1;
        else
            return n * factorial(n - 1);
    }

    private Integer calculate(String[] expArr) throws IllegalArgumentException {
        Stack<Integer> operandStack = new Stack<>();
        for (String s : expArr) {
            if (s.matches("-?\\d+"))
                operandStack.push(Integer.parseInt(s)); // push numbers to the stack
            else // else: expecting operator
            {
                // directional operations need pop's called out of order
                // it is efficient to pop now instead of calling this for every directional
                // operation
                int temp = operandStack.pop();
                if (operandStack.isEmpty() && s.equals("$"))
                    return temp; // calculation worked
                switch (s) {
                    case "+":
                        if (operandStack.isEmpty())
                            throw new IllegalArgumentException("unexpected operator(s)");
                        operandStack.push(operandStack.pop() + temp);
                        break;
                    case "-":
                        if (operandStack.isEmpty())
                            throw new IllegalArgumentException("unexpected operator(s)");
                        operandStack.push(operandStack.pop() - temp);
                        break;
                    case "*":
                        if (operandStack.isEmpty())
                            throw new IllegalArgumentException("unexpected operator(s)");
                        operandStack.push(operandStack.pop() * temp);
                        break;
                    case "/":
                        if (operandStack.isEmpty())
                            throw new IllegalArgumentException("unexpected operator(s)");
                        if (temp == 0)
                            throw new IllegalArgumentException("division by 0");
                        operandStack.push(operandStack.pop() / temp);
                        break;
                    case "^":
                        if (operandStack.isEmpty())
                            throw new IllegalArgumentException("unexpected operator(s)");
                        operandStack.push((int) Math.pow(operandStack.pop(), temp));
                        break;
                    case "_":
                        operandStack.push(-temp);
                        break;
                    case "#":
                        if (temp < 0)
                            throw new IllegalArgumentException("taking sqrt of negative number");
                        operandStack.push((int) Math.sqrt(temp));
                        break;
                    case "!":
                        operandStack.push((int) factorial(temp));
                        break;
                    case "<":
                        operandStack.push((operandStack.pop() < temp) ? 1 : 0);
                        break;
                    case "<=":
                        operandStack.push((operandStack.pop() <= temp) ? 1 : 0);
                        break;
                    case ">":
                        operandStack.push((operandStack.pop() > temp) ? 1 : 0);
                        break;
                    case ">=":
                        operandStack.push((operandStack.pop() >= temp) ? 1 : 0);
                        break;
                    case "==":
                        operandStack.push((operandStack.pop() == temp) ? 1 : 0);
                        break;
                    case "!=":
                        operandStack.push((operandStack.pop() != temp) ? 1 : 0);
                        break;
                    case "&&":
                        operandStack.push((operandStack.pop() != 0 && temp != 0) ? 1 : 0);
                        break;
                    case "||":
                        operandStack.push((operandStack.pop() != 0 || temp != 0) ? 1 : 0);
                        break;
                    case "$":
                        throw new IllegalArgumentException("end of expression reached with unexpected operand(s)");
                    default:
                        throw new IllegalArgumentException("unsupported operation: " + s);
                }
            }
        }
        return Integer.MIN_VALUE;
    }

    // #region Initialization Methods
    private String setValue(String token) {
        Scanner kb = new Scanner(System.in);
        String input = "";

        while (!input.matches("-?\\d+")) {
            System.out.print("Enter the value of " + token + " > ");
            input = kb.nextLine();

            if (!input.matches("-?\\d+")) {
                System.out.println("ERROR: Please enter an integer!");
            }
        }
        token = input;

        return token;
    }

    private String[] assignVariables(String[] tokens) {
        Hashtable<String, String> variables = new Hashtable<String, String>();

        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i].matches("[a-zA-Z]+")) {
                if (variables.containsKey(tokens[i])) {
                    tokens[i] = variables.get(tokens[i]);
                } else {
                    String input = setValue(tokens[i]);
                    variables.put(tokens[i], input);
                    tokens[i] = input;
                }
            }
        }

        return tokens;
    }
    // #endregion

    @Override
    public String toString() {
        return expression;
    }
}

class BST<E> {
    public static void main(String[] args) {
        // Testing the tree
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

class ExpressionTree {
    public static void main(String[] args) {
        ExpressionTree tree = new ExpressionTree();
        tree.buildTree("2 4 + -3.5 *");
    }

    private BST<String> tree;

    public ExpressionTree() {
        tree = new BST<String>();
    }

    public void printTree() {
        this.tree.printTree();
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

class InfixConverter {

    private String[] tokens;
    private Stack<String> operatorStack;
    private String expression;

    public InfixConverter(String expression) {
        expression = expression.trim();
        this.expression = expression.replaceAll(" +", " ");
        tokens = this.expression.split(" +");
        operatorStack = new Stack<String>();
    }

    // #region Helper Functions
    // This function will ensure a consistent way of adding tokens to the output
    // string
    public void appendToken(StringBuilder expression, String token) {
        expression.append(token + ' ');
    }

    public String getExpression() {
        return expression.substring(0, expression.length() - 2);
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getLastToken(StringBuilder expression) {
        String[] expr = expression.toString().trim().split(" ");
        return expr[expr.length - 1];
    }

    public int precedence(String token) {
        switch (token) {
            // right to left ops
            case "!":
            case "^":
                return 7;
            // left to right ops
            case "*":
            case "/":
                return 6;
            case "%":
            case "+":
            case "-":
                return 5;
            case "<":
            case "<=":
            case ">":
            case ">=":
                return 4;
            case "==":
            case "!=":
                return 3;
            case "&&":
                return 2;
            case "||":
                return 1;
            case "(":
                return 0;
            // compare token on stack with next token
            // if token on stack has higher precedence append the next token
            // else pop the token on the stack, append it, then push next token
            default:
                System.out.printf("Unsupported Token: \"%s\"\n", token);
                return -1;
        }
    }

    public boolean comparePrecedence(String currentToken, String stackToken) {
        // Returns true if the current token has higher precedence than the token on the
        // stack and false if it does not
        if (Utils.isUnaryOperator(currentToken)) {
            if (currentToken.equals(stackToken)) {
                // This is an operation with right to left associativity
                return true;
            }
        }
        return (precedence(currentToken) > precedence(stackToken));

    }

    // #endregion
    public String Convert() {
        StringBuilder expression = new StringBuilder();

        for (String token : tokens) {
            if (Utils.isDigit(token)) {
                // We always append digits to the string.
                appendToken(expression, token);
            } else {
                // This must be an operator OR an unsupported token.
                switch (token) {
                    case "(":
                        operatorStack.push(token);
                        break;
                    case ")":
                        while (!operatorStack.peek().equals("(")) {
                            appendToken(expression, operatorStack.pop());
                        }
                        operatorStack.pop();
                        break;
                    case "$":
                        while (!operatorStack.isEmpty()) {
                            appendToken(expression, operatorStack.pop());
                        }
                        break;
                    default:
                        if (operatorStack.isEmpty())
                            operatorStack.push(token);
                        else if (comparePrecedence(token, operatorStack.peek())) {
                            // The current token has higher precedence, push it.
                            operatorStack.push(token);
                        } else {
                            // The curent token has lower or equal precedence
                            while (!operatorStack.isEmpty() && !comparePrecedence(token, operatorStack.peek())) {
                                appendToken(expression, operatorStack.pop());
                            }
                        }
                }
            }
        }
        return expression.toString();
    }

}