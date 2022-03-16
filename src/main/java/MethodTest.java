import controllers.UserController;
import io.javalin.Javalin;

import static io.javalin.apibuilder.ApiBuilder.*;

public class MethodTest {
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(9000);

        UserController userController = new UserController();

        //User Login
        app.post("/login", userController::login);

        //Employee Methods
        app.get("/reimbs", userController::pastReimbs);

        //Finance Manager Methods
        app.routes(() -> {
            path("{username}/all", () -> {
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
