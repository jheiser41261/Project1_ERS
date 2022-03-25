package services;

import models.Reimbursement;
import models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import repositories.UserDAO;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private UserService userService;
    private UserDAO userDAO = Mockito.mock(UserDAO.class);

    public UserServiceTest(){
        this.userService = new UserService(userDAO);
    }

    @Test
    void viewPastReimbs() {
        Integer userId = 1;
        List<Reimbursement> expectedOutput = new ArrayList<>();
        Mockito.when(userDAO.viewPastReimbs(userId)).thenReturn(expectedOutput);

        List<Reimbursement> actualOutput = userService.viewPastReimbs(userId);

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void viewAllReimbsWithoutPerms() {
        String testUsername = "user-employee";
        User expectedUser = new User(1, testUsername, "pass123", "firstName", "lastName", 1);
        Mockito.when(userDAO.getUser(testUsername)).thenReturn(expectedUser);

        List<Reimbursement> expectedOutput = null;

        List<Reimbursement> actualOutput = userService.viewAllReimbs(testUsername);

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void viewAllReimbsWithPerms() {
        String testUsername = "user-manager";
        User expectedUser = new User(2, testUsername, "pass123", "firstName", "lastName", 2);
        Mockito.when(userDAO.getUser(testUsername)).thenReturn(expectedUser);

        List<Reimbursement> expectedOutput = new ArrayList<>();

        List<Reimbursement> actualOutput = userService.viewAllReimbs(testUsername);

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void filterReimbsByStatusWithoutPerms() {
        String testUsername = "user-employee";
        User expectedUser = new User(1, testUsername, "pass123", "firstName", "lastName", 1);
        Mockito.when(userDAO.getUser(testUsername)).thenReturn(expectedUser);

        List<Reimbursement> expectedOutput = null;

        List<Reimbursement> actualOutput = userService.filterReimbsByStatus(testUsername, 1);

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void filterReimbsByStatusWithPerms() {
        String testUsername = "user-manager";
        User expectedUser = new User(2, testUsername, "pass123", "firstName", "lastName", 2);
        Mockito.when(userDAO.getUser(testUsername)).thenReturn(expectedUser);

        List<Reimbursement> expectedOutput = new ArrayList<>();

        List<Reimbursement> actualOutput = userService.filterReimbsByStatus(testUsername, 1);

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void approveReimbWithoutPerms() {
        String testUsername = "user-employee";
        User expectedUser = new User(1, testUsername, "pass123", "firstName", "lastName", 1);
        Mockito.when(userDAO.getUser(testUsername)).thenReturn(expectedUser);

        Integer reimbId = 1;
        Reimbursement expectedReimb = new Reimbursement(reimbId, "1");
        Mockito.when(userDAO.getReimb(reimbId)).thenReturn(expectedReimb);

        String expectedResult = null;

        String actualResult = userService.approveReimb(testUsername, reimbId);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    void approveReimbSameAuthor() {
        String testUsername = "user-manager";
        User expectedUser = new User(2, testUsername, "pass123", "firstName", "lastName", 2);
        Mockito.when(userDAO.getUser(testUsername)).thenReturn(expectedUser);

        Integer reimbId = 1;
        Reimbursement expectedReimb = new Reimbursement(reimbId, expectedUser.getId().toString());
        Mockito.when(userDAO.getReimb(reimbId)).thenReturn(expectedReimb);

        String expectedResult = null;

        String actualResult = userService.approveReimb(testUsername, reimbId);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    void approveReimbWithPerms() {
        String testUsername = "user-manager";
        User expectedUser = new User(3, testUsername, "pass123", "firstName", "lastName", 2);
        Mockito.when(userDAO.getUser(testUsername)).thenReturn(expectedUser);

        Integer reimbId = 1;
        Reimbursement expectedReimb = new Reimbursement(reimbId, "2");
        Mockito.when(userDAO.getReimb(reimbId)).thenReturn(expectedReimb);

        String expectedResult = "Reimbursement #" + reimbId + " approved";

        String actualResult = userService.approveReimb(testUsername, reimbId);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    void denyReimbWithoutPerms() {
        String testUsername = "user-employee";
        User expectedUser = new User(1, testUsername, "pass123", "firstName", "lastName", 1);
        Mockito.when(userDAO.getUser(testUsername)).thenReturn(expectedUser);

        Integer reimbId = 1;
        Reimbursement expectedReimb = new Reimbursement(reimbId, "1");
        Mockito.when(userDAO.getReimb(reimbId)).thenReturn(expectedReimb);

        String expectedResult = null;

        String actualResult = userService.denyReimb(testUsername, reimbId);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    void denyReimbSameAuthor() {
        String testUsername = "user-manager";
        User expectedUser = new User(2, testUsername, "pass123", "firstName", "lastName", 2);
        Mockito.when(userDAO.getUser(testUsername)).thenReturn(expectedUser);

        Integer reimbId = 1;
        Reimbursement expectedReimb = new Reimbursement(reimbId, expectedUser.getId().toString());
        Mockito.when(userDAO.getReimb(reimbId)).thenReturn(expectedReimb);

        String expectedResult = null;

        String actualResult = userService.denyReimb(testUsername, reimbId);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    void denyReimbWithPerms() {
        String testUsername = "user-manager";
        User expectedUser = new User(3, testUsername, "pass123", "firstName", "lastName", 2);
        Mockito.when(userDAO.getUser(testUsername)).thenReturn(expectedUser);

        Integer reimbId = 1;
        Reimbursement expectedReimb = new Reimbursement(reimbId, "2");
        Mockito.when(userDAO.getReimb(reimbId)).thenReturn(expectedReimb);

        String expectedResult = "Reimbursement #" + reimbId + " denied";

        String actualResult = userService.denyReimb(testUsername, reimbId);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    void addReimb() {
        String testUsername = "valid-user";
        User expectedUser = new User(1, testUsername, "pass123", "firstName", "lastName", 1);
        Mockito.when(userDAO.getUser(testUsername)).thenReturn(expectedUser);
        Reimbursement reimbToAdd = new Reimbursement(100.00, "Unit Test", "1");

        userService.addReimb(testUsername, reimbToAdd);

        Mockito.verify(userDAO, Mockito.times(1)).addReimb(expectedUser.getId(), reimbToAdd);
    }

    @Test
    void validateCredentialsBadUsername() {
        String expectedUsername = "incorrect";
        String expectedPassword = "correct";
        User expectedOutput = null;
        Mockito.when(userDAO.getUser(expectedUsername)).thenReturn(expectedOutput);

        User actualOutput = userService.validateCredentials(expectedUsername, expectedPassword);

        Assertions.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void validateCredentialsBadPassword() {
        String expectedUsername = "correct";
        String expectedPassword = "incorrect";
        User expectedOutput = null;
        User userFromDb = new User("correct", "correct", "firstName", "lastName");
        Mockito.when(userDAO.getUser(expectedUsername)).thenReturn(userFromDb);

        User actualOutput = userService.validateCredentials(expectedUsername, expectedPassword);

        Assertions.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void validateCredentialsGoodLogin() {
        String expectedUsername = "correct";
        String expectedPassword = "correct";
        User expectedOutput = new User(expectedUsername, expectedPassword, "firstName", "lastName");
        Mockito.when(userDAO.getUser(expectedUsername)).thenReturn(expectedOutput);

        User actualOutput = userService.validateCredentials(expectedUsername, expectedPassword);

        Assertions.assertEquals(expectedOutput, actualOutput);
    }
}