package com.no1project.reservation.dto;

public class TeacherRegisterRequest {
    // User 情報
    private String name;
    private String email;
    private String password;

    // Teacher 情報
    private int grade;
    private String myClass;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public int getGrade() { return grade; }
    public void setGrade(int grade) { this.grade = grade; }

    public String getMyClass() { return myClass; }
    public void setMyClass(String myClass) { this.myClass = myClass; }
}
