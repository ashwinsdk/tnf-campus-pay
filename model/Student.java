package model;

import lombok.*;

// model for student containing all the fields involved in Student table
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Student {
    private Long id;
    private String name;
    private String course;
    private String email;
    private String phone;
    private boolean is_active;
}
