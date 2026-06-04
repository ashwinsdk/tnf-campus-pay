package repository;

import model.Student;

import java.sql.Connection;
import java.util.List;

interface StudentRepository {
    void save(Connection con,Student student);
    Student findById(Connection con,int id);
    Student findByName(Connection con,String name);
    Student findByEmail(Connection con,String email);
    List<Student> findAll(Connection con);
    void update(Connection con,Student student);
    void deactivate(Connection con,int id);
    void activate(Connection con,int id);
}
