package com.no1project.reservation.dto;

public class ReservationAttendeeDto {
    private int userId;
    private String name;
    private int grade;
    private String myClass;
    private int number;
    private String reservationDate;

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getGrade() { return grade; }
    public void setGrade(int grade) { this.grade = grade; }

    public String getMyClass() { return myClass; }
    public void setMyClass(String myClass) { this.myClass = myClass; }

    public int getNumber() { return number; }
    public void setNumber(int number) { this.number = number; }

    public String getReservationDate() { return reservationDate; }
    public void setReservationDate(String reservationDate) { this.reservationDate = reservationDate; }
}
