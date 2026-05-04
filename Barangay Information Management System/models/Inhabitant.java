// models/Inhabitant.java
package models;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;

public class Inhabitant implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // Basic Information
    private String name;
    private String sex; // MALE, FEMALE
    private String civilStatus; // SINGLE, MARRIED, WIDOW/ER, SEPARATED
    private LocalDate birthDate;
    private int age;
    
    // Address Information
    private String address;
    private String barangay;
    private String cityMunicipality;
    private String province;
    
    // Contact Information
    private String phoneNumber;
    private String email;
    
    // Additional Information
    private String occupation;
    private boolean isVoter;
    private String religion;
    private String citizenship;
    private String sectoralGroup; // Senior Citizen, PWD, OFW, Solo Parent, None
    
    public Inhabitant() {
        this.citizenship = "Filipino";
        this.sectoralGroup = "None";
        this.isVoter = false;
    }
    
    // Calculate age automatically
    public void calculateAge() {
        if (birthDate != null) {
            this.age = Period.between(birthDate, LocalDate.now()).getYears();
        }
    }
    
    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getSex() { return sex; }
    public void setSex(String sex) { this.sex = sex; }
    
    public String getCivilStatus() { return civilStatus; }
    public void setCivilStatus(String civilStatus) { this.civilStatus = civilStatus; }
    
    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { 
        this.birthDate = birthDate;
        calculateAge();
    }
    
    public int getAge() { return age; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getBarangay() { return barangay; }
    public void setBarangay(String barangay) { this.barangay = barangay; }
    
    public String getCityMunicipality() { return cityMunicipality; }
    public void setCityMunicipality(String cityMunicipality) { this.cityMunicipality = cityMunicipality; }
    
    public String getProvince() { return province; }
    public void setProvince(String province) { this.province = province; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getOccupation() { return occupation; }
    public void setOccupation(String occupation) { this.occupation = occupation; }
    
    public boolean isVoter() { return isVoter; }
    public void setVoter(boolean voter) { isVoter = voter; }
    
    public String getReligion() { return religion; }
    public void setReligion(String religion) { this.religion = religion; }
    
    public String getCitizenship() { return citizenship; }
    public void setCitizenship(String citizenship) { this.citizenship = citizenship; }
    
    public String getSectoralGroup() { return sectoralGroup; }
    public void setSectoralGroup(String sectoralGroup) { this.sectoralGroup = sectoralGroup; }
    
    public String getFullAddress() {
        return String.format("%s, %s, %s, %s", 
            address != null ? address : "", 
            barangay != null ? barangay : "",
            cityMunicipality != null ? cityMunicipality : "",
            province != null ? province : "");
    }
    
    @Override
    public String toString() {
        return String.format("%s (%d years old) - %s", name, age, barangay);
    }
}