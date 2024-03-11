package project3a;

class Operator {
    String symbol;
    int precedence;

    /**
     * Constructs an Operator object with the given symbol and assigns its precedence based on the symbol.
     * @param symbol the symbol representing the operator
     */
    public Operator(String symbol) {
        this.symbol = symbol;
        this.precedence = getPrecedence(symbol);
    }

    /**
     * Determines the precedence of an operator based on its symbol.
     * @param symbol the symbol of the operator
     * @return the precedence value of the operator
     */
    private int getPrecedence(String symbol) {
        switch (symbol) {
            case "^":
                return 7;
            case "*":
            case "/":
            case "%":
                return 6;
            case "+":
            case "-":
                return 5;
            case ">":
            case ">=":
            case "<":
            case "<=":
                return 4;
            case "==":
            case "!=":
                return 3; 
            case "&&":
                return 2; 
            case "||":
                return 1; 
            default:
                return 0;
        }
    }
}

