import controllers.UserController;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

import static io.javalin.apibuilder.ApiBuilder.*;

public class MainDriver {
    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            config.addStaticFiles("/", Location.CLASSPATH);
        }).start(9000);

        UserController userController = new UserController();

        //User Login
        app.post("/login", userController::login); //Default path

        //Employee Methods
        app.get("/employee/dashboard", userController::pastReimbs); //Default dashboard for Employees

        //Finance Manager Methods

        //Login
        app.post("/fmlogin", userController::loginFM);

        //Dashboard
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

        //Methods for both Roles
        app.post("/{username}/new", userController::newReimb);

        //Utility methods
        app.get("/user", userController::getUserById);
        app.get("/username", userController::getUserByUsername);
    }
}
