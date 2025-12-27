package com.no1project.reservation.dto;

public class BatchUpdateStudentGradeRequest {
    private Integer fromGrade; // nullなら全員
    private int toGrade;

    public Integer getFromGrade() { return fromGrade; }
    public void setFromGrade(Integer fromGrade) { this.fromGrade = fromGrade; }
    public int getToGrade() { return toGrade; }
    public void setToGrade(int toGrade) { this.toGrade = toGrade; }
}
