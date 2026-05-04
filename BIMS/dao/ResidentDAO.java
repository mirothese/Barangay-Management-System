// dao/ResidentDAO.java
package dao;

import model.Resident;
import java.io.*;
import java.util.*;

public class ResidentDAO {
    private static final String DATA_FILE = "data/residents.dat";
    private List<Resident> residents;
    private Timer autoSaveTimer;
    private boolean dataChanged = false;
    
    // Region dropdown options
    public static final String[] REGIONS = {
        "REGION I (ILOCOS REGION)",
        "REGION II (CAGAYAN VALLEY)",
        "REGION III (CENTRAL LUZON)",
        "REGION IV-A (CALABARZON)",
        "REGION V (BICOL REGION)",
        "REGION VI (WESTERN VISAYAS)",
        "REGION VII (CENTRAL VISAYAS)",
        "REGION VIII (EASTERN VISAYAS)",
        "REGION IX (ZAMBOANGA PENINSULA)",
        "REGION X (NORTHERN MINDANAO)",
        "REGION XI (DAVAO REGION)",
        "REGION XII (SOCCSKSARGEN)",
        "REGION XIII (CARAGA)",
        "CORDILLERA ADMINISTRATIVE REGION (CAR)",
        "MIMAROPA REGION",
        "NEGROS ISLAND REGION (NIR)",
        "BANGSAMORO AUTONOMOUS REGION IN MUSLIM MINDANAO (BARMM)",
        "NATIONAL CAPITAL REGION (NCR)"
    };
    
    // Province mapping
    public static final Map<String, String[]> PROVINCE_MAP = new HashMap<>();
    static {
        PROVINCE_MAP.put("REGION I (ILOCOS REGION)", new String[]{"ILOCOS NORTE"});
        PROVINCE_MAP.put("REGION II (CAGAYAN VALLEY)", new String[]{"CAGAYAN", "ISABELA", "NUEVA VIZCAYA", "QUIRINO"});
        PROVINCE_MAP.put("NATIONAL CAPITAL REGION (NCR)", new String[]{"MANILA", "QUEZON CITY", "MAKATI", "TAGUIG", "PASIG", "MANDALUYONG"});
    }
    
    // City/Municipality mapping
    public static final Map<String, String[]> CITY_MAP = new HashMap<>();
    static {
        CITY_MAP.put("ILOCOS NORTE", new String[]{"CITY OF LAOAG", "BATAC", "BADOC", "CURRIMAO"});
        CITY_MAP.put("MANILA", new String[]{"MANILA"});
        CITY_MAP.put("QUEZON CITY", new String[]{"QUEZON CITY"});
    }
    
    // Barangays for City of Laoag
    public static final String[] BARANGAYS = {
        "BGY. NO. 1, SAN LORENZO", "BGY. NO. 2, SANTA JOAQUINA", "BGY. NO. 3, NRA. SRA. DEL ROSARIO",
        "BGY. NO. 4, SAN GUILLERMO", "BGY. NO. 5, SAN PEDRO", "BGY. NO. 6, SAN AGUSTIN",
        "BGY. NO. 7-A, NRA. SRA. DEL NATIVIDAD", "BGY. NO. 7-B, NRA. SRA. DEL NATIVIDAD",
        "BGY. NO. 8, SAN VICENTE", "BGY. NO. 9, SANTA ANGELA", "BGY. NO. 10, SAN JOSE",
        "BGY. NO. 11, SANTA BALBINA", "BGY. NO. 12, SAN ISIDRO", "BGY. NO. 13, NRA. SRA. DE VISITATION",
        "BGY. NO. 14, SANTO TOMAS", "BGY. NO. 15, SAN GUILLERMO", "BGY. NO. 16, SAN JACINTO",
        "BGY. NO. 17, SAN FRANCISCO", "BGY. NO. 18, SAN QUIRINO", "BGY. NO. 19, SANTA MARCELA",
        "BGY. NO. 20, SAN MIGUEL", "BGY. NO. 21, SAN PEDRO", "BGY. NO. 22, SAN ANDRES",
        "BGY. NO. 23, SAN MATIAS", "BGY. NO. 24, NRA. SRA. DE CONSOLACION", "BGY. NO. 25, SANTA CAYETANA",
        "BGY. NO. 26, SAN MARCELINO", "BGY. NO. 27, NRA. SRA. DE SOLEDAD", "BGY. NO. 28, SAN BERNABE"
    };
    
    public ResidentDAO() {
        residents = new ArrayList<>();
        loadData();
        startAutoSaveTimer();
    }
    
    private void startAutoSaveTimer() {
        autoSaveTimer = new Timer(true);
        autoSaveTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (dataChanged) {
                    saveDataImmediate();
                    dataChanged = false;
                    System.out.println("Auto-save completed at " + new Date());
                }
            }
        }, 10000, 10000);
    }
    
    @SuppressWarnings("unchecked")
    private void loadData() {
        File file = new File(DATA_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                residents = (List<Resident>) ois.readObject();
                System.out.println("Loaded " + residents.size() + " residents from file.");
            } catch (Exception e) {
                System.err.println("Error loading residents: " + e.getMessage());
                residents = new ArrayList<>();
            }
        } else {
            residents = new ArrayList<>();
            saveDataImmediate();
            System.out.println("Created new data file.");
        }
    }
    
    private void saveDataImmediate() {
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
        
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(residents);
            oos.flush();
        } catch (IOException e) {
            System.err.println("Error saving residents: " + e.getMessage());
        }
    }
    
    private void saveData() {
        dataChanged = true;
        saveDataImmediate();
    }
    
    // CRUD Operations
    public void addInhabitant(Resident resident) {
        residents.add(resident);
        saveData();
        System.out.println("Added resident: " + resident.getLastName() + ", " + resident.getFirstName());
    }
    
    public List<Resident> getAllInhabitants() {
        return new ArrayList<>(residents);
    }
    
    public Resident findByFullName(String lastName, String firstName) {
        return residents.stream()
            .filter(r -> r.getLastName() != null && r.getFirstName() != null &&
                        r.getLastName().equalsIgnoreCase(lastName) && 
                        r.getFirstName().equalsIgnoreCase(firstName))
            .findFirst()
            .orElse(null);
    }
    
    public boolean updateInhabitant(Resident updated) {
        for (int i = 0; i < residents.size(); i++) {
            if (residents.get(i).getLastName() != null && 
                residents.get(i).getFirstName() != null &&
                residents.get(i).getLastName().equalsIgnoreCase(updated.getLastName()) &&
                residents.get(i).getFirstName().equalsIgnoreCase(updated.getFirstName())) {
                residents.set(i, updated);
                saveData();
                System.out.println("Updated resident: " + updated.getLastName() + ", " + updated.getFirstName());
                return true;
            }
        }
        return false;
    }
    
    public boolean deleteInhabitant(String lastName, String firstName) {
        boolean removed = residents.removeIf(r -> 
            r.getLastName() != null && r.getFirstName() != null &&
            r.getLastName().equalsIgnoreCase(lastName) && 
            r.getFirstName().equalsIgnoreCase(firstName));
        if (removed) {
            saveData();
            System.out.println("Deleted resident: " + lastName + ", " + firstName);
        }
        return removed;
    }
    
    // Search Methods
    public List<Resident> searchByLastName(String lastName) {
        List<Resident> results = new ArrayList<>();
        for (Resident r : residents) {
            if (r.getLastName() != null && r.getLastName().toLowerCase().contains(lastName.toLowerCase())) {
                results.add(r);
            }
        }
        return results;
    }
    
    public List<Resident> searchByFirstName(String firstName) {
        List<Resident> results = new ArrayList<>();
        for (Resident r : residents) {
            if (r.getFirstName() != null && r.getFirstName().toLowerCase().contains(firstName.toLowerCase())) {
                results.add(r);
            }
        }
        return results;
    }
    
    public List<Resident> searchByAgeRange(int minAge, int maxAge) {
        List<Resident> results = new ArrayList<>();
        for (Resident r : residents) {
            int age = r.getAge();
            if (age >= minAge && age <= maxAge) {
                results.add(r);
            }
        }
        return results;
    }
    
    public List<Resident> searchByVoter(boolean isVoter) {
        List<Resident> results = new ArrayList<>();
        String voterStatus = isVoter ? "YES" : "NO";
        for (Resident r : residents) {
            if (r.getResidentVoter() != null && r.getResidentVoter().equals(voterStatus)) {
                results.add(r);
            }
        }
        return results;
    }
    
    public List<Resident> searchBySectoralGroup(String sectoralGroup) {
        List<Resident> results = new ArrayList<>();
        for (Resident r : residents) {
            if (r.getSectoralGroup() != null && r.getSectoralGroup().equalsIgnoreCase(sectoralGroup)) {
                results.add(r);
            }
        }
        return results;
    }
    
    public List<Resident> searchByKeyword(String keyword) {
        List<Resident> results = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();
        
        for (Resident r : residents) {
            if ((r.getLastName() != null && r.getLastName().toLowerCase().contains(lowerKeyword)) ||
                (r.getFirstName() != null && r.getFirstName().toLowerCase().contains(lowerKeyword)) ||
                (r.getBarangay() != null && r.getBarangay().toLowerCase().contains(lowerKeyword)) ||
                (r.getProfessionOccupation() != null && r.getProfessionOccupation().toLowerCase().contains(lowerKeyword))) {
                results.add(r);
            }
        }
        return results;
    }
    
    // Statistics Methods
    public int getTotalResidents() { 
        return residents.size(); 
    }
    
    public int getMaleCount() {return (int) residents.stream().filter(r -> r.getSex() != null && "MALE".equals(r.getSex())).count();}
    public int getFemaleCount() {return (int) residents.stream().filter(r -> r.getSex() != null && "FEMALE".equals(r.getSex())).count();}
    public int getVoterCount() {return (int) residents.stream().filter(r -> r.getResidentVoter() != null && "YES".equals(r.getResidentVoter()) && r.getAge() >= 18).count();}
    public int getSeniorCitizenCount() {return (int) residents.stream().filter(r -> r.getAge() >= 60).count();}
    public int getYouthCount() {return (int) residents.stream().filter(r -> r.getAge() >= 15 && r.getAge() <= 30).count();}
    public int getChildrenCount() {return (int) residents.stream().filter(r -> r.getAge() <= 14).count();}
    public int getAdultCount() {int total = getTotalResidents();int children = getChildrenCount();int youth = getYouthCount();int seniors = getSeniorCitizenCount();return total - children - youth - seniors;}
    public int getCountBySector(String sector) {return (int) residents.stream().filter(r -> r.getSectoralGroup() != null && sector.equalsIgnoreCase(r.getSectoralGroup())).count();}
}