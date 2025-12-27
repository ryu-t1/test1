package com.no1project.reservation.dto;

public class UpdateStudentProfileRequest {
    private int grade;
    private String myClass;
    private int number;

    public int getGrade() { return grade; }
    public void setGrade(int grade) { this.grade = grade; }
    public String getMyClass() { return myClass; }
    public void setMyClass(String myClass) { this.myClass = myClass; }
    public int getNumber() { return number; }
    public void setNumber(int number) { this.number = number; }
}
