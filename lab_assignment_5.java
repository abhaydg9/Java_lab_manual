// ASSIGNMENT 5: Capstone Project – Student Record Management System 

//Problem Statement: 

//Design and implement a Student Record Management System using Java that allows for the management of student records (add, update, delete, search, and view) with persistent storage. 
//The application must support exception handling, file handling (to store and retrieve data), multithreading (to simulate loading), and must leverage the Java Collections Framework. The 
//system should allow sorting of students by marks, provide the option to search and delete student records, and display the sorted list of students. Additionally, the system should use OOP concepts 
//like inheritance, abstraction, and interfaces to ensure modular and reusable code. 

// CODE:- 

// Assignment 5: Capstone Project – Student Record Management System
// Name: Abhay
// Roll Number: 2401360004

import java.io.*;
import java.util.*;

// 1. EXCEPTION HANDLING

class StudentNotFoundException extends Exception {
    public StudentNotFoundException(String message) {
        super(message);
    }
}

// 2. ABSTRACTION & INHERITANCE

interface IStudentRecord {
    void addStudent(Student s);

    void updateStudent(int rollNo, double newMarks) throws StudentNotFoundException;

    void deleteStudent(int rollNo) throws StudentNotFoundException;

    Student searchStudent(int rollNo) throws StudentNotFoundException;

    void displayAllSorted();

    void saveRecords();
}

abstract class Person {
    protected String name;
    protected String email;

    public Person(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public abstract String getDetails();
}

class Student extends Person {
    private int rollNo;
    private String course;
    private double marks;
    private char grade;

    public Student(int rollNo, String name, String email, String course, double marks) {
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

    public int getRollNo() {
        return rollNo;
    }

    public double getMarks() {
        return marks;
    }

    public String getName() {
        return name;
    }

    // For File Handling: Convert object to CSV string
    public String toCSV() {
        return rollNo + "," + name + "," + email + "," + course + "," + marks;
    }

    @Override
    public String getDetails() {
        return String.format("Roll: %-5d | Name: %-15s | Course: %-10s | Marks: %-5.1f | Grade: %s",
                rollNo, name, course, marks, grade);
    }
}

// 3. COMPARATOR (COLLECTIONS)

class MarksComparator implements Comparator<Student> {
    @Override
    public int compare(Student s1, Student s2) {
        return Double.compare(s2.getMarks(), s1.getMarks());
    }
}

// 4. MULTITHREADING

class LoadingSimulator implements Runnable {
    private String message;

    public LoadingSimulator(String message) {
        this.message = message;
    }

    @Override
    public void run() {
        System.out.print(message);
        try {
            for (int i = 0; i < 4; i++) {
                System.out.print(".");
                Thread.sleep(300);
            }
            System.out.println(" Done!");
        } catch (InterruptedException e) {
            System.out.println("Process interrupted.");
        }
    }
}

// 5. CORE LOGIC (FILE HANDLING & COLLECTIONS)
class StudentManager implements IStudentRecord {
    private ArrayList<Student> students;
    private final String FILE_NAME = "student_records.txt";

    public StudentManager() {
        students = new ArrayList<>();
        loadRecords();
    }

    private void loadRecords() {
        File file = new File(FILE_NAME);
        if (!file.exists())
            return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 5) {
                    int roll = Integer.parseInt(data[0]);
                    String name = data[1];
                    String email = data[2];
                    String course = data[3];
                    double marks = Double.parseDouble(data[4]);
                    students.add(new Student(roll, name, email, course, marks));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }

    // --- FILE HANDLING: WRITE ---
    @Override
    public void saveRecords() {

        Thread t = new Thread(new LoadingSimulator("Saving Data"));
        t.start();
        try {
            t.join();

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
                for (Student s : students) {
                    bw.write(s.toCSV());
                    bw.newLine();
                }
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    @Override
    public void addStudent(Student s) {
        // Check duplicates
        for (Student existing : students) {
            if (existing.getRollNo() == s.getRollNo()) {
                System.out.println("Error: Roll Number " + s.getRollNo() + " already exists.");
                return;
            }
        }
        students.add(s);
        System.out.println("Student added successfully.");
    }

    @Override
    public void updateStudent(int rollNo, double newMarks) throws StudentNotFoundException {
        Student s = findStudentHelper(rollNo);
        if (s == null)
            throw new StudentNotFoundException("Update Failed: Student not found.");

        s.setMarks(newMarks);
        System.out.println("Marks updated successfully.");
    }

    @Override
    public void deleteStudent(int rollNo) throws StudentNotFoundException {
        Student s = findStudentHelper(rollNo);
        if (s == null)
            throw new StudentNotFoundException("Delete Failed: Student not found.");

        students.remove(s);
        System.out.println("Student record deleted.");
    }

    @Override
    public Student searchStudent(int rollNo) throws StudentNotFoundException {
        Student s = findStudentHelper(rollNo);
        if (s == null)
            throw new StudentNotFoundException("Search Failed: Student not found.");
        return s;
    }

    private Student findStudentHelper(int rollNo) {
        for (Student s : students) {
            if (s.getRollNo() == rollNo)
                return s;
        }
        return null;
    }

    @Override
    public void displayAllSorted() {
        if (students.isEmpty()) {
            System.out.println("No records found.");
            return;
        }

        // Collections Framework: Sorting
        Collections.sort(students, new MarksComparator());

        System.out.println("\n================ STUDENT RECORDS (Sorted by Marks) ================");
        for (Student s : students) {
            System.out.println(s.getDetails());
        }
        System.out.println("===================================================================");
    }
}

// 6. MAIN EXECUTION

public class lab_assignment_5 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StudentManager manager = new StudentManager();

        System.out.println("Welcome to the Student Record Management System (Capstone Project)");

        while (true) {
            System.out.println("\n1. Add Student");
            System.out.println("2. Update Marks");
            System.out.println("3. Delete Student");
            System.out.println("4. Search Student");
            System.out.println("5. View All (Sorted)");
            System.out.println("6. Save & Exit");
            System.out.print("Select Option: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        System.out.print("Enter Roll No: ");
                        int roll = Integer.parseInt(scanner.nextLine());
                        System.out.print("Enter Name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter Email: ");
                        String email = scanner.nextLine();
                        System.out.print("Enter Course: ");
                        String course = scanner.nextLine();
                        System.out.print("Enter Marks: ");
                        double marks = Double.parseDouble(scanner.nextLine());

                        manager.addStudent(new Student(roll, name, email, course, marks));
                        break;

                    case 2:
                        System.out.print("Enter Roll No to Update: ");
                        int uRoll = Integer.parseInt(scanner.nextLine());
                        System.out.print("Enter New Marks: ");
                        double uMarks = Double.parseDouble(scanner.nextLine());
                        manager.updateStudent(uRoll, uMarks);
                        break;

                    case 3:
                        System.out.print("Enter Roll No to Delete: ");
                        int dRoll = Integer.parseInt(scanner.nextLine());
                        manager.deleteStudent(dRoll);
                        break;

                    case 4:
                        System.out.print("Enter Roll No to Search: ");
                        int sRoll = Integer.parseInt(scanner.nextLine());
                        Student s = manager.searchStudent(sRoll);
                        System.out.println("\nRecord Found: " + s.getDetails());
                        break;

                    case 5:
                        manager.displayAllSorted();
                        break;

                    case 6:
                        manager.saveRecords();
                        System.out.println("System Exiting... Goodbye!");
                        scanner.close();
                        System.exit(0);

                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Input Error: Please enter valid numbers.");
            } catch (StudentNotFoundException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Unexpected Error: " + e.getMessage());
            }
        }
    }
}
