package pl.coderslab;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println(ConsoleColors.GREEN_BOLD + "Hello" + ConsoleColors.RESET);
        String[][] csvFile = loadData();
        boolean onOff = true;
        while (onOff) {
            showMenu();
            Scanner scanner = new Scanner(System.in);
            String user = scanner.nextLine();
            System.out.println();
            switch (user) {
                case "add" -> csvFile = addTask(csvFile);
                case "remove" -> csvFile = removeTask(csvFile);
                case "list" -> listFile(csvFile);
                case "exit" -> onOff = exitMenu(csvFile);
                default -> System.out.println("Please insert a correct option.");
            }
        }
    }

    static String[][] loadData() {
        Path path = Paths.get("tasks.csv");
        StringBuilder stringBuilder = new StringBuilder();
        int counter = 0;
        try (Scanner scanner = new Scanner(path)) {
            while (scanner.hasNextLine()) {
                stringBuilder.append(scanner.nextLine()).append("/");
                counter++;
            }
        } catch (IOException e) {
            System.out.println("File don't exist!");
        }
        String textLine = String.valueOf(stringBuilder);
        String[] textArray = textLine.split("/");
        String[][] arraysText = new String[counter][textArray[0].split(",").length];
        for (int x = 0; x < counter; x++) {
            arraysText[x] = textArray[x].split(",");
        }
        return arraysText;
    }

    static void listFile(String[][] csvFile) {
        System.out.println("\n" + ConsoleColors.CYAN + "List:" + ConsoleColors.RESET);
        for (int x = 0; x < csvFile.length; x++) {
            System.out.print(x + " : ");
            for (int i = 0; i < csvFile[x].length; i++) {
                if (i == 2) {
                    System.out.print(ConsoleColors.RED + csvFile[x][i] + ConsoleColors.RESET);
                } else
                    System.out.print(csvFile[x][i]);
            }
            System.out.println();
        }
    }

    static void showMenu() {
        System.out.println("\n" + ConsoleColors.CYAN + "Please select an option: \n" + ConsoleColors.RESET +
                "1. add \n" +
                "2. remove \n" +
                "3. list \n" +
                ConsoleColors.BLUE + "4. exit");
        System.out.print(ConsoleColors.YELLOW_BOLD + "INSERT: " + ConsoleColors.RESET);
    }

    static String[][] addTask(String[][] csvFile) {
        String[][] updated = Arrays.copyOf(csvFile, csvFile.length + 1);
        Scanner scanner = new Scanner(System.in);
        String[] user = new String[csvFile[0].length];
        for (int x = 0; x < user.length; x++) {
            if (x == 0) {
                System.out.print("Please add task description: ");
                user[x] = scanner.nextLine();
            }
            if (x == 1) {
                System.out.print("Please add task due date (yyyy-mm-dd): ");
                user[x] = " " + scanner.nextLine();
            }
            if (x == 2) {
                System.out.print("Is your task important" + ConsoleColors.RED + " true" + ConsoleColors.RESET + "/" + ConsoleColors.RED + "false" + ConsoleColors.RESET + " : ");
                user[x] = " " + scanner.nextLine();
            }
        }
        updated[csvFile.length] = user;
        return updated;
    }

    static String[][] removeTask(String[][] csvFile) {
        String[][] updated = new String[csvFile.length - 1][];
        Scanner scanner = new Scanner(System.in);
        boolean correct = false;
        int user = 0;
        while (!correct) {
            System.out.print("Please select number to remove: ");
            user = scanner.nextInt();
            System.out.println();
            if (user >= 0 && user < csvFile.length) {
                correct = true;
            } else System.out.println("Inserted number is incorrect.\n");
        }
        String[] message = csvFile[user];
        correct = false;
        while (!correct) {
            System.out.print("The following data " + Arrays.toString(message) + " will be deleted, please confirm" + ConsoleColors.RED + " true" + ConsoleColors.RESET + "/" + ConsoleColors.RED + "false" + ConsoleColors.RESET + " : ");
            scanner = new Scanner(System.in);
            String confirmation = scanner.nextLine();
            switch (confirmation) {
                case "true" -> {
                    for (int x = 0, i = 0; x < updated.length; x++) {
                        if (i == user) {
                            i++;
                        }
                        updated[x] = csvFile[i];
                        i++;
                    }
                    correct = true;
                }
                case "false" -> {
                    updated = csvFile;
                    correct = true;
                }
                default -> System.out.println("Please insert a correct option.\n"); ///
            }
        }
        return updated;
    }

    static boolean exitMenu(String[][] csvFile) {
        File oldFile = new File("tasks.csv");
        if (oldFile.delete()) {
            File newFile = new File("tasks.csv");
            StringBuilder stringBuilder = new StringBuilder();
            for (int x = 0; x < csvFile.length; x++) {
                for (int i = 0; i < csvFile[x].length; i++) {
                    if (i == csvFile[x].length - 1) {
                        if (x == csvFile.length - 1) {
                            stringBuilder.append(csvFile[x][i]);
                        } else
                            stringBuilder.append(csvFile[x][i]).append("\n");
                    } else stringBuilder.append(csvFile[x][i]).append(",");
                }
            }
            stringBuilder.trimToSize();
            try (PrintWriter printWriter = new PrintWriter(newFile)) {
                printWriter.print(stringBuilder);
            } catch (FileNotFoundException e) {
                System.out.println("Data saving failed !");
            }
        }
        System.out.println(ConsoleColors.RED_BOLD + "Bye Bye" + ConsoleColors.RESET);
        return false;
    }
}

