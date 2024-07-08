package com.example.police;

import java.util.Date;

public class PoliceData {

    private String subject;
    private String gender;
    private String district;
    private String village;
    private String incidentDate;
    private String dateOfBirth;
    private String address;
    private String imageUri;

    public PoliceData() {}

    // Constructor
    public PoliceData(String subject, String gender, String district, String village, String incidentDate, String dateOfBirth, String address, String imageUri) {
        this.subject = subject;
        this.gender = gender;
        this.district = district;
        this.village = village;
        this.incidentDate = incidentDate;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.imageUri = imageUri;
    }

    // Getters and Setters
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getIncidentDate() {
        return incidentDate;
    }

    public void setIncidentDate(String incidentDate) {
        this.incidentDate = incidentDate;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    @Override
    public String toString() {
        return "Subject: " + subject + "\n\n" +
                "Gender: " + gender + "\n\n" +
                "District: " + district + "\n\n" +
                "Village: " + village + "\n\n" +
                "Incident Date: " + incidentDate + "\n\n" +
                "Date Of Birth: " + dateOfBirth + "\n\n" +
                "Address: " + address + "\n\n" +
                "Image Uri: " + imageUri + "\n\n";
    }
}
