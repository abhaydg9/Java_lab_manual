// ASSIGNMENT 4: File Handling and Collections 

//Problem Statement: 

//Implement a Student Record Management System with persistent storage using file handling and Java Collections Framework. The system should read student records from a file (students.txt) at the start 
//of the application and save updated records back to the file upon exit. The records should be managed using collections like ArrayList or HashMap to store student information and should be sorted by marks 
//using Comparator. The system should allow for viewing, sorting, and displaying student data using Iterator. Additionally, implement file attributes using the File class and demonstrate reading records 
//randomly using RandomAccessFile. 

// CODE :- 

// Assignment 4: File Handling and Collections
// Name: Abhay
// Roll Number: 2401360004

import java.io.*;
import java.util.*;

class Student {
    int rollNo;
    String name;
    String course;
    double marks;
    char grade;

    public Student(int rollNo, String name, String course, double marks) {
        this.rollNo = rollNo;
        this.name = name;
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

    public String toFileString() {
        return rollNo + "," + name + "," + course + "," + marks;
    }

    @Override
    public String toString() {
        return "Roll: " + rollNo + " | Name: " + name + " | Marks: " + marks + " | Grade: " + grade;
    }
}

// 2. Comparator for Sorting

// Used to sort students based on Marks (High to Low)
class MarksComparator implements Comparator<Student> {
    @Override
    public int compare(Student s1, Student s2) {
        // Compare marks: If s2 > s1, return positive (for descending order)
        return Double.compare(s2.marks, s1.marks);
    }
}

// 3. The Manager Class (File & Collection Operations)
class StudentManager {
    // COLLECTION: Using ArrayList to store records in memory
    private ArrayList<Student> studentList = new ArrayList<>();
    private final String FILE_NAME = "students.txt";

    public void loadFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("No existing data file found. Starting fresh.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Data format: rollNo,name,course,marks
                String[] data = line.split(",");
                if (data.length == 4) {
                    int roll = Integer.parseInt(data[0]);
                    String name = data[1];
                    String course = data[2];
                    double marks = Double.parseDouble(data[3]);
                    studentList.add(new Student(roll, name, course, marks));
                }
            }
            System.out.println("Data loaded successfully from " + FILE_NAME);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    public void saveToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Student s : studentList) {
                bw.write(s.toFileString());
                bw.newLine();
            }
            System.out.println("Records saved to " + FILE_NAME);
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    public void readRawFileContent() {
        System.out.println("\n--- Reading File using RandomAccessFile ---");
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("File does not exist yet (Save data first).");
            return;
        }

        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            // Move pointer to the beginning
            raf.seek(0);
            String line;
            while ((line = raf.readLine()) != null) {
                System.out.println("Raw Data: " + line);
            }
        } catch (IOException e) {
            System.out.println("Error with RandomAccessFile: " + e.getMessage());
        }
    }

    public void showFileAttributes() {
        File file = new File(FILE_NAME);
        System.out.println("\n--- File Attributes ---");
        if (file.exists()) {
            System.out.println("File Name  : " + file.getName());
            System.out.println("Absolute Path: " + file.getAbsolutePath());
            System.out.println("File Size  : " + file.length() + " bytes");
            System.out.println("Readable   : " + file.canRead());
            System.out.println("Writable   : " + file.canWrite());
        } else {
            System.out.println("File " + FILE_NAME + " does not exist yet.");
        }
    }

    public void addStudent(Student s) {
        studentList.add(s);
        System.out.println("Student added.");
    }

    public void viewSortedByMarks() {
        if (studentList.isEmpty()) {
            System.out.println("No records to display.");
            return;
        }

        Collections.sort(studentList, new MarksComparator());

        System.out.println("\n--- Student List (Sorted by Marks) ---");

        Iterator<Student> iterator = studentList.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }
}
// 4. Main Execution

public class lab_assignment_4 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StudentManager manager = new StudentManager();

        manager.loadFromFile();

        while (true) {
            System.out.println("\n=== Student File System (Assignment 4) ===");
            System.out.println("1. Add Student");
            System.out.println("2. View Sorted Students (by Marks)");
            System.out.println("3. Show File Attributes");
            System.out.println("4. Read File using RandomAccessFile");
            System.out.println("5. Save & Exit");
            System.out.print("Choose option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter Roll No: ");
                    int roll = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter Course: ");
                    String course = scanner.nextLine();
                    System.out.print("Enter Marks: ");
                    double marks = scanner.nextDouble();
                    manager.addStudent(new Student(roll, name, course, marks));
                    break;
                case 2:
                    manager.viewSortedByMarks();
                    break;
                case 3:
                    manager.showFileAttributes();
                    break;
                case 4:
                    manager.readRawFileContent();
                    break;
                case 5:
                    manager.saveToFile();
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}
