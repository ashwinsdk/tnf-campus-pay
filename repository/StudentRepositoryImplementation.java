package repository;

import config.DatabaseConfig;
import exception.StudentNotFoundException;
import model.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class StudentRepositoryImplementation implements StudentRepository  {

    private static final Logger logger = LoggerFactory.getLogger(StudentRepositoryImplementation.class);

    Connection con = DatabaseConfig.getConnection();

    public StudentRepositoryImplementation() throws SQLException {
    }

    @Override
    public void save(Student student) {
        logger.info("Saving student into DB process started");
        try(PreparedStatement ps = con.prepareStatement("insert into student(name,course,email,phone) values (?,?,?,?)")){
            ps.setString(1,student.getName());
            ps.setString(2,student.getCourse());
            ps.setString(3,student.getEmail());
            ps.setString(4,student.getPhone());

            logger.debug("Inserting student with name={} amd email={} into DB right now", student.getName(), student.getEmail());

            int rows = ps.executeUpdate();

            if(rows==0){
                logger.warn("No rows inserted");
                throw new RuntimeException("Student not saved! ");
            }
            logger.info("Saved student with name={} and email={} into DB",student.getName(),student.getEmail());
        }
        catch (SQLException e){
            logger.error("Failed to save student into DB",e);
            throw new RuntimeException("Failed to save student! ",e);
        }

    }

    @Override
    public Student findById(int id) {
        logger.info("Finding student by id into DB process started");
        try(PreparedStatement ps = con.prepareStatement("SELECT * from student where id = ?")){
            ps.setInt(1,id);

            logger.debug("Finding student by id={} into DB process right now",id);

            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                logger.debug("Found student by id={} into DB process",id);
                return new Student(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("course"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getBoolean("is_active"));
            }
            logger.warn("Student with id={} found in DB",id);
            return null;
        }
        catch (SQLException e) {
            logger.error("Failed to find student by id={} into DB",id,e);
            throw new RuntimeException("Failed to find student with id="+id,e);
        }
    }

    @Override
    public Student findByName(String name) {
        return null;
    }

    @Override
    public Student findByEmail(String email) {
        return null;
    }

    @Override
    public List<Student> findAll() {
        return List.of();
    }

    @Override
    public void update(Student student) {

    }
}
