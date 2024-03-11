package project3a;

import java.util.Stack;

class InfixExpressionParser {
    String expression;
    Stack<Node> nodeStack;
    Stack<Operator> operatorStack;

    /**
     * Constructs an InfixExpressionParser object with the given expression.
     * @param expression the infix expression to be parsed
     */
    public InfixExpressionParser(String expression) {
        this.expression = expression.replaceAll("\\s+", "");
        nodeStack = new Stack<>();
        operatorStack = new Stack<>();
    }

    /**
     * Parses the infix expression and constructs the corresponding expression tree.
     * @return the root node of the expression tree if parsing is successful, otherwise null
     */
    public Node parse() {
        StringBuilder postfix = new StringBuilder();
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if (Character.isDigit(c)) {
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    postfix.append(expression.charAt(i));
                    i++;
                }
                i--;
                postfix.append(" ");
            } else if (c == '(') {
                operatorStack.push(new Operator("("));
            } else if (c == ')') {
                while (!operatorStack.isEmpty() && !operatorStack.peek().symbol.equals("(")) {
                    postfix.append(operatorStack.pop().symbol).append(" ");
                }
                if (operatorStack.isEmpty()) {
                    System.out.println("Error: Mismatched parentheses");
                    return null;
                }
                operatorStack.pop(); // Pop the '('
            } else if (isOperator(String.valueOf(c))) {
                Operator currentOperator = new Operator(String.valueOf(c));
                if (currentOperator.symbol.equals("&")) {
                    currentOperator = new Operator("&&");
                } else if (currentOperator.symbol.equals("|")) {
                    currentOperator = new Operator("||");
                }
                while (!operatorStack.isEmpty() && !operatorStack.peek().symbol.equals("(") && currentOperator.precedence <= operatorStack.peek().precedence) {
                    postfix.append(operatorStack.pop().symbol).append(" ");
                }
                operatorStack.push(currentOperator);
            } else if (c == '=') {
                // Handle equality comparison
                if (i + 1 < expression.length() && expression.charAt(i + 1) == '=') {
                    while (!operatorStack.isEmpty() && !operatorStack.peek().symbol.equals("(") && operatorStack.peek().precedence >= 3) {
                        postfix.append(operatorStack.pop().symbol).append(" ");
                    }
                    operatorStack.push(new Operator("=="));
                    i++; // Skip next character since it's already processed
                } else {
                    System.out.println("Error: Invalid expression");
                    return null;
                }
            } else if (c == '!') {
                if (i + 1 < expression.length() && expression.charAt(i + 1) == '=') {
                    postfix.append("!=").append(" ");
                    i++; // Skip next character since it's already processed
                } else {
                    System.out.println("Error: Invalid expression");
                    return null;
                }
            } else {
                System.out.println("Error: Invalid character in expression");
                return null;
            }
        }
        while (!operatorStack.isEmpty()) {
            postfix.append(operatorStack.pop().symbol).append(" ");
        }
        return buildTree(postfix.toString().trim());
    }
    
    /**
     * Checks if the given string represents an operator.
     * @param op the string to be checked
     * @return true if the string represents an operator, false otherwise
     */
    private boolean isOperator(String op) {
        return op.matches("[+\\-*/%^><=!|&]");
    }

    /**
     * Builds a binary expression tree from the given postfix expression.
     * @param postfixExpression the postfix expression to build the tree from
     * @return the root node of the constructed expression tree, or null if there was an error during construction
     */
    public Node buildTree(String postfixExpression) {
        String[] tokens = postfixExpression.split("\\s+");
        for (String token : tokens) {
            if (isOperator(token)) {
                if (nodeStack.size() < 2) {
                    System.out.println("Error: Invalid expression - Not enough operands for operator");
                    return null;
                }
                Node right = nodeStack.pop();
                Node left = nodeStack.pop();
                Node newNode = new Node(token);
                newNode.left = left;
                newNode.right = right;
                nodeStack.push(newNode);
            } else {
                nodeStack.push(new Node(token));
            }
        }
        if (nodeStack.size() != 1) {
            System.out.println("Error: Invalid expression - Incorrect number of operands or operators");
            return null;
        }
        return nodeStack.pop();
    }

    /**
     * Evaluates the expression represented by the given expression tree root.
     * @param root the root node of the expression tree
     * @return the result of evaluating the expression, or 0 if there was an error during evaluation
     */
    public int evaluate(Node root) {
        if (root == null) {
            System.out.println("Error: Invalid expression");
            return 0;
        }
        if (root.left == null && root.right == null) {
            return Integer.parseInt(root.value);
        }
        if (root.value.equals("&&")) {
            return (evaluate(root.left) != 0 && evaluate(root.right) != 0) ? 1 : 0;
        }
        if (root.value.equals("||")) {
            return (evaluate(root.left) != 0 || evaluate(root.right) != 0) ? 1 : 0;
        }
        int leftValue = evaluate(root.left);
        int rightValue = evaluate(root.right);
        switch (root.value) {
            case "+":
                return leftValue + rightValue;
            case "-":
                return leftValue - rightValue;
            case "*":
                return leftValue * rightValue;
            case "/":
                if (rightValue == 0) {
                    System.out.println("Error: Division by zero");
                    return 0;
                }
                return leftValue / rightValue;
            case "%":
                if (rightValue == 0) {
                    System.out.println("Error: Division by zero");
                    return 0;
                }
                return leftValue % rightValue;
            case "^":
                return (int) Math.pow(leftValue, rightValue);
            case "==":
                return leftValue == rightValue ? 1 : 0;
            case "!=":
                return leftValue != rightValue ? 1 : 0;
            case ">":
                return leftValue > rightValue ? 1 : 0;
            case ">=":
                return leftValue >= rightValue ? 1 : 0;
            case "<":
                return leftValue < rightValue ? 1 : 0;
            case "<=":
                return leftValue <= rightValue ? 1 : 0;
            default:
                System.out.println("Error: Invalid operator");
                return 0;
        }
    }
   
}
