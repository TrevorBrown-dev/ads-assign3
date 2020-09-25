import java.util.Stack;

public class InfixConverter {
    public static void main(String[] args) {
        InfixConverter c = new InfixConverter(" a b c   d   e  fd 2");

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
                    // right to left ops
                    case "!":
                    case "^":
                        // left to right ops
                    case "*":
                    case "/":
                    case "%":
                    case "+":
                    case "-":
                    case "<":
                    case "<=":
                    case ">":
                    case ">=":
                    case "==":
                    case "!=":
                    case "&&":
                    case "||":
                        // compare token on stack with next token
                        // if token on stack has higher precedence append the next token
                        // else pop the token on the stack, append it, then push next token
                }
            }

        }

        System.out.println(getLastToken(expression));
    }

}