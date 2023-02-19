package pl.coderslab;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String userInput;
        do{
            System.out.println(ConsoleColors.BLUE_BOLD + "Hello! Please select an option" + ConsoleColors.RESET);
            menu();
            userInput = scan.nextLine();
            switch(userInput){
                case "remove":
                    System.out.println(ConsoleColors.GREEN +"Which task would you like to delete? (index)"+ConsoleColors.RESET);
                    int index = scan.nextInt();
                    remove(index);
                    break;
                case "list":
                    list("tasks.csv");
                    break;
                case "add":
                    add(fileReader("tasks.csv"));
                    break;
            }
        } while (!userInput.equals("exit"));
    }

    public static void menu() {
        String[] menu = {"list", "add", "remove", "exit"};
        for (String option : menu) {
            System.out.println(ConsoleColors.YELLOW + option + ConsoleColors.RESET);
        }
    }

    public static String[][] fileReader(String str){
        Path path = Paths.get(str);
        File file = new File(str);
        long rows = 0;
        int data = 0;
        try(Scanner scan = new Scanner(file)){
            rows = Files.lines(path).count();
            String[][] tasks = new String[(int) rows][3];
            while(scan.hasNextLine()){
                tasks[data] = scan.nextLine().trim().split(",");
                data++;
            }
            return tasks;
        }catch(IOException e){
            System.err.println("File not found");
        }
        return null;
    }

    public static void add(String[][] tasksBefore){
        System.out.println(ConsoleColors.BLUE_BOLD + "Enter task describtion" + ConsoleColors.RESET);
        Scanner scan = new Scanner(System.in);
        String taskDescribtion = scan.nextLine();
        System.out.println(ConsoleColors.BLUE_BOLD + "Enter task due date (YYYY-MM-DD)" + ConsoleColors.RESET);
        String dueDate = scan.next();
        System.out.println(ConsoleColors.BLUE_BOLD + "Is this task important? true/false" + ConsoleColors.RESET);
        String importance = scan.next();
        tasksBefore = fileReader("tasks.csv");
        String[] newTask = new String [3];
        newTask[0] = taskDescribtion;
        newTask[1] = dueDate;
        newTask[2] = importance;
        List<String> outList = new ArrayList<>();
        for(String[] elements : tasksBefore){
            outList.add(elements[0] + ", " + elements[1] + ", " + elements[2]);
        }
        Path path = Paths.get("tasks.csv");
        outList.add(newTask[0] + ", " + newTask[1] + ", " + newTask[2]);
        try{
            Files.write(path, outList);
        }
        catch(IOException e){
            System.err.println("File not found");
        }

    }

    public static void list(String str){
        Path path = Paths.get(str);
        String fileToString = "";
        try{
            fileToString = Files.readString(path);
        }
        catch(IOException e){
            System.err.println("File not found");
        }
        System.out.println(fileToString);
    }

    public static void remove(int index){
        Path path = Paths.get("tasks.csv");
        String[][] before = fileReader("tasks.csv");
        String[][] after;
        List<String> list = new ArrayList<>();
        for(int i = 0; i < before.length - 1; i++){
            if(i != index){
                after = Arrays.copyOf(before, before.length - 1);
                list.add(after[i][0] + ", " + after[i][1] + ", " + after[i][2]);
            }
        }
        try{
            Files.write(path, list);
        }
        catch(IOException e){
            System.err.println("File not found");
        }
    }
}