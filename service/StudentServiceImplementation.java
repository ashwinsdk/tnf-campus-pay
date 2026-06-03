package service;

import exception.DuplicateStudentException;
import exception.StudentNotFoundException;
import model.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.StudentRepositoryImplementation;

import java.sql.SQLException;
import java.util.List;
import java.util.function.Predicate;

public class StudentServiceImplementation implements StudentService{

    StudentRepositoryImplementation staticRepo = new StudentRepositoryImplementation();

    private static final Logger logger = LoggerFactory.getLogger(StudentServiceImplementation.class);


    public StudentServiceImplementation() throws SQLException {
    }

    @Override
    public void registerStudent(Student student) {
        logger.info("Register Service Started!");
        try{
            Predicate<String> isEmpty = s -> s==null || s.isEmpty();
            if(isEmpty.test(student.getName())){
                logger.warn("Student name is empty!");
                throw new IllegalArgumentException("Student name is empty");
            }
            if(isEmpty.test(student.getPhone())){
                logger.warn("Student phone is empty!");
                throw new IllegalArgumentException("Student phone is empty");
            }
            if(isEmpty.test(student.getEmail())){
                logger.warn("Student email is empty!");
                throw new IllegalArgumentException("Student email is empty");
            }
            if(isEmpty.test(student.getCourse())){
                logger.warn("Student course is empty!");
                throw new IllegalArgumentException("Student course is empty");
            }

            if(!student.getEmail().contains("@")){
                logger.warn("Student email is not valid!");
                throw new IllegalArgumentException("Student course email does not contain @");
            }

            if(staticRepo.findByEmail(student.getEmail())!=null){
                logger.warn("Student email already exists!");
                throw new DuplicateStudentException("Student already exists");
            }
            logger.info("Register Service Success!");

            staticRepo.save(student);

        }
        catch(DuplicateStudentException e){
            logger.error(e.getMessage());
            System.out.println(e.getMessage());
        }

    }

    @Override
    public Student getStudentById(int id) {
        logger.info("Get Student by ID Service Started!");
        try{
            if(id<=0){
                logger.warn("Student id is below 0!");
                throw new IllegalArgumentException("Student id should be greater than 0");
            }
            logger.debug("Get Student by ID Service Started for id={} !",id);
            Student student = staticRepo.findById(id);

            if(student==null){
                logger.warn("Student by ID Service failed for id={} !",id);
                throw new StudentNotFoundException("Student not found");
            }
            logger.info("Get Student by ID Service Started for id={} !",id);
            return student;
        }
        catch (StudentNotFoundException e){
            logger.error(e.getMessage());
            System.out.println(e.getMessage());
            return null;
        }

    }

    @Override
    public List<Student> getAllStudents() {
        logger.info("Get All Student Service Started!");
        try{
            List<Student> studentsList = staticRepo.findAll();
            if(studentsList==null){
                logger.warn("Student list is empty!");
                throw new IllegalArgumentException("Student list is empty");
            }
            logger.info("Found the Student list for all students!");
            return studentsList;
        }
        catch(IllegalArgumentException e){
            logger.error(e.getMessage());
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public void updateStudent(Student student) {
        logger.info("Update Student Service Started!");
        try{
            Predicate<String> isEmpty = s -> s==null || s.isEmpty();
            if(isEmpty.test(student.getName())){
                logger.warn("Student name is empty!");
                throw new IllegalArgumentException("Student name is empty");
            }
            if(isEmpty.test(student.getPhone())){
                logger.warn("Student phone is empty!");
                throw new IllegalArgumentException("Student phone is empty");
            }
            if(isEmpty.test(student.getEmail())){
                logger.warn("Student email is empty!");
                throw new IllegalArgumentException("Student email is empty");
            }
            if(isEmpty.test(student.getCourse())){
                logger.warn("Student course is empty!");
                throw new IllegalArgumentException("Student course is empty");
            }

            if(!student.getEmail().contains("@")){
                logger.warn("Student email is not valid!");
                throw new IllegalArgumentException("Student course email does not contain @");
            }

            if(staticRepo.findById(student.getId())==null){
                logger.warn("Student doesn't exists!");
                throw new StudentNotFoundException("Student doesn't exists!");
            }
            logger.info("Update Service Success!");

            staticRepo.update(student);


        }
        catch(StudentNotFoundException e){
            logger.error(e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deActivateStudent(int id) {
        logger.info("DeActivate Student Service Started!");
        try{
            if(id<=0){
                logger.warn("Student id is below 0!");
                throw new IllegalArgumentException("Student id should be greater than 0");
            }
            logger.debug("DeActivate Student Service Started for id={} !",id);
            staticRepo.deactivate(id);
            logger.info("DeActivate Student Service Done for id={} !",id);


        }
        catch(IllegalArgumentException e){
            logger.error(e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void activateStudent(int id) {
        logger.info("Activate Student Service Started!");
        try{
            if(id<=0){
                logger.warn("Student id is below 0!");
                throw new IllegalArgumentException("Student id should be greater than 0");
            }
            logger.debug("Activate Student Service Started for id={} !",id);
            staticRepo.activate(id);
            logger.info("Activate Student Service Done for id={} !",id);



        }
        catch(IllegalArgumentException e){
            logger.error(e.getMessage());
            System.out.println(e.getMessage());
        }
    }
}
