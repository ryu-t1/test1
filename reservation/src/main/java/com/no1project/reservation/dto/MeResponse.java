package com.no1project.reservation.dto;

public class MeResponse {
    private int userId;
    private String name;
    private String role;
    private String email;

    private int grade;
    private String myClass;
    private int number;

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getGrade() { return grade; }
    public void setGrade(int grade) { this.grade = grade; }

    public String getMyClass() { return myClass; }
    public void setMyClass(String myClass) { this.myClass = myClass; }

    public int getNumber() { return number; }
    public void setNumber(int number) { this.number = number; }
}
