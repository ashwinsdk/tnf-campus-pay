package repository;

import exception.StudentNotFoundException;
import model.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentRepositoryImplementation implements StudentRepository {

    private static final Logger logger = LoggerFactory.getLogger(StudentRepositoryImplementation.class);

   // Connection con = DatabaseConfig.getConnection();


    @Override
    public void save(Connection con,Student student) {
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

            Student newStudent = findByName(con,student.getName());
            student.setId(newStudent.getId());
            PreparedStatement sp = con.prepareStatement("insert into campus_payment(id) values(?)");

            sp.setInt(1, newStudent.getId());
            sp.executeUpdate();
            sp.close();
            logger.info("Updated the newly saved student's id with id={} into DB", student.getId());
        }
        catch (SQLException e){
            logger.error("Failed to save student into DB");
            throw new RuntimeException("Failed to save student! ",e);
        }

    }


    @Override
    public Student findById(Connection con,int id) {
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
            logger.error("Failed to find student by id={} into DB",id);
            throw new RuntimeException("Failed to find student with id="+id,e);
        }
    }

    @Override
    public Student findByName(Connection con,String name) {
        logger.info("Finding student by name into DB process started");
        try(PreparedStatement ps = con.prepareStatement("SELECT * from student where name = ?")){
            ps.setString(1,name);

            logger.debug("Finding student by name={} into DB process right now",name);

            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                logger.debug("Found student by name={} into DB process",name);
                return new Student(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("course"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getBoolean("is_active"));
            }
            logger.warn("Student with name={} found in DB",name);
            return null;
        }
        catch (SQLException e) {
            logger.error("Failed to find student by name={} into DB",name);
            throw new RuntimeException("Failed to find student with name="+name,e);
        }
    }

    @Override
    public Student findByEmail(Connection con,String email) {
        logger.info("Finding student by email into DB process started");
        try(PreparedStatement ps = con.prepareStatement("SELECT * from student where email = ?")){
            ps.setString(1,email);

            logger.debug("Finding student by email={} into DB process right now",email);

            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                logger.debug("Found student by email={} into DB process",email);
                return new Student(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("course"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getBoolean("is_active"));
            }
            logger.warn("Student with email={} found in DB",email);
            return null;
        }
        catch (SQLException e) {
            logger.error("Failed to find student by email={} into DB",email);
            throw new RuntimeException("Failed to find student with email="+email,e);
        }
    }

    @Override
    public List<Student> findAll(Connection con) {
        try(Statement st = con.createStatement()){
            logger.info("Finding All student into DB process started");
            ResultSet rs = st.executeQuery("SELECT * from student");
            logger.debug("Finding All student into DB process right now");
            List<Student> studentsList = new ArrayList<>();
            while(rs.next()){
                studentsList.add(new Student(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("course"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getBoolean("is_active")));
            }
            logger.info("Returned all students from DB as a list");
            return studentsList;

        }
        catch (SQLException e) {
            logger.error("Failed to find All student into DB process");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Connection con,Student student) {
        logger.info("Updating student into DB process started");
        try(PreparedStatement ps = con.prepareStatement("UPDATE student SET name = ?, course = ?, email = ?, phone = ?, is_active = ? WHERE id = ? ")){
            ps.setString(1, student.getName());
            ps.setString(2, student.getCourse());
            ps.setString(3, student.getEmail());
            ps.setString(4, student.getPhone());
            ps.setBoolean(5, student.is_active());
            ps.setInt(6, student.getId());
            logger.debug("Updating student into DB process right now");
            int rows =  ps.executeUpdate();

            if(rows == 0){
                throw new StudentNotFoundException("Student whose value we are updating is not found");
            }
            logger.info("Updated student with id={} into DB process",student.getId());

        }
        catch (SQLException e) {
            logger.error("Failed to update student with id={} into DB",student.getId());
            throw new RuntimeException("Failed to update student with id="+student.getId(),e);
        }


    }

    @Override
    public void deactivate(Connection con,int id) {
        logger.info("Deactivating student with id={} into DB process",id);
        try(PreparedStatement ps = con.prepareStatement("UPDATE student is_active = ? WHERE id = ? ")){
            ps.setBoolean(1, false);
            ps.setInt(2, id);
            logger.debug("Deactivating student with id={} into DB process",id);
            int rows =  ps.executeUpdate();
            if(rows == 0){
                throw new StudentNotFoundException("Student whose value we are deactivating is not found");
            }
        }
        catch (SQLException e) {
            logger.error("Failed to deactivate student with id={} into DB",id);
            throw new RuntimeException("Failed to deactivate student with id="+id,e);
        }
    }

    @Override
    public void activate(Connection con,int id) {
        logger.info("Activating student with id={} into DB process",id);
        try(PreparedStatement ps = con.prepareStatement("UPDATE student is_active = ? WHERE id = ? ")){
            ps.setBoolean(1, true);
            ps.setInt(2, id);
            logger.debug("Activating student with id={} into DB process",id);
            int rows =  ps.executeUpdate();
            if(rows == 0){
                throw new StudentNotFoundException("Student whose value we are Activating is not found");
            }
        }
        catch (SQLException e) {
            logger.error("Failed to Activating student with id={} into DB",id);
            throw new RuntimeException("Failed to Activating student with id="+id,e);
        }
    }

}
