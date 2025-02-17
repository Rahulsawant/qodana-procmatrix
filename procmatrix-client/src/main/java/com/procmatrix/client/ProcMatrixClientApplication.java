package com.procmatrix.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class ProcMatrixClientApplication implements CommandLineRunner {

    private final MatrixHandler matrixHandler;

    @Autowired
    public ProcMatrixClientApplication(MatrixHandler matrixHandler) {
        this.matrixHandler = matrixHandler;
    }

    public static void main(String[] args) {
        SpringApplication.run(ProcMatrixClientApplication.class, args);
    }

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Choose an API to invoke:");
            System.out.println("1. Get Matrix by ID");
            System.out.println("2. Save Matrix");
            System.out.println("3. Delete Matrix by ID");
            System.out.println("4. Add Matrices by IDs");
            System.out.println("5. Add Matrices from Body");
            System.out.println("6. Rotate Matrix by ID");
            System.out.println("7. Rotate Matrix from Body");
            System.out.println("8. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    matrixHandler.handleGetMatrix(scanner);
                    break;
                case 2:
                    matrixHandler.handleSaveMatrix(scanner);
                    break;
                case 3:
                    matrixHandler.handleDeleteMatrix(scanner);
                    break;
                case 4:
                    matrixHandler.handleAddMatrices(scanner);
                    break;
                case 5:
                    matrixHandler.handleAddMatricesFromBody(scanner);
                    break;
                case 6:
                    matrixHandler.handleRotateMatrixById(scanner);
                    break;
                case 7:
                    matrixHandler.handleRotateMatrixFromBody(scanner);
                    break;
                case 8:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}