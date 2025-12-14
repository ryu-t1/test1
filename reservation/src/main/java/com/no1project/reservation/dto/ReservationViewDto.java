package com.no1project.reservation.dto;

public class ReservationViewDto {
    private int reservationId;
    private String reservationDate;

    private int eventId;
    private String eventDate;
    private String deadline;
    private String place;
    private String item;
    private String note;
    private String companyName;

    public int getReservationId() { return reservationId; }
    public void setReservationId(int reservationId) { this.reservationId = reservationId; }

    public String getReservationDate() { return reservationDate; }
    public void setReservationDate(String reservationDate) { this.reservationDate = reservationDate; }

    public int getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }

    public String getEventDate() { return eventDate; }
    public void setEventDate(String eventDate) { this.eventDate = eventDate; }

    public String getDeadline() { return deadline; }
    public void setDeadline(String deadline) { this.deadline = deadline; }

    public String getPlace() { return place; }
    public void setPlace(String place) { this.place = place; }

    public String getItem() { return item; }
    public void setItem(String item) { this.item = item; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
}
