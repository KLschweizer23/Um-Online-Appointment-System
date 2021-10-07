package com.example.umonlineappointmentsystem;

public class AppointmentObject {
    private String id, name, email, purpose, yearCourse, date;

    public AppointmentObject() {
    }

    public AppointmentObject(String id, String name, String email, String purpose, String yearCourse, String date) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.purpose = purpose;
        this.yearCourse = yearCourse;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getYearCourse() {
        return yearCourse;
    }

    public void setYearCourse(String yearCourse) {
        this.yearCourse = yearCourse;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
