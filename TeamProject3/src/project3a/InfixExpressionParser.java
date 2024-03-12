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
		this.expression = expression.replaceAll("\\s+", ""); //remove spaces in input
		nodeStack = new Stack<>(); //Initialize stacks
		operatorStack = new Stack<>();
	}

	/**
	 * Parses the infix expression to postfix expression, calls build tree
	 */
	public Node parse() {
		StringBuilder postfix = new StringBuilder();
		for (int i = 0; i < expression.length(); i++) {//iterate expressions length
			char c = expression.charAt(i); //current character
			if (Character.isDigit(c)) { //if digit
				while (i < expression.length() && (Character.isDigit(expression.charAt(i)))) { //iterate until non-digit
					postfix.append(expression.charAt(i));
					i++;
				}
				i--; //correct i
				postfix.append(" ");
			} 
			else if (c == '(') {operatorStack.push(new Operator("("));} //if opening parenthesis, push to operatorStack
			else if (c == ')') { //if closing parentheses
				while (!operatorStack.isEmpty() && !operatorStack.peek().symbol.equals("(")) { //iterate until opening parentheses
					postfix.append(operatorStack.pop().symbol).append(" ");
				}
				if (operatorStack.isEmpty()) { // unbalanced parentheses case 
					System.out.println("Error: Mismatched parentheses");
					return null;
				}
				operatorStack.pop(); // Pop the '('
			} 
			else if (isOperator(String.valueOf(c))) { //if operator
				Operator currentOperator = new Operator(String.valueOf(c));
				//handle double operators
				if (i + 1 < expression.length() && isOperator(expression.substring(i + 1, i + 2))) {
					//if not end of expression and is double operator
					String doubleOperator = expression.substring(i, i + 2); //create multi-character operator
					currentOperator = new Operator(doubleOperator); 
					i++; // skip next character 
				}
				operatorStack.push(currentOperator); //push operator to stack
			} 
			else { //invalid character case
				System.out.println("Error: Invalid character in expression");
				return null;
			}
		}

		while (!operatorStack.isEmpty()) {postfix.append(operatorStack.pop().symbol).append(" ");} //finish parse
		return buildTree(postfix.toString().trim());
	}


	/**
	 * Checks if the given string represents an operator.
	 * @param op: the string to be checked
	 * @return: true if the string represents an operator, false otherwise
	 */
	private boolean isOperator(String op) {return op.matches("[+\\-*/%^><=!|&]{1,2}");}

	/**
	 * Builds a binary expression tree from the given postfix expression.
	 * @param postfixExpression: the postfix expression to build the tree from
	 * @return: the root node of the constructed expression tree, or null if there was an error during construction
	 */
	public Node buildTree(String postfixExpression) {
		String[] tokens = postfixExpression.split("\\s"); //creates string array of tokens, broken up by using space delimiter
		for (String token : tokens) {
			if (isOperator(token)) { //if operator
				if (nodeStack.size() < 2) { //missing operand case
					System.out.println("Error: Invalid expression - Not enough operands for operator");
					return null;
				}
				Node right = nodeStack.pop(); //pull right and left nodes
				Node left = nodeStack.pop();
				if (token.length() == 2) {token = token.substring(0, 1) + token.substring(1, 2);} //if double operator, combine characters
				Node newNode = new Node(token); //connect operands to operators
				newNode.left = left;
				newNode.right = right;
				nodeStack.push(newNode);
			} 
			else {nodeStack.push(new Node(token));} //otherwise, push token to node stack
		}
		if (nodeStack.size() != 1) {//missing operand/operator case
			System.out.println("Error: Invalid expression - Incorrect number of operands or operators");
			return null;
		}
		return nodeStack.pop(); //return most recent node
	}

	/**
	 * Evaluates the expression represented by the given expression tree root.
	 * @param root: the root node of the expression tree
	 * @return: the result of evaluating the expression, or 0 if there was an error during evaluation
	 */
	public int evaluate(Node root) {
		if (root == null) { //if root is null, invalid expression
			System.out.println("Error: Invalid expression");
			return 0;
		}
		//if left and right are not null, return int value of node
		if (root.left == null && root.right == null) {return Integer.parseInt(root.value);} 
		//if logical and, evaluate left and right nodes, then evaluate
		if (root.value.equals("&&")) {return (evaluate(root.left) != 0 && evaluate(root.right) != 0) ? 1 : 0;}
		//if logical or, evaluate left and right nodes, then evaluate
		if (root.value.equals("||")) {return (evaluate(root.left) != 0 || evaluate(root.right) != 0) ? 1 : 0;}
		//evaluate left and right values
		int leftValue = evaluate(root.left);
		int rightValue = evaluate(root.right);
		//switch statement for remaining operators
		switch (root.value) {
		case "+":
			return leftValue + rightValue;
		case "-":
			return leftValue - rightValue;
		case "*":
			return leftValue * rightValue;
		case "/":
			if (rightValue == 0) { //dived by 0 case
				System.out.println("Error: Division by zero");
				return 0;
			}
			return leftValue / rightValue;
		case "%":
			if (rightValue == 0) {//divide by 0 case 
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
