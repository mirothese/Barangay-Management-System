// models/DashboardStats.java
package models;

import java.io.Serializable;

public class DashboardStats implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // Resident statistics
    private int totalResidents;
    private int maleCount;
    private int femaleCount;
    private int voterCount;
    private int seniorCitizenCount;
    private int pwdCount;
    private int ofwCount;
    private int soloParentCount;
    
    // Official statistics
    private int totalOfficials;
    private int activeOfficials;
    
    // Constructor
    public DashboardStats() {
        totalResidents = 0;
        maleCount = 0;
        femaleCount = 0;
        voterCount = 0;
        seniorCitizenCount = 0;
        pwdCount = 0;
        ofwCount = 0;
        soloParentCount = 0;
        totalOfficials = 0;
        activeOfficials = 0;
    }
    
    // Getters and Setters
    public int getTotalResidents() { return totalResidents; }
    public void setTotalResidents(int totalResidents) { this.totalResidents = totalResidents; }
    
    public int getMaleCount() { return maleCount; }
    public void setMaleCount(int maleCount) { this.maleCount = maleCount; }
    
    public int getFemaleCount() { return femaleCount; }
    public void setFemaleCount(int femaleCount) { this.femaleCount = femaleCount; }
    
    public int getVoterCount() { return voterCount; }
    public void setVoterCount(int voterCount) { this.voterCount = voterCount; }
    
    public int getSeniorCitizenCount() { return seniorCitizenCount; }
    public void setSeniorCitizenCount(int seniorCitizenCount) { this.seniorCitizenCount = seniorCitizenCount; }
    
    public int getPwdCount() { return pwdCount; }
    public void setPwdCount(int pwdCount) { this.pwdCount = pwdCount; }
    
    public int getOfwCount() { return ofwCount; }
    public void setOfwCount(int ofwCount) { this.ofwCount = ofwCount; }
    
    public int getSoloParentCount() { return soloParentCount; }
    public void setSoloParentCount(int soloParentCount) { this.soloParentCount = soloParentCount; }
    
    public int getTotalOfficials() { return totalOfficials; }
    public void setTotalOfficials(int totalOfficials) { this.totalOfficials = totalOfficials; }
    
    public int getActiveOfficials() { return activeOfficials; }
    public void setActiveOfficials(int activeOfficials) { this.activeOfficials = activeOfficials; }
    
    // Calculated fields
    public double getMalePercentage() {
        return totalResidents > 0 ? (maleCount * 100.0 / totalResidents) : 0;
    }
    
    public double getFemalePercentage() {
        return totalResidents > 0 ? (femaleCount * 100.0 / totalResidents) : 0;
    }
    
    public double getVoterPercentage() {
        return totalResidents > 0 ? (voterCount * 100.0 / totalResidents) : 0;
    }
}