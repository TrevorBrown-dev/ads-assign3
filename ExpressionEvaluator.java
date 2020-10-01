import java.util.Scanner;
import java.util.Stack;
import java.util.Hashtable;

public class ExpressionEvaluator {
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