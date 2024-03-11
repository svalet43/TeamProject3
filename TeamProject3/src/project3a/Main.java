package project3a;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        // Check if the input file path is provided
       

        try (BufferedReader reader = new BufferedReader(new FileReader("inputFile.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                InfixExpressionParser parser = new InfixExpressionParser(line);
                Node root = parser.parse();
                if (root != null) {
                    int result = parser.evaluate(root);
                    System.out.println("Expression: " + line + " Result: " + result);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
