// dao/InhabitantDAO.java
package dao;

import models.Inhabitant;
import java.io.*;
import java.util.*;

public class InhabitantDAO {
    private static final String DATA_FILE = "data/inhabitants.dat";
    private List<Inhabitant> inhabitants;
    
    public InhabitantDAO() {
        inhabitants = new ArrayList<>();
        loadData();
        
        // Add sample data if empty
        if (inhabitants.isEmpty()) {
            addSampleData();
            saveData();
        }
    }
    
    private void addSampleData() {
        Inhabitant sample1 = new Inhabitant();
        sample1.setName("Juan Dela Cruz");
        sample1.setSex("MALE");
        sample1.setCivilStatus("MARRIED");
        sample1.setBirthDate(java.time.LocalDate.of(1985, 5, 15));
        sample1.setAddress("123 Rizal St.");
        sample1.setBarangay("San Antonio");
        sample1.setCityMunicipality("Quezon City");
        sample1.setProvince("Metro Manila");
        sample1.setPhoneNumber("09123456789");
        sample1.setEmail("juan@example.com");
        sample1.setOccupation("Teacher");
        sample1.setVoter(true);
        sample1.setReligion("Roman Catholic");
        sample1.setSectoralGroup("None");
        inhabitants.add(sample1);
        
        Inhabitant sample2 = new Inhabitant();
        sample2.setName("Maria Santos");
        sample2.setSex("FEMALE");
        sample2.setCivilStatus("SINGLE");
        sample2.setBirthDate(java.time.LocalDate.of(1990, 8, 20));
        sample2.setAddress("456 Mabini St.");
        sample2.setBarangay("San Antonio");
        sample2.setCityMunicipality("Quezon City");
        sample2.setProvince("Metro Manila");
        sample2.setPhoneNumber("09234567890");
        sample2.setOccupation("Nurse");
        sample2.setVoter(true);
        sample2.setSectoralGroup("OFW");
        inhabitants.add(sample2);
        
        Inhabitant sample3 = new Inhabitant();
        sample3.setName("Pedro Reyes");
        sample3.setSex("MALE");
        sample3.setCivilStatus("WIDOW/ER");
        sample3.setBirthDate(java.time.LocalDate.of(1955, 3, 10));
        sample3.setAddress("789 Bonifacio St.");
        sample3.setBarangay("San Antonio");
        sample3.setCityMunicipality("Quezon City");
        sample3.setProvince("Metro Manila");
        sample3.setPhoneNumber("09345678901");
        sample3.setOccupation("Retired");
        sample3.setVoter(true);
        sample3.setSectoralGroup("Senior Citizen");
        inhabitants.add(sample3);
    }
    
    private void loadData() {
        File file = new File(DATA_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                inhabitants = (List<Inhabitant>) ois.readObject();
            } catch (Exception e) {
                System.err.println("Error loading inhabitants: " + e.getMessage());
            }
        }
    }
    
    private void saveData() {
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
        
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(inhabitants);
        } catch (IOException e) {
            System.err.println("Error saving inhabitants: " + e.getMessage());
        }
    }
    
    // CRUD Operations
    public void addInhabitant(Inhabitant inhabitant) {
        inhabitants.add(inhabitant);
        saveData();
    }
    
    public List<Inhabitant> getAllInhabitants() {
        return new ArrayList<>(inhabitants);
    }
    
    public Inhabitant findByName(String name) {
        return inhabitants.stream()
            .filter(i -> i.getName().equalsIgnoreCase(name))
            .findFirst()
            .orElse(null);
    }
    
    public boolean updateInhabitant(Inhabitant updated) {
        for (int i = 0; i < inhabitants.size(); i++) {
            if (inhabitants.get(i).getName().equalsIgnoreCase(updated.getName())) {
                inhabitants.set(i, updated);
                saveData();
                return true;
            }
        }
        return false;
    }
    
    public boolean deleteInhabitant(String name) {
        boolean removed = inhabitants.removeIf(i -> i.getName().equalsIgnoreCase(name));
        if (removed) {
            saveData();
        }
        return removed;
    }
    
    public List<Inhabitant> searchByKeyword(String keyword) {
        List<Inhabitant> results = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();
        
        for (Inhabitant i : inhabitants) {
            if (i.getName().toLowerCase().contains(lowerKeyword) ||
                i.getOccupation().toLowerCase().contains(lowerKeyword) ||
                i.getBarangay().toLowerCase().contains(lowerKeyword)) {
                results.add(i);
            }
        }
        return results;
    }
    
    public int getTotalResidents() {
        return inhabitants.size();
    }
    
    public int getMaleCount() {
        return (int) inhabitants.stream().filter(i -> "MALE".equals(i.getSex())).count();
    }
    
    public int getFemaleCount() {
        return (int) inhabitants.stream().filter(i -> "FEMALE".equals(i.getSex())).count();
    }
    
    public int getVoterCount() {
        return (int) inhabitants.stream().filter(Inhabitant::isVoter).count();
    }
    
    public int getSeniorCitizenCount() {
        return (int) inhabitants.stream().filter(i -> i.getAge() >= 60).count();
    }
    
    public int getCountBySector(String sector) {
        return (int) inhabitants.stream()
            .filter(i -> sector.equals(i.getSectoralGroup()))
            .count();
    }
}