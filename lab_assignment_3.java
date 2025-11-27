//ASSIGNMENT 3: Exception Handling, 
//Multithreading, and Wrapper Classes 

//Problem Statement: 

//Enhance the Student Management System by implementing exception handling and multithreading to ensure safe execution and responsiveness. The system should handle invalid 
//input (such as marks outside the valid range or empty fields) using try-catch-finally blocks andcustom exceptions like StudentNotFoundException. Additionally, the system should simulate a loading 
//process when adding or saving student data by using multithreading. The program should utilize wrapper classes (such as Integer, Double) for data conversion and autoboxing where applicable, providing a robust 
//and responsive user interface for managing student records.   

// CODE :- 

// Assignment 3: Advanced Student System (Exceptions, Multithreading, Wrappers)
// Name: Abhay
// Roll Number: 2401360004

import java.util.ArrayList;
import java.util.Scanner;

class StudentNotFoundException extends Exception {
    public StudentNotFoundException(String message) {
        super(message);
    }
}

interface RecordActions {
    void addStudent(Student s);

    void deleteStudent(int rollNo) throws StudentNotFoundException;

    void updateStudent(int rollNo, double newMarks) throws StudentNotFoundException;

    void viewStudent(int rollNo) throws StudentNotFoundException;
}

abstract class Person {
    String name;
    String email;

    public Person(String name, String email) {
        this.name = name;
        this.email = email;
    }

    abstract void displayInfo();
}

class Student extends Person {

    int rollNo;
    String course;
    double marks;
    char grade;

    public Student(String name, String email, int rollNo, String course, double marks) {
        super(name, email);
        this.rollNo = rollNo;
        this.course = course;
        this.marks = marks;
        calculateGrade();
    }

    private void calculateGrade() {
        if (marks >= 90)
            grade = 'A';
        else if (marks >= 80)
            grade = 'B';
        else if (marks >= 70)
            grade = 'C';
        else if (marks >= 60)
            grade = 'D';
        else
            grade = 'F';
    }

    public void setMarks(double marks) {
        this.marks = marks;
        calculateGrade();
    }

    @Override
    void displayInfo() {
        System.out.println("\n--- Student Profile ---");
        System.out.println("Name    : " + name);
        System.out.println("Email   : " + email);
        System.out.println("Roll No : " + rollNo);
        System.out.println("Course  : " + course);
        System.out.println("Marks   : " + marks);
        System.out.println("Grade   : " + grade);
    }
}

class LoadingTask implements Runnable {
    @Override
    public void run() {
        try {
            System.out.print("Processing");
            for (int i = 0; i < 5; i++) {
                System.out.print(".");
                Thread.sleep(400);
            }
            System.out.println(" Done!");
        } catch (InterruptedException e) {
            System.out.println("Task Interrupted");
        }
    }
}

class StudentManager implements RecordActions {
    private ArrayList<Student> studentList = new ArrayList<>();

    @Override
    public void addStudent(Student s) {

        for (Student existing : studentList) {
            if (existing.rollNo == s.rollNo) {
                System.out.println("ERROR: Roll Number already exists!");
                return;
            }
        }

        Thread loadingThread = new Thread(new LoadingTask());
        loadingThread.start();

        try {

            loadingThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        studentList.add(s);
        System.out.println("Success: Student added to database.");
    }

    @Override
    public void deleteStudent(int rollNo) throws StudentNotFoundException {
        boolean removed = studentList.removeIf(s -> s.rollNo == rollNo);
        if (!removed) {

            throw new StudentNotFoundException("Student with Roll No " + rollNo + " not found for deletion.");
        }
        System.out.println("Success: Student deleted.");
    }

    @Override
    public void updateStudent(int rollNo, double newMarks) throws StudentNotFoundException {
        for (Student s : studentList) {
            if (s.rollNo == rollNo) {
                s.setMarks(newMarks);
                System.out.println("Success: Marks updated.");
                return;
            }
        }
        throw new StudentNotFoundException("Cannot update. Student ID " + rollNo + " does not exist.");
    }

    @Override
    public void viewStudent(int rollNo) throws StudentNotFoundException {
        for (Student s : studentList) {
            if (s.rollNo == rollNo) {
                s.displayInfo();
                return;
            }
        }
        throw new StudentNotFoundException("Search failed. Roll No " + rollNo + " is not in records.");
    }

    public void viewAll() {
        if (studentList.isEmpty()) {
            System.out.println("No records found.");
        } else {
            for (Student s : studentList)
                s.displayInfo();
        }
    }
}

public class lab_assignment_3 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StudentManager manager = new StudentManager();

        while (true) {
            System.out.println("\n=== Advanced Student System ===");
            System.out.println("1. Add Student");
            System.out.println("2. View Student");
            System.out.println("3. Update Marks");
            System.out.println("4. Delete Student");
            System.out.println("5. View All");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");

            try {

                String input = scanner.nextLine();
                int choice = Integer.parseInt(input);

                switch (choice) {
                    case 1:
                        System.out.print("Enter Name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter Email: ");
                        String email = scanner.nextLine();

                        System.out.print("Enter Roll No: ");

                        int rollNo = Integer.parseInt(scanner.nextLine());

                        System.out.print("Enter Course: ");
                        String course = scanner.nextLine();

                        System.out.print("Enter Marks: ");

                        double marks = Double.parseDouble(scanner.nextLine());

                        Student newStudent = new Student(name, email, rollNo, course, marks);
                        manager.addStudent(newStudent);
                        break;

                    case 2:
                        System.out.print("Enter Roll No to view: ");
                        int viewId = Integer.parseInt(scanner.nextLine());
                        manager.viewStudent(viewId);
                        break;

                    case 3:
                        System.out.print("Enter Roll No to update: ");
                        int updateId = Integer.parseInt(scanner.nextLine());
                        System.out.print("Enter new Marks: ");
                        double newM = Double.parseDouble(scanner.nextLine());
                        manager.updateStudent(updateId, newM);
                        break;

                    case 4:
                        System.out.print("Enter Roll No to delete: ");
                        int delId = Integer.parseInt(scanner.nextLine());
                        manager.deleteStudent(delId);
                        break;

                    case 5:
                        manager.viewAll();
                        break;

                    case 6:
                        System.out.println("Exiting system...");
                        scanner.close();
                        return;

                    default:
                        System.out.println("Invalid option! Please enter 1-6.");
                }

            } catch (NumberFormatException e) {

                System.out.println("Input Error: Please enter valid numbers. " + e.getMessage());
            } catch (StudentNotFoundException e) {

                System.out.println("Database Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
        }
    }
}
