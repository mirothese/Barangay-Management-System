package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;

public class Resident implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String lastName;
    private String firstName;
    private String middleName;
    private String extensionName;
    private String fullName;
    
    private String region;
    private String province;
    private String cityMunicipality;
    private String barangay;
    
    private String sex;
    private String civilStatus;
    private LocalDate birthDate;
    private String placeOfBirth;
    private int age;
    
    private String residentVoter;
    private String citizenship;
    private String professionOccupation;
    private String religion;
    private String sectoralGroup;
    
    private String email;
    private String phoneNumber;
    private String residenceAddress;
    
    public Resident() {
        this.citizenship = "FILIPINO";
        this.sectoralGroup = "NONE";
        this.residentVoter = "NO";
        this.extensionName = "N/A";
        this.middleName = "N/A";
    }
    
    public void calculateAge() {
        if (birthDate != null) {
            this.age = Period.between(birthDate, LocalDate.now()).getYears();
        }
    }
    
    public void setFullName() {
        this.fullName = lastName + ", " + firstName;
        if (middleName != null && !middleName.isEmpty() && !middleName.equals("N/A")) {
            this.fullName += " " + middleName;
        }
        if (extensionName != null && !extensionName.isEmpty() && !extensionName.equals("N/A")) {
            this.fullName += " " + extensionName;
        }
    }
    
    // Getters and Setters
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }
    
    public String getExtensionName() { return extensionName; }
    public void setExtensionName(String extensionName) { this.extensionName = extensionName; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    
    public String getProvince() { return province; }
    public void setProvince(String province) { this.province = province; }
    
    public String getCityMunicipality() { return cityMunicipality; }
    public void setCityMunicipality(String cityMunicipality) { this.cityMunicipality = cityMunicipality; }
    
    public String getBarangay() { return barangay; }
    public void setBarangay(String barangay) { this.barangay = barangay; }
    
    public String getSex() { return sex; }
    public void setSex(String sex) { this.sex = sex; }
    
    public String getCivilStatus() { return civilStatus; }
    public void setCivilStatus(String civilStatus) { this.civilStatus = civilStatus; }
    
    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { 
        this.birthDate = birthDate;
        calculateAge();
    }
    
    public String getPlaceOfBirth() { return placeOfBirth; }
    public void setPlaceOfBirth(String placeOfBirth) { this.placeOfBirth = placeOfBirth; }
    
    public int getAge() { return age; }
    
    public String getResidentVoter() { return residentVoter; }
    public void setResidentVoter(String residentVoter) { this.residentVoter = residentVoter; }
    
    public String getCitizenship() { return citizenship; }
    public void setCitizenship(String citizenship) { this.citizenship = citizenship; }
    
    public String getProfessionOccupation() { return professionOccupation; }
    public void setProfessionOccupation(String professionOccupation) { this.professionOccupation = professionOccupation; }
    
    public String getReligion() { return religion; }
    public void setReligion(String religion) { this.religion = religion; }
    
    public String getSectoralGroup() { return sectoralGroup; }
    public void setSectoralGroup(String sectoralGroup) { this.sectoralGroup = sectoralGroup; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public String getResidenceAddress() { return residenceAddress; }
    public void setResidenceAddress(String residenceAddress) { this.residenceAddress = residenceAddress; }
    
    public String getFullAddress() {
        return residenceAddress + ", " + barangay + ", " + cityMunicipality + ", " + province;
    }
}