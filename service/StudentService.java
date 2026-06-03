package service;

import exception.DuplicateStudentException;
import model.Student;
import java.sql.Connection;
import java.util.List;

interface StudentService {
    void registerStudent(Connection con,Student student) throws DuplicateStudentException;
    Student getStudentById(Connection con,int id);
    List<Student> getAllStudents(Connection con);
    void updateStudent(Connection con,Student student);
    void deActivateStudent(Connection con,int id);
    void activateStudent(Connection con,int id);
}
