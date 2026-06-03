package model;

import lombok.*;

// model for student containing all the fields involved in Student table
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Student {
    private int id;
    private String name;
    private String course;
    private String email;
    private String phone;
    private boolean is_active;

    public Student(String name, String course, String email, String phone) {
        this.name = name;
        this.course = course;
        this.email = email;
        this.phone = phone;
    }
}
