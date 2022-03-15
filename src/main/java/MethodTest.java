import models.Reimbursement;
import models.User;
import repositories.UserDAO;
import repositories.UserDAOImpl;
import services.UserService;

import java.util.Scanner;

public class MethodTest {
    public static void main(String[] args) {
        UserService userService = new UserService();
        Scanner sc = new Scanner(System.in);

        System.out.print("Username: ");
        String username = sc.nextLine();

        /*System.out.print("Password: ");
        String password = sc.nextLine();

        User user = userService.validateCredentials(username, password);

        if(user != null){
            Reimbursement reimb = new Reimbursement();

            System.out.print("Dollar Amount: ");
            reimb.setAmount(Double.valueOf(sc.nextLine()));

            System.out.print("Description: ");
            reimb.setDescription(sc.nextLine());

            reimb.setAuthor(user.getId());

            System.out.print("Type ID: ");
            reimb.setType(Integer.parseInt(sc.nextLine()));

            userService.addReimb(username, reimb);
        }*/

        System.out.println(userService.viewAllReimbs(username));
        //System.out.println(userService.filterReimbsByStatus(username, Integer.parseInt(id)));

    }
}
