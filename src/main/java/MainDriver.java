import controllers.UserController;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

import static io.javalin.apibuilder.ApiBuilder.*;

public class MethodTest {
    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            config.addStaticFiles("/", Location.CLASSPATH);
        }).start(9000);

        UserController userController = new UserController();

        //User Login
        app.post("/login", userController::login); //Default path

        //Employee Methods
        app.get("/reimbs", userController::pastReimbs); //Default dashboard for Employees

        //Finance Manager Methods
        //Login
        app.post("/login/fm", userController::loginFM);

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
    }
}
