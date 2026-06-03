package main;

import model.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.StudentRepositoryImplementation;

import java.sql.SQLException;
import java.util.List;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) throws SQLException {
        Student st = new Student(3,"Prakash Ranjan","BTech MEA","prakash@gmail.com","1234567890",true);
        StudentRepositoryImplementation std = new StudentRepositoryImplementation();
        std.update(st);
      //  std.findByEmail("prayas@example.com");
      //  List<Student> studentList = std.findAll();
     //   System.out.println(studentList);
    }
}
