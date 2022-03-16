package repositories;

import models.Reimbursement;
import models.Status;
import models.User;

import java.util.List;

public interface UserDAO {
    //Employee Methods
    List<Reimbursement> viewPastReimbs(Integer userId);

    //Finance Manager Methods
    List<Reimbursement> viewAllReimbs();
    List<Reimbursement> filterReimbsByStatus(Integer statusId);
    void approveReimb(Integer reimbId, Integer userId);
    void denyReimb(Integer reimbId, Integer userId);

    //Methods for both Roles
    void addReimb(Integer userId, Reimbursement reimbursement);

    //Method for validating login credentials
    User getUser(String username);

    //Method for preventing Finance Managers from updating their own submissions
    Reimbursement getReimb(Integer reimbId);
}
