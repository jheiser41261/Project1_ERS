package controllers;

import io.javalin.http.Context;
import models.*;
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

        context.sessionAttribute("user", userFromDb);

        if (userFromDb == null)
            jsonResponse = new JsonResponse(false, "Invalid Username or Password", null);
        else
            jsonResponse = new JsonResponse(true, "Login successful", userFromDb);

        context.json(jsonResponse);
    }

    public void checkSession(Context context){
        User user = context.sessionAttribute("user");

        if(user == null){
            context.json(new JsonResponse(false, "You are not logged in, redirecting", null));
        } else{
            context.json(new JsonResponse(true, user.getUsername() + " is logged in", user));
        }
    }

    public void logout(Context context){
        context.sessionAttribute("user", null);
        context.json(new JsonResponse(true, "Logged out", null));
    }

    public void pastReimbs(Context context){
        Integer userId = Integer.parseInt(context.queryParam("userId"));

        List<Reimbursement> reimbs = userService.viewPastReimbs(userId);

        for(Reimbursement reimb : reimbs){
            String reimbStatus = reimb.getStatus();
            String reimbType = reimb.getType();

            Status status = null;
            Type type = null;

            for(Status status1 : Status.values()){
                if(status1.getReimbStatus() == Integer.parseInt(reimbStatus))
                    status = status1;
            }

            for(Type type1 : Type.values()){
                if(type1.getReimbType() == Integer.parseInt(reimbType))
                    type = type1;
            }

            reimb.setStatus(status.toString());
            reimb.setType(type.toString());
        }

        context.json(new JsonResponse(true, "Reimbursements for User: " + userId, reimbs));
    }

    public void allReimbs(Context context){
        String username = context.pathParam("username");

        List<Reimbursement> reimbs = userService.viewAllReimbs(username);

        for(Reimbursement reimb : reimbs){
            String reimbStatus = reimb.getStatus();
            String reimbType = reimb.getType();

            Status status = null;
            Type type = null;

            for(Status status1 : Status.values()){
                if(status1.getReimbStatus() == Integer.parseInt(reimbStatus))
                    status = status1;
            }

            for(Type type1 : Type.values()){
                if(type1.getReimbType() == Integer.parseInt(reimbType))
                    type = type1;
            }

            reimb.setStatus(status.toString());
            reimb.setType(type.toString());
        }

        context.json(new JsonResponse(true, "Showing all reimbursements", reimbs));
    }

    public void reimbsByStatus(Context context){
        String username = context.pathParam("username");
        Integer statusId = Integer.parseInt(context.queryParam("statusId"));

        List<Reimbursement> reimbs = userService.filterReimbsByStatus(username, statusId);

        for(Reimbursement reimb : reimbs){
            String reimbStatus = reimb.getStatus();
            String reimbType = reimb.getType();

            Status status = null;
            Type type = null;

            for(Status status1 : Status.values()){
                if(status1.getReimbStatus() == Integer.parseInt(reimbStatus))
                    status = status1;
            }

            for(Type type1 : Type.values()){
                if(type1.getReimbType() == Integer.parseInt(reimbType))
                    type = type1;
            }

            reimb.setStatus(status.toString());
            reimb.setType(type.toString());
        }

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
