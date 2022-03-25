import controllers.UserController;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import models.Status;

import static io.javalin.apibuilder.ApiBuilder.*;

public class MainDriver {
    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            config.addStaticFiles("/", Location.CLASSPATH);
        }).start(9000);

        UserController userController = new UserController();

        //User Sessions
        app.post("/login", userController::login); //Default path
        app.get("/check-session", userController::checkSession);
        app.delete("/logout", userController::logout);

        //Employee
        app.get("/employee/dashboard", userController::pastReimbs); //Default dashboard for Employees

        //Finance Manager
        app.routes(() -> {
            path("{username}/all", () -> { //Default dashboard for Finance Managers
                get(userController::allReimbs);
                path("filter", () -> {
                    get(userController::reimbsByStatus);
                });
            });

            path("{username}", () -> {
                path("approve", () -> {
                    patch(userController::approveReimb);
                });
                path("deny", () -> {
                    patch(userController::denyReimb);
                });
            });
        });

        //Both Roles
        app.post("/{username}/new", userController::newReimb);
    }
}
