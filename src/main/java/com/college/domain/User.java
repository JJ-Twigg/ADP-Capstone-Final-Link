package com.college.domain;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users") // better to avoid reserved keyword "user"
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-increment PK
    private int userId;

    private String email;
    private String password;
    private String role;
    private LocalDate startDate;

    private String name;
    private String surname;
    private Integer age;
    private String gender;


    @Lob
    @Column(name = "image", columnDefinition = "LONGBLOB")
    private byte[] image;



    //FK TO EMPLOYEE
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Employee employee;

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }



    //FK BRIDGE TABLE
    // Bridge table: User to Role
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserRole> userRoles = new ArrayList<>();






    //  constructor for builder
    private User(UserBuilder builder) {
        this.userId = builder.userId;
        this.email = builder.email;
        this.password = builder.password;
        this.role = builder.role;
        this.startDate = builder.startDate;
        this.name = builder.name;
        this.surname = builder.surname;
        this.age = builder.age;
        this.gender = builder.gender;
    }

    public User() {}

    // Getters & Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", startDate=" + startDate +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                '}';
    }

    // Builder
    public static class UserBuilder {
        private int userId;  // optional (mostly auto-gen by DB)
        private String email;
        private String password;
        private String role;
        private LocalDate startDate;
        private String name;
        private String surname;
        private Integer age;
        private String gender;

        public UserBuilder userId(int userId) {
            this.userId = userId;
            return this;
        }

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder role(String role) {
            this.role = role;
            return this;
        }

        public UserBuilder startDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public UserBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UserBuilder surname(String surname) {
            this.surname = surname;
            return this;
        }

        public UserBuilder age(Integer age) {
            this.age = age;
            return this;
        }

        public UserBuilder gender(String gender) {
            this.gender = gender;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
