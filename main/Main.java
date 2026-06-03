package main;

import model.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.StudentRepositoryImplementation;

import java.sql.SQLException;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) throws SQLException {
      //  Student st = new Student("Prakash Ranjan","BTech ECE","prakash@gmail.com","1234567890");
        StudentRepositoryImplementation std = new StudentRepositoryImplementation();

        std.findById(2);
    }
}
