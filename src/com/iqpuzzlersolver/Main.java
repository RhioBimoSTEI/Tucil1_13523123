package com.iqpuzzlersolver;

import java.io.*;

public class Main {
    public static void main(String[] args) {
        String inputFile = "test/test.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            // Line 1: N M P
                     
            // Line 2: Puzzle type (S)

            // Line 3++: Puzzle Pieces

            // Solver

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
