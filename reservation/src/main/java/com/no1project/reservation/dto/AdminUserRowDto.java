package com.no1project.reservation.dto;

public class AdminUserRowDto {
    private int userId;
    private String name;
    private String email;
    private String role;

    // student/teacher 共通で表示できるように寄せる
    private Integer grade;     // nullあり
    private String myClass;    // nullあり
    private Integer number;    // studentだけ

    // getter/setter
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public Integer getGrade() { return grade; }
    public void setGrade(Integer grade) { this.grade = grade; }
    public String getMyClass() { return myClass; }
    public void setMyClass(String myClass) { this.myClass = myClass; }
    public Integer getNumber() { return number; }
    public void setNumber(Integer number) { this.number = number; }
}
