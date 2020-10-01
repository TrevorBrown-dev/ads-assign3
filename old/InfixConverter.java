package old;

import java.util.Stack;

public class InfixConverter {

    public static void eval(String expression) {
        // Convert to postfix
        InfixConverter c = new InfixConverter(expression);

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
    }

    public static void main(String[] args) {
        // TODO: implement driver class that takes user input
        eval(" ( 2 + 4 ) * 3 $");
    }

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