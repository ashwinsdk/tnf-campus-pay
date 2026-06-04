CREATE TABLE student (
                         id int PRIMARY KEY AUTO_INCREMENT,
                         name VARCHAR(100) NOT NULL,
                         course VARCHAR(100) NOT NULL,
                         email VARCHAR(255) NOT NULL UNIQUE,
                         phone VARCHAR(15) NOT NULL UNIQUE,
                         is_active BOOLEAN NOT NULL DEFAULT TRUE,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE campus_payment (
                                id INT PRIMARY KEY,
                                canteen BOOLEAN NOT NULL DEFAULT FALSE,
                                library BOOLEAN NOT NULL DEFAULT FALSE,
                                hackathon BOOLEAN NOT NULL DEFAULT FALSE,
                                workshop BOOLEAN NOT NULL DEFAULT FALSE,
                                hostel BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE student (
                         id int PRIMARY KEY AUTO_INCREMENT,
                         name VARCHAR(100) NOT NULL,
                         course VARCHAR(100) NOT NULL,
                         email VARCHAR(255) NOT NULL UNIQUE,
                         phone VARCHAR(15) NOT NULL UNIQUE,
                         is_active BOOLEAN NOT NULL DEFAULT TRUE,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE wallet (
                                id INT PRIMARY KEY,
                                student_id INT,
                                balance DOUBLE,
);

CREATE TABLE group_expense (
                               group_id INT NOT NULL AUTO_INCREMENT,
                               group_name VARCHAR(100),
                               total_amount DOUBLE,
                               created_by INT,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               PRIMARY KEY (group_id)
);


CREATE TABLE group_members (
                               id INT NOT NULL AUTO_INCREMENT,
                               group_id INT,
                               student_id INT,
                               PRIMARY KEY (id),
                               KEY (group_id)
);

CREATE TABLE expense_split (
                               split_id INT NOT NULL AUTO_INCREMENT,
                               group_id INT,
                               student_id INT,
                               amount_owed DOUBLE,
                               is_paid TINYINT(1) DEFAULT 0,
                               PRIMARY KEY (split_id),
                               KEY (group_id)
);

CREATE TABLE transactions (
                              transaction_id INT AUTO_INCREMENT PRIMARY KEY,
                              sender_id INT NOT NULL,
                              receiver_id INT NOT NULL,
                              amount DOUBLE NOT NULL,
                              transaction_type VARCHAR(50) NOT NULL,
                              transaction_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);