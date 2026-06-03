package service;

import exception.DuplicateStudentException;
import model.Student;

import java.util.List;

interface StudentService {
    void registerStudent(Student student) throws DuplicateStudentException;
    Student getStudentById(int id);
    List<Student> getAllStudents();
    void updateStudent(Student student);
    void deActivateStudent(int id);
    void activateStudent(int id);
}
