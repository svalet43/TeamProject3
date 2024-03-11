package project3a;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
    	InfixExpressionParser infix;
		String line;
		
        try (BufferedReader reader = new BufferedReader(new FileReader("C:\\\\Users\\\\Mason\\\\Documents\\\\GitHub\\\\Team_Project_3\\\\Team_Project_3\\\\inputFile.txt"))) {
            while ((line = reader.readLine()) != null) {infix = new InfixExpressionParser(line);
            infix.parse();
            //System.out.println("Expression: " + line + " Result: " + result);
            }
        } 
        catch (FileNotFoundException e) {System.out.println("Error: File not found.");}
        catch (IOException e) {System.out.println("Error reading file.");}
	}


       /* for (String expression : expressions) {
            InfixExpressionParser parser = new InfixExpressionParser(expression);
            Node root = parser.parse();
            if (root != null) {
                int result = parser.evaluate(root);
                System.out.println("Expression: " + expression + " Result: " + result);
            }
        }
        */
}
