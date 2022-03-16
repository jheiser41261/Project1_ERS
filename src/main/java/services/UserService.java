package services;

import models.Reimbursement;
import models.User;
import repositories.UserDAO;
import repositories.UserDAOImpl;

import java.util.List;
import java.util.Objects;

public class UserService {
    private UserDAO userDAO;

    public UserService(UserDAO userDAO){
        this.userDAO = userDAO;
    }

    public UserService(){
        this.userDAO = new UserDAOImpl();
    }

    public List<Reimbursement> viewPastReimbs(Integer userId) {
        return userDAO.viewPastReimbs(userId);
    }

    public List<Reimbursement> viewAllReimbs(String username) {
        User user = this.userDAO.getUser(username);

        if(user.getRole() != 2)
            return null;

        return this.userDAO.viewAllReimbs();
    }

    public List<Reimbursement> filterReimbsByStatus(String username, Integer statusId) {
        User user = this.userDAO.getUser(username);

        if(user.getRole() != 2)
            return null;

        return this.userDAO.filterReimbsByStatus(statusId);
    }

    public void approveReimb(String username, Integer reimbId) {
        User user = this.userDAO.getUser(username);

        if(user.getRole() == 2)
            this.userDAO.approveReimb(reimbId, user.getId());
    }

    public void denyReimb(String username, Integer reimbId) {
        User user = this.userDAO.getUser(username);

        if(user.getRole() == 2)
            this.userDAO.denyReimb(reimbId, user.getId());
    }

    public void addReimb(String username, Reimbursement reimbursement) {
        User user = userDAO.getUser(username);
        this.userDAO.addReimb(user.getId(), reimbursement);
    }

    public User validateCredentials(String username, String password){
        User user = this.userDAO.getUser(username);

        if(user == null)
            return null;

        if(!password.equals(user.getPassword()))
            return null;

        return user;
    }
}
