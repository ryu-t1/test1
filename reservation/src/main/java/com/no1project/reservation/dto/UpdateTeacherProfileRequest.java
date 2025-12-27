package com.no1project.reservation.dto;

public class UpdateTeacherProfileRequest {
    private int grade;
    private String myClass;

    public int getGrade() { return grade; }
    public void setGrade(int grade) { this.grade = grade; }
    public String getMyClass() { return myClass; }
    public void setMyClass(String myClass) { this.myClass = myClass; }
}
