package repository;

import model.Student;

import java.util.List;

interface StudentRepository {
    void save(Student student);
    Student findById(int id);
    Student findByName(String name);
    Student findByEmail(String email);
    List<Student> findAll();
    void update(Student student);
}
