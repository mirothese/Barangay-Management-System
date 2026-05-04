// dao/DashboardDAO.java
package dao;

import models.DashboardStats;

public class DashboardDAO {
    private InhabitantDAO inhabitantDAO;
    private OfficialDAO officialDAO;
    
    public DashboardDAO() {
        inhabitantDAO = new InhabitantDAO();
        officialDAO = new OfficialDAO();
    }
    
    public DashboardStats getDashboardStats() {
        DashboardStats stats = new DashboardStats();
        
        // Resident statistics
        stats.setTotalResidents(inhabitantDAO.getTotalResidents());
        stats.setMaleCount(inhabitantDAO.getMaleCount());
        stats.setFemaleCount(inhabitantDAO.getFemaleCount());
        stats.setVoterCount(inhabitantDAO.getVoterCount());
        stats.setSeniorCitizenCount(inhabitantDAO.getSeniorCitizenCount());
        stats.setPwdCount(inhabitantDAO.getCountBySector("PWD"));
        stats.setOfwCount(inhabitantDAO.getCountBySector("OFW"));
        stats.setSoloParentCount(inhabitantDAO.getCountBySector("Solo Parent"));
        
        // Official statistics
        stats.setTotalOfficials(officialDAO.getTotalOfficials());
        stats.setActiveOfficials(officialDAO.getActiveOfficialsCount());
        
        return stats;
    }
}