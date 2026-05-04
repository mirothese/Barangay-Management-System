// dao/OfficialDAO.java
package dao;

import models.Official;
import java.io.*;
import java.util.*;

public class OfficialDAO {
    private static final String DATA_FILE = "data/officials.dat";
    private List<Official> officials;
    
    public OfficialDAO() {
        officials = new ArrayList<>();
        loadData();
        
        if (officials.isEmpty()) {
            addSampleData();
            saveData();
        }
    }
    
    private void addSampleData() {
        Official sample1 = new Official();
        sample1.setName("Rodrigo Dela Cruz");
        sample1.setPosition("Barangay Captain");
        sample1.setSex("MALE");
        sample1.setBirthDate(java.time.LocalDate.of(1975, 7, 4));
        sample1.setTermStart(java.time.LocalDate.of(2023, 7, 1));
        sample1.setTermEnd(java.time.LocalDate.of(2026, 6, 30));
        sample1.setCommittee("Executive Committee");
        sample1.setContactNumber("09456789012");
        sample1.setActive(true);
        officials.add(sample1);
        
        Official sample2 = new Official();
        sample2.setName("Maria Clara");
        sample2.setPosition("Barangay Kagawad");
        sample2.setSex("FEMALE");
        sample2.setBirthDate(java.time.LocalDate.of(1980, 2, 28));
        sample2.setTermStart(java.time.LocalDate.of(2023, 7, 1));
        sample2.setTermEnd(java.time.LocalDate.of(2026, 6, 30));
        sample2.setCommittee("Health Committee");
        sample2.setContactNumber("09567890123");
        sample2.setActive(true);
        officials.add(sample2);
        
        Official sample3 = new Official();
        sample3.setName("Juan Luna");
        sample3.setPosition("Barangay Secretary");
        sample3.setSex("MALE");
        sample3.setBirthDate(java.time.LocalDate.of(1988, 9, 15));
        sample3.setTermStart(java.time.LocalDate.of(2023, 7, 1));
        sample3.setTermEnd(java.time.LocalDate.of(2026, 6, 30));
        sample3.setCommittee("Records Management");
        sample3.setContactNumber("09678901234");
        sample3.setActive(true);
        officials.add(sample3);
    }
    
    private void loadData() {
        File file = new File(DATA_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                officials = (List<Official>) ois.readObject();
            } catch (Exception e) {
                System.err.println("Error loading officials: " + e.getMessage());
            }
        }
    }
    
    private void saveData() {
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
        
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(officials);
        } catch (IOException e) {
            System.err.println("Error saving officials: " + e.getMessage());
        }
    }
    
    // CRUD Operations
    public void addOfficial(Official official) {
        officials.add(official);
        saveData();
    }
    
    public List<Official> getAllOfficials() {
        return new ArrayList<>(officials);
    }
    
    public Official findByName(String name) {
        return officials.stream()
            .filter(o -> o.getName().equalsIgnoreCase(name))
            .findFirst()
            .orElse(null);
    }
    
    public boolean updateOfficial(Official updated) {
        for (int i = 0; i < officials.size(); i++) {
            if (officials.get(i).getName().equalsIgnoreCase(updated.getName())) {
                officials.set(i, updated);
                saveData();
                return true;
            }
        }
        return false;
    }
    
    public boolean deleteOfficial(String name) {
        boolean removed = officials.removeIf(o -> o.getName().equalsIgnoreCase(name));
        if (removed) {
            saveData();
        }
        return removed;
    }
    
    public List<Official> getActiveOfficials() {
        List<Official> active = new ArrayList<>();
        for (Official o : officials) {
            if (o.isCurrentlyInOffice()) {
                active.add(o);
            }
        }
        return active;
    }
    
    public int getTotalOfficials() {
        return officials.size();
    }
    
    public int getActiveOfficialsCount() {
        return (int) officials.stream().filter(Official::isCurrentlyInOffice).count();
    }
}