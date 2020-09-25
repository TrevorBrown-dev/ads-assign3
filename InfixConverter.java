import java.util.Stack;

public class InfixConverter {
    public static void main(String[] args) {
        InfixConverter c = new InfixConverter("2 ^ 2 ^ 3 $");

    }

    private String[] tokens;
    private Stack<String> operatorStack;

    public InfixConverter(String expression) {
        expression = expression.trim();
        expression = expression.replaceAll(" +", " ");
        tokens = expression.split(" +");
        operatorStack = new Stack<String>();
        Convert();
    }

    // #region Helper Functions
    // This function will ensure a consistent way of adding tokens to the output
    // string
    public void appendToken(StringBuilder expression, String token) {
        expression.append(token + ' ');
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
        // TODO: there should be some edge cases in here for right to left
        // assiciativity. Or maybe that goes somewhere else
        if (currentToken.equals("!") || currentToken.equals("^")) {
            if (currentToken.equals(stackToken)) {
                // This is an operation with right to left associativity
                return true;
            }
        }
        return (precedence(currentToken) > precedence(stackToken));

    }

    // #endregion
    public void Convert() {

        StringBuilder expression = new StringBuilder();

        for (String token : tokens) {
            if (token.matches("-?\\d+")) {
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
        System.out.println(expression);
    }

}