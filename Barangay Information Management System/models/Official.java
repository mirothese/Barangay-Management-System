// models/Official.java
package models;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;

public class Official implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String name;
    private String position; // Barangay Captain, Kagawad, Secretary, Treasurer, etc.
    private String sex;
    private LocalDate birthDate;
    private int age;
    private LocalDate termStart;
    private LocalDate termEnd;
    private String committee;
    private String contactNumber;
    private boolean isActive;
    
    public Official() {
        this.isActive = true;
    }
    
    public void calculateAge() {
        if (birthDate != null) {
            this.age = Period.between(birthDate, LocalDate.now()).getYears();
        }
    }
    
    public boolean isCurrentlyInOffice() {
        LocalDate now = LocalDate.now();
        return isActive && termStart != null && termEnd != null &&
               now.isAfter(termStart) && now.isBefore(termEnd);
    }
    
    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    
    public String getSex() { return sex; }
    public void setSex(String sex) { this.sex = sex; }
    
    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        calculateAge();
    }
    
    public int getAge() { return age; }
    
    public LocalDate getTermStart() { return termStart; }
    public void setTermStart(LocalDate termStart) { this.termStart = termStart; }
    
    public LocalDate getTermEnd() { return termEnd; }
    public void setTermEnd(LocalDate termEnd) { this.termEnd = termEnd; }
    
    public String getCommittee() { return committee; }
    public void setCommittee(String committee) { this.committee = committee; }
    
    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    
    @Override
    public String toString() {
        return String.format("%s - %s (%s)", name, position, isCurrentlyInOffice() ? "Active" : "Inactive");
    }
}