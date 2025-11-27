// ASSIGNMENT 2: Inheritance, Interfaces, and 
//Modular Design 

//Problem Statement: 

//Design and implement a Student Management System using inheritance, polymorphism, and interfaces. The system should consist of an abstract class Person with common fields such as name and 
//email, and a concrete class Student that extends Person with additional fields like rollNo, course, marks, and grade. Implement an interface RecordActions with methods to add, delete, update, 
//and view student records. Use a StudentManager class to handle the operations on student records, ensuring that duplicate roll numbers are prevented. The system should demonstrate method overriding, 
//method overloading, and the use of abstract methods. 

// CODE :-

// Assignment 2: Inheritance, Interfaces, and Modular Design
// Name: Abhay
// Roll Number: 2401360004

import java.util.ArrayList;
import java.util.Scanner;

interface RecordActions {
    void addStudent(Student s);

    void deleteStudent(int rollNo);

    void updateStudent(int rollNo, double newMarks);

    void viewStudent(int rollNo);
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

class StudentManager implements RecordActions {
    private ArrayList<Student> studentList = new ArrayList<>();

    @Override
    public void addStudent(Student s) {

        for (Student existing : studentList) {
            if (existing.rollNo == s.rollNo) {
                System.out.println("ERROR: Roll Number " + s.rollNo + " already exists!");
                return;
            }
        }
        studentList.add(s);
        System.out.println("Success: Student added.");
    }

    @Override
    public void deleteStudent(int rollNo) {

        boolean removed = studentList.removeIf(s -> s.rollNo == rollNo);
        if (removed) {
            System.out.println("Success: Student with Roll No " + rollNo + " deleted.");
        } else {
            System.out.println("Error: Student not found.");
        }
    }

    @Override
    public void updateStudent(int rollNo, double newMarks) {
        for (Student s : studentList) {
            if (s.rollNo == rollNo) {
                s.setMarks(newMarks);
                System.out.println("Success: Marks updated for " + s.name);
                return;
            }
        }
        System.out.println("Error: Student not found.");
    }

    @Override
    public void viewStudent(int rollNo) {
        for (Student s : studentList) {
            if (s.rollNo == rollNo) {
                s.displayInfo();
                return;
            }
        }
        System.out.println("Error: Student not found.");
    }

    public void viewStudent(String name) {
        boolean found = false;
        for (Student s : studentList) {
            if (s.name.equalsIgnoreCase(name)) {
                s.displayInfo();
                found = true;
            }
        }
        if (!found) {
            System.out.println("Error: No student found with name " + name);
        }
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

public class lab_assignment_2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StudentManager manager = new StudentManager();

        while (true) {
            System.out.println("\n=== Student Management System ===");
            System.out.println("1. Add Student");
            System.out.println("2. View Student (by Roll No)");
            System.out.println("3. View Student (by Name) [Overloading Demo]");
            System.out.println("4. Update Marks");
            System.out.println("5. Delete Student");
            System.out.println("6. View All");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Enter Roll No: ");
                    int rollNo = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter Course: ");
                    String course = scanner.nextLine();
                    System.out.print("Enter Marks: ");
                    double marks = scanner.nextDouble();

                    Student newStudent = new Student(name, email, rollNo, course, marks);
                    manager.addStudent(newStudent);
                    break;
                case 2:
                    System.out.print("Enter Roll No to view: ");
                    manager.viewStudent(scanner.nextInt());
                    break;
                case 3:
                    System.out.print("Enter Name to view: ");
                    manager.viewStudent(scanner.nextLine());
                    break;
                case 4:
                    System.out.print("Enter Roll No to update: ");
                    int r = scanner.nextInt();
                    System.out.print("Enter new Marks: ");
                    double m = scanner.nextDouble();
                    manager.updateStudent(r, m);
                    break;
                case 5:
                    System.out.print("Enter Roll No to delete: ");
                    manager.deleteStudent(scanner.nextInt());
                    break;
                case 6:
                    manager.viewAll();
                    break;
                case 7:
                    System.out.println("Exiting system...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }
}
