package controllers;

import io.javalin.http.Context;
import models.JsonResponse;
import models.Reimbursement;
import models.User;
import services.UserService;

import java.util.List;


public class UserController {
    private UserService userService;

    public UserController(){
        this.userService = new UserService();
    }

    public UserController(UserService userService){
        this.userService = userService;
    }

    public void login(Context context){
        JsonResponse jsonResponse;

        User credentials = context.bodyAsClass(User.class);

        User userFromDb = userService.validateCredentials(credentials.getUsername(), credentials.getPassword());

        if (userFromDb == null)
            jsonResponse = new JsonResponse(false, "Invalid Username or Password", null);
        else if (userFromDb.getRole() != 1)
            jsonResponse = new JsonResponse(false, userFromDb.getUsername() + " is a Finance Manager, please sign in through the correct form", null);
        else
            jsonResponse = new JsonResponse(true, "Login successful", userFromDb);

        context.json(jsonResponse);
    }

    public void loginFM(Context context){
        JsonResponse jsonResponse;

        User credentials = context.bodyAsClass(User.class);

        User userFromDb = userService.validateCredentials(credentials.getUsername(), credentials.getPassword());

        if (userFromDb == null)
            jsonResponse = new JsonResponse(false, "Invalid Username or Password", null);
        else if (userFromDb.getRole() != 2)
            jsonResponse = new JsonResponse(false, userFromDb.getUsername() + " is not registered as a Finance Manager", null);
        else
            jsonResponse = new JsonResponse(true, "Login successful", userFromDb);

        context.json(jsonResponse);
    }

    public void getUserById(Context context){
        Integer userId = Integer.parseInt(context.queryParam("userId"));

        User user = userService.getUserById(userId);
        context.json(new JsonResponse(true, "Retrieved User #" + userId, user));
    }

    public void getUserByUsername(Context context){
        String username = context.queryParam("username");

        User user = userService.getUserByUsername(username);
        context.json(new JsonResponse(true, "Retrieved user: " + username, user));
    }

    public void pastReimbs(Context context){
        Integer userId = Integer.parseInt(context.queryParam("userId"));

        List<Reimbursement> reimbs = userService.viewPastReimbs(userId);
        context.json(new JsonResponse(true, "Reimbursements for User: " + userId, reimbs));
    }

    public void allReimbs(Context context){
        String username = context.pathParam("username");

        List<Reimbursement> reimbs = userService.viewAllReimbs(username);
        context.json(new JsonResponse(true, "Showing all reimbursements", reimbs));
    }

    public void reimbsByStatus(Context context){
        String username = context.pathParam("username");
        Integer statusId = Integer.parseInt(context.queryParam("statusId"));

        List<Reimbursement> reimbs = userService.filterReimbsByStatus(username, statusId);
        context.json(new JsonResponse(true, "All reimbursements of Status " + statusId, reimbs));
    }

    public void approveReimb(Context context){
        String username = context.pathParam("username");
        Integer reimbId = Integer.parseInt(context.queryParam("reimbId"));

        String result = userService.approveReimb(username, reimbId);

        if(result == null)
            context.json(new JsonResponse(false, "You cannot approve your own requests", null));
        else
            context.json(new JsonResponse(true, result, null));
    }

    public void denyReimb(Context context){
        String username = context.pathParam("username");
        Integer reimbId = Integer.parseInt(context.queryParam("reimbId"));

        String result = userService.denyReimb(username, reimbId);

        if (result == null)
            context.json(new JsonResponse(false, "You cannot deny your own requests", null));
        else
            context.json(new JsonResponse(true, result, null));
    }

    public void newReimb(Context context){
        String username = context.pathParam("username");
        Reimbursement reimb = context.bodyAsClass(Reimbursement.class);

        userService.addReimb(username, reimb);
        context.json(new JsonResponse(true, "New reimbursement request created", reimb));
    }
}
